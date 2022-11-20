package com.tp.pry20220169.cucumber.glue;

import com.tp.pry20220169.controller.ArticleCompare;
import com.tp.pry20220169.domain.repository.ArticleRepository;
import com.tp.pry20220169.resource.ArticleResource;
import com.tp.pry20220169.resource.SaveArticleResource;
import com.tp.pry20220169.util.RestResponsePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CompareArticlesSteps {

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    ArticleRepository articleRepository;
    ArticleCompare.IdListWrapper compareList = new ArticleCompare.IdListWrapper();
    RestResponsePage<ArticleResource> articles;

    @Given("I have added articles to the compare list")
    public void iHaveAddedArticlesToTheCompareList() {
        compareList.setIds(new ArrayList<>());
        for (int i = 0; i < 2; i++) {
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
            ArticleResource response = restTemplate.postForObject("/api/articles", newArticleRequest, ArticleResource.class);
            compareList.getIds().add(response.getId());
        }

    }

    @When("I click the compare articles button")
    public void iClickTheCompareArticlesButton() {
        articles = restTemplate.exchange(
                "/api/articles/compare",
                HttpMethod.POST,
                new HttpEntity<>(compareList, null),
                new ParameterizedTypeReference<RestResponsePage<ArticleResource>>() {
                }).getBody();
    }

    @Then("The system returns compared articles")
    public void theSystemReturnsComparedArticles() {
        assertThat(articles.getContent().size()).isEqualTo(2);
        articleRepository.deleteAll();
    }

}
