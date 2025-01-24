package persistence;

import core.domain.token.Customer;
import core.domain.token.CustomerId;
import core.domain.token.CustomerRepository;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Eduardo Filipe Fernandes Miranda (s223113)
 */
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
    public void remove(CustomerId id) {
        Customer customer = customers.get(id);
        if (customer == null) {
            System.out.println("Customer not found");
            return;
        }
        Customer remove = customers.remove(id);
        System.out.println("Removed customer: " + remove.getId());
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customers.values().stream().toList();
    }

    @Override
    public Customer findByToken(String token) {
        System.out.println("Looking for customer with token: " + token);
        return customers.values().stream()
                .filter(customer -> customer.getTokens().contains(token))
                .findFirst()
                .orElse(null);
    }
}
