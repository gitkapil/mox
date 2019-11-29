package steps;

import apiHelpers.Transaction;
import com.jayway.restassured.response.Response;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Constants;
import utils.PropertyHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.*;
import java.util.zip.CheckedOutputStream;

public class PaymentStatus_StepDefs extends UtilManager {

    TestContext testContext;
    public String latestFirstTransactionId = "";

    public PaymentStatus_StepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    final static Logger logger = Logger.getLogger(PaymentStatus_StepDefs.class);

    @Given("^I have a valid payment id$")
    public void i_have_a() {
        testContext.getApiManager().getPaymentStatus().setPaymentRequestId(testContext.getApiManager().getPaymentRequest().paymentRequestIdInResponse());
        testContext.getApiManager().getPaymentStatus().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getPaymentStatus().setRequestDateTime(getDateHelper().getUTCNowDateTime());
    }

    @When("^I make a request for the check status$")
    public void i_make_a_request_for_the_check_status() {

        testContext.getApiManager().getPaymentStatus().retrievePaymentStatus(getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));
    }

    @When("^I make a request for the check status with POS role$")
    public void i_make_a_request_for_the_check_statusWithPOSRole() {

        testContext.getApiManager().getPaymentStatus().retrievePaymentStatusWithPOSRole(getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));
    }


    @When("^I make a request for the check status for amount \"([^\"]*)\"$")
    public void i_make_a_request_for_the_check_statusForAmount(String amount) throws InterruptedException {
        if (amount.equalsIgnoreCase("1.80")) {
            Thread.sleep(16000);
            testContext.getApiManager().getPaymentStatus().retrievePaymentStatus(getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                    testContext.getApiManager().getMerchantManagementSigningKeyId(),
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                    testContext.getApiManager().getMerchantManagementSigningKey(),
                    new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));
        } else {

            testContext.getApiManager().getPaymentStatus().retrievePaymentStatus(getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                    testContext.getApiManager().getMerchantManagementSigningKeyId(),
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                    testContext.getApiManager().getMerchantManagementSigningKey(),
                    new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));
        }
    }

    @Then("^I should receive a successful check status response$")
    public void i_should_receive_a_successful_check_status_response() {
        Assert.assertEquals(getRestHelper().getResponseHeaderValue(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse(), "X-Application-Context "), null, "Expects X-Application-Context header to not exists");
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()), 200, "Check Payment Status was not successful!");
    }


    @Then("^I should receive a transaction of for successful payment$")
    public void i_should_receive_a_transactionIdForSuccessfulPayment() {
        Response response = testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse();
        Assert.assertEquals(getRestHelper().getResponseHeaderValue(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse(), "X-Application-Context "), null, "Expects X-Application-Context header to not exists");
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()), 200, "Check Payment Status was not successful!");
        if (!PropertyHelper.getInstance().getPropertyCascading("version").equals("0.10")) {
            Assert.assertNotNull(response.path(Constants.TRANSACTIONS), "transactions should not be null");
            ArrayList returnedTransactions = testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse().path("transactions");
            testContext.getApiManager().getPaymentStatus().setTransactionId(returnedTransactions.get(0).toString());
        }
    }


    @Then("^I should receive a successful check status response for amount \"([^\"]*)\"$")
    public void i_should_receive_a_successful_check_status_responseForAmount(String amount) {
        logger.info("********** Retrieving Payment Request Status ***********");
        Assert.assertEquals(getRestHelper().getResponseHeaderValue(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse(), "X-Application-Context "), null, "Expects X-Application-Context header to not exists");
        if (amount.equalsIgnoreCase("1.81")) {
            Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()), 200, "Check Payment Status was not successful!");
        } else if (amount.equalsIgnoreCase("1.80")) {
            Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()), 200, "Check Payment Status was not successful!");
        } else if (amount.equalsIgnoreCase("1.45")) {
            Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()), 400, "Check Payment Status was not successful!");
        }
    }

    @Then("^the response body should contain valid payment request id, created timestamp, totalAmount, currencyCode, statusDescription, statusCode, effectiveDuration within check status response$")
    public void the_response_body_should_contain_valid_payment_id_created_timestamp_links_check_status() {
        Assert.assertNotNull(testContext.getApiManager().getPaymentStatus().paymentRequestIdInResponse(), "Payment Request Id is not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPaymentStatus().createdTimestampInResponse(), "Created Timestamp is not present in the response!!");

        Assert.assertEquals(testContext.getApiManager().getPaymentStatus().effectiveDurationInResponse().toString(),
                testContext.getApiManager().getPaymentRequest().getEffectiveDuration(), "Effective Duration does not match");

        Assert.assertNotNull(testContext.getApiManager().getPaymentStatus().statusCodeInResponse(), "Status Code is not present in the response!!");

        Assert.assertNotNull(testContext.getApiManager().getPaymentStatus().statusDescriptionInResponse(), "Status Description is not present in the response!!");

        Assert.assertEquals(String.format("%.2f", Double.parseDouble(testContext.getApiManager().getPaymentStatus().totalAmountInResponse())), String.format("%.2f", testContext.getApiManager().getPaymentRequest().getTotalAmountInDouble()), "Total Amount isn't matching!");

        Assert.assertEquals(testContext.getApiManager().getPaymentStatus().currencyCodeInResponse(), testContext.getApiManager().getPaymentRequest().getCurrency(), "Currency Code isn't matching!");
    }

    @Then("^the response body should also have app success callback URL, app fail Callback Url if applicable within check status response$")
    public void the_response_body_should_also_have_app_success_callback_app_fail_callback_uri_if_applicable_check_status() {

        if (testContext.getApiManager().getPaymentRequest().getAppSuccessCallback() == null) {
            Assert.assertNull(testContext.getApiManager().getPaymentStatus().appSuccessCallbackInResponse(), "App Success Call Back is present within the response when it should not be");
        } else {
            Assert.assertEquals(testContext.getApiManager().getPaymentStatus().appSuccessCallbackInResponse(), testContext.getApiManager().getPaymentRequest().getAppSuccessCallback(), "App Success Callback isn't matching!");
        }

        if (testContext.getApiManager().getPaymentRequest().getAppFailCallback() == null) {
            Assert.assertNull(testContext.getApiManager().getPaymentStatus().appFailCallbackInResponse(), "App Fail Call Back is present within the response when it should not be");
        } else {
            Assert.assertEquals(testContext.getApiManager().getPaymentStatus().appFailCallbackInResponse(), testContext.getApiManager().getPaymentRequest().getAppFailCallback(), "App Fail Callback isn't matching!");
        }
    }

    @Given("^I dont send Bearer with the auth token in the check status request$")
    public void i_dont_send_Bearer_with_the_auth_token_in_the_check_status_request() {
        testContext.getApiManager().getPaymentStatus().setAuthToken(testContext.getApiManager().getAccessToken().getAccessToken());
    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within check status response$")
    public void i_should_receive_a_error_response_with_error_description_and_errorcode_within_check_status_response(int responseCode, String errorDesc, String errorCode) {
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()), responseCode, "Different response code being returned");
        Assert.assertEquals(getRestHelper().getErrorCode(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()), errorCode, "Different error code being returned");
        Assert.assertTrue(getRestHelper().getErrorDescription(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()).contains(errorDesc), "Different error description being returned..Expected: " + errorDesc + "  Actual: " + getRestHelper().getErrorDescription(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()));

    }

    @Then("^error message should be \"([^\"]*)\" within check status response$")
    public void error_message_should_be_within_check_status_response(String errorMessage) {
        Assert.assertTrue(getRestHelper().getErrorMessage(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()).contains(errorMessage), "Different error message being returned..Expected: " + errorMessage + "  Actual: " + getRestHelper().getErrorMessage(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()));

    }

    @When("^I make a request for the payment status with \"([^\"]*)\" missing in the header$")
    public void i_make_a_request_for_the_payment_status_with_missing_in_the_header(String key) {
        testContext.getApiManager().getPaymentStatus().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getPaymentStatus().setRequestDateTime(getDateHelper().getUTCNowDateTime());

        testContext.getApiManager().getPaymentStatus().retrievePaymentStatusWithMissingHeaderKeys(getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"), key,
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
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
    }

    @Then("^the response body should contain correct \"([^\"]*)\" and \"([^\"]*)\"$")
    public void the_response_body_should_contain_correct_and(String statusDesc, String statusCode) {
        Assert.assertEquals(testContext.getApiManager().getPaymentStatus().statusDescriptionInResponse(), statusDesc, "Status Description is not correct!");
        Assert.assertEquals(testContext.getApiManager().getPaymentStatus().statusCodeInResponse(), statusCode, "Status Code is not correct!");
    }

    @When("^I make a request for the check status with invalid value for request date time \"([^\"]*)\"$")
    public void invalid_value_request_date_time(String value) {
        testContext.getApiManager().getPaymentStatus().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getPaymentStatus().setRequestDateTime(value);
        testContext.getApiManager().getPaymentStatus().retrievePaymentStatusInvalidDate(getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))),
                value);
    }


    @And("^the response body should contain a list of transactions$")
    public void theResponseBodyShouldContainListOfTransactions() {
        Response response = testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse();
        if (!PropertyHelper.getInstance().getPropertyCascading("version").equals("0.10")) {

            List<Transaction> listOfTransactions = response.path("transactions");
            System.out.println("listOfTransactions : " + listOfTransactions);
            Assert.assertNotNull(listOfTransactions, "transactions list cannot be null for success payment!");
            testContext.getApiManager().getPaymentStatus().setTransactions(listOfTransactions);
        } else {
            Assert.assertFalse(response.getBody().toString().contains(Constants.TRANSACTIONS), "transactions should not be present in version 0.10");
        }
    }

    @And("^validate payment status response for amount \"([^\"]*)\"$")
    public void validatePaymentStatusResponse(String amount) {
        Response response = testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse();
        if (amount.equalsIgnoreCase("1.81")) {
            Assert.assertEquals(response.path(Constants.PAYMENT_REQUEST_ID), testContext.getApiManager().getPaymentRequest().getPaymentRequestId(), "Payment Request id didn't match");
            Assert.assertEquals(response.path(Constants.STATUS_DESCRIPTION), "Payment Success", "status description is not expected");
            if (!PropertyHelper.getInstance().getPropertyCascading("version").equals("0.10")) {
                Assert.assertNotNull(response.path(Constants.TRANSACTIONS), "transactions should not be null");
            }
        } else if (amount.equalsIgnoreCase("1.80")) {
            Assert.assertEquals(response.path(Constants.PAYMENT_REQUEST_ID), testContext.getApiManager().getPaymentRequest().getPaymentRequestId(), "Payment Request id didn't match");
            Assert.assertEquals(response.path(Constants.STATUS_DESCRIPTION), "Payment Request Expired", "status description is not expected");
        } else if (amount.equalsIgnoreCase("1.45")) {
            Assert.assertEquals(getRestHelper().getResponseStatusCode(response), 400, "response code should be 400");
            if (getRestHelper().getErrorDescription(response) != null) {
                if (getRestHelper().getErrorDescription(response).contains("'")) {
                }
                org.testng.Assert.assertTrue(
                        getRestHelper().getErrorDescription(response)
                                .replace("\"", "")
                                .contains("Internal Server Error, contact support"),
                        "Different error description being returned..Expected: " + "Internal Server Error, contact support" + "Actual: " + getRestHelper().getErrorDescription(response));
            }
            Assert.assertEquals(getRestHelper().getErrorCode(response), "EB099", "Different error code being returned");
        }
    }

    @Then("^verify transaction list contains transactionId as retrieved in check status response$")
    public void verifyTransactionListContainsTransactionIdAsRetrievedInCheckStatusResponse() {
        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path("transactions");
        HashMap<String, String> firstTransaction = (HashMap) returnedTransactions.get(0);

        if (testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse().path(Constants.STATUS_DESCRIPTION).equals("Payment Success")) {
            latestFirstTransactionId = firstTransaction.get("transactionId");

            if (PropertyHelper.getInstance().getPropertyCascading("version").equals(0.12)) {
                List<HashMap> checkStatusTransactions = testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse().getBody().path(Constants.TRANSACTIONS);
                Object checkStatus_transactionId = checkStatusTransactions.get(0).get(Constants.TRANSACTION_ID);

                Assert.assertEquals(latestFirstTransactionId, checkStatus_transactionId, "TransactionId in Transactions list do not match with TransactionId in Check Status API Response!!");

            } else if (PropertyHelper.getInstance().getPropertyCascading("version").equals(0.11)) {
                Assert.assertEquals(latestFirstTransactionId, testContext.getApiManager().getPaymentStatus().getTransactions().get(0), "TransactionId in Transactions list do not match with TransactionId in Check Status API Response!!");
            }
            //Setting latest transaction Id in setters
            testContext.getApiManager().getTransaction().setTransactionId(latestFirstTransactionId);
        } else {
            Assert.assertFalse(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse().getBody().toString().contains(Constants.TRANSACTIONS), "TransactionId in Transactions list should not match with TransactionId in Check Status API Response as payment is not successful!!");
        }
    }

    @And("^validate the check status response body$")
    public void validateTheCheckStatusResponseBody() {
        Response response = testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse();
        String version = PropertyHelper.getInstance().getPropertyCascading("version");

        Assert.assertNotNull(testContext.getApiManager().getPaymentStatus().paymentRequestIdInResponse(), "Payment Request Id is not present in the response!!");
        Assert.assertEquals(response.path(Constants.PAYMENT_REQUEST_ID), testContext.getApiManager().getPaymentRequest().getPaymentRequestId(), "paymentRequestId doesn't match!");
        Assert.assertEquals(String.format("%.2f", Double.parseDouble(testContext.getApiManager().getPaymentStatus().totalAmountInResponse())), String.format("%.2f", testContext.getApiManager().getPaymentRequest().getTotalAmountInDouble()), "Total Amount isn't matching!");
        Assert.assertEquals(testContext.getApiManager().getPaymentStatus().currencyCodeInResponse(), testContext.getApiManager().getPaymentRequest().getCurrency(), "Currency Code isn't matching!");
        Assert.assertNotNull(testContext.getApiManager().getPaymentStatus().createdTimestampInResponse(), "Created Timestamp is not present in the response!!");

        if (testContext.getApiManager().getPaymentRequest().getEffectiveDuration().isEmpty()) {
            Assert.assertEquals(testContext.getApiManager().getPaymentRequest().effectiveDurationInResponse().toString(), "600", "effectiveDuration should be 600 seconds by default if no effectiveDuration is provided in request.");
            Assert.assertEquals(testContext.getApiManager().getPaymentStatus().effectiveDurationInResponse().toString(), "600", "effectiveDuration should be 600 seconds by default if no effectiveDuration is provided in request.");
        } else {
            Assert.assertEquals(testContext.getApiManager().getPaymentStatus().effectiveDurationInResponse().toString(),
                    testContext.getApiManager().getPaymentRequest().getEffectiveDuration(), "Effective Duration does not match");
        }
        Assert.assertNotNull(testContext.getApiManager().getPaymentStatus().statusDescriptionInResponse(), "Status Description is not present in the response!!");
        Assert.assertNotNull(testContext.getApiManager().getPaymentStatus().statusCodeInResponse(), "Status Code is not present in the response!!");

        if (response.path(Constants.STATUS_DESCRIPTION).equals("Payment Success")) {
            theResponseBodyShouldContainListOfTransactions();
            List<Map> transactions = response.path(Constants.TRANSACTIONS);

            if (version.equals("0.12")) {
                Assert.assertNotNull(transactions.get(0).get(Constants.TRANSACTION_ID), "transactionId should be present for successful payment!");
                Assert.assertNotNull(transactions.get(0).get(Constants.FEE_AMOUNT), "feeAmount should be present for successful payment!");
                Assert.assertNotNull(transactions.get(0).get(Constants.TRANSACTION_SOURCE), "transactionSource should be present for successful payment!");
                Assert.assertNotNull(transactions.get(0).get(Constants.TRANSACTION_SOURCE_DESCRIPTION), "transactionSourceDescription should be present for successful payment!");
                Assert.assertNotNull(transactions.get(0).get(Constants.TRANSACTION_TYPE), "transactionType should be present for successful payment!");
                Assert.assertNotNull(transactions.get(0).get(Constants.TRANSACTION_TYPE_DESCRIPTION), "transactionTypeDescription should be present for successful payment!");
                Assert.assertNotNull(transactions.get(0).get(Constants.ORDER_ID), "orderId should be present for successful payment!");
                Assert.assertNotNull(transactions.get(0).get(Constants.ORDER_DESCRIPTION), "orderDescription should be present for successful payment!");
                Assert.assertNotNull(transactions.get(0).get(Constants.TRANSACTION_TIME), "transactionTime should be present for successful payment!");
            } else if (version.equals("0.11")) {
                Assert.assertNotNull(transactions.toString(), "Only transactionId should be present in version 0.11 for successful payment!");
            }
        } else if (version.equals("0.10")) {
            Assert.assertFalse(response.toString().contains(Constants.TRANSACTIONS), "transactions list should not be present in version 0.10!");
        }
    }

    @Then("^I should receive \"([^\"]*)\" status code in check status response$")
    public void iShouldReceiveStatusCodeInCheckStatusResponse(int statusCode) throws Throwable {
        Response response = testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), statusCode, "Different statusCode being returned");
    }

    @And("^error message should be \"([^\"]*)\" within the check status response$")
    public void errorMessageShouldBeWithinTheCheckStatusResponse(String errorMessage) throws Throwable {
        Response response = testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse();
        Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned. \nExpected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(response));
    }


    @And("^get payment status response should return error description \"([^\"]*)\", errorCode \"([^\"]*)\" and response code \"([^\"]*)\"$")
    public void getPaymentResponseErrorForMagicNumber(String errorDescription, String errorCode, int httpStatus) {
        testContext.getApiManager().getPaymentStatus().retrievePaymentStatus(getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));
        Response response = testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()), httpStatus, "Different response code being returned");
        Assert.assertEquals(getRestHelper().getErrorCode(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()), errorCode, "Different error code being returned");
        Assert.assertTrue(getRestHelper().getErrorDescription(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()).contains(errorDescription), "Different error description being returned..Expected: " + errorCode + "  Actual: " + getRestHelper().getErrorDescription(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()));
    }

    @And("^get payment status response should return status description \"([^\"]*)\"$")
    public void getPaymentResponseShouldReturnStatus(String statusDescription) {
        testContext.getApiManager().getPaymentStatus().retrievePaymentStatus(getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_payment_request_resource"),
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));
        Response response = testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse();
        Assert.assertEquals(response.path(Constants.STATUS_DESCRIPTION), statusDescription, "Status description is not the same as expected");


    }
}
