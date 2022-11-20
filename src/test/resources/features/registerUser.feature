Feature: Register User

  Scenario: As a user i want to register my account in the app
    Given I am a user and click the button register
    When I complete the form fields with a unique email
    And click complete button
    Then The system returns a successful response


  Scenario Outline: As a user i want to register my account with an existing account email
    Given I am a user with an account register with <email> and click the button register
    When I complete the form fields with <email>
    Then The system verifies that te email is registered and shows an error message
    Examples:
      | email |
      | "test@gmail.com" |

  Scenario Outline: As a user i want to register my account with a weak password
    Given I am a user and click the button register
    When I complete the form fields with a weak <password>
    And click complete button
    Then The system verifies the password is weak and returns an error message response
    Examples:
      | password |
      | "123" |
