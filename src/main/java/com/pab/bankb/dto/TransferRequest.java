package com.pab.bankb.dto;

import java.math.BigDecimal;

public class TransferRequest {
    private Long accountId;
    private String recipientNumber;
    private String recipientNameAndAddress;
    private String title;
    private BigDecimal transferAmount;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getRecipientNameAndAddress() {
        return recipientNameAndAddress;
    }

    public void setRecipientNameAndAddress(String recipientNameAndAddress) {
        this.recipientNameAndAddress = recipientNameAndAddress;
    }

    public String getRecipientNumber() {
        return recipientNumber;
    }

    public void setRecipientNumber(String recipientNumber) {
        this.recipientNumber = recipientNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }
}
