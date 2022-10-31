package com.tp.pry20220169.cucumber.glue;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CreateArticlesCollectionSteps {
    @Given("I am a logged in")
    public void iAmALoggedIn() {
    }

    @When("I click on the create collection button")
    public void iClickOnTheCreateCollectionButton() {
    }

    @And("I enter a valid name for the new collection")
    public void iEnterAValidNameForTheNewCollection() {
    }

    @Then("The system will return message {string}")
    public void theSystemWillReturnMessage(String message) {
    }

    @And("I enter an invalid name for the new collection")
    public void iEnterAnInvalidNameForTheNewCollection() {
    }

    @Then("The system will return the error {string}")
    public void theSystemWillReturnTheError(String error) {
    }
}
