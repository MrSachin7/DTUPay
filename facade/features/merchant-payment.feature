Feature: Merchant requests a payment
    Scenario: Merchant requests a valid payment
        Given the merchant is registered with DTUPay
        And the merchant has a valid token for a customer
        When the merchant requests the payment
        Then the PaymentRequested event is emitted
        And the PaymentRequested event contains the correct information
        When the PaymentCompleted event is received
        Then paymentId is returned

    Scenario: Invalid Payment Processing
        Given the merchant is registered with DTUPay
        And the merchant has a valid token for a customer
        When the merchant requests the invalid payment
        Then event completes with exception
