package steps;

import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import utils.Constants;
import java.util.*;

public class ManagementGetSigningKeys_StepDef extends UtilManager {
    TestContext testContext;
    ManagementCommon common;
    private static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";

    public ManagementGetSigningKeys_StepDef(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(this.testContext);
    }

    @Given("^I am a user with permissions to use signing key$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getGetSigningKey().setAuthTokenWithBearer(token));
    }

    @And("^I make a request to get signing keys with application id$")
    public void makeRequests() {
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().getGetSigningKey().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                RESOURCE_ENDPOINT_PROPERTY_NAME) + "/";
        testContext.getApiManager().getGetSigningKey().makeCallWithApplicationID(url, testContext.getApiManager().getGetSigningKey().getApplicationId());
    }

    @And("^I make a request to get signing keys with application id and missing header \"([^\"]*)\"$")
    public void makeRequestsWithMissingHeaderValue(String keys) {
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().getGetSigningKey().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                RESOURCE_ENDPOINT_PROPERTY_NAME) + "/";
        testContext.getApiManager().getGetSigningKey().makeCallWithMissingHeader(url, testContext.getApiManager().getGetSigningKey().getApplicationId(), keys);
    }

    @And("^I make a request to get signing keys with application id \"([^\"]*)\"$")
    public void makeRequestsWithInvalidApplicationId(String applicationId) {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                RESOURCE_ENDPOINT_PROPERTY_NAME) + "/";
        testContext.getApiManager().getGetSigningKey().makeCallWithApplicationID(url, applicationId);
    }

    @And("^I make a request to get signing keys with \"([^\"]*)\" and invalid header \"([^\"]*)\" for keys \"([^\"]*)\"$")
    public void makeRequestWithInvalidHeaderInput(String headerKey, String headerValue, String applicationId) {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                RESOURCE_ENDPOINT_PROPERTY_NAME) + "/";
        testContext.getApiManager().getGetSigningKey().makeCall(url,headerKey,headerValue, applicationId);
    }

    @Then("^I should receive a signing key response error \"([^\"]*)\" status with code \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void failedResponse(String httpStatus, String errorCode, String errorDescription) {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetSigningKey().getResponse()),
                Integer.parseInt(httpStatus),
                "Status code expected " + httpStatus + " but got " + getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetSigningKey().getResponse())
        );
        Assert.assertEquals(
                getRestHelper().getErrorCode(testContext.getApiManager().getGetSigningKey().getResponse()),
                errorCode,
                "Error code expected " + errorCode + " but got " +
                        getRestHelper().getErrorCode(testContext.getApiManager().getGetSigningKey().getResponse())
        );

        if (!getRestHelper().getErrorDescription(testContext.getApiManager().getGetSigningKey().getResponse()).contains(errorDescription)) {
            Assert.assertEquals(
                    getRestHelper().getErrorDescription(testContext.getApiManager().getGetSigningKey().getResponse()),
                    errorDescription,
                    "Error description expected " + errorDescription + " but got " +
                            getRestHelper().getErrorDescription(testContext.getApiManager().getGetSigningKey().getResponse())
            );
        }
    }

    @Given("^I am a GET signing key authorized DRAGON user with the signingKey.ReadWrite.All privilege$")
    public void i_am_an_authorized_DRAGON_user_with_role() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getGetSigningKey().setAuthTokenWithBearer(token));
    }

    @Then("^error message should be \"([^\"]*)\" within the get signing key response$")
    public void i_should_receive_a_error_message(String errorMessage) {
        Response response = testContext.getApiManager().getGetSigningKey().getResponse();
        org.testng.Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned..Expected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(response));
    }

    @Then("^I should receive a successful signing key response$")
    public void successResponse() {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(
                        testContext.getApiManager().getGetSigningKey().getResponse()),
                HttpStatus.SC_OK
        );

        String returnedObject = testContext.getApiManager().getGetSigningKey().getResponse().getBody().prettyPrint();
        if (returnedObject != null) {
            String newString = "{\"response\":" + returnedObject + "}";
            Map<String, Object> retMap = new Gson().fromJson(
                    newString, new TypeToken<HashMap<String, Object>>() {
                    }.getType()
            );
            ArrayList<Map> arrayList = (ArrayList) retMap.get("response");
            Map firstElement = arrayList.get(0);
            testContext.getApiManager().getGetSigningKey().setKeyId(firstElement.get(Constants.KEY_ID).toString());
            testContext.getApiManager().getGetSigningKey().setApplicationId(firstElement.get(Constants.APPLICATION_ID).toString());
            Assert.assertTrue(firstElement.containsKey(Constants.APPLICATION_ID), testContext.getApiManager().getGetSigningKey().getApplicationId());
            Assert.assertTrue(firstElement.containsKey(Constants.KEY_ID));
            Assert.assertTrue(firstElement.containsKey(Constants.APPLICATION_ID));
            Assert.assertEquals(testContext.getApiManager().getGetSigningKey().getApplicationId(), firstElement.get(Constants.APPLICATION_ID),"ApplicationID didn't match");
            Assert.assertTrue(firstElement.containsKey(Constants.ALG));
            Assert.assertTrue(firstElement.containsKey(Constants.TYPE));
            Assert.assertTrue(firstElement.containsKey(Constants.SIZE));
            Assert.assertTrue(firstElement.containsKey(Constants.ACTIVATE_AT));
            Assert.assertTrue(firstElement.containsKey(Constants.DEACTIVATED_AT));
            Assert.assertTrue(firstElement.containsKey(Constants.ENTITY_STATUS));
            Assert.assertTrue(firstElement.containsKey(Constants.CREATED_AT));
            Assert.assertTrue(firstElement.containsKey(Constants.LAST_UPDATED_AT));
            Assert.assertEquals(firstElement.toString().split(",").length, 10);
        } else {
            getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetSigningKey().getResponse());
        }
    }

    @When("^I make a get request to the signingKey endpoint with \"([^\"]*)\" and \"([^\"]*)\" missing in the header$")
    public void i_make_a_put_request_to_the_application_endpoint_with_key_missing_in_the_header(String applicationId, String key) {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                RESOURCE_ENDPOINT_PROPERTY_NAME) + "/" + applicationId + "/keys/signing";
        testContext.getApiManager().getGetSigningKey().makeCallWithMissingHeader(url, applicationId, key);
    }
}
