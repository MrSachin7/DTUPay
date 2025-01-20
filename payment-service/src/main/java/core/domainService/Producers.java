package core.domainService;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class Producers {

    @Produces
    @ApplicationScoped
    public BankService bankService() {
        return new BankServiceService().getBankServicePort();
    }
}
