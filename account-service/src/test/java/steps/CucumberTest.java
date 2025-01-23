package steps;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        publish = false,
        features = "features",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class CucumberTest {
    // This will run all the Cucumber tests in the project
}