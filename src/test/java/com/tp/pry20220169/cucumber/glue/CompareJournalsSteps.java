package com.tp.pry20220169.cucumber.glue;

import com.tp.pry20220169.controller.AuthorCompare;
import com.tp.pry20220169.controller.JournalCompare;
import com.tp.pry20220169.domain.repository.AuthorRepository;
import com.tp.pry20220169.domain.repository.JournalRepository;
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
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CompareJournalsSteps {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    JournalRepository journalRepository;

    JournalCompare.IdListWrapper compareList = new JournalCompare.IdListWrapper();

    RestResponsePage<JournalResource> journals;

    @Given("I have added journals to the compare list")
    public void iHaveAddedJournalsToTheCompareList() {
        compareList.setIds(new ArrayList<>());
        for (int i = 0; i < 2; i++) {
            String randomString = UUID.randomUUID().toString();
            SaveJournalResource newJournal = SaveJournalResource.builder()
                    .name(randomString)
                    .metrics(List.of(new SaveMetricResource(randomString, randomString, 2000, randomString)))
                    .build();
            AuthorResource response = restTemplate.postForObject("/api/journals", newJournal, AuthorResource.class);
            compareList.getIds().add(response.getId());
        }
    }

    @When("I click the compare journals button")
    public void iClickTheCompareJournalsButton() {
        journals = restTemplate.exchange(
                "/api/journals/compare",
                HttpMethod.POST,
                new HttpEntity<>(compareList, null),
                new ParameterizedTypeReference<RestResponsePage<JournalResource>>() {
                }).getBody();
    }

    @Then("The system returns compared journals")
    public void theSystemReturnsComparedJournals() {
        assertThat(journals.getContent().size()).isEqualTo(2);
        journalRepository.deleteAll();
    }
}
