package com.saga2.dealer_service.service;


import com.saga2.dealer_service.dto.DealerEligibilityResponse;
import com.saga2.dealer_service.entity.Dealer;
import com.saga2.dealer_service.enums.DealerStatus;
import com.saga2.dealer_service.exception.DealerNotFoundException;
import com.saga2.dealer_service.repository.DealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DealerEligibilityCacheService {

    private final DealerRepository dealerRepository;

    @Cacheable(cacheNames = "dealerEligibility", key = "#p0")
    @Transactional(readOnly = true)
    public DealerEligibilityResponse checkEligibility(String dealerId){

        System.out.println("CACHE MISS: Fetching dealer from PostgreSQL: " + dealerId);

        Dealer dealer = dealerRepository.findById(dealerId).orElseThrow(()->
                new DealerNotFoundException("Dealer Not FOund with id: "+dealerId));

        boolean eligible = dealer.getStatus() == DealerStatus.ACTIVE;

        return new DealerEligibilityResponse(dealer.getDealerId(), eligible,dealer.getStatus());
    }
}
