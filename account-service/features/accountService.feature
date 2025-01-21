Feature: Account Service

  Scenario: Register a new account successfully
    Given a user with firstname "Satish", lastname "Gurung" and CPR number "3682351003"
    When the user registers with the DTUPay
    Then the user should be successfully registered
    And the account should have the correct CPR number "3682351003"

