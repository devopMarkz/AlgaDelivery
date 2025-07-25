package com.algaworks.algadelivery.courier.management.domain.model;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

// GETTER
// SETTER (AccessLevel.PRIVATE)
// No ArgsConstructor(AccessLevel.PACKAGE)
// Equals And Hashcode
public class AssignedDelivery {

    private UUID id;
    private OffsetDateTime assignedAt;

    AssignedDelivery() {
    }

    static AssignedDelivery pending(UUID deliveryId) {
        AssignedDelivery delivery = new AssignedDelivery();
        delivery.setId(deliveryId);
        delivery.setAssignedAt(OffsetDateTime.now());
        return delivery;
    }

    public UUID getId() {
        return id;
    }

    public OffsetDateTime getAssignedAt() {
        return assignedAt;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    private void setAssignedAt(OffsetDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AssignedDelivery that = (AssignedDelivery) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
