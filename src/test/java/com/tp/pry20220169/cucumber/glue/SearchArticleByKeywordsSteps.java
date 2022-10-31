package com.tp.pry20220169.cucumber.glue;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SearchArticleByKeywordsSteps {
    @Given("I am a user who enters {string} in the search engine")
    public void iAmAUserWhoEntersKeywordsInTheSearchEngine(String keywords) {
    }

    @When("I press click Search")
    public void iPressClickSearch() {
    }

    @Then("the system shows a list of articles whose keywords, title or description contain the largest portion of the search words")
    public void theSystemShowsAListOfArticlesWhoseKeywordsTitleOrDescriptionContainTheLargestPortionOfTheSearchWords() {
    }

    @And("The system sorts the list by the highest number of matches")
    public void theSystemSortsTheListByTheHighestNumberOfMatches() {
    }

    @And("The system does not find any articles that match the keywords")
    public void theSystemDoesNotFindAnyArticlesThatMatchTheKeywords() {
    }

    @Then("The system shows you the message {string}")
    public void theSystemShowsYouTheMessageMessage(String message) {
    }
}
