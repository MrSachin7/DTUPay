package org.acme.dto;

public record RegisterCustomerRequest(String firstname, String lastname, String cprNumber, String accountNumber) {
}
