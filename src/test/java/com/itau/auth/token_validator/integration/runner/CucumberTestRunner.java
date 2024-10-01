package com.itau.auth.token_validator.integration.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.itau.auth.token_validator.integration.steps",
        plugin = {"pretty", "html:target/cucumber-html-reports", "json:target/cucumber.json"}
)
public class CucumberTestRunner {
}
