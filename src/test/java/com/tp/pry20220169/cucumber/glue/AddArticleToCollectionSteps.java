package com.tp.pry20220169.cucumber.glue;

import com.tp.pry20220169.resource.*;
import com.tp.pry20220169.resource.security.AuthenticationRequest;
import com.tp.pry20220169.resource.security.AuthenticationResponse;
import com.tp.pry20220169.util.RestResponsePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Date;
import java.util.List;

public class AddArticleToCollectionSteps {

    @Autowired
    private TestRestTemplate restTemplate;


    private AuthenticationResponse userResponse;

    private ArticleResource articleResponse;

    private CollectionResource collectionResponse;

    private Page<CollectionResource> collectionList;

    @Given("I am a logged in user")
    public void iAmALoggedInUser() {
        AuthenticationRequest authRequest = AuthenticationRequest.builder()
                .username("diego")
                .password("1234")
                .build();
        userResponse = restTemplate.postForObject("/security/users/login", authRequest, AuthenticationResponse.class);
        restTemplate.postForObject("/api/users/" + userResponse.getId().toString() + "/account", null, AccountResource.class);
    }

    @And("I am on the articles details")
    public void iAmOnTheArticlesDetails() {
        createArticle();
    }

    private void createArticle() {
        SaveArticleResource newArticleRequest = SaveArticleResource.builder()
                .title("Article 2")
                .conferenceName("Conference 1")
                .journalName("Journal 1")
                .publicationDate(new Date())
                .description("Description of Article 1")
                .keywords(List.of("Innovation", "Machine Learning"))
                .categories(List.of("Science", "Technology"))
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
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userResponse.getAccess_token());
        HttpEntity<?> requestEntity = new HttpEntity<>(null, headers);
        collectionResponse = restTemplate.postForObject(String.format("/api/users/%s/account/collections", userResponse.getId().toString()), collectionRequest, CollectionResource.class, headers);


        System.out.println("HERE");
        System.out.println(collectionResponse.getName());
        System.out.println(collectionResponse.getId());

        RestResponsePage<CollectionResource> response = restTemplate.exchange(
                String.format("/api/users/%s/account/collections", userResponse.getId().toString()),
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<RestResponsePage<CollectionResource>>() {
                }).getBody();
        response.getContent().forEach(it -> System.out.println(it.getName()));

    }


    @And("I click add to collection button")
    public void iClickAddToCollectionButton() {

    }

    @When("I select an existing collection")
    public void iSelectAnExistingCollection() {
    }

    @Then("The system will add the article to the selected collection")
    public void theSystemWillAddTheArticleToTheSelectedCollection() {
    }

    @And("The system will return the message {string}")
    public void theSystemWillReturnTheMessage(String message) {
    }

    @And("I don't have an existing collection")
    public void iDonTHaveAnExistingCollection() {
    }

    @Then("The system will return the error message {string}")
    public void theSystemWillReturnTheErrorMessage(String message) {
    }
}
