package com.tp.pry20220169.cucumber.glue;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ValidateUserSteps {
    @Given("I am a user already registered in the app")
    public void iAmAUserAlreadyRegisteredInTheApp() {
    }

    @When("I complete the form fields with my {string} and {string}")
    public void iCompleteTheFormFieldsWithMyEmailAndPassword(String email, String password) {
    }

    @And("click the login button")
    public void clickTheLoginButton() {
    }

    @Then("The app returns a successful response")
    public void theAppReturnsASuccessfulResponse() {
    }

    @Given("I am a user without and account registered")
    public void iAmAUserWithoutAndAccountRegistered() {
    }

    @When("I complete the form fields with a non existing {string}")
    public void iCompleteTheFormFieldsWithANonExistingEmail(String email) {
    }

    @Then("The system authenticates the unregistered user")
    public void theSystemAuthenticatesTheUnregisteredUser() {
    }

    @And("The system shows the message {string}")
    public void theSystemShowsTheMessageError(String error) {
    }

    @Then("The app authenticates the wrong password and returns error {string}")
    public void theAppAuthenticatesTheWrongPasswordAndReturnsErrorMessage(String message) {
    }
}
