package events;

import events.tokens.generate.GenerateTokenRequested;

import java.util.Map;

public final class EventTypes {

    public static final String GENERATE_TOKEN_REQUESTED = "GenerateTokenRequested";
    public static final String GENERATE_TOKEN_SUCCEEDED = "GenerateTokenSucceeded";
    public static final String GENERATE_TOKEN_FAILED = "GenerateTokenFailed";

    private static final Map<String, Class> eventTypes = Map.of(
            GENERATE_TOKEN_REQUESTED, GenerateTokenRequested.class,
            GENERATE_TOKEN_SUCCEEDED, GenerateTokenRequested.class,
            GENERATE_TOKEN_FAILED, GenerateTokenRequested.class
    );


    private EventTypes() {
        // Private constructor to prevent instantiation
        throw new AssertionError("Cannot instantiate EventTypes");
    }
}
