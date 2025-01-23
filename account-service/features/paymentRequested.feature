Feature: Payment requested
  Scenario: Successful payment requested response
    Given that an merchant exists in the system
    When the payment request is initiated with the merchant id
    Then the payment request should be successfully processed
    And the payment completed event should be emitted
    And the event should have the same correlation id as the payment request
    And the bank account number should be the same as the merchant's bank account number

  Scenario: Unsuccessful payment requested response when merchant does not exist
    Given that an merchant does not exists in the system
    When the payment request is initiated with the merchant id
    Then the payment request should not be successfully processed
    And the payment completed event should be emitted
    And the event should have the same correlation id as the payment request
    And the bank account number should be null
