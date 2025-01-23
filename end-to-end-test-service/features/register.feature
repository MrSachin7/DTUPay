Feature: Register

  Scenario: Successful registration
    Given a customer with name "Braaszilasdasa", last name "Chisldaseaas", and CPR "4512313330" and bankAccountNumber "1231231231"
    When the customer registers with Simple DTU Pay using their bank account
    Then the registration should be successful
    And the customer should be assigned a random identifier

  Scenario: Unsuccessful registration due to invalid CPR (not 10 digits)
    Given a customer with name "Braaszilasdasa", last name "Chisldaseaas", and CPR "NOTVALID" and bankAccountNumber "1231231231"
    When the customer registers with Simple DTU Pay using their bank account
    Then the registration should not be successful