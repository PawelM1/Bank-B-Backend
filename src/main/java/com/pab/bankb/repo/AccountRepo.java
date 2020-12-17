package com.pab.bankb.repo;

import com.pab.bankb.model.Account;
import com.pab.bankb.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;


public interface AccountRepo extends JpaRepository<Account, Long> {
    Optional<Account> findByClient(Client client);

    Optional<Account> findByClient_Email(String clientEmail);

    Optional<Account> findByAccountNumber(String accountNumber);

    @Modifying(clearAutomatically = true)
    @Query("update Account a set a.balance = a.balance + :amount where a.accountId = 1")
    void addMoneyToMainBankAccountBalance(@Param("amount") BigDecimal amount);

    @Modifying(clearAutomatically = true)
    @Query("update Account a set a.balance = a.balance - :amount where a.accountId = 1")
    void subtractMoneyToMainBankAccountBalance(@Param("amount") BigDecimal amount);

    Boolean existsByAccountNumber(String accountNumber);
}
