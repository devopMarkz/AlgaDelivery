package com.algaworks.algadelivery.delivery.tracking.domain.model;

import java.time.Duration;

public class DeliveryEstimate {

    private Duration estimatedTime;
    private Double distanceInKm;

    public DeliveryEstimate(Duration estimatedTime, Double distanceInKm) {
        this.estimatedTime = estimatedTime;
        this.distanceInKm = distanceInKm;
    }

    public Duration getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Duration estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Double getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(Double distanceInKm) {
        this.distanceInKm = distanceInKm;
    }
}
