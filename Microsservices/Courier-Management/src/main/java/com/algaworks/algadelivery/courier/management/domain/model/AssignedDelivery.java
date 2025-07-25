package com.algaworks.algadelivery.courier.management.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

// GETTER
// SETTER (AccessLevel.PRIVATE)
// No ArgsConstructor(AccessLevel.PACKAGE)
// Equals And Hashcode
@Entity
public class AssignedDelivery {

    @Id
    private UUID id;

    private OffsetDateTime assignedAt;

    @ManyToOne(optional = false)
    private Courier courier;

    AssignedDelivery() {
    }

    static AssignedDelivery pending(UUID deliveryId, Courier courier) {
        AssignedDelivery delivery = new AssignedDelivery();
        delivery.setId(deliveryId);
        delivery.setCourier(courier);
        return delivery;
    }

    private Courier getCourier() {
        return courier;
    }

    private void setCourier(Courier courier) {
        this.courier = courier;
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
