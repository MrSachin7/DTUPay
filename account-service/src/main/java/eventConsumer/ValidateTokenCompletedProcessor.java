package eventConsumer;

import core.domainService.AccountService;
import events.AccountValidationCompleted;
import events.ValidateTokenCompleted;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class ValidateTokenCompletedProcessor {

    private final AccountService accountService;

    private final Emitter<AccountValidationCompleted> accountValidationCompletedEmitter;


    public ValidateTokenCompletedProcessor(AccountService accountService,
                                           @Channel("ValidateCustomerCompleted") Emitter<AccountValidationCompleted> accountValidationCompletedEmitter) {
        this.accountService = accountService;
        this.accountValidationCompletedEmitter = accountValidationCompletedEmitter;
    }

    @Incoming("ValidateTokenCompleted")
    public void process(JsonObject request) {
        ValidateTokenCompleted event = request.mapTo(ValidateTokenCompleted.class);
        if (!event.wasSuccessful()) {
            System.out.println("Aborting since the event was unsuccessful");
            return;
        }

        try {
            String bankAccountNumber = accountService.retrieveBankAccount(event.getCustomerId());

            AccountValidationCompleted eventToFire = new AccountValidationCompleted(event.getCorrelationId(),
                    event.getCustomerId(),
                    bankAccountNumber,
                    null);
            accountValidationCompletedEmitter.send(eventToFire);

        } catch (Exception e) {
            AccountValidationCompleted eventToFire = new AccountValidationCompleted(event.getCorrelationId(),
                    event.getCustomerId(),
                    null,
                    e.getMessage());
            accountValidationCompletedEmitter.send(eventToFire);

        }


    }
}
