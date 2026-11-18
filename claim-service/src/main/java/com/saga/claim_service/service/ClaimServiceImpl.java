package com.saga.claim_service.service;


import com.saga.claim_service.client.dto.WarrantyEligibilityResponse;
import com.saga.claim_service.client.service.VehicleClient;
import com.saga.claim_service.dto.ClaimHistoryResponse;
import com.saga.claim_service.dto.ClaimResponse;
import com.saga.claim_service.dto.CreateClaimRequest;
import com.saga.claim_service.dto.UpdateClaimRequest;
import com.saga.claim_service.entity.Claim;
import com.saga.claim_service.entity.ClaimStatusHistory;
import com.saga.claim_service.enums.Status;
import com.saga.claim_service.exception.ClaimNotFoundException;
import com.saga.claim_service.kafka.event.ClaimSubmittedEvent;
import com.saga.claim_service.kafka.producer.ClaimEventProducer;
import com.saga.claim_service.repository.ClaimRepository;
import com.saga.claim_service.repository.ClaimStatusHistoryRepository;
import com.saga.claim_service.resilanace4j.VehicleEligibilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ClaimServiceImpl implements ClaimService{

    private final ClaimRepository claimRepository;

    private final ClaimStatusHistoryRepository claimStatusHistoryRepository;

    private final VehicleEligibilityService vehicleEligibilityService;

    private final ClaimEventProducer claimEventProducer;

    @Override
    @Transactional
    public ClaimResponse createClaim(CreateClaimRequest request) {

        WarrantyEligibilityResponse eligibilityResponse = vehicleEligibilityService.checkEligibility(request.vin());

        if(!eligibilityResponse.eligible()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, eligibilityResponse.reason());
        }
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
    @Transactional
    public ClaimResponse getClaimById(Long id) {
        Claim claim = claimRepository.findById(id).orElseThrow(()->
                new ClaimNotFoundException("Claim Not Found with the Id: "+id));
        return toResponse(claim);
    }

    @Override
    @Transactional
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
    @Transactional
    public ClaimResponse submitClaim(Long id) {

        ClaimResponse response = changeStatus(id, Status.DRAFT, Status.SUBMITTED);

        claimEventProducer.publish(new ClaimSubmittedEvent(response.id(), response.vin(), response.status().name()));

        return response;
    }

    @Override
    @Transactional
    public ClaimResponse startReview(Long id) {
        return changeStatus(
                id, Status.SUBMITTED, Status.UNDER_REVIEW
        );
    }

    @Override
    @Transactional
    public ClaimResponse approveClaim(Long id) {
        return changeStatus(
                id, Status.UNDER_REVIEW, Status.APPROVED
        );
    }

    @Override
    @Transactional
    public ClaimResponse rejectClaim(Long id) {
        return changeStatus(id, Status.UNDER_REVIEW, Status.REJECTED);
    }

    @Override
    public List<ClaimHistoryResponse> getClaimHistory(Long id) {
        if(!claimRepository.existsById(id)){
            throw  new ClaimNotFoundException("Claim not found with ID: " + id);
        }

        return claimStatusHistoryRepository.findByClaimIdOrderByChangedAtAscIdAsc(id)
                .stream().map(history -> new ClaimHistoryResponse(
                        history.getId(),
                        history.getOldStatus(),
                        history.getNewStatus(),
                        history.getChangedAt())).toList();
    }

    private ClaimResponse changeStatus(Long id,
                                       Status expectedStatus,
                                       Status newStatus){

        Claim claim = claimRepository.findById(id).orElseThrow(() -> new ClaimNotFoundException("Claim not found with ID : "+id));

        if(claim.getStatus() != expectedStatus){
            throw new ResponseStatusException(HttpStatus.CONFLICT,  "Expected status " + expectedStatus
                    + " but current status is " + claim.getStatus()
            );
        }

        Status oldStatus = claim.getStatus();
        claim.setStatus(newStatus);

        ClaimStatusHistory history = new ClaimStatusHistory();

        history.setClaim(claim);
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);

        claimStatusHistoryRepository.save(history);
        Claim updated = claimRepository.saveAndFlush(claim);

        return toResponse(updated);
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
