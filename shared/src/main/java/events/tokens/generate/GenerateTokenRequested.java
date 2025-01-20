package events.tokens.generate;


import events.Event;
import events.EventTypes;

public class GenerateTokenRequested extends Event<GenerateTokenRequested.Payload> {

    public GenerateTokenRequested(String customerId, int amount) {
        super(EventTypes.GENERATE_TOKEN_REQUESTED, new Payload(customerId, amount));
    }

    public GenerateTokenRequested() {
        super();
        markAsRequested();
    }

    public static class Payload{

        private String customerId;
        private int amount;

        public Payload(String customerId, int amount) {
            this.customerId = customerId;
            this.amount = amount;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

    }
}
