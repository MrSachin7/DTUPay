package core.domain.common;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class ValueObject {

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        ValueObject that = (ValueObject) other;
        return Stream.of(this.getEqualityComponents())
                .allMatch(component -> Arrays.asList(that.getEqualityComponents()).contains(component));
    }

    protected abstract Object[] getEqualityComponents();

    @Override
    public int hashCode() {
        return Stream.of(getEqualityComponents())
                .mapToInt(Objects::hashCode)
                .reduce(0, (x, y) -> x ^ y);
    }
}
