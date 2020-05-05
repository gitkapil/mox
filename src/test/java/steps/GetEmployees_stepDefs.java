package steps;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Constants;
import java.util.HashMap;
import java.util.List;

public class GetEmployees_stepDefs extends UtilManager {

    TestContext testContext;
    Response response;
    private static final String RESOURCE_ENDPOINT = "base_uri";

    public GetEmployees_stepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    final static Logger logger = Logger.getLogger(GetEmployees_stepDefs.class);

    @When("^I make a request to get the details of all employees$")
    public void makeRequestToThirdPartyHTTPApis() {
        String url = getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT) + "/employees";
        testContext.getApiManager().getEmployeeDetails().makeRequest(url);
    }

    @Then("^I should receive a \"([^\"]*)\" response for employees$")
    public void iShouldReceiveResponseForEmployees(int statusCode) {
        response = testContext.getApiManager().getEmployeeDetails().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getEmployeeDetails().getResponse()),
                statusCode, "Different HTTP response code being returned");
        List<HashMap> items = response.path(Constants.DATA);
        for (int i = 0; i <= items.size() - 1; i++) {
            Assert.assertNotNull(items.get(i).get(Constants.ID), "Id is not present in data");
            Assert.assertNotNull(items.get(i).get("employee_name"), "name is not present in data");
            Assert.assertNotNull(items.get(i).get("employee_salary"), "salary is not present in data");
            Assert.assertNotNull(items.get(i).get("employee_age"), "age is not present in data");
            Assert.assertNotNull(items.get(i).get("profile_image"), "image is not present in data");

        }
    }
}
