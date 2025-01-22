package core.domain.common;

// Only aggregates can have repositories
public interface Repository<TAgg extends Aggregate<TId>, TId> {
    TAgg find(TId id);
    void add(TAgg aggregate);
    void remove(TAgg aggregate);
}
