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

    @When("^I make request for a new client with name as \"([^\"]*)\", peakId as \"([^\"]*)\", subUnitId as \"([^\"]*)\", organisationId as \"([^\"]*)\", description as \"([^\"]*)\", platform as \"([^\"]*)\", pdfChannel as \"([^\"]*)\" and passwordChannel as \"([^\"]*)\"$")
    public void iMakeRequest(String applicationName, String peakId, String subUnitId, String organisationId, String description, String platform, String pdfChannel, String passwordChannel) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatform(platform);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPdfChannel(pdfChannel);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPasswordChannel(passwordChannel);
        makeRequest();
    }

    @And("^verify the response body contains all mandatory details$")
    public void verifyTheResponseBodyContainsAllMandatoryDetails() {

        HashMap applicationResponse = response.path("application");
        HashMap signingKeyResponse = response.path("signingKey");
        HashMap passwordMetadataResponse = response.path("passwordMetadata");

        //Validate application response details
        Assert.assertNotNull(applicationResponse.get("applicationId"), "applicationId cannot be null!");
        Assert.assertNotNull(applicationResponse.get("clientId"), "clientId cannot be null!");
        Assert.assertEquals(applicationResponse.get("peakId"), testContext.getApiManager().getOneClickMerchantOnboarding().getPeakId(), "peakId should be same as provided in request body!");
        Assert.assertEquals(applicationResponse.get("subUnitId"), testContext.getApiManager().getOneClickMerchantOnboarding().getSubUnitId(), "subUnitId should be same as provided in request body!");
        Assert.assertEquals(applicationResponse.get("organisationId"), testContext.getApiManager().getOneClickMerchantOnboarding().getOrganisationId(), "organisationId should be same as provided in request body!");
        Assert.assertEquals(applicationResponse.get("platform"), testContext.getApiManager().getOneClickMerchantOnboarding().getPlatform(), "platform should be same as provided in request body!");
        Assert.assertEquals(applicationResponse.get("applicationDescription"), testContext.getApiManager().getOneClickMerchantOnboarding().getDescription(), "applicationDescription should be same as provided in request body!");

        //Validate signingKey response details
        Assert.assertNotNull(signingKeyResponse.get("keyId"), "Signing keyId cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get("keyName"), "Signing keyName cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get("alg"), "Signing alg cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get("type"), "Signing type cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get("size"), "Signing size cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get("activateAt"), "Signing activateAt cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get("deactivateAt"), "Signing deactivateAt cannot be null!");
        Assert.assertNotNull(signingKeyResponse.get("entityStatus"), "Signing entityStatus cannot be null!");

        //Validate passwordMetadata response details
        Assert.assertNotNull(passwordMetadataResponse.get("keyId"), "passwordMetada keyId cannot be null!");
        Assert.assertNotNull(passwordMetadataResponse.get("activateAt"), "passwordMetada activateAt cannot be null!");
        Assert.assertNotNull(passwordMetadataResponse.get("deactivateAt"), "passwordMetada deactivateAt cannot be null!");

        //Validate other response body parameters
        Assert.assertNotNull(response.path("grantUrl"), "grantUrl cannot be null!");
        Assert.assertNotNull(response.path("pdfStatus"), "pdfStatus cannot be null!");
        Assert.assertEquals(response.path("pdfStatus"), "Sent", "pdfStatus should be Sent!!");
        Assert.assertNotNull(response.path("pdfPin"), "pdfPin cannot be null!");
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

    @When("^I provide application name as \"([^\"]*)\", peakId as \"([^\"]*)\", subUnitId as \"([^\"]*)\", organisationId as \"([^\"]*)\", description as \"([^\"]*)\", platform as \"([^\"]*)\", pdfChannel as \"([^\"]*)\" and passwordChannel as \"([^\"]*)\" in request body with invalid key \"([^\"]*)\" for \"([^\"]*)\" in header$")
    public void iProvideRequestBody(String applicationName, String peakId, String subUnitId, String organisationId, String description, String platform, String pdfChannel, String passwordChannel, String invalidValue, String key) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatform(platform);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPdfChannel(pdfChannel);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPasswordChannel(passwordChannel);
        makeInvalidRequest(key, invalidValue);
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

    @When("^I make request for a new client with peakId as \"([^\"]*)\", subUnitId as \"([^\"]*)\", organisationId as \"([^\"]*)\", description as \"([^\"]*)\", platform as \"([^\"]*)\", pdfChannel as \"([^\"]*)\" and passwordChannel as \"([^\"]*)\"$")
    public void iMakeRequestForANewClientWithPeakIdAsSubUnitIdAsOrganisationIdAsDescriptionAsPlatformAsPdfChannelAsAndPasswordChannelAs(String peakId, String subUnitId, String organisationId, String description, String platform, String pdfChannel, String passwordChannel) throws Throwable {
        //testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatform(platform);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPdfChannel(pdfChannel);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPasswordChannel(passwordChannel);
        makeRequest();
    }

    @When("^I make request for a new client with name as \"([^\"]*)\", subUnitId as \"([^\"]*)\", organisationId as \"([^\"]*)\", description as \"([^\"]*)\", platform as \"([^\"]*)\", pdfChannel as \"([^\"]*)\" and passwordChannel as \"([^\"]*)\"$")
    public void iMakeRequestForANewClientWithNameAsSubUnitIdAsOrganisationIdAsDescriptionAsPlatformAsPdfChannelAsAndPasswordChannelAs(String applicationName, String subUnitId, String organisationId, String description, String platform, String pdfChannel, String passwordChannel) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        //testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatform(platform);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPdfChannel(pdfChannel);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPasswordChannel(passwordChannel);
        makeRequest();
    }

    @When("^I make request for a new client with name as \"([^\"]*)\", peakId as \"([^\"]*)\", organisationId as \"([^\"]*)\", description as \"([^\"]*)\", platform as \"([^\"]*)\", pdfChannel as \"([^\"]*)\" and passwordChannel as \"([^\"]*)\"$")
    public void iMakeRequestForANewClientWithNameAsPeakIdAsOrganisationIdAsDescriptionAsPlatformAsPdfChannelAsAndPasswordChannelAs(String applicationName, String peakId, String organisationId, String description, String platform, String pdfChannel, String passwordChannel) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        //testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatform(platform);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPdfChannel(pdfChannel);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPasswordChannel(passwordChannel);
        makeRequest();
    }

    @When("^I make request for a new client with name as \"([^\"]*)\", peakId as \"([^\"]*)\", subUnitId as \"([^\"]*)\", description as \"([^\"]*)\", platform as \"([^\"]*)\", pdfChannel as \"([^\"]*)\" and passwordChannel as \"([^\"]*)\"$")
    public void iMakeRequestForANewClientWithNameAsPeakIdAsSubUnitIdAsDescriptionAsPlatformAsPdfChannelAsAndPasswordChannelAs(String applicationName, String peakId, String subUnitId, String description, String platform, String pdfChannel, String passwordChannel) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        //testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatform(platform);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPdfChannel(pdfChannel);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPasswordChannel(passwordChannel);
        makeRequest();
    }

    @When("^I make request for a new client with name as \"([^\"]*)\", peakId as \"([^\"]*)\", subUnitId as \"([^\"]*)\", organisationId as \"([^\"]*)\", description as \"([^\"]*)\", pdfChannel as \"([^\"]*)\" and passwordChannel as \"([^\"]*)\"$")
    public void iMakeRequestForANewClientWithNameAsPeakIdAsSubUnitIdAsOrganisationIdAsDescriptionAsPdfChannelAsAndPasswordChannelAs(String applicationName, String peakId, String subUnitId, String organisationId, String description, String pdfChannel, String passwordChannel) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        //testContext.getApiManager().getOneClickMerchantOnboarding().setPlatform(platform);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPdfChannel(pdfChannel);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPasswordChannel(passwordChannel);
        makeRequest();
    }

    @When("^I make request for a new client with name as \"([^\"]*)\", peakId as \"([^\"]*)\", subUnitId as \"([^\"]*)\", organisationId as \"([^\"]*)\", description as \"([^\"]*)\", platform as \"([^\"]*)\" and passwordChannel as \"([^\"]*)\"$")
    public void iMakeRequestForANewClientWithNameAsPeakIdAsSubUnitIdAsOrganisationIdAsDescriptionAsPlatformAsAndPasswordChannelAs(String applicationName, String peakId, String subUnitId, String organisationId, String description, String platform, String passwordChannel) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatform(platform);
        //testContext.getApiManager().getOneClickMerchantOnboarding().setPdfChannel(pdfChannel);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPasswordChannel(passwordChannel);
        makeRequest();
    }

    @When("^I make request for a new client with name as \"([^\"]*)\", peakId as \"([^\"]*)\", subUnitId as \"([^\"]*)\", organisationId as \"([^\"]*)\", description as \"([^\"]*)\", platform as \"([^\"]*)\" and pdfChannel as \"([^\"]*)\"$")
    public void iMakeRequestForANewClientWithNameAsPeakIdAsSubUnitIdAsOrganisationIdAsDescriptionAsPlatformAsAndPdfChannelAs(String applicationName, String peakId, String subUnitId, String organisationId, String description, String platform, String pdfChannel) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatform(platform);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPdfChannel(pdfChannel);
        //testContext.getApiManager().getOneClickMerchantOnboarding().setPasswordChannel(passwordChannel);
        makeRequest();
    }

    @When("^I make request for a new client with name as \"([^\"]*)\", peakId as \"([^\"]*)\", subUnitId as \"([^\"]*)\", organisationId as \"([^\"]*)\", platform as \"([^\"]*)\", pdfChannel as \"([^\"]*)\" and passwordChannel as \"([^\"]*)\"$")
    public void iMakeRequestForANewClientWithNameAsPeakIdAsSubUnitIdAsOrganisationIdAsPlatformAsPdfChannelAsAndPasswordChannelAs(String applicationName, String peakId, String subUnitId, String organisationId, String platform, String pdfChannel, String passwordChannel) throws Throwable {
        testContext.getApiManager().getOneClickMerchantOnboarding().setApplicationName(applicationName);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPeakId(peakId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(subUnitId);
        testContext.getApiManager().getOneClickMerchantOnboarding().setOrganisationId(organisationId);
        //testContext.getApiManager().getOneClickMerchantOnboarding().setDescription(description);
        testContext.getApiManager().getOneClickMerchantOnboarding().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getOneClickMerchantOnboarding().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getOneClickMerchantOnboarding().setPlatform(platform);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPdfChannel(pdfChannel);
        testContext.getApiManager().getOneClickMerchantOnboarding().setPasswordChannel(passwordChannel);
        makeRequest();
    }
}
