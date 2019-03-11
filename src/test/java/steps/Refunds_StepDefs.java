package steps;

import managers.TestContext;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.junit.Assert;


public class Refunds_StepDefs extends UtilManager {
    TestContext testContext;

    public Refunds_StepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    final static Logger logger = Logger.getLogger(Refunds_StepDefs.class);

    @Given("^I have a \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void i_have_a(String amount, String currency, String reason)   {
       testContext.getApiManager().getRefunds().setAmount(Double.parseDouble(amount));
       testContext.getApiManager().getRefunds().setCurrencyCode(currency);
       testContext.getApiManager().getRefunds().setReason(reason);
       testContext.getApiManager().getRefunds().setRequestDateTime(getDateHelper().getUTCNowDateTime());
       testContext.getApiManager().getRefunds().setTraceId(getGeneral().generateUniqueUUID());
       testContext.getApiManager().getRefunds().setTransactionId(getGeneral().generateUniqueUUID());
    }

    @When("^I make a request for the refund$")
    public void i_make_a_request_for_the_refund()   {
        logger.info("********** Creating Refund Request ***********");
        testContext.getApiManager().getRefunds().retrieveRefunds(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_1"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_2"));

    }

    @Then("^I should receive a successful refund response$")
    public void i_should_receive_a_successful_refund_response()   {
        Assert.assertEquals("Refund request was not successful!", 200, getRestHelper().getResponseStatusCode(testContext.getApiManager().getRefunds().getRefundsResponse()));
    }

    @Then("^the response body should contain valid refund id, amount, currencyCode, reasonCode, transaction Id$")
    public void the_response_body_should_contain_valid_refund_id_amount_currencyCode_reasonCode_transaction_Id()   {
       Assert.assertNotNull("Refund Id is null!", testContext.getApiManager().getRefunds().refundIdInResponse());

        Assert.assertNotNull("Response code is null!", testContext.getApiManager().getRefunds().reasonCodeInResponse());

        Assert.assertEquals("Amount isn't matching", testContext.getApiManager().getRefunds().getAmount().toString(), testContext.getApiManager().getRefunds().amountInResponse());

        Assert.assertEquals("Currency code isn't matching", testContext.getApiManager().getRefunds().getCurrencyCode(), testContext.getApiManager().getRefunds().currencyCodeInResponse());

        Assert.assertEquals("TransactionId isn't matching", testContext.getApiManager().getRefunds().getTransactionId(), testContext.getApiManager().getRefunds().transactionIdInResponse());
    }

    @Then("^the response body should also have reason if applicable$")
    public void the_response_body_should_also_have_reason_if_applicable()   {
       if (testContext.getApiManager().getRefunds().getReason()==null)
           Assert.assertNull("Reason is present when it should not be", testContext.getApiManager().getRefunds().reasonInResponse());
       else
           Assert.assertEquals("Reason isn't matching", testContext.getApiManager().getRefunds().getReason(), testContext.getApiManager().getRefunds().reasonInResponse());
    }

    @Given("^I have a valid transaction for refund$")
    public void i_have_a_valid_transaction_for_refund()   {
        testContext.getApiManager().getRefunds().setAmount(23.33);
        testContext.getApiManager().getRefunds().setCurrencyCode("HKD");
        testContext.getApiManager().getRefunds().setReason("customer requested");
        testContext.getApiManager().getRefunds().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getRefunds().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getRefunds().setTransactionId(getGeneral().generateUniqueUUID());
    }

    @Given("^I dont send Bearer with the auth token in the refund request$")
    public void i_dont_send_Bearer_with_the_auth_token_in_the_refund_request()   {
        testContext.getApiManager().getRefunds().setAuthToken(testContext.getApiManager().getAccessToken().getAccessToken());
    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within refund response$")
    public void i_should_receive_a_error_response_with_error_description_and_errorcode_within_refund_response(int responseCode, String errorDesc, String errorCode)   {
        Assert.assertEquals("Different response code being returned", responseCode, getRestHelper().getResponseStatusCode(testContext.getApiManager().getRefunds().getRefundsResponse()));

        Assert.assertEquals("Different error code being returned", errorCode, getRestHelper().getErrorCode(testContext.getApiManager().getRefunds().getRefundsResponse()));

        Assert.assertTrue("Different error description being returned..Expected: "+ errorDesc+ "  Actual: "+ getRestHelper().getErrorDescription(testContext.getApiManager().getRefunds().getRefundsResponse()), getRestHelper().getErrorDescription(testContext.getApiManager().getRefunds().getRefundsResponse()).contains(errorDesc));

    }

    @Then("^error message should be \"([^\"]*)\" within refund response$")
    public void error_message_should_be_within_refund_response(String errorMessage)   {
        Assert.assertTrue("Different error message being returned..Expected: "+ errorMessage+ "  Actual: "+ getRestHelper().getErrorMessage(testContext.getApiManager().getRefunds().getRefundsResponse()), getRestHelper().getErrorMessage(testContext.getApiManager().getRefunds().getRefundsResponse()).contains(errorMessage) );

    }

    @When("^I make a request for the refund with \"([^\"]*)\" missing in the header$")
    public void i_make_a_request_for_the_refund_with_missing_in_the_header(String key)  {
        testContext.getApiManager().getRefunds().retrieveRefundWithMissingHeaderKeys(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_1"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_2"), key);

    }

    @When("^I make a request for the refund with \"([^\"]*)\" missing in the body$")
    public void i_make_a_request_for_the_refund_with_missing_in_the_body(String key)  {
        testContext.getApiManager().getRefunds().retrieveRefundWithMissingBodyKeys(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_1"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_2"), key);

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
