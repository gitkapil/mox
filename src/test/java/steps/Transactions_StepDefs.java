package steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Transactions_StepDefs extends UtilManager {

    TestContext testContext;

    public Transactions_StepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    final static Logger logger = Logger.getLogger(Transactions_StepDefs.class);

    public String latestFirstTransactionId = "";

    public void queryTransactionListWithQueryStringParams(HashMap<String, String> queryStringParams) {
        testContext.getApiManager().getTransaction().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getTransaction().setRequestDateTime(getDateHelper().getUTCNowDateTime());

        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "get_transaction_resource");

        testContext.getApiManager().getTransaction().retrieveTransactionList(
                url,
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,"signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,"signing_key"),
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
        System.out.println("+++++++++++++++++++++++ Get Transaction List Response from Realisation +++++++++++++++++++++++");
        System.out.println();
        System.out.println(testContext.getApiManager().getTransaction().getTransactionListResponse().asString());
        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    @And("^I should have at least (\\d+) number of transactions returned$")
    public void iShouldHaveAtLeastMinimumNumberOfTransactionsReturned(Integer minimum) {
        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path("transactions");
        Assert.assertTrue(returnedTransactions.size() >= minimum);
    }

    @When("^I take the first transaction id and make a call with startingAfter between \"([^\"]*)\" and \"([^\"]*)\" with limit of (\\d+) transactions$")
    public void iTakeTheFirstTransactionIdAndMakeACallWithStartingAfterBetweenAndWithLimitOfLimitTransactions(String from, String to, Integer limit) throws Throwable {
        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path("transactions");
        HashMap<String, String> firstTransaction = (HashMap)returnedTransactions.get(0);
        latestFirstTransactionId = firstTransaction.get("transactionId");

        HashMap<String, String> queryStringParams = new HashMap<>();
        queryStringParams.put("startingAfter", latestFirstTransactionId);
        queryStringParams.put("from", from);
        queryStringParams.put("to", to);
        queryStringParams.put("limit", String.valueOf(limit));

        queryTransactionListWithQueryStringParams(queryStringParams);
    }

    @And("^the returned transactions list should not have the transaction id used in the request$")
    public void theReturnedTransactionsListShouldNotHaveTheTransactionIdUsedInTheRequest() {
        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path("transactions");
        Assert.assertFalse(returnedTransactions.stream().anyMatch(t ->
                ((HashMap)t).get("transactionId") instanceof String &&
                        ((HashMap)t).get("transactionId").toString()
                                .equals(latestFirstTransactionId)
                ));
    }

    @And("^I should have a maximum (\\d+) number of transactions returned$")
    public void iShouldHaveAMaximumLimitNumberOfTransactionsReturned(Integer maximum) {
        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path("transactions");
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
    public void iShouldReceiveAErrorResponseWithErrorDescriptionAndErrorcodeWithinTransactionResponse(String errDesc, String errCode) throws Throwable {
        ArrayList errors = testContext.getApiManager().getTransaction().getTransactionListResponse().path("errors");
        HashMap<String, String> error1 = (HashMap)errors.get(0);
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
        ArrayList returnedTransactions = testContext.getApiManager().getTransaction().getTransactionListResponse().path("transactions");
        Assert.assertTrue(returnedTransactions.size() == Integer.parseInt(actual));
    }
}
