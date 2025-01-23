Feature: Payment Service
  Scenario: Successful Payment
    Given a customer account "CUST123", merchant account "MERCH456", and amount of 100.50
    When the system processes a payment with customer account "CUST123", merchant account "MERCH456", and amount 100.50
    Then the payment is successfully processed
    And a payment ID is generated

#  Scenario: Validate Payment Transfer
#    Given a customer account "1234567890"
#    And a merchant account "0987654321"
#    And a transfer amount of 50.00
#    When the system processes a payment
#    Then the system stores the payment
#    And transfers money from "1234567890" to "0987654321" with amount 50.00