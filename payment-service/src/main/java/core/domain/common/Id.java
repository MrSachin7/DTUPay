package core.domain.common;

import java.util.UUID;

public abstract class Id extends ValueObject{
    protected UUID value;

    protected Id() {
    }

    public String getValue() {
        return value.toString();
    }
    @Override
    protected Object[] getEqualityComponents() {
        return new Object[]{value};
    }
}
