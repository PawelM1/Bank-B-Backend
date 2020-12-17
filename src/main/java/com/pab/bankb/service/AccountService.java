package com.pab.bankb.service;

import com.pab.bankb.dto.AccountDto;
import com.pab.bankb.dto.DepositWithdrawMoneyRequest;
import com.pab.bankb.model.*;
import com.pab.bankb.repo.AccountRepo;
import com.pab.bankb.repo.ClientRepo;
import com.pab.bankb.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

@Service
public class AccountService {

    private ClientRepo clientRepo;
    private AccountRepo accountRepo;
    private TransactionRepo transactionRepo;
    private String bankNumber;

    public AccountService(ClientRepo clientRepo, AccountRepo accountRepo, TransactionRepo transactionRepo, @Value("${bank.number}") String bankNumber) {
        this.clientRepo = clientRepo;
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
        this.bankNumber = bankNumber;
    }

    public Account CreateAccount(Client client) {
        Account account = new Account();

        String accountNumber;

        do {
            accountNumber = this.generateAccountNumber();
        } while (accountRepo.existsByAccountNumber(accountNumber) != Boolean.FALSE);

        account.setAccountNumber(accountNumber);
        account.setBalance(BigDecimal.ZERO);
        account.setClient(client);

        return accountRepo.save(account);
    }

    private String generateAccountNumber() {
        StringBuilder accountNumber = new StringBuilder();

        accountNumber.append(bankNumber);

        for (int i = 0; i < 16; i++) {
            accountNumber.append((int) (Math.random() * 9));
        }

        String sk = generateAccountSkNumber(accountNumber);

        accountNumber.insert(0, sk);

        return accountNumber.toString();
    }

    private String generateAccountSkNumber(StringBuilder accountNumber) {
        BigInteger bigInteger = new BigInteger(accountNumber + "252100");  //PL code 25 21 00

        String sk = String.valueOf(98 - bigInteger.mod(BigInteger.valueOf(97)).intValue());
        if (sk.length() == 1) sk = "0" + sk;

        return sk;
    }

    @Transactional
    public AccountDto getAccountByClientEmail(String email) {
        Client client = clientRepo.getClientByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found clint for given email"));

        Account account = accountRepo.findByClient_Email(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found account for given client"));

        return this.convertAccountToAccountDto(account, client);

    }

    @Transactional
    public void depositMoneyToAccount(DepositWithdrawMoneyRequest depositMoney) {
        Account account = accountRepo.findById(depositMoney.getAccountId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found account for given id"));

        account.setBalance(account.getBalance().add(depositMoney.getAmount()));

        Transaction transaction = new Transaction(account, null, "Bankomat", "Rzeszów bankomat nr 21", "Wpłata", depositMoney.getAmount(), Instant.now(), TransactionType.wewnętrzny, TransferType.Wpłata, TransferStatus.Zrealizowany);

        try {
            accountRepo.save(account);
            accountRepo.addMoneyToMainBankAccountBalance(depositMoney.getAmount());
            transactionRepo.save(transaction);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong data provided");
        }
    }

    @Transactional
    public void withdrawMoneyToAccount(DepositWithdrawMoneyRequest withdrawMoney) {
        Account account = accountRepo.findById(withdrawMoney.getAccountId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found account for given email"));

        account.setBalance(account.getBalance().subtract(withdrawMoney.getAmount()));

        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lack of funds");

        Transaction transaction = new Transaction(account, null, "Bankomat", "Rzeszów bankomat nr 21", "Wypłata", withdrawMoney.getAmount(), Instant.now(), TransactionType.wewnętrzny, TransferType.Wypłata, TransferStatus.Zrealizowany);

        try {
            accountRepo.save(account);
            accountRepo.addMoneyToMainBankAccountBalance(withdrawMoney.getAmount().negate());
            transactionRepo.save(transaction);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong data provided");
        }
    }

    private AccountDto convertAccountToAccountDto(Account account, Client client) {
        return new AccountDto(account.getAccountId(), account.getAccountNumber(), account.getBalance(),
                client.getClientId(), client.getNameAndAddress(), client.getEmail(), client.getPhoneNumber());
    }
}
