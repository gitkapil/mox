package steps;

import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Constants;

import java.sql.SQLOutput;
import java.util.*;

public class ManagementCreateClientPassword_StepDefs extends UtilManager {
    TestContext testContext;
    ManagementCommon common;
    private static final Set<String> ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String VALID_BASE64_ENCODED_RSA_PUBLIC_KEY = "valid_base64_encoded_rsa_public_key";
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
        testContext.getApiManager().getPostPasswordCreateClientPassword().setApplicationId(value);
    }

    @When("^I get a list of applications$")
    public void getListOfApplications() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET,
                token -> testContext.getApiManager().getPostApplication().setAuthTokenWithBearer(token));
        getApplications_stepDefs.list_of_applications_without_any_filters();
    }

    @And("^I get the first application id$")
    public void getFirstApplicationId() {
        String applicationId = "";

        Response response = testContext.getApiManager().getGetApplication().getResponse();
        ArrayList<HashMap> items = response.path("items");
        HashMap item = items.get(0);
        applicationId = (String) item.get("applicationId");

        testContext.getApiManager().getPostPasswordCreateClientPassword().setApplicationId(applicationId);
    }

    @And("^I have created password data with \"([^\"]*)\" and activate at \"([^\"]*)\", deactivate at \"([^\"]*)\" and \"([^\"]*)\"$")
    public void setData(String applicationId, String activateAt, String deactivateAt, String entityStatus) {
        testContext.getApiManager().getPostPasswordCreateClientPassword().setActivateAt(activateAt);
        testContext.getApiManager().getPostPasswordCreateClientPassword().setDeactivateAt(deactivateAt);
        testContext.getApiManager().getPostPasswordCreateClientPassword().setApplicationId(applicationId);
        testContext.getApiManager().getPostPasswordCreateClientPassword().setEntityStatus(entityStatus);
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().getPostPasswordCreateClientPassword().getApplicationId() + "/keys/passwords";
        makeRequest(url);
    }

    @Then("^I should have an error with status \"([^\"]*)\", error code as \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void failResponse(String httpStatus, String errorCode, String errorDescription) {
        Assert.assertEquals(
                Integer.parseInt(httpStatus),
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse()),
                "Expected http status " + httpStatus + " but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse())
        );

        Assert.assertEquals(
                errorCode,
                getRestHelper().getErrorCode(testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse()),
                "Expected error code " + errorCode + " but got " +
                        getRestHelper().getErrorCode(testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse())
        );

        if (!getRestHelper().getErrorDescription(testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse()).contains(errorDescription)) {
            Assert.assertEquals(
                    errorDescription,
                    getRestHelper().getErrorDescription(testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse()),
                    "Expected error description " + errorDescription + " but got " +
                            getRestHelper().getErrorDescription(testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse())
            );
        }
    }

    @When("^I create a new AAD client$")
    public void createNewClient() {
        createClient_stepDefs.login();
        createClient_stepDefs.createNewClient();
    }

    @Given("^I am logging in as a user with AAD Password role$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPostPasswordCreateClientPassword().setAuthTokenWithBearer(token));
    }

    @And("^I get the client id from the response$")
    public void getNewClientClientId() {
        testContext.getApiManager().getPostPasswordCreateClientPassword().setClientId(createClient_stepDefs.getClientId());
    }

    @And("^I create an application with that client id$")
    public void createApplicationId() {
        postApplications_stepDefs.i_am_an_authorized_DRAGON_user_with_role();
        postApplications_stepDefs.i_have_a_clientId_from_an_existing_AAD_application(testContext.getApiManager().getPostPasswordCreateClientPassword().getClientId());
        postApplications_stepDefs.i_have_a_peakId_subUnitId_and_organisationId_from_an_existing_PM4B_merchant_identity(
                getGeneral().generateUniqueUUID(), getGeneral().generateUniqueUUID(), getGeneral().generateUniqueUUID(),
                "Test"
        );
        postApplications_stepDefs.i_make_a_post_request_to_the_application_endpoint();
    }

    @And("^I get the application id from the response$")
    public void getApplicationIdFromNewApplication() {
        HashMap keys = testContext.getApiManager().getPostApplication().getPostApplicationRequestResponse().path(".");
        String applicationId = (String) keys.get("applicationId");
        testContext.getApiManager().getPostPasswordCreateClientPassword().setApplicationId(applicationId);
    }

    @And("^I create a new AAD password with null header \"([^\"]*)\"$")
    public void createPassword(String nullHeaderValues) {
        testContext.getApiManager().getPostPasswordCreateClientPassword().setActivateAt("2019-01-01T00:00:00Z");
        testContext.getApiManager().getPostPasswordCreateClientPassword().setDeactivateAt("2032-02-02T00:00:00Z");
        testContext.getApiManager().getPostPasswordCreateClientPassword().setApplicationId("9b6e98a4-32ed-493d-88fe-795e60b06d68");
        makeRequestWithNullHeaderValue(nullHeaderValues);
    }

    @Then("^I should see the response from post password request$")
    public void postPasswordResponse() {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse()),
                HttpStatus.SC_CREATED, "Request was not successful!");
    }

    @Then("^validate the response from post password request$")
    public void validatePasswordResponse() {
        Assert.assertNotNull(testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse().path(Constants.PDF_PIN));
        Assert.assertNotNull(testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse().path(Constants.PDF_URL));

        HashMap returnResponse = testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse().path("passwordMetaData");

        Assert.assertEquals(testContext.getApiManager().getPostPasswordCreateClientPassword().
                getApplicationId(), returnResponse.get(Constants.APPLICATION_ID), "application id didn't match");

        Assert.assertNotNull(returnResponse.get(Constants.ACTIVATE_AT), "activated at time shouldn't be null");

        Assert.assertNotNull(returnResponse.get(Constants.DEACTIVATED_AT), " deactivated at time shouldn't be null");

        Assert.assertNotNull(returnResponse.get(Constants.KEY_ID));
      //  Assert.assertNotNull(returnResponse.get(Constants.LAST_UPDATED_AT));
        Assert.assertNotNull(returnResponse.get(Constants.CREATED_AT));
        Assert.assertNotNull(returnResponse.get(Constants.CLIENT_ID));
        Assert.assertEquals(returnResponse.size()==7, "password metadata didn't match");
    }

    @And("^I create a public key with the application id$")
    public void createPublicKey() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET,
                token -> testContext.getApiManager().getPostPublicKey().setAuthTokenWithBearer(token));
        testContext.getApiManager().getPostPublicKey().setApplicationId(
                testContext.getApiManager().getPostPasswordCreateClientPassword().getApplicationId()
        );


        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                RESOURCE_ENDPOINT_PROPERTY_NAME) + "/";
        String value = getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, VALID_BASE64_ENCODED_RSA_PUBLIC_KEY);
        String activateAt = "2019-01-01T00:00:00Z";
        String deactivateAt = "2023-02-02T00:00:00Z";
        String entityStatus = "A";
        String description = "Test description";

        testContext.getApiManager().getPostPublicKey().postPublicKeys(
                url,
                value,
                activateAt,
                deactivateAt,
                entityStatus,
                description);

        Assert.assertEquals(201,
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostPublicKey().getResponse()),
                "Unable to create Post public key");
    }

    @Then("^the password response should be successful$")
    public void successResponse() {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getCreateClient().getResponse()),
                201, "Request was not successful!");

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

    @Then("^error message should be \"([^\"]*)\" within the POST password response$")
    public void i_should_receive_a_error_message(String errorMessage) {
        Response response = testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse();
        Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage) ,
                "Different error message being returned..Expected: "+ errorMessage+ " Actual: " +
                        getRestHelper().getErrorMessage(response));

    }

    public void makeRequestWithNullHeaderValue(String nullHeaderValues) {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().getPostPasswordCreateClientPassword().getApplicationId() + "/keys/passwords";
        testContext.getApiManager().getPostPasswordCreateClientPassword().makeRequestWithNullHeader(url, nullHeaderValues);
    }

    public void makeRequest(String url) {
        testContext.getApiManager().getPostPasswordCreateClientPassword().makeRequest(url);
    }
}
