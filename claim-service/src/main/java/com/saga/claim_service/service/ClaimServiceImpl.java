package com.saga.claim_service.service;


import com.saga.claim_service.dto.ClaimResponse;
import com.saga.claim_service.dto.CreateClaimRequest;
import com.saga.claim_service.dto.UpdateClaimRequest;
import com.saga.claim_service.entity.Claim;
import com.saga.claim_service.enums.Status;
import com.saga.claim_service.exception.ClaimNotFoundException;
import com.saga.claim_service.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ClaimServiceImpl implements ClaimService{

    private final ClaimRepository claimRepository;

    @Override
    @Transactional
    public ClaimResponse createClaim(CreateClaimRequest request) {

        Claim claim = new Claim();

        String claimNumber = "CLM" + UUID.randomUUID().toString().toUpperCase();

        claim.setClaimNumber(claimNumber);
        claim.setDealerId(request.dealerId());
        claim.setVin(request.vin());
        claim.setFailureDescription(request.failureDescription());
        claim.setClaimedAmount(request.claimedAmount());
        claim.setStatus(Status.DRAFT);

        Claim savedClaim = claimRepository.save(claim);

        return new ClaimResponse(
                savedClaim.getId(),
                savedClaim.getClaimNumber(),
                savedClaim.getDealerId(),
                savedClaim.getVin(),
                savedClaim.getFailureDescription(),
                savedClaim.getClaimedAmount(),
                savedClaim.getStatus(),
                savedClaim.getCreatedAt(),
                savedClaim.getUpdatedAt());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClaimResponse> getAllClaims(Pageable pageable) {
        Page<Claim> claims = claimRepository.findAll(pageable);
        return claims.map(this::toResponse);
//        claim -> new ClaimResponse(claim.getId(),
//                claim.getClaimNumber(),
//                claim.getDealerId(),
//                claim.getVin(),
//                claim.getFailureDescription(),
//                claim.getClaimedAmount(),
//                claim.getStatus(),
//                claim.getCreatedAt(),
//                claim.getUpdatedAt()

    }

    @Override
    public ClaimResponse getClaimById(Long id) {
        Claim claim = claimRepository.findById(id).orElseThrow(()->
                new ClaimNotFoundException("Claim Not Found with the Id: "+id));
        return toResponse(claim);
    }

    @Override
    public ClaimResponse updateClaim(Long id, UpdateClaimRequest request) {
        Claim claim = claimRepository.findById(id).
                orElseThrow(() ->
                        new ClaimNotFoundException("No claim is found id : "+id)
                );

        if(claim.getStatus() != Status.DRAFT){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Only DRAFT claims can be updated");
        }

        claim.setFailureDescription(request.failureDescription());
        claim.setClaimedAmount(request.claimedAmount());


        Claim updatedClaim = claimRepository.save(claim);
        return toResponse(updatedClaim);
    }

    @Override
    public ClaimResponse submitClaim(Long id) {
        Claim claim = claimRepository.findById(id).orElseThrow(() ->
                new ClaimNotFoundException("Claim is not found with id : "+id));

        if (claim.getStatus() != Status.DRAFT){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only DRAFT claims can be submitted");
        }

        claim.setStatus(Status.SUBMITTED);
        Claim saved = claimRepository.save(claim);
        return toResponse(saved);
    }

    private ClaimResponse toResponse(Claim claim){
        return new ClaimResponse(claim.getId(),
                claim.getClaimNumber(),
                claim.getDealerId(),
                claim.getVin(),
                claim.getFailureDescription(),
                claim.getClaimedAmount(),
                claim.getStatus(),
                claim.getCreatedAt(),
                claim.getUpdatedAt());
    }
}
