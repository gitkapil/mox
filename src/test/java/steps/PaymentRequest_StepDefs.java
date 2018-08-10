package steps;

import com.jayway.restassured.response.Response;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.BaseStep;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PaymentRequest_StepDefs implements BaseStep {

    final static Logger logger = Logger.getLogger(PaymentRequest_StepDefs.class);
    List<Response> paymentResponses= new ArrayList<Response>();

    @Given("^I am an authorized merchant$")
    public void i_am_an_authorized_merchant()  {

      paymentRequest.setAuthToken(accessToken.getAccessToken().path("access_token").toString());
      paymentRequest.setAuthTokenwithBearer(paymentRequest.getAuthToken());

      checkStatus.setAuthToken(accessToken.getAccessToken().path("access_token").toString());
      checkStatus.setAuthTokenwithBearer(checkStatus.getAuthToken());

      refund.setAuthToken(accessToken.getAccessToken().path("access_token").toString());
      refund.setAuthTokenwithBearer(refund.getAuthToken());

    }

    @Given("^I dont send Bearer with the auth token$")
    public void no_bearer_as_prefix()  {

        paymentRequest.setAuthToken(accessToken.getAccessToken().path("access_token").toString());

    }

    
    @Given("^I am a merchant with invalid auth token$")
    public void i_am_an_authorized_merchant_invalid_token()  {
        paymentRequest.setAuthToken("random_authtoken");
        checkStatus.setAuthToken("random_authtoken");
        refund.setAuthToken("random_authtoken");

    }

    @Given("^I am a merchant with unverified auth token$")
    public void i_am_an_authorized_merchant_unverified_token()  {
        paymentRequest.setAuthToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        checkStatus.setAuthToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        refund.setAuthToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");

    }

    @Given("^I am a merchant with missing auth token$")
    public void i_am_an_authorized_merchant_missing_token()  {
        paymentRequest.setAuthToken("");
        checkStatus.setAuthToken("");
        refund.setAuthToken("");
    }



    @Given("^I have transaction details \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"$")
    public void i_have_valid_transaction_details(String amount, String currency, String description, String channel, String invoiceId, String merchantId, String effectiveDuration, String returnURL)  {
        paymentRequest.setTraceId(general.generateUniqueUUID());
        paymentRequest.createTransaction(amount,currency,description,channel,invoiceId,merchantId,effectiveDuration,returnURL);
        paymentRequest.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));

    }


    @When("^I make a request for the payment$")
    public void i_make_a_request_for_the_payment()  {
        paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_path"));
        
    }

    @Then("^I should recieve a payment response with valid trace id in the header$")
    public void i_should_recieve_a_response_with_valid_trace_id_in_the_header()  {

        Assert.assertEquals(restHelper.getResponseStatusCode(paymentRequest.getPaymentRequestResponse()), 200,"Request was not successful!");

        Assert.assertNotNull(paymentRequest.traceIdInResponseHeader(), "Trace Id is not present in the response header!!");

        Assert.assertEquals(paymentRequest.traceIdInResponseHeader(), paymentRequest.getTraceId(),"Trace Id present in the response is not matching with the Trace Id passed in the request!!");
        
    }

    @Then("^the response body should contain valid payment id, created timestamp, transaction details, links, expiry Duration$")
    public void the_response_body_should_contain_valid_payment_id_created_timestamp_transaction_details_links() throws Throwable {
        Assert.assertNotNull(paymentRequest.paymentIdInResponse(), "Payment Id is not present in the response!!");

        Assert.assertNotNull(paymentRequest.createdTimestampInResponse(), "Created Timestamp is not present in the response!!");

        Assert.assertTrue(paymentRequest.isLinksValid(),"Links within response is either incomplete or incorrect..Please check!!");

        Assert.assertNotNull(restHelper.getResponseBodyValue(paymentRequest.getPaymentRequestResponse(), "transaction"), "Transaction details is missing from the response..Please check!!");

        Assert.assertNull(paymentRequest.isTransactionValid(), paymentRequest.isTransactionValid()+ "..Please check!");

        Assert.assertTrue(paymentRequest.isExpiryDurationValid(), "Expiry Duration is not valid!");

    }


    @Then("^I should recieve a (\\d+) error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within payment response$")
    public void i_should_recieve_a_error_response_with_error_description_and_errorcode(int responseCode, String errorDesc, String errorCode) {
       // logger.info(" Error message: "+ restHelper.getErrorMessage(paymentRequest.getPaymentRequestResponse()));

        Assert.assertEquals(restHelper.getResponseStatusCode(paymentRequest.getPaymentRequestResponse()), responseCode,"Different response code being returned");

        Assert.assertEquals(restHelper.getErrorCode(paymentRequest.getPaymentRequestResponse()), errorCode,"Different error code being returned");

       // Assert.assertEquals(restHelper.getErrorDescription(paymentRequest.getPaymentRequestResponse()), errorDesc,"Different error description being returned");

        Assert.assertTrue(restHelper.getErrorDescription(paymentRequest.getPaymentRequestResponse()).contains(errorDesc) ,"Different error description being returned");

    }

    @Then("^error message should be \"([^\"]*)\" within payment response$")
    public void i_should_recieve_a_error_message(String errorMessage) {

        Assert.assertTrue(restHelper.getErrorMessage(paymentRequest.getPaymentRequestResponse()).contains(errorMessage) ,"Different error message being returned");

    }


    @Given("^I send request date timestamp in an invalid \"([^\"]*)\"$")
    public void i_send_request_date_timestamp_in_an_invalid(String format) {
        paymentRequest.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), format));

    }

    @Given("^I send invalid \"([^\"]*)\"$")
    public void i_send_invalid(String traceId) {
        paymentRequest.setTraceId(traceId);
        refund.setTraceId(traceId);
        checkStatus.setTraceId(traceId);
    }

    @Given("^I do not send request date timestamp in the header$")
    public void i_do_not_send_request_date_timestamp_in_the_header() {
        paymentRequest.setRequestDateTime("");
    }

    @Given("^I do not send traceid in the header$")
    public void i_do_not_send_traceid_in_the_header() {

        paymentRequest.setTraceId("");
        refund.setTraceId("");
        checkStatus.setTraceId("");
    }


    @Given("^request date timestamp in the header is more than (\\d+) mins behind than the current timestamp$")
    public void request_date_timestamp_in_the_header_is_mins_more_than_the_current_timestamp(int minutes)  {
        Calendar reqDate= dateHelper.getSystemDateandTimeStamp();
        reqDate= dateHelper.subtractMinutesFromSystemDateTime(reqDate, minutes+1);

        paymentRequest.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(reqDate, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));

    }

    @When("^I make two payment requests with the same trace id within (\\d+) minutes$")
    public void i_make_two_requests_with_the_same_trace_id_within_minutes(int minutes) {
        paymentResponses.add(paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_path")));

        general.waitFor(minutes*60);
        paymentRequest.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));

        paymentResponses.add(paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_path")));

    }


    @Then("^I should recieve one valid payment response$")
    public void i_should_recieve_one_valid_payment_response() {
        Assert.assertEquals(paymentResponses.get(0).statusCode(), 200, "First request failed!");
    }

    @Then("^one invalid payment response with (\\d+) status code$")
    public void one_invalid_payment_response_with_status_code(int statusCode) {
        Assert.assertEquals(paymentResponses.get(1).statusCode(), statusCode, "Second request has returned a different status code!");

    }

    @Then("^\"([^\"]*)\" error description and \"([^\"]*)\" errorcode within payment response$")
    public void error_description_and_errorcode_within_payment_response(String errorDesc, String errorCode)  {
        Assert.assertEquals(restHelper.getErrorCode(paymentResponses.get(1)), errorCode,"Different error code being returned");

        Assert.assertEquals(restHelper.getErrorDescription(paymentResponses.get(1)), errorDesc,"Different error description being returned");

    }


    @When("^I make two payment requests with the same trace id with a gap of (\\d+) minutes$")
    public void i_make_two_payment_requests_with_the_same_trace_id_with_a_gap_of_minutes(int minutes)  {
        paymentResponses.add(paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_path")));

        general.waitFor((minutes+1)*60);
        paymentRequest.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));

        paymentResponses.add(paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_path")));

    }

    @Then("^I should recieve two valid payment responses$")
    public void i_should_recieve_two_valid_payment_responses()  {
        Assert.assertEquals(paymentResponses.get(0).statusCode(), 200, "First request failed!");
        Assert.assertEquals(paymentResponses.get(1).statusCode(), 200, "Second request failed!");
    }

    @When("^I make two payment requests with the different trace ids within (\\d+) minutes$")
    public void i_make_two_payment_requests_with_the_different_trace_ids_within_minutes(int minutes)  {
        paymentResponses.add(paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_path")));

        general.waitFor((minutes-1)*60);
        paymentRequest.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        paymentRequest.setTraceId(general.generateUniqueUUID());

        paymentResponses.add(paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_path")));

    }



}
