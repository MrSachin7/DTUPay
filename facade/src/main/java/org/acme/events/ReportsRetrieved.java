package org.acme.events;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ReportsRetrieved {
    private String correlationId;

    public ReportsRetrieved() {
    }

    public ReportsRetrieved(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}
