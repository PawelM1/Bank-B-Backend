package com.pab.bankb.dto;

public class ClientDto {
    private Long clientId;
    private String nameAndAddress;
    private String email;
    private String phoneNumber;
    private String accountNumber;

    public ClientDto(Long clientId, String nameAndAddress, String email, String phoneNumber, String accountNumber) {
        this.clientId = clientId;
        this.nameAndAddress = nameAndAddress;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.accountNumber = accountNumber;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getNameAndAddress() {
        return nameAndAddress;
    }

    public void setNameAndAddress(String nameAndAddress) {
        this.nameAndAddress = nameAndAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
