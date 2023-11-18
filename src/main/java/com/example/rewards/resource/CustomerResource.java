package com.example.rewards.resource;

import com.example.rewards.entity.Customer;
import com.example.rewards.exception.CustomerException;
import com.example.rewards.service.CustomerService;
import com.example.rewards.types.CustomerTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Customer", description = "Customer API")
@RestController
@RequestMapping(path = "/customer")
public class CustomerResource {
    private final CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(
            summary = "Create customer",
            description = "Create customer by specifying its name. In response there is id value which can be used later " +
                    "to create transaction with this customer or to get customer"
    )
    @RequestMapping(method = RequestMethod.POST)
    public Long createCustomer(@RequestBody CustomerTO customerTO) throws CustomerException {
        return customerService.saveCustomer(customerTO);
    }

    @Operation(
            summary = "Get customer",
            description = "Get customer by specifying its id"
    )
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @ResponseBody
    public Optional<Customer> getCustomerPlainObject(@PathVariable("id") Long id) {
        return customerService.getCustomer(id);
    }
}
