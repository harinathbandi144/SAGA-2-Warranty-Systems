package com.saga.claim_service.service;

import com.saga.claim_service.dto.ClaimResponse;
import com.saga.claim_service.dto.CreateClaimRequest;
import com.saga.claim_service.dto.UpdateClaimRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClaimService {

    ClaimResponse createClaim(CreateClaimRequest request);

    Page<ClaimResponse> getAllClaims(Pageable pageable);

    ClaimResponse getClaimById(Long id);

    ClaimResponse updateClaim(Long id, UpdateClaimRequest request);

    ClaimResponse submitClaim(Long id);
}
