package org.acme.service;

import org.acme.dto.RegisterCustomerRequest;
import org.acme.dto.RegisterCustomerResponse;

public interface CustomerService {

    RegisterCustomerResponse registerCustomer(RegisterCustomerRequest registerCustomerDto);
}
