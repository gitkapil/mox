package steps;

import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.Set;
import java.util.UUID;


public class ManagementPostApplications_StepDefs extends UtilManager{
    // NB: These are the dragon token (for testing) roles {"roles": ["Application.ReadWrite.All"]}.  CSO tokens use claim {"role": "user"}
    private static final Set<String> ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final Set<String> INCORRECT_ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String SIG_HEADER_LIST_POST_APPLICATION = "header-list-post-application";

    TestContext testContext;
    ManagementCommon common;

    public ManagementPostApplications_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }

    final static Logger logger = Logger.getLogger(ManagementPostApplications_StepDefs.class);

    @Given("^I am a POST application authorized DRAGON user with the Application.ReadWrite.All privilege$")
    public void i_am_an_authorized_DRAGON_user_with_role()  {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPostApplication().setAuthTokenWithBearer(token));
    }

    @Given("^I am a POST application authorized DRAGON user with the ApplicationKey.ReadWrite.All privilege$")
    public void i_am_an_authorized_DRAGON_user_with_incorrect_role()  {
        common.iAmAnAuthorizedDragonUser(INCORRECT_ROLE_SET, token -> testContext.getApiManager().getPostApplication().setAuthTokenWithBearer(token));
    }

    @Given("^I am a DRAGON user with invalid \"([^\"]*)\"$")
    public void i_am_a_DRAGON_user_with_invalid_token(String token)  {
        common.iAmADragonUserWithToken(token, tokenArg -> testContext.getApiManager().getPostApplication().setAuthToken(tokenArg));
    }

    @Given("^I have a \"([^\"]*)\" from an existing AAD application$")
    public void i_have_a_clientId_from_an_existing_AAD_application(String clientId){
        if (clientId.trim().toUpperCase().equals("RANDOM")) {
            logger.info("input clientId is: " + clientId);
            UUID randomUuid = UUID.randomUUID();
            logger.info("randomUuid is: " + randomUuid);
            logger.info("set " + randomUuid + " as clientId ");
            testContext.getApiManager().getPostApplication().setClientId(randomUuid.toString());
        } else {
            testContext.getApiManager().getPostApplication().setClientId(clientId);
        }
        testContext.getApiManager().getPostApplication().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getPostApplication().setTraceId(getGeneral().generateUniqueUUID());
    }

    @Given("^I have a \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\" and a super large description from an existing PM4B merchant identity$")
    public void i_have_a_peakId_subUnitId_and_organisationId_from_an_existing_PM4B_merchant_identity(String peakId,
                                                                                                     String subUnitId,
                                                                                                     String organisationId){

        String description = StringUtils.repeat("*", 257);

        testContext.getApiManager().getPostApplication().setPeakId(peakId);
        testContext.getApiManager().getPostApplication().setSubUnitId(subUnitId);
        testContext.getApiManager().getPostApplication().setOrganisationId(organisationId);
        testContext.getApiManager().getPostApplication().setDescription(description);
        testContext.getApiManager().getPostApplication().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getPostApplication().setTraceId(getGeneral().generateUniqueUUID());
    }

    @Given("^I have a \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\" from an existing PM4B merchant identity$")
    public void i_have_a_peakId_subUnitId_and_organisationId_from_an_existing_PM4B_merchant_identity(String peakId,
                                                                                                     String subUnitId,
                                                                                                     String organisationId,
                                                                                                     String description){
        testContext.getApiManager().getPostApplication().setPeakId(peakId);
        testContext.getApiManager().getPostApplication().setSubUnitId(subUnitId);
        testContext.getApiManager().getPostApplication().setOrganisationId(organisationId);
        testContext.getApiManager().getPostApplication().setDescription(description);
        testContext.getApiManager().getPostApplication().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getPostApplication().setTraceId(getGeneral().generateUniqueUUID());
    }

    @Given("^I have valid application details$")
    public void i_have_valid_application_details(){
        i_have_a_clientId_from_an_existing_AAD_application(UUID.randomUUID().toString());
        i_have_a_peakId_subUnitId_and_organisationId_from_an_existing_PM4B_merchant_identity(
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    @When("^I make a POST request to the application endpoint$")
    public void i_make_a_post_request_to_the_application_endpoint()  {
        logger.info("********** Executing POST Application Request ***********");
        testContext.getApiManager().getPostApplication().executeRequest(
                getRestHelper().getBaseURI()+getFileHelper()
                        .getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME),
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                Sets.newHashSet(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        SIG_HEADER_LIST_POST_APPLICATION).split(",")));
    }

    @When("^I make a POST request to the application endpoint with \"([^\"]*)\" missing in the header$")
    public void i_make_a_post_request_to_the_application_endpoint_with_key_missing_in_the_header(String key)  {
        testContext.getApiManager().getPostApplication().executeRequestWithMissingHeaderKeys(
                getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME), key,
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                Sets.newHashSet(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, SIG_HEADER_LIST_POST_APPLICATION).split(",")));
    }

    @Then("^I should receive a successful applications response$")
    public void i_should_receive_a_successful_applications_response()  {
        Response response = testContext.getApiManager().getPostApplication().getPostApplicationRequestResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), 201,"Request was not successful!");
        Assert.assertEquals(getRestHelper().getResponseHeaderValue(response, "X-Application-Context "), null, "Expects X-Application-Context header to not exists");
        Assert.assertNotNull(response, "The response for Post Client Request was null");
    }

    @Then("^the response body should contain a valid applicationId, clientId, peakId, subUnitId and organisationId$")
    public void the_response_body_should_contain_a_valid_applicationId_clientId_peakId_subUnitId_and_organisationId(){
        Assert.assertNotNull(testContext.getApiManager().getPostApplication().applicationIdInResponse(), "applicationId is not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPostApplication().clientIdInResponse(), "clientId is not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPostApplication().peakIdInResponse(), "peakId is not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPostApplication().subUnitIdInResponse(), "subUnitId is not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPostApplication().organisationIdInResponse(), "organisationId is not present in the response!!");

        Assert.assertEquals(testContext.getApiManager().getPostApplication().clientIdInResponse(),
                testContext.getApiManager().getPostApplication().getClientId(),
                "clientId returned does not match.");

        Assert.assertEquals(testContext.getApiManager().getPostApplication().peakIdInResponse(),
                testContext.getApiManager().getPostApplication().getPeakId(),
                "peakId returned does not match.");

        Assert.assertEquals(testContext.getApiManager().getPostApplication().subUnitIdInResponse(),
                testContext.getApiManager().getPostApplication().getSubUnitId(),
                "subUnitId returned does not match.");

        Assert.assertEquals(testContext.getApiManager().getPostApplication().organisationIdInResponse(),
                testContext.getApiManager().getPostApplication().getOrganisationId(),
                "organisationId returned does not match.");
    }

    @Then("^the response body should also have empty notificationPath and empty notificationHost$")
    public void the_response_body_should_also_have_empty_notificationPath_and_empty_notificationHost() throws Throwable {
        Assert.assertNotNull(testContext.getApiManager().getPostApplication().notificationPathInResponse(), "notificationPath is not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPostApplication().notificationHostInResponse(), "notificationHost is not present in the response!!");

        Assert.assertEquals(testContext.getApiManager().getPostApplication().notificationPathInResponse(),
                "",
                "notificationPath returned is not the empty string");

        Assert.assertEquals(testContext.getApiManager().getPostApplication().notificationHostInResponse(),
                "",
                "notificationHost returned is not the empty string");
    }

    @Then("^I should receive a quoted \"([^\"]*)\" error response with \'(.*)\' error description and \"([^\"]*)\" errorcode within the POST application response$")
    public void i_should_receive_a_quoted_error_response_with_error_description_and_errorcode(int responseCode, String errorDesc, String errorCode) {
        i_should_receive_an_error_response_with_error_description_and_errorcode("" + responseCode, errorDesc, errorCode);
    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within the POST application response$")
    public void i_should_receive_an_error_response_with_error_description_and_errorcode(String responseCode, String errorDesc, String errorCode) {
        Response response = testContext.getApiManager().getPostApplication().getPostApplicationRequestResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), Integer.parseInt(responseCode),"Different response code being returned");

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

        Assert.assertEquals(getRestHelper().getErrorCode(response), errorCode,"Different error code being returned");
    }

    @Then("^error message should be \"([^\"]*)\" within the POST application response$")
    public void i_should_receive_a_error_message(String errorMessage) {
        Response response = testContext.getApiManager().getPostApplication().getPostApplicationRequestResponse();
        Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage) ,
                "Different error message being returned..Expected: "+ errorMessage+ " Actual: " +
                        getRestHelper().getErrorMessage(response));

    }

    @Given("^I have valid application details with no TraceId value sent in the header$")
    public void i_have_valid_application_details_with_no_TraceId_sent_in_the_header() {
        i_have_valid_application_details();
        testContext.getApiManager().getPostApplication().setTraceId(null);
    }

    @Given("^I have valid application details with invalid value \"([^\"]*)\" set for Request Date Time sent in the header$")
    public void i_have_valid_application_details_with_no_RequestDateTime_sent_in_the_header(String value) {
        i_have_valid_application_details();
        testContext.getApiManager().getPostApplication().setRequestDateTime(value);
    }

    @And("^I make a POST request with the same client Id to the application endpoint$")
    public void iMakeAPOSTRequestWithTheSameClientIdToTheApplicationEndpoint() {
        i_make_a_post_request_to_the_application_endpoint();
    }
}
