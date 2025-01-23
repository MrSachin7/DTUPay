Feature: Token

  Scenario: Successful token generation for the first time when generating 5 tokens
    Given a customer has a valid account in the DTU Pay system
    And the customer has never generated the token before
    When the customer generates 5 tokens
    Then the token should be generated successfully
    And the customer should receive 5 tokens

  Scenario: Unsuccessful token generation for the first time when generating more than 5 tokens
    Given a customer has a valid account in the DTU Pay system
    And the customer has never generated the token before
    When the customer generates 6 tokens
    Then the token should not be generated
    And the customer should not receive any tokens

  Scenario: Unsuccessful token generation when the customer already has a token
    Given a customer has a valid account in the DTU Pay system
    And the customer already has at least one remaining token in the system
    When the customer generates 4 tokens
    Then the token should not be generated
    And the customer should not receive any tokens

  Scenario: Unsuccessful token generation when the customer does not exist in the system
    Given a customer does not have a valid account in the DTU Pay system
    And the customer has never generated the token before
    When the customer generates 5 tokens
    Then the token should not be generated
    And the customer should not receive any tokens

  Scenario: Successful token generation not for the first time when the user only one token left
    Given a customer has a valid account in the DTU Pay system
    And the customer has only one token left
    When the customer generates 4 tokens
    Then the token should be generated successfully
    And the customer should receive 4 tokens