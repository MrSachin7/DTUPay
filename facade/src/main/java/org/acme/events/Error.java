package org.acme.events;

import java.util.List;

public class Error {

    private List<String> errors;

    public Error() {}

    public Error(List<String> errors) {
        this.errors = errors;
    }


    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void addError(String error) {
        this.errors.add(error);
    }
}
