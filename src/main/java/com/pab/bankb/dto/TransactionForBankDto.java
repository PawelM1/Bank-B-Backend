package com.pab.bankb.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionForBankDto {
    private Long transferId;
    private String senderNameAndAddress;
    private String senderNumber;
    private String recipientNameAndAddress;
    private String recipientNumber;
    private String title;
    private BigDecimal transferAmount;
    private Instant transferDate;

    public TransactionForBankDto() {
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public String getSenderNameAndAddress() {
        return senderNameAndAddress;
    }

    public void setSenderNameAndAddress(String senderNameAndAddress) {
        this.senderNameAndAddress = senderNameAndAddress;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
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

    public Instant getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Instant transferDate) {
        this.transferDate = transferDate;
    }
}
