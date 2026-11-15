package com.saga.claim_service.service;


import com.saga.claim_service.dto.ClaimResponse;
import com.saga.claim_service.dto.CreateClaimRequest;
import com.saga.claim_service.entity.Claim;
import com.saga.claim_service.entity.ClaimStatusHistory;
import com.saga.claim_service.enums.Status;
import com.saga.claim_service.exception.ClaimNotFoundException;
import com.saga.claim_service.repository.ClaimRepository;
import com.saga.claim_service.repository.ClaimStatusHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClaimServiceImplTest {

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private ClaimStatusHistoryRepository claimStatusHistoryRepository;

    @InjectMocks
    private ClaimServiceImpl claimServiceImpl;

    @Test
    void getClaimById_whenClaimDoesNotExist_shouldThrowException(){

        when(claimRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ClaimNotFoundException.class, () -> claimServiceImpl.getClaimById(999L));
    }

    @Test
    void createClaim_whenValidRequest_shouldSaveAndReturnClaim(){

        // ARRANGE: Prepare test data
        //Create request data and configure mock repository
        CreateClaimRequest request = new CreateClaimRequest(
                "DLR1001",
                "1HGCM82633A004352",
                "Engine overheating",
                new BigDecimal("15000.00")
        );

        // Mock repository save() behavior
        //Call the real claimService.createClaim(request)
        when(claimRepository.save(any(Claim.class)))
                .thenAnswer(invocation -> {
                    Claim claim = invocation.getArgument(0);
                    claim.setId(1L);
                    return claim;
                });

        // ACT: Call the real service method
        //Check returned ID, dealer, status and amount
        ClaimResponse response = claimServiceImpl.createClaim(request);

        assertEquals(1L,response.id());
        assertEquals("DLR1001",response.dealerId());
        assertEquals(Status.DRAFT, response.status());
        assertEquals(
                new BigDecimal("15000.00"),
                response.claimedAmount());

        assertTrue(response.claimNumber().startsWith("CLM"));

        // VERIFY: Repository save() was called once
        verify(claimRepository, times(1)).save(any(Claim.class));
    }

    @Test
    void submitClaim_whenDraft_shouldUpdateStatusAndSaveHistory(){

        // ARRANGE: Create an existing DRAFT claim
        Claim claim = new Claim();

        claim.setId(5L);
        claim.setStatus(Status.DRAFT);

        // Pretend the repository found this claim
        when(claimRepository.findById(5L))
                .thenReturn(Optional.of(claim));

        // Pretend saveAndFlush() returns the saved claim
        when(claimRepository.saveAndFlush(any(Claim.class)))
                .thenAnswer(invocation ->
                    invocation.getArgument(0)
                );

        // ACT: Call the real service method
        ClaimResponse response = claimServiceImpl.submitClaim(5L);

        // ASSERT: Check the returned status
        assertEquals(Status.SUBMITTED, claim.getStatus());

        // VERIFY: Check claim was saved
        verify(claimRepository).saveAndFlush(any(Claim.class));

        // CAPTURE: Get the history object passed to save()
        ArgumentCaptor<ClaimStatusHistory> historyCaptor =
                ArgumentCaptor.forClass(ClaimStatusHistory.class);

        verify(claimStatusHistoryRepository)
                .save(historyCaptor.capture());

        // ASSERT: Check the saved history values
        ClaimStatusHistory history = historyCaptor.getValue();

        assertEquals(Status.DRAFT, history.getOldStatus());
        assertEquals(Status.SUBMITTED, history.getNewStatus());
        assertEquals(5L, history.getClaim().getId());
    }
}
