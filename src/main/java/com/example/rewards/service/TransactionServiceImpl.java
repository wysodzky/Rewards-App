package com.example.rewards.service;

import com.example.rewards.entity.Customer;
import com.example.rewards.entity.Transaction;
import com.example.rewards.exception.TransactionException;
import com.example.rewards.repository.CustomerRepository;
import com.example.rewards.repository.TransactionRepository;
import com.example.rewards.types.RewardPointsTO;
import com.example.rewards.types.TransactionTO;
import com.example.rewards.types.TransactionUpdateTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class TransactionServiceImpl implements TransactionService {

    Logger logger = Logger.getLogger(TransactionServiceImpl.class.getName());

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    private final RewardCalculator rewardCalculator;

    public TransactionServiceImpl(TransactionRepository transactionRepository, CustomerRepository customerRepository, RewardCalculator rewardCalculator) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.rewardCalculator = rewardCalculator;
    }

    @Override
    public Long saveTransaction(TransactionTO transactionTO) throws TransactionException {
        Optional<Customer> customer = customerRepository.findById(transactionTO.getCustomerId());

        if (customer.isEmpty()) {
            throw new TransactionException("Customer with id : " + transactionTO.getCustomerId() + " not found. " +
                    "Cannot process!");
        }

        Transaction transaction = new Transaction();

        int transactionValue = transactionTO.getTransactionValue();
        checkTransactionValue(transactionValue);
        transaction.setTransactionValue(transactionValue);

        transaction.setTransactionDate(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        transaction.setRewardPoints(rewardCalculator.calculateRewardPoints(transactionValue));
        transaction.setCustomer(customer.get());

        Transaction savedTransaction =  transactionRepository.save(transaction);

        return savedTransaction.getId();
    }

    @Override
    public Long updateTransaction(TransactionUpdateTO transactionUpdateTO) throws TransactionException {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionUpdateTO.getId());
        if (transactionOptional.isEmpty()) {
            throw new TransactionException("Transaction with id: " + transactionUpdateTO.getId() + " does not exist");
        }

        Optional<Customer> customerOptional = customerRepository.findById(transactionUpdateTO.getCustomerId());

        if (customerOptional.isEmpty()) {
            throw new TransactionException("Customer with id: " + transactionUpdateTO.getCustomerId() +" does not exist");
        }

        Customer customer = customerOptional.get();

        Transaction transaction = transactionOptional.get();
        transaction.setTransactionValue(transactionUpdateTO.getTransactionValue());
        transaction.setTransactionDate(transactionUpdateTO.getTransactionDate());
        transaction.setRewardPoints(rewardCalculator.calculateRewardPoints(transactionUpdateTO.getTransactionValue()));
        transaction.setCustomer(customer);

        Transaction transactionUpdated = transactionRepository.save(transaction);
        return transactionUpdated.getId();
    }

    @Override
    public Transaction getTransaction(Long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            return transaction.get();
        }
        logger.log(Level.WARNING,"Transaction with id:" + id + "not found");
        return null;
    }

    @Override
    public RewardPointsTO getRewardPointsFromThreeMonthPeriod(Long customerId) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime firstDayOfCurrentMonth = today.with(ChronoField.DAY_OF_MONTH, 1);
        List<Transaction> transactionsCurrentMonth = transactionRepository.
                findTransactionFromPeriodOfTime(firstDayOfCurrentMonth.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                        today.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), customerId);

        int currentMonthSum = transactionsCurrentMonth.stream().mapToInt(Transaction::getRewardPoints).sum();

        LocalDateTime startLastMonth = today.minusMonths(1).with(ChronoField.DAY_OF_MONTH, 1);
        LocalDateTime endLastMonth = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

        List<Transaction> transactionsFromLastMonth = transactionRepository.
                findTransactionFromPeriodOfTime(startLastMonth
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), endLastMonth.
        atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), customerId);


        int lastMonthSum = transactionsFromLastMonth.stream().mapToInt(Transaction::getRewardPoints).sum();

        LocalDateTime startSecondLastMonth = today.minusMonths(2).with(ChronoField.DAY_OF_MONTH, 1);
        LocalDateTime endSecondLastMonth = today.minusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

        List<Transaction> transactionsFromSecondLastMonth = transactionRepository.
                findTransactionFromPeriodOfTime(startSecondLastMonth.
                        atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), endSecondLastMonth.
                        atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), customerId);


        int secondLastMonthSum = transactionsFromSecondLastMonth.stream().mapToInt(Transaction::getRewardPoints).sum();

        RewardPointsTO rewardPointsTO = new RewardPointsTO();
        rewardPointsTO.setCurrentMonthPoints(currentMonthSum);
        rewardPointsTO.setLastMonthPoints(lastMonthSum);
        rewardPointsTO.setSecondLastMonthPoints(secondLastMonthSum);

        int total = currentMonthSum + lastMonthSum + secondLastMonthSum;
        rewardPointsTO.setTotal(total);

        return rewardPointsTO;

    }

    private void checkTransactionValue(int transactionValue) throws TransactionException {
        if (transactionValue <= 0) {
            throw new TransactionException("Transaction value must be higher than 0!");
        }
    }

}
