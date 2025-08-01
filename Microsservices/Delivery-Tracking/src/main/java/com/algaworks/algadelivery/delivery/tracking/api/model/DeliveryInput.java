package com.algaworks.algadelivery.delivery.tracking.api.model;

import com.algaworks.algadelivery.delivery.tracking.domain.model.ContactPoint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class DeliveryInput {

    @NotNull
    @Valid
    private ContactPointInput sender;

    @NotNull
    @Valid
    private ContactPointInput recipient;

    @NotEmpty
    @Valid
    @Size(min = 1)
    private List<ItemInput> items;

    public DeliveryInput() {
    }

    public ContactPointInput getSender() {
        return sender;
    }

    public void setSender(ContactPointInput sender) {
        this.sender = sender;
    }

    public ContactPointInput getRecipient() {
        return recipient;
    }

    public void setRecipient(ContactPointInput recipient) {
        this.recipient = recipient;
    }

    public List<ItemInput> getItems() {
        return items;
    }

    public void setItems(List<ItemInput> items) {
        this.items = items;
    }
}
