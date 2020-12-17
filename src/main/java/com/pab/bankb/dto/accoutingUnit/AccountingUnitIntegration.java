package com.pab.bankb.dto.accoutingUnit;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.List;

@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class AccountingUnitIntegration {
    private String BankNo;
    private BigDecimal PaymentSum;
    private List<Payments> Payments;

    public String getBankNo() {
        return BankNo;
    }

    public void setBankNo(String bankNo) {
        BankNo = bankNo;
    }

    public BigDecimal getPaymentSum() {
        return PaymentSum;
    }

    public void setPaymentSum(BigDecimal paymentSum) {
        PaymentSum = paymentSum;
    }

    public List<com.pab.bankb.dto.accoutingUnit.Payments> getPayments() {
        return Payments;
    }

    public void setPayments(List<com.pab.bankb.dto.accoutingUnit.Payments> payments) {
        Payments = payments;
    }

}
