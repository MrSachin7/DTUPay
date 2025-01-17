package events;

import java.io.Serial;
import java.io.Serializable;

public class RegisterCustomerRequested implements Serializable {

    private String coRelationId;
    private String firstname;
    private String lastname;
    private String cprNumber;
    private String accountNumber;

    // Default constructor
    public RegisterCustomerRequested() {}

    // Parameterized constructor
    public RegisterCustomerRequested(String coRelationId, String firstname, String lastname, String cprNumber, String accountNumber) {
        this.coRelationId = coRelationId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.cprNumber = cprNumber;
        this.accountNumber = accountNumber;
    }

    // Getters and Setters
    public String getCoRelationId() {
        return coRelationId;
    }

    public void setCoRelationId(String coRelationId) {
        this.coRelationId = coRelationId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCprNumber() {
        return cprNumber;
    }

    public void setCprNumber(String cprNumber) {
        this.cprNumber = cprNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
