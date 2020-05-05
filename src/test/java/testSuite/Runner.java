package testSuite;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = {"pretty", "html:target/cucumber-html-report", "json:target/cucumber.json", "pretty:target/cucumber-pretty.txt"},
        glue = {"steps"},
        features = {"src/test/resources/features/"},
        monochrome = true,
       tags = {"@test"}
)
public class Runner {

}