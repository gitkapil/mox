package steps;

import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Constants;
import utils.DataBaseConnector;

import java.sql.Array;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class OneClickMerchantOnboarding_StepDefs extends UtilManager {

    // NB: These are the dragon token (for testing) roles {"roles": ["Application.ReadWrite.All"]}.  CSO tokens use claim {"role": "user"}
    private static final Set<String> ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final Set<String> INCORRECT_ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String SIG_HEADER_LIST_POST_APPLICATION = "header-list-post-application";

    TestContext testContext;
    ManagementCommon common;

    Response response;
    String oneClickURL;
    Response resp_one;

    public OneClickMerchantOnboarding_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }

    final static Logger logger = Logger.getLogger(ManagementPostApplications_StepDefs.class);


    @Given("^I am logging in as a user with correct privileges$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getOneClickMerchantOnboarding().setAuthTokenWithBearer(token));
    }

    public void makeRequest() {
        String url = getRestHelper().getBaseURI() + "onboarding";
        this.oneClickURL = url;
        testContext.getApiManager().getOneClickMerchantOnboarding().makeRequest(url);
    }

    public void makeInvalidRequest(String key, String invalidValue) {
        String url = getRestHelper().getBaseURI() + "onboarding";
        this.oneClickURL = url;
        testContext.getApiManager().getOneClickMerchantOnboarding().makeInvalidOnboardRequest(url, key, invalidValue);
    }

    @Then("^I should receive a successful merchant onboarding response$")
    public void iShouldReceiveASuccessfulMerchantOnboardingResponse() {
        response = testContext.getApiManager().getOneClickMerchantOnboarding().getOneClickOnboardingRequestResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), 201, "Request was not successful!");
        Assert.assertEquals(getRestHelper().getResponseHeaderValue(response, "X-Application-Context "), null, "Expects X-Application-Context header to not exists");
        Assert.assertNotNull(response, "The response for Post One Click Merchant Onboarding Request was null");
    }

    @And("^verify the response body contains all mandatory details$")
    public void verifyTheResponseBodyContainsAllMandatoryDetails() {

        HashMap applicationResponse = response.path("application");
        HashMap signingKeyResponse = response.path("signingKey");
        HashMap passwordMetadataResponse = response.path("passwordMetadata");

        //Validate application response details
        Assert.assertNotNull(applicationResponse.get(Constants.APPLICATION_ID), "applicationId cannot be null!");
        Assert.assertNotNull(applicationResponse.get(Constants.CLIENT_ID), "clientId cannot be null!");
        Assert.assertEquals(applicationResponse.get(Constants.PEAK_ID), testContext.getApiManager().getOneClickMerchantOnboarding().getPeakId(), "peakId should be same as provided in request body!");
        Assert.assertEquals(applicationResponse.get(Constants.SUB_UNIT_ID), testContext.getApiManager().getOneClickMerchantOnboarding().getSubUnitId(), "subUnitId should be same as provided in request body!");
        Assert.assertEquals(applicationResponse.get(Constants.ORGANISATION_ID), testContext.getApiManager().getOneClickMerchantOnboarding().getOrganisationId(), "organisationId should be same as provided in request body!");
        Assert.assertEquals(applicationResponse.get(Constants.PLATFORM_ID), testContext.getApiManager().getOneClickMerchantOnboarding().getPlatformId(), "platformId should be same as provided in request body!");
        Assert.assertNotNull(applicationResponse.get(Constants.PLATFORM_NAME), "platformName should not be null!");
        Assert.assertEquals(applicationResponse.get(Constants.APPLICATION_DESCRIPTION), testContext.getApiManager().getOneClickMerchantOnboarding().getDescription(), "applicationDescription should be same as provided in request body!");

        //Validate signingKey response details
        Assert.assertNotNull(signingKeyResponse.get(Constants.KEY_ID), "Signing keyId cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get(Constants.ALG), "Signing alg cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get(Constants.TYPE), "Signing type cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get(Constants.SIZE), "Signing size cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get(Constants.ACTIVATE_AT), "Signing activateAt cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get(Constants.DEACTIVAT_AT), "Signing deactivateAt cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get(Constants.ENTITY_STATUS), "Signing entityStatus cannot be null!");

        //Validate passwordMetadata response details
        Assert.assertNotNull(passwordMetadataResponse.get(Constants.KEY_ID), "passwordMetada keyId cannot be null!");
        Assert.assertNotNull(passwordMetadataResponse.get(Constants.ACTIVATE_AT), "passwordMetada activateAt cannot be null!");
        Assert.assertNotNull(passwordMetadataResponse.get(Constants.DEACTIVAT_AT), "passwordMetada deactivateAt cannot be null!");

        //Validate other response body parameters
        Assert.assertNotNull(response.path(Constants.GRANT_URL), "grantUrl cannot be null!");
        Assert.assertNotNull(response.path(Constants.PDF_URL), "pdfUrl cannot be null!");
        Assert.assertNotNull(response.path(Constants.PDF_PIN), "pdfPin cannot be null!");
        Assert.assertEquals(response.path(Constants.PDF_PIN).toString().length(), 16, "pdfPin should be 16 characters.");
    }

    @When("^I make request to one click merchant onboard endpoint with \"([^\"]*)\" missing in the header$")
    public void iMakeRequestToOneClickMerchantOnboardEndpointWithMissingInTheHeader(String key) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().executeRequestWithMissingHeaderKeys(getRestHelper().getBaseURI() + "onboarding", key, testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                Sets.newHashSet(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, SIG_HEADER_LIST_POST_APPLICATION).split(",")));
    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode in response$")
    public void iShouldReceiveAErrorResponseWithErrorDescriptionAndErrorcodeInResponse(int responseCode, String errorDesc, String errorCode) throws Throwable {
        Response response = testContext.getApiManager().getOneClickMerchantOnboarding().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), responseCode, "Different response code being returned");

        String applicationNameErrorDescription = "Field error in object 'onboardingInputModel': field 'applicationName' must match \".*-(sandbox|merchant)-client-app\"; rejected value []";

        if (getRestHelper().getErrorDescription(response) != null) {

            if (getRestHelper().getErrorDescription(response).contains("'")) {
                System.out.println("here : " + getRestHelper().getErrorDescription(response));
                System.out.println("there: " + errorDesc);
            }
            if (errorDesc.equalsIgnoreCase("applicationNameErrorDescription")) {
                Assert.assertTrue(getRestHelper().getErrorDescription(response).contains(applicationNameErrorDescription),
                        "Different error description being returned..Expected: " + applicationNameErrorDescription + "Actual: " + getRestHelper().getErrorDescription(response));
            } else {
                Assert.assertTrue(
                        getRestHelper().getErrorDescription(response)
                                .replace("\"", "")
                                .contains(errorDesc),
                        "Different error description being returned..Expected: " + errorDesc + "Actual: " + getRestHelper().getErrorDescription(response));
            }
        }

        Assert.assertEquals(getRestHelper().getErrorCode(response), errorCode, "Different error code being returned");
    }

    @And("^error message should be \"([^\"]*)\" within the response$")
    public void errorMessageShouldBeWithinTheResponse(String errorMessage) throws Throwable {
        Response response = testContext.getApiManager().getOneClickMerchantOnboarding().getResponse();
        Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned..Expected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(response));
    }

    @Then("^I should receive a \"([^\"]*)\" status code in response$")
    public void iShouldReceiveAStatusCodeInResponse(int statusCode) throws Throwable {
        Response response = testContext.getApiManager().getOneClickMerchantOnboarding().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), statusCode, "Different statusCode being returned");
    }

    @When("^I make request to one click merchant onboard endpoint with invalid key \"([^\"]*)\" for \"([^\"]*)\" in header$")
    public void iMakeRequestToOneClickMerchantOnboardEndpointWithInvalidKeyForInHeader(String invalidValue, String key) {
        testContext.getApiManager().getOneClickMerchantOnboarding().executeRequestWithInvalidHeaderKeys(getRestHelper().getBaseURI() + "onboarding", key, invalidValue, testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                Sets.newHashSet(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, SIG_HEADER_LIST_POST_APPLICATION).split(",")));
    }

    @Given("^I am a DRAGON user with invalid \"([^\"]*)\" auth token$")
    public void iAmADRAGONUserWithInvalidAuthToken(String token) throws Throwable {
        common.iAmADragonUserWithToken(token, tokenArg -> testContext.getApiManager().getOneClickMerchantOnboarding().setAuthToken(tokenArg));
    }

    @When("^I make request to one click merchant onboard endpoint$")
    public void iMakeRequestToOneClickMerchantOnboardEndpoint() {
        logger.info("********** Executing POST One Click Merchant Onboarding Request ***********");

        testContext.getApiManager().getOneClickMerchantOnboarding().executeRequest(
                getRestHelper().getBaseURI() + "onboarding",
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                Sets.newHashSet(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        SIG_HEADER_LIST_POST_APPLICATION).split(",")));
    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within one click onboard response$")
    public void iShouldReceiveAErrorResponseWithErrorDescriptionAndErrorcodeWithinOneClickOnboardResponse(int responseCode, String errorDesc, String errorCode) throws Throwable {
        Response response = testContext.getApiManager().getOneClickMerchantOnboarding().getResponse();

        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), responseCode, "Expected Response Code: " + responseCode + "Actual: " + response.getStatusCode());

        if (getRestHelper().getErrorDescription(response) != null) {
            if (getRestHelper().getErrorDescription(response).contains("'")) {
                System.out.println("here : " + getRestHelper().getErrorDescription(response));
                System.out.println("there: " + errorDesc);
            }
            Assert.assertTrue(
                    getRestHelper().getErrorDescription(response)
                            .replace("\"", "")
                            .contains(errorDesc),
                    "Different error description being returned..Expected: " + errorDesc + "Actual: " + getRestHelper().getErrorDescription(response));
        }
        Assert.assertEquals(getRestHelper().getErrorCode(response), errorCode, "Different error code being returned");

    }

    @And("^error message should be \"([^\"]*)\" within one click onboard response$")
    public void errorMessageShouldBeWithinOneClickOnboardResponse(String errorMessage) throws Throwable {
        Response response = testContext.getApiManager().getOneClickMerchantOnboarding().getResponse();
        Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned..Expected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(response));
    }

    @When("^I make request to one click merchant onboard endpoint with null request body$")
    public void iMakeRequestToOneClickMerchantOnboardEndpointWithNullRequestBody() {
        logger.info("********** Executing POST One Click Merchant Onboarding Request ***********");

        testContext.getApiManager().getOneClickMerchantOnboarding().executeRequestNullBody(
                getRestHelper().getBaseURI() + "onboarding",
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                Sets.newHashSet(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        SIG_HEADER_LIST_POST_APPLICATION).split(",")));
    }

    @Then("^I should receive a \"([^\"]*)\" http code with \"([^\"]*)\" error message$")
    public void iShouldReceiveAHttpCodeWithErrorMessage(int responseCode, String message) throws Throwable {
        Response response = testContext.getApiManager().getOneClickMerchantOnboarding().getResponse();
        Assert.assertEquals(response.getStatusCode(), responseCode, "Response code returned " + response.getStatusCode() + "\n Expected: " + responseCode);
        Assert.assertEquals(getRestHelper().getErrorMessage(response), message, "Error message returned " + getRestHelper().getErrorMessage(response) + "\n Expected: " + message);
    }

    @And("^Validate errorCode and errorDescription within one click onboard response$")
    public void validateErrorCodeAndErrorDescriptionWithinOneClickOnboardResponse() {
        Response response = testContext.getApiManager().getOneClickMerchantOnboarding().getResponse();

        //Assertion included in method getErrorDescriptionsOneClick(response)
        getRestHelper().getErrorDescriptionsOneClick(response);

        //Assertion included in method getErrorCodeOneClick(response)
        getRestHelper().getErrorCodeOneClick(response);
    }

    @When("^I make request to one click merchant onboard endpoint without request body$")
    public void iMakeRequestToOneClickMerchantOnboardEndpointWithoutRequestBody() {
        logger.info("********** Executing POST One Click Merchant Onboarding Request without Body ***********");

        testContext.getApiManager().getOneClickMerchantOnboarding().executeRequestWithoutBody(
                getRestHelper().getBaseURI() + "onboarding",
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                Sets.newHashSet(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        SIG_HEADER_LIST_POST_APPLICATION).split(",")));
    }

    @When("^I make request for a new client with name as \"([^\"]*)\", peakId as \"([^\"]*)\", subUnitId as \"([^\"]*)\", organisationId as \"([^\"]*)\", description as \"([^\"]*)\" and platformId as \"([^\"]*)\"$")
    public void iMakeRequestForANewClientWithNameAsPeakIdAsSubUnitIdAsOrganisationIdAsDescriptionAsAndPlatformIdAs(String applicationName, String peakId, String subUnitId, String organisationId, String description, String platformId) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatformId(platformId);
        makeRequest();
    }

    @When("^I provide application name as \"([^\"]*)\", peakId as \"([^\"]*)\", subUnitId as \"([^\"]*)\", organisationId as \"([^\"]*)\", description as \"([^\"]*)\" and platformId as \"([^\"]*)\" in request body with invalid key \"([^\"]*)\" for \"([^\"]*)\" in header$")
    public void iProvideApplicationNameAsPeakIdAsSubUnitIdAsOrganisationIdAsDescriptionAsAndPlatformIdAsInRequestBodyWithInvalidKeyForInHeader(String applicationName, String peakId, String subUnitId, String organisationId, String description, String platformId, String invalidValue, String key) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatformId(platformId);
        makeInvalidRequest(key, invalidValue);
    }

    @When("^I make request for a new client with peakId as \"([^\"]*)\", subUnitId as \"([^\"]*)\", organisationId as \"([^\"]*)\", description as \"([^\"]*)\" and platformId as \"([^\"]*)\"$")
    public void iMakeRequestForANewClientWithPeakIdAsSubUnitIdAsOrganisationIdAsDescriptionAsAndPlatformIdAs(String peakId, String subUnitId, String organisationId, String description, String platformId) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatformId(platformId);
        makeRequest();
    }

    @When("^I make request for a new client with name as \"([^\"]*)\", subUnitId as \"([^\"]*)\", organisationId as \"([^\"]*)\", description as \"([^\"]*)\" and platformId as \"([^\"]*)\"$")
    public void iMakeRequestForANewClientWithNameAsSubUnitIdAsOrganisationIdAsDescriptionAsAndPlatformIdAs(String applicationName, String subUnitId, String organisationId, String description, String platformId) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatformId(platformId);
        makeRequest();
    }

    @When("^I make request for a new client with name as \"([^\"]*)\", peakId as \"([^\"]*)\", organisationId as \"([^\"]*)\", description as \"([^\"]*)\" and platformId as \"([^\"]*)\"$")
    public void iMakeRequestForANewClientWithNameAsPeakIdAsOrganisationIdAsDescriptionAsAndPlatformIdAs(String applicationName, String peakId, String organisationId, String description, String platformId) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatformId(platformId);
        makeRequest();
    }

    @When("^I make request for a new client with name as \"([^\"]*)\", peakId as \"([^\"]*)\", subUnitId as \"([^\"]*)\", description as \"([^\"]*)\" and platformId as \"([^\"]*)\"$")
    public void iMakeRequestForANewClientWithNameAsPeakIdAsSubUnitIdAsDescriptionAsAndPlatformIdAs(String applicationName, String peakId, String subUnitId, String description, String platformId) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatformId(platformId);
        makeRequest();
    }

    @When("^I make request for a new client with name as \"([^\"]*)\", peakId as \"([^\"]*)\", subUnitId as \"([^\"]*)\", organisationId as \"([^\"]*)\" and description as \"([^\"]*)\"$")
    public void iMakeRequestForANewClientWithNameAsPeakIdAsSubUnitIdAsOrganisationIdAsAndDescriptionAs(String applicationName, String peakId, String subUnitId, String organisationId, String description) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        makeRequest();
    }

    @When("^I make request for a new client with name as \"([^\"]*)\", peakId as \"([^\"]*)\", subUnitId as \"([^\"]*)\", organisationId as \"([^\"]*)\" and platformId as \"([^\"]*)\"$")
    public void iMakeRequestForANewClientWithNameAsPeakIdAsSubUnitIdAsOrganisationIdAsAndPlatformIdAs(String applicationName, String peakId, String subUnitId, String organisationId, String platformId) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatformId(platformId);
        makeRequest();
    }

    @And("^validate \"([^\"]*)\" and platformName from database$")
    public void validateAndPlatformNameFromDatabase(String platformId) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        response = testContext.getApiManager().getOneClickMerchantOnboarding().getOneClickOnboardingRequestResponse();

        //validate response body platformId is equal to request body platformId
        HashMap applicationResp = response.path("application");
        Object platformId_resp = applicationResp.get("platformId");
        Object platformName_resp = applicationResp.get("platformName");

        Assert.assertEquals(platformId_resp, platformId, "Actual platformId doesn't match with Expected platformId!! Expected platformId is " + platformId + "but found " + platformId_resp);

        //validate platformId is present in database
        String platformId_resp_api = platformId_resp.toString().replaceAll("-", "").toUpperCase();

        String sqlQuery = "SELECT hex(PLTFM_ID), PLTFM_NAME FROM merchant_management.pltfm WHERE hex(PLTFM_ID)='" + platformId_resp_api + "';";
        String db_resp = DataBaseConnector.executeSQLQuery_List("CI", sqlQuery, Constants.DB_CONNECTION_URL).toString();
        System.out.println("db_resp: " + db_resp);
        String arr[] = db_resp.split(" ");
        String db_platformId = arr[0].replace("[", "");
        String db_platformName = arr[1].replace("]", "");
        System.out.println("db_platformId = " + db_platformId);
        System.out.println("db_platformName = " + db_platformName);
        Assert.assertEquals(db_platformId, platformId_resp_api, "API response platformId doesn't match with expected platformId in database");

        //Validate platformName corresponds to the platformId
        Assert.assertEquals(platformName_resp, db_platformName, "platformName in API response should be equal to platformName in DB.");
    }

    @When("^I make request for existing client with name as \"([^\"]*)\", peakId as \"([^\"]*)\", subUnitId as \"([^\"]*)\", organisationId as \"([^\"]*)\", description as \"([^\"]*)\" and platformId as \"([^\"]*)\"$")
    public void iMakeRequestForExistingClientWithNameAsPeakIdAsSubUnitIdAsOrganisationIdAsDescriptionAsAndPlatformIdAs(String applicationName, String peakId, String subUnitId, String organisationId, String description, String platformId) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatformId(platformId);
        makeRequest();
    }

    @Then("^I should receive a success \"([^\"]*)\" status response$")
    public void iShouldReceiveASuccessStatusResponse(int statusCode) {
        response = testContext.getApiManager().getOneClickMerchantOnboarding().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), statusCode, "Different statusCode being returned");
    }

    @And("^verify the response body contains \"([^\"]*)\" and \"([^\"]*)\"$")
    public void verifyTheResponseBodyContainsAnd(String reasonCode, String reasonDescription) throws Throwable {
        HashMap applicationResponse = response.path("application");
        HashMap signingKeyResponse = response.path("signingKey");
        HashMap passwordMetadataResponse = response.path("passwordMetadata");

        //Validate reasonCode and reasonDescription
        Assert.assertEquals(response.path(Constants.REASON_CODE), reasonCode, "reasonCode is different!");
        Assert.assertEquals(response.path(Constants.REASON_DESCRIPTION), reasonDescription, "reasonDescription is different!");

        //Validate application response details
        Assert.assertNotNull(applicationResponse.get(Constants.APPLICATION_ID), "applicationId cannot be null!");
        Assert.assertNotNull(applicationResponse.get(Constants.CLIENT_ID), "clientId cannot be null!");
        Assert.assertEquals(applicationResponse.get(Constants.PEAK_ID), testContext.getApiManager().getOneClickMerchantOnboarding().getPeakId(), "peakId should be same as provided in request body!");
        Assert.assertEquals(applicationResponse.get(Constants.SUB_UNIT_ID), testContext.getApiManager().getOneClickMerchantOnboarding().getSubUnitId(), "subUnitId should be same as provided in request body!");
        Assert.assertEquals(applicationResponse.get(Constants.ORGANISATION_ID), testContext.getApiManager().getOneClickMerchantOnboarding().getOrganisationId(), "organisationId should be same as provided in request body!");
        Assert.assertEquals(applicationResponse.get(Constants.PLATFORM_ID), testContext.getApiManager().getOneClickMerchantOnboarding().getPlatformId(), "platformId should be same as provided in request body!");
        Assert.assertNotNull(applicationResponse.get(Constants.PLATFORM_NAME), "platformName should not be null!");
        Assert.assertEquals(applicationResponse.get(Constants.APPLICATION_DESCRIPTION), testContext.getApiManager().getOneClickMerchantOnboarding().getDescription(), "applicationDescription should be same as provided in request body!");

        //Validate signingKey response details
        Assert.assertNotNull(signingKeyResponse.get(Constants.KEY_ID), "Signing keyId cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get(Constants.ALG), "Signing alg cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get(Constants.TYPE), "Signing type cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get(Constants.SIZE), "Signing size cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get(Constants.ACTIVATE_AT), "Signing activateAt cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get(Constants.DEACTIVAT_AT), "Signing deactivateAt cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get(Constants.ENTITY_STATUS), "Signing entityStatus cannot be null!");

        //Validate passwordMetadata response details
        Assert.assertNotNull(passwordMetadataResponse.get(Constants.KEY_ID), "passwordMetada keyId cannot be null!");
        Assert.assertNotNull(passwordMetadataResponse.get(Constants.ACTIVATE_AT), "passwordMetada activateAt cannot be null!");
        Assert.assertNotNull(passwordMetadataResponse.get(Constants.DEACTIVAT_AT), "passwordMetada deactivateAt cannot be null!");

        //Validate other response body parameters
        Assert.assertNotNull(response.path(Constants.GRANT_URL), "grantUrl cannot be null!");
        Assert.assertNotNull(response.path(Constants.PDF_URL), "pdfUrl cannot be null!");
        Assert.assertNotNull(response.path(Constants.PDF_PIN), "pdfPin cannot be null!");
        Assert.assertEquals(response.path(Constants.PDF_PIN).toString().length(), 16, "pdfPin should be 16 characters.");
    }

    @When("^I make request for same client with same applicationName, peakId as \"([^\"]*)\", subUnitId as \"([^\"]*)\", organisationId as \"([^\"]*)\", description as \"([^\"]*)\" and platformId as \"([^\"]*)\"$")
    public void iMakeRequestForSameClientWithSameApplicationNamePeakIdAsSubUnitIdAsOrganisationIdAsDescriptionAsAndPlatformIdAs(String peakId, String subUnitId, String organisationId, String description, String platformId) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatformId(platformId);
        makeRequest();
    }

    @And("^verify the response body should be returned for same client$")
    public void verifyTheResponseBodyShouldBeReturnedForSameClient() {

        HashMap applicationResponse_one = resp_one.path("application");
        HashMap signingKeyResponse_one = resp_one.path("signingKey");

        HashMap applicationResponse_two = response.path("application");
        HashMap signingKeyResponse_two = response.path("signingKey");

        //Validate application response details
        Assert.assertEquals(applicationResponse_two.get(Constants.APPLICATION_ID), applicationResponse_one.get("applicationId"), "applicationId isn't same!");
        Assert.assertEquals(applicationResponse_two.get(Constants.CLIENT_ID), applicationResponse_one.get("clientId"), "clientId isn't same!");
        Assert.assertEquals(applicationResponse_two.get(Constants.PEAK_ID), applicationResponse_one.get("peakId"), "peakId isn't same!");
        Assert.assertEquals(applicationResponse_two.get(Constants.SUB_UNIT_ID), applicationResponse_one.get("subUnitId"), "subUnitId isn't same!");
        Assert.assertEquals(applicationResponse_two.get(Constants.ORGANISATION_ID), applicationResponse_one.get("organisationId"), "organisationId isn't same!");
        Assert.assertEquals(applicationResponse_two.get(Constants.PLATFORM_ID), applicationResponse_one.get("platformId"), "platformId isn't same!");
        Assert.assertEquals(applicationResponse_two.get(Constants.PLATFORM_NAME), applicationResponse_one.get("platformName"), "platformName isn't same!");
        Assert.assertEquals(applicationResponse_two.get(Constants.APPLICATION_DESCRIPTION), applicationResponse_one.get("applicationDescription"), "applicationDescription isn't same!");

        //Validate signingKey response details
        Assert.assertEquals(signingKeyResponse_two.get(Constants.KEY_ID), signingKeyResponse_one.get("keyId"), "Signing keyId isn't same!");
        Assert.assertEquals(signingKeyResponse_two.get(Constants.KEY_NAME), signingKeyResponse_one.get("keyName"), "Signing keyName isn't same!");
        Assert.assertEquals(signingKeyResponse_two.get(Constants.ALG), signingKeyResponse_one.get("alg"), "Signing alg isn't same!");
        Assert.assertEquals(signingKeyResponse_two.get(Constants.TYPE), signingKeyResponse_one.get("type"), "Signing type isn't same!");
        Assert.assertEquals(signingKeyResponse_two.get(Constants.SIZE), signingKeyResponse_one.get("size"), "Signing size isn't same!");
        Assert.assertEquals(signingKeyResponse_two.get(Constants.ENTITY_STATUS), signingKeyResponse_one.get("entityStatus"), "Signing entityStatus isn't same!");

        Assert.assertEquals(response.path(Constants.GRANT_URL).toString(), resp_one.path("grantUrl"), "grantUrl isn't same!");
    }

    @And("^store the response of first API hit$")
    public void storeTheResponseOfFirstAPIHit() {
        resp_one = response;
    }

    @And("^Validate errorCodes and errorDescriptions in response$")
    public void validateErrorCodesAndErrorDescriptionsInResponse() {
        Response response = testContext.getApiManager().getOneClickMerchantOnboarding().getResponse();
        //Assertion included in method getErrorDescriptionsLongName
        getRestHelper().getErrorDescriptionsLongName(response);

        //Assertion included in method getErrorCodeOneClick(response)
        getRestHelper().getErrorCodeOneClick(response);
    }
}
