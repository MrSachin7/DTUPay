Feature: Payment Requested

  Scenario: Successful payment transaction
    Given that a customer and a merchant exists in the system
    And the customer has a valid payment method
    When the payment request is initiated with the customerId and merchantId
    Then the payment request should be successfully processed
    And the payment completed event should be emitted
    And the event should have the same correlation id as the payment request
    And the payment should be a valid payment
