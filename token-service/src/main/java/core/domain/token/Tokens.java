package core.domain.token;

import core.domain.common.ValueObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tokens extends ValueObject {

    private List<UUID> tokens = new ArrayList<>(6);

    private Tokens() {

    }

    public static Tokens newTokens() {
        return new Tokens();
    }

    public void generateTokens(int amount) throws TokenException {
        if (amount > 5){
            throw new TokenException("Cannot generate more than 5 tokens at a time");
        }
        if (amount < 1){
            throw new TokenException("Cannot generate less than 1 token");
        }

        if (tokens.size() > 1){
            throw new TokenException("Cannot generate token when the customer already has multiple unused tokens");
        }
        // Then we generate amount number of tokens

        for (int i = 0; i < amount; i++){
            tokens.add(UUID.randomUUID());
        }
    }

    public void removeToken(UUID token){
        tokens.remove(token);
    }

    public List<UUID> getTokens() {
        return tokens;
    }

    @Override
    protected Object[] getEqualityComponents() {
        return new Object[0];
    }


}
