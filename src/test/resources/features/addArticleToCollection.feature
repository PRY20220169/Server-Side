Feature: Add Article to Collection

  Scenario Outline: As a user I want to add an article to an existing collection
    Given I am a logged in user
    And I am on the articles details
    And I have at least one existing collection
    When I click add to collection button
    Then The system will add the article to the selected collection
    And The system will return the message <message>
    Examples:
      | message                               |
      | "Artículo añadido satisfactoriamente" |

  Scenario Outline: As a user I want to add an article without any existing collection
    Given I am a logged in user
    And I am on the articles details
    And I don't have an existing collection
    When I click add to collection button without collection
    Then The system will return the error message <message>
    Examples:
      | message                                      |
      | "Cree una colección para añadir el artículo" |