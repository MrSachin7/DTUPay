package org.acme.dto;

public record StartPaymentRequest(String token, double amount) {
}
