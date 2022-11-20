package com.tp.pry20220169.cucumber.glue;

import com.tp.pry20220169.exception.ExceptionResponse;
import com.tp.pry20220169.resource.*;
import com.tp.pry20220169.resource.security.AuthenticationRequest;
import com.tp.pry20220169.resource.security.AuthenticationResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CreateArticlesCollectionSteps {

    @Autowired
    TestRestTemplate restTemplate;

    AuthenticationResponse userResponse;

    AccountResource accountResponse;

    HttpHeaders headers = new HttpHeaders();

    SaveCollectionResource collectionRequest;
    CollectionResource collectionResponse;
    Exception errorResponse;

    @Given("I am a logged in")
    public void iAmALoggedIn() {
        AuthenticationRequest authRequest = AuthenticationRequest.builder()
                .username("diego")
                .password("1234")
                .build();
        userResponse = restTemplate.postForObject("/security/users/login", authRequest, AuthenticationResponse.class);
        SaveAccountResource saveAccountResource = SaveAccountResource.builder()
                .firstName("diego2")
                .lastName("johnson2")
                .build();
//        accountResponse = restTemplate.postForObject("/api/users/" + userResponse.getId().toString() + "/account", saveAccountResource, AccountResource.class);
        headers.set("Authorization", "Bearer " + userResponse.getAccess_token());
    }

    @And("I enter a valid name for the new collection")
    public void iEnterAValidNameForTheNewCollection() {
        collectionRequest = SaveCollectionResource.builder()
                .name("Collection")
                .build();
    }

    @When("I click on the create collection button")
    public void iClickOnTheCreateCollectionButton() {
        ResponseEntity<CollectionResource> response = restTemplate.exchange("/api/users/" + userResponse.getId() + "/account/collections",
                HttpMethod.POST,
                new HttpEntity<>(collectionRequest, headers),
                CollectionResource.class);
        collectionResponse = restTemplate.exchange("/api/users/" + userResponse.getId() + "/account/collections",
                HttpMethod.POST,
                new HttpEntity<>(collectionRequest, headers),
                CollectionResource.class).getBody();
    }


    @Then("The system will return message {string}")
    public void theSystemWillReturnMessage(String message) {
        //TODO: Add logic for returns to carry a custom message like a ResponseWrapper that has the content, message and status code
        assertThat(collectionResponse.getName()).isEqualTo(collectionRequest.getName());
    }

    @And("I enter an invalid name for the new collection")
    public void iEnterAnInvalidNameForTheNewCollection() {
        collectionRequest = SaveCollectionResource.builder()
                .name("")
                .build();
    }

    @When("I click on the create collection button with invalid name")
    public void iClickOnTheCreateCollectionButtonWithInvalidName() {
        errorResponse = restTemplate.
                postForObject("/api/users/" + userResponse.getId() + "/account/collections", collectionRequest, Exception.class);
    }

    @Then("The system will return the error {string}")
    public void theSystemWillReturnTheError(String error) {
        assertThat(errorResponse).isNotNull();
    }


}
