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
import java.util.List;


public class PaymentRequest_StepDefs implements BaseStep {

    final static Logger logger = Logger.getLogger(PaymentRequest_StepDefs.class);
    List<Response> paymentResponses= new ArrayList<Response>();

    @Given("^I am an authorized merchant$")
    public void i_am_an_authorized_merchant()  {

      paymentRequest.setAuthToken(accessToken.getAccessToken());
      paymentRequest.setAuthTokenwithBearer(paymentRequest.getAuthToken());

        paymentStatus.setAuthToken(accessToken.getAccessToken());
        paymentStatus.setAuthTokenwithBearer();

    }

    @Given("^I dont send Bearer with the auth token$")
    public void no_bearer_as_prefix()  {

        paymentRequest.setAuthToken(accessToken.getAccessToken());

    }



    @Given("^I am a merchant with invalid \"([^\"]*)\"$")
    public void i_am_a_merchant_with_invalid_token(String token)  {
        if (token.equals("need_to_generate_it_with_invalid_appid")){

            System.out.println("Here- invalid appid!!");
            System.out.println("clientid 3: "+ fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "developer-client-id-3"));
            System.out.println("clientsecret 3: "+ fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "developer-client-secret-3"));

            accessToken.setMerchantDetails(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "developer-client-id-3"),
                    fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "developer-client-secret-3"));
            accessToken.createBody_RetrieveAccessToken();

            accessToken.retrieveAccessToken(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_base_path")+System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));
            paymentRequest.setAuthToken(accessToken.getAccessToken());
            paymentRequest.setAuthTokenwithBearer(paymentRequest.getAuthToken());

        }
        else if (token.equals("need_to_generate_it_with_invalid_roles")){
            System.out.println("Here- invalid roles!!");
            System.out.println("clientid 2: "+ fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "developer-client-id-2"));
            System.out.println("clientsecret 2: "+ fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "developer-client-secret-2"));

            accessToken.setMerchantDetails(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "developer-client-id-2"),
                    fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "developer-client-secret-2"));
            accessToken.createBody_RetrieveAccessToken();

            accessToken.retrieveAccessToken(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_base_path")+System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));
            paymentRequest.setAuthToken(accessToken.getAccessToken());
            paymentRequest.setAuthTokenwithBearer(paymentRequest.getAuthToken());

        }
        else
        {
            paymentRequest.setAuthToken(token);
            paymentRequest.setAuthTokenwithBearer(paymentRequest.getAuthToken());
        }
    }

    @Given("^I have payment details \"([^\"]*)\", \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"$")
    public void i_have_payment_details(String merchantId, String totalAmount, String currency, String notificationURI){
        paymentRequest.setMerchantId(merchantId);
        paymentRequest.setTotalAmount(Double.parseDouble(totalAmount));
        paymentRequest.setCurrency(currency);
        paymentRequest.setNotificationURI(notificationURI);
        paymentRequest.setShoppingCart(null);
        paymentRequest.setMerchantData(null);

        paymentRequest.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        paymentRequest.setTraceId(general.generateUniqueUUID());
    }


    @Given("^I have valid payment details$")
    public void i_have_valid_payment_details(){
        paymentRequest.setMerchantId("053598653254");
        paymentRequest.setTotalAmount(Double.parseDouble("20"));
        paymentRequest.setCurrency("HKD");
        paymentRequest.setNotificationURI("https://pizzahut.com/return");
        paymentRequest.setShoppingCart(null);
        paymentRequest.setMerchantData(null);

        paymentRequest.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        paymentRequest.setTraceId(general.generateUniqueUUID());
    }

    @Given("^I have shopping cart details$")
    public void i_have_shopping_cart_details(DataTable dt) {
        paymentRequest.createShoppingCart(dt);
    }


    @Given("^I have merchant data \"([^\"]*)\", \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"$")
    public void i_have_merchant_data(String description, String channel, String orderId, String effectiveDuration) {
         paymentRequest.createMerchantData(description, channel, orderId, effectiveDuration);
    }

    @When("^I make a request for the payment$")
    public void i_make_a_request_for_the_payment()  {
        logger.info("********** Creating Payment Request ***********");
        paymentRequest.retrievePaymentRequest(restHelper.getBaseURI()+System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_path"));
        
    }

    @When("^I make a request for the payment with \"([^\"]*)\" missing in the header$")
    public void i_make_a_request_for_the_payment_with_missing_in_the_header(String key)  {
        paymentRequest.retrievePaymentRequestWithMissingHeaderKeys(restHelper.getBaseURI()+System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_path"), key);

    }

    @Then("^I should recieve a successful payment response$")
    public void i_should_recieve_a_successful_response()  {

        Assert.assertEquals(restHelper.getResponseStatusCode(paymentRequest.getPaymentRequestResponse()), 200,"Request was not successful!");

    }

    @Then("^the response body should contain valid payment request id, created timestamp, web link, app link$")
    public void the_response_body_should_contain_valid_payment_id_created_timestamp_links() throws Throwable {
        Assert.assertNotNull(paymentRequest.paymentRequestIdInResponse(), "Payment Request Id is not present in the response!!");

        Assert.assertNotNull(paymentRequest.createdTimestampInResponse(), "Created Timestamp is not present in the response!!");

        Assert.assertNotNull(paymentRequest.webLinkInResponse(), "Web Link is not present in the response!!");

        Assert.assertNotNull(paymentRequest.appLinkInResponse(), "App Link is not present in the response!!");

        Assert.assertEquals(paymentRequest.effectiveDurationInResponse().toString(), "600", "Effective Duration isn't 600!");


        // Assert.assertEquals(paymentRequest.effectiveDurationInResponse(), paymentRequest.getEffectiveDuration(), "Effective Duration isn't matching!");
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

        Assert.assertTrue(restHelper.getErrorMessage(paymentRequest.getPaymentRequestResponse()).contains(errorMessage) ,"Different error message being returned");

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
        paymentRequest.setMerchantId("053598653254");
        paymentRequest.setTotalAmount(Double.parseDouble("20"));
        paymentRequest.setCurrency("HKD");
        paymentRequest.setNotificationURI("https://pizzahut.com/return");

        paymentRequest.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        paymentRequest.setTraceId(general.generateUniqueUUID());

        if (parameter.equalsIgnoreCase("totalamount"))
            paymentRequest.setTotalAmount(Double.parseDouble(invalid_value));


    }

    @Given("^I have valid payment details with no TraceId sent in the header$")
    public void i_have_valid_payment_details_with_no_TraceId_sent_in_the_header() {
        paymentRequest.setMerchantId("053598653254");
        paymentRequest.setTotalAmount(Double.parseDouble("20"));
        paymentRequest.setCurrency("HKD");
        paymentRequest.setNotificationURI("https://pizzahut.com/return");

        paymentRequest.setRequestDateTime(dateHelper.convertDateTimeIntoAFormat(dateHelper.getSystemDateandTimeStamp(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));


    }

    @Given("^I have valid payment details with no Request Date Time sent in the header$")
    public void i_have_valid_payment_details_with_no_RequestDateTime_sent_in_the_header() {
        paymentRequest.setMerchantId("053598653254");
        paymentRequest.setTotalAmount(Double.parseDouble("20"));
        paymentRequest.setCurrency("HKD");
        paymentRequest.setNotificationURI("https://pizzahut.com/return");

        paymentRequest.setTraceId(general.generateUniqueUUID());
        paymentRequest.setRequestDateTime("");

    }




}
