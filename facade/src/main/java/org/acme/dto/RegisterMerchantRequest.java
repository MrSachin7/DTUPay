package org.acme.dto;

public record RegisterMerchantRequest(String firstname, String lastname, String cprNumber, String accountNumber) {
}
