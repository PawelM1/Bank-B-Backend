package com.pab.bankb.repo;

import com.pab.bankb.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount_AccountId(Long account_accountId);
}
