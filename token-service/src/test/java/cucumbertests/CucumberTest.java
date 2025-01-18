package cucumbertests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = "summary", publish = false, features = "features", snippets = CucumberOptions.SnippetType.CAMELCASE)
public class CucumberTest {
}
