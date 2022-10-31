package com.tp.pry20220169.cucumber.glue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

public class CompareArticlesSteps {
    @Given("I have added articles to the compare list")
    public void iHaveAddedArticlesToTheCompareList() {
        List<Long> ids = List.of(1L, 2L);

    }

    @When("I click the compare articles button")
    public void iClickTheCompareArticlesButton() {

    }

    @Then("The system returns compared articles")
    public void theSystemReturnsComparedArticles() {
        //
    }
}
