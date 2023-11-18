package com.example.rewards.service;

import com.example.rewards.entity.Customer;
import com.example.rewards.exception.CustomerException;
import com.example.rewards.repository.CustomerRepository;
import com.example.rewards.types.CustomerTO;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public Long saveCustomer(CustomerTO customerTO) throws CustomerException {
        validateCustomerTO(customerTO);
        Customer customer = new Customer();
        customer.setName(customerTO.getName());
        Customer customerSaved = customerRepository.save(customer);
        return customerSaved.getId();
    }

    @Override
    public Optional<Customer> getCustomer(Long id) {
        return customerRepository.findById(id);
    }

    private void validateCustomerTO(CustomerTO customerTO) throws CustomerException {
        if (StringUtils.isEmpty(customerTO.getName())) {
            throw new CustomerException("Customer name cannot be empty!");
        }
    }
}
