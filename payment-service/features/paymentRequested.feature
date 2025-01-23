Feature: Payment Requested

  Scenario: Successful payment transaction
    When the paymentRequested event is received
    And the ValidateCustomer event is received
    And the ValidateMerchant is received
    Then the payment request should be successfully processed
    And the payment completed event should be emitted
    And the event should have the same correlation id as the payment request

  Scenario: Unsuccessful payment transaction due to failed customer validation
    When the paymentRequested event is received
    And the ValidateCustomer event is received with a failed event
    And the ValidateMerchant is received
    Then the payment request should not be successfully processed
    And the payment completed event should be emitted
    And the event should have the same correlation id as the payment request

  Scenario: Unsuccessful payment transaction due to failed merchant validation
    When the paymentRequested event is received
    And the ValidateCustomer event is received
    And the ValidateMerchant is received with a failed event
    Then the payment request should not be successfully processed
    And the payment completed event should be emitted
    And the event should have the same correlation id as the payment request
