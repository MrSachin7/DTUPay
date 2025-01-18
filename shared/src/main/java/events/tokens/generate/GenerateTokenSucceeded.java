package events.tokens.generate;

import events.Event;
import events.EventTypes;

public class GenerateTokenSucceeded extends Event<GenerateTokenSucceeded.Payload> {

    public GenerateTokenSucceeded() {
        super(EventTypes.GENERATE_TOKEN_SUCCEEDED, new Payload());
    }

    public static class Payload{

    }
}
