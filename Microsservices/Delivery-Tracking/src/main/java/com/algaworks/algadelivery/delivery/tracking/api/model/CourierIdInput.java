package com.algaworks.algadelivery.delivery.tracking.api.model;

import java.util.UUID;

public class CourierIdInput {

    private UUID courierId;

    public CourierIdInput() {
    }

    public CourierIdInput(UUID courierId) {
        this.courierId = courierId;
    }

    public UUID getCourierId() {
        return courierId;
    }

    public void setCourierId(UUID courierId) {
        this.courierId = courierId;
    }
}
