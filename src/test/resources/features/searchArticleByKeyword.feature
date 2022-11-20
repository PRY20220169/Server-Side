Feature: Search Article By keywords

  Scenario Outline: As i user i want to find a list of relevant articles
    Given I am a user who enters <keywords> in the search engine
    And the system has articles with those keywords
    When I press click Search
    Then the system shows a list of articles whose keywords, title or description contain the largest portion of the search words
    And The system sorts the list by the highest number of matches
    Examples:
      | keywords |
      | "mobile" |

  Scenario Outline: As a user I can't find any article that matches the keywords entered
    Given I am a user who enters <keywords> in the search engine
    When I press click Search
    And The system does not find any articles that match the keywords
    Then The system shows you the message <message>
    Examples:
      | keywords | message                                                                                                         |
      | "notFindableKeyword" | "The searched keywords did not find relevant results. Check the spelling and/or expand your search parameters." |


