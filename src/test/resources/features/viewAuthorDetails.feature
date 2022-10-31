Feature: View Author Details

  Scenario: As i user i want to visualize an article's author details
    Given I am a user viewing an article's details
    When I click the name of the author
    Then The system returns the author's information

  Scenario Outline: As a user i want to visualize an author's details from a search
    Given I am a user who has performed an article search by <keyword>
    When I click the author's name from a result
    Then The system returns the author's information
    Examples:
      | keyword  |
      | "mobile" |


