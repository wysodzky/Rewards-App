package com.example.rewards;

import com.example.rewards.entity.Customer;
import com.example.rewards.entity.Transaction;
import com.example.rewards.exception.TransactionException;
import com.example.rewards.repository.CustomerRepository;
import com.example.rewards.repository.TransactionRepository;
import com.example.rewards.service.TransactionService;
import com.example.rewards.types.RewardPointsTO;
import com.example.rewards.types.TransactionTO;
import com.example.rewards.types.TransactionUpdateTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void testTransactionSave() throws TransactionException {
        Customer customer = new Customer();
        customer.setName("Customer1");
        Long customerId = customerRepository.save(customer).getId();


        TransactionTO transactionTO = new TransactionTO();
        transactionTO.setTransactionValue(120);
        transactionTO.setCustomerId(customerId);

        Long transactionId = transactionService.saveTransaction(transactionTO);

        Transaction transaction = transactionService.getTransaction(transactionId);

        Assertions.assertEquals(transactionTO.getTransactionValue(), transaction.getTransactionValue());
        Assertions.assertEquals(transactionTO.getCustomerId(), transaction.getCustomer().getId());
        Assertions.assertNotEquals(0, transaction.getRewardPoints());

    }

    @Test
    void testTransactionSaveWithCustomerNotExist() {
        TransactionTO transactionTO = new TransactionTO();
        transactionTO.setTransactionValue(120);
        transactionTO.setCustomerId(9899L);

        Assertions.assertThrows(TransactionException.class, () -> transactionService.saveTransaction(transactionTO));
    }

    @Test
    void testTransactionUpdate() throws TransactionException {
        Customer customer = new Customer();
        customer.setName("Customer1");
        Long customerId = customerRepository.save(customer).getId();


        TransactionTO transactionTO = new TransactionTO();
        transactionTO.setTransactionValue(120);
        transactionTO.setCustomerId(customerId);

        Long transactionId = transactionService.saveTransaction(transactionTO);

        Transaction transaction = transactionService.getTransaction(transactionId);

        Assertions.assertEquals(transactionTO.getTransactionValue(), transaction.getTransactionValue());
        Assertions.assertEquals(transactionTO.getCustomerId(), transaction.getCustomer().getId());
        Assertions.assertEquals(90, transaction.getRewardPoints());


        TransactionUpdateTO transactionUpdateTO = new TransactionUpdateTO();
        transactionUpdateTO.setTransactionValue(200);
        transactionUpdateTO.setTransactionDate(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        transactionUpdateTO.setCustomerId(customerId);
        transactionUpdateTO.setId(transactionId);

        Long transactionUpdatedId = transactionService.updateTransaction(transactionUpdateTO);

        Transaction transactionUpdated = transactionService.getTransaction(transactionUpdatedId);

        Assertions.assertEquals(transactionUpdateTO.getTransactionValue(), transactionUpdated.getTransactionValue());
        Assertions.assertEquals(250, transactionUpdated.getRewardPoints());

    }



    @Test
    void testRewardPointsFromThreeMonthPeriod() {
        Customer customer = new Customer();
        customer.setName("Customer1");
        Long customerId = customerRepository.save(customer).getId();

        customer  = customerRepository.findById(customerId).get();


        Transaction transactionCurrentMonth = new Transaction();
        transactionCurrentMonth.setRewardPoints(90);
        transactionCurrentMonth.setTransactionDate(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        transactionCurrentMonth.setTransactionValue(120);
        transactionCurrentMonth.setCustomer(customer);

        transactionRepository.save(transactionCurrentMonth);



        Transaction transactionLastMonth = new Transaction();
        transactionLastMonth.setRewardPoints(90);
        transactionLastMonth.setTransactionDate(LocalDateTime.now().minusMonths(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        transactionLastMonth.setTransactionValue(120);
        transactionLastMonth.setCustomer(customer);

        transactionRepository.save(transactionLastMonth);


        Transaction transactionSecondLastMonth = new Transaction();
        transactionSecondLastMonth.setRewardPoints(90);
        transactionSecondLastMonth.setTransactionDate(LocalDateTime.now().minusMonths(2).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        transactionSecondLastMonth.setTransactionValue(120);
        transactionSecondLastMonth.setCustomer(customer);


        transactionRepository.save(transactionSecondLastMonth);


        RewardPointsTO rewardPointsTO = transactionService.getRewardPointsFromThreeMonthPeriod(customerId);

        Assertions.assertEquals(90, rewardPointsTO.getCurrentMonthPoints());
        Assertions.assertEquals(90, rewardPointsTO.getLastMonthPoints());
        Assertions.assertEquals(90, rewardPointsTO.getSecondLastMonthPoints());
        Assertions.assertEquals(270, rewardPointsTO.getTotal());


    }

    @Test
    void testRewardPointsWithUpdateTransaction() throws TransactionException {
        Customer customer = new Customer();
        customer.setName("Customer1");
        Long customerId = customerRepository.save(customer).getId();
        customer  = customerRepository.findById(customerId).get();


        Transaction transaction = new Transaction();
        transaction.setRewardPoints(90);
        transaction.setTransactionDate(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        transaction.setTransactionValue(120);
        transaction.setCustomer(customer);

        Transaction savedValue = transactionRepository.save(transaction);

        TransactionUpdateTO transactionUpdateTO = new TransactionUpdateTO();
        transactionUpdateTO.setCustomerId(customerId);
        transactionUpdateTO.setTransactionValue(200);
        transactionUpdateTO.setTransactionDate(LocalDateTime.now().minusMonths(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        transactionUpdateTO.setId(savedValue.getId());


         transactionService.updateTransaction(transactionUpdateTO);

         RewardPointsTO rewardPointsTO = transactionService.getRewardPointsFromThreeMonthPeriod(customerId);

         Assertions.assertEquals(250, rewardPointsTO.getLastMonthPoints());
         Assertions.assertEquals(0, rewardPointsTO.getCurrentMonthPoints());
         Assertions.assertEquals(0, rewardPointsTO.getSecondLastMonthPoints());
         Assertions.assertEquals(250, rewardPointsTO.getTotal());

    }

}
