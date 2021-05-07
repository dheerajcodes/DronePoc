package com.drone.poc.stepDefinitions;

import com.drone.poc.context.ScenarioContext;
import com.drone.poc.exceptions.TestDataNotFoundException;
import io.cucumber.spring.CucumberContextConfiguration;
import junit.framework.TestCase;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

@CucumberContextConfiguration
@SpringBootTest
@TestPropertySource("classpath:test.data.properties")
public class CucumberStepDefinition extends TestCase {
    @Autowired
    @Getter
    private ScenarioContext scenarioContext;

    @Autowired
    @Getter
    private Environment environment;

    protected String getTestData(String testDataKey) {
        String value = environment.getProperty(testDataKey);
        if (value == null) throw new TestDataNotFoundException(testDataKey);
        return value;
    }
}
