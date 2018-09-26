package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.BaseStep;

public class Refunds_StepDefs implements BaseStep {

    final static Logger logger = Logger.getLogger(Refunds_StepDefs.class);

    @Given("^I have a \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void i_have_a(String amount, String currency, String reason)   {
       refunds.setAmount(Double.parseDouble(amount));
       refunds.setCurrencyCode(currency);
       refunds.setReason(reason);
      // refunds.setRefundsHeader(null);
      // refunds.setRefundsBody(null);

        refunds.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        refunds.setTraceId(general.generateUniqueUUID());
        refunds.setTransactionId(general.generateUniqueUUID());
    }

    @When("^I make a request for the refund$")
    public void i_make_a_request_for_the_refund()   {
        logger.info("********** Creating Refund Request ***********");
        refunds.retrieveRefunds(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_1"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_2"));

    }

    @Then("^I should recieve a successful refund response$")
    public void i_should_recieve_a_successful_refund_response()   {
        Assert.assertEquals("Refund request was not successful!", 200, restHelper.getResponseStatusCode(refunds.getRefundsResponse()));
    }

    @Then("^the response body should contain valid refund id, amount, currencyCode, reasonCode, transaction Id$")
    public void the_response_body_should_contain_valid_refund_id_amount_currencyCode_reasonCode_transaction_Id()   {
       Assert.assertNotNull("Refund Id is null!", refunds.refundIdInResponse());

        Assert.assertNotNull("Response code is null!", refunds.reasonCodeInResponse());

        Assert.assertEquals("Amount isn't matching", refunds.getAmount().toString(), refunds.amountInResponse());

        Assert.assertEquals("Currency code isn't matching", refunds.getCurrencyCode(), refunds.currencyCodeInResponse());

        Assert.assertEquals("TransactionId isn't matching", refunds.getTransactionId(), refunds.transactionIdInResponse());
    }

    @Then("^the response body should also have reason if applicable$")
    public void the_response_body_should_also_have_reason_if_applicable()   {
       if (refunds.getReason()==null)
           Assert.assertNull("Reason is present when it should not be", refunds.reasonInResponse());
       else
           Assert.assertEquals("Reason isn't matching", refunds.getReason(), refunds.reasonInResponse());
    }

    @Given("^I have a valid transaction for refund$")
    public void i_have_a_valid_transaction_for_refund()   {
        refunds.setAmount(23.33);
        refunds.setCurrencyCode("HKD");
        refunds.setReason("customer requested");
       // refunds.setRefundsHeader(null);
       // refunds.setRefundsBody(null);

        refunds.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        refunds.setTraceId(general.generateUniqueUUID());
        refunds.setTransactionId(general.generateUniqueUUID());
    }

    @Given("^I dont send Bearer with the auth token in the refund request$")
    public void i_dont_send_Bearer_with_the_auth_token_in_the_refund_request()   {
        refunds.setAuthToken(accessToken.getAccessToken());
    }

    @Then("^I should recieve a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within refund response$")
    public void i_should_recieve_a_error_response_with_error_description_and_errorcode_within_refund_response(int responseCode, String errorDesc, String errorCode)   {
        Assert.assertEquals("Different response code being returned", responseCode, restHelper.getResponseStatusCode(refunds.getRefundsResponse()));

        Assert.assertEquals("Different error code being returned", errorCode, restHelper.getErrorCode(refunds.getRefundsResponse()));

        Assert.assertTrue("Different error description being returned..Expected: "+ errorDesc+ "  Actual: "+ restHelper.getErrorDescription(refunds.getRefundsResponse()), restHelper.getErrorDescription(refunds.getRefundsResponse()).contains(errorDesc));

    }

    @Then("^error message should be \"([^\"]*)\" within refund response$")
    public void error_message_should_be_within_refund_response(String errorMessage)   {
        Assert.assertTrue("Different error message being returned..Expected: "+ errorMessage+ "  Actual: "+ restHelper.getErrorMessage(refunds.getRefundsResponse()), restHelper.getErrorMessage(refunds.getRefundsResponse()).contains(errorMessage) );

    }

    @When("^I make a request for the refund with \"([^\"]*)\" missing in the header$")
    public void i_make_a_request_for_the_refund_with_missing_in_the_header(String key)  {
        refunds.retrieveRefundWithMissingHeaderKeys(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_1"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_2"), key);

    }

    @When("^I make a request for the refund with \"([^\"]*)\" missing in the body$")
    public void i_make_a_request_for_the_refund_with_missing_in_the_body(String key)  {
        refunds.retrieveRefundWithMissingBodyKeys(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_1"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "refund_resource_2"), key);

    }

    @When("^I send invalid auth token \"([^\"]*)\" in the refund request$")
    public void i_send_invalid_in_the_refund_request(String authToken) {
        refunds.setAuthToken(authToken);
        refunds.setAuthTokenwithBearer();
    }


}
