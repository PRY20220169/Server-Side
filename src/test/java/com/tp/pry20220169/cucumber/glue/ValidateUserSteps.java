package com.tp.pry20220169.cucumber.glue;

import com.tp.pry20220169.exception.ExceptionResponse;
import com.tp.pry20220169.resource.CollectionResource;
import com.tp.pry20220169.resource.security.AuthenticationRequest;
import com.tp.pry20220169.resource.security.AuthenticationResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ValidateUserSteps {

    @Autowired
    TestRestTemplate restTemplate;
    AuthenticationResponse userResponse;

    AuthenticationRequest authRequest;

    ExceptionResponse errorResponse;

    @Given("I am a user already registered in the app")
    public void iAmAUserAlreadyRegisteredInTheApp() {
        //user already in database
    }

    @When("I complete the form fields with my {string} and {string}")
    public void iCompleteTheFormFieldsWithMyEmailAndPassword(String email, String password) {
        authRequest = AuthenticationRequest.builder()
                .username(email)
                .password(password)
                .build();
    }

    @And("click the login button")
    public void clickTheLoginButton() {
        userResponse = restTemplate.postForObject("/security/users/login", authRequest, AuthenticationResponse.class);
    }


    @And("click the login button with the wrong password")
    public void clickTheLoginButtonWithTheWrongPassword() {
        errorResponse = restTemplate.
                postForObject("/security/users/login", authRequest, ExceptionResponse.class);
    }

    @Then("The app returns a successful response")
    public void theAppReturnsASuccessfulResponse() {
        assertThat(userResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @Given("I am a user without and account registered")
    public void iAmAUserWithoutAndAccountRegistered() {
        // No user in the database
    }

    @When("I complete the form fields with a non existing {string}")
    public void iCompleteTheFormFieldsWithANonExistingEmail(String email) {
        authRequest = AuthenticationRequest.builder()
                .username(email)
                .password("12345678")
                .build();
    }

    @Then("The system authenticates the unregistered user")
    public void theSystemAuthenticatesTheUnregisteredUser() {
        errorResponse = restTemplate.
                postForObject("/security/users/login", authRequest, ExceptionResponse.class);
    }

    @And("The system shows the message {string}")
    public void theSystemShowsTheMessageError(String error) {
        assertThat(errorResponse.getMessage()).isEqualTo(error);
    }

    @Then("The app authenticates the wrong password and returns error {string}")
    public void theAppAuthenticatesTheWrongPasswordAndReturnsErrorMessage(String message) {
        assertThat(errorResponse.getMessage()).isEqualTo(message);
    }

}
