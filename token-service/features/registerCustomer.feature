Feature: CustomerRegistration successful reaction
  Scenario: Successful registration of a customer
    When a customer registered event is received
    Then the customer should be registered to the repository