Feature: Validate User

  Scenario Outline: As a user i want to login with my account successfully
    Given I am a user already registered in the app
    When I complete the form fields with my <email> and <password>
    And click the login button
    Then The app returns a successful response
    Examples:
      | email            | password       |
      | "test@gmail.com" | "securePassword" |

  Scenario Outline: As a user i try to login with a non registered email
    Given I am a user without and account registered
    When I complete the form fields with a non existing <email>
    Then The system authenticates the unregistered user
    And The system shows the message <error>
    Examples:
      | email                  | error                                                |
      | "noExisting@gmail.com" | "Introduce una direccion de correo electronico valido" |

  Scenario Outline: As user i want to login with the wrong password
    Given I am a user already registered in the app
    When I complete the form fields with my <email> and <password>
    And click the login button
    Then The app authenticates the wrong password and returns error <message>
    Examples:
      | email            | password      | message                 |
      | "test@gmail.com" | "wrongPassword" | "Wrong email or password" |
