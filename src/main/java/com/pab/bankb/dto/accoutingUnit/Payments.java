package com.pab.bankb.dto.accoutingUnit;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class Payments {
    private String DebitedAccountNumber;
    private String DebitedNameAndAddress;
    private String CreditedAccountNumber;
    private String CreditedNameAndAddress;
    private String Title;
    private BigDecimal Amount;

    public Payments() {
    }

    public Payments(String debitedAccountNumber, String debitedNameAndAddress, String creditedAccountNumber, String creditedNameAndAddress, String title, BigDecimal amount) {
        DebitedAccountNumber = debitedAccountNumber;
        DebitedNameAndAddress = debitedNameAndAddress;
        CreditedAccountNumber = creditedAccountNumber;
        CreditedNameAndAddress = creditedNameAndAddress;
        Title = title;
        Amount = amount;
    }

    public String getDebitedAccountNumber() {
        return DebitedAccountNumber;
    }

    public void setDebitedAccountNumber(String debitedAccountNumber) {
        DebitedAccountNumber = debitedAccountNumber;
    }

    public String getDebitedNameAndAddress() {
        return DebitedNameAndAddress;
    }

    public void setDebitedNameAndAddress(String debitedNameAndAddress) {
        DebitedNameAndAddress = debitedNameAndAddress;
    }

    public String getCreditedAccountNumber() {
        return CreditedAccountNumber;
    }

    public void setCreditedAccountNumber(String creditedAccountNumber) {
        CreditedAccountNumber = creditedAccountNumber;
    }

    public String getCreditedNameAndAddress() {
        return CreditedNameAndAddress;
    }

    public void setCreditedNameAndAddress(String creditedNameAndAddress) {
        CreditedNameAndAddress = creditedNameAndAddress;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public BigDecimal getAmount() {
        return Amount;
    }

    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

}
