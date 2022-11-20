Feature: Visualize Article Details

  Scenario: As user i want to visualize a selected article details
    Given I am a user who has performed a search
    And The system returns a list with articles
    When I click on an article from the list with
    Then The system returns the article details
