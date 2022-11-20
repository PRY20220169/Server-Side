package com.tp.pry20220169.cucumber.glue;

import com.tp.pry20220169.controller.ArticleSearchController;
import com.tp.pry20220169.domain.model.Author;
import com.tp.pry20220169.resource.*;
import com.tp.pry20220169.resource.security.AuthenticationResponse;
import com.tp.pry20220169.util.RestResponsePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ViewAuthorDetailsSteps {

    @Autowired
    TestRestTemplate restTemplate;

    ArticleResource articleResponse;

    RestResponsePage<ArticleResource> articlesFromSearch;

    AuthorResource authorResource;
    Author authorDetails;


    @Given("I am a user viewing an article's details")
    public void iAmAUserViewingAnArticleSDetails() {
        setArticle("network");
        setAuthor();
        addAuthorToArticle(articleResponse.getId());
        articleResponse = restTemplate.getForObject("/api/articles/" + articleResponse.getId(), ArticleResource.class);
    }

    private void addAuthorToArticle(Long articleId) {
        articleResponse = restTemplate.postForObject("/api/articles/" + articleId + "/authors/" + authorResource.getId(),
                null,
                ArticleResource.class);
    }

    private void setAuthor() {
        String randomString = UUID.randomUUID().toString();
        SaveAuthorResource newAuthor = SaveAuthorResource.builder()
                .firstName(randomString)
                .lastName(randomString)
                .address(randomString)
                .email(randomString + "@gmail.com")
                .organizations(List.of(randomString))
                .metrics(List.of(new SaveMetricResource(randomString, randomString, 2000, randomString)))
                .build();
        authorResource = restTemplate.postForObject("/api/authors", newAuthor, AuthorResource.class);
    }

    private void setArticle(String keyword) {
        String randomString = UUID.randomUUID().toString();
        SaveArticleResource newArticleRequest = SaveArticleResource.builder()
                .title(randomString)
                .conferenceName(randomString)
                .journalName(randomString)
                .publicationDate(new Date())
                .description(randomString)
                .keywords(List.of(keyword))
                .categories(List.of(randomString))
                .numberOfCitations(2)
                .numberOfReferences(2)
                .build();
        articleResponse = restTemplate.postForObject("/api/articles", newArticleRequest, ArticleResource.class);
    }

    @When("I click the name of the author")
    public void iClickTheNameOfTheAuthor() {
        authorDetails = articleResponse.getAuthors().get(0);
    }

    @Given("I am a user who has performed an article search by {string}")
    public void iAmAUserWhoHasPerformedAnArticleSearchByKeyword(String keyword) {
        setArticle(keyword);
        ArticleSearchController.KeywordsWrapper keywordsWrapper = new ArticleSearchController.KeywordsWrapper();
        keywordsWrapper.setKeywords(List.of(keyword));
        articlesFromSearch = restTemplate.exchange(
                "/api/articles/search/keywords",
                HttpMethod.POST,
                new HttpEntity<>(keywordsWrapper, null),
                new ParameterizedTypeReference<RestResponsePage<ArticleResource>>() {
                }).getBody();
        articleResponse = articlesFromSearch.getContent().get(0);
        setAuthor();
        addAuthorToArticle(articleResponse.getId());
    }

    @When("I click the author's name from a result")
    public void iClickTheAuthorSNameFromAResult() {
        authorDetails = articleResponse.getAuthors().get(0);
    }

    @Then("The system returns the author's information")
    public void theSystemReturnsTheAuthorSInformation() {
        assertThat(authorDetails.getFirstName()).isNotNull();
    }

}
