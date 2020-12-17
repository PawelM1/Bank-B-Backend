package com.pab.bankb.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionForClientDto {
    private Long transferId;
    private String accountNumber;
    private String nameAndAddress;
    private String title;
    private String transferType;
    private BigDecimal transferAmount;
    private Instant transferDate;

    public TransactionForClientDto() {
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getNameAndAddress() {
        return nameAndAddress;
    }

    public void setNameAndAddress(String nameAndAddress) {
        this.nameAndAddress = nameAndAddress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
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
