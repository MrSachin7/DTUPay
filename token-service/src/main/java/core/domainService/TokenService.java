package core.domainService;

import core.domain.common.NotFoundException;
import core.domain.token.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

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
        Tokens tokens = customer.generateTokens(amount);
        return tokens.getTokens().stream().map(UUID::toString).toList();
    }
}
