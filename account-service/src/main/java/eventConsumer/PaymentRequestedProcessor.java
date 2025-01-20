package eventConsumer;

import core.domainService.AccountService;
import events.AccountValidationCompleted;
import events.PaymentRequested;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class PaymentRequestedProcessor {

    private final AccountService accountService;

    public PaymentRequestedProcessor(AccountService accountService) {
        this.accountService = accountService;
    }

    @Incoming("PaymentRequested")
    @Outgoing("ValidateMerchantCompleted")
    public AccountValidationCompleted process(JsonObject request){
        PaymentRequested event = request.mapTo(PaymentRequested.class);

        try {
            String bankAccountNumber = accountService.retrieveBankAccount(event.getMerchantId());
            return new AccountValidationCompleted(event.getCorrelationId(),
                    event.getMerchantId(),
                    bankAccountNumber,
                    null
            );
        } catch (Exception e) {
            return new AccountValidationCompleted(event.getCorrelationId(),
                    event.getMerchantId(),
                    null,
                    e.getMessage()
            );
        }


    }
}
