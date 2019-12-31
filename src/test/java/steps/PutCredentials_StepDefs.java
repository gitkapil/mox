package steps;

import apiHelpers.GetApplication;
import apiHelpers.PutCredentialsMerchants;
import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Constants;
import utils.DataBaseConnector;
import utils.PropertyHelper;

import java.sql.SQLException;
import java.util.Set;

public class PutCredentials_StepDefs extends UtilManager {
    private static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final Set<String> APPLICATION_ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private final static Logger logger = Logger.getLogger(PutCredentials_StepDefs.class);
    TestContext testContext;
    ManagementCommon common;

    public PutCredentials_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }

    @Given("^I am an authorized to put credentials as DRAGON user$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().postCredentialsMerchants().setAuthTokenWithBearer(token));
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPutCredentialsMerchants().setAuthTokenWithBearer(token));
    }

    @And("^I hit the put credentials endpoint with new credential name \"([^\"]*)\"$")
    public void hitPutCredentialsWithCredentialsName(String credentialName) {

        //Onboarding
        testContext.getApiManager().getPutCredentialsMerchants().setCredentialName("validName");
        testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialName);

        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("subUnitId"));

        //POST Credentials
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().postCredentialsMerchants().getCredentialName());

        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();

        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);

        testContext.getApiManager().postCredentialsMerchants().setCredentialId(credentialId);
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPutCredentialsMerchants().setAuthTokenWithBearer(token));
        //Put Credentials
        String putCredentialEndPoint = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + applicationResponse.getBody().path("applicationId") + "/credentials" + "/" + credentialId;
        testContext.getApiManager().getPutCredentialsMerchants().makeRequest(putCredentialEndPoint, testContext.getApiManager().getPutCredentialsMerchants().getCredentialName());

    }


    @And("^I hit the put credentials endpoint with existing expired credential name \"([^\"]*)\"$")
    public void hitPutCredentialsWithExistingCredentialsName(String credentialName) throws SQLException, ClassNotFoundException {

        //Onboarding
        testContext.getApiManager().getPutCredentialsMerchants().setCredentialName("validName");
        testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialName);

        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("subUnitId"));

        //POST Credentials
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().postCredentialsMerchants().getCredentialName());

        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();

        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);

        String env = PropertyHelper.getInstance().getPropertyCascading("env");
        String userType = PropertyHelper.getInstance().getPropertyCascading("usertype");

        if (env.equalsIgnoreCase("SIT") && userType.equalsIgnoreCase("merchant")) {
            DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_SIT_MERCHANT, Constants.DB_PASSWORD_ADMIN_SIT_MERCHANT, Constants.DB_CONNECTION_URL_SIT_MERCHANT);

        } else if (env.equalsIgnoreCase("CI") && userType.equalsIgnoreCase("merchant")) {
            DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_CI_MERCHANT, Constants.DB_PASSWORD_ADMIN_CI_MERCHANT, Constants.DB_CONNECTION_URL_CI_MERCHANT);

        } else if (env.equalsIgnoreCase("SIT") && userType.equalsIgnoreCase("developer")) {
            DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_SIT_SANDBOX, Constants.DB_PASSWORD_ADMIN_SIT_SANDBOX, Constants.DB_CONNECTION_URL_SIT_SANDBOX);

        } else if (env.equalsIgnoreCase("CI") && userType.equalsIgnoreCase("developer")) {
            DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_CI_SANDBOX, Constants.DB_PASSWORD_ADMIN_CI_SANDBOX, Constants.DB_CONNECTION_URL_CI_SANDBOX);

        } else if (env.equalsIgnoreCase("PRE") && userType.equalsIgnoreCase("merchant")) {
            DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_PRE_MERCHANT, Constants.DB_PASSWORD_ADMIN_PRE_MERCHANT, Constants.DB_CONNECTION_URL_PRE_MERCHANT);

        } else if (env.equalsIgnoreCase("PRE") && userType.equalsIgnoreCase("developer")) {
            DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_PRE_SANDBOX, Constants.DB_PASSWORD_ADMIN_PRE_SANDBOX, Constants.DB_CONNECTION_URL_PRE_SANDBOX);

        } else if (env.equalsIgnoreCase("UAT1") && userType.equalsIgnoreCase("merchant")) {
            DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_UAT1_MERCHANT, Constants.DB_PASSWORD_ADMIN_UAT1_MERCHANT, Constants.DB_CONNECTION_URL_UAT1_MERCHANT);

        } else if (env.equalsIgnoreCase("UAT1") && userType.equalsIgnoreCase("developer")) {
            DataBaseConnector.expireCredentialsWithCredentialID(credentialId, Constants.DB_USERNAME_ADMIN_UAT1_SANDBOX, Constants.DB_PASSWORD_ADMIN_UAT1_SANDBOX, Constants.DB_CONNECTION_URL_UAT1_SANDBOX);
        }

        logger.info("expired the credential with " + credentialId);

        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, "credential");
        logger.info("created new credential with newCredentialDifferent");

        Response newCredentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();
        String newCredentialId = newCredentialResponse.path(Constants.CREDENTIAL_ID);
        testContext.getApiManager().postCredentialsMerchants().setCredentialId(newCredentialId);
        String putCredentialEndPoint = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + applicationResponse.getBody().path(Constants.APPLICATION_ID) + "/credentials" + "/" + newCredentialId;
        testContext.getApiManager().getPutCredentialsMerchants().makeRequest(putCredentialEndPoint, testContext.getApiManager().postCredentialsMerchants().getCredentialName());

    }


    @And("^I hit the put credentials endpoint with new invalid credential name \"([^\"]*)\"$")
    public void hitPutCredentialsWithInvalidCredentialsName(String credentialName) {

        //Onboarding
        testContext.getApiManager().getPutCredentialsMerchants().setCredentialName(credentialName);
        testContext.getApiManager().postCredentialsMerchants().setCredentialName("validName");

        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("subUnitId"));

        //POST Credentials
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().postCredentialsMerchants().getCredentialName());

        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();

        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);

        testContext.getApiManager().postCredentialsMerchants().setCredentialId(credentialId);

        //Put Credentials
        String putCredentialEndPoint = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + applicationResponse.getBody().path("applicationId") + "/credentials" + "/" + credentialId;
        testContext.getApiManager().getPutCredentialsMerchants().makeRequest(putCredentialEndPoint, testContext.getApiManager().getPutCredentialsMerchants().getCredentialName());

    }


    @And("^I hit the put credentials endpoint with without input body$")
    public void hitPutCredentialsWithoutInputBody() {

        testContext.getApiManager().getPutCredentialsMerchants().setCredentialName("credentialName");
        testContext.getApiManager().postCredentialsMerchants().setCredentialName("credentialName");

        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("subUnitId"));

        //POST Credentials
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().getPutCredentialsMerchants().getCredentialName());

        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();

        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);

        testContext.getApiManager().postCredentialsMerchants().setCredentialId(credentialId);

        //Put Credentials
        String putCredentialEndPoint = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + applicationResponse.getBody().path("applicationId") + "/credentials" + "/" + credentialId;
        testContext.getApiManager().getPutCredentialsMerchants().makeRequestWithoutInputBody(putCredentialEndPoint);

    }

    @And("^I hit the put credentials endpoint with empty input body$")
    public void hitPutCredentialsWithEmptyInputBody() {

        testContext.getApiManager().getPutCredentialsMerchants().setCredentialName("credentialName");
        testContext.getApiManager().postCredentialsMerchants().setCredentialName("credentialName");

        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("subUnitId"));

        //POST Credentials
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().getPutCredentialsMerchants().getCredentialName());

        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();

        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);

        testContext.getApiManager().postCredentialsMerchants().setCredentialId(credentialId);

        //Put Credentials
        String putCredentialEndPoint = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + applicationResponse.getBody().path("applicationId") + "/credentials" + "/" + credentialId;
        testContext.getApiManager().getPutCredentialsMerchants().makeRequestWithEmptyInputBody(putCredentialEndPoint, "credentialName");

    }

    @Then("^put credentials response should be successful$")
    public void verifySuccessResponse() {
        Assert.assertEquals(
                HttpStatus.SC_CREATED,
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutCredentialsMerchants().getResponse()),
                "Expected 200 but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutCredentialsMerchants().getResponse()));
        Assert.assertEquals(testContext.getApiManager().getPutCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_NAME), testContext.getApiManager().getPutCredentialsMerchants().getCredentialName(), "new credential name not updated");
        Assert.assertEquals(testContext.getApiManager().getPutCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_ID), testContext.getApiManager().postCredentialsMerchants().getCredentialId(), "credentials Id is not same as used in endpoint");
        Assert.assertEquals(testContext.getApiManager().getPutCredentialsMerchants().getResponse().path(Constants.STATUS), "A", "credentials status should be active");

    }


    @Then("^put credentials response should update the expired name successful$")
    public void expiredNameShouldBeUpdatedSuccessfully() {
        Assert.assertEquals(
                HttpStatus.SC_CREATED,
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutCredentialsMerchants().getResponse()),
                "Expected 200 but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutCredentialsMerchants().getResponse()));
        Assert.assertEquals(testContext.getApiManager().getPutCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_NAME), testContext.getApiManager().postCredentialsMerchants().getCredentialName(), "new credential name not updated");
        Assert.assertEquals(testContext.getApiManager().getPutCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_ID), testContext.getApiManager().postCredentialsMerchants().getCredentialId(), "credentials Id is not same as used in endpoint");
        Assert.assertEquals(testContext.getApiManager().getPutCredentialsMerchants().getResponse().path(Constants.STATUS), "A", "credentials status should be active");

    }

    @Then("^put credentials response should be updated$")
    public void putCredentialsResponseShouldBeUpdated() {
        Assert.assertEquals(
                HttpStatus.SC_CREATED,
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutCredentialsMerchants().getResponse()),
                "Expected 200 but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutCredentialsMerchants().getResponse()));
        Assert.assertEquals(testContext.getApiManager().getPutCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_NAME), testContext.getApiManager().getPutCredentialsMerchants().getCredentialName(), "new credential name not updated");
        Assert.assertEquals(testContext.getApiManager().getPutCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_ID), testContext.getApiManager().postCredentialsMerchants().getCredentialId(), "credentials Id is not same as used in endpoint");
        Assert.assertEquals(testContext.getApiManager().getPutCredentialsMerchants().getResponse().path(Constants.STATUS), "D", "credentials status should be deactivated");

    }

    @And("^I hit the Put credentials endpoint with new credential name \"([^\"]*)\" and status \"([^\"]*)\"$")
    public void hitPutCredentialsWithCredentialsStatus(String credentialName, String credentialsStatus) {

        testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialName);
        testContext.getApiManager().getPutCredentialsMerchants().setCredentialName(credentialName);
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("subUnitId"));

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().postCredentialsMerchants().getCredentialName());

        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();

        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);

        testContext.getApiManager().postCredentialsMerchants().setCredentialId(credentialId);

        String putCredentialEndPoint = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + applicationResponse.getBody().path("applicationId") + "/credentials" + "/" + credentialId;
        testContext.getApiManager().getPutCredentialsMerchants().makeRequestWithDeactivateStatusInBody(putCredentialEndPoint, testContext.getApiManager().getPutCredentialsMerchants().getCredentialName(), credentialsStatus);
    }


    @And("^I hit the put credentials endpoint to update new credentials name as existing credential name \"([^\"]*)\"$")
    public void hitPutCredentialsWithWithExistingCredentialName(String credentialName) {

        testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialName);
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("subUnitId"));

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().postCredentialsMerchants().getCredentialName());

        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();

        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);

        testContext.getApiManager().postCredentialsMerchants().setCredentialId(credentialId);
        testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialResponse.path(Constants.CREDENTIAL_NAME));

        String putCredentialEndPoint = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + applicationResponse.getBody().path("applicationId") + "/credentials" + "/" + credentialId;
        testContext.getApiManager().getPutCredentialsMerchants().makeRequest(putCredentialEndPoint, testContext.getApiManager().postCredentialsMerchants().getCredentialName());
    }

    @When("^I send invalid auth token \"([^\"]*)\" to put credentials$")
    public void i_send_invalid_in_the_check_status_request(String authToken) {
        testContext.getApiManager().getPutCredentialsMerchants().setAuthToken(authToken);
    }


    @And("^I hit the put credentials endpoint with credential name \"([^\"]*)\"$")
    public void hitPuttCredentialsWithCredentialsName(String credentialName) {
        testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialName);
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();

        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("subUnitId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setClientId(applicationResponse.getBody().path("clientId"));

        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path(Constants.APPLICATION_ID));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path(Constants.SUB_UNIT_ID));
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().postCredentialsMerchants().getCredentialName());
    }


    @And("^I hit update API to reactivate the deactivated credentials \"([^\"]*)\" and credential name \"([^\"]*)\"$")
    public void reactivateTheDeactivatedCredentials(String credentialsStatus, String credentialName) {
        String putCredentialEndPoint = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials" + "/" + testContext.getApiManager().postCredentialsMerchants().getCredentialId();
        testContext.getApiManager().getPutCredentialsMerchants().makeRequestWithDeactivateStatusInBody(putCredentialEndPoint, testContext.getApiManager().getPutCredentialsMerchants().getCredentialName(), credentialsStatus);
    }


    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorCode within put credentials response$")
    public void i_should_receive_a_error_response_with_error_description_and_errorCode_within_postCredential_response(int responseCode, String errorDesc, String errorCode) {
        Response response = testContext.getApiManager().getPutCredentialsMerchants().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), responseCode, "Different response code being returned");
        Assert.assertEquals(getRestHelper().getErrorCode(testContext.getApiManager().getPutCredentialsMerchants().getResponse()), errorCode, "Different error code being returned");
        Assert.assertTrue(getRestHelper().getErrorDescription(testContext.getApiManager().getPutCredentialsMerchants().getResponse()).
                contains(errorDesc), "Different error description being returned..Expected: " + errorDesc + "  Actual: " +
                getRestHelper().getErrorDescription(testContext.getApiManager().getPutCredentialsMerchants().getResponse()));
    }

    @Then("^error message should be \"([^\"]*)\" within put credentials response$")
    public void error_message_should_be_within_check_status_response(String errorMessage) {
        Assert.assertTrue(getRestHelper().getErrorMessage(testContext.getApiManager().getPutCredentialsMerchants().getResponse()).contains(errorMessage), "Different error message being returned..Expected: " + errorMessage + "  Actual: " + getRestHelper().getErrorMessage(testContext.getApiManager().getPutCredentialsMerchants().getResponse()));
    }

    public String getNewCredentialId() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPutCredentialsMerchants().setAuthTokenWithBearer(token));
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        String credentialName = RandomStringUtils.randomAlphabetic(10);
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + applicationResponse.getBody().path("applicationId") + "/credentials";
        testContext.getApiManager().getPutCredentialsMerchants().makeRequest(url, credentialName);
        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();
        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);
        return credentialId;
    }

    @When("^I hit the put credentials endpoint with new credential name \"([^\"]*)\" and status \"([^\"]*)\"$")
    public void iHitPutCredentialsEndpointWithNewCredentialNameAndStatus(String credentialName, String status) throws Throwable {
        //POST Credentials
        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();
        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);
        String post_credentialName = credentialResponse.path(Constants.CREDENTIAL_NAME);

        //PUT Credential
        String putCredentialEndPoint = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials/" + credentialId;

        testContext.getApiManager().getPutCredentialsMerchants().makeRequestWithStatus(putCredentialEndPoint, post_credentialName, status);

        //Setting credentalId and credentialName
        testContext.getApiManager().getCredentialsMerchants().setGetCredentialId(credentialId);
        testContext.getApiManager().getCredentialsMerchants().setGetCredentialName(post_credentialName);
    }


    @And("^I hit the post credential endpoint with existing deactivated credential name$")
    public void hitPutCredentialsWithDeactivatedCredentialName() {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().getPutCredentialsMerchants().getCredentialName());
        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();
        String credentialName = credentialResponse.path(Constants.CREDENTIAL_NAME);
    }


    @And("^I should receive a \"([^\"]*)\" status code with \"([^\"]*)\" message \"([^\"]*)\" with put credentials response$")
    public void errorMessageShouldBeWithInResponse(int httpsCode, int statusCode, String errorMessage) throws Throwable {
        Response response = testContext.getApiManager().getPutCredentialsMerchants().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), httpsCode, "Expected Response Code: " + httpsCode + "Actual: " + response.getStatusCode());
        Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned..Expected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(response));
    }


    @And("^I hit the put credentials endpoint with invalid header for \"([^\"]*)\" and values \"([^\"]*)\"$")
    public void hitPutCredentialsWithInvalidAPIVersion(String key, String headerValue) {

        //Onboarding
        testContext.getApiManager().getPutCredentialsMerchants().setCredentialName("credentialName");
        testContext.getApiManager().postCredentialsMerchants().setCredentialName("credentialName");

        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("subUnitId"));

        //POST Credentials
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().getPutCredentialsMerchants().getCredentialName());

        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();

        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);

        testContext.getApiManager().postCredentialsMerchants().setCredentialId(credentialId);

        //Put Credentials
        String putCredentialEndPoint = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials" + "/" + credentialId;
        testContext.getApiManager().getPutCredentialsMerchants().makeRequestWithInvalidHeaders(putCredentialEndPoint, testContext.getApiManager().postCredentialsMerchants().getCredentialName(), key, headerValue);

    }


    @And("^I hit the put credentials endpoint with invalid credential name \"([^\"]*)\" and valid application id \"([^\"]*)\"$")
    public void hitPutCredentialsWithInvalidApplicationIdAndValidCredentialsName(String credentialName, String applicationId) {

        //Onboarding
        testContext.getApiManager().getPutCredentialsMerchants().setCredentialName(credentialName);
        testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialName);
        testContext.getApiManager().getPutCredentialsMerchants().setApplicationId(applicationId);
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("subUnitId"));

        //POST Credentials
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().getPutCredentialsMerchants().getCredentialName());

        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();

        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);

        testContext.getApiManager().postCredentialsMerchants().setCredentialId(credentialId);

        //Put Credentials
        String putCredentialEndPoint = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().getPutCredentialsMerchants().getApplicationId() + "/credentials" + "/" + credentialId;
        testContext.getApiManager().getPutCredentialsMerchants().makeRequest(putCredentialEndPoint, testContext.getApiManager().postCredentialsMerchants().getCredentialName());

    }


    @And("^I hit the put credentials endpoint with missing header keys \"([^\"]*)\"$")
    public void hitPostCredentialsWithMissingHeader(String key) {

        //Onboarding
        testContext.getApiManager().getPutCredentialsMerchants().setCredentialName("credentialName");
        testContext.getApiManager().postCredentialsMerchants().setCredentialName("credentialName");

        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("subUnitId"));

        //POST Credentials
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().getPutCredentialsMerchants().getCredentialName());

        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();

        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);

        testContext.getApiManager().postCredentialsMerchants().setCredentialId(credentialId);

        //Put Credentials
        String putCredentialEndPoint = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials" + "/" + credentialId;
        testContext.getApiManager().getPutCredentialsMerchants().makeRequestWithMissingHeaderValues(putCredentialEndPoint, testContext.getApiManager().postCredentialsMerchants().getCredentialName(), key);
    }


    @And("^I hit the put credentials endpoint with invalid API versions invalid header \"([^\"]*)\" and values \"([^\"]*)\"$")
    public void hitPostCredentialsWithInvalidAPIVersion(String key, String headerValue) {
        testContext.getApiManager().getPutCredentialsMerchants().setCredentialName("credentialName");
        testContext.getApiManager().postCredentialsMerchants().setCredentialName("credentialName");

        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("subUnitId"));

        //POST Credentials
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().getPutCredentialsMerchants().getCredentialName());

        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();

        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);

        testContext.getApiManager().postCredentialsMerchants().setCredentialId(credentialId);

        String putUrl = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().getPutCredentialsMerchants().makeRequestWithInvalidHeaders(putUrl, testContext.getApiManager().postCredentialsMerchants().getCredentialName(), key, headerValue);
    }

    @And("^I hit the put credentials endpoint with invalid credential id \"([^\"]*)\" and valid credential name \"([^\"]*)\"$")
    public void hitPutCredentialsWithInvalidCredentialIdAndValidCredentialsName(String credentialId, String credentialName) {

        //Onboarding
        testContext.getApiManager().getPutCredentialsMerchants().setCredentialName(credentialName);
        testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialName);

        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("subUnitId"));

        //POST Credentials
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().getPutCredentialsMerchants().getCredentialName());

        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();

        testContext.getApiManager().postCredentialsMerchants().setCredentialId(credentialResponse.path(Constants.CREDENTIAL_ID));

        testContext.getApiManager().getPutCredentialsMerchants().setCredentialId(credentialId);
        //Put Credentials
        String putCredentialEndPoint = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials" + "/" + credentialId;
        testContext.getApiManager().getPutCredentialsMerchants().makeRequest(putCredentialEndPoint, testContext.getApiManager().postCredentialsMerchants().getCredentialName());

    }

}


