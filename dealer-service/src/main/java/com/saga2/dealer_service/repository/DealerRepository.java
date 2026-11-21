package com.saga2.dealer_service.repository;

import com.saga2.dealer_service.entity.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DealerRepository extends JpaRepository<Dealer, String> {
}
