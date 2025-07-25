package com.algaworks.algadelivery.delivery.tracking.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.Objects;
import java.util.UUID;

@Entity
public class Item {

    @Id
    private UUID id;

    private String name;

    private Integer quantity;

    @ManyToOne(optional = false)
    private Delivery delivery;

    Item(){}

    static Item brandNew(String name, Integer quantity, Delivery delivery) {
        Item item = new Item();
        item.setId(UUID.randomUUID());
        item.setName(name);
        item.setQuantity(quantity);
        item.setDelivery(delivery);
        return item;
    }

    private Delivery getDelivery() {
        return delivery;
    }

    private void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    private void setName(String name) {
        this.name = name;
    }

    void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
