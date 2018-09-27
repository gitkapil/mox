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
        paymentStatus.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));

       paymentStatus.retrievePaymentStatus(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"));

    }

    @Then("^I should recieve a successful check status response$")
    public void i_should_recieve_a_successful_check_status_response() {
        logger.info("********** Retrieving Payment Request Status ***********");
        Assert.assertEquals(restHelper.getResponseStatusCode(paymentStatus.getPaymentStatusResponse()), 200,"Check Payment Status was not successful!");

    }

    @Then("^the response body should contain valid payment request id, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration within check status response$")
    public void the_response_body_should_contain_valid_payment_id_created_timestamp_links_check_status()  {
        Assert.assertNotNull(paymentStatus.paymentRequestIdInResponse(), "Payment Request Id is not present in the response!!");

        Assert.assertNotNull(paymentStatus.createdTimestampInResponse(), "Created Timestamp is not present in the response!!");

        Assert.assertNotNull(paymentStatus.webLinkInResponse(), "Web Link is not present in the response!!");

        Assert.assertNotNull(paymentStatus.appLinkInResponse(), "App Link is not present in the response!!");

        Assert.assertEquals(paymentStatus.effectiveDurationInResponse().toString(), "600", "Effective Duration isn't 600!");

        Assert.assertNotNull(paymentStatus.statusCodeInResponse(), "Status Code is not present in the response!!");

        Assert.assertNotNull(paymentStatus.statusDescriptionInResponse(), "Status Description is not present in the response!!");

        Assert.assertEquals(Double.parseDouble(paymentStatus.totalAmountInResponse()), paymentRequest.getTotalAmount(), "Total Amount isn't matching!");

        Assert.assertEquals(paymentStatus.currencyCodeInResponse(), paymentRequest.getCurrency(), "Currency Code isn't matching!");


        // Assert.assertEquals(paymentRequest.effectiveDurationInResponse(), paymentRequest.getEffectiveDuration(), "Effective Duration isn't matching!");
    }

    @Then("^the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable within check status response$")
    public void the_response_body_should_also_have_notification_url_app_success_callback_app_fail_callback_uri_if_applicable_check_status()  {
        if (paymentRequest.getnotificationURI()==null)
            Assert.assertNull(paymentStatus.notificationURIInResponse(), "NotificationUri is present within the response when it should not be");
        else
            Assert.assertEquals(paymentStatus.notificationURIInResponse(), paymentRequest.getnotificationURI(), "Notification Uri isn't matching!");

        if (paymentRequest.getAppSuccessCallback()==null)
            Assert.assertNull(paymentStatus.appSuccessCallbackInResponse(), "App Success Call Back is present within the response when it should not be");
        else
            Assert.assertEquals(paymentStatus.appSuccessCallbackInResponse(), paymentRequest.getAppSuccessCallback(), "App Success Callback isn't matching!");


        if (paymentRequest.getAppFailCallback()==null)
            Assert.assertNull(paymentStatus.appFailCallbackInResponse(), "App Fail Call Back is present within the response when it should not be");
        else
            Assert.assertEquals(paymentStatus.appFailCallbackInResponse(), paymentRequest.getAppFailCallback(), "App Fail Callback isn't matching!");


    }


    @Then("^the response body should have transactionid if the payment status is success within check status response$")
    public void transaction_id_within_check_status_response() {
        if (paymentStatus.statusCodeInResponse().equals("PR005"))
            Assert.assertNotNull(paymentStatus.transactionIdInResponse(), "Transaction Id is not present in the response!!");
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
        Assert.assertTrue(restHelper.getErrorMessage(paymentStatus.getPaymentStatusResponse()).contains(errorMessage) ,"Different error message being returned..Expected: "+ errorMessage+ "  Actual: "+ restHelper.getErrorMessage(paymentStatus.getPaymentStatusResponse()));

    }

    @When("^I make a request for the payment status with \"([^\"]*)\" missing in the header$")
    public void i_make_a_request_for_the_payment_status_with_missing_in_the_header(String key)  {
        paymentStatus.setTraceId(general.generateUniqueUUID());
        paymentStatus.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        
        paymentStatus.retrievePaymentStatusWithMissingHeaderKeys(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"), key);

    }

    @When("^I send invalid auth token \"([^\"]*)\" in the check status request$")
    public void i_send_invalid_in_the_check_status_request(String authToken) {
        paymentStatus.setAuthToken(authToken);
        paymentStatus.setAuthTokenwithBearer();
    }

    @Given("^I have a payment id \"([^\"]*)\"$")
    public void i_have_a_valid(String paymentReqId) {
        paymentStatus.setPaymentRequestId(paymentReqId);
    }

    @Then("^the response body should contain correct \"([^\"]*)\" and \"([^\"]*)\"$")
    public void the_response_body_should_contain_correct_and(String statusDesc, String statusCode)  {
        Assert.assertEquals(statusDesc, paymentStatus.statusDescriptionInResponse(), "Status Description is not correct!");

        Assert.assertEquals(statusCode, paymentStatus.statusCodeInResponse(), "Status Code is not correct!");
    }



}
