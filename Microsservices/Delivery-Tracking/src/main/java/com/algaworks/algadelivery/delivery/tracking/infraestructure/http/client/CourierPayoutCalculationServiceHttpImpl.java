package com.algaworks.algadelivery.delivery.tracking.infraestructure.http.client;

import com.algaworks.algadelivery.delivery.tracking.domain.service.CourierPayoutCalculationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CourierPayoutCalculationServiceHttpImpl implements CourierPayoutCalculationService {

    private final CourierApiClient courierApiClient;

    public CourierPayoutCalculationServiceHttpImpl(CourierApiClient courierApiClient) {
        this.courierApiClient = courierApiClient;
    }

    @Override
    public BigDecimal calculatePayout(Double distanceInKm) {
        var courierPayoutResultModel = courierApiClient.payoutCalculation(
                new CourierPayoutCalculationInput(distanceInKm)
        );
        return courierPayoutResultModel.getPayoutFee();
    }

}
