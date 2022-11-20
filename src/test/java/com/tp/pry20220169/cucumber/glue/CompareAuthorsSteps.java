package com.tp.pry20220169.cucumber.glue;

import com.tp.pry20220169.controller.ArticleCompare;
import com.tp.pry20220169.controller.AuthorCompare;
import com.tp.pry20220169.domain.repository.ArticleRepository;
import com.tp.pry20220169.domain.repository.AuthorRepository;
import com.tp.pry20220169.resource.*;
import com.tp.pry20220169.util.RestResponsePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CompareAuthorsSteps {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    AuthorRepository authorRepository;

    AuthorCompare.IdListWrapper compareList = new AuthorCompare.IdListWrapper();

    RestResponsePage<AuthorResource> authors;

    @Given("I have added two authors to the compare list")
    public void iHaveAddedTwoAuthorsToTheCompareList() {
        compareList.setIds(new ArrayList<>());
        for (int i = 0; i < 2; i++) {
            String randomString = UUID.randomUUID().toString();
            SaveAuthorResource newAuthor = SaveAuthorResource.builder()
                    .firstName(randomString)
                    .lastName(randomString)
                    .address(randomString)
                    .email(randomString + "@gmail.com")
                    .organizations(List.of(randomString))
                    .metrics(List.of(new SaveMetricResource(randomString, randomString, 2000, randomString)))
                    .build();
            AuthorResource response = restTemplate.postForObject("/api/authors", newAuthor, AuthorResource.class);
            compareList.getIds().add(response.getId());
        }
    }

    @When("I click the compare authors button")
    public void iClickTheCompareAuthorsButton() {
        authors = restTemplate.exchange(
                "/api/authors/compare",
                HttpMethod.POST,
                new HttpEntity<>(compareList, null),
                new ParameterizedTypeReference<RestResponsePage<AuthorResource>>() {
                }).getBody();
    }

    @Then("The system returns compared authors")
    public void theSystemReturnsComparedAuthors() {
        assertThat(authors.getContent().size()).isEqualTo(2);
        authorRepository.deleteAll();
    }
}
