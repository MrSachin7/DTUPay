package core.domain.token;

import core.domain.common.Aggregate;

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


}
