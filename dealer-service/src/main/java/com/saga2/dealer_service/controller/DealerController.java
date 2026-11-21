package com.saga2.dealer_service.controller;


import com.saga2.dealer_service.dto.*;
import com.saga2.dealer_service.entity.Dealer;
import com.saga2.dealer_service.service.DealerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dealers")
@RequiredArgsConstructor
public class DealerController {

    private final DealerService dealerService;

    @PostMapping
    public ResponseEntity<DealerResponse> createDealer(@RequestBody CreateDealerRequest dealer){
        DealerResponse dealer1 = dealerService.createDealer(dealer);

        return ResponseEntity.status(HttpStatus.CREATED).body(dealer1);
    }

    @GetMapping("/{dealerId}")
    public ResponseEntity<DealerResponse> getDealerById(@PathVariable String dealerId){
        DealerResponse response = dealerService.getDealerById(dealerId);


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{dealerId}")
    public ResponseEntity<DealerResponse> updateDealer(@PathVariable String dealerId, @Valid @RequestBody UpdateDealerRequest request){
        DealerResponse response = dealerService.updateDealerById(dealerId, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{dealerId}/status")
    public ResponseEntity<DealerResponse> updateStatus(@Valid @RequestBody UpdateDealerStatusRequest status, @PathVariable String dealerId){
        DealerResponse response = dealerService.updateStatus(status, dealerId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("{dealerId}/eligibility")
    public ResponseEntity<DealerEligibilityResponse> checkEligibility(@PathVariable String dealerId){
        return ResponseEntity.status(HttpStatus.FOUND).body(dealerService.checkEligibility(dealerId));
    }
}
