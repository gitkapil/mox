package steps;

import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.testng.Assert;
import utils.Constants;
import utils.PropertyHelper;

import java.util.*;


public class Transactions_StepDefs extends UtilManager {

    TestContext testContext;

    public Transactions_StepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    final static Logger logger = Logger.getLogger(Transactions_StepDefs.class);

    public String latestFirstTransactionId = "";
    public Response response = null;

    public void queryTransactionListWithQueryStringParams(HashMap<String, String> queryStringParams) {
        testContext.getApiManager().getTransaction().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getTransaction().setRequestDateTime(getDateHelper().getUTCNowDateTime());

        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "get_transaction_resource");

        testContext.getApiManager().getTransaction().retrieveTransactionList(
                url,
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))),
                queryStringParams);
    }

    @When("^I query for a list of transactions between \"([^\"]*)\" and \"([^\"]*)\" with \"([^\"]*)\"$")
    public void i_query_for_a_list_of_transactions_between_with(String from, String to, String limit) {
        HashMap<String, String> queryStringParams = new HashMap<>();
        queryStringParams.put("from", from);
        queryStringParams.put("to", to);
        queryStringParams.put("limit", String.valueOf(limit));

        queryTransactionListWithQueryStringParams(queryStringParams);
    }

    @Then("^I should receive a successful transaction response$")
    public void iShouldReceiveASuccessfulTransactionResponse() {
        logger.info("********** Retrieving Transaction List ***********");
        Assert.assertEquals(getRestHelper().getResponseHeaderValue(
                testContext.getApiManager().getTransaction().getTransactionListResponse(),
                "X-Application-Context "),
                null, "Expects X-Application-Context header to not exists");
        Assert.assertEquals(getRestHelper().getResponseStatusCode(
                testContext.getApiManager().getTransaction().getTransactionListResponse()),
                200,
                "Get Transaction List was not successful!");
        logger.info("****** Get Transaction List Response from Realisation *******");
        logger.info(testContext.getApiManager().getTransaction().getTransactionListResponse().prettyPrint());

        transactionObjectIsASubSet();
        transactionSourceDescriptionIsConvertedProperly();
        transactionTypeDescriptionIsConvertedProperly();
        statusCodeIsConvertedProperly();
    }

    @Then("^I should receive a successful transaction list response after payment done$")
    public void iShouldReceiveASuccessfulTransactionList() {

        logger.info("**********Transaction List ***********");

        Assert.assertEquals(getRestHelper().getResponseHeaderValue(testContext.getApiManager().getTransaction().getTransactionListResponse(), "X-Application-Context "), null, "Expects X-Application-Context header to not exists");

        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getTransaction().getTransactionListResponse()), 200, "Get Transaction List was not successful!");

        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path(Constants.TRANSACTIONS);

        Response response = testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse();

        HashMap<String, String> firstTransaction = (HashMap) returnedTransactions.get(0);

        if (!PropertyHelper.getInstance().getPropertyCascading("version").equals("0.10")) {
            if (PropertyHelper.getInstance().getPropertyCascading("version").equals("0.11")) {
                Assert.assertEquals(firstTransaction.get(Constants.TRANSACTION_ID), testContext.getApiManager().getPaymentStatus().getTransactions().get(0), "Transaction Id is different ");
            } else if (PropertyHelper.getInstance().getPropertyCascading("version").equals("0.12")) {
                System.out.println("firstTransaction.get(Constants.TRANSACTION_ID) : " + firstTransaction.get(Constants.TRANSACTION_ID));

                if (response.path(Constants.STATUS_DESCRIPTION).equals("Payment Success")) {
                    List<Map> transactions = response.path(Constants.TRANSACTIONS);
                    System.out.println("checkstatus_transactions.get(0).get(Constants.TRANSACTION_ID) : " + transactions.get(0).get(Constants.TRANSACTION_ID));
                    String checkstatus_transactionId = transactions.get(0).get(Constants.TRANSACTION_ID).toString();
                    Assert.assertEquals(firstTransaction.get(Constants.TRANSACTION_ID), checkstatus_transactionId, "Transaction Id is different ");
                }
            }
            Assert.assertEquals(testContext.getApiManager().getPaymentRequest().getOrderId(), firstTransaction.get(Constants.ORDER_ID));

            Assert.assertEquals(testContext.getApiManager().getPaymentRequest().getOrderDescription(), firstTransaction.get(Constants.ORDER_DESCRIPTION));

            Assert.assertEquals(firstTransaction.get(Constants.TRANSACTION_TYPE), "003", "transaction type for refund should be 004");

            Assert.assertEquals(firstTransaction.get(Constants.TRANSACTION_TYPE_DESCRIPTION), "Collect payment", "transaction type for refund should be 004");

            Assert.assertEquals(firstTransaction.get(Constants.TRANSACTION_SOURCE), "001", "transaction source for POS should be 001");

            Assert.assertEquals(testContext.getApiManager().getPaymentRequest().getPayMeMemberId(), firstTransaction.get(Constants.PAYER_ID));

            Assert.assertEquals(testContext.getApiManager().getPaymentRequest().getPayerName(), firstTransaction.get(Constants.PAYER_NAME));

            Assert.assertEquals(firstTransaction.get(Constants.TRANSACTION_SOURCE_DESCRIPTION), "POS", "transaction description for POS should be POS");

            Assert.assertEquals(testContext.getApiManager().getPaymentRequest().getTotalAmount(), firstTransaction.get(Constants.TRANSACTION_AMOUNT), "transaction is not same as requested in payment");


        }
    }


    @Then("^I should receive a successful transaction list response after payment refund$")
    public void iShouldReceiveASuccessfulTransactionAfterPostRefund() {

        logger.info("**********Transaction List ***********");

        Assert.assertEquals(getRestHelper().getResponseHeaderValue(testContext.getApiManager().getTransaction().getTransactionListResponse(), "X-Application-Context "), null, "Expects X-Application-Context header to not exists");

        Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getTransaction().getTransactionListResponse()), 200, "Get Transaction List was not successful!");

        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path(Constants.TRANSACTIONS);

        HashMap<String, String> firstTransaction = (HashMap) returnedTransactions.get(0);
        if (!PropertyHelper.getInstance().getPropertyCascading("version").equals("0.10")) {
            Assert.assertEquals(firstTransaction.get(Constants.TRANSACTION_ID), testContext.getApiManager().getRefunds().getRefundId(), "refundId is not transactionId");

            Assert.assertEquals(testContext.getApiManager().getRefunds().getRefundsResponse().path(Constants.REASON_MESSAGE), firstTransaction.get(Constants.ORDER_ID), "OrderId in Transaction response should be equal to reasonMessage in Refund response");

            Assert.assertEquals(firstTransaction.get(Constants.TRANSACTION_TYPE), "004", "transaction type for refund should be 004");

            Assert.assertEquals(firstTransaction.get(Constants.TRANSACTION_TYPE_DESCRIPTION), "Refund", "transaction type for refund should be 004");

            Assert.assertEquals(firstTransaction.get(Constants.TRANSACTION_SOURCE), "001", "transaction source for POS should be 001");

            Assert.assertEquals(testContext.getApiManager().getPaymentRequest().getPayMeMemberId(), firstTransaction.get(Constants.PAYER_ID));

            Assert.assertEquals(testContext.getApiManager().getPaymentRequest().getPayerName(), firstTransaction.get(Constants.PAYER_NAME));

            Assert.assertEquals(firstTransaction.get(Constants.TRANSACTION_SOURCE_DESCRIPTION), "POS", "transaction description for POS should be POS");

            Assert.assertEquals(testContext.getApiManager().getRefunds().getAmount(), firstTransaction.get(Constants.TRANSACTION_AMOUNT), "transaction is not same as requested in payment");

            Assert.assertEquals(testContext.getApiManager().getRefunds().getRefundId(), firstTransaction.get(Constants.TRANSACTION_ID), "transaction is not same as requested in payment");

        }
    }

    @And("^I should have at least (\\d+) number of transactions returned$")
    public void iShouldHaveAtLeastMinimumNumberOfTransactionsReturned(Integer minimum) {
        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path(Constants.TRANSACTIONS);
        Assert.assertTrue(returnedTransactions.size() >= minimum);
    }

    @When("^I take the first transaction id and make a call with startingAfter between \"([^\"]*)\" and \"([^\"]*)\" with limit of (\\d+) transactions$")
    public void iTakeTheFirstTransactionIdAndMakeACallWithStartingAfterBetweenAndWithLimitOfLimitTransactions
            (String from, String to, Integer limit) throws Throwable {
        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path(Constants.TRANSACTIONS);
        HashMap<String, String> firstTransaction = (HashMap) returnedTransactions.get(0);
        latestFirstTransactionId = firstTransaction.get(Constants.TRANSACTION_ID);

        HashMap<String, String> queryStringParams = new HashMap<>();
        queryStringParams.put("startingAfter", latestFirstTransactionId);
        queryStringParams.put("from", from);
        queryStringParams.put("to", to);
        queryStringParams.put("limit", String.valueOf(limit));
        queryTransactionListWithQueryStringParams(queryStringParams);
    }

    public void transactionSourceDescriptionIsConvertedProperly() {
        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path(Constants.TRANSACTIONS);
        returnedTransactions.stream().forEach(t -> {
            String sourceDescription = (String) ((HashMap) t).get(Constants.TRANSACTION_SOURCE_DESCRIPTION);
            //should be Online/null/POS
            if (sourceDescription == null || sourceDescription.contentEquals("Online") || sourceDescription.contentEquals("POS") || sourceDescription.length() == 0) {
                //right format
            } else {
                Assert.assertEquals(true, false, "TransactionSourceDescription has an invalid value (" + sourceDescription + ")");
            }
        });
    }

    public void transactionTypeDescriptionIsConvertedProperly() {
        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path(Constants.TRANSACTIONS);
        returnedTransactions.stream().forEach(t -> {
            String typeDescription = (String) ((HashMap) t).get(Constants.TRANSACTION_TYPE_DESCRIPTION);
            //should be Online or null
            if (typeDescription == null || typeDescription.contentEquals("Collect payment") ||
                    typeDescription.contentEquals("Refund") || typeDescription.contentEquals("Bank Transfer") ||
                    typeDescription.contentEquals("Adjustment") || typeDescription.length() == 0) {
                //right format
            } else {
                Assert.assertEquals(true, false, "transactionTypeDescription has an invalid value (" + typeDescription + ")");
            }
        });
    }

    public void statusCodeIsConvertedProperly() {
        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path(Constants.TRANSACTIONS);
        returnedTransactions.stream().forEach(t -> {
            String statusCode = (String) ((HashMap) t).get(Constants.STATUS_CODE);
            //should be Online or null
            if (statusCode == null || statusCode.contentEquals("001") || statusCode.contentEquals("002")
                    || statusCode.contentEquals("003")) {
                //right format
            } else {
                Assert.assertEquals(true, false, "statusCode has an invalid value (" + statusCode + ")");
            }
        });
    }

    public void transactionObjectIsASubSet() {
        String[] predefinedSet = {
                Constants.TRANSACTION_ID,
                Constants.PAYER_ID,
                Constants.TRANSACTION_SOURCE,
                Constants.TRANSACTION_SOURCE_DESCRIPTION,
                Constants.TRANSACTION_TYPE,
                Constants.TRANSACTION_TYPE_DESCRIPTION,
                Constants.TRANSACTION_TIME,
                Constants.TRANSACTION_AMOUNT,
                Constants.TRANSACTION_CURRENCY_CODE,
                Constants.FEE_AMOUNT,
                Constants.FEE_CURRENCY_CODE,
                Constants.STATUS_CODE,
                Constants.STATUS_DESCRIPTION,
                Constants.PAYER_NAME,
                Constants.ORDER_DESCRIPTION,
                Constants.ORDER_ID,
                Constants.MESSAGE,
                Constants.REFERENCE,
                Constants.REFUNDABLE,
                Constants.REASON_CODE
        };

        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path(Constants.TRANSACTIONS);
        returnedTransactions.parallelStream().forEach(t -> {
            Set<String> keySet = ((HashMap) t).keySet();
            Collection<String> diff = CollectionUtils.disjunction(Arrays.asList(predefinedSet), keySet);
            if (diff.size() != 0) {
                if (!diff.contains(Constants.TRANSACTION_SOURCE_DESCRIPTION) && !diff.contains(Constants.TRANSACTION_TYPE_DESCRIPTION) &&
                        !diff.contains(Constants.MESSAGE) && !diff.contains(Constants.REASON_CODE)) {
                    Assert.assertEquals(true, false,
                            "Returned transaction object contain fields that are not a subset (" +
                                    String.join(",", diff) + ")");
                }
            }
        });
    }


    @And("^the returned transactions list should not have the transaction id used in the request$")
    public void theReturnedTransactionsListShouldNotHaveTheTransactionIdUsedInTheRequest() {
        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path(Constants.TRANSACTIONS);
        Assert.assertFalse(returnedTransactions.stream().anyMatch(t ->
                ((HashMap) t).get(Constants.TRANSACTION_ID) instanceof String &&
                        ((HashMap) t).get(Constants.TRANSACTION_ID).toString()
                                .equals(latestFirstTransactionId)
        ));
    }

    @And("^I should have a maximum (\\d+) number of transactions returned$")
    public void iShouldHaveAMaximumLimitNumberOfTransactionsReturned(Integer maximum) {
        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path(Constants.TRANSACTIONS);
        Assert.assertTrue(returnedTransactions.size() <= maximum);
    }

    @When("^I query for a list of transactions between \"([^\"]*)\" and \"([^\"]*)\"$")
    public void iQueryForAListOfTransactionsBetweenAnd(String from, String to) throws Throwable {
        HashMap<String, String> queryStringParams = new HashMap<>();
        queryStringParams.put("from", from);
        queryStringParams.put("to", to);
        queryTransactionListWithQueryStringParams(queryStringParams);
    }

    @When("^I query for a list of transactions starting after \"([^\"]*)\"$")
    public void iQueryForAListOfTransactionsStartingAfter(String startingAfter) throws Throwable {
        HashMap<String, String> queryStringParams = new HashMap<>();
        queryStringParams.put("startingAfter", startingAfter);
        queryTransactionListWithQueryStringParams(queryStringParams);
    }

    @Then("^I should receive a error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within transaction response$")
    public void iShouldReceiveAErrorResponseWithErrorDescriptionAndErrorcodeWithinTransactionResponse(String
                                                                                                              errDesc, String errCode) throws Throwable {
        ArrayList errors = testContext.getApiManager().getTransaction().getTransactionListResponse().path("errors");
        HashMap<String, String> error1 = (HashMap) errors.get(0);
        Assert.assertEquals(error1.get("errorCode"), errCode);
        Assert.assertTrue(error1.get("errorDescription").indexOf(errDesc) > -1);
    }

    @When("^I query for a list of transactions with \"([^\"]*)\"$")
    public void iQueryForAListOfTransactionsWithLimit(String limit) {
        HashMap<String, String> queryStringParams = new HashMap<>();
        queryStringParams.put("limit", String.valueOf(limit));
        queryTransactionListWithQueryStringParams(queryStringParams);
    }

    @Then("^I should receive \"([^\"]*)\" number of transactions$")
    public void iShouldReceiveNumberOfTransactions(String actual) {
        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path(Constants.TRANSACTIONS);
        Assert.assertTrue(returnedTransactions.size() == Integer.parseInt(actual),
                "Expected to have " + actual + " records, but got " + returnedTransactions.size());
    }

    @When("^I query for a list of transactions$")
    public void iQueryForAListOfTransactions() {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "get_transaction_resource");
        testContext.getApiManager().getTransaction().retrieveTransactionListWithoutQueryParam(
                url,
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));
    }


    //GET Transaction ID code

    @When("^I query for a list of transactions with \"([^\"]*)\" transactionId$")
    public void iQueryForAListOfTransactionsWithTransactionId(String transactionId) {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "get_transaction_resource");
        String transactionId_url = url + "/" + transactionId;

        testContext.getApiManager().getTransaction().retrieveTransactionIdResponseWithoutQueryParam(
                transactionId_url,
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));

    }


    @When("^I make request to transactionID endpoint with \"([^\"]*)\" missing in the header$")
    public void iMakeRequestToTransactionIDEndpointWithMissingInTheHeader(String key) {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "get_transaction_resource");
        String transactionId_url = url + "/" + testContext.getApiManager().getTransaction().getTransactionId();
        testContext.getApiManager().getTransaction().executeRequestWithMissingHeaderKeys(
                transactionId_url,
                key,
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));

    }

    @When("^validate the transaction details$")
    public void validateTheTransactionResponse() throws Throwable {
        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path(Constants.TRANSACTIONS);
        HashMap<String, String> firstTransaction = (HashMap) returnedTransactions.get(0);
        latestFirstTransactionId = firstTransaction.get(Constants.TRANSACTION_ID);
    }


    @When("^I query for transaction details with transactionId retrieved in check status response$")
    public void iQueryForTransactionDetailsWithTransactionIdRetrievedInCheckStatusResponse() {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "get_transaction_resource");
        String transactionId_url = url + "/" + testContext.getApiManager().getTransaction().getTransactionId();

        testContext.getApiManager().getTransaction().retrieveTransactionIdResponseWithoutQueryParam(
                transactionId_url,
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));
    }

    @Then("^I should receive a \"([^\"]*)\" status code in transactionID API response$")
    public void iShouldReceiveAStatusCodeInTransactionIDAPIResponse(int statusCode) {
        Assert.assertEquals(testContext.getApiManager().getTransaction().transactionIdResponse.getStatusCode(), statusCode, "Different statusCode being returned");
    }

    @And("^error message should be \"([^\"]*)\" within transactionID API response$")
    public void errorMessageShouldBeWithinTransactionIDAPIResponse(String errorMessage) {
        Response response = testContext.getApiManager().getTransaction().transactionIdResponse;
        Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned.\nExpected: " + errorMessage + "\nActual: " +
                        getRestHelper().getErrorMessage(response));
    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode in transactionID API response$")
    public void iShouldReceiveAErrorResponseWithErrorDescriptionAndErrorcodeInTransactionIDAPIResponse(
            int statusCode, String errorDesc, String errorCode) throws Throwable {
        Response response = testContext.getApiManager().getTransaction().transactionIdResponse;

        Assert.assertEquals(testContext.getApiManager().getTransaction().transactionIdResponse.getStatusCode(), statusCode, "Different statusCode being returned");
        Assert.assertEquals(getRestHelper().getErrorCode(response), errorCode, "Different error code being returned");
        Assert.assertTrue(getRestHelper().getErrorDescription(response).contains(errorDesc), "Different error description being returned.\nExpected: " + errorDesc + "\nActual: " + getRestHelper().getErrorDescription(testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse()));
    }

    @When("^I make request to transactionID endpoint with invalid key \"([^\"]*)\" for \"([^\"]*)\" in header$")
    public void iMakeRequestToTransactionIDEndpointWithInvalidKeyForInHeader(String key, String invalidValue) throws
            Throwable {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "get_transaction_resource");
        String transactionId_url = url + "/" + testContext.getApiManager().getTransaction().getTransactionId();
        System.out.println("transactionId_url : " + transactionId_url);
        testContext.getApiManager().getTransaction().executeRequestWithInvalidHeaderKeys(
                transactionId_url,
                invalidValue,
                key,
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))));
    }

    @Given("^I am a user with invalid \"([^\"]*)\" auth token$")
    public void iAmAUserWithInvalidAuthToken(String token) throws Throwable {
        testContext.getApiManager().getTransaction().setAuthToken(token);
        testContext.getApiManager().getTransaction().setAuthTokenwithBearer();
        if (testContext.getApiManager().getAccessToken().getType().equalsIgnoreCase("merchant")) {
            getRestHelper().setBaseURI(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-api-management-url")
                    + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_APIs"));
        } else {
            getRestHelper().setBaseURI(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "sandbox-api-management-url")
                    + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_APIs"));
        }
    }

    @Given("^I am a user with invalid \"([^\"]*)\" auth token without Bearer$")
    public void iAmAUserWithInvalidAuthTokenWithoutBearer(String token) throws Throwable {
        testContext.getApiManager().getTransaction().setAuthToken(token);
        testContext.getApiManager().getTransaction().setAuthTokenWithoutBearer();
        if (testContext.getApiManager().getAccessToken().getType().equalsIgnoreCase("merchant")) {
            getRestHelper().setBaseURI(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-api-management-url")
                    + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_APIs"));
        } else {
            getRestHelper().setBaseURI(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "sandbox-api-management-url")
                    + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_APIs"));
        }
    }

    @Then("^I should receive a successful transactionID response$")
    public void iShouldReceiveASuccessfulTransactionIDResponse() {
        response = testContext.getApiManager().getTransaction().transactionIdResponse;
        Assert.assertEquals(testContext.getApiManager().getTransaction().transactionIdResponse.getStatusCode(), 200, "Different statusCode being returned");
    }

    @And("^validate GET transaction by ID response$")
    public void validateGETTransactionByIDResponse() {
        response = testContext.getApiManager().getTransaction().transactionIdResponse;
        ArrayList transactionDetailsList = response.path(Constants.TRANSACTIONS);
        ArrayList checkStatus = testContext.getApiManager().getPaymentStatus().getPaymentStatusResponse().path(Constants.TRANSACTIONS);
        HashMap checkStatus_transactions;

        for (int i = 0; i <= transactionDetailsList.size() - 1; i++) {

            HashMap<String, String> t = (HashMap) transactionDetailsList.get(i);

            Assert.assertNotNull(t.get(Constants.TRANSACTION_ID), "transactionId cannot be null");

            if (t.get(Constants.TRANSACTION_TYPE).equals("003")) {
                checkStatus_transactions = (HashMap) checkStatus.get(0);
                Assert.assertEquals(t.get(Constants.TRANSACTION_ID), checkStatus_transactions.get(Constants.TRANSACTION_ID), "transactionId didn't match! \nExpected: " + testContext.getApiManager().getPaymentStatus().getTransactionId() + "\nActual: " + t.get(Constants.TRANSACTION_ID));
                if (testContext.getApiManager().getPaymentRequest().getPaymentRequestBody().containsKey(Constants.ORDER_ID)) {
                    Assert.assertNotNull(t.get(Constants.ORDER_ID), "orderId cannot be null");
                }
                Assert.assertFalse(t.containsKey(Constants.REASON_CODE), "reasonCode should be present only for Refund transactions!");

                checkStatus_transactions = (HashMap) checkStatus.get(0);
                Object checkStatus_transactionTime = checkStatus_transactions.get(Constants.TRANSACTION_TIME);
                Assert.assertEquals(t.get(Constants.TRANSACTION_TIME), checkStatus_transactionTime, "transactionTime should be equal to transactionTime of check status API response!");

            } else if (t.get(Constants.TRANSACTION_TYPE).equals("004")) {
                Assert.assertEquals(t.get(Constants.TRANSACTION_ID), testContext.getApiManager().getRefunds().getRefundId(), "transactionId didn't match! \nExpected: " + testContext.getApiManager().getRefunds().getRefundId() + "\nActual: " + t.get(Constants.TRANSACTION_ID));
                Assert.assertNotNull(t.get(Constants.REASON_CODE), "reasonCode cannot be null");
            }
            Assert.assertNotNull(t.get(Constants.PAYER_ID), "payerId cannot be null");
            Assert.assertNotNull(t.get(Constants.TRANSACTION_SOURCE), "transactionSource cannot be null");
            Assert.assertNotNull(t.get(Constants.TRANSACTION_SOURCE_DESCRIPTION), "transactionSourceDescription cannot be null");
            Assert.assertNotNull(t.get(Constants.TRANSACTION_TYPE), "transactionType cannot be null");
            Assert.assertNotNull(t.get(Constants.TRANSACTION_TYPE_DESCRIPTION), "transactionTypeDescription cannot be null");
            Assert.assertNotNull(t.get(Constants.TRANSACTION_TIME), "transactionTime cannot be null");
            Assert.assertNotNull(t.get(Constants.TRANSACTION_AMOUNT), "transactionAmount cannot be null");
            Assert.assertNotNull(t.get(Constants.TRANSACTION_CURRENCY_CODE), "transactionCurrencyCode cannot be null");
            Assert.assertNotNull(t.get(Constants.FEE_AMOUNT), "feeAmount cannot be null");
            Assert.assertNotNull(t.get(Constants.FEE_CURRENCY_CODE), "feeCurrencyCode cannot be null");
            Assert.assertNotNull(t.get(Constants.STATUS_CODE), "statusCode cannot be null");
            Assert.assertNotNull(t.get(Constants.STATUS_DESCRIPTION), "statusDescription cannot be null");
            Assert.assertNotNull(t.get(Constants.PAYER_NAME), "payerName cannot be null");
            Assert.assertNotNull(t.get(Constants.ORDER_DESCRIPTION), "orderDescription cannot be null");
            Assert.assertNotNull(t.get(Constants.REFUNDABLE), "refundable cannot be null");
        }
        Assert.assertNotNull(response.path(Constants.LIST_DATE), "listDate cannot be null");
    }

    @When("^I make a request for GET Transactions by ID API with invalid value \"([^\"]*)\" for request date time$")
    public void iMakeARequestForGETTransactionsByIDAPIWithInvalidValueForRequestDateTime(String value) throws
            Throwable {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "get_transaction_resource");
        String transactionId_url = url + "/" + testContext.getApiManager().getTransaction().getTransactionId();
        System.out.println("transactionId_url : " + transactionId_url);
        testContext.getApiManager().getTransaction().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getTransaction().setRequestDateTime(value);
        testContext.getApiManager().getTransaction().retrieveTransactionInvalidDate(transactionId_url,
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))),
                value);
    }

    @And("^validate GET transaction list populates in ascending order$")
    public void validateGETTransactionListPopulatesInAscendingOrder() {
        response = testContext.getApiManager().getTransaction().transactionIdResponse;
        ArrayList transactionDetailsList = response.path(Constants.TRANSACTIONS);
        ArrayList<DateTime> time = new ArrayList();
        for (int i = 0; i <= transactionDetailsList.size() - 1; i++) {
            HashMap t = (HashMap) transactionDetailsList.get(i);
            String getDateTime = t.get(Constants.TRANSACTION_TIME).toString().substring(0, 19);
            DateTime date = DateTime.parse(getDateTime);
            time.add(i, date);
        }
        DateTime time1 = time.get(0);
        DateTime time2 = time.get(1);

        Assert.assertTrue(time1.isBefore(time2), "First transaction record should be displayed first.");
    }
}
