package com.algaworks.algadelivery.delivery.tracking.api.controller;

import com.algaworks.algadelivery.delivery.tracking.api.model.DeliveryInput;
import com.algaworks.algadelivery.delivery.tracking.domain.model.Delivery;
import com.algaworks.algadelivery.delivery.tracking.domain.service.DeliveryPreparationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/deliveries")
public class DeliveryController {

    private final DeliveryPreparationService deliveryPreparationService;

    public DeliveryController(DeliveryPreparationService deliveryPreparationService) {
        this.deliveryPreparationService = deliveryPreparationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Delivery draft(@RequestBody @Valid DeliveryInput input) {
        return deliveryPreparationService.draft(input);
    }

    @PutMapping("/{deliveryId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Delivery edit(@PathVariable UUID deliveryId, @RequestBody @Valid DeliveryInput input) {
        return deliveryPreparationService.edit(deliveryId, input);
    }


}
