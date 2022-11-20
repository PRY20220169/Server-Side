package com.tp.pry20220169.cucumber.glue;

import com.tp.pry20220169.domain.repository.*;
import com.tp.pry20220169.exception.ExceptionResponse;
import com.tp.pry20220169.resource.*;
import com.tp.pry20220169.resource.security.AuthenticationRequest;
import com.tp.pry20220169.resource.security.AuthenticationResponse;
import com.tp.pry20220169.util.RestResponsePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.ScenarioScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ScenarioScope
public class AddArticleToCollectionSteps {

    @Autowired
    TestRestTemplate restTemplate;

    AuthenticationResponse userResponse;

    ArticleResource articleResponse;

    AccountResource accountResponse;

    CollectionResource collectionResponse;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CollectionRepository collectionRepository;

    HttpHeaders headers = new HttpHeaders();

    ExceptionResponse exceptionResponse;

    @Given("I am a logged in user")
    public void iAmALoggedInUser() {
        AuthenticationRequest authRequest = AuthenticationRequest.builder()
                .username("diego")
                .password("1234")
                .build();
        userResponse = restTemplate.postForObject("/security/users/login", authRequest, AuthenticationResponse.class);
        SaveAccountResource saveAccountResource = SaveAccountResource.builder()
                .firstName("diego")
                .lastName("johnson")
                .build();
        //TODO: Move this to a setup class that creates the account before all the other classes are tested, that way it stays in the temporal database during tests execution
        accountResponse = restTemplate.postForObject("/api/users/" + userResponse.getId().toString() + "/account", saveAccountResource, AccountResource.class);
        headers.set("Authorization", "Bearer " + userResponse.getAccess_token());
    }

    @And("I am on the articles details")
    public void iAmOnTheArticlesDetails() {
        setArticle();
    }

    private void setArticle() {
        String randomString = UUID.randomUUID().toString();
        SaveArticleResource newArticleRequest = SaveArticleResource.builder()
                .title(randomString)
                .conferenceName(randomString)
                .journalName(randomString)
                .publicationDate(new Date())
                .description(randomString)
                .keywords(List.of(randomString))
                .categories(List.of(randomString))
                .numberOfCitations(2)
                .numberOfReferences(2)
                .build();
        articleResponse = restTemplate.postForObject("/api/articles", newArticleRequest, ArticleResource.class);
    }

    @And("I have at least one existing collection")
    public void iHaveAtLeastOneExistingCollection() {
        SaveCollectionResource collectionRequest = SaveCollectionResource.builder()
                .name("Collection 1")
                .build();
        HttpEntity<?> requestEntity = new HttpEntity<>(collectionRequest, headers);
        collectionResponse = restTemplate.exchange("/api/users/" + accountResponse.getId() + "/account/collections",
                HttpMethod.POST,
                requestEntity,
                CollectionResource.class).getBody();
        assertThat(collectionResponse).isNotNull();
    }


    @When("I click add to collection button")
    public void iClickAddToCollectionButton() {
        collectionResponse = restTemplate.exchange(
                "/api/collections/" + collectionResponse.getId() + "/articles/" + articleResponse.getId(),
                HttpMethod.POST,
                new HttpEntity<>(null, headers),
                CollectionResource.class).getBody();

    }

    @Then("The system will add the article to the selected collection")
    public void theSystemWillAddTheArticleToTheSelectedCollection() {
        assertThat(collectionResponse.getArticles().size()).isGreaterThan(0);
    }

    @And("The system will return the message {string}")
    public void theSystemWillReturnTheMessage(String message) {
        //TODO: Add logic for returns to carry a custom message like a ResponseWrapper that has the content, message and status code
        cleanup();
    }

    @And("I don't have an existing collection")
    public void iDonTHaveAnExistingCollection() {
        RestResponsePage<CollectionResource> collections = restTemplate.exchange(
                String.format("/api/users/%s/account/collections", userResponse.getId().toString()),
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                new ParameterizedTypeReference<RestResponsePage<CollectionResource>>() {
                }).getBody();
        assertThat(collections.getContent().size()).isZero();
    }

    @When("I click add to collection button without collection")
    public void iClickAddToCollectionButtonWithoutCollection() {
        exceptionResponse = restTemplate.exchange(
                "/api/collections/0/articles/" + articleResponse.getId(),
                HttpMethod.POST,
                new HttpEntity<>(null, headers),
                ExceptionResponse.class).getBody();
    }

    @Then("The system will return the error message {string}")
    public void theSystemWillReturnTheErrorMessage(String message) {
        assertThat(exceptionResponse.getMessage()).isEqualTo(message);
        cleanup();
    }


    private void cleanup() {
//        accountRepository.deleteAll();
        collectionRepository.deleteAll();
        articleRepository.deleteAll();
    }
}
