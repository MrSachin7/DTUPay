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

        if(customer == null){
            throw new NotFoundException("Customer not found");
        }
        customer.generateTokens(amount);
        return customer.getTokens();
    }

    public void validateToken(String customerId, String token) throws NotFoundException {
        Customer customer = repository.find(CustomerId.from(customerId));

        if (customer == null){
            throw new NotFoundException("Customer not found");
        }

        customer.validateToken(token);
    }

    public void removeToken(String customerId, String tokenUsed) {
        Customer customer = repository.find(CustomerId.from(customerId));
        customer.removeToken(tokenUsed);
    }
}