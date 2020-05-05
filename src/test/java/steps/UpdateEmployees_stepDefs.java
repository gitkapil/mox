package steps;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Constants;

public class UpdateEmployees_stepDefs extends UtilManager {

    TestContext testContext;
    Response response;
    private static final String RESOURCE_ENDPOINT = "base_uri";

    public UpdateEmployees_stepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    final static Logger logger = Logger.getLogger(UpdateEmployees_stepDefs.class);

    @When("^I make a request to update the detail of employee existing employee with name \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void iMakeRequestToUpdateExistingEmployeeDetailsById(String name, String salary, String age) {
        testContext.getApiManager().updateEmployeeDetails().setName(name);
        testContext.getApiManager().updateEmployeeDetails().setSalary(salary);
        testContext.getApiManager().updateEmployeeDetails().setAge(age);
        String url = getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT) + "/update/"+testContext.getApiManager().createEmployee().getId();
        testContext.getApiManager().updateEmployeeDetails().makeRequest(url);
    }

    @Then("^I should receive a \"([^\"]*)\" response code update employee$")
    public void iShouldReceiveTheStatusCode(int statusCode) {
        response = testContext.getApiManager().updateEmployeeDetails().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().updateEmployeeDetails().getResponse()), statusCode, "Different HTTP response code being returned");
        Assert.assertEquals(response.path(Constants.STATUS),"success");
    }


}
