Feature: Reports Requested
  Scenario: Retrieve all payments successfully
    When the system receives a ReportsRequested event for all reports
    Then the system retrieves all payments from the repository
    And the system publishes a ReportsRetrieved event with all payment data

  Scenario: Retrieve customer payments successfully
    When the system receives a ReportsRequested event for customer reports with a valid customer ID
    Then the system retrieves payments for that customer from the repository
    And the system publishes a ReportsRetrieved event with the customer's payment data

  Scenario: Retrieve merchant payments successfully
    When the system receives a ReportsRequested event for merchant reports with a valid merchant ID
    Then the system retrieves payments for that merchant from the repository
    And the system publishes a ReportsRetrieved event with the merchant's payment data
    And the customer id is omitted

  Scenario: Invalid report type
    When the system receives a ReportsRequested event with an invalid report type
    Then the system publishes a ReportsRetrieved event with an error message "Invalid report type requested"

  Scenario: Missing customer ID
    When the system receives a ReportsRequested event for customer reports with a missing customer ID
    Then the system publishes a ReportsRetrieved event with an error message "Customer ID is required"

  Scenario: Missing merchant ID
    When the system receives a ReportsRequested event for merchant reports with a missing merchant ID
    Then the system publishes a ReportsRetrieved event with an error message "Merchant ID is required"
