Feature: Payment
  Scenario: Successful payment
    Given a customer with name "Braaszdfasdilasdasa", last name "Chisldadfgsdaseaas", and CPR "4836313336"
    And the customer is registered with the bank with an initial balance of 1000 kr
    And the customer is registered with Simple DTU Pay using their bank account
    And a merchant with name "Nepsdfsaddfgasdasdla", last name "Dendmsafddasdasdarka", and CPR "4589686814"
    And the merchant is registered with the bank with an initial balance of 1000 kr
    And the merchant is registered with Simple DTU Pay using their bank
    And the customer has generated token
    When the merchant initiates a payment for 10 kr by the customer with the valid token
    Then the payment should be successful
    And the balance of the customer should be 990 kr
    And the balance of the merchant should be 1010 kr

  Scenario: Unsuccessful payment due to invalid token
    Given a customer with name "Braaszasisladfgsdasa", last name "Chisdfgsasldaseaas", and CPR "1342591230"
    And the customer is registered with the bank with an initial balance of 1000 kr
    And the customer is registered with Simple DTU Pay using their bank account
    And a merchant with name "Nepsdfsadasdasdla", last name "Dendmsadaassdarka", and CPR "4564236748"
    And the merchant is registered with the bank with an initial balance of 1000 kr
    And the merchant is registered with Simple DTU Pay using their bank
    And the customer has generated token
    When the merchant initiates a payment for 10 kr by the customer with the invalid token
    Then the payment should not be successful
    And the balance of the customer should be 1000 kr
    And the balance of the merchant should be 1000 kr

  Scenario: Unsuccessful payment because merchant is not registered with DTU Pay
    Given a customer with name "Braaszasilfsasdasa", last name "Chisasldafdsseaas", and CPR "2312801027"
    And the customer is registered with the bank with an initial balance of 1000 kr
    And the customer is registered with Simple DTU Pay using their bank account
    And a merchant with name "Nepsdfsadsdfasdasdla", last name "Dendmsadsdfaassdarka", and CPR "1575752682"
    And the merchant is registered with the bank with an initial balance of 1000 kr
    And the merchant is not registered with Simple DTU Pay using their bank
    And the customer has generated token
    When the merchant initiates a payment for 10 kr by the customer with the valid token
    Then the payment should not be successful
    And the balance of the customer should be 1000 kr

