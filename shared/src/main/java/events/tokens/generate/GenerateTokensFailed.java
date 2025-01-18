package events.tokens.generate;

import events.Event;
import events.EventTypes;

public class GenerateTokensFailed extends Event<Void> {

    public GenerateTokensFailed() {
        super(EventTypes.GENERATE_TOKEN_FAILED);
    }

    public GenerateTokensFailed(String errorMessage) {
        markAsFailed(errorMessage);
    }
}
