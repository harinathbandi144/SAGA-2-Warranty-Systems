package com.saga2.dealer_service.service;

import com.saga2.dealer_service.dto.*;
import com.saga2.dealer_service.entity.Dealer;
import com.saga2.dealer_service.enums.DealerStatus;
import com.saga2.dealer_service.exception.DealerAlreadyExistsException;
import com.saga2.dealer_service.exception.DealerNotFoundException;
import com.saga2.dealer_service.kafka.event.DealerStatusChangedEvent;
import com.saga2.dealer_service.repository.DealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealerServiceImpl implements DealerService{

    private final DealerRepository dealerRepository;
    private final DealerEligibilityCacheService dealerEligibilityCacheService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public DealerResponse createDealer(CreateDealerRequest dealer) {
        if (dealerRepository.existsById(dealer.dealerId())){
            throw new DealerAlreadyExistsException("Dealer already exists: "+ dealer.dealerId());
        }
        Dealer dealer1 = new Dealer();

        dealer1.setDealerId(dealer.dealerId());
        dealer1.setDealerName(dealer.dealerName());
        dealer1.setCity(dealer.city());
        dealer1.setEmail(dealer.email());
        dealer1.setPhone(dealer.phone());
        dealer1.setStatus(DealerStatus.ACTIVE);
        Dealer dealer2 = dealerRepository.save(dealer1);

        return toResponse(dealer2);
    }

    @Override
    @Transactional(readOnly = true)
    public DealerResponse getDealerById(String dealerId) {
        Dealer dealer = dealerRepository.findById(dealerId).orElseThrow(()->
                new DealerNotFoundException("Dealer Not Found with the ID: "+dealerId));

        return toResponse(dealer);

    }

    @Override
    @Transactional
    public DealerResponse updateDealerById(String dealerId, UpdateDealerRequest request) {

        Dealer dealer1 = dealerRepository.findById(dealerId).orElseThrow(()->
                new DealerNotFoundException("Dealer Id Not Found with ID : "+dealerId));

        if (dealer1.getStatus() != DealerStatus.ACTIVE){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Dealer Status is inactive : "+dealer1.getDealerName());
        }
        dealer1.setDealerName(request.dealerName());
        dealer1.setEmail(request.email());
        dealer1.setCity(request.city());
        dealer1.setPhone(request.phone());

        dealerRepository.flush();

        return toResponse(dealer1);
    }

    @Override
    @CacheEvict(
            cacheNames = "dealerEligibility",
            key = "#p1" //String dealerI means second argument
    )
    @Transactional
    public DealerResponse updateStatus(UpdateDealerStatusRequest status, String dealerID) {
        Dealer dealer = dealerRepository.findById(dealerID).orElseThrow(() -> new DealerNotFoundException("Dealer is not found: "+dealerID));

       DealerStatus oldStatus = dealer.getStatus();
       DealerStatus newStatus = status.status();

       if (oldStatus==newStatus){
           return toResponse(dealer);
       }

       dealer.setStatus(newStatus);

       dealerRepository.flush();

        eventPublisher.publishEvent(
                new DealerStatusChangedEvent(
                        UUID.randomUUID().toString(),
                        dealerID,
                        dealer.getEmail(),
                        oldStatus.name(),
                        newStatus.name(),
                        Instant.now()
                )
        );

       return toResponse(dealer);
    }

    @Override
    public DealerEligibilityResponse checkEligibility(String dealerId) {
        return dealerEligibilityCacheService.checkEligibility(dealerId);
    }

    private DealerResponse toResponse(Dealer dealer){
        return new DealerResponse(dealer.getDealerId(),dealer.getDealerName(),
                dealer.getEmail(),dealer.getPhone(),dealer.getCity(),dealer.getStatus(),
                dealer.getCreatedAt(),dealer.getUpdatedAt());
    }
}
