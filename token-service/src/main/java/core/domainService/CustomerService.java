package core.domainService;

import core.domain.token.Customer;
import core.domain.token.CustomerId;
import core.domain.token.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public void addCustomer(String customerId) {
        System.out.println("Processing customer request");
        Customer aggregate = Customer.from(CustomerId.from(customerId));
        repository.add(aggregate);
    }
}
