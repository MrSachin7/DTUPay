package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.RegisterCustomerRequest;
import org.acme.dto.RegisterCustomerResponse;

@ApplicationScoped
public class CustomerServiceImpl implements CustomerService {
    public CustomerServiceImpl() {
    }

    @Override
    public RegisterCustomerResponse registerCustomer(RegisterCustomerRequest registerCustomerDto) {
        return null;
    }
}
