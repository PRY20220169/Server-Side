Feature: Compare Journals

  Scenario: As a user i want to compare two journals
    Given I have added journals to the compare list
    When I click the compare journals button
    Then The system returns compared journals