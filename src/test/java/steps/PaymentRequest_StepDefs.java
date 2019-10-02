package steps;

import managers.TestContext;
import com.jayway.restassured.response.Response;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.EnvHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class PaymentRequest_StepDefs extends UtilManager {

    TestContext testContext;

    public PaymentRequest_StepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    final static Logger logger = Logger.getLogger(PaymentRequest_StepDefs.class);
    List<Response> paymentResponses = new ArrayList<Response>();

    @Given("^I am an authorized user$")
    public void i_am_an_authorized_user() {

        testContext.getApiManager().getPaymentRequest().setAuthToken(testContext.getApiManager().getAccessToken().getAccessToken());
        testContext.getApiManager().getPaymentRequest().setAuthTokenwithBearer(testContext.getApiManager().getPaymentRequest().getAuthToken());

        testContext.getApiManager().getPaymentStatus().setAuthToken(testContext.getApiManager().getAccessToken().getAccessToken());
        testContext.getApiManager().getPaymentStatus().setAuthTokenwithBearer();

        testContext.getApiManager().getTransaction().setAuthToken(testContext.getApiManager().getAccessToken().getAccessToken());
        testContext.getApiManager().getTransaction().setAuthTokenwithBearer();

        // refunds.setAuthToken(testContext.getApiManager().getAccessToken().getAccessToken());
        // refunds.setAuthTokenwithBearer();

        if (testContext.getApiManager().getAccessToken().getType().equalsIgnoreCase("merchant")) {
            getRestHelper().setBaseURI(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-api-management-url")
                    + getEnvSpecificBasePathAPIs());
        } else {
            getRestHelper().setBaseURI(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "sandbox-api-management-url")
                    + getEnvSpecificBasePathAPIs());
        }

    }

    private String getEnvSpecificBasePathAPIs() {
        if (EnvHelper.getInstance().isLocalDevMode()) {
            return getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "Base_Path_APIs");
        }
        return getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_APIs");
    }

    @Given("^I dont send Bearer with the auth token$")
    public void no_bearer_as_prefix() {

        testContext.getApiManager().getPaymentRequest().setAuthToken(testContext.getApiManager().getAccessToken().getAccessToken());

    }


    @Given("^I am a merchant with invalid \"([^\"]*)\"$")
    public void i_am_a_merchant_with_invalid_token(String token) {
        testContext.getApiManager().getPaymentRequest().setAuthToken(token);
        testContext.getApiManager().getPaymentRequest().setAuthTokenwithBearer(testContext.getApiManager().getPaymentRequest().getAuthToken());

        if (testContext.getApiManager().getAccessToken().getType().equalsIgnoreCase("merchant")) {
            getRestHelper().setBaseURI(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-api-management-url")
                    + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_APIs"));
        } else {
            getRestHelper().setBaseURI(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "sandbox-api-management-url")
                    + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_APIs"));
        }

    }

    @Given("^I have payment details \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"$")
    public void i_have_payment_details(String totalAmount, String currency, String notificationURI, String appSuccessCallback, String appFailCallback, String effectiveDuration) {
        testContext.getApiManager().getPaymentRequest().setTotalAmount(totalAmount);
        testContext.getApiManager().getPaymentRequest().setCurrency(currency);
        testContext.getApiManager().getPaymentRequest().setNotificationURI(Hooks.hostIP + notificationURI);
        testContext.getApiManager().getPaymentRequest().setAppSuccessCallback(Hooks.hostIP + appSuccessCallback);
        testContext.getApiManager().getPaymentRequest().setAppFailCallback(Hooks.hostIP + appFailCallback);
        testContext.getApiManager().getPaymentRequest().setEffectiveDuration(effectiveDuration);
        testContext.getApiManager().getPaymentRequest().setShoppingCart(null);
        testContext.getApiManager().getPaymentRequest().setMerchantData(null);
        testContext.getApiManager().getPaymentRequest().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getPaymentRequest().setTraceId(getGeneral().generateUniqueUUID());
    }


    @Given("^I have valid payment details$")
    public void i_have_valid_payment_details() {
        testContext.getApiManager().getPaymentRequest().setTotalAmount("100");
        testContext.getApiManager().getPaymentRequest().setCurrency("HKD");
        //testContext.getApiManager().getPaymentRequest().setNotificationURI("https://pizzahut.com/return");
        //testContext.getApiManager().getPaymentRequest().setAppSuccessCallback("https://pizzahut.com/confirmation");
        //testContext.getApiManager().getPaymentRequest().setAppFailCallback("https://pizzahut.com/unsuccessful");
        testContext.getApiManager().getPaymentRequest().setNotificationURI(Hooks.hostIP + "/return");
        testContext.getApiManager().getPaymentRequest().setAppSuccessCallback(Hooks.hostIP + "/confirmation");
        testContext.getApiManager().getPaymentRequest().setAppFailCallback(Hooks.hostIP + "/unsuccessful");
        testContext.getApiManager().getPaymentRequest().setEffectiveDuration("600");
        testContext.getApiManager().getPaymentRequest().setShoppingCart(null);
        testContext.getApiManager().getPaymentRequest().setMerchantData(null);
        testContext.getApiManager().getPaymentRequest().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getPaymentRequest().setTraceId(getGeneral().generateUniqueUUID());
    }

    @Given("^I have shopping cart details$")
    public void i_have_shopping_cart_details(DataTable dt) {
        testContext.getApiManager().getPaymentRequest().createShoppingCart(dt);
    }


    @Given("^I have merchant data \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"$")
    public void i_have_merchant_data(String description, String orderId, String additionalData) {
        testContext.getApiManager().getPaymentRequest().createMerchantData(description, orderId, additionalData);
    }

    @Given("^the additionalData is of more than (\\d+) characters$")
    public void the_additionalData_is_of_more_than_characters(int arg1) {
        String invalidAdditionalData = "Morethan1024charactersMorethan1024characters" +
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
        testContext.getApiManager().getPaymentRequest().createMerchantData("description", "B12421832", invalidAdditionalData);
    }

    @When("^I make a request for the payment$")
    public void i_make_a_request_for_the_payment() {
        logger.info("********** Creating Payment Request ***********");
        testContext.getApiManager().getPaymentRequest().retrievePaymentRequest(getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @When("^I make a request for the payment with \"([^\"]*)\" missing in the header$")
    public void i_make_a_request_for_the_payment_with_missing_in_the_header(String key) {
        testContext.getApiManager().getPaymentRequest().retrievePaymentRequestWithMissingHeaderKeys(getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"), key,
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-post").split(","))));

    }

    @Then("^I should receive a successful payment response$")
    public void i_should_receive_a_successful_response() {
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse()), 201, "Request was not successful!");
        Assert.assertEquals(getRestHelper().getResponseHeaderValue(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse(), "X-Application-Context "), null, "Expects X-Application-Context header to not exists");
        Assert.assertNotNull(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse(), "The response for Create Payment Request was null");

    }

    @Then("^the response body should contain valid payment request id, business logos, created timestamp, web link, app link, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration$")
    public void the_response_body_should_contain_valid_payment_id_created_timestamp_links() {
        Assert.assertNotNull(testContext.getApiManager().getPaymentRequest().paymentRequestIdInResponse(), "Payment Request Id is not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPaymentRequest().businessLogosInResponse(), "Business Logos  are not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPaymentRequest().createdTimestampInResponse(), "Created Timestamp is not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPaymentRequest().webLinkInResponse(), "Web Link is not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPaymentRequest().appLinkInResponse(), "App Link is not present in the response!!");

        if (testContext.getApiManager().getPaymentRequest().getEffectiveDuration().isEmpty()) {
            Assert.assertEquals(testContext.getApiManager().getPaymentRequest().effectiveDurationInResponse().toString(), "600", "effectiveDuration should be 600 seconds by default if no effectiveDuration is provided in request.");
        } else {
            Assert.assertEquals(testContext.getApiManager().getPaymentRequest().effectiveDurationInResponse().toString(),
                    testContext.getApiManager().getPaymentRequest().getEffectiveDuration(),
                    "Effective Duration returned does not match.");
        }

        Assert.assertEquals(testContext.getApiManager().getPaymentRequest().statusCodeInResponse(), "PR001", "Status Code is not PR001");

        Assert.assertEquals(testContext.getApiManager().getPaymentRequest().statusDescriptionInResponse(), "Request for Payment Initiated", "Status Description is not \"Request for Payment Initiated\"");

        Assert.assertEquals(String.format("%.2f", Double.parseDouble(testContext.getApiManager().getPaymentRequest().totalAmountInResponse())),
                String.format("%.2f", testContext.getApiManager().getPaymentRequest().getTotalAmountInDouble()), "Total Amount isn't matching!");

        Assert.assertEquals(testContext.getApiManager().getPaymentRequest().currencyCodeInResponse(), testContext.getApiManager().getPaymentRequest().getCurrency(), "Currency Code isn't matching!");


        // Assert.assertEquals(testContext.getApiManager().getPaymentRequest().effectiveDurationInResponse(), testContext.getApiManager().getPaymentRequest().getEffectiveDuration(), "Effective Duration isn't matching!");
    }

    @Then("^the response body should also have notification URI, app success callback URL, app fail Callback Url if applicable$")
    public void the_response_body_should_also_have_notification_url_app_success_callback_app_fail_callback_uri_if_applicable() throws Throwable {
        if (testContext.getApiManager().getPaymentRequest().getnotificationURI() == null)
            Assert.assertNull(testContext.getApiManager().getPaymentRequest().notificationURIInResponse(), "NotificationUri is present within the response when it should not be");
        else
            Assert.assertEquals(testContext.getApiManager().getPaymentRequest().notificationURIInResponse(), testContext.getApiManager().getPaymentRequest().getnotificationURI(), "Notification Uri isn't matching!");

        if (testContext.getApiManager().getPaymentRequest().getAppSuccessCallback() == null)
            Assert.assertNull(testContext.getApiManager().getPaymentRequest().appSuccessCallbackInResponse(), "App Success Call Back is present within the response when it should not be");
        else
            Assert.assertEquals(testContext.getApiManager().getPaymentRequest().appSuccessCallbackInResponse(), testContext.getApiManager().getPaymentRequest().getAppSuccessCallback(), "App Success Callback isn't matching!");


        if (testContext.getApiManager().getPaymentRequest().getAppFailCallback() == null)
            Assert.assertNull(testContext.getApiManager().getPaymentRequest().appFailCallbackInResponse(), "App Fail Call Back is present within the response when it should not be");
        else
            Assert.assertEquals(testContext.getApiManager().getPaymentRequest().appFailCallbackInResponse(), testContext.getApiManager().getPaymentRequest().getAppFailCallback(), "App Fail Callback isn't matching!");


    }

    @Then("^I should receive a quoted \"([^\"]*)\" error response with \'(.*)\' error description and \"([^\"]*)\" errorcode within payment response$")
    public void i_should_receive_a_error_response_with_error_description_and_errorcode_1(int responseCode, String errorDesc, String errorCode) {
        i_should_receive_a_error_response_with_error_description_and_errorcode(responseCode, errorDesc, errorCode);
    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within payment response$")
    public void i_should_receive_a_error_response_with_error_description_and_errorcode(int responseCode, String errorDesc, String errorCode) {
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse()), responseCode, "Different response code being returned");

        Assert.assertTrue(

                getRestHelper().getErrorDescription(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse())
                        .replace("\"", "")
                        .contains(errorDesc),
                "Different error description being returned..Expected: " + errorDesc + "Actual: " + getRestHelper().getErrorDescription(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse()));

        Assert.assertEquals(getRestHelper().getErrorCode(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse()), errorCode, "Different error code being returned");
    }

    @Then("^error message should be \"([^\"]*)\" within payment response$")
    public void i_should_receive_a_error_message(String errorMessage) {
        Assert.assertTrue(
                getRestHelper().getErrorMessage(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse()).contains(errorMessage),
                "Different error message being returned..Expected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse()));

    }


    @Then("^I should receive a (\\d+) error response within payment response$")
    public void i_should_receive_a_error_response_within_payment_response(int errorCode) {
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getPaymentRequest().getPaymentRequestResponse()), errorCode, "Different response code being returned");

    }

    @Given("^I send request date timestamp in an invalid \"([^\"]*)\"$")
    public void i_send_request_date_timestamp_in_an_invalid(String format) {
        testContext.getApiManager().getPaymentRequest().setRequestDateTime(getDateHelper().convertDateTimeIntoAFormat(getDateHelper().getSystemDateandTimeStamp(), format));

    }

    @Then("^\"([^\"]*)\" error description and \"([^\"]*)\" errorcode within payment response$")
    public void error_description_and_errorcode_within_payment_response(String errorDesc, String errorCode) {
        Assert.assertEquals(getRestHelper().getErrorCode(paymentResponses.get(1)), errorCode, "Different error code being returned");

        Assert.assertEquals(getRestHelper().getErrorDescription(paymentResponses.get(1)), errorDesc, "Different error description being returned");

    }


    @Given("^I have payment details with \"([^\"]*)\" set for the \"([^\"]*)\"$")
    public void i_have_payment_details_with_set_for_the(String invalid_value, String parameter) {
        testContext.getApiManager().getPaymentRequest().setTotalAmount("20");
        testContext.getApiManager().getPaymentRequest().setCurrency("HKD");
        //testContext.getApiManager().getPaymentRequest().setNotificationURI("https://pizzahut.com/return");
        //testContext.getApiManager().getPaymentRequest().setAppSuccessCallback("https://pizzahut.com/confirmation");
        //testContext.getApiManager().getPaymentRequest().setAppFailCallback("https://pizzahut.com/unsuccessful");
        testContext.getApiManager().getPaymentRequest().setNotificationURI(Hooks.hostIP + "/return");
        testContext.getApiManager().getPaymentRequest().setAppSuccessCallback(Hooks.hostIP + "/confirmation");
        testContext.getApiManager().getPaymentRequest().setAppFailCallback(Hooks.hostIP + "/unsuccessful");
        testContext.getApiManager().getPaymentRequest().setEffectiveDuration("600");
        testContext.getApiManager().getPaymentRequest().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getPaymentRequest().setTraceId(getGeneral().generateUniqueUUID());

        if (parameter.equalsIgnoreCase("totalamount"))
            testContext.getApiManager().getPaymentRequest().setTotalAmount(invalid_value);


    }

    @Given("^I have valid payment details with no TraceId value sent in the header$")
    public void i_have_valid_payment_details_with_no_TraceId_sent_in_the_header() {
        testContext.getApiManager().getPaymentRequest().setTotalAmount("20");
        testContext.getApiManager().getPaymentRequest().setCurrency("HKD");
        /*testContext.getApiManager().getPaymentRequest().setNotificationURI("https://pizzahut.com/return");
        testContext.getApiManager().getPaymentRequest().setAppSuccessCallback("https://pizzahut.com/confirmation");
        testContext.getApiManager().getPaymentRequest().setAppFailCallback("https://pizzahut.com/unsuccessful");*/
        testContext.getApiManager().getPaymentRequest().setNotificationURI(Hooks.hostIP + "/return");
        testContext.getApiManager().getPaymentRequest().setAppSuccessCallback(Hooks.hostIP + "/confirmation");
        testContext.getApiManager().getPaymentRequest().setAppFailCallback(Hooks.hostIP + "/unsuccessful");
        testContext.getApiManager().getPaymentRequest().setEffectiveDuration("600");
        testContext.getApiManager().getPaymentRequest().setRequestDateTime(getDateHelper().getUTCNowDateTime());
    }

    @Given("^I have valid payment details with invalid value \"([^\"]*)\" set for Request Date Time sent in the header$")
    public void i_have_valid_payment_details_with_no_RequestDateTime_sent_in_the_header(String value) {
        testContext.getApiManager().getPaymentRequest().setTotalAmount("20");
        testContext.getApiManager().getPaymentRequest().setCurrency("HKD");
        /*testContext.getApiManager().getPaymentRequest().setNotificationURI("https://pizzahut.com/return");
        testContext.getApiManager().getPaymentRequest().setAppSuccessCallback("https://pizzahut.com/confirmation");
        testContext.getApiManager().getPaymentRequest().setAppFailCallback("https://pizzahut.com/unsuccessful")*/
        ;
        testContext.getApiManager().getPaymentRequest().setNotificationURI(Hooks.hostIP + "/return");
        testContext.getApiManager().getPaymentRequest().setAppSuccessCallback(Hooks.hostIP + "/confirmation");
        testContext.getApiManager().getPaymentRequest().setAppFailCallback(Hooks.hostIP + "/unsuccessful");
        testContext.getApiManager().getPaymentRequest().setEffectiveDuration("600");
        testContext.getApiManager().getPaymentRequest().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getPaymentRequest().setRequestDateTime(value);

    }


}
