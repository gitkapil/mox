package steps;

import managers.TestContext;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import java.util.Arrays;
import java.util.HashSet;

public class PaymentStatus_StepDefs extends UtilManager{

    TestContext testContext;

    public PaymentStatus_StepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    final static Logger logger = Logger.getLogger(PaymentStatus_StepDefs.class);

    @Given("^I have a valid payment id$")
    public void i_have_a()  {
        testContext.getApiManager().getPaymentStatus().setPaymentRequestId(testContext.getApiManager().getPaymentRequest().paymentRequestIdInResponse());
        testContext.getApiManager().getPaymentStatus().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getPaymentStatus().setRequestDateTime(getDateHelper().getUTCNowDateTime());
    }

    @When("^I make a request for the check status$")
    public void i_make_a_request_for_the_check_status(){

        testContext.getApiManager().getPaymentStatus().retrievePaymentStatus(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,"signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,"signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));

    }

    @Then("^I should receive a successful check status response$")
    public void i_should_receive_a_successful_check_status_response() {
        logger.info("********** Retrieving Payment Request Status ***********");
        Assert.assertEquals(getRestHelper().getResponseHeaderValue(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse(), "X-Application-Context "), null, "Expects X-Application-Context header to not exists");
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()), 200,"Check Payment Status was not successful!");

    }

    @Then("^the response body should contain valid payment request id, created timestamp, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration within check status response$")
    public void the_response_body_should_contain_valid_payment_id_created_timestamp_links_check_status()  {
        Assert.assertNotNull(testContext.getApiManager().getPaymentStatus().paymentRequestIdInResponse(), "Payment Request Id is not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPaymentStatus().createdTimestampInResponse(), "Created Timestamp is not present in the response!!");

        Assert.assertEquals(testContext.getApiManager().getPaymentStatus().effectiveDurationInResponse().toString(),
                testContext.getApiManager().getPaymentRequest().getEffectiveDuration(), "Effective Duration does not match");

        Assert.assertNotNull(testContext.getApiManager().getPaymentStatus().statusCodeInResponse(), "Status Code is not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPaymentStatus().statusDescriptionInResponse(), "Status Description is not present in the response!!");

        Assert.assertEquals(String.format("%.2f", Double.parseDouble(testContext.getApiManager().getPaymentStatus().totalAmountInResponse())), String.format("%.2f", testContext.getApiManager().getPaymentRequest().getTotalAmountInDouble()), "Total Amount isn't matching!");

        Assert.assertEquals(testContext.getApiManager().getPaymentStatus().currencyCodeInResponse(), testContext.getApiManager().getPaymentRequest().getCurrency(), "Currency Code isn't matching!");


        // Assert.assertEquals(testContext.getApiManager().getPaymentRequest().effectiveDurationInResponse(), testContext.getApiManager().getPaymentRequest().getEffectiveDuration(), "Effective Duration isn't matching!");
    }

    @Then("^the response body should also have app success callback URL, app fail Callback Url if applicable within check status response$")
    public void the_response_body_should_also_have_app_success_callback_app_fail_callback_uri_if_applicable_check_status()  {

        if (testContext.getApiManager().getPaymentRequest().getAppSuccessCallback()==null)
            Assert.assertNull(testContext.getApiManager().getPaymentStatus().appSuccessCallbackInResponse(), "App Success Call Back is present within the response when it should not be");
        else{
            if (testContext.getApiManager().getAccessToken().getType().equalsIgnoreCase("merchant"))
            {
                if(System.getProperty("env").equalsIgnoreCase("ci"))
                    Assert.assertEquals(testContext.getApiManager().getPaymentStatus().appSuccessCallbackInResponse(),"http://localhost/success", "App Success Callback isn't matching with emulator!");
                else
                    Assert.assertEquals(testContext.getApiManager().getPaymentStatus().appSuccessCallbackInResponse(), testContext.getApiManager().getPaymentRequest().getAppSuccessCallback(), "App Success Callback isn't matching!");
            }
            else
                Assert.assertEquals(testContext.getApiManager().getPaymentStatus().appSuccessCallbackInResponse(),"http://localhost/success", "App Success Callback isn't matching with emulator!");
        }


        if (testContext.getApiManager().getPaymentRequest().getAppFailCallback()==null)
            Assert.assertNull(testContext.getApiManager().getPaymentStatus().appFailCallbackInResponse(), "App Fail Call Back is present within the response when it should not be");
        else
        {
            if (testContext.getApiManager().getAccessToken().getType().equalsIgnoreCase("merchant"))
            {
                if(System.getProperty("env").equalsIgnoreCase("ci"))
                    Assert.assertEquals(testContext.getApiManager().getPaymentStatus().appFailCallbackInResponse(),"http://localhost/fail", "App Fail Callback isn't matching with emulator!");
                else
                    Assert.assertEquals(testContext.getApiManager().getPaymentStatus().appFailCallbackInResponse(), testContext.getApiManager().getPaymentRequest().getAppFailCallback(), "App Fail Callback isn't matching!");

            }
            else
                Assert.assertEquals(testContext.getApiManager().getPaymentStatus().appFailCallbackInResponse(),"http://localhost/fail", "App Fail Callback isn't matching with emulator!");
        }


    }


    @Given("^I dont send Bearer with the auth token in the check status request$")
    public void i_dont_send_Bearer_with_the_auth_token_in_the_check_status_request(){

        testContext.getApiManager().getPaymentStatus().setAuthToken(testContext.getApiManager().getAccessToken().getAccessToken());

    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within check status response$")
    public void i_should_receive_a_error_response_with_error_description_and_errorcode_within_check_status_response(int responseCode, String errorDesc, String errorCode) {
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()), responseCode,"Different response code being returned");

        Assert.assertEquals(getRestHelper().getErrorCode(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()), errorCode,"Different error code being returned");

        Assert.assertTrue(getRestHelper().getErrorDescription(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()).contains(errorDesc) ,"Different error description being returned..Expected: "+ errorDesc+ "  Actual: "+ getRestHelper().getErrorDescription(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()));

    }

    @Then("^error message should be \"([^\"]*)\" within check status response$")
    public void error_message_should_be_within_check_status_response(String errorMessage) {
        Assert.assertTrue(getRestHelper().getErrorMessage(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()).contains(errorMessage) ,"Different error message being returned..Expected: "+ errorMessage+ "  Actual: "+ getRestHelper().getErrorMessage(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()));

    }

    @When("^I make a request for the payment status with \"([^\"]*)\" missing in the header$")
    public void i_make_a_request_for_the_payment_status_with_missing_in_the_header(String key) {
        testContext.getApiManager().getPaymentStatus().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getPaymentStatus().setRequestDateTime(getDateHelper().getUTCNowDateTime());

        testContext.getApiManager().getPaymentStatus().retrievePaymentStatusWithMissingHeaderKeys(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"), key,
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,"signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,"signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));

    }

    @When("^I send invalid auth token \"([^\"]*)\" in the check status request$")
    public void i_send_invalid_in_the_check_status_request(String authToken) {
        testContext.getApiManager().getPaymentStatus().setAuthToken(authToken);
        testContext.getApiManager().getPaymentStatus().setAuthTokenwithBearer();
    }

    @Given("^I have a payment id \"([^\"]*)\"$")
    public void i_have_a_valid(String paymentReqId) {
        testContext.getApiManager().getPaymentStatus().setPaymentRequestId(paymentReqId);
        testContext.getApiManager().getPaymentStatus().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getPaymentStatus().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getPaymentRequest().setNotificationURI(null);
        testContext.getApiManager().getPaymentRequest().setAppFailCallback(null);
        testContext.getApiManager().getPaymentRequest().setAppSuccessCallback(null);
    }

    @Then("^the response body should contain correct \"([^\"]*)\" and \"([^\"]*)\"$")
    public void the_response_body_should_contain_correct_and(String statusDesc, String statusCode)  {
        Assert.assertEquals(testContext.getApiManager().getPaymentStatus().statusDescriptionInResponse(), statusDesc,"Status Description is not correct!");

        Assert.assertEquals(testContext.getApiManager().getPaymentStatus().statusCodeInResponse(), statusCode,"Status Code is not correct!");
    }

    @When("^I make a request for the check status with invalid value for request date time \"([^\"]*)\"$")
    public void invalid_value_request_date_time(String value)  {
        testContext.getApiManager().getPaymentStatus().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getPaymentStatus().setRequestDateTime(value);

        testContext.getApiManager().getPaymentStatus().retrievePaymentStatus(getRestHelper().getBaseURI()+getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,"signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,"signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));

    }



}
