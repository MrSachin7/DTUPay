Feature: Customer Registration

  Scenario: Customer wants to register with DTU Pay
    Given a customer with firstname "Ari", lastname "Soso", CPR number "1234567890", and account number "12345"
    When the customer registers with DTUPay
    Then the RegisterCustomerRequested event is published
    And the event contains the correct data
