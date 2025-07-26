package com.algaworks.algadelivery.delivery.tracking.infraestructure.http.client;

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
