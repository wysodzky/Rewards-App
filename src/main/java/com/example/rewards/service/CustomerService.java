package com.example.rewards.service;

import com.example.rewards.entity.Customer;
import com.example.rewards.exception.CustomerException;
import com.example.rewards.types.CustomerTO;

import java.util.Optional;

public interface CustomerService {
    Long saveCustomer(CustomerTO customerTO) throws CustomerException;
    Optional<Customer> getCustomer(Long id);
}
