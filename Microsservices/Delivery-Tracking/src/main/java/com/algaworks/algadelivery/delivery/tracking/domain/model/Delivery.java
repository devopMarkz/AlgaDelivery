package com.algaworks.algadelivery.delivery.tracking.domain.model;

import com.algaworks.algadelivery.delivery.tracking.domain.exception.DomainException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Table
public class Delivery {

    @Id
    private UUID id;

    private UUID courierId;

    private DeliveryStatus status;

    private OffsetDateTime placedAt;
    private OffsetDateTime assignedAt;
    private OffsetDateTime expectedDeliveryAt;
    private OffsetDateTime fulfilledAt;

    private BigDecimal distanceFee;
    private BigDecimal courierPayout;
    private BigDecimal totalCost;

    private Integer totalItems;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "sender_zip_code")),
            @AttributeOverride(name = "street", column = @Column(name = "sender_street")),
            @AttributeOverride(name = "number", column = @Column(name = "sender_number")),
            @AttributeOverride(name = "complement", column = @Column(name = "sender_complement")),
            @AttributeOverride(name = "name", column = @Column(name = "sender_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "sender_phone"))
    })
    private ContactPoint sender;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "recipient_zip_code")),
            @AttributeOverride(name = "street", column = @Column(name = "recipient_street")),
            @AttributeOverride(name = "number", column = @Column(name = "recipient_number")),
            @AttributeOverride(name = "complement", column = @Column(name = "recipient_complement")),
            @AttributeOverride(name = "name", column = @Column(name = "recipient_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "recipient_phone"))
    })
    private ContactPoint recipient;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "delivery")
    private List<Item> items = new ArrayList<>();

    protected Delivery(){}

    // Aqui, pudemos definir um objeto no estado padrão, totalmente controlável pela própria classe
    // Evita inconsistências durante o desenvolvimento
    // Static Factory
    public static Delivery draft(){
        Delivery delivery = new Delivery();
        delivery.setId(UUID.randomUUID());
        delivery.setStatus(DeliveryStatus.DRAFT);
        delivery.setTotalItems(0);
        delivery.setTotalCost(BigDecimal.ZERO);
        delivery.setCourierPayout(BigDecimal.ZERO);
        delivery.setDistanceFee(BigDecimal.ZERO);
        return delivery;
    }

    // Apenas Delivery (aggregate root) pode modificar a lista de Items
    public UUID addItem(String itemName, int quantity){
        Item item = Item.brandNew(itemName, quantity, this);
        items.add(item);
        calculateTotalItems();
        return item.getId();
    }

    public void removeItem(UUID itemId){
        items.removeIf(item -> item.getId().equals(itemId));
        calculateTotalItems();
    }

    public void removeItems(){
        items.clear();
        calculateTotalItems();
    }

    public void editPreparationDetails(PreparationDetails details) {
        verifyIfCanBeEdited();

        setSender(details.getSender());
        setRecipient(details.getRecipient());
        setDistanceFee(details.getDistanceFee());
        setCourierPayout(details.getCourierPayout());
        setExpectedDeliveryAt(OffsetDateTime.now().plus(details.getExpectedDeliveryTime()));
        setTotalCost(this.getDistanceFee().add(this.getCourierPayout()));
    }

    public void place(){
        verifyIfCanBePlaced();
        this.changeStatusTo(DeliveryStatus.WAITING_FOR_COURIER);
        this.setPlacedAt(OffsetDateTime.now());
    }

    public void pickUp(UUID courierId){
        this.setCourierId(courierId);
        this.changeStatusTo(DeliveryStatus.IN_TRANSIT);
        this.setAssignedAt(OffsetDateTime.now());
    }

    public void markAsDelivered(){
        this.changeStatusTo(DeliveryStatus.DELIVERED);
        this.setFulfilledAt(OffsetDateTime.now());
    }

    public void changeItemQuantity(UUID itemId, int quantity){
        Item item = getItems().stream().filter(i -> i.getId().equals(itemId)).findFirst().orElseThrow();
        item.setQuantity(quantity);
        calculateTotalItems();
    }

    private void verifyIfCanBePlaced(){
        if(!isFilled()){
            throw new DomainException();
        }
        if(!getStatus().equals(DeliveryStatus.DRAFT)){
            throw new DomainException();
        }
    }

    private void verifyIfCanBeEdited(){
        if(!getStatus().equals(DeliveryStatus.DRAFT)){
            throw new DomainException();
        }
    }

    private boolean isFilled(){
        return this.getSender() != null
                && this.getRecipient() != null
                && this.getTotalCost() != null;
    }

    private void changeStatusTo(DeliveryStatus newStatus){
        if(newStatus != null && this.getStatus().canNotChangeTo(newStatus)){
            throw new DomainException("Cannot change status to " + newStatus);
        }
        this.setStatus(newStatus);
    }

    public static class PreparationDetails {
        private ContactPoint sender;
        private ContactPoint recipient;
        private BigDecimal distanceFee;
        private BigDecimal courierPayout;
        private Duration expectedDeliveryTime;

        private PreparationDetails() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private ContactPoint sender;
            private ContactPoint recipient;
            private BigDecimal distanceFee;
            private BigDecimal courierPayout;
            private Duration expectedDeliveryTime;

            public Builder sender(ContactPoint sender) {
                this.sender = sender;
                return this;
            }

            public Builder recipient(ContactPoint recipient) {
                this.recipient = recipient;
                return this;
            }

            public Builder distanceFee(BigDecimal distanceFee) {
                this.distanceFee = distanceFee;
                return this;
            }

            public Builder courierPayout(BigDecimal courierPayout) {
                this.courierPayout = courierPayout;
                return this;
            }

            public Builder expectedDeliveryTime(Duration expectedDeliveryTime) {
                this.expectedDeliveryTime = expectedDeliveryTime;
                return this;
            }

            public PreparationDetails build() {
                PreparationDetails details = new PreparationDetails();
                details.sender = this.sender;
                details.recipient = this.recipient;
                details.distanceFee = this.distanceFee;
                details.courierPayout = this.courierPayout;
                details.expectedDeliveryTime = this.expectedDeliveryTime;
                return details;
            }
        }

        public ContactPoint getSender() {
            return sender;
        }

        public ContactPoint getRecipient() {
            return recipient;
        }

        public BigDecimal getDistanceFee() {
            return distanceFee;
        }

        public BigDecimal getCourierPayout() {
            return courierPayout;
        }

        public Duration getExpectedDeliveryTime() {
            return expectedDeliveryTime;
        }
    }

    // Getters e Setters

    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    private void calculateTotalItems(){
        int totalItems = getItems().stream().mapToInt(Item::getQuantity).sum();
        setTotalItems(totalItems);
    }

    public UUID getId() {
        return id;
    }

    public UUID getCourierId() {
        return courierId;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public OffsetDateTime getPlacedAt() {
        return placedAt;
    }

    public OffsetDateTime getAssignedAt() {
        return assignedAt;
    }

    public OffsetDateTime getExpectedDeliveryAt() {
        return expectedDeliveryAt;
    }

    public OffsetDateTime getFulfilledAt() {
        return fulfilledAt;
    }

    public BigDecimal getDistanceFee() {
        return distanceFee;
    }

    public BigDecimal getCourierPayout() {
        return courierPayout;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public ContactPoint getSender() {
        return sender;
    }

    public ContactPoint getRecipient() {
        return recipient;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    private void setCourierId(UUID courierId) {
        this.courierId = courierId;
    }

    private void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    private void setPlacedAt(OffsetDateTime placedAt) {
        this.placedAt = placedAt;
    }

    private void setAssignedAt(OffsetDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    private void setExpectedDeliveryAt(OffsetDateTime expectedDeliveryAt) {
        this.expectedDeliveryAt = expectedDeliveryAt;
    }

    private void setFulfilledAt(OffsetDateTime fulfilledAt) {
        this.fulfilledAt = fulfilledAt;
    }

    private void setDistanceFee(BigDecimal distanceFee) {
        this.distanceFee = distanceFee;
    }

    private void setCourierPayout(BigDecimal courierPayout) {
        this.courierPayout = courierPayout;
    }

    private void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    private void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    private void setSender(ContactPoint sender) {
        this.sender = sender;
    }

    private void setRecipient(ContactPoint recipient) {
        this.recipient = recipient;
    }

    private void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Delivery delivery = (Delivery) o;
        return Objects.equals(id, delivery.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
