package core.domain.account;

import core.domain.common.ValueObject;

public class Name extends ValueObject {
    private String firstName;
    private String lastName;

    private Name(){
    }

    public static Name from(String firstName, String lastName){
        Name name = new Name();
        name.firstName = firstName;
        name.lastName = lastName;
        return name;
    }

    @Override
    protected Object[] getEqualityComponents() {
        return new Object[]{firstName, lastName};
    }
}
