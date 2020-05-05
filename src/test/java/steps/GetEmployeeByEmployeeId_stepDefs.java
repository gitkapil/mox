package steps;

import com.jayway.restassured.response.Response;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Constants;

public class GetEmployeeByEmployeeId_stepDefs extends UtilManager {

    TestContext testContext;
    Response response;
    private static final String RESOURCE_ENDPOINT = "base_uri";

    public GetEmployeeByEmployeeId_stepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    final static Logger logger = Logger.getLogger(GetEmployeeByEmployeeId_stepDefs.class);


    @Given("^I am an user with having access to APIs$")
    public void i_am_an_user_having_access_to_apis() {
        logger.info("we are not using access token");
    }

    @When("^I make a request to get the employee details with employee Id \"([^\"]*)\"$")
    public void iMakeRequestToGetEmployeeDetailsWithId(String employeeId) {

        String url = getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                RESOURCE_ENDPOINT) + "/employee/" + employeeId;

        testContext.getApiManager().getEmployeeDetails().makeRequest(url);
    }

    @Then("^I should receive a \"([^\"]*)\" response$")
    public void iShouldReceiveResponse(int statusCode) {
        response = testContext.getApiManager().getEmployeeDetails().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getEmployeeDetails().getResponse()),
                statusCode, "Different HTTP response code being returned");
        Assert.assertNotNull(response.path(Constants.ID), "Id is not present in data");
        Assert.assertNotNull(response.path("data.employee_name"), "name is not present in data");
        Assert.assertNotNull(response.path("data.employee_salary"), "salary is not present in data");
        Assert.assertNotNull(response.path("data.employee_age"), "age is not present in data");
        Assert.assertNotNull(response.path("data.profile_image"), "image is not present in data");
    }

}
