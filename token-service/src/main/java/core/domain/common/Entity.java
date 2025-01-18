package core.domain.common;

public abstract class Entity<TId> extends ValueObject {
    protected TId id;

    protected Entity(TId id) {
        this.id = id;
    }

    protected Entity() {
        // Default constructor for serialization purposes, if needed
    }

    public TId getId() {
        return id;
    }

    public void setId(TId id) {
        this.id = id;
    }
}
