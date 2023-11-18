package com.example.rewards.repository;

import com.example.rewards.entity.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Operation(summary = "Get transactions from period of time",
            description = "Query gets transaction between startDate and endDate period of time for specific customerId")
    @Query("from Transaction t where t.transactionDate >= :startDate AND t.transactionDate <= :endDate AND t.customer.id = :customerId")
    List<Transaction> findTransactionFromPeriodOfTime(@Param("startDate") Long startDate, @Param("endDate") Long endDate,
                                                      @Param("customerId") Long customerId);

}
