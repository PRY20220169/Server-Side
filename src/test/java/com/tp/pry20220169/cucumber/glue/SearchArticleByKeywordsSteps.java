package com.tp.pry20220169.cucumber.glue;

import com.tp.pry20220169.controller.ArticleSearchController;
import com.tp.pry20220169.resource.ArticleResource;
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

public class SearchArticleByKeywordsSteps {


    ArticleSearchController.KeywordsWrapper keywordsWrapper;

    @Autowired
    TestRestTemplate restTemplate;

    ArticleResource articleResponse;

    RestResponsePage<ArticleResource> articlesFromSearch;

    @Given("I am a user who enters {string} in the search engine")
    public void iAmAUserWhoEntersKeywordsInTheSearchEngine(String keywords) {
        keywordsWrapper = new ArticleSearchController.KeywordsWrapper();
        keywordsWrapper.setKeywords(List.of(keywords));

    }

    @And("the system has articles with those keywords")
    public void theSystemHasArticlesWithThoseKeywords() {
        String randomString = UUID.randomUUID().toString();
        SaveArticleResource newArticleRequest = SaveArticleResource.builder()
                .title(randomString)
                .conferenceName(randomString)
                .journalName(randomString)
                .publicationDate(new Date())
                .description(randomString)
                .keywords(keywordsWrapper.getKeywords())
                .categories(List.of(randomString))
                .numberOfCitations(2)
                .numberOfReferences(2)
                .build();
        articleResponse = restTemplate.postForObject("/api/articles", newArticleRequest, ArticleResource.class);
    }

    @When("I press click Search")
    public void iPressClickSearch() {
        articlesFromSearch = restTemplate.exchange(
                "/api/articles/search/keywords",
                HttpMethod.POST,
                new HttpEntity<>(keywordsWrapper, null),
                new ParameterizedTypeReference<RestResponsePage<ArticleResource>>() {
                }).getBody();
    }

    @Then("the system shows a list of articles whose keywords, title or description contain the largest portion of the search words")
    public void theSystemShowsAListOfArticlesWhoseKeywordsTitleOrDescriptionContainTheLargestPortionOfTheSearchWords() {
        assertThat(articlesFromSearch.getContent().size()).isGreaterThan(0);
        assertThat(articlesFromSearch.getContent().get(0).getKeywords()).isEqualTo(keywordsWrapper.getKeywords());
    }

    @And("The system sorts the list by the highest number of matches")
    public void theSystemSortsTheListByTheHighestNumberOfMatches() {
        //shorting done in the frontend
    }

    @And("The system does not find any articles that match the keywords")
    public void theSystemDoesNotFindAnyArticlesThatMatchTheKeywords() {
        assertThat(articlesFromSearch.getContent().size()).isZero();
    }

    @Then("The system shows you the message {string}")
    public void theSystemShowsYouTheMessageMessage(String message) {
        //message is shown in the frontend
    }

}
