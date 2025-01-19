Feature: Token validation
  Scenario: Successful token validation when the token exists on the user
    Given a customer exits in the system for token validation
    And the customer has generated a token
    When the customer validates the token with a valid token
    Then the token should be validated successfully
    And the event is created with the same coRelation id for token validation

  Scenario: UnSuccessful token validation when the token does not exists on the user
    Given a customer exits in the system for token validation
    And the customer has generated a token
    When the customer validates the token with an invalid token
    Then the token should not be validated
    And the event is created with the same coRelation id for token validation

  Scenario: UnSuccessful token validation when user does not exists in the system
    Given a customer does not exits in the system for token validation
    And the customer has generated a token
    When the customer validates the token with an invalid token
    Then the token should not be validated
    And the event is created with the same coRelation id for token validation
