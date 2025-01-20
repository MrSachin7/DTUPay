package events;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.UUID;

@RegisterForReflection
public class Event<T> {
    private String correlationId;
    private String eventType;
    private T payload;
    private EventStatus status;
    private String errorMessage;

    public enum EventStatus {
        SUCCEEDED,
        FAILED,
        REQUESTED,
    }

    public Event() {

    }

    public boolean isSucceeded() {
        return status == EventStatus.SUCCEEDED;
    }

    public Event(String eventType) {
        this.correlationId = UUID.randomUUID().toString();
        this.eventType = eventType;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    protected Event(String eventType, T payload) {
        this.correlationId = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.payload = payload;
    }

    protected void markAsSucceeded() {
        this.status = EventStatus.SUCCEEDED;
    }
    protected void markAsRequested() {
        this.status = EventStatus.REQUESTED;
    }



    public void markAsFailed(String errorMessage) {
        this.status = EventStatus.FAILED;
        this.errorMessage = errorMessage;
    }

    // Getters
    public String getCorrelationId() { return correlationId; }
    public String getEventType() { return eventType; }
    public T getPayload() { return payload; }
    public EventStatus getStatus() { return status; }
    public String getErrorMessage() { return errorMessage; }

    @Override
    public String toString() {
        return String.format(
                " CorrelationId=%s, type=%s, status=%s]",
                 correlationId, eventType, status
        );
    }
}