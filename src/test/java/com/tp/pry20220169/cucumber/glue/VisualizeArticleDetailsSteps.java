package com.tp.pry20220169.cucumber.glue;

import com.tp.pry20220169.controller.ArticleSearchController;
import com.tp.pry20220169.domain.model.Author;
import com.tp.pry20220169.resource.ArticleResource;
import com.tp.pry20220169.resource.AuthorResource;
import com.tp.pry20220169.resource.SaveArticleResource;
import com.tp.pry20220169.util.RestResponsePage;
import io.cucumber.java.en.And;
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

public class VisualizeArticleDetailsSteps {

    @Autowired
    TestRestTemplate restTemplate;

    ArticleResource articleResponse;

    RestResponsePage<ArticleResource> articlesFromSearch;

    ArticleResource articleDetails;

    final String keyword = "mobile";

    @Given("I am a user who has performed a search")
    public void iAmAUserWhoHasPerformedASearch() {
        setArticle(keyword);
        ArticleSearchController.KeywordsWrapper keywordsWrapper = new ArticleSearchController.KeywordsWrapper();
        keywordsWrapper.setKeywords(List.of(keyword));
        articlesFromSearch = restTemplate.exchange(
                "/api/articles/search/keywords",
                HttpMethod.POST,
                new HttpEntity<>(keywordsWrapper, null),
                new ParameterizedTypeReference<RestResponsePage<ArticleResource>>() {
                }).getBody();
    }

    @And("The system returns a list with articles")
    public void theSystemReturnsAListWithArticles() {
        assertThat(articlesFromSearch.getSize()).isGreaterThan(0);
    }

    @When("I click on an article from the list with")
    public void iClickOnAnArticleFromTheListWith() {
        articleDetails = articlesFromSearch.getContent().get(0);
    }

    @Then("The system returns the article details")
    public void theSystemReturnsTheArticleDetails() {
        assertThat(articleDetails.getDescription()).isNotBlank();
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

}
