Feature: Validate token

  Scenario: Validate a token successfully
    Given a customer in the system
    And the customer has a valid token
    When the validate token is requested with the existing customer
    Then the token should be successfully validated
    And the event should be fired with same correlation id for validate token
    And the event should consist of the correct customer id