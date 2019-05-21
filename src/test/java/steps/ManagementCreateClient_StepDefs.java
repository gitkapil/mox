package steps;

import com.google.common.collect.Sets;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class ManagementCreateClient_StepDefs extends UtilManager {
    TestContext testContext;
    ManagementCommon common;
    private static final Set<String> ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    final static Logger logger = Logger.getLogger(ManagementCreateClient_StepDefs.class);

    public ManagementCreateClient_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(this.testContext);
    }

    @When("^I create a new client with a unique name and grant url$")
    public void createNewClient() {
        testContext.getApiManager().getCreateClient().setClientName(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getCreateClient().setGrantUrl("http://localhost");
        makeRequest();
    }

    @When("^I create a new client with name as \"([^\"]*)\" and grant url as \"([^\"]*)\"$")
    public void setNameAndUrl(String name, String url) {
        testContext.getApiManager().getCreateClient().setClientName(name);
        testContext.getApiManager().getCreateClient().setGrantUrl(url);
        makeRequest();
    }

    @And("^I create a new client with the same name and grant url$")
    public void newClient() {
        testContext.getApiManager().getCreateClient().setGrantUrl("http://localhost");
        makeRequest();
    }

    @And("^I retrieve the application name$")
    public void updateApplicationName() {
        HashMap returnResponse = testContext.getApiManager().getCreateClient().getResponse().path(".");
        String applicationName = (String)returnResponse.get("name");
        testContext.getApiManager().getCreateClient().setClientName(applicationName);
    }

    public String getClientId() {
        HashMap returnResponse = testContext.getApiManager().getCreateClient().getResponse().path(".");
        return (String)returnResponse.get("clientId");
    }

    @Then("^the create response will return http status \"([^\"]*)\" with error code \"([^\"]*)\" and description \"([^\"]*)\"$")
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


    @Then("^the create application response should be successful$")
    public void successResponse() {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getCreateClient().getResponse()),
                201,"Request was not successful!");

        String[] predefinedSet = {
                "name",
                "clientId",
                "redirectUris",
                "grantUrl"
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

    @Given("^I am logging in as a user with AAD role$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getCreateClient().setAuthTokenWithBearer(token));
    }

    public void makeRequest() {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/clients";
        testContext.getApiManager().getCreateClient().makeRequest(url);
    }
}
