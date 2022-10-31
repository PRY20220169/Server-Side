Feature: Compare Authors

  Scenario: As a user i want to compare two authors
    Given I have added two authors to the compare list
    When I click the compare authors button
    Then The system returns compared authors