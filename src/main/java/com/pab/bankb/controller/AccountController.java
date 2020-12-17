package com.pab.bankb.controller;

import com.pab.bankb.dto.AccountDto;
import com.pab.bankb.dto.DepositWithdrawMoneyRequest;
import com.pab.bankb.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/account")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<AccountDto> getAccountByEmail(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccountByClientEmail(email));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Void> depositMoneyToAccount(@RequestBody DepositWithdrawMoneyRequest depositWithdrawMoneyRequest) {
        accountService.depositMoneyToAccount(depositWithdrawMoneyRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdrawMoneyFromAccount(@RequestBody DepositWithdrawMoneyRequest depositWithdrawMoneyRequest) {
        accountService.withdrawMoneyToAccount(depositWithdrawMoneyRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
