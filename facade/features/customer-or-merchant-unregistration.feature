Feature: Customer/Merchant unegistration

  Scenario: Customer wants to unregister with DTU Pay
    Given a customer
    And the customer is registered with DTUPay
    When the customer unregisters with DTUPay
    Then the UnregisterCustomerRequested event is published
    And the UnregisterCustomerRequested contains the correct data