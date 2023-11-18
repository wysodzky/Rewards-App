package com.example.rewards.resource;

import com.example.rewards.entity.Transaction;
import com.example.rewards.exception.TransactionException;
import com.example.rewards.service.TransactionService;
import com.example.rewards.types.RewardPointsTO;
import com.example.rewards.types.TransactionTO;
import com.example.rewards.types.TransactionUpdateTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Transaction", description = "Transaction API")
@RestController
@RequestMapping(path = "/transaction")
public class TransactionResource {
    private final TransactionService transactionService;

    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(
            summary = "Save transaction",
            description = "Save transaction by specifying transactionTO object. In response transaction Id"
    )
    @RequestMapping(method = RequestMethod.POST)
    public Long saveTransaction(@RequestBody TransactionTO transactionTO) throws TransactionException {
        return transactionService.saveTransaction(transactionTO);
    }

    @Operation(
            summary = "Get transaction",
            description = "Get transaction by specifying transaction id"
    )
    @RequestMapping(method = RequestMethod.GET , path = "/{id}")
    public Transaction getTransactionPlainObject(@PathVariable Long id) {
        return transactionService.getTransaction(id);
    }

    @Operation(
            summary = "Update transaction",
            description = "Update transaction by using transactionUpdateTO object"
    )
    @RequestMapping(method = RequestMethod.PUT)
    public Long updateTransaction(@RequestBody TransactionUpdateTO transactionUpdateTO) throws TransactionException {
        return transactionService.updateTransaction(transactionUpdateTO);
    }

    @Operation(
            summary = "Get reward points",
            description = "Get reward points from 3 month starting from current by using customer id"
    )
    @RequestMapping(method = RequestMethod.GET, path = "/rewardPoints/{id}")
    public RewardPointsTO getRewardPointsForCustomer(@PathVariable Long id) {
        return transactionService.getRewardPointsFromThreeMonthPeriod(id);
    }
}
