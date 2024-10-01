package com.itau.auth.token_validator.integration.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClaimsValidationStep {

    @Autowired
    private RestTemplate restTemplate;

    private String jwtToken;

    private ResponseEntity<String> response;

    @LocalServerPort
    private int port;

    private final String END_POINT = "/api/v1/token/validate";

    @Given("a valid JWT token")
    public void a_valid_jwt_token() {
        jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";
    }

    @Given("an invalid JWT token")
    public void an_invalid_jwt_token() {
        jwtToken = "eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";
    }

    @Given("a JWT token with a name claim containing numbers")
    public void a_jwt_token_with_a_name_claim_containing_numbers() {
        jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs";
    }

    @Given("a JWT token with more than 3 claims")
    public void a_jwt_token_with_more_than_3_claims() {
        jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiTWVtYmVyIiwiT3JnIjoiQlIiLCJTZWVkIjoiMTQ2MjciLCJOYW1lIjoiVmFsZGlyIEFyYW5oYSJ9.cmrXV_Flm5mfdpfNUVopY_I2zeJUy4EZ4i3Fea98zvY";
    }

    @When("the token is sent")
    public void the_token_is_sent() {
        String url = "http://localhost:" + port + END_POINT;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(jwtToken, headers);

        try {
            response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        } catch (HttpClientErrorException e) {
            response = ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int status) {
        assertThat(response.getStatusCodeValue()).isEqualTo(status);
    }

    @Then("the response should contain {string}")
    public void the_response_should_contain(String expectedContent) {
        assertThat(response.getBody()).contains(expectedContent);
    }
}

