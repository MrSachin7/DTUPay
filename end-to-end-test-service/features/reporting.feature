Feature: Reporting service

  Scenario: All report generation
    Given that a multiple payments are made successfully in the system
    When the reporting service is called to generate a report
    Then the report should be generated successfully
    And the payments must be included in the report

  Scenario: Report generation for a specific customer
    Given that multiple payments are made successfully in the system by a specific customer
    When the reporting service is called to generate a report for the customer
    Then the report should be generated successfully
    And the payments must be included in the report
    And there should not be any reports for other customers

  Scenario: Report generation for a specific merchant
    Given that a multiple payments are made successfully in the system
    When the reporting service is called to generate a report for the merchant
    Then the report should be generated successfully
    And the payments must be included in the report
    And there should not be any reports for other merchants
    And the reports must not contain any information about the customers