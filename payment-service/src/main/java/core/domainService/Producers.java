package core.domainService;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

/**
 * @author: Janusz Jakub Wilczek (s243891)
 */
@ApplicationScoped
public class Producers {

    @Produces
    @ApplicationScoped
    public BankService bankService() {
        return new BankServiceService().getBankServicePort();
    }
}
