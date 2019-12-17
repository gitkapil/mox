package testSuite;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = {"pretty", "html:target/cucumber-html-report", "json:target/cucumber.json", "pretty:target/cucumber-pretty.txt"},

        glue = {"steps"},
        features = {"src/test/resources/features/PUT_Credentials.feature"},
        //,plugin = { "com.cucumber.listener.ExtentCucumberFormatter:target/cucumber-reports/report.html"}
        tags = {"@trial"}
)
public class Runner {

}