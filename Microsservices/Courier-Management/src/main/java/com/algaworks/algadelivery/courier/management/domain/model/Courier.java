package com.algaworks.algadelivery.courier.management.domain.model;

import java.time.OffsetDateTime;
import java.util.*;

public class Courier {

    private UUID id;
    private String name;
    private String phone;
    private Integer fullfiledDeliveriesQuantity;
    private Integer pendingDeliveriesQuantity;
    private OffsetDateTime lastFullfiledDeliveryAt;
    private List<AssignedDelivery> pendingDeliveries = new ArrayList<>();

    Courier() {}

    public static Courier brandNew(String name, String phone) {
        Courier courier = new Courier();
        courier.setId(UUID.randomUUID());
        courier.setName(name);
        courier.setPhone(phone);
        courier.setPendingDeliveriesQuantity(0);
        courier.setFullfiledDeliveriesQuantity(0);
        return courier;
    }

    public void assing(UUID deliveryId) {
        this.pendingDeliveries.add(
                AssignedDelivery.pending(deliveryId)
        );
        this.pendingDeliveriesQuantity++;
    }

    public void fulfill(UUID deliveryId) {
        AssignedDelivery assignedDelivery = this.pendingDeliveries.stream().filter(a -> a.getId().equals(deliveryId)).findFirst().orElseThrow();
        this.pendingDeliveries.remove(assignedDelivery);
        this.pendingDeliveriesQuantity--;
        this.fullfiledDeliveriesQuantity++;
        this.lastFullfiledDeliveryAt = OffsetDateTime.now();
    }

    public List<AssignedDelivery> getPendingDeliveries() {
        return Collections.unmodifiableList(pendingDeliveries);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    private void setFullfiledDeliveriesQuantity(Integer fullfiledDeliveriesQuantity) {
        this.fullfiledDeliveriesQuantity = fullfiledDeliveriesQuantity;
    }

    private void setPendingDeliveriesQuantity(Integer pendingDeliveriesQuantity) {
        this.pendingDeliveriesQuantity = pendingDeliveriesQuantity;
    }

    private void setLastFullfiledDeliveryAt(OffsetDateTime lastFullfiledDeliveryAt) {
        this.lastFullfiledDeliveryAt = lastFullfiledDeliveryAt;
    }

    private void setPendingDeliveries(List<AssignedDelivery> pendingDeliveries) {
        this.pendingDeliveries = pendingDeliveries;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getFullfiledDeliveriesQuantity() {
        return fullfiledDeliveriesQuantity;
    }

    public Integer getPendingDeliveriesQuantity() {
        return pendingDeliveriesQuantity;
    }

    public OffsetDateTime getLastFullfiledDeliveryAt() {
        return lastFullfiledDeliveryAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Courier courier = (Courier) o;
        return Objects.equals(id, courier.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
