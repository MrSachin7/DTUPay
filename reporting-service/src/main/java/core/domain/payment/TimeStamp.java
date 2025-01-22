package core.domain.payment;

import core.domain.common.ValueObject;

import java.time.LocalDateTime;

public class TimeStamp extends ValueObject {

    private LocalDateTime value;

    @Override
    protected Object[] getEqualityComponents() {
        return new Object[]{value};
    }

    public LocalDateTime getValue() {
        return value;
    }

    public static TimeStamp now() {
        TimeStamp timeStamp = new TimeStamp();
        timeStamp.value = LocalDateTime.now();
        return timeStamp;
    }

}
