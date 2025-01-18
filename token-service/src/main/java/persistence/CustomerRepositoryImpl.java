package persistence;

import core.domain.token.Customer;
import core.domain.token.CustomerId;
import core.domain.token.CustomerRepository;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class CustomerRepositoryImpl implements CustomerRepository {
    private final Map<CustomerId, Customer> customers = new HashMap<>();

    @Override
    public Customer find(CustomerId customerId) {
        return customers.get(customerId);
    }

    @Override
    public void add(Customer aggregate) {
        customers.put(aggregate.getId(), aggregate);

    }

    @Override
    public void remove(Customer aggregate) {
        customers.remove(aggregate.getId());
    }
}
