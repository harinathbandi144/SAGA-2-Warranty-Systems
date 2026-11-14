package com.saga.claim_service.repository;

import com.saga.claim_service.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepository extends JpaRepository<Claim, Long> {

}
