package core.domainService;

import core.domain.common.NotFoundException;
import core.domain.token.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TokenService {

    private final CustomerRepository repository;

    public TokenService(CustomerRepository repository) {
        this.repository = repository;
    }

    public List<String> generateTokens(String customerId, int amount) throws TokenException, NotFoundException {
        System.out.println("Processing token request");
        Customer customer = repository.find(CustomerId.from(customerId));
        if (customer == null) {
            customer = Customer.from(CustomerId.from(customerId));
            repository.add(customer);
        }
        customer.generateTokens(amount);
        return customer.getTokens();

    }

    public String validateToken(String token) throws NotFoundException {
        Customer customer = repository.findByToken(token);

        if (customer == null) {
            throw new NotFoundException("Invalid token");
        }

        // Now we dispose the token
        customer.deleteToken(token);

        return customer.getId().getValue();
    }

    public void unregisterCustomer(String customerId) {
        repository.remove(CustomerId.from(customerId));
    }
}
