Feature: Payment
  Scenario: Successful payment
    Given a customer with name "Braaszilasdasa", last name "Chisldaseaas", and CPR "4512313330"
    And the customer is registered with the bank with an initial balance of 1000 kr
    And the customer is registered with Simple DTU Pay using their bank account
    And a merchant with name "Nepsdfsadasdla", last name "Dendmsadasdarka", and CPR "4588367868"
    And the merchant is registered with the bank with an initial balance of 1000 kr
    And the merchant is registered with Simple DTU Pay using their bank
    And the customer has generated token
    When the merchant initiates a payment for 10 kr by the customer with the valid token
    Then the payment should be successful
    And the balance of the customer should be 990 kr
    And the balance of the merchant should be 1010 kr

  Scenario: Unsuccessful payment
    Given a customer with name "Braaszilasdasa", last name "Chisldaseaas", and CPR "4512313330"
    And the customer is registered with the bank with an initial balance of 1000 kr
    And the customer is registered with Simple DTU Pay using their bank account
    And a merchant with name "Nepsdfsadasdla", last name "Dendmsadasdarka", and CPR "4588367868"
    And the merchant is registered with the bank with an initial balance of 1000 kr
    And the merchant is registered with Simple DTU Pay using their bank
    And the customer has generated token
    When the merchant initiates a payment for 10 kr by the customer with the valid token
    Then the payment should be successful
    And the balance of the customer should be 990 kr
    And the balance of the merchant should be 1010 kr

