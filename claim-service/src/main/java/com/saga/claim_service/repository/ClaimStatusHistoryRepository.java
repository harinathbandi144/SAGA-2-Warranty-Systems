package com.saga.claim_service.repository;

import com.saga.claim_service.entity.ClaimStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimStatusHistoryRepository extends JpaRepository<ClaimStatusHistory, Long> {

    List<ClaimStatusHistory> findByClaimIdOrderByChangedAtAscIdAsc(Long claimId);
}
