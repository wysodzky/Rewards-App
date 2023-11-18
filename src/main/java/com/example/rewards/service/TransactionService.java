package com.example.rewards.service;

import com.example.rewards.entity.Transaction;
import com.example.rewards.exception.TransactionException;
import com.example.rewards.types.RewardPointsTO;
import com.example.rewards.types.TransactionTO;
import com.example.rewards.types.TransactionUpdateTO;

public interface TransactionService {
    Long saveTransaction(TransactionTO transactionTO) throws TransactionException;

    Long updateTransaction(TransactionUpdateTO transactionUpdateTO) throws TransactionException;
    Transaction getTransaction(Long id);

    RewardPointsTO getRewardPointsFromThreeMonthPeriod(Long customerId);
}
