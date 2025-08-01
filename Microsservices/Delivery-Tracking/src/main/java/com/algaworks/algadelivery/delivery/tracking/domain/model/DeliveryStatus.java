package com.algaworks.algadelivery.delivery.tracking.domain.model;

import java.util.Arrays;
import java.util.List;

public enum DeliveryStatus {

    DRAFT,
    WAITING_FOR_COURIER(DRAFT),
    IN_TRANSIT(WAITING_FOR_COURIER),
    DELIVERED(IN_TRANSIT);

    private final List<DeliveryStatus> previousStatus;

    DeliveryStatus(DeliveryStatus... previousStatus) {
        this.previousStatus = Arrays.asList(previousStatus);
    }

    public boolean canNotChangeTo(DeliveryStatus otherStatus) {
        DeliveryStatus currentStatus = this;
        return !otherStatus.previousStatus.contains(currentStatus);
    }

    public boolean canChangeTo(DeliveryStatus otherStatus) {
        return !canNotChangeTo(otherStatus);
    }

}
