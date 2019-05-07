package steps;

import com.google.common.collect.Sets;
import cucumber.api.java.en.And;
import managers.TestContext;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class Refunds_StepDefs extends UtilManager {
    TestContext testContext;
    ManagementCommon common;
    private static final Set<String> ROLE_SET = Sets.newHashSet("refund");
    private static final Set<String> INCORRECT_ROLE_SET = Sets.newHashSet("paymentRequest");
    private static final String VALID_TRANSACTION_ID = "";
    private static final String VALID_PAYER_ID = "";

    public Refunds_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(this.testContext);
    }

    final static Logger logger = Logger.getLogger(Refunds_StepDefs.class);

    @Given("^I am logging in as a user with incorrect role$")
    public void logIn() {
        common.iAmAnAuthorizedDragonUserForRefunds(ROLE_SET, token -> testContext.getApiManager().getRefunds().setAuthTokenWithBearer(token));
    }

    @And("^I have a valid transaction for refund$")
    public void validTransactionForRefund() {
        testContext.getApiManager().getRefunds().setTransactionId(VALID_TRANSACTION_ID);
    }

    @When("^I try to make a call to refund$")
    public void callRefund() {
        testContext.getApiManager().getRefunds().setTransactionId(VALID_TRANSACTION_ID);
        testContext.getApiManager().getRefunds().setPayerId(VALID_PAYER_ID);
        testContext.getApiManager().getRefunds().setAmount("100");
        testContext.getApiManager().getRefunds().setCurrencyCode("HKD");
        testContext.getApiManager().getRefunds().setReasonCode("00");
        testContext.getApiManager().getRefunds().setReasonMessage("test");
        testContext.getApiManager().getRefunds().retrieveRefunds(
                getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_1"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_2"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));
    }

    @Given("^I am logging in as a user with refund role$")
    public void invalidLogIn() {
        common.iAmAnAuthorizedDragonUserForRefunds(INCORRECT_ROLE_SET, token -> testContext.getApiManager().getRefunds().setAuthTokenWithBearer(token));
    }

    @Given("^I try to make a call to refund with transaction id as \"([^\"]*)\"$")
    public void callWithTransactionId(String transactionId) {
        testContext.getApiManager().getRefunds().setTransactionId(transactionId);
    }

    @And("^I enter the refund data with payerId \"([^\"]*)\", refund amount \"([^\"]*)\", refund currency \"([^\"]*)\", reason Code \"([^\"]*)\" and reason message \"([^\"]*)\"$")
    public void enterBody(String payerId, String amount, String currencyCode, String reasonCode, String reasonMessage) {
        testContext.getApiManager().getRefunds().setPayerId(payerId);
        testContext.getApiManager().getRefunds().setAmount(amount);
        testContext.getApiManager().getRefunds().setCurrencyCode(currencyCode);
        testContext.getApiManager().getRefunds().setReasonCode(reasonCode);
        testContext.getApiManager().getRefunds().setReasonMessage(reasonMessage);
        testContext.getApiManager().getRefunds().retrieveRefunds(
                getRestHelper().getBaseURI() +
                        getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_1"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_2"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));
    }



    @When("^I make a request for the refund$")
    public void i_make_a_request_for_the_refund()   {
        logger.info("********** Creating Refund Request ***********");
        testContext.getApiManager().getRefunds().retrieveRefunds(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_1"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_2"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @Then("^I should receive a successful refund response$")
    public void i_should_receive_a_successful_refund_response()   {
        Assert.assertEquals("Refund request was not successful!", 200, getRestHelper().getResponseStatusCode(testContext.getApiManager().getRefunds().getRefundsResponse()));
    }

    @Given("^I dont send Bearer with the auth token in the refund request$")
    public void i_dont_send_Bearer_with_the_auth_token_in_the_refund_request()   {
        testContext.getApiManager().getRefunds().setAuthToken(testContext.getApiManager().getAccessToken().getAccessToken());
    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within refund response$")
    public void i_should_receive_a_error_response_with_error_description_and_errorcode_within_refund_response(int responseCode, String errorDesc, String errorCode)   {
        Assert.assertEquals("Different response code being returned", responseCode, getRestHelper().getResponseStatusCode(testContext.getApiManager().getRefunds().getRefundsResponse()));
        if (!errorCode.equalsIgnoreCase("null")) {
            Assert.assertEquals("Different error code being returned", errorCode, getRestHelper().getErrorCode(testContext.getApiManager().getRefunds().getRefundsResponse()));
        }

        if (!errorDesc.equalsIgnoreCase("null")) {
            Assert.assertTrue("Different error description being returned..Expected: " + errorDesc + "  Actual: " + getRestHelper().getErrorDescription(testContext.getApiManager().getRefunds().getRefundsResponse()), getRestHelper().getErrorDescription(testContext.getApiManager().getRefunds().getRefundsResponse()).contains(errorDesc));
        }

    }

    @Then("^error message should be \"([^\"]*)\" within refund response$")
    public void error_message_should_be_within_refund_response(String errorMessage)   {
        Assert.assertTrue("Different error message being returned..Expected: "+ errorMessage+ "  Actual: "+ getRestHelper().getErrorMessage(testContext.getApiManager().getRefunds().getRefundsResponse()), getRestHelper().getErrorMessage(testContext.getApiManager().getRefunds().getRefundsResponse()).contains(errorMessage) );

    }

    @When("^I make a request for the refund with \"([^\"]*)\" missing in the header$")
    public void i_make_a_request_for_the_refund_with_missing_in_the_header(String key)  {
        testContext.getApiManager().getRefunds().retrieveRefundWithMissingHeaderKeys(
                getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_1"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_2"),
                key,
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @When("^I make a request for the refund with \"([^\"]*)\" missing in the body$")
    public void i_make_a_request_for_the_refund_with_missing_in_the_body(String key)  {
        testContext.getApiManager().getRefunds().retrieveRefundWithMissingBodyKeys(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_1"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_2"), key,
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @When("^I send invalid auth token \"([^\"]*)\" in the refund request$")
    public void i_send_invalid_in_the_refund_request(String authToken) {
        testContext.getApiManager().getRefunds().setAuthToken(authToken);
        testContext.getApiManager().getRefunds().setAuthTokenwithBearer();
    }


    @Given("^I send invalid value \"([^\"]*)\" for the request date time in the refund request$")
    public void invalid_request_date_time_value(String value)   {
        testContext.getApiManager().getRefunds().setRequestDateTime(value);
    }



}
