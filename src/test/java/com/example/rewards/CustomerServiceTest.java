package com.example.rewards;

import com.example.rewards.exception.CustomerException;
import com.example.rewards.service.CustomerService;
import com.example.rewards.types.CustomerTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    private  CustomerService customerService;

    @Test
    void testCustomerSave() throws CustomerException {
        CustomerTO customer = new CustomerTO();
        String name = "Customer1";
        customer.setName(name);
        Long id = customerService.saveCustomer(customer);

        Assertions.assertEquals(customerService.getCustomer(id).get().getName(), "Customer1");
    }

    @Test
    void testCustomerEmptyNameSave() {
        CustomerTO customer = new CustomerTO();
        String name = "";
        customer.setName(name);
        Assertions.assertThrows(CustomerException.class, () -> customerService.saveCustomer(customer));
    }

}
