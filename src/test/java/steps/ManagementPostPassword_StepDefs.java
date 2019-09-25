package steps;
import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Constants;
import utils.PropertyHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ManagementPostPassword_StepDefs extends UtilManager {
    TestContext testContext;
    ManagementCommon common;
    private static final Set<String> ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String VALID_BASE64_ENCODED_RSA_PUBLIC_KEY = "valid_base64_encoded_rsa_public_key";
    final static Logger logger = Logger.getLogger(ManagementPostPassword_StepDefs.class);


    ManagementGetApplications_StepDefs getApplications_stepDefs;

    public ManagementPostPassword_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(this.testContext);
        getApplications_stepDefs = new ManagementGetApplications_StepDefs(this.testContext);
    }

    @When("^I set the application id as \"([^\"]*)\"$")
    public void setApplicationId(String value) {
        testContext.getApiManager().getPostPasswordCreateClientPassword().setApplicationId(value);
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


    @And("^I have created password data with application id, activate at \"([^\"]*)\", and deactivate at \"([^\"]*)\"$")
    public void setData(String activateAt, String deactivateAt) {
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().getPostPasswordCreateClientPassword().setSubUnitId(applicationResponse.getBody().path("application.subUnitId"));
        testContext.getApiManager().getPostPasswordCreateClientPassword().setActivateAt(activateAt);
        testContext.getApiManager().getPostPasswordCreateClientPassword().setDeactivateAt(deactivateAt);
        testContext.getApiManager().getPostPasswordCreateClientPassword().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
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

    @Given("^I am logging in as a user with AAD Password role$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPostPasswordCreateClientPassword().setAuthTokenWithBearer(token));
    }


    @And("^I create a new AAD password with applicationId, activateAt, deactivate and null header \"([^\"]*)\"$")
    public void createPassword(String nullHeaderValues) {
        testContext.getApiManager().getPostPasswordCreateClientPassword().setActivateAt("2019-01-01T00:00:00Z");
        testContext.getApiManager().getPostPasswordCreateClientPassword().setDeactivateAt("2032-02-02T00:00:00Z");
        testContext.getApiManager().getPostPasswordCreateClientPassword().setApplicationId(new OneClickMerchantOnboarding_StepDefs(testContext).
                createApplicationWithOneClickApi().getBody().path("application.applicationId"));
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

        Response response = testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse();
        String pdfUrl =testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse().path(Constants.PDF_PIN);
        String env = PropertyHelper.getInstance().getPropertyCascading("env");
        String usertype = PropertyHelper.getInstance().getPropertyCascading("usertype");
       // Assert.assertTrue();
        Assert.assertNotNull(testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse().path(Constants.PDF_PIN));
        Assert.assertNotNull(testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse().path(Constants.PDF_URL));

        if (env.equalsIgnoreCase("SIT") && usertype.equalsIgnoreCase("merchant")) {
            Assert.assertTrue(response.path(Constants.PDF_URL).toString().contains("https://sacct" + env.toLowerCase() + "hkdragboarding.blob.core.windows.net/paymeapi-pdf/" + testContext.getApiManager().getPostPasswordCreateClientPassword().getSubUnitId() + "_LV_"));
        } else if (env.equalsIgnoreCase("SIT") && usertype.equalsIgnoreCase("developer")) {
            System.out.println("response:" + response.path(Constants.PDF_URL));
            System.out.println("custom: " + "https://sacct" + env.toLowerCase() + "hkdragboarding.blob.core.windows.net/paymeapi-pdf/" + testContext.getApiManager().getPostPasswordCreateClientPassword().getSubUnitId() + "_SB_");
            Assert.assertTrue(response.path(Constants.PDF_URL).toString().contains("https://sacct" + env.toLowerCase() + "hkdragsandbox.blob.core.windows.net/paymeapi-pdf/" + testContext.getApiManager().getPostPasswordCreateClientPassword().getSubUnitId() + "_SB_"));
        } else if (env.equalsIgnoreCase("CI") && usertype.equalsIgnoreCase("merchant")) {
            Assert.assertTrue(response.path(Constants.PDF_URL).toString().contains("https://sacct" + env.toLowerCase() + "dragmerch.blob.core.windows.net/paymeapi-pdf/" + testContext.getApiManager().getPostPasswordCreateClientPassword().getSubUnitId() + "_LV_"));
        } else if (env.equalsIgnoreCase("CI") && usertype.equalsIgnoreCase("developer")) {
            Assert.assertTrue(response.path(Constants.PDF_URL).toString().contains("https://sacct" + env.toLowerCase() + "dragmerch.blob.core.windows.net/paymeapi-pdf/" + testContext.getApiManager().getPostPasswordCreateClientPassword().getSubUnitId() + "_SB_"));
        }
        HashMap returnResponse = testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse().path("passwordMetaData");
        Assert.assertEquals(testContext.getApiManager().getPostPasswordCreateClientPassword().
        getApplicationId(), returnResponse.get(Constants.APPLICATION_ID), "application id didn't match");
        Assert.assertNotNull(returnResponse.get(Constants.ACTIVATE_AT), "activated at time shouldn't be null");
        Assert.assertNotNull(returnResponse.get(Constants.DEACTIVATED_AT), " deactivated at time shouldn't be null");
        Assert.assertNotNull(returnResponse.get(Constants.KEY_ID));
        Assert.assertNotNull(returnResponse.get(Constants.LAST_UPDATED_AT));
        Assert.assertNotNull(returnResponse.get(Constants.CREATED_AT));
        Assert.assertNotNull(returnResponse.get(Constants.CLIENT_ID));
        Assert.assertEquals(returnResponse.size(), 8, "password metadata didn't match");
        testContext.getApiManager().getPostPasswordCreateClientPassword().setApplicationId(returnResponse.get(Constants.APPLICATION_ID).toString());
        testContext.getApiManager().getPostPasswordCreateClientPassword().setEntityStatus(returnResponse.get(Constants.ENTITY_STATUS).toString());
        testContext.getApiManager().getPostPasswordCreateClientPassword().setKeyId(returnResponse.get(Constants.KEY_ID).toString());

    }

    @Then("^error message should be \"([^\"]*)\" within the POST password response$")
    public void i_should_receive_a_error_message(String errorMessage) {
        Response response = testContext.getApiManager().getPostPasswordCreateClientPassword().getResponse();
        Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned..Expected: " + errorMessage + " Actual: " +
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
