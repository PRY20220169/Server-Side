Feature: Visualize Journal Details

  Scenario: As a user I view the details of a journal from a search
    Given that I have searched for articles
    When I click on the journal name from on one of the results
    Then The system will return the Journal Detail

  Scenario: As a user I view the details of a journal from an article
    Given that I have searched for an article
    When I click on Journal Name from the article's details
    Then The system will return the Journal Detail