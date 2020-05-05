package steps;

import com.jayway.restassured.response.Response;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Constants;

public class CreateEmployees_stepDefs extends UtilManager {

    TestContext testContext;
    Response response;
    private static final String RESOURCE_ENDPOINT = "base_uri";

    public CreateEmployees_stepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    final static Logger logger = Logger.getLogger(CreateEmployees_stepDefs.class);

    @When("^I make a request to create the detail of new employee with name \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void iMakeRequestToCreateNewEmployee(String name, String salary, String age) {
        testContext.getApiManager().createEmployee().setName(name);
        testContext.getApiManager().createEmployee().setSalary(salary);
        testContext.getApiManager().createEmployee().setAge(age);
        String url = getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT) + "/" + "create";
        testContext.getApiManager().createEmployee().makeRequest(url);
    }

    @Then("^I should receive a \"([^\"]*)\" response code create employee$")
    public void iShouldReceiveTheStatusCode(int statusCode) {
        response = testContext.getApiManager().createEmployee().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().createEmployee().getResponse()),
                statusCode, "Different HTTP response code being returned");
        testContext.getApiManager().createEmployee().setId(response.path("data.id").toString());
    }

    @Then("^response should have correct values$")
    public void responseShouldHaveCorrectValues() {
        response = testContext.getApiManager().createEmployee().getResponse();
        Assert.assertEquals(response.path(Constants.STATUS), "success");
        Assert.assertEquals(response.path("data.name"), testContext.getApiManager().createEmployee().getName(), "name should be same as provided in input body");
        Assert.assertEquals(response.path("data.salary"), testContext.getApiManager().createEmployee().getSalary(), "salary should be same as provided in input body");
        Assert.assertEquals(response.path("data.age"), testContext.getApiManager().createEmployee().getAge(), "age should be same as provided in input body");
        Assert.assertNotNull(response.path("data.id"), "Id should not be null");
    }
}
