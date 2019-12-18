package steps;

import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.Constants;

import java.util.*;

public class GetCredentials_StepDefs extends UtilManager {
    private static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final Set<String> APPLICATION_ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    final static Logger logger = Logger.getLogger(GetCredentials_StepDefs.class);

    TestContext testContext;
    ManagementCommon common;

    public GetCredentials_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }

    @Given("^I am an authorized DRAGON user$")
    public void i_am_an_authorized_DRAGON_user() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getCredentialsMerchants().setAuthTokenWithBearer(token));
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().postCredentialsMerchants().setAuthTokenWithBearer(token));
    }

    @When("^I hit get credentials endpoint without any filter$")
    public void iHitGetCredentialsEndpointWithoutAnyFilter() {
        logger.info("********** GET Credentials Request *********** \n");

        //Get applicationId from POST credentials API

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @Then("^I should receive successful get credential response$")
    public void iShouldReceiveSuccessfulGetCredentialResponse() {
        Assert.assertEquals(
                HttpStatus.SC_OK
                , getRestHelper().getResponseStatusCode(testContext.getApiManager().getCredentialsMerchants().getResponse()));
    }

    @And("^validate GET credentials response$")
    public void validateGETCredentialsResponse() {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();

        HashMap page = response.path(Constants.PAGE);

        Assert.assertNotNull("page_current should not be null!", page.get(Constants.CURRENT));
        Assert.assertNotNull("page_totalItems should not be null!", page.get(Constants.TOTAL_ITEMS));
        Assert.assertNotNull("page_size should not be null!", page.get(Constants.SIZE));

        List<HashMap> items = response.path(Constants.ITEM);

        for (int i = 0; i <= items.size() - 1; i++) {
            HashMap<Object, Object> signingKey = (HashMap) items.get(i).get(Constants.SIGNING_KEY);
            HashMap<Object, Object> secret = (HashMap) items.get(i).get(Constants.SECRET);

            System.out.println("secret.get(Constants.ID) : " + secret.get(Constants.ID));
            System.out.println("testContext.getApiManager().postCredentialsMerchants().getResponse().path(\"secret.Id\") : " + testContext.getApiManager().postCredentialsMerchants().getResponse().path("secret.Id"));

            Assert.assertNotNull("items_credentialId cannot be null!", items.get(i).get(Constants.CREDENTIAL_ID));
            Assert.assertEquals("credentialId of GET credentials API should be equal to credentialId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_ID), items.get(i).get(Constants.CREDENTIAL_ID));

            Assert.assertNotNull("items_credentialName cannot be null!", items.get(i).get(Constants.CREDENTIAL_NAME));
            Assert.assertEquals("credentialName of GET credentials API should be equal to credentialName of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getCredentialName(), items.get(i).get(Constants.CREDENTIAL_NAME));

//            Assert.assertNotNull("items_applicationId cannot be null!", items.get(i).get(Constants.APPLICATION_ID));
//            Assert.assertEquals("applicationId of GET credentials API should be equal to applicationId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.APPLICATION_ID), items.get(i).get(Constants.APPLICATION_ID));

            Assert.assertEquals("status of GET credentials API should be equal to status of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.STATUS), items.get(i).get(Constants.STATUS));
//            Assert.assertEquals("activateAt of GET credentials API should be equal to activateAt of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.ACTIVATE_AT), items.get(i).get(Constants.ACTIVATE_AT));
            //  Assert.assertEquals("expireAt of GET credentials API should be equal to expireAt of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.EXPIRE_AT), items.get(i).get(Constants.EXPIRE_AT));

//            Assert.assertNotNull("items_createdBy cannot be null!", items.get(i).get(Constants.CREATED_BY));
//            Assert.assertEquals("createdBy of GET credentials API should be equal to createdBy of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.CREATED_BY), items.get(i).get(Constants.CREATED_BY));
//
//            Assert.assertNotNull("items_lastUpdatedBy cannot be null!", items.get(i).get(Constants.LAST_UPDATED_BY));
//            Assert.assertEquals("lastUpdatedBy of GET credentials API should be equal to lastUpdatedBy of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.LAST_UPDATED_BY), items.get(i).get(Constants.LAST_UPDATED_BY));

            Assert.assertNotNull("items_createdAt cannot be null!", items.get(i).get(Constants.CREATED_AT));
            Assert.assertEquals("createdAt of GET credentials API should be equal to createdAt of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.CREATED_AT), items.get(i).get(Constants.CREATED_AT));

            Assert.assertNotNull("items_lastUpdatedAt cannot be null!", items.get(i).get(Constants.LAST_UPDATED_AT));
            Assert.assertEquals("lastUpdatedAt of GET credentials API should be equal to lastUpdatedAt of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.LAST_UPDATED_AT), items.get(i).get(Constants.LAST_UPDATED_AT));

            //validating signingKey details
            Assert.assertNotNull("signingKey_Id cannot not be null!", signingKey.get(Constants.ID));
            Assert.assertNotNull("signingKey_keyId cannot not be null!", signingKey.get(Constants.KEY_ID));
            Assert.assertEquals("signingKey_keyId of GET credentials API should be equal to signingKey_keyId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.keyId"), signingKey.get(Constants.KEY_ID));
            Assert.assertNotNull("signingKey_alg cannot not be null!", signingKey.get(Constants.ALG));
            Assert.assertNotNull("signingKey_type cannot not be null!", signingKey.get(Constants.TYPE));
            Assert.assertNotNull("signingKey_size cannot not be null!", signingKey.get(Constants.SIZE));

            //validating secret details
            Assert.assertNotNull("secret_Id cannot not be null!", secret.get(Constants.ID));
            Assert.assertNotNull("secret_clientId cannot not be null!", secret.get(Constants.CLIENT_ID));

            Assert.assertEquals("secret_Id of GET credentials API should be equal to secret_Id of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("secret.Id"), secret.get(Constants.ID));
            Assert.assertEquals("secret_clientId of GET credentials API should be equal to secret_clientId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("secret.clientId"), secret.get(Constants.CLIENT_ID));
        }
    }

    @And("^I hit get credentials endpoint with filter status as \"([^\"]*)\"$")
    public void iHitGetCredentialsEndpointWithFilterStatusAs(String filter_status) {
        logger.info("********** GET Credentials Request *********** \n");

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?status=" + filter_status;
        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @And("^I hit get credentials endpoint with filter credentialName as provided in POST credentials API request$")
    public void iHitGetCredentialsEndpointWithFilterCredentialNameAsProvidedInPOSTCredentialsAPIRequest() {
        logger.info("********** GET Credentials Request *********** \n");

        //credentialName from POST Credentials API
        String post_credentialName = testContext.getApiManager().postCredentialsMerchants().getCredentialName();

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?credentialName=" + post_credentialName;
        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @And("^I hit get credentials endpoint with filter credentialId retrieved in POST credentials API response$")
    public void iHitGetCredentialsEndpointWithFilterCredentialIdRetrievedInPOSTCredentialsAPIResponse() {
        logger.info("********** GET Credentials Request *********** \n");

        //credentialId from POST Credentials API
        String post_credentialId = testContext.getApiManager().postCredentialsMerchants().getResponse().getBody().path(Constants.CREDENTIAL_ID);

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?credentialId=" + post_credentialId;
        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @When("^I query for a list of credentials with filter limit as \"([^\"]*)\"$")
    public void iQueryForAListOfCredentialsWithFilterLimitAs(String filter_limit) {
        logger.info("********** GET Credentials Request *********** \n");

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?status=A&limit=" + filter_limit;
        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }


    @And("^I should receive \"([^\"]*)\" number of credentials against limit \"([^\"]*)\"$")
    public void iShouldReceiveNumberOfCredentialsAgainstLimit(String actual, int limit) {
        ArrayList returnedCredentialsCount = testContext.getApiManager().getCredentialsMerchants().getResponse().path(Constants.ITEM);
        System.out.println("returnedCredentialsCount : " + returnedCredentialsCount.size());

        //validate list of credentials in items
        if (limit == 31) {
            org.testng.Assert.assertTrue(returnedCredentialsCount.size() <= Integer.parseInt(actual) && returnedCredentialsCount.size() >= 1,
                    "Expected to have " + actual + " records, but received " + returnedCredentialsCount.size());
        } else {
            org.testng.Assert.assertTrue(returnedCredentialsCount.size() == Integer.parseInt(actual),
                    "Expected to have " + actual + " records, but received " + returnedCredentialsCount.size());
        }

        //validate size
        Assert.assertEquals("Expected size is " + actual + "\nSize in API response is " + testContext.getApiManager().getCredentialsMerchants().getResponse().path("page.size"), actual, testContext.getApiManager().getCredentialsMerchants().getResponse().path("page.size").toString());

    }

    @When("^I query for a list of credentials with filter page as \"([^\"]*)\"$")
    public void iQueryForAListOfCredentialsWithFilterPageAs(String filter_page) {
        logger.info("********** GET Credentials Request *********** \n");

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?status=A&page=" + filter_page;
        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @When("^I hit the post credentials endpoint five times with credential name \"([^\"]*)\"$")
    public void iHitThePostCredentialsEndpointFiveTimesWithCredentialName(String credentialName) {
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        System.out.println("url : " + url);
        int x = 1;
        while (x <= 5) {
            testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialName);
            testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().postCredentialsMerchants().getCredentialName());
            x++;
        }
    }

    @Given("^I am an unauthorized DRAGON user with invalid \"([^\"]*)\" auth token$")
    public void iAmAnUnauthorizedDRAGONUserWithInvalidAuthToken(String token) {
        common.iAmADragonUserWithToken(token, tokenArg -> testContext.getApiManager().getCredentialsMerchants().setAuthToken(tokenArg));
    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within get credentials response$")
    public void iShouldReceiveAErrorResponseWithErrorDescriptionAndErrorcodeWithinGetCredentialsResponse(int responseCode, String errorDesc, String errorCode) {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();

        org.testng.Assert.assertEquals(getRestHelper().getResponseStatusCode(response), responseCode, "Different response code being returned");
        org.testng.Assert.assertEquals(getRestHelper().getErrorCode(response), errorCode, "Different error code being returned");
        org.testng.Assert.assertTrue(getRestHelper().getErrorDescription(response).contains(errorDesc), "Different error description being returned.\nExpected: " + errorDesc + "\nActual: " + getRestHelper().getErrorDescription(testContext.getApiManager().postCredentialsMerchants().getResponse()));
    }

    @And("^error message should be \"([^\"]*)\" within get credentials response$")
    public void errorMessageShouldBeWithinGetCredentialsResponse(String errorMessage) throws Throwable {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();
        org.testng.Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned.\nExpected: " + errorMessage + "\nActual: " +
                        getRestHelper().getErrorMessage(response));
    }

    @When("^I hit get credentials endpoint for invalid header \"([^\"]*)\" with value \"([^\"]*)\"$")
    public void iHitGetCredentialsEndpointForInvalidHeaderWithValue(String key, String headerValue) {

        logger.info("********** GET Credentials Request *********** \n");

        //Get applicationId from POST credentials API

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/2e29d12b-aa89-4787-9a4a-da98da949306/credentials";
        testContext.getApiManager().getCredentialsMerchants().makeRequestWithInvalidHeaders(url, key, headerValue);
    }

    @Then("^I should receive status code \"([^\"]*)\" and message \"([^\"]*)\" in get credentials response$")
    public void iShouldReceiveStatusCodeAndMessageInGetCredentialsResponse(int statusCode, String errorMessage) {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();
        org.testng.Assert.assertEquals(getRestHelper().getResponseStatusCode(response), statusCode, "Expected Response Code: " + statusCode + "\nActual: " + response.getStatusCode());
        org.testng.Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned.\nExpected: " + errorMessage + "\nActual: " +
                        getRestHelper().getErrorMessage(response));

    }

    @When("^I make request to get credentials endpoint with invalid applicationId \"([^\"]*)\"$")
    public void iMakeRequestToGetCredentialsEndpointWithInvalidApplicationId(String applicationId) {
        logger.info("********** GET Credentials Request *********** \n");

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + applicationId + "/credentials";
        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }
}
