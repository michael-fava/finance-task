package com.mfava.akcetask.repo;

import com.mfava.akcetask.model.Account;
import com.mfava.akcetask.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Stream<Transaction> findAllByDebitAccountEqualsOrCreditAccountEquals(Account dbAccount, Account crAccount);
}
