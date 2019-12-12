package steps;

import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import cucumber.api.java.ca.I;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import managers.TestContext;
import managers.UtilManager;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import utils.Constants;
import utils.DateHelper;

import java.util.HashMap;
import java.util.Set;

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
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().postCredentialsMerchants().setAuthTokenWithBearer(token));
    }

    @And("^I hit the post credentials endpoint with credential name \"([^\"]*)\"$")
    public void hitPostCredentialsWithCredentialsName(String credentialName) {
        testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialName);
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().postCredentialsMerchants().getCredentialName());
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
        Assert.assertEquals(
                HttpStatus.SC_CREATED,
                getRestHelper().getResponseStatusCode(testContext.getApiManager().postCredentialsMerchants().getResponse()),
                "Expected 201 but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().postCredentialsMerchants().getResponse())
        );
        Response response = testContext.getApiManager().postCredentialsMerchants().getResponse();
        Assert.assertNotNull(response.path(Constants.PDF_PIN));
        Assert.assertNotNull(response.path(Constants.PDF_URL));
        Assert.assertNotNull(response.path(Constants.CREDENTIAL_ID));
        Assert.assertEquals(response.path(Constants.CREDENTIAL_NAME), testContext.getApiManager().postCredentialsMerchants().getCredentialName(), "Credential Name should be same as provided in input body");
        Assert.assertEquals(response.path(Constants.STATUS), "A", "Credential status should always be active A ");

        /*String date =response.path(Constants.ACTIVATE_AT);
        String currentDate = DateHelper.getCurrentDate();
        String futureDate = DateHelper.getFutureDate(1);*/

        HashMap signingKey = response.path(Constants.SIGNING_KEY);
        HashMap secret = response.path(Constants.SECRET);

        if (signingKey != null) {
            Assert.assertNotNull(signingKey.get(Constants.ALG));
            Assert.assertNotNull(signingKey.get(Constants.TYPE));
            Assert.assertNotNull(signingKey.get(Constants.SIZE));
            Assert.assertNotNull(signingKey.get(Constants.KEY_ID));
            Assert.assertNotNull(signingKey.get(Constants.VALUE));
        }
        if (secret != null) {
            Assert.assertNotNull(secret.get(Constants.ID));
            Assert.assertNotNull(secret.get(Constants.CLIENT_ID));
            Assert.assertNotNull(secret.get(Constants.VALUE));
        } else {
            getRestHelper().getResponseStatusCode(testContext.getApiManager().postCredentialsMerchants().getResponse());
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
}


