package com.pab.bankb.service;

import com.pab.bankb.dto.TransactionForBankDto;
import com.pab.bankb.dto.TransactionForClientDto;
import com.pab.bankb.dto.TransferRequest;
import com.pab.bankb.dto.accoutingUnit.AccountingUnitIntegration;
import com.pab.bankb.dto.accoutingUnit.Payments;
import com.pab.bankb.model.*;
import com.pab.bankb.repo.AccountRepo;
import com.pab.bankb.repo.TransactionRepo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private AccountRepo accountRepo;
    private TransactionRepo transactionRepo;

    public TransactionService(AccountRepo accountRepo, TransactionRepo transactionRepo) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }

    @Transactional
    public void sendTransfer(TransferRequest transferRequest) {
        if (transferRequest.getTransferAmount().compareTo(BigDecimal.ZERO) < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer amount must be positive");

        Account account = accountRepo.findById(transferRequest.getAccountId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found account for given account_id"));

        if (account.getAccountNumber().equals(transferRequest.getRecipientNumber()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot send a transfer to yourself");

        //Get money from sender account
        this.subtractFundsFromAccount(account, transferRequest.getTransferAmount());

        Transaction transaction = new Transaction();

        transaction.setAccount(account);

        this.convertTransferRequestToTransaction(transaction, transferRequest);

        //set Transaction Type (Wewnętrzny / Zewnętrzny)
        this.setTransactionType(transaction, transaction.getRecipientOrSenderNumber());

        //If 'Wewnęrzny' make a transfer at the bank, if 'Zewnętrzny' send the transfer to the unit of account
        if (transaction.getTransactionType() == TransactionType.wewnętrzny) this.makeInternalTransfer(transaction);
        else if (transaction.getTransactionType() == TransactionType.zewnętrzny) this.makeExternalTransfer(transaction);
    }

    @Transactional
    public List<TransactionForClientDto> getTransactionListForAccount(Long id) {
        List<Transaction> transactionListForAccount = transactionRepo.findByAccount_AccountId(id);
        transactionListForAccount = transactionListForAccount.stream().
                filter(transaction -> transaction.getTransferStatus().equals(TransferStatus.Zrealizowany)).
                collect(Collectors.toList());

        return transactionListForAccount.stream().map(this::convertTransactionToTransactionForClientDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void synchronizationWithTheAccountingUnit() {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final String ulrToAccountingUnit = "https://jrozliczajaca.herokuapp.com/przelewy";

        List<Transaction> allTransaction = transactionRepo.findAll();
        List<Transaction> transactionWithZleconyStatus = allTransaction.stream()
                .filter(transaction -> transaction.getTransferStatus().equals(TransferStatus.Wyslany)).collect(Collectors.toList());

        List<Payments> paymentsForAccountingUnit = new ArrayList<>(10);
        BigDecimal paymentsSum = BigDecimal.ZERO;

        for (Transaction transaction : transactionWithZleconyStatus) {
            paymentsForAccountingUnit.add(new Payments(transaction.getAccount().getAccountNumber(), transaction.getAccount().getClient().getNameAndAddress(), transaction.getRecipientOrSenderNumber(), transaction.getRecipientOrSenderAddressAndName(), transaction.getTitle(), transaction.getTransferAmount()));
            paymentsSum = paymentsSum.add(transaction.getTransferAmount());
            transaction.setTransferStatus(TransferStatus.Zrealizowany);
            transactionRepo.save(transaction);
        }

        Account mainBankAccount = accountRepo.findById((long) 1).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found main bank account"));

        AccountingUnitIntegration transactionsForUnitIntegration = new AccountingUnitIntegration();
        transactionsForUnitIntegration.setBankNo(mainBankAccount.getAccountNumber());

        transactionsForUnitIntegration.setPayments(paymentsForAccountingUnit);
        transactionsForUnitIntegration.setPaymentSum(paymentsSum);

        HttpEntity<AccountingUnitIntegration> httpEntity = new HttpEntity<>(transactionsForUnitIntegration, headers);

        AccountingUnitIntegration receivedTransfer = rest.postForObject(ulrToAccountingUnit, httpEntity, AccountingUnitIntegration.class);

        //If received Payments isEmpty, the bank has no incoming transfers from accouting unit, so return
        if (receivedTransfer == null || receivedTransfer.getPayments().isEmpty()) return;

        //save incoming transfer from accouting unit to database
        for (Payments payments : receivedTransfer.getPayments()) {
            Optional<Account> account = accountRepo.findByAccountNumber(payments.getCreditedAccountNumber());
            if (account.isPresent()) {
                Transaction transaction = new Transaction();
                //increase balance in mainBank account and Client account
                account.get().setBalance(account.get().getBalance().add(payments.getAmount()));
                mainBankAccount.setBalance(mainBankAccount.getBalance().add(payments.getAmount()));

                //Transaction to db
                transaction.setAccount(account.get());
                transaction.setTransactionType(TransactionType.zewnętrzny);
                this.convertPaymentToTransaction(payments, transaction);

                transactionRepo.save(transaction);
                accountRepo.save(account.get());
            }
        }

        //save mainBankAccount with increased balance
        this.accountRepo.save(mainBankAccount);
    }

    @Transactional
    public List<TransactionForBankDto> getTransactionListForBank() {
        List<Transaction> transactionListForAccount = transactionRepo.findAll();
        List<TransactionForBankDto> transactionForBankList = new ArrayList<>(100);
        for (Transaction transaction : transactionListForAccount) {
            if (transaction.getTransferStatus() == TransferStatus.Zrealizowany)
                transactionForBankList.add(this.covertTransactionToTransactionForBankDto(transaction, new TransactionForBankDto()));
        }

        return transactionForBankList;
    }


    private TransactionForBankDto covertTransactionToTransactionForBankDto(Transaction transaction, TransactionForBankDto transactionForBank) {
        if (transaction.getTransferType() == TransferType.Obciążenie && transaction.getTransferStatus() == TransferStatus.Zrealizowany) {
            transactionForBank.setTransferId(transaction.getTransactionId());
            transactionForBank.setSenderNameAndAddress(transaction.getAccount().getClient().getNameAndAddress());
            transactionForBank.setSenderNumber(transaction.getAccount().getAccountNumber());
            transactionForBank.setRecipientNameAndAddress(transaction.getRecipientOrSenderAddressAndName());
            transactionForBank.setRecipientNumber(transaction.getRecipientOrSenderNumber());
        } else if (transaction.getTransferType() == TransferType.Uznanie && transaction.getTransferStatus() == TransferStatus.Zrealizowany) {
            transactionForBank.setTransferId(transaction.getTransactionId());
            transactionForBank.setSenderNameAndAddress(transaction.getRecipientOrSenderAddressAndName());
            transactionForBank.setSenderNumber(transaction.getRecipientOrSenderNumber());
            transactionForBank.setRecipientNameAndAddress(transaction.getAccount().getClient().getNameAndAddress());
            transactionForBank.setRecipientNumber(transaction.getAccount().getAccountNumber());
        } else if (transaction.getTransferType() == TransferType.Wpłata || transaction.getTransferType() == TransferType.Wypłata) {
            transactionForBank.setTransferId(transaction.getTransactionId());
            transactionForBank.setSenderNameAndAddress(transaction.getRecipientOrSenderAddressAndName());
            transactionForBank.setSenderNumber(null);
            transactionForBank.setRecipientNameAndAddress(transaction.getAccount().getClient().getNameAndAddress());
            transactionForBank.setRecipientNumber(transaction.getAccount().getAccountNumber());
        }
        transactionForBank.setTitle(transaction.getTitle());
        transactionForBank.setTransferAmount(transaction.getTransferAmount());
        transactionForBank.setTransferDate(transaction.getTransferDate());

        return transactionForBank;
    }

    private void subtractFundsFromAccount(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().subtract(amount));
        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lack of funds");
    }

    private void setTransactionType(Transaction transaction, String accountNumber) {
        if (accountNumber.startsWith("10302172", 2)) transaction.setTransactionType(TransactionType.wewnętrzny);
        else transaction.setTransactionType(TransactionType.zewnętrzny);
    }

    private void makeExternalTransfer(Transaction transactionAsSender) {
        Instant transactionDate = Instant.now();
        transactionAsSender.setTransferDate(transactionDate);
        transactionAsSender.setTransferStatus(TransferStatus.Wyslany);

        this.accountRepo.subtractMoneyToMainBankAccountBalance(transactionAsSender.getTransferAmount());
        transactionRepo.save(transactionAsSender);
    }

    private void makeInternalTransfer(Transaction transactionAsSender) {
        //Copy some value of transactionAsSender, except account, transferType, because they must be different
        Transaction transactionAsRecipient = new Transaction(transactionAsSender.getAccount().getAccountNumber(), transactionAsSender.getAccount().getClient().getNameAndAddress(), transactionAsSender.getTitle(), transactionAsSender.getTransferAmount(), transactionAsSender.getTransactionType(), transactionAsSender.getTransferStatus());
        transactionAsRecipient.setTransferType(TransferType.Uznanie);

        Account recipientAccount = accountRepo.findByAccountNumber(transactionAsSender.getRecipientOrSenderNumber()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found account with given account number"));

        //Add money to recipient account
        recipientAccount.setBalance(recipientAccount.getBalance().add(transactionAsSender.getTransferAmount()));

        transactionAsRecipient.setAccount(recipientAccount);

        Instant transactionDate = Instant.now();
        transactionAsSender.setTransferDate(transactionDate);
        transactionAsRecipient.setTransferDate(transactionDate);

        transactionAsSender.setTransferStatus(TransferStatus.Zrealizowany);
        transactionAsRecipient.setTransferStatus(TransferStatus.Zrealizowany);

        transactionRepo.save(transactionAsSender);
        transactionRepo.save(transactionAsRecipient);
    }


    private void convertTransferRequestToTransaction(Transaction transaction, TransferRequest transferRequest) {
        transaction.setRecipientOrSenderNumber(transferRequest.getRecipientNumber());
        transaction.setRecipientOrSenderAddressAndName(transferRequest.getRecipientNameAndAddress());
        transaction.setTitle(transferRequest.getTitle());
        transaction.setTransferAmount(transferRequest.getTransferAmount());
        transaction.setTransferType(TransferType.Obciążenie);
        transaction.setTransferStatus(TransferStatus.Zlecony);
    }

    private TransactionForClientDto convertTransactionToTransactionForClientDto(Transaction transaction) {
        TransactionForClientDto transactionForClientDto = new TransactionForClientDto();
        transactionForClientDto.setTransferId(transaction.getTransactionId());
        transactionForClientDto.setNameAndAddress(transaction.getRecipientOrSenderAddressAndName());
        transactionForClientDto.setAccountNumber(transaction.getRecipientOrSenderNumber());
        transactionForClientDto.setTitle(transaction.getTitle());
        transactionForClientDto.setTransferType(transaction.getTransferType().name());
        transactionForClientDto.setTransferAmount(transaction.getTransferAmount());
        transactionForClientDto.setTransferDate(transaction.getTransferDate());

        return transactionForClientDto;
    }

    private void convertPaymentToTransaction(Payments payments, Transaction transaction) {
        transaction.setRecipientOrSenderAddressAndName(payments.getDebitedNameAndAddress());
        transaction.setRecipientOrSenderNumber(payments.getDebitedAccountNumber());
        transaction.setTitle(payments.getTitle());
        transaction.setTransferAmount(payments.getAmount());
        transaction.setTransferDate(Instant.now());
        transaction.setTransferType(TransferType.Uznanie);
        transaction.setTransferStatus(TransferStatus.Zrealizowany);
    }
}
