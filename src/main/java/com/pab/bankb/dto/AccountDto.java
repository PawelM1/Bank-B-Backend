package com.pab.bankb.dto;

import java.math.BigDecimal;

public class AccountDto {
    private Long account_id;
    private String account_number;
    private BigDecimal balance;
    private Long client_id;
    private String name_and_address;
    private String email;
    private String phone_number;

    public AccountDto() {
    }

    public AccountDto(Long account_id, String account_number, BigDecimal balance, Long client_id, String name_and_address, String email, String phone_number) {
        this.account_id = account_id;
        this.account_number = account_number;
        this.balance = balance;
        this.client_id = client_id;
        this.name_and_address = name_and_address;
        this.email = email;
        this.phone_number = phone_number;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getClient_id() {
        return client_id;
    }

    public void setClient_id(Long client_id) {
        this.client_id = client_id;
    }

    public String getName_and_address() {
        return name_and_address;
    }

    public void setName_and_address(String name_and_address) {
        this.name_and_address = name_and_address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
