Feature: Payment Completed
  Scenario: Successful payment of the user
    Given a customer exists in the system
    And the customer already has a token
    When the system receives the payment completed event with successful payment
    Then the system removes the used token from the customer

  Scenario: Unsuccessful payment of the user
    Given a customer exists in the system
    And the customer already has a token
    When the system receives the payment completed event with unsuccessful payment
    Then the system does not remove the used token from the customer
