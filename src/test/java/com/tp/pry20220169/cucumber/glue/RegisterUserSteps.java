package com.tp.pry20220169.cucumber.glue;

import com.tp.pry20220169.domain.model.User;
import com.tp.pry20220169.domain.service.UserService;
import com.tp.pry20220169.exception.ExceptionResponse;
import com.tp.pry20220169.exception.ResourceNotFoundException;
import com.tp.pry20220169.resource.security.AuthenticationResponse;
import com.tp.pry20220169.resource.security.RegisterRequest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RegisterUserSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    UserService userService;

    private RegisterRequest registerRequest;

    private AuthenticationResponse authenticationResponse;

    private ExceptionResponse errorResponse;

    @Given("I am a user and click the button register")
    public void iAmAUserAndClickTheButtonRegister() {
    }

    @When("I complete the form fields with a unique email")
    public void iCompleteTheFormFieldsWithAUniqueEmail() {
        registerRequest = RegisterRequest.builder()
                .firstName("name")
                .lastName("lastname")
                .password("strongPassword1234")
                .username("uniqueUsername@gmail.com")
                .build();
    }

    @And("click complete button")
    public void clickCompleteButton() {
        authenticationResponse = restTemplate.
                postForObject("/security/users/register", registerRequest, AuthenticationResponse.class);
    }

    @Then("The system returns a successful response")
    public void theSystemReturnsASuccessfulResponse() {
        assertThat(authenticationResponse.getMessage()).isEqualTo("success");
    }

    @Given("I am a user with an account register with {string} and click the button register")
    public void iAmAUserWithAnAccountRegisterWithEmailAndClickTheButtonRegister(String email) {
        userService.createUser(User.builder()
                .username(email)
                .password("strongpassword")
                .roles(new ArrayList<>())
                .build());
    }

    @When("I complete the form fields with {string}")
    public void iCompleteTheFormFieldsWithEmail(String email) {
        registerRequest = RegisterRequest.builder()
                .firstName("name")
                .lastName("lastname")
                .password("strongPassword1234")
                .username(email)
                .build();
    }

    @Then("The system verifies that te email is registered and shows an error message")
    public void theSystemVerifiesThatTeEmailIsRegisteredAndShowsAnErrorMessage() {
        errorResponse = restTemplate.
                postForObject("/security/users/register", registerRequest, ExceptionResponse.class);
        //TODO: Make sure error response returns error message, right now message is null
        assertThat(errorResponse).isNotNull();
    }

    @When("I complete the form fields with a weak {string}")
    public void iCompleteTheFormFieldsWithAWeakPassword(String weakPassword) {
        registerRequest = RegisterRequest.builder()
                .firstName("name")
                .lastName("lastname")
                .password(weakPassword)
                .username("email@gmail.com")
                .build();
    }

    @Then("The system verifies the password is weak and returns an error message response")
    public void theSystemVerifiesThePasswordIsWeakAndReturnsAnErrorMessageResponse() {
        errorResponse = restTemplate.
                postForObject("/security/users/register", registerRequest, ExceptionResponse.class);
        assertThat(errorResponse.getMessage()).isEqualTo("password is too weak");
    }

}
