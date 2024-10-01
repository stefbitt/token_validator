Feature: Claims validation via real REST API

  Scenario: Validate claims successfully
    Given a valid JWT token
    When the token is sent
    Then the response status should be 200
    And the response should contain "true"

  Scenario: Validate claims with invalid token
    Given an invalid JWT token
    When the token is sent
    Then the response status should be 400
    And the response should contain "false"

  Scenario: Validate claims when name contains numbers
    Given a JWT token with a name claim containing numbers
    When the token is sent
    Then the response status should be 400
    And the response should contain "false"

  Scenario: Validate claims when there are more than 3 claims
    Given a JWT token with more than 3 claims
    When the token is sent
    Then the response status should be 400
    And the response should contain "false"

  Scenario: Validate claims when Seed is not a prime number
    Given a JWT token with Seed a not prime number
    When the token is sent
    Then the response status should be 400
    And the response should contain "false"

  Scenario: Validate claims when name more 256 Characters
    Given a JWT token with name more 256 Characters
    When the token is sent
    Then the response status should be 400
    And the response should contain "false"