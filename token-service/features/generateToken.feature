Feature: Token Generation
  Scenario: Successful token generation when user requests token for the first time
    Given a customer exits in the system
    And the customer has never generated a token
    When the customer generates a tokens with amount 5
    Then the tokens should be generated successfully
    And the event is created with the same coRelation id

  Scenario: Successful token generation when user requests token while already having one token
    Given a customer exits in the system
    And the customer already has 1 token
    When the customer generates a tokens with amount 4
    Then the tokens should be generated successfully
    And the event is created with the same coRelation id

  Scenario: Unsuccessful token generation when user requests token while already having more than one token
    Given a customer exits in the system
    And the customer already has 2 token
    When the customer generates a tokens with amount 4
    Then the tokens should not be generated
    And the event is created with the same coRelation id

  Scenario: Unsuccessful token generation when user requests for more than 5 tokens
    Given a customer exits in the system
    And the customer has never generated a token
    When the customer generates a tokens with amount 6
    Then the tokens should not be generated
    And the event is created with the same coRelation id

  Scenario: Unsuccessful token generation when user does not exists in the system
    Given a customer does not exits in the system
    And the customer has never generated a token
    When the customer generates a tokens with amount 5
    Then the tokens should not be generated
    And the event is created with the same coRelation id
