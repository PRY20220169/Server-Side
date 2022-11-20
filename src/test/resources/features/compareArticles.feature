Feature: Compare Articles

  Scenario: As a user i want to compare two or more articles
    Given I have added articles to the compare list
    When I click the compare articles button
    Then The system returns compared articles