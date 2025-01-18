package org.acme.events;

import java.util.Optional;

public abstract class Event<T> {

    protected String coRelationId;


    private T body;

    private Error error;


    public Optional<T> getBody() {
        return Optional.ofNullable(body);
    }

    public Optional<Error> getError() {
        return Optional.ofNullable(error);
    }

    private boolean wasSuccessful(){
        return getError().isEmpty();
    };

    public void setBody(T body) {
        this.body = body;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getCoRelationId() {
        return coRelationId;
    }
}
