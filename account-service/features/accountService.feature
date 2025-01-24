Feature: Account Service

  Scenario: Register a new account successfully
    Given a user with firstname "Satish", lastname "Gurung", CPR number "3682351003" and account number "1234567890"
    When the user registers with the DTUPay
    Then the user should be successfully registered
    And the account should have the correct CPR number "3682351003"
    And the event should be fired with same correlation id

    Scenario: Register an account unsuccessfully when the CPR number is invalid (not 10 digits)
      Given a user with firstname "Satish", lastname "Gurung", CPR number "1" and account number "1234567890"
      When the user registers with the DTUPay
      Then the user should not be successfully registered
      And the event should be fired with same correlation id