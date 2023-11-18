package com.example.rewards.types;

public class TransactionTO {
    private Long customerId;
    private Integer transactionValue;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(Integer transactionValue) {
        this.transactionValue = transactionValue;
    }
}
