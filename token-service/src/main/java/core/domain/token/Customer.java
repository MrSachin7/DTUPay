package core.domain.token;

import core.domain.common.Aggregate;
import core.domain.common.NotFoundException;

import java.util.List;
import java.util.UUID;

// TODO: Ask teacher why this can be an aggregate
// TODO: Should aggregate be copied or just the properties that are relevant to the microservice ?
public class Customer extends Aggregate<CustomerId> {
    private Tokens tokens;

    private Customer(){

    }

    public static Customer from(CustomerId customerId){
        Customer customer = new Customer();
        customer.id = customerId;
        customer.tokens = Tokens.newTokens();
        return customer;
    }

    public void generateTokens(int amount) throws TokenException {
        tokens.generateTokens(amount);
    }

    public List<String> getTokens(){
        return tokens.getTokens().stream().map(UUID::toString).toList();
    }

    public void validateToken(String token) throws NotFoundException {
        tokens.doesTokenExists(token);
    }

    public void removeToken(String tokenUsed) {
        tokens.removeToken(tokenUsed);
    }
}
