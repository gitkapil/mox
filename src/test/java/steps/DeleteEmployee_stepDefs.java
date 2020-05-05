package steps;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.testng.Assert;

public class DeleteEmployee_stepDefs extends UtilManager {

    TestContext testContext;
    Response response;
    private static final String RESOURCE_ENDPOINT = "base_uri";

    public DeleteEmployee_stepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    final static Logger logger = Logger.getLogger(DeleteEmployee_stepDefs.class);

    @When("^I make a request to delete the employee details with employee Id \"([^\"]*)\"$")
    public void iMakeRequestToDeleteEmployeeWithId(String employeeId) {
        String url = getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                RESOURCE_ENDPOINT) + "/delete/" + testContext.getApiManager().createEmployee().getId();
        testContext.getApiManager().deleteEmployee().makeRequest(url);
    }

    @Then("^I should receive a \"([^\"]*)\" response code and correct for delete employee$")
    public void iShouldReceiveResponseCodeForDeleteEmployee(int statusCode) {
        response = testContext.getApiManager().deleteEmployee().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().deleteEmployee().getResponse()),
                statusCode, "Different HTTP response code being returned");
        Assert.assertNotNull(response.path("status"), "status should not be null");
        Assert.assertNotNull(response.path("message"), "message should be there");
    }


}
