package core.domain.common;

public abstract class Aggregate<TId> extends Entity<TId>{
    protected Aggregate(TId id) {
        super(id);
    }

    protected Aggregate() {
        // Default constructor for serialization purposes, if needed
    }
    @Override
    protected Object[] getEqualityComponents() {
        return new Object[]{id};
    }
}
