package com.saga.claim_service.controller;

import com.saga.claim_service.dto.ClaimHistoryResponse;
import com.saga.claim_service.dto.ClaimResponse;
import com.saga.claim_service.dto.CreateClaimRequest;
import com.saga.claim_service.dto.UpdateClaimRequest;
import com.saga.claim_service.service.ClaimService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/claims")
public class ClaimController {

        private final ClaimService claimService;

        @PostMapping
        public ResponseEntity<ClaimResponse> createClaim(@Valid @RequestBody CreateClaimRequest request){
            ClaimResponse response = claimService.createClaim(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @GetMapping
        public ResponseEntity<Page<ClaimResponse>> getAllClaims(
                @PageableDefault(page = 0,
                        size = 10,
                        sort = "createdAt",
                        direction = Sort.Direction.DESC) Pageable pageable){
            Page<ClaimResponse> response = claimService.getAllClaims(pageable);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<ClaimResponse> getClaimById(@PathVariable Long id) {
            ClaimResponse response = claimService.getClaimById(id);

            return ResponseEntity.status(HttpStatus.FOUND).body(response);
        }

        @PutMapping("/{id}")
        public ResponseEntity<ClaimResponse> updateClaim(@PathVariable Long id,
                                                         @Valid @RequestBody UpdateClaimRequest updateClaimRequest){

            ClaimResponse response = claimService.updateClaim(id, updateClaimRequest);

            return ResponseEntity.ok(response);
        }

        @PostMapping("/{id}/submit")
        public ResponseEntity<ClaimResponse> submitClaim(@PathVariable Long id){
            ClaimResponse response = claimService.submitClaim(id);

            return ResponseEntity.ok(response);
        }

        @PostMapping("/{id}/start-review")
        public ResponseEntity<ClaimResponse> startReview(@PathVariable Long id){
            return ResponseEntity.ok(claimService.startReview(id));
        }

        @PostMapping("/{id}/approve")
        public ResponseEntity<ClaimResponse> approveClaim(@PathVariable Long id){
            return ResponseEntity.ok(claimService.approveClaim(id));
        }

        @PostMapping("/{id}/reject")
        public ResponseEntity<ClaimResponse> rejectClaim(@PathVariable Long id){
            return ResponseEntity.ok(claimService.rejectClaim(id));
        }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<ClaimHistoryResponse>> getClaimHistory(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                claimService.getClaimHistory(id)
        );
    }
}
