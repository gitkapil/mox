package steps;

import com.google.common.collect.Sets;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.*;

public class ManagementCreateClientPassword_StepDefs extends UtilManager {
    TestContext testContext;
    ManagementCommon common;
    private static final Set<String> ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    final static Logger logger = Logger.getLogger(ManagementCreateClient_StepDefs.class);
    ManagementCreateClient_StepDefs createClient_stepDefs;
    ManagementPostApplications_StepDefs postApplications_stepDefs;
    ManagementGetApplications_StepDefs getApplications_stepDefs;

    public ManagementCreateClientPassword_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(this.testContext);
        createClient_stepDefs = new ManagementCreateClient_StepDefs(this.testContext);
        postApplications_stepDefs = new ManagementPostApplications_StepDefs(this.testContext);
        getApplications_stepDefs = new ManagementGetApplications_StepDefs(this.testContext);
    }

    @When("^I set the application id as \"([^\"]*)\"$")
    public void setApplicationId(String value) {
        testContext.getApiManager().getCreateClientPassword().setApplicationId(value);
    }

    @When("^I get a list of applications$")
    public void getListOfApplications() {
        getApplications_stepDefs.list_of_applications_without_any_filters();
    }

    @And("^I get the first application id$")
    public void getFirstApplicationId() {
        String applicationId = "";

        Response response = testContext.getApiManager().getGetApplication().getResponse();
        ArrayList<HashMap> items = response.path("items");
        HashMap item = items.get(0);
        applicationId = (String)item.get("applicationId");

        testContext.getApiManager().getCreateClientPassword().setApplicationId(applicationId);
    }

    @And("^I set create password data with activate at \"([^\"]*)\", deactivate at \"([^\"]*)\", and description \"([^\"]*)\"$")
    public void setData(String activateAt, String deactivateAt, String description) {
        testContext.getApiManager().getCreateClientPassword().setActivateAt(activateAt);
        testContext.getApiManager().getCreateClientPassword().setDeactivateAt(deactivateAt);
        testContext.getApiManager().getCreateClientPassword().setDescription(description);
    }

    @Then("^I should have an error with status \"([^\"]*)\", error code as \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void failResponse(String httpStatus, String errorCode, String errorDescription) {
        Assert.assertEquals(
                Integer.parseInt(httpStatus),
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getCreateClient().getResponse()),
                "Expected http status " + httpStatus + " but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getCreateClient().getResponse())
        );

        Assert.assertEquals(
                errorCode,
                getRestHelper().getErrorCode(testContext.getApiManager().getCreateClient().getResponse()),
                "Expected error code " + errorCode + " but got " +
                        getRestHelper().getErrorCode(testContext.getApiManager().getCreateClient().getResponse())
        );

        if (!getRestHelper().getErrorDescription(testContext.getApiManager().getCreateClient().getResponse()).contains(errorDescription)) {
            Assert.assertEquals(
                    errorDescription,
                    getRestHelper().getErrorDescription(testContext.getApiManager().getCreateClient().getResponse()),
                    "Expected error description " + errorDescription + " but got " +
                            getRestHelper().getErrorDescription(testContext.getApiManager().getCreateClient().getResponse())
            );
        }
    }

    @When("^I create a new AAD client$")
    public void createNewClient() {
        createClient_stepDefs.createNewClient();
    }

    @Given("^I am logging in as a user with AAD Password role$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getCreateClientPassword().setAuthTokenWithBearer(token));
    }

    @And("^I get the client id from the response$")
    public void getNewClientClientId() {
        testContext.getApiManager().getCreateClientPassword().setClientId(createClient_stepDefs.getClientId());
    }

    @And("^I create an application with that client id$")
    public void createApplicationId() {
        postApplications_stepDefs.i_have_a_clientId_from_an_existing_AAD_application(
                testContext.getApiManager().getCreateClientPassword().getClientId()
        );
        postApplications_stepDefs.i_make_a_post_request_to_the_application_endpoint();
    }

    @And("^I get the application id from the response$")
    public void getApplicationIdFromNewApplication() {
        HashMap keys = testContext.getApiManager().getPostApplication().getPostApplicationRequestResponse().path(".");
        String applicationId = (String)keys.get("applicationId");
        testContext.getApiManager().getCreateClientPassword().setApplicationId(applicationId);
    }

    @And("^I create a new AAD password$")
    public void createPassword() {
        testContext.getApiManager().getCreateClientPassword().setActivateAt("2019-01-01T00:00:00Z");
        testContext.getApiManager().getCreateClientPassword().setDeactivateAt("2019-02-02T00:00:00Z");
        testContext.getApiManager().getCreateClientPassword().setDescription("test");
        makeRequest();
    }

    @Then("^the password response should be successful$")
    public void successResponse() {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getCreateClient().getResponse()),
                201,"Request was not successful!");

        String[] predefinedSet = {
                "keyId",
                "keyName",
                "applicatioId",
                "value",
                "activateAt",
                "deactivateAt",
                "description",
                "createdAt",
                "lastUpdatedAt"
        };

        HashMap returnResponse = testContext.getApiManager().getCreateClient().getResponse().path(".");
        Set<String> keySet = returnResponse.keySet();
        Collection<String> diff = CollectionUtils.disjunction(Arrays.asList(predefinedSet), keySet);

        if (diff.size() == 0) {
        } else {
            Assert.assertEquals(true, false,
                    "Returned object contain fields that are not a subset (" +
                            String.join(",", diff) + ")");
        }
    }

    public void makeRequest() {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/applications/" +
                testContext.getApiManager().getCreateClientPassword().getApplicationId() + "/keys/passwords";
        testContext.getApiManager().getCreateClientPassword().makeRequest(url);
    }
}
