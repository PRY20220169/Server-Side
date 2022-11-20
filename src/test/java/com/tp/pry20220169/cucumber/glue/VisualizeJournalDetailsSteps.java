package com.tp.pry20220169.cucumber.glue;

import com.tp.pry20220169.controller.ArticleSearchController;
import com.tp.pry20220169.domain.model.Journal;
import com.tp.pry20220169.resource.ArticleResource;
import com.tp.pry20220169.resource.JournalResource;
import com.tp.pry20220169.resource.SaveArticleResource;
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

public class VisualizeJournalDetailsSteps {

    @Autowired
    TestRestTemplate restTemplate;

    ArticleResource articleResponse;

    RestResponsePage<ArticleResource> articlesFromSearch;

    Journal journalDetails;
    
    String articleInfo = UUID.randomUUID().toString();

    String journalName = "journal 1";

    final String keyword = "mobile";

    @Given("that I have searched for articles")
    public void thatIHaveSearchedForArticles() {
        setArticle();
        ArticleSearchController.KeywordsWrapper keywordsWrapper = new ArticleSearchController.KeywordsWrapper();
        keywordsWrapper.setKeywords(List.of(keyword));
        articlesFromSearch = restTemplate.exchange(
                "/api/articles/search/keywords",
                HttpMethod.POST,
                new HttpEntity<>(keywordsWrapper, null),
                new ParameterizedTypeReference<RestResponsePage<ArticleResource>>() {
                }).getBody();
        articleResponse = articlesFromSearch.getContent().stream().filter(a -> a.getTitle().equals(articleInfo)).findFirst().get();
    }

    @When("I click on the journal name from on one of the results")
    public void iClickOnTheJournalNameFromOnOneOfTheResults() {
        journalDetails = articleResponse.getJournal();
    }

    @Then("The system will return the Journal Detail")
    public void theSystemWillReturnTheJournalDetail() {
        assertThat(journalDetails.getName()).isEqualTo(journalName);
    }

    @Given("that I have searched for an article")
    public void thatIHaveSearchedForAnArticle() {
        setArticle();
        ArticleSearchController.KeywordsWrapper keywordsWrapper = new ArticleSearchController.KeywordsWrapper();
        keywordsWrapper.setKeywords(List.of(keyword));
        articlesFromSearch = restTemplate.exchange(
                "/api/articles/search/keywords",
                HttpMethod.POST,
                new HttpEntity<>(keywordsWrapper, null),
                new ParameterizedTypeReference<RestResponsePage<ArticleResource>>() {
                }).getBody();
        articleResponse = articlesFromSearch.getContent().stream().filter(a -> a.getTitle().equals(articleInfo)).findFirst().get();

    }

    @When("I click on Journal Name from the article's details")
    public void iClickOnJournalNameFromTheArticleSDetails() {
        journalDetails = articleResponse.getJournal();
    }

    private void setArticle() {
        SaveArticleResource newArticleRequest = SaveArticleResource.builder()
                .title(articleInfo)
                .conferenceName(articleInfo)
                .journalName(journalName)
                .publicationDate(new Date())
                .description(articleInfo)
                .keywords(List.of(keyword))
                .categories(List.of(articleInfo))
                .numberOfCitations(2)
                .numberOfReferences(2)
                .build();
        articleResponse = restTemplate.postForObject("/api/articles", newArticleRequest, ArticleResource.class);
    }

}
