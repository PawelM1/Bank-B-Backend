package com.pab.bankb.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "accountId", referencedColumnName = "accountId")
    private Account account;

    @NotBlank(message = "Sender Number can't be empty")
    private String recipientOrSenderNumber;
    @NotBlank(message = "Recipient address and name can't be empty")
    private String recipientOrSenderAddressAndName;
    @NotBlank(message = "Title can't be empty")
    private String title;

    @NotBlank(message = "Transfer amount can't be empty")
    @Column(precision = 10, scale = 2)
    @Type(type = "big_decimal")
    private BigDecimal transferAmount;
    private Instant transferDate;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Enumerated(EnumType.STRING)
    private TransferType transferType;
    @Enumerated(EnumType.STRING)
    private TransferStatus transferStatus;

    public Transaction() {
    }

    public Transaction(Account account, String recipientOrSenderNumber, String recipientOrSenderAddressAndName, String recipientOrSenderAddress, String title, BigDecimal transferAmount, Instant transferDate, TransactionType transactionType, TransferType transferType, TransferStatus transferStatus) {
        this.account = account;
        this.recipientOrSenderNumber = recipientOrSenderNumber;
        this.recipientOrSenderAddressAndName = recipientOrSenderAddressAndName;
        this.title = title;
        this.transferAmount = transferAmount;
        this.transferDate = transferDate;
        this.transactionType = transactionType;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
    }

    public Transaction(String recipientOrSenderNumber, String recipientOrSenderAddressAndName, String title, BigDecimal transferAmount, TransactionType transactionType, TransferStatus transferStatus) {
        this.recipientOrSenderNumber = recipientOrSenderNumber;
        this.recipientOrSenderAddressAndName = recipientOrSenderAddressAndName;
        this.title = title;
        this.transferAmount = transferAmount;
        this.transactionType = transactionType;
        this.transferStatus = transferStatus;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getRecipientOrSenderNumber() {
        return recipientOrSenderNumber;
    }

    public void setRecipientOrSenderNumber(String recipientOrSenderNumber) {
        this.recipientOrSenderNumber = recipientOrSenderNumber;
    }

    public String getRecipientOrSenderAddressAndName() {
        return recipientOrSenderAddressAndName;
    }

    public void setRecipientOrSenderAddressAndName(String recipientOrSenderAddressAndName) {
        this.recipientOrSenderAddressAndName = recipientOrSenderAddressAndName;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account accountId) {
        this.account = accountId;
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransferType getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferType transferType) {
        this.transferType = transferType;
    }

    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }

    public Instant getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Instant transferDate) {
        this.transferDate = transferDate;
    }
}

