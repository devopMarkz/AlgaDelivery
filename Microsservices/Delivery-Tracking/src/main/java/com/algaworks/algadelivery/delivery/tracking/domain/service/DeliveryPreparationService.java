package com.algaworks.algadelivery.delivery.tracking.domain.service;

import com.algaworks.algadelivery.delivery.tracking.api.model.ContactPointInput;
import com.algaworks.algadelivery.delivery.tracking.api.model.DeliveryInput;
import com.algaworks.algadelivery.delivery.tracking.api.model.ItemInput;
import com.algaworks.algadelivery.delivery.tracking.domain.exception.DomainException;
import com.algaworks.algadelivery.delivery.tracking.domain.model.ContactPoint;
import com.algaworks.algadelivery.delivery.tracking.domain.model.Delivery;
import com.algaworks.algadelivery.delivery.tracking.domain.model.DeliveryEstimate;
import com.algaworks.algadelivery.delivery.tracking.domain.repository.DeliveryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

@Service
public class DeliveryPreparationService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryTimeEstimationService deliveryTimeEstimationService;
    private final CourierPayoutCalculationService courierPayoutCalculationService;

    public DeliveryPreparationService(DeliveryRepository deliveryRepository,
                                      @Qualifier(value = "deliveryTimeEstimationServiceFakeImpl") DeliveryTimeEstimationService deliveryTimeEstimationService,
                                      CourierPayoutCalculationService courierPayoutCalculationService) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryTimeEstimationService = deliveryTimeEstimationService;
        this.courierPayoutCalculationService = courierPayoutCalculationService;
    }

    @Transactional
    public Delivery draft(DeliveryInput input){
        Delivery delivery = Delivery.draft();
        handlePreparation(input, delivery);
        return deliveryRepository.saveAndFlush(delivery);
    }

    @Transactional
    public Delivery edit(@PathVariable UUID deliveryId, DeliveryInput input) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(DomainException::new);
        delivery.removeItems();
        handlePreparation(input, delivery);
        return deliveryRepository.saveAndFlush(delivery);
    }

    private void handlePreparation(DeliveryInput input, Delivery delivery) {
        ContactPointInput senderInput = input.getSender();
        ContactPointInput recipientInput = input.getRecipient();

        ContactPoint sender = new ContactPoint(
                senderInput.getZipCode(),
                senderInput.getStreet(),
                senderInput.getNumber(),
                senderInput.getComplement(),
                senderInput.getName(),
                senderInput.getPhone()
        );

        ContactPoint recipient = new ContactPoint(
                recipientInput.getZipCode(),
                recipientInput.getStreet(),
                recipientInput.getNumber(),
                recipientInput.getComplement(),
                recipientInput.getName(),
                recipientInput.getPhone()
        );

        DeliveryEstimate estimate = deliveryTimeEstimationService.estimate(sender, recipient);
        BigDecimal calculatedPayout = courierPayoutCalculationService.calculatePayout(estimate.getDistanceInKm());

        BigDecimal distanceFee = calculateFee(estimate.getDistanceInKm());

        var preparationDetails = Delivery.PreparationDetails.builder()
                .recipient(recipient)
                .sender(sender)
                .expectedDeliveryTime(estimate.getEstimatedTime())
                .courierPayout(calculatedPayout)
                .distanceFee(distanceFee)
                .build();

        delivery.editPreparationDetails(preparationDetails);

        for (ItemInput itemInput : input.getItems()) {
            delivery.addItem(itemInput.getName(), itemInput.getQuantity());
        }
    }

    private BigDecimal calculateFee(Double distanceInKm) {
        return new BigDecimal("3")
                .multiply(new BigDecimal(distanceInKm))
                .setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }
}
