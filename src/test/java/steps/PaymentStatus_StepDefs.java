package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.BaseStep;



public class PaymentStatus_StepDefs implements BaseStep {

    final static Logger logger = Logger.getLogger(PaymentStatus_StepDefs.class);

    @Given("^I have a valid payment id$")
    public void i_have_a()  {
       paymentStatus.setPaymentRequestId(paymentRequest.paymentRequestIdInResponse());
    }

    @When("^I make a request for the check status$")
    public void i_make_a_request_for_the_check_status(){
        paymentStatus.setTraceId(general.generateUniqueUUID());

        paymentStatus.retrievePaymentStatus(restHelper.getBaseURI()+System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "check_payment_status_resource"));
    }

    @Then("^I should recieve a successful check status response$")
    public void i_should_recieve_a_successful_check_status_response() {
        logger.info("********** Retrieving Payment Request Status ***********");
        Assert.assertEquals(restHelper.getResponseStatusCode(paymentStatus.getPaymentStatusResponse()), 200,"Check Payment Status was not successful!");

    }

    @Then("^the response body should contain valid status description and status code$")
    public void the_response_body_should_contain_valid_status_description_and_status_code(){
        Assert.assertNotNull(paymentStatus.statusDescriptionInResponse(), "Status Description is not present in the response!!");

        Assert.assertNotNull(paymentStatus.statusCodeInResponse(), "Status Code is not present in the response!!");

    }

    @Given("^I dont send Bearer with the auth token in the check status request$")
    public void i_dont_send_Bearer_with_the_auth_token_in_the_check_status_request(){

        paymentStatus.setAuthToken(accessToken.getAccessToken());

    }

    @Then("^I should recieve a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within check status response$")
    public void i_should_recieve_a_error_response_with_error_description_and_errorcode_within_check_status_response(int responseCode, String errorDesc, String errorCode) {
        Assert.assertEquals(restHelper.getResponseStatusCode(paymentStatus.getPaymentStatusResponse()), responseCode,"Different response code being returned");

        Assert.assertEquals(restHelper.getErrorCode(paymentStatus.getPaymentStatusResponse()), errorCode,"Different error code being returned");

        Assert.assertTrue(restHelper.getErrorDescription(paymentStatus.getPaymentStatusResponse()).contains(errorDesc) ,"Different error description being returned..Expected: "+ errorDesc+ "  Actual: "+ restHelper.getErrorDescription(paymentStatus.getPaymentStatusResponse()));

    }

    @Then("^error message should be \"([^\"]*)\" within check status response$")
    public void error_message_should_be_within_check_status_response(String errorMessage) {
        Assert.assertTrue(restHelper.getErrorMessage(paymentStatus.getPaymentStatusResponse()).contains(errorMessage) ,"Different error message being returned");

    }

    @When("^I make a request for the payment status with \"([^\"]*)\" missing in the header$")
    public void i_make_a_request_for_the_payment_status_with_missing_in_the_header(String key)  {
        paymentStatus.setTraceId(general.generateUniqueUUID());
        paymentStatus.retrievePaymentStatusWithMissingHeaderKeys(restHelper.getBaseURI()+System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "check_payment_status_resource"), key);

    }

    @When("^I send invalid auth token \"([^\"]*)\" in the check status request$")
    public void i_send_invalid_in_the_check_status_request(String authToken) {
        paymentStatus.setAuthToken(authToken);
        paymentStatus.setAuthTokenwithBearer();
    }

    @Given("^I have a \"([^\"]*)\"$")
    public void i_have_a_valid(String paymentReqId) {
        paymentStatus.setPaymentRequestId(paymentReqId);
    }

    @Then("^the response body should contain correct \"([^\"]*)\" and \"([^\"]*)\"$")
    public void the_response_body_should_contain_correct_and(String statusDesc, String statusCode)  {
        Assert.assertEquals(statusDesc, paymentStatus.statusDescriptionInResponse(), "Status Description is not correct!");

        Assert.assertEquals(statusCode, paymentStatus.statusCodeInResponse(), "Status Code is not correct!");
    }



}
