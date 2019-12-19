package steps;
import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import utils.Constants;
import java.util.Set;

public class PutCredentials_StepDefs extends UtilManager {
    private static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final Set<String> APPLICATION_ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";

    TestContext testContext;
    ManagementCommon common;

    public PutCredentials_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }

    @Given("^I am an authorized to put credentials as DRAGON user$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPutCredentialsMerchants().setAuthTokenWithBearer(token));
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().postCredentialsMerchants().setAuthTokenWithBearer(token));
    }

    @And("^I hit the put credentials endpoint with new credential name \"([^\"]*)\"$")
    public void hitPutCredentialsWithCredentialsName(String credentialName) {

        testContext.getApiManager().getPutCredentialsMerchants().setCredentialName(credentialName);
        testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialName);

        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("subUnitId"));

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().getPutCredentialsMerchants().getCredentialName());

        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();

        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);

        testContext.getApiManager().postCredentialsMerchants().setCredentialId(credentialId);

        String putCredentialEndPoint = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + applicationResponse.getBody().path("applicationId") + "/credentials" + "/" + credentialId ;
        testContext.getApiManager().getPutCredentialsMerchants().makeRequest(putCredentialEndPoint,testContext.getApiManager().postCredentialsMerchants().getCredentialName());
    }

    @Then("^put credentials response should be successful$")
    public void verifySuccessResponse() {
        Assert.assertEquals(
                HttpStatus.SC_CREATED,
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutCredentialsMerchants().getResponse()),
                "Expected 200 but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutCredentialsMerchants().getResponse()));
        Assert.assertEquals(testContext.getApiManager().getPutCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_NAME), testContext.getApiManager().postCredentialsMerchants().getCredentialName(),"new credential name not updated");
        Assert.assertEquals(testContext.getApiManager().getPutCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_ID), testContext.getApiManager().postCredentialsMerchants().getCredentialId(),"credentials Id is not same as used in endpoint");
        Assert.assertEquals(testContext.getApiManager().getPutCredentialsMerchants().getResponse().path(Constants.STATUS), "A", "credentials status should be active");

    }
    @Then("^put credentials response should be updated$")
    public void putCredentialsResponseShouldBeUpdated() {
        Assert.assertEquals(
                HttpStatus.SC_CREATED,
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutCredentialsMerchants().getResponse()),
                "Expected 200 but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutCredentialsMerchants().getResponse()));
        Assert.assertEquals(testContext.getApiManager().getPutCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_NAME), testContext.getApiManager().getPutCredentialsMerchants().getCredentialName(),"new credential name not updated");
        Assert.assertEquals(testContext.getApiManager().getPutCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_ID), testContext.getApiManager().postCredentialsMerchants().getCredentialId(),"credentials Id is not same as used in endpoint");
        Assert.assertEquals(testContext.getApiManager().getPutCredentialsMerchants().getResponse().path(Constants.STATUS), "D", "credentials status should be deactivated");

    }

    @And("^I hit the put credentials endpoint with new credential name \"([^\"]*)\" and status \"([^\"]*)\"$")
    public void hitPutCredentialsWithCredentialsStatus(String credentialName, String credentialsStatus) {

        testContext.getApiManager().getPutCredentialsMerchants().setCredentialName(credentialName);
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("subUnitId"));

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().getPutCredentialsMerchants().getCredentialName());

        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();

        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);
        testContext.getApiManager().postCredentialsMerchants().setCredentialId(credentialId);
        String putCredentialEndPoint = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + applicationResponse.getBody().path("applicationId") + "/credentials" + "/" + credentialId ;
        testContext.getApiManager().getPutCredentialsMerchants().makeRequestWithDeactivateStatusInBody(putCredentialEndPoint, testContext.getApiManager().getPutCredentialsMerchants().getCredentialName(),credentialsStatus);
    }


    @And("^I hit the put credentials endpoint to update new credentials name as existing credential name \"([^\"]*)\"$")
    public void hitPutCredentialsWithWithExistingCredentialName(String credentialName) {

        testContext.getApiManager().getPutCredentialsMerchants().setCredentialName(credentialName);
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("subUnitId"));

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().getPutCredentialsMerchants().getCredentialName());

        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();

        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);

        testContext.getApiManager().postCredentialsMerchants().setCredentialId(credentialId);
        testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialResponse.path(Constants.CREDENTIAL_NAME));

        String putCredentialEndPoint = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + applicationResponse.getBody().path("applicationId") + "/credentials" + "/" + credentialId ;
        testContext.getApiManager().getPutCredentialsMerchants().makeRequest(putCredentialEndPoint,testContext.getApiManager().postCredentialsMerchants().getCredentialName());
    }



    @And("^I hit update API to reactivate the deactivated credentials \"([^\"]*)\" and credential name \"([^\"]*)\"$")
    public void reactivateTheDeactivatedCredentials(String credentialsStatus, String credentialName) {
        String putCredentialEndPoint = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials" + "/" + testContext.getApiManager().postCredentialsMerchants().getCredentialId() ;
        testContext.getApiManager().getPutCredentialsMerchants().makeRequestWithDeactivateStatusInBody(putCredentialEndPoint, testContext.getApiManager().getPutCredentialsMerchants().getCredentialName(),credentialsStatus);
    }


    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorCode within put credentials response$")
    public void i_should_receive_a_error_response_with_error_description_and_errorCode_within_postCredential_response(int responseCode, String errorDesc, String errorCode) {
        Response response = testContext.getApiManager().getPutCredentialsMerchants().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), responseCode, "Different response code being returned");
        Assert.assertEquals(getRestHelper().getErrorCode(testContext.getApiManager().getPutCredentialsMerchants().getResponse()), errorCode, "Different error code being returned");
        Assert.assertTrue(getRestHelper().getErrorDescription(testContext.getApiManager().getPutCredentialsMerchants().getResponse()).contains(errorDesc), "Different error description being returned..Expected: " + errorDesc + "  Actual: " + getRestHelper().getErrorDescription(testContext.getApiManager().getPutCredentialsMerchants().getResponse()));
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
}


