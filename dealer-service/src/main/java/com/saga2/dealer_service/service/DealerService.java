package com.saga2.dealer_service.service;

import com.saga2.dealer_service.dto.*;
import com.saga2.dealer_service.entity.Dealer;

public interface DealerService{

    DealerResponse createDealer(CreateDealerRequest createDealerRequest);

    DealerResponse getDealerById(String dealerId);

    DealerResponse updateDealerById(String dealerId, UpdateDealerRequest request);

    DealerResponse updateStatus(UpdateDealerStatusRequest status, String deaderID);

    DealerEligibilityResponse checkEligibility(String dealerId);
}
