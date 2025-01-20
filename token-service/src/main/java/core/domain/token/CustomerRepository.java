package core.domain.token;

import core.domain.common.Repository;

import java.util.List;

public interface CustomerRepository extends Repository<Customer, CustomerId> {

    List<Customer> getAllCustomers();

    Customer findByToken(String token);
}
