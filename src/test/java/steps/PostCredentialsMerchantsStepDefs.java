package steps;

import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.gl.E;
import managers.TestContext;
import managers.UtilManager;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import utils.Constants;
import utils.PropertyHelper;

import java.util.*;

public class PostCredentialsMerchantsStepDefs extends UtilManager {
    private static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final Set<String> APPLICATION_ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";

    TestContext testContext;
    ManagementCommon common;

    public PostCredentialsMerchantsStepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }

    @Given("^I am an authorized to create credentials as DRAGON user$")
    public void pologin() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().postCredentialsMerchants().setAuthTokenWithBearer(token));
    }

    @And("^I hit the post credentials endpoint with credential name \"([^\"]*)\"$")
    public void hitPostCredentialsWithCredentialsName(String credentialName) {
        testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialName);
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("application.subUnitId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setClientId(applicationResponse.getBody().path("application.clientId"));

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().postCredentialsMerchants().getCredentialName());
    }


    @And("^I hit the post credentials endpoint with invalid applicationId \"([^\"]*)\" and valid credential name \"([^\"]*)\"$")
    public void hitPostCredentialsWithInvalidApplicationIdAndValidCredentialsName(String credentialName, String applicationId) {
        testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialName);
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("application.subUnitId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setClientId(applicationResponse.getBody().path("application.clientId"));

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + applicationId + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().postCredentialsMerchants().getCredentialName());
    }


    @And("^I hit the post credentials endpoint without request body$")
    public void hitPostCredentialsWithRequestBody() {
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequestWithoutInputBody(url);
    }

    @And("^I hit the post credentials endpoint with invalid API versions invalid header \"([^\"]*)\" and values \"([^\"]*)\"$")
    public void hitPostCredentialsWithInvalidAPIVersion(String key, String headerValue) {

        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();

        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequestWithInvalidHeaders(url, key, headerValue);
    }

    @And("^I hit the post credentials endpoint with missing header keys \"([^\"]*)\"$")
    public void hitPostCredentialsWithMissingHeader(String key) {

        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequestWithMissingHeaderValues(url, key);
    }

    @And("^I hit the post credentials endpoint without credential name \"([^\"]*)\"$")
    public void hitPostCredentialsWithoutCredentialsName(String credentialName) {
        testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialName);
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequestWithoutCredentialName(url, testContext.getApiManager().postCredentialsMerchants().getCredentialName());
    }

    @And("^I hit the post credentials endpoint six times with same credential name \"([^\"]*)\"$")
    public void hitPostCredentialsSixTimesWithDifferentName(String credentialName) {
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        int x = 1;
        while (x <= 6) {
            testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialName);
            testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().postCredentialsMerchants().getCredentialName());
            x++;

        }
    }

    @And("^I hit the post credentials endpoint second times with same credential name \"([^\"]*)\"$")
    public void hitPostCredentialsSecondTimeWithSameCredentials(String credentialName) {
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialName);
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        int x = 1;
        while (x <= 2) {
            testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().postCredentialsMerchants().getCredentialName());
            x++;
        }
    }

    @Then("^the create credentials response should be successful$")
    public void validResponse() {
        String env = PropertyHelper.getInstance().getPropertyCascading("env");
        String userType = PropertyHelper.getInstance().getPropertyCascading("usertype");

        Assert.assertEquals(
                HttpStatus.SC_CREATED,
                getRestHelper().getResponseStatusCode(testContext.getApiManager().postCredentialsMerchants().getResponse()),
                "Expected 201 but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().postCredentialsMerchants().getResponse()));

        Response response = testContext.getApiManager().postCredentialsMerchants().getResponse();
        Assert.assertNotNull(response.path(Constants.PDF_URL), "pdfUrl cannot be null!");

        if (env.equalsIgnoreCase("SIT") && userType.equalsIgnoreCase("merchant")) {
            Assert.assertTrue(response.path(Constants.PDF_URL).toString().contains("https://sacct" + env.toLowerCase() + "hkdragboarding.blob.core.windows.net/paymeapi-pdf/" + testContext.getApiManager().getOneClickMerchantOnboarding().getSubUnitId() + "_LV_"));
        } else if (env.equalsIgnoreCase("SIT") && userType.equalsIgnoreCase("developer")) {
            Assert.assertTrue(response.path(Constants.PDF_URL).toString().contains("https://sacct" + env.toLowerCase() + "hkdragsandbox.blob.core.windows.net/paymeapi-pdf/" + testContext.getApiManager().getOneClickMerchantOnboarding().getSubUnitId() + "_SB_"));
        } else if (env.equalsIgnoreCase("CI") && userType.equalsIgnoreCase("merchant")) {
            Assert.assertTrue(response.path(Constants.PDF_URL).toString().contains("https://sacct" + env.toLowerCase() + "dragmerch.blob.core.windows.net/paymeapi-pdf/" + testContext.getApiManager().getOneClickMerchantOnboarding().getSubUnitId() + "_LV_"));
        } else if (env.equalsIgnoreCase("CI") && userType.equalsIgnoreCase("developer")) {
            Assert.assertTrue(response.path(Constants.PDF_URL).toString().contains("https://sacct" + env.toLowerCase() + "dragmerch.blob.core.windows.net/paymeapi-pdf/" + testContext.getApiManager().getOneClickMerchantOnboarding().getSubUnitId() + "_SB_"));
        }
        Assert.assertNotNull(response.path(Constants.PDF_PIN), "pdfPin cannot be null!");
        Assert.assertEquals(response.path(Constants.PDF_PIN).toString().length(), 16, "pdfPin should be 16 characters.");
        Assert.assertNotNull(response.path(Constants.CREDENTIAL_ID));
        Assert.assertEquals(response.path(Constants.CREDENTIAL_NAME), testContext.getApiManager().postCredentialsMerchants().getCredentialName(), "Credential Name should be same as provided in input body");
        Assert.assertEquals(response.path(Constants.STATUS), "A", "Credential status should always be active A ");
        Assert.assertEquals(response.path(Constants.ACTIVATE_AT).toString().substring(0, 10), getDateHelper().getCurrentDate(), "Activate date should be today's date");
        Assert.assertEquals(response.path(Constants.CREATED_AT).toString().substring(0, 10), getDateHelper().getCurrentDate(), "createdAT date should be today's date");
        Assert.assertEquals(response.path(Constants.LAST_UPDATED_AT).toString().substring(0, 10), getDateHelper().getCurrentDate(), "lastUpdatedAt date should be today's date");
        Assert.assertEquals(response.path(Constants.EXPIRE_AT).toString(), getDateHelper().getFutureDate(1).concat(response.path(Constants.ACTIVATE_AT).toString().substring(10)), "deactivatedAt date should be one year later than createdAt date");

        Assert.assertEquals(response.path(Constants.APPLICATION_ID).toString(), testContext.getApiManager().postCredentialsMerchants().getApplicationId(), "applicationId should be same as used in endpoint");



//        Assert.assertEquals(response.path(Constants.LAST_UPDATED_AT).toString(), response.path(Constants.CREATED_AT).toString(), "lastUpdatedAt date and createdAt should be the same date");
//        Assert.assertEquals(response.path(Constants.LAST_UPDATED_AT).toString(), response.path(Constants.ACTIVATE_AT).toString(), "lastUpdatedAt date and activateAt should be the same date");
//        Assert.assertEquals(response.path(Constants.ACTIVATE_AT).toString(), response.path(Constants.CREATED_AT).toString(), "activateAt date and createdAt should be the same date");

        if (env.equalsIgnoreCase("SIT") && userType.equalsIgnoreCase("merchant")) {
            Assert.assertEquals(response.path(Constants.LAST_UPDATED_BY).toString(), getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-id"), "updatedBy is not correct");
            Assert.assertEquals(response.path(Constants.CREATED_BY).toString(), getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-id"), "updatedBy is not correct");
        } else if (env.equalsIgnoreCase("CI") && userType.equalsIgnoreCase("merchant")) {
            Assert.assertEquals(response.path(Constants.LAST_UPDATED_BY).toString(), getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-id"), "updatedBy is not correct");
            Assert.assertEquals(response.path(Constants.CREATED_BY).toString(), getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-id"), "updatedBy is not correct");

        } else if (env.equalsIgnoreCase("SIT") && userType.equalsIgnoreCase("developer")) {
            Assert.assertEquals(response.path(Constants.LAST_UPDATED_BY).toString(), getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "developer-client-id"), "updatedBy is not correct");
            Assert.assertEquals(response.path(Constants.CREATED_BY).toString(), getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "developer-client-id"), "updatedBy is not correct");

        } else if (env.equalsIgnoreCase("CI") && userType.equalsIgnoreCase("developer")) {
            Assert.assertEquals(response.path(Constants.LAST_UPDATED_BY).toString(), getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "developer-client-id"), "updatedBy is not correct");
            Assert.assertEquals(response.path(Constants.CREATED_BY).toString(), getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "developer-client-id"), "updatedBy is not correct");

        } else if (env.equalsIgnoreCase("PRE") && userType.equalsIgnoreCase("merchant")) {
            Assert.assertEquals(response.path(Constants.LAST_UPDATED_BY).toString(), getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-id"), "updatedBy is not correct");
            Assert.assertEquals(response.path(Constants.CREATED_BY).toString(), getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-id"), "updatedBy is not correct");

        } else if (env.equalsIgnoreCase("PRE") && userType.equalsIgnoreCase("developer")) {
            Assert.assertEquals(response.path(Constants.LAST_UPDATED_BY).toString(), getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "developer-client-id"), "updatedBy is not correct");
            Assert.assertEquals(response.path(Constants.CREATED_BY).toString(), getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "developer-client-id"), "updatedBy is not correct");

        } else if (env.equalsIgnoreCase("UAT1") && userType.equalsIgnoreCase("merchant")) {
            Assert.assertEquals(response.path(Constants.LAST_UPDATED_BY).toString(), getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-id"), "updatedBy is not correct");
            Assert.assertEquals(response.path(Constants.CREATED_BY).toString(), getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-id"), "updatedBy is not correct");

        } else if (env.equalsIgnoreCase("UAT1") && userType.equalsIgnoreCase("developer")) {
            Assert.assertEquals(response.path(Constants.LAST_UPDATED_BY).toString(), getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "developer-client-id"), "updatedBy is not correct");
            Assert.assertEquals(response.path(Constants.CREATED_BY).toString(), getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "developer-client-id"), "updatedBy is not correct");

            HashMap signingKey = response.path(Constants.SIGNING_KEY);
            HashMap secret = response.path(Constants.SECRET);

            if (signingKey != null) {
                Assert.assertEquals(signingKey.size(),6);
                Assert.assertNotNull(signingKey.get(Constants.ALG));
                Assert.assertNotNull(signingKey.get(Constants.ID));
                Assert.assertNotNull(signingKey.get(Constants.TYPE));
                Assert.assertNotNull(signingKey.get(Constants.SIZE));
                Assert.assertNotNull(signingKey.get(Constants.KEY_ID));
                Assert.assertNotNull(signingKey.get(Constants.VALUE));
                Assert.assertEquals(signingKey.get(Constants.VALUE), "");
            }
            if (secret != null) {
                Assert.assertEquals(secret.size(),3);
                Assert.assertNotNull(secret.get(Constants.ID));
                Assert.assertEquals(secret.get(Constants.CLIENT_ID),testContext.getApiManager().getOneClickMerchantOnboarding().getClientId(), "clientId should belongs from the applicationId");
                Assert.assertNotNull(secret.get(Constants.VALUE));
                Assert.assertEquals(secret.get(Constants.VALUE), "");
            } else {
                getRestHelper().getResponseStatusCode(testContext.getApiManager().postCredentialsMerchants().getResponse());
            }
        }
    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorCode within create credentials response$")
    public void i_should_receive_a_error_response_with_error_description_and_errorCode_within_postCredential_response(int responseCode, String errorDesc, String errorCode) {
        Response response = testContext.getApiManager().postCredentialsMerchants().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), responseCode, "Different response code being returned");
        Assert.assertEquals(getRestHelper().getErrorCode(testContext.getApiManager().postCredentialsMerchants().getResponse()), errorCode, "Different error code being returned");
        Assert.assertTrue(getRestHelper().getErrorDescription(testContext.getApiManager().postCredentialsMerchants().getResponse()).contains(errorDesc), "Different error description being returned..Expected: " + errorDesc + "  Actual: " + getRestHelper().getErrorDescription(testContext.getApiManager().postCredentialsMerchants().getResponse()));
    }

    @Then("^error message should be \"([^\"]*)\" within create credentials response$")
    public void error_message_should_be_within_check_status_response(String errorMessage) {
        Assert.assertTrue(getRestHelper().getErrorMessage(testContext.getApiManager().postCredentialsMerchants().getResponse()).contains(errorMessage), "Different error message being returned..Expected: " + errorMessage + "  Actual: " + getRestHelper().getErrorMessage(testContext.getApiManager().postCredentialsMerchants().getResponse()));
    }

    @When("^I send invalid auth token \"([^\"]*)\" to create credentials$")
    public void i_send_invalid_in_the_check_status_request(String authToken) {
        testContext.getApiManager().postCredentialsMerchants().setAuthToken(authToken);
    }

    @And("^I should receive a \"([^\"]*)\" status code with \"([^\"]*)\" message \"([^\"]*)\" with create credentials response$")
    public void errorMessageShouldBeWithInResponse(int httpsCode, int statusCode, String errorMessage) throws Throwable {
        Response response = testContext.getApiManager().postCredentialsMerchants().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), httpsCode, "Expected Response Code: " + httpsCode + "Actual: " + response.getStatusCode());
        Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned..Expected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(response));
    }
}


