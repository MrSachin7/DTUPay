Feature: Token Generation
  Scenario: Successful token generation
    Given a customer exits in the system
    And the customer has never generated a token
    When the customer generates a tokens with amount 5
    Then the tokens should be generated successfully
    And the event is created with the same coRelation id