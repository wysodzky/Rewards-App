package com.example.rewards.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "t_transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "transaction_date")
    private Long transactionDate;

    @Column(name = "transaction_value")
    private Integer transactionValue;

    @Column(name = "reward_points")
    private Integer rewardPoints;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Long transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Integer getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(Integer transactionValue) {
        this.transactionValue = transactionValue;
    }

    public Integer getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(Integer rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
