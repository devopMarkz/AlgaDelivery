package com.algaworks.algadelivery.courier.management.domain.model;

import java.math.BigDecimal;

public class CourierPayoutResultModel {

    private BigDecimal payoutFee;

    public CourierPayoutResultModel() {
    }

    public CourierPayoutResultModel(BigDecimal payoutFee) {
        this.payoutFee = payoutFee;
    }

    public BigDecimal getPayoutFee() {
        return payoutFee;
    }

    public void setPayoutFee(BigDecimal payoutFee) {
        this.payoutFee = payoutFee;
    }
}
