package com.pab.bankb.dto;

import java.math.BigDecimal;

public class DepositWithdrawMoneyRequest {
    private Long accountId;
    private BigDecimal amount;

    public DepositWithdrawMoneyRequest() {
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
