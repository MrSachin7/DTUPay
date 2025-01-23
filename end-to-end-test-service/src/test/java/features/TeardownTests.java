package features;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.After;
import services.CustomerService;

public class TeardownTests {

    private final BankService bankService = new BankServiceService().getBankServicePort();
    private final CustomerService customerService = new CustomerService();

    @After
    public void tearDown() throws Exception {

        System.out.println("Tearing down tests");

        for (String account : Users.bankAccounts) {
            bankService.retireAccount(account);
        }

        Users.bankAccounts.clear();

        for (String dtuPayAccount : Users.dtuPayAccounts) {
            customerService.unregisterCustomer(dtuPayAccount);
        }
        Users.dtuPayAccounts.clear();

    }

}
