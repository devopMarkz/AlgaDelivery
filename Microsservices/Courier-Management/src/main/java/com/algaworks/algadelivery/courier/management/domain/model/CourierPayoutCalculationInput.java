package com.algaworks.algadelivery.courier.management.domain.model;

public class CourierPayoutCalculationInput {

    private Double distanceInKm;

    public CourierPayoutCalculationInput(Double distanceInKm) {
        this.distanceInKm = distanceInKm;
    }

    public CourierPayoutCalculationInput() {
    }

    public Double getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(Double distanceInKm) {
        this.distanceInKm = distanceInKm;
    }
}
