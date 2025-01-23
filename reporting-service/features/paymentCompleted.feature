Feature: Payment Completed
  Scenario: Successful payment
    When the system receives the payment completed event with successful payment
    Then the system stores the payment in the repository

  Scenario: Unsuccessful payment
    When the system receives the payment completed event with unsuccessful payment
    Then the system does not store the payment in the repository