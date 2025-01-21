package persistence;

import core.domain.token.Customer;
import core.domain.token.CustomerId;
import core.domain.token.CustomerRepository;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.List;
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

        System.out.println("New list of customers: ");
        customers.forEach((key, value) -> System.out.println("Key: " + key + " Value: " + value));
    }

    @Override
    public void remove(Customer aggregate) {
        customers.remove(aggregate.getId());
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customers.values().stream().toList();
    }

    @Override
    public Customer findByToken(String token) {
        return customers.values().stream()
                .filter(customer -> customer.getTokens().contains(token))
                .findFirst()
                .orElse(null);
    }
}
