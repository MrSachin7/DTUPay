package steps;

import core.domain.account.AccountRepository;
import core.domain.account.Account;
import core.domain.account.CprNumber;
import core.domain.account.Name;
import core.domain.account.BankAccountNumber;
import core.domainService.AccountService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Test;
import persistence.AccountRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceSteps {

    private final AccountRepository accountRepository = new AccountRepositoryImpl();
    private final AccountService accountService = new AccountService(accountRepository);


    private Account account;

    @Given("a user with firstname {string}, lastname {string} and CPR number {string}")
    public void aUserWithFirstnameLastnameAndCprNumber(String firstname, String lastname, String cprNumber) {
        CprNumber cpr = CprNumber.from(cprNumber);
        Name name = Name.from(firstname, lastname);
        BankAccountNumber bankAccountNumber = BankAccountNumber.from("9876543210");
        account = Account.newAccount(cpr, name, bankAccountNumber);

        System.out.println("Created Account ID: " + account.getId());

    }

    @When("the user registers with the DTUPay")
    public void theUserRegistersAccount() {
        accountService.registerAccount(account.getName().getFirstName(), account.getName().getLastName(),
                account.getCprNumber().getValue(), account.getBankAccountNumber().getValue());

        System.out.println("Registering account with ID: " + account.getId());

    }

    @Then("the user should be successfully registered")
    public void theUserShouldBeSuccessfullyRegistered() {
        System.out.println("Account ID: " + account.getId());
        Account retrievedAccount = accountRepository.find(account.getId());
        System.out.println("Account retrieved: " + retrievedAccount);
        assertNotNull(retrievedAccount, "The user should be successfully registered");
    }

    @Then("the account should have the correct CPR number {string}")
    public void theAccountShouldHaveTheCorrectCPRNumber(String expectedCprNumber) {
        Account retrievedAccount = accountRepository.find(account.getId());
        assertEquals(expectedCprNumber, retrievedAccount.getCprNumber().getValue(), "The CPR number should be correct");
    }
}
