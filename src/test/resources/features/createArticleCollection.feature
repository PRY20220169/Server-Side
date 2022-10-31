Feature: Create Articles Collection

  Scenario Outline: As a user I create a collection
    Given I am a logged in
    When I click on the create collection button
    And I enter a valid name for the new collection
    Then The system will return message <message>
    Examples:
      | message                               |
      | "Colección Creada Satisfactoriamente" |

  Scenario Outline: As a user I create a collection with an invalid name
    Given I am a logged in
    When I click on the create collection button
    And I enter an invalid name for the new collection
    Then The system will return the error <error>
    Examples:
      | error                                    |
      | "No se ha podido crear la nueva colección" |