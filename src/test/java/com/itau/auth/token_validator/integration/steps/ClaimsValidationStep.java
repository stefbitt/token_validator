package com.itau.auth.token_validator.integration.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
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

    @Given("a JWT token with Seed a not prime number")
    public void a_jwt_token_with_seed_a_not_prime_number() {
        jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiNCIsIk5hbWUiOiJNYXJpYSBPbGl2aWEifQ.qxBPAEOf991Xly9YeRKkZzBl1ngVSl4G64bC0Z_jnmY";
    }

    @Given("a JWT token with name more 256 Characters")
    public void a_jwt_token_with_name_more_256_characters() {
        jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiMiIsIk5hbWUiOiJMb3JlbSBpcHN1bSBkb2xvciBzaXQgYW1ldC4gUXVpIGNvcnJ1cHRpIGRpc3RpbmN0aW8gc2l0IG9mZmljaWlzIGZ1Z2l0IGV1bSB2b2x1cHRhdGVtIGZhY2lsaXMuIFNpdCBhbWV0IG9tbmlzIGV0IGlzdGUgZWFydW0gdXQgaWxsdW0gZXhjZXB0dXJpIGVhIHByYWVzZW50aXVtIGlwc2FtLiBBYiByZXBlbGxhdCB0ZW1wb3JpYnVzIGhpYyBtb2xsaXRpYSB2ZW5pYW0gZXQgcGFyaWF0dXIgdm9sdXB0YXMgcXVpIG1vbGVzdGlhZSBpcHN1bSBlc3QgcmVydW0gYmxhbmRpdGlpcy4gQXQgcmVwZWxsZW5kdXMgdWxsYW0gZXN0IGVhcXVlIGFtZXQgYXV0IG5paGlsIGF1dGVtLiBJbiBhcGVyaWFtIGRvbG9ydW0gZXVtIGluY2lkdW50IGNvbnNlcXVhdHVyIHF1aSBzaW50IG51bXF1YW0hIFF1byB2ZXJpdGF0aXMgcGVyc3BpY2lhdGlzIGVzdCBoYXJ1bSBpbGxvIGF1dCBxdWlzIG5pc2kgcXVpIHN1bnQgZmFjaWxpcyBxdW8gcGFyaWF0dXIgcXVhZXJhdC4gUXVpIHF1aWRlbSBleGNlcHR1cmkgYXV0IHZvbHVwdGF0ZW0gcGVyc3BpY2lhdGlzIGN1bSBhbGlxdWFtIGRlYml0aXMgc2VkIGNvbW1vZGkgY29uc2VjdGV0dXIgdXQgcmF0aW9uZSBkb2xvcmVzIGF1dCB2b2x1cHRhcyBkb2xvcmVtLiBFdCBudWxsYSB0ZW1wb3JhIGVhIHN1c2NpcGl0IG1vbGVzdGlhcyBBdCBwYXJpYXR1ciBxdWFtIGV0IHF1YXMgb2RpdCBub24gZGlzdGluY3RpbyBlbmltLiBRdWkgcmVwcmVoZW5kZXJpdCBhdHF1ZSBhYiBwcmFlc2VudGl1bSBuaXNpIGF1dCBuZW1vIGRpc3RpbmN0aW8uIE5vbiBhbWV0IHNlcXVpIHV0IHBhcmlhdHVyIG1vbGVzdGlhZSBldCB2b2x1cHRhdHVtIHZlbmlhbSBzaXQgYWNjdXNhbXVzIHZvbHVwdGFzLiBBIG5lY2Vzc2l0YXRpYnVzIGFtZXQgdmVsIGlsbHVtIGFzc3VtZW5kYSB2ZWwgZW5pbSBtYWlvcmVzIHV0IHJlcnVtIGFsaWFzIGV0IG1heGltZSByZXBlbGxlbmR1cyBlYSBkb2xvciBzYXBpZW50ZT8ifQ.oZ5RVkn-21Pmk-7tOsKQJYG2VEGOrY60zGfhO9qF6oY";
    }

    @When("the token is sent")
    public void the_token_is_sent() {
        String url = "http://localhost:" + port + END_POINT;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

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

