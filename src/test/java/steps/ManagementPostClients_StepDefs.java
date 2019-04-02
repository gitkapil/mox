package steps;

import com.jayway.restassured.response.Response;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.EnvHelper;

import java.util.*;

import static org.junit.Assert.assertTrue;


public class ManagementPostClients_StepDefs extends UtilManager{
    private static final String CSO_ROLE = "CSO";

    TestContext testContext;

    public ManagementPostClients_StepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    final static Logger logger = Logger.getLogger(ManagementPostClients_StepDefs.class);
    List<Response> paymentResponses= new ArrayList<Response>();

    @Given("^I am an authorized CSO user$")
    public void i_am_an_authorized_CSO_user()  {

        boolean isAuthorisedInCsoRole = Optional.ofNullable(testContext.getApiManager().getAccessToken().getAccessTokenClaimSet().getClaim("roles"))
                .filter(v -> v instanceof List)
                .map(v -> (List)v)
                .orElse(Collections.emptyList())
                .stream()
                .anyMatch(v -> CSO_ROLE.equals(v));

        assertTrue("Expected token to have CSO role", isAuthorisedInCsoRole);

        testContext.getApiManager().getPostClient().setAuthTokenWithBearer(testContext.getApiManager().getAccessToken().getAccessToken());

        if(testContext.getApiManager().getAccessToken().getType().equalsIgnoreCase("merchant")){
            getRestHelper().setBaseURI(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-api-management-url")
                    +getEnvSpecificBasePathAPIs());
        }

        else{
            getRestHelper().setBaseURI(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "sandbox-api-management-url")
                    +getEnvSpecificBasePathAPIs());
        }
    }

    private String getEnvSpecificBasePathAPIs() {
        if (EnvHelper.getInstance().isLocalDevMode()) {
            return getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "Base_Path_APIs");
        }
        return getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_APIs");
    }

    @Given("^I dont send Bearer with the auth token$")
    public void no_bearer_as_prefix()  {
        testContext.getApiManager().getPaymentRequest().setAuthToken(testContext.getApiManager().getAccessToken().getAccessToken());
    }


    @Given("^I am a merchant with invalid \"([^\"]*)\"$")
    public void i_am_a_merchant_with_invalid_token(String token)  {
        testContext.getApiManager().getPaymentRequest().setAuthToken(token);
        testContext.getApiManager().getPaymentRequest().setAuthTokenwithBearer(testContext.getApiManager().getPaymentRequest().getAuthToken());

        if(testContext.getApiManager().getAccessToken().getType().equalsIgnoreCase("merchant")){
            getRestHelper().setBaseURI(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-api-management-url")
                    +getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_APIs"));
        }

        else{
            getRestHelper().setBaseURI(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "sandbox-api-management-url")
                    +getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_APIs"));
        }
    }

    @Given("^I have a \"([^\"]*)\" from an existing AAD application$")
    public void i_have_a_clientId_from_an_existing_AAD_application(String clientId){
        testContext.getApiManager().getPostClient().setClientId(clientId);
        testContext.getApiManager().getPaymentRequest().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getPaymentRequest().setTraceId(getGeneral().generateUniqueUUID());
    }

    @Given("^I have a \"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\" from an existing PM4B merchant identity$")
    public void i_have_a_peakId_subUnitId_and_organisationId_from_an_existing_PM4B_merchant_identity(String peakId, String subUnitId, String organisationId){
        testContext.getApiManager().getPostClient().setPeakId(peakId);
        testContext.getApiManager().getPostClient().setSubUnitId(subUnitId);
        testContext.getApiManager().getPostClient().setOrganisationId(organisationId);
        testContext.getApiManager().getPostClient().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getPostClient().setTraceId(getGeneral().generateUniqueUUID());
    }

    @Given("^I have valid client details$")
    public void i_have_valid_client_details(){
        i_have_a_clientId_from_an_existing_AAD_application("TODO");
        i_have_a_peakId_subUnitId_and_organisationId_from_an_existing_PM4B_merchant_identity("TODO", "TODO", "TODO");
    }

    @When("^I make a POST request to the application endpoint$")
    public void i_make_a_post_request_to_the_application_endpoint()  {
        logger.info("********** Creating Payment Request ***********");
        testContext.getApiManager().getPostClient().executeRequest(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_client_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));
    }

    @When("^I make a POST request to the application endpoint with \"([^\"]*)\" missing in the header$")
    public void i_make_a_post_request_to_the_application_endpoint_with_key_missing_in_the_header(String key)  {
        testContext.getApiManager().getPostClient().executeRequestWithMissingHeaderKeys(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"), key,
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));
    }

    @Then("^I should receive a successful applications response$")
    public void i_should_receive_a_successful_applications_response()  {
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostClient().getPostClientRequestResponse()), 201,"Request was not successful!");
        Assert.assertEquals(getRestHelper().getResponseHeaderValue(testContext.getApiManager().getPostClient().getPostClientRequestResponse(), "X-Application-Context "), null, "Expects X-Application-Context header to not exists");
        Assert.assertNotNull(testContext.getApiManager().getPostClient().getPostClientRequestResponse(), "The response for Post Client Request was null");
    }

    @Then("^the response body should contain a valid applicationId, clientId, peakId, subUnitId and organisationId$")
    public void the_response_body_should_contain_a_valid_applicationId_clientId_peakId_subUnitId_and_organisationId(){
        Assert.assertNotNull(testContext.getApiManager().getPostClient().applicationIdInResponse(), "applicationId is not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPostClient().clientIdInResponse(), "clientId is not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPostClient().peakIdInResponse(), "peakId is not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPostClient().subUnitIdInResponse(), "subUnitId is not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPostClient().organisationIdInResponse(), "organisationId is not present in the response!!");

        Assert.assertEquals(testContext.getApiManager().getPostClient().clientIdInResponse(),
                testContext.getApiManager().getPostClient().getClientId(),
                "clientId returned does not match.");

        Assert.assertEquals(testContext.getApiManager().getPostClient().peakIdInResponse(),
                testContext.getApiManager().getPostClient().getPeakId(),
                "peakId returned does not match.");

        Assert.assertEquals(testContext.getApiManager().getPostClient().subUnitIdInResponse(),
                testContext.getApiManager().getPostClient().getSubUnitId(),
                "subUnitId returned does not match.");

        Assert.assertEquals(testContext.getApiManager().getPostClient().organisationIdInResponse(),
                testContext.getApiManager().getPostClient().getOrganisationId(),
                "organisationId returned does not match.");
    }

    @Then("^the response body should also have empty notificationPath and empty notificationHost$")
    public void the_response_body_should_also_have_empty_notificationPath_and_empty_notificationHost() throws Throwable {
        Assert.assertNotNull(testContext.getApiManager().getPostClient().notificationPathInResponse(), "notificationPath is not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPostClient().notificationHostInResponse(), "notificationHost is not present in the response!!");

        Assert.assertEquals(testContext.getApiManager().getPostClient().notificationPathInResponse(),
                "",
                "notificationPath returned is not the empty string");

        Assert.assertEquals(testContext.getApiManager().getPostClient().notificationHostInResponse(),
                "",
                "notificationHost returned is not the empty string");
    }

    @Then("^I should receive a quoted \"([^\"]*)\" error response with \'(.*)\' error description and \"([^\"]*)\" errorcode within payment response$")
    public void i_should_receive_a_error_response_with_error_description_and_errorcode_1(int responseCode, String errorDesc, String errorCode) {
        i_should_receive_a_error_response_with_error_description_and_errorcode(responseCode, errorDesc, errorCode);
    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within the create client response$")
    public void i_should_receive_a_error_response_with_error_description_and_errorcode(int responseCode, String errorDesc, String errorCode) {
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse()), responseCode,"Different response code being returned");

        Assert.assertTrue(

                getRestHelper().getErrorDescription(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse())
                        .replace("\"", "")
                        .contains(errorDesc) ,
                "Different error description being returned..Expected: "+ errorDesc+ "Actual: "+ getRestHelper().getErrorDescription(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse()));

        Assert.assertEquals(getRestHelper().getErrorCode(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse()), errorCode,"Different error code being returned");
    }

    @Then("^error message should be \"([^\"]*)\" within the create client response$")
    public void i_should_receive_a_error_message(String errorMessage) {
        Assert.assertTrue(
                getRestHelper().getErrorMessage(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse()).contains(errorMessage) ,
                "Different error message being returned..Expected: "+ errorMessage+ " Actual: " +
                        getRestHelper().getErrorMessage(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse()));

    }


    @Then("^I should receive a (\\d+) error response within payment response$")
    public void i_should_receive_a_error_response_within_payment_response(int errorCode) {
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse()), errorCode,"Different response code being returned");

    }

    @Given("^I send request date timestamp in an invalid \"([^\"]*)\"$")
    public void i_send_request_date_timestamp_in_an_invalid(String format) {
        testContext.getApiManager().getPaymentRequest().setRequestDateTime(getDateHelper().convertDateTimeIntoAFormat(getDateHelper().getSystemDateandTimeStamp(), format));

    }

    @Then("^\"([^\"]*)\" error description and \"([^\"]*)\" errorcode within payment response$")
    public void error_description_and_errorcode_within_payment_response(String errorDesc, String errorCode)  {
        Assert.assertEquals(getRestHelper().getErrorCode(paymentResponses.get(1)), errorCode,"Different error code being returned");

        Assert.assertEquals(getRestHelper().getErrorDescription(paymentResponses.get(1)), errorDesc,"Different error description being returned");

    }


    @Given("^I have payment details with \"([^\"]*)\" set for the \"([^\"]*)\"$")
    public void i_have_payment_details_with_set_for_the(String invalid_value, String parameter) {
        testContext.getApiManager().getPaymentRequest().setTotalAmount("20");
        testContext.getApiManager().getPaymentRequest().setCurrency("HKD");
        //testContext.getApiManager().getPaymentRequest().setNotificationURI("https://pizzahut.com/return");
        //testContext.getApiManager().getPaymentRequest().setAppSuccessCallback("https://pizzahut.com/confirmation");
        //testContext.getApiManager().getPaymentRequest().setAppFailCallback("https://pizzahut.com/unsuccessful");
        testContext.getApiManager().getPaymentRequest().setNotificationURI(Hooks.hostIP+"/return");
        testContext.getApiManager().getPaymentRequest().setAppSuccessCallback(Hooks.hostIP+"/confirmation");
        testContext.getApiManager().getPaymentRequest().setAppFailCallback(Hooks.hostIP+"/unsuccessful");
        testContext.getApiManager().getPaymentRequest().setEffectiveDuration("600");
        testContext.getApiManager().getPaymentRequest().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getPaymentRequest().setTraceId(getGeneral().generateUniqueUUID());

        if (parameter.equalsIgnoreCase("totalamount"))
            testContext.getApiManager().getPaymentRequest().setTotalAmount(invalid_value);


    }

    @Given("^I have valid client details with no TraceId value sent in the header$")
    public void i_have_valid_client_details_with_no_TraceId_sent_in_the_header() {
        testContext.getApiManager().getPaymentRequest().setTotalAmount("20");
        testContext.getApiManager().getPaymentRequest().setCurrency("HKD");
        /*testContext.getApiManager().getPaymentRequest().setNotificationURI("https://pizzahut.com/return");
        testContext.getApiManager().getPaymentRequest().setAppSuccessCallback("https://pizzahut.com/confirmation");
        testContext.getApiManager().getPaymentRequest().setAppFailCallback("https://pizzahut.com/unsuccessful");*/
        testContext.getApiManager().getPaymentRequest().setNotificationURI(Hooks.hostIP+"/return");
        testContext.getApiManager().getPaymentRequest().setAppSuccessCallback(Hooks.hostIP+"/confirmation");
        testContext.getApiManager().getPaymentRequest().setAppFailCallback(Hooks.hostIP+"/unsuccessful");
        testContext.getApiManager().getPaymentRequest().setEffectiveDuration("600");
        testContext.getApiManager().getPaymentRequest().setRequestDateTime(getDateHelper().getUTCNowDateTime());
    }

    @Given("^I have valid client details with invalid value \"([^\"]*)\" set for Request Date Time sent in the header$")
    public void i_have_valid_client_details_with_no_RequestDateTime_sent_in_the_header(String value) {
        testContext.getApiManager().getPaymentRequest().setTotalAmount("20");
        testContext.getApiManager().getPaymentRequest().setCurrency("HKD");
        /*testContext.getApiManager().getPaymentRequest().setNotificationURI("https://pizzahut.com/return");
        testContext.getApiManager().getPaymentRequest().setAppSuccessCallback("https://pizzahut.com/confirmation");
        testContext.getApiManager().getPaymentRequest().setAppFailCallback("https://pizzahut.com/unsuccessful")*/;
        testContext.getApiManager().getPaymentRequest().setNotificationURI(Hooks.hostIP+"/return");
        testContext.getApiManager().getPaymentRequest().setAppSuccessCallback(Hooks.hostIP+"/confirmation");
        testContext.getApiManager().getPaymentRequest().setAppFailCallback(Hooks.hostIP+"/unsuccessful");
        testContext.getApiManager().getPaymentRequest().setEffectiveDuration("600");
        testContext.getApiManager().getPaymentRequest().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getPaymentRequest().setRequestDateTime(value);

    }
}
