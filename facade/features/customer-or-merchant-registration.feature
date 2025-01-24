Feature: Customer/Merchant Registration

  Scenario: Customer wants to register with DTU Pay
    Given a customer with firstname "Ari", lastname "Soso", CPR number "1234567890", and account number "12345"
    When the customer registers with DTUPay
    Then the RegisterCustomerRequested event is published
    And the event contains the correct data
    When the RegisterCustomerResponse is received with non-empty id
    Then the customer is registered

  Scenario: Customer registration interleaving requests
    Given a customer with firstname "Ari", lastname "Soso", CPR number "1234567890", and account number "12345"
    When the customer registers with DTUPay
    Then the RegisterCustomerRequested event is published for the first customer
    Given another customer with firstname "Bri", lastname "Soso", CPR number "1234567890", and account number "12345"
    When the second customer registers with DTUPay
    Then the RegisterCustomerRequested event is published for the second customer
    When the RegisterCustomerResponse is received for the second customer with non-empty id
    Then the second customer is registered
    When the RegisterCustomerResponse is received for the first customer with non-empty id
    Then the first customer is registered

