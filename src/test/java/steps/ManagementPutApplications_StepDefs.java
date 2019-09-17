package steps;
import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Constants;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;


public class ManagementPutApplications_StepDefs extends UtilManager {
    // NB: These are the dragon token (for testing) roles.  CSO tokens use claim {"role": "user"}
    private static final Set<String> ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final Set<String> INCORRECT_ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String SIG_HEADER_LIST_POST_APPLICATION = "header-list-post-application";

    private TestContext testContext;
    private ManagementCommon common;
    private ManagementPostApplications_StepDefs postApplications_stepDefs;

    public ManagementPutApplications_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        this.common = new ManagementCommon(testContext);
        this.postApplications_stepDefs = new ManagementPostApplications_StepDefs(testContext);
    }

    final static Logger logger = Logger.getLogger(ManagementPutApplications_StepDefs.class);

    @Given("^I am a PUT application authorized DRAGON user with Application.ReadWrite.All$")
    public void i_am_an_authorized_DRAGON_user() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPutApplication().setAuthTokenWithBearer(token));
    }

    @Given("^I am a PUT application authorized DRAGON user with ApplicationKey.ReadWrite.All$")
    public void i_am_an_authorized_DRAGON_incorrect_user() {
        common.iAmAnAuthorizedDragonUser(INCORRECT_ROLE_SET, token -> testContext.getApiManager().getPutApplication().setAuthTokenWithBearer(token));
    }

    @Given("^I am a PUT application DRAGON user with Application.ReadWrite.All with invalid \"([^\"]*)\"$")
    public void i_am_a_DRAGON_user_with_invalid_token(String token) {
        common.iAmADragonUserWithToken(token, tokenArg -> testContext.getApiManager().getPutApplication().setAuthToken(tokenArg));
    }

    @Given("^I have updated \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\" values$")
    public void i_have_updated_clientId_peakId_subUnitId_and_organisationId_values(String clientId, String peakId,
                                                                                   String subUnitId, String organisationId
    ) {
        testContext.getApiManager().getPutApplication().setClientId(getSubstituteValue(clientId));
        testContext.getApiManager().getPutApplication().setPeakId(getSubstituteValue(peakId));
        testContext.getApiManager().getPutApplication().setSubUnitId(getSubstituteValue(subUnitId));
        testContext.getApiManager().getPutApplication().setOrganisationId(getSubstituteValue(organisationId));
        testContext.getApiManager().getPutApplication().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getPutApplication().setTraceId(getGeneral().generateUniqueUUID());
    }


    @Given("^I have updated the \"([^\"]*)\" and platformId values$")
    public void updatedDescriptionAndPlatformValues(String description) {
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().getPutApplication().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        testContext.getApiManager().getPutApplication().setPlatformName(applicationResponse.getBody().path("application.platformName"));
        if(description.equalsIgnoreCase("superlargestring")) {
            String d = StringUtils.repeat("*", 257);
            testContext.getApiManager().getPutApplication().setDescription(d);
        } else{
            testContext.getApiManager().getPutApplication().setDescription(description);
        }
        testContext.getApiManager().getPutApplication().setPlatformId(applicationResponse.getBody().path("application.platformId"));
    }

    private String getSubstituteValue(String value) {
        if ("random".equalsIgnoreCase(value)) {
            return UUID.randomUUID().toString();
        } else if ("no_value".equalsIgnoreCase(value)) {
            return null;
        } else {
            return value;
        }
    }

    @When("^I make a PUT request to the application endpoint$")
    public void i_make_a_put_request_to_the_application_endpoint() throws IOException {
        logger.info("********** Executing PUT Application Request ***********");
        testContext.getApiManager().getPutApplication().executeRequest(
                getRestHelper().getBaseURI() + getFileHelper()
                        .getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) + "/" + testContext.getApiManager().getPutApplication().getApplicationId());
        logger.info("********** Executed PUT Application Request ***********");
    }


    @When("^I make a PUT request to the application endpoint with missing \"([^\"]*)\" value$")
    public void makeRequestWithMissingBodyValues(String missingBody) throws IOException {
        logger.info("********** Executing PUT Application Request ***********");
        testContext.getApiManager().getPutApplication().executeRequestWithMissingBody(missingBody,
                getRestHelper().getBaseURI() + getFileHelper()
                        .getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) + "/" + testContext.getApiManager().getPutApplication().getApplicationId());
        logger.info("********** Executed PUT Application Request ***********");
    }


    @When("^I make a PUT request to the application endpoint with invalid platformId \"([^\"]*)\" value and description \"([^\"]*)\"$")
    public void makeRequestWithInvalidBod(String platformId, String description) throws IOException {
        logger.info("********** Executing PUT Application Request ***********");
        testContext.getApiManager().getPutApplication().executeRequestWithInvalidInputBody(
                getRestHelper().getBaseURI() + getFileHelper()
                        .getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) + "/" + testContext.getApiManager().getPutApplication().getApplicationId(), platformId, description);
        logger.info("********** Executed PUT Application Request ***********");
    }


    @When("^I make a PUT request to the application endpoint with invalid \"([^\"]*)\"$")
    public void makeRequestWithInvalidApplicationId(String applicationId) throws IOException {
        logger.info("********** Executing PUT Application Request ***********");
        testContext.getApiManager().getPutApplication().executeRequest(
                getRestHelper().getBaseURI() + getFileHelper()
                        .getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) + "/" + applicationId);
        logger.info("********** Executed PUT Application Request ***********");
    }


    @When("^I make a PUT request to the application endpoint with no body")
    public void i_make_a_put_request_to_the_application_WithNoBody() throws IOException {
        logger.info("********** Executing PUT Application Request ***********");
        testContext.getApiManager().getPutApplication().executeRequestWithNoBody(
                getRestHelper().getBaseURI() + getFileHelper()
                        .getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) + "/" + testContext.getApiManager().getPutApplication().getApplicationId());
        logger.info("********** Executed PUT Application Request ***********");
    }

    @When("^I make a PUT request to the application endpoint with \"([^\"]*)\" missing in the header$")
    public void i_make_a_put_request_to_the_application_endpoint_with_key_missing_in_the_header(String key) {
        testContext.getApiManager().getPutApplication().executePutRequestWithMissingHeaderKeys(getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) + "/" + testContext.getApiManager().getPutApplication().getApplicationId(), key);
    }

    @Then("^I should receive a successful PUT application response$")
    public void i_should_receive_a_successful_put_applications_response() {
        Response response = testContext.getApiManager().getPutApplication().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), HttpStatus.SC_OK, "Request was not successful!");
        Assert.assertEquals(getRestHelper().getResponseHeaderValue(response, "X-Application-Context "), null, "Expects X-Application-Context header to not exists");
        Assert.assertNotNull(response, "The response for PUT application Request was null");
    }

    @Then("^validate the put application response$")
    public void validate_put_applications_response() {
        String response = testContext.getApiManager().getPutApplication().getResponse().getBody().asString();
        Assert.assertEquals(response.split(",").length, 11);
        Assert.assertEquals(testContext.getApiManager().getPutApplication().getResponse().getBody().path(Constants.APPLICATION_ID), testContext.getApiManager().getPutApplication().getApplicationId());
        Assert.assertTrue(response.contains(Constants.CLIENT_ID), testContext.getApiManager().getPutApplication().getClientId());
        Assert.assertTrue(response.contains(Constants.PEAK_ID), testContext.getApiManager().getPutApplication().getPeakId());
        Assert.assertTrue(response.contains(Constants.SUB_UNIT_ID), testContext.getApiManager().getPutApplication().getSubUnitId());
        Assert.assertTrue(response.contains(Constants.ORGANISATION_ID), testContext.getApiManager().getPutApplication().getOrganisationId());
        Assert.assertTrue(response.contains(Constants.PLATFORM_NAME), testContext.getApiManager().getPutApplication().getPlatformName());
        Assert.assertEquals(testContext.getApiManager().getPutApplication().getResponse().getBody().path(Constants.PLATFORM_ID), testContext.getApiManager().getPutApplication().getPlatformId());
        Assert.assertEquals(testContext.getApiManager().getPutApplication().getResponse().getBody().path(Constants.DESCRIPTION), testContext.getApiManager().getPutApplication().getDescription());

    }


    @Then("^the PUT response body should contain a valid applicationId, clientId, peakId, subUnitId, organisationId and description$")
    public void the_response_body_should_contain_a_valid_applicationId_clientId_peakId_subUnitId_and_organisationId() {
        Assert.assertNotNull(testContext.getApiManager().getPutApplication().applicationIdInResponse(), "applicationId is not present in the response!!");
        Assert.assertNotNull(testContext.getApiManager().getPutApplication().clientIdInResponse(), "clientId is not present in the response!!");
        Assert.assertNotNull(testContext.getApiManager().getPutApplication().peakIdInResponse(), "peakId is not present in the response!!");
        Assert.assertNotNull(testContext.getApiManager().getPutApplication().subUnitIdInResponse(), "subUnitId is not present in the response!!");
        Assert.assertNotNull(testContext.getApiManager().getPutApplication().organisationIdInResponse(), "organisationId is not present in the response!!");
        Assert.assertNotNull(testContext.getApiManager().getPutApplication().getDescription(), "description is not present in the response");
        Assert.assertEquals(testContext.getApiManager().getPutApplication().applicationIdInResponse(),
                testContext.getApiManager().getPutApplication().getApplicationId(),
                "applicationId returned does not match.");

        Assert.assertEquals(testContext.getApiManager().getPutApplication().clientIdInResponse(),
                testContext.getApiManager().getPutApplication().getClientId(),
                "clientId returned does not match.");

        Assert.assertEquals(testContext.getApiManager().getPutApplication().peakIdInResponse(),
                testContext.getApiManager().getPutApplication().getPeakId(),
                "peakId returned does not match.");

        Assert.assertEquals(testContext.getApiManager().getPutApplication().subUnitIdInResponse(),
                testContext.getApiManager().getPutApplication().getSubUnitId(),
                "subUnitId returned does not match.");

        Assert.assertEquals(testContext.getApiManager().getPutApplication().organisationIdInResponse(),
                testContext.getApiManager().getPutApplication().getOrganisationId(),
                "organisationId returned does not match.");

        Assert.assertEquals(testContext.getApiManager().getPutApplication().descriptionInResponse(),
                testContext.getApiManager().getPutApplication().getDescription(),
                "description returned does not match.");
    }

    @Then("^the PUT response body should also have empty notificationPath and empty notificationHost$")
    public void the_response_body_should_also_have_empty_notificationPath_and_empty_notificationHost() throws Throwable {
        Assert.assertNotNull(testContext.getApiManager().getPutApplication().notificationPathInResponse(), "notificationPath is not present in the response!!");
        Assert.assertNotNull(testContext.getApiManager().getPutApplication().notificationHostInResponse(), "notificationHost is not present in the response!!");
        Assert.assertEquals(testContext.getApiManager().getPutApplication().notificationPathInResponse(),
                "",
                "notificationPath returned is not the empty string");

        Assert.assertEquals(testContext.getApiManager().getPutApplication().notificationHostInResponse(),
                "",
                "notificationHost returned is not the empty string");
    }

    @Then("^I should receive a quoted \"([^\"]*)\" error response with \'(.*)\' error description and \"([^\"]*)\" errorcode within the PUT application response$")
    public void i_should_receive_a_quoted_error_response_with_error_description_and_errorcode(int responseCode, String errorDesc, String errorCode) {
        i_should_receive_an_error_response_with_error_description_and_errorcode(responseCode, errorDesc, errorCode);
    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within the PUT application response$")
    public void i_should_receive_an_error_response_with_error_description_and_errorcode(int responseCode, String errorDesc, String errorCode) {
        Response response = testContext.getApiManager().getPutApplication().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), responseCode, "Different response code being returned");
        if (getRestHelper().getErrorDescription(response) != null) {
            if (getRestHelper().getErrorDescription(response).contains("'")) {
            }
            Assert.assertTrue(
                    getRestHelper().getErrorDescription(response)
                            .replace("\"", "")
                            .contains(errorDesc),
                    "Different error description being returned..Expected: " + errorDesc + "Actual: " + getRestHelper().getErrorDescription(response));
        }
        Assert.assertEquals(getRestHelper().getErrorCode(response), errorCode, "Different error code being returned");
    }

    @Then("^error message should be \"([^\"]*)\" within the PUT application response$")
    public void i_should_receive_a_error_message(String errorMessage) {
        Response response = testContext.getApiManager().getPutApplication().getResponse();
        Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned..Expected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(response));
    }

    @And("^I retrieve the applicationId and the keyId from the response$")
    public void iRetrieveTheKeyIdFromTheResponse() {
        testContext.getApiManager().getPostPublicKey().getResponse();
        HashMap dataMap = (HashMap) testContext.getApiManager().getPostPublicKey().getResponse().path(".");
        String newKeyId = dataMap.get("keyId").toString();
        String returnedApplicationId = dataMap.get("applicationId").toString();
        testContext.getApiManager().getPutPublicKeys().setKeyId(newKeyId);
        testContext.getApiManager().getPutPublicKeys().setApplicationId(returnedApplicationId);
    }
}
