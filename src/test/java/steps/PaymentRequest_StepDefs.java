package steps;

import com.jayway.restassured.response.Response;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.BaseStep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class PaymentRequest_StepDefs implements BaseStep {

    final static Logger logger = Logger.getLogger(PaymentRequest_StepDefs.class);
    List<Response> paymentResponses= new ArrayList<Response>();

    @Given("^I am an authorized user$")
    public void i_am_an_authorized_user()  {

        paymentRequest.setAuthToken(accessToken.getAccessToken());
        paymentRequest.setAuthTokenwithBearer(paymentRequest.getAuthToken());

        paymentStatus.setAuthToken(accessToken.getAccessToken());
        paymentStatus.setAuthTokenwithBearer();

        refunds.setAuthToken(accessToken.getAccessToken());
        refunds.setAuthTokenwithBearer();

        if(accessToken.getType().equalsIgnoreCase("merchant")){
                restHelper.setBaseURI(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "merchant-api-management-url")
                        +fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_APIs"));
        }

        else{
                restHelper.setBaseURI(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "sandbox-api-management-url")
                        +fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_APIs"));
        }

    }

    @Given("^I dont send Bearer with the auth token$")
    public void no_bearer_as_prefix()  {

        paymentRequest.setAuthToken(accessToken.getAccessToken());

    }



    @Given("^I am a merchant with invalid \"([^\"]*)\"$")
    public void i_am_a_merchant_with_invalid_token(String token)  {
        paymentRequest.setAuthToken(token);
        paymentRequest.setAuthTokenwithBearer(paymentRequest.getAuthToken());

        if(accessToken.getType().equalsIgnoreCase("merchant")){
                restHelper.setBaseURI(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "merchant-api-management-url")
                        +fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_APIs"));
        }

        else{
                restHelper.setBaseURI(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "sandbox-api-management-url")
                        +fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_APIs"));
        }

    }

    @Given("^I have payment details \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"$")
    public void i_have_payment_details(String totalAmount, String currency, String notificationURI, String appSuccessCallback, String appFailCallback, String effectiveDuration){
        paymentRequest.setTotalAmount(totalAmount);
        paymentRequest.setCurrency(currency);
        paymentRequest.setNotificationURI(notificationURI);
        paymentRequest.setAppSuccessCallback(appSuccessCallback);
        paymentRequest.setAppFailCallback(appFailCallback);
        paymentRequest.setEffectiveDuration(effectiveDuration);
        paymentRequest.setShoppingCart(null);
        paymentRequest.setMerchantData(null);

        //paymentRequest.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        paymentRequest.setRequestDateTime(dateHelper.getUTCNowDateTime());
        paymentRequest.setTraceId(general.generateUniqueUUID());
    }


    @Given("^I have valid payment details$")
    public void i_have_valid_payment_details(){
        paymentRequest.setTotalAmount("100");
        paymentRequest.setCurrency("HKD");
        paymentRequest.setNotificationURI("https://pizzahut.com/return");
        paymentRequest.setAppSuccessCallback("https://pizzahut.com/confirmation");
        paymentRequest.setAppFailCallback("https://pizzahut.com/unsuccessful");
        paymentRequest.setEffectiveDuration("600");
        paymentRequest.setShoppingCart(null);
        paymentRequest.setMerchantData(null);

       // paymentRequest.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        paymentRequest.setRequestDateTime(dateHelper.getUTCNowDateTime());
        paymentRequest.setTraceId(general.generateUniqueUUID());
    }

    @Given("^I have shopping cart details$")
    public void i_have_shopping_cart_details(DataTable dt) {
        paymentRequest.createShoppingCart(dt);
    }


    @Given("^I have merchant data \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"$")
    public void i_have_merchant_data(String description, String orderId, String additionalData) {
         paymentRequest.createMerchantData(description, orderId,additionalData);
    }

    @Given("^the additionalData is of more than (\\d+) characters$")
    public void the_additionalData_is_of_more_than_characters(int arg1) {
       String invalidAdditionalData="Morethan1024charactersMorethan1024characters" +
               "Morethan1024charactersMorethan1024charactersMorethan1024characters" +
               "Morethan1024charactersMorethan1024charactersMorethan1024characters" +
               "Morethan1024charactersMorethan1024charactersMorethan1024characters" +
               "Morethan1024charactersMorethan1024charactersMorethan1024characters" +
               "Morethan1024charactersMorethan1024charactersMorethan1024characters" +
               "Morethan1024charactersMorethan1024charactersMorethan1024characters" +
               "Morethan1024charactersMorethan1024charactersMorethan1024characters" +
               "Morethan1024charactersMorethan1024charactersMorethan1024characters" +
               "Morethan1024charactersMorethan1024charactersMorethan1024characters" +
               "Morethan1024charactersMorethan1024charactersMorethan1024characters" +
               "Morethan1024charactersMorethan1024charactersMorethan1024characters" +
               "Morethan1024charactersMorethan1024charactersMorethan1024characters" +
               "Morethan1024charactersMorethan1024charactersMorethan1024characters" +
               "Morethan1024charactersMorethan1024charactersMorethan1024characters" +
               "Morethan1024charactersMorethan1024charactersMorethan1024characters";
        paymentRequest.createMerchantData("description", "B12421832", invalidAdditionalData);
    }

    @When("^I make a request for the payment$")
    public void i_make_a_request_for_the_payment()  {
        logger.info("********** Creating Payment Request ***********");
        paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                accessToken.getClientId(),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @When("^I make a request for the payment with \"([^\"]*)\" missing in the header$")
    public void i_make_a_request_for_the_payment_with_missing_in_the_header(String key)  {
        paymentRequest.retrievePaymentRequestWithMissingHeaderKeys(restHelper.getBaseURI()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"), key,
                accessToken.getClientId(),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @Then("^I should recieve a successful payment response$")
    public void i_should_recieve_a_successful_response()  {
        Assert.assertEquals(restHelper.getResponseStatusCode(paymentRequest.getPaymentRequestResponse()), 201,"Request was not successful!");
        Assert.assertNotNull(paymentRequest.getPaymentRequestResponse(), "The response for Create Payment Request was null");

    }

    @Then("^the response body should contain valid payment request id, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration$")
    public void the_response_body_should_contain_valid_payment_id_created_timestamp_links(){
        Assert.assertNotNull(paymentRequest.paymentRequestIdInResponse(), "Payment Request Id is not present in the response!!");

        Assert.assertNotNull(paymentRequest.createdTimestampInResponse(), "Created Timestamp is not present in the response!!");

        Assert.assertNotNull(paymentRequest.webLinkInResponse(), "Web Link is not present in the response!!");

        Assert.assertNotNull(paymentRequest.appLinkInResponse(), "App Link is not present in the response!!");

        Assert.assertEquals(paymentRequest.effectiveDurationInResponse().toString(), "600", "Effective Duration isn't 600!");

        Assert.assertEquals(paymentRequest.statusCodeInResponse(), "PR001", "Status Code is not PR001");

        Assert.assertEquals(paymentRequest.statusDescriptionInResponse(), "Request for Payment Initiated", "Status Description is not \"Request for Payment Initiated\"");

        Assert.assertEquals(String.format("%.2f", Double.parseDouble(paymentRequest.totalAmountInResponse())), String.format("%.2f", paymentRequest.getTotalAmountInDouble()), "Total Amount isn't matching!");

        Assert.assertEquals(paymentRequest.currencyCodeInResponse(), paymentRequest.getCurrency(), "Currency Code isn't matching!");


        // Assert.assertEquals(paymentRequest.effectiveDurationInResponse(), paymentRequest.getEffectiveDuration(), "Effective Duration isn't matching!");
    }

    @Then("^the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable$")
    public void the_response_body_should_also_have_notification_url_app_success_callback_app_fail_callback_uri_if_applicable() throws Throwable {
        if (paymentRequest.getnotificationURI()==null)
            Assert.assertNull(paymentRequest.notificationURIInResponse(), "NotificationUri is present within the response when it should not be");
        else
            Assert.assertEquals(paymentRequest.notificationURIInResponse(), paymentRequest.getnotificationURI(), "Notification Uri isn't matching!");

        if (paymentRequest.getAppSuccessCallback()==null)
            Assert.assertNull(paymentRequest.appSuccessCallbackInResponse(), "App Success Call Back is present within the response when it should not be");
        else
            Assert.assertEquals(paymentRequest.appSuccessCallbackInResponse(), paymentRequest.getAppSuccessCallback(), "App Success Callback isn't matching!");


        if (paymentRequest.getAppFailCallback()==null)
            Assert.assertNull(paymentRequest.appFailCallbackInResponse(), "App Fail Call Back is present within the response when it should not be");
        else
            Assert.assertEquals(paymentRequest.appFailCallbackInResponse(), paymentRequest.getAppFailCallback(), "App Fail Callback isn't matching!");


    }


    @Then("^I should recieve a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within payment response$")
    public void i_should_recieve_a_error_response_with_error_description_and_errorcode(int responseCode, String errorDesc, String errorCode) {
       // logger.info(" Error message: "+ restHelper.getErrorMessage(paymentRequest.getPaymentRequestResponse()));

        Assert.assertEquals(restHelper.getResponseStatusCode(paymentRequest.getPaymentRequestResponse()), responseCode,"Different response code being returned");

        Assert.assertEquals(restHelper.getErrorCode(paymentRequest.getPaymentRequestResponse()), errorCode,"Different error code being returned");

       // Assert.assertEquals(restHelper.getErrorDescription(paymentRequest.getPaymentRequestResponse()), errorDesc,"Different error description being returned");

        Assert.assertTrue(restHelper.getErrorDescription(paymentRequest.getPaymentRequestResponse()).contains(errorDesc) ,"Different error description being returned..Expected: "+ errorDesc+ "Actual: "+ restHelper.getErrorDescription(paymentRequest.getPaymentRequestResponse()));

    }

    @Then("^error message should be \"([^\"]*)\" within payment response$")
    public void i_should_recieve_a_error_message(String errorMessage) {

        Assert.assertTrue(restHelper.getErrorMessage(paymentRequest.getPaymentRequestResponse()).contains(errorMessage) ,"Different error message being returned..Expected: "+ errorMessage+ " Actual: "+restHelper.getErrorMessage(paymentRequest.getPaymentRequestResponse()));

    }


    @Then("^I should recieve a (\\d+) error response within payment response$")
    public void i_should_recieve_a_error_response_within_payment_response(int errorCode) {
        Assert.assertEquals(restHelper.getResponseStatusCode(paymentRequest.getPaymentRequestResponse()), errorCode,"Different response code being returned");

    }

    @Given("^I send request date timestamp in an invalid \"([^\"]*)\"$")
    public void i_send_request_date_timestamp_in_an_invalid(String format) {
        paymentRequest.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), format));

    }

    @Then("^\"([^\"]*)\" error description and \"([^\"]*)\" errorcode within payment response$")
    public void error_description_and_errorcode_within_payment_response(String errorDesc, String errorCode)  {
        Assert.assertEquals(restHelper.getErrorCode(paymentResponses.get(1)), errorCode,"Different error code being returned");

        Assert.assertEquals(restHelper.getErrorDescription(paymentResponses.get(1)), errorDesc,"Different error description being returned");

    }


    @Given("^I have payment details with \"([^\"]*)\" set for the \"([^\"]*)\"$")
    public void i_have_payment_details_with_set_for_the(String invalid_value, String parameter) {
        paymentRequest.setTotalAmount("20");
        paymentRequest.setCurrency("HKD");
        paymentRequest.setNotificationURI("https://pizzahut.com/return");
        paymentRequest.setAppSuccessCallback("https://pizzahut.com/confirmation");
        paymentRequest.setAppFailCallback("https://pizzahut.com/unsuccessful");
        paymentRequest.setEffectiveDuration("600");

        //paymentRequest.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        paymentRequest.setRequestDateTime(dateHelper.getUTCNowDateTime());
        paymentRequest.setTraceId(general.generateUniqueUUID());

        if (parameter.equalsIgnoreCase("totalamount"))
            paymentRequest.setTotalAmount(invalid_value);


    }

    @Given("^I have valid payment details with no TraceId sent in the header$")
    public void i_have_valid_payment_details_with_no_TraceId_sent_in_the_header() {
        paymentRequest.setTotalAmount("20");
        paymentRequest.setCurrency("HKD");
        paymentRequest.setNotificationURI("https://pizzahut.com/return");
        paymentRequest.setAppSuccessCallback("https://pizzahut.com/confirmation");
        paymentRequest.setAppFailCallback("https://pizzahut.com/unsuccessful");
        paymentRequest.setEffectiveDuration("600");

       // paymentRequest.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        paymentRequest.setRequestDateTime(dateHelper.getUTCNowDateTime());


    }

    @Given("^I have valid payment details with invalid value \"([^\"]*)\" set for Request Date Time sent in the header$")
    public void i_have_valid_payment_details_with_no_RequestDateTime_sent_in_the_header(String value) {
        paymentRequest.setTotalAmount("20");
        paymentRequest.setCurrency("HKD");
        paymentRequest.setNotificationURI("https://pizzahut.com/return");
        paymentRequest.setAppSuccessCallback("https://pizzahut.com/confirmation");
        paymentRequest.setAppFailCallback("https://pizzahut.com/unsuccessful");
        paymentRequest.setEffectiveDuration("600");

        paymentRequest.setTraceId(general.generateUniqueUUID());
        paymentRequest.setRequestDateTime(value);

    }




}
