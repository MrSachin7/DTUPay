Feature: A user requests reports
  Scenario: Manager requests reports
    When a manager requests reports for all payments
    Then the ReportsRequested event is published
    And the ReportsRequested event contains the correct data
    When the ReportsResponse is received
    Then the reports are returned

  Scenario: Customer requests reports
    Given a customer is registered at DTUPay
    When the customer requests reports payments
    Then the ReportsRequested event is published
    And the event contains the correct customer data
    When the ReportsResponse is received
    Then the reports are returned

    Scenario: Merchant requests reports
    Given a merchant is registered at DTUPay
    When the merchant requests reports payments
    Then the ReportsRequested event is published
    And the event contains the correct merchant data
    When the ReportsResponse is received
    Then the reports are returned