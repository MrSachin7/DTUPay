Feature: Token Generation

  Scenario: Customer requests tokens
    Given a customer has a valid account with DTUPay
    When the customer requests tokens
    Then the GenerateTokenRequested event is emitted
    And the GenerateTokenRequested event has the correct data
    And the GenerateTokenCompleted is received

  Scenario: Customer token request interleaving requests
    Given a customer has a valid account with DTUPay
    When the customer requests tokens
    Then the GenerateTokenRequested event is emitted for the first customer
    And the GenerateTokenRequested event for first customer has the correct data
    Given another customer with valid account
    When the second customer requests tokens
    Then the GenerateTokenRequested event is emitted for the second customer
    And the GenerateTokenRequested event for second customer has the correct data
    And the GenerateTokenCompleted is received for the first customer
    And the GenerateTokenCompleted is received for the second customer
