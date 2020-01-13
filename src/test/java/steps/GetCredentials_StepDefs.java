package steps;

import com.google.common.collect.Sets;
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
import org.junit.Assert;
import utils.Constants;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GetCredentials_StepDefs extends UtilManager {
    private static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final Set<String> APPLICATION_ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    final static Logger logger = Logger.getLogger(GetCredentials_StepDefs.class);
    public List<String> post_response;

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
    public void iHitGetCredentialsEndpointWithoutAnyFilter() throws InterruptedException {
        logger.info("********** GET Credentials Request *********** \n");

        Thread.sleep(1000);
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
        Assert.assertNotEquals("page_totalItems should not be zero!", 0, page.get(Constants.TOTAL_ITEMS));
        Assert.assertNotNull("page_size should not be null!", page.get(Constants.SIZE));

        List<HashMap> items = response.path(Constants.ITEM);

        for (int i = 0; i <= items.size() - 1; i++) {
            HashMap<Object, Object> signingKey = (HashMap) items.get(i).get(Constants.SIGNING_KEY);
            HashMap<Object, Object> secret = (HashMap) items.get(i).get(Constants.SECRET);

            Assert.assertNotNull("items_credentialId cannot be null!", items.get(i).get(Constants.CREDENTIAL_ID));
            Assert.assertNotNull("items_credentialName cannot be null!", items.get(i).get(Constants.CREDENTIAL_NAME));
            Assert.assertNotNull("items_applicationId cannot be null!", items.get(i).get(Constants.APPLICATION_ID));
            Assert.assertNotNull("items_createdBy cannot be null!", items.get(i).get(Constants.CREATED_BY));
            Assert.assertNotNull("items_lastUpdatedBy cannot be null!", items.get(i).get(Constants.LAST_UPDATED_BY));
            Assert.assertNotNull("items_createdAt cannot be null!", items.get(i).get(Constants.CREATED_AT));
            Assert.assertNotNull("items_lastUpdatedAt cannot be null!", items.get(i).get(Constants.LAST_UPDATED_AT));
            Assert.assertNotNull("signingKey_Id cannot not be null!", signingKey.get(Constants.ID));
            Assert.assertNotNull("signingKey_keyId cannot not be null!", signingKey.get(Constants.KEY_ID));
            Assert.assertNotNull("signingKey_alg cannot not be null!", signingKey.get(Constants.ALG));
            Assert.assertNotNull("signingKey_type cannot not be null!", signingKey.get(Constants.TYPE));
            Assert.assertNotNull("signingKey_size cannot not be null!", signingKey.get(Constants.SIZE));
            Assert.assertNotNull("secret_Id cannot not be null!", secret.get(Constants.ID));
            Assert.assertNotNull("secret_clientId cannot not be null!", secret.get(Constants.CLIENT_ID));

            //Validating latest record created by POST credentials API

            if (i == 0) {

                Assert.assertEquals("credentialId of GET credentials API should be equal to credentialId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_ID), items.get(i).get(Constants.CREDENTIAL_ID));
                Assert.assertEquals("credentialName of GET credentials API should be equal to credentialName of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getCredentialName(), items.get(i).get(Constants.CREDENTIAL_NAME));
                Assert.assertEquals("applicationId of GET credentials API should be equal to applicationId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.APPLICATION_ID), items.get(i).get(Constants.APPLICATION_ID));
                Assert.assertEquals("status of GET credentials API should be equal to status of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.STATUS), items.get(i).get(Constants.STATUS));
//BUG raised DRAG-2433
                Assert.assertEquals("activateAt of GET credentials API should be equal to activateAt of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.ACTIVATE_AT), items.get(i).get(Constants.ACTIVATE_AT));
                Assert.assertEquals("expireAt of GET credentials API should be equal to expireAt of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.EXPIRE_AT), items.get(i).get(Constants.EXPIRE_AT));
                Assert.assertEquals("createdBy of GET credentials API should be equal to createdBy of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.CREATED_BY), items.get(i).get(Constants.CREATED_BY));
                Assert.assertEquals("lastUpdatedBy of GET credentials API should be equal to lastUpdatedBy of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.LAST_UPDATED_BY), items.get(i).get(Constants.LAST_UPDATED_BY));
                Assert.assertEquals("createdAt of GET credentials API should be equal to createdAt of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.CREATED_AT), items.get(i).get(Constants.CREATED_AT));
                Assert.assertEquals("lastUpdatedAt of GET credentials API should be equal to lastUpdatedAt of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.LAST_UPDATED_AT), items.get(i).get(Constants.LAST_UPDATED_AT));

                //validating signingKey details
                Assert.assertEquals("signingKey_Id of GET credentials API should be equal to signingKey_Id of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.Id"), signingKey.get(Constants.ID));
                Assert.assertEquals("signingKey_keyId of GET credentials API should be equal to signingKey_keyId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.keyId"), signingKey.get(Constants.KEY_ID));
                Assert.assertEquals("signingKey_alg of GET credentials API should be equal to signingKey_keyId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.alg"), signingKey.get(Constants.ALG));
                Assert.assertEquals("signingKey_type of GET credentials API should be equal to signingKey_keyId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.type"), signingKey.get(Constants.TYPE));
                Assert.assertEquals("signingKey_size of GET credentials API should be equal to signingKey_keyId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.size"), signingKey.get(Constants.SIZE));


                //validating secret details
                Assert.assertEquals("secret_Id of GET credentials API should be equal to secret_Id of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("secret.Id"), secret.get(Constants.ID));
                Assert.assertEquals("secret_clientId of GET credentials API should be equal to secret_clientId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("secret.clientId"), secret.get(Constants.CLIENT_ID));

            } else if (i == items.size() - 1) {

                //Validating record created by POST Onboarding API

                Assert.assertEquals("credentialId of GET credentials API should be equal to credentialId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREDENTIAL_ID), items.get(i).get(Constants.CREDENTIAL_ID));
                Assert.assertEquals("credentialName of GET credentials API should be equal to credentialName of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREDENTIAL_NAME), items.get(i).get(Constants.CREDENTIAL_NAME));
                Assert.assertEquals("applicationId of GET credentials API should be equal to applicationId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.APPLICATION_ID), items.get(i).get(Constants.APPLICATION_ID));
                Assert.assertEquals("status of GET credentials API should be equal to status of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.STATUS), items.get(i).get(Constants.STATUS));
                Assert.assertEquals("activateAt of GET credentials API should be equal to activateAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.ACTIVATE_AT), items.get(i).get(Constants.ACTIVATE_AT));
                Assert.assertEquals("expireAt of GET credentials API should be equal to expireAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.EXPIRE_AT), items.get(i).get(Constants.EXPIRE_AT));
                Assert.assertEquals("createdBy of GET credentials API should be equal to createdBy of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREATED_BY), items.get(i).get(Constants.CREATED_BY));
                Assert.assertEquals("lastUpdatedBy of GET credentials API should be equal to lastUpdatedBy of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.LAST_UPDATED_BY), items.get(i).get(Constants.LAST_UPDATED_BY));
                Assert.assertEquals("createdAt of GET credentials API should be equal to createdAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREATED_AT), items.get(i).get(Constants.CREATED_AT));
                Assert.assertEquals("lastUpdatedAt of GET credentials API should be equal to lastUpdatedAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.LAST_UPDATED_AT), items.get(i).get(Constants.LAST_UPDATED_AT));

                //validating signingKey details
                Assert.assertEquals("signingKey_Id of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.Id"), signingKey.get(Constants.ID));
                Assert.assertEquals("signingKey_keyId of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.keyId"), signingKey.get(Constants.KEY_ID));
                Assert.assertEquals("signingKey_alg of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.alg"), signingKey.get(Constants.ALG));
                Assert.assertEquals("signingKey_type of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.type"), signingKey.get(Constants.TYPE));
                Assert.assertEquals("signingKey_size of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.size"), signingKey.get(Constants.SIZE));

                //validating secret details
                Assert.assertEquals("secret_Id of GET credentials API should be equal to secret_Id of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("secret.Id"), secret.get(Constants.ID));
                Assert.assertEquals("secret_clientId of GET credentials API should be equal to secret_clientId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("secret.clientId"), secret.get(Constants.CLIENT_ID));
            }
        }
    }

    @And("^I hit get credentials endpoint with filter status as \"([^\"]*)\"$")
    public void iHitGetCredentialsEndpointWithFilterStatusAs(String filter_status) {
        logger.info("********** GET Credentials Request *********** \n");

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?status=" + filter_status;

        testContext.getApiManager().getCredentialsMerchants().setGetCredentialsUrl(url);
        testContext.getApiManager().getCredentialsMerchants().setFilterStatus(filter_status);

        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @And("^I hit get credentials endpoint with filter credentialName as provided in POST credentials API request$")
    public void iHitGetCredentialsEndpointWithFilterCredentialNameAsProvidedInPOSTCredentialsAPIRequest() {
        logger.info("********** GET Credentials Request *********** \n");

        //credentialName from POST Credentials API
        String post_credentialName = testContext.getApiManager().postCredentialsMerchants().getCredentialName();
        String url;

        if (post_credentialName.contains("&")) {
            String encodedName = post_credentialName.replace("&", "%26");

            url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?credentialName=" + encodedName;
        } else {
            url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?credentialName=" + post_credentialName;
        }
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
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?limit=" + filter_limit;
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
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?page=" + filter_page;
        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @When("^I hit the post credentials endpoint five times with credential name \"([^\"]*)\"$")
    public void iHitThePostCredentialsEndpointFiveTimesWithCredentialName(String credentialName) throws InterruptedException {
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();

        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path(Constants.APPLICATION_ID));
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";

        int x = 1;
        while (x <= 4) {
            Thread.sleep(1000);
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
        org.testng.Assert.assertTrue(getRestHelper().getErrorDescription(response).contains(errorDesc), "Different error description being returned.\nExpected: " + errorDesc + "\nActual: " + getRestHelper().getErrorDescription(response));
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
        String url;
        if (applicationId.equalsIgnoreCase("space")) {
            url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/ /credentials";
        } else if (applicationId.equalsIgnoreCase("null")) {
            url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "//credentials";
        } else {
            url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + applicationId + "/credentials";
        }
        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @When("^I hit get credentials endpoint with filter credentialName as \"([^\"]*)\"$")
    public void iHitGetCredentialsEndpointWithFilterCredentialNameAs(String invalid_credentialName) throws UnsupportedEncodingException {
        logger.info("********** GET Credentials Request *********** \n");
        String url;

        if (invalid_credentialName.equalsIgnoreCase("space")) {
            url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?credentialName= ";
        } else if (invalid_credentialName.equalsIgnoreCase("null")) {
            url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?credentialName=";
        } else {
            testContext.getApiManager().postCredentialsMerchants().setCredentialName(invalid_credentialName);
            //String str = URLEncoder.encode(invalid_credentialName, StandardCharsets.US_ASCII.toString());

            url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?credentialName=" + testContext.getApiManager().postCredentialsMerchants().getCredentialName();
        }
        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @When("^I hit get credentials endpoint with filter credentialId as \"([^\"]*)\"$")
    public void iHitGetCredentialsEndpointWithFilterCredentialIdAs(String credentialId) {
        logger.info("********** GET Credentials Request *********** \n");
        String url;
        if (credentialId.equalsIgnoreCase("space")) {
            url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?credentialId= ";
        } else if (credentialId.equalsIgnoreCase("null")) {
            url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?credentialId=";
        } else {
            //String str = URLEncoder.encode(invalid_credentialName, "UTF-8");
            url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?credentialId=" + credentialId;
        }
        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @When("^I hit get credentials endpoint with applicationId \"([^\"]*)\"$")
    public void iHitGetCredentialsEndpointWithApplicationId(String applicationId) {
        logger.info("********** GET Credentials Request *********** \n");
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + applicationId + "/credentials";
        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @And("^validate GET credentials response returns empty list$")
    public void validateGETCredentialsResponseReturnsEmptyList() {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();
        HashMap page = response.path(Constants.PAGE);
        Assert.assertNotNull("page_current should not be null!", page.get(Constants.CURRENT));
        Assert.assertNotNull("page_totalItems should not be null!", page.get(Constants.TOTAL_ITEMS));
        Assert.assertNotNull("page_size should not be null!", page.get(Constants.SIZE));

        Assert.assertTrue("page_current should be 0", page.get(Constants.CURRENT).equals(0));
        Assert.assertTrue("page_totalItems should be 0", page.get(Constants.TOTAL_ITEMS).equals(0));
        Assert.assertTrue("page_size should be 20 by default", page.get(Constants.SIZE).equals(20));

        List<HashMap> items = response.path(Constants.ITEM);
        Assert.assertTrue("List of credentials should be empty when no credentials are present", items.isEmpty());
    }

    @When("^I hit get credentials endpoint with filter \"([^\"]*)\" as \"([^\"]*)\" which doesn't exist$")
    public void iHitGetCredentialsEndpointWithFilterAsWhichDoesntExist(String filterName, String value) {
        logger.info("********** GET Credentials Request *********** \n");

        if (filterName.equalsIgnoreCase("credentialId") && value.isEmpty()) {
            String url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?" + filterName + "=" + value;
            testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
        } else if (filterName.equalsIgnoreCase("credentialName") && value.equalsIgnoreCase("space")) {
            value = " ";
            String url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?" + filterName + "=" + value;
            testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
        } else if (filterName.equalsIgnoreCase("credentialId") && value.equalsIgnoreCase("space")) {
            value = " ";
            String url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?" + filterName + "=" + value;
            testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
        } else {
            String url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?" + filterName + "=" + value;
            testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
        }
    }

    @When("^I hit get credentials endpoint with applicationId from onboarding response$")
    public void iHitGetCredentialsEndpointWithApplicationIdFromOnboardingResponse() {
        logger.info("********** GET Credentials Request *********** \n");

        //Get applicationId from POST Onboarding API
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().getOneClickMerchantOnboarding().applicationIdInResponse() + "/credentials";
        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @And("^validate GET credentials response with onboarding response$")
    public void validateGETCredentialsResponseWithOnboardingResponse() {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();

        HashMap page = response.path(Constants.PAGE);

        Assert.assertNotNull("page_current should not be null!", page.get(Constants.CURRENT));
        Assert.assertNotNull("page_totalItems should not be null!", page.get(Constants.TOTAL_ITEMS));
        Assert.assertNotNull("page_size should not be null!", page.get(Constants.SIZE));

        List<HashMap> items = response.path(Constants.ITEM);

        for (int i = 0; i <= items.size() - 1; i++) {
            HashMap<Object, Object> signingKey = (HashMap) items.get(i).get(Constants.SIGNING_KEY);
            HashMap<Object, Object> secret = (HashMap) items.get(i).get(Constants.SECRET);

            Assert.assertNotNull("items_credentialId cannot be null!", items.get(i).get(Constants.CREDENTIAL_ID));
            Assert.assertEquals("credentialId of GET credentials API should be equal to credentialId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREDENTIAL_ID), items.get(i).get(Constants.CREDENTIAL_ID));

            Assert.assertNotNull("items_credentialName cannot be null!", items.get(i).get(Constants.CREDENTIAL_NAME));
            Assert.assertEquals("credentialName of GET credentials API should be equal to credentialName of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREDENTIAL_NAME), items.get(i).get(Constants.CREDENTIAL_NAME));

            Assert.assertNotNull("items_applicationId cannot be null!", items.get(i).get(Constants.APPLICATION_ID));
            Assert.assertEquals("applicationId of GET credentials API should be equal to applicationId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.APPLICATION_ID), items.get(i).get(Constants.APPLICATION_ID));
            Assert.assertEquals("status of GET credentials API should be equal to status of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.STATUS), items.get(i).get(Constants.STATUS));
            Assert.assertEquals("activateAt of GET credentials API should be equal to activateAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.ACTIVATE_AT), items.get(i).get(Constants.ACTIVATE_AT));
            Assert.assertEquals("expireAt of GET credentials API should be equal to expireAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.EXPIRE_AT), items.get(i).get(Constants.EXPIRE_AT));

            Assert.assertNotNull("items_createdBy cannot be null!", items.get(i).get(Constants.CREATED_BY));
            Assert.assertEquals("createdBy of GET credentials API should be equal to createdBy of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREATED_BY), items.get(i).get(Constants.CREATED_BY));

            Assert.assertNotNull("items_lastUpdatedBy cannot be null!", items.get(i).get(Constants.LAST_UPDATED_BY));
            Assert.assertEquals("lastUpdatedBy of GET credentials API should be equal to lastUpdatedBy of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.LAST_UPDATED_BY), items.get(i).get(Constants.LAST_UPDATED_BY));

            Assert.assertNotNull("items_createdAt cannot be null!", items.get(i).get(Constants.CREATED_AT));
            Assert.assertEquals("createdAt of GET credentials API should be equal to createdAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREATED_AT), items.get(i).get(Constants.CREATED_AT));

            Assert.assertNotNull("items_lastUpdatedAt cannot be null!", items.get(i).get(Constants.LAST_UPDATED_AT));
            Assert.assertEquals("lastUpdatedAt of GET credentials API should be equal to lastUpdatedAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.LAST_UPDATED_AT), items.get(i).get(Constants.LAST_UPDATED_AT));

            //validating signingKey details
            Assert.assertNotNull("signingKey_Id cannot not be null!", signingKey.get(Constants.ID));
            Assert.assertEquals("signingKey_Id of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.Id"), signingKey.get(Constants.ID));
            Assert.assertNotNull("signingKey_keyId cannot not be null!", signingKey.get(Constants.KEY_ID));
            Assert.assertEquals("signingKey_keyId of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.keyId"), signingKey.get(Constants.KEY_ID));
            Assert.assertNotNull("signingKey_alg cannot not be null!", signingKey.get(Constants.ALG));
            Assert.assertEquals("signingKey_alg of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.alg"), signingKey.get(Constants.ALG));
            Assert.assertNotNull("signingKey_type cannot not be null!", signingKey.get(Constants.TYPE));
            Assert.assertEquals("signingKey_type of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.type"), signingKey.get(Constants.TYPE));
            Assert.assertNotNull("signingKey_size cannot not be null!", signingKey.get(Constants.SIZE));
            Assert.assertEquals("signingKey_size of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.size"), signingKey.get(Constants.SIZE));

            //validating secret details
            Assert.assertNotNull("secret_Id cannot not be null!", secret.get(Constants.ID));
            Assert.assertNotNull("secret_clientId cannot not be null!", secret.get(Constants.CLIENT_ID));

            Assert.assertEquals("secret_Id of GET credentials API should be equal to secret_Id of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("secret.Id"), secret.get(Constants.ID));
            Assert.assertEquals("secret_clientId of GET credentials API should be equal to secret_clientId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("secret.clientId"), secret.get(Constants.CLIENT_ID));
        }
    }

    @And("^validate GET credentials response with PUT credentials$")
    public void validateGETCredentialsResponseWithPUTCredentials() {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();

        HashMap page = response.path(Constants.PAGE);

        Assert.assertNotNull("page_current should not be null!", page.get(Constants.CURRENT));
        Assert.assertNotNull("page_totalItems should not be null!", page.get(Constants.TOTAL_ITEMS));
        Assert.assertNotNull("page_size should not be null!", page.get(Constants.SIZE));

        List<HashMap> items = response.path(Constants.ITEM);

        for (int i = 0; i <= items.size() - 1; i++) {
            HashMap<Object, Object> signingKey = (HashMap) items.get(i).get(Constants.SIGNING_KEY);
            HashMap<Object, Object> secret = (HashMap) items.get(i).get(Constants.SECRET);

            Assert.assertNotNull("items_credentialId cannot be null!", items.get(i).get(Constants.CREDENTIAL_ID));
            Assert.assertNotNull("items_credentialName cannot be null!", items.get(i).get(Constants.CREDENTIAL_NAME));
            Assert.assertNotNull("items_applicationId cannot be null!", items.get(i).get(Constants.APPLICATION_ID));
            Assert.assertNotNull("items_createdBy cannot be null!", items.get(i).get(Constants.CREATED_BY));
            Assert.assertNotNull("items_lastUpdatedBy cannot be null!", items.get(i).get(Constants.LAST_UPDATED_BY));
            Assert.assertNotNull("items_createdAt cannot be null!", items.get(i).get(Constants.CREATED_AT));
            Assert.assertNotNull("items_lastUpdatedAt cannot be null!", items.get(i).get(Constants.LAST_UPDATED_AT));
            Assert.assertNotNull("signingKey_Id cannot not be null!", signingKey.get(Constants.ID));
            Assert.assertNotNull("signingKey_keyId cannot not be null!", signingKey.get(Constants.KEY_ID));
            Assert.assertNotNull("signingKey_alg cannot not be null!", signingKey.get(Constants.ALG));
            Assert.assertNotNull("signingKey_type cannot not be null!", signingKey.get(Constants.TYPE));
            Assert.assertNotNull("signingKey_size cannot not be null!", signingKey.get(Constants.SIZE));
            Assert.assertNotNull("secret_Id cannot not be null!", secret.get(Constants.ID));
            Assert.assertNotNull("secret_clientId cannot not be null!", secret.get(Constants.CLIENT_ID));

            //Validating latest record updated by PUT credentials API

            if (i == 0) {

                Assert.assertEquals("credentialId of GET credentials API should be equal to credentialId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_ID), items.get(i).get(Constants.CREDENTIAL_ID));
                Assert.assertEquals("credentialName of GET credentials API should be equal to credentialName of PUT Credentials API", testContext.getApiManager().getPutCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_NAME), items.get(i).get(Constants.CREDENTIAL_NAME));
                Assert.assertEquals("applicationId of GET credentials API should be equal to applicationId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.APPLICATION_ID), items.get(i).get(Constants.APPLICATION_ID));
                Assert.assertEquals("status of GET credentials API should be equal to status of PUT Credentials API", testContext.getApiManager().getPutCredentialsMerchants().getResponse().path(Constants.STATUS), items.get(i).get(Constants.STATUS));
                Assert.assertEquals("activateAt of GET credentials API should be equal to activateAt of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.ACTIVATE_AT), items.get(i).get(Constants.ACTIVATE_AT));
                Assert.assertEquals("expireAt of GET credentials API should be equal to expireAt of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.EXPIRE_AT), items.get(i).get(Constants.EXPIRE_AT));
                Assert.assertEquals("createdBy of GET credentials API should be equal to createdBy of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.CREATED_BY), items.get(i).get(Constants.CREATED_BY));
                Assert.assertEquals("lastUpdatedBy of GET credentials API should be equal to lastUpdatedBy of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.LAST_UPDATED_BY), items.get(i).get(Constants.LAST_UPDATED_BY));
                Assert.assertEquals("createdAt of GET credentials API should be equal to createdAt of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.CREATED_AT), items.get(i).get(Constants.CREATED_AT));
                Assert.assertEquals("lastUpdatedAt date should be today's date", getDateHelper().getCurrentDate(), items.get(i).get(Constants.LAST_UPDATED_AT).toString().substring(0, 10));

                //validating signingKey details
                Assert.assertEquals("signingKey_Id of GET credentials API should be equal to signingKey_Id of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.Id"), signingKey.get(Constants.ID));
                Assert.assertEquals("signingKey_keyId of GET credentials API should be equal to signingKey_keyId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.keyId"), signingKey.get(Constants.KEY_ID));
                Assert.assertEquals("signingKey_alg of GET credentials API should be equal to signingKey_keyId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.alg"), signingKey.get(Constants.ALG));
                Assert.assertEquals("signingKey_type of GET credentials API should be equal to signingKey_keyId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.type"), signingKey.get(Constants.TYPE));
                Assert.assertEquals("signingKey_size of GET credentials API should be equal to signingKey_keyId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.size"), signingKey.get(Constants.SIZE));


                //validating secret details
                Assert.assertEquals("secret_Id of GET credentials API should be equal to secret_Id of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("secret.Id"), secret.get(Constants.ID));
                Assert.assertEquals("secret_clientId of GET credentials API should be equal to secret_clientId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("secret.clientId"), secret.get(Constants.CLIENT_ID));

            } else if (i == items.size() - 1) {

                //Validating record created by POST Onboarding API

                Assert.assertEquals("credentialId of GET credentials API should be equal to credentialId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREDENTIAL_ID), items.get(i).get(Constants.CREDENTIAL_ID));
                Assert.assertEquals("credentialName of GET credentials API should be equal to credentialName of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREDENTIAL_NAME), items.get(i).get(Constants.CREDENTIAL_NAME));
                Assert.assertEquals("applicationId of GET credentials API should be equal to applicationId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.APPLICATION_ID), items.get(i).get(Constants.APPLICATION_ID));
                Assert.assertEquals("status of GET credentials API should be equal to status of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.STATUS), items.get(i).get(Constants.STATUS));
//                Assert.assertEquals("activateAt of GET credentials API should be equal to activateAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.ACTIVATE_AT), items.get(i).get(Constants.ACTIVATE_AT));
//                Assert.assertEquals("expireAt of GET credentials API should be equal to expireAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.EXPIRE_AT), items.get(i).get(Constants.EXPIRE_AT));
                Assert.assertEquals("createdBy of GET credentials API should be equal to createdBy of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREATED_BY), items.get(i).get(Constants.CREATED_BY));
                Assert.assertEquals("lastUpdatedBy of GET credentials API should be equal to lastUpdatedBy of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.LAST_UPDATED_BY), items.get(i).get(Constants.LAST_UPDATED_BY));
//                Assert.assertEquals("createdAt of GET credentials API should be equal to createdAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREATED_AT), items.get(i).get(Constants.CREATED_AT));
//                Assert.assertEquals("lastUpdatedAt of GET credentials API should be equal to lastUpdatedAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.LAST_UPDATED_AT), items.get(i).get(Constants.LAST_UPDATED_AT));

                //validating signingKey details
                Assert.assertEquals("signingKey_Id of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.Id"), signingKey.get(Constants.ID));
                Assert.assertEquals("signingKey_keyId of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.keyId"), signingKey.get(Constants.KEY_ID));
                Assert.assertEquals("signingKey_alg of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.alg"), signingKey.get(Constants.ALG));
                Assert.assertEquals("signingKey_type of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.type"), signingKey.get(Constants.TYPE));
                Assert.assertEquals("signingKey_size of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.size"), signingKey.get(Constants.SIZE));

                //validating secret details
                Assert.assertEquals("secret_Id of GET credentials API should be equal to secret_Id of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("secret.Id"), secret.get(Constants.ID));
                Assert.assertEquals("secret_clientId of GET credentials API should be equal to secret_clientId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("secret.clientId"), secret.get(Constants.CLIENT_ID));
            }
        }
    }

    @And("^validate GET credentials response returns with filter status$")
    public void validateGETCredentialsResponseReturnsWithFilterStatus() {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();

        HashMap page = response.path(Constants.PAGE);
        Assert.assertNotNull("page_current should not be null!", page.get(Constants.CURRENT));
        Assert.assertNotNull("page_totalItems should not be null!", page.get(Constants.TOTAL_ITEMS));
        Assert.assertNotNull("page_size should not be null!", page.get(Constants.SIZE));

        List<HashMap> items = response.path(Constants.ITEM);

        for (int i = 0; i <= items.size() - 1; i++) {

            if (testContext.getApiManager().getCredentialsMerchants().getFilterStatus().equalsIgnoreCase("A")) {
                Assert.assertEquals("status of GET credentials API with filter status=A should return only Active credentials", "A", items.get(i).get(Constants.STATUS));
            } else if (testContext.getApiManager().getCredentialsMerchants().getFilterStatus().equalsIgnoreCase("D")) {
                Assert.assertEquals("status of GET credentials API with filter status=D should return only Deactivated credentials", "D", items.get(i).get(Constants.STATUS));
            } else if (testContext.getApiManager().getCredentialsMerchants().getFilterStatus().equalsIgnoreCase("E")) {
                Assert.assertEquals("status of GET credentials API with filter status=E should return only Expired credentials", "E", items.get(i).get(Constants.STATUS));
            }
        }
    }

    @When("^I query for a list of credentials with filter sortDirection as \"([^\"]*)\"$")
    public void iQueryForAListOfCredentialsWithFilterSortDirectionAs(String sortDirection) {
        logger.info("********** GET Credentials Request *********** \n");
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?sortDirection=" + sortDirection;
        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @And("^validate GET credentials response in \"([^\"]*)\"$")
    public void validateGETCredentialsResponseIn(String sortDirection) {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();

        HashMap page = response.path(Constants.PAGE);
        Assert.assertNotNull("page_current should not be null!", page.get(Constants.CURRENT));
        Assert.assertNotNull("page_totalItems should not be null!", page.get(Constants.TOTAL_ITEMS));
        Assert.assertNotNull("page_size should not be null!", page.get(Constants.SIZE));

        List<HashMap> items = response.path(Constants.ITEM);

        for (int i = 0; i <= items.size() - 1; i++) {
            HashMap<Object, Object> signingKey = (HashMap) items.get(i).get(Constants.SIGNING_KEY);
            HashMap<Object, Object> secret = (HashMap) items.get(i).get(Constants.SECRET);

            Assert.assertNotNull("items_credentialId cannot be null!", items.get(i).get(Constants.CREDENTIAL_ID));
            Assert.assertNotNull("items_credentialName cannot be null!", items.get(i).get(Constants.CREDENTIAL_NAME));
            Assert.assertNotNull("items_applicationId cannot be null!", items.get(i).get(Constants.APPLICATION_ID));
            Assert.assertNotNull("items_createdBy cannot be null!", items.get(i).get(Constants.CREATED_BY));
            Assert.assertNotNull("items_lastUpdatedBy cannot be null!", items.get(i).get(Constants.LAST_UPDATED_BY));
            Assert.assertNotNull("items_createdAt cannot be null!", items.get(i).get(Constants.CREATED_AT));
            Assert.assertNotNull("items_lastUpdatedAt cannot be null!", items.get(i).get(Constants.LAST_UPDATED_AT));
            Assert.assertNotNull("signingKey_Id cannot not be null!", signingKey.get(Constants.ID));
            Assert.assertNotNull("signingKey_keyId cannot not be null!", signingKey.get(Constants.KEY_ID));
            Assert.assertNotNull("signingKey_alg cannot not be null!", signingKey.get(Constants.ALG));
            Assert.assertNotNull("signingKey_type cannot not be null!", signingKey.get(Constants.TYPE));
            Assert.assertNotNull("signingKey_size cannot not be null!", signingKey.get(Constants.SIZE));
            Assert.assertNotNull("secret_Id cannot not be null!", secret.get(Constants.ID));
            Assert.assertNotNull("secret_clientId cannot not be null!", secret.get(Constants.CLIENT_ID));

            //Validating latest record as per filter sortDirection
            if (sortDirection.equalsIgnoreCase("ASC")) {

                if (i == 0) {

                    //Validating record created by POST Onboarding API

                    Assert.assertEquals("credentialId of GET credentials API should be equal to credentialId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREDENTIAL_ID), items.get(i).get(Constants.CREDENTIAL_ID));
                    Assert.assertEquals("credentialName of GET credentials API should be equal to credentialName of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREDENTIAL_NAME), items.get(i).get(Constants.CREDENTIAL_NAME));
                    Assert.assertEquals("applicationId of GET credentials API should be equal to applicationId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.APPLICATION_ID), items.get(i).get(Constants.APPLICATION_ID));
                    Assert.assertEquals("status of GET credentials API should be equal to status of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.STATUS), items.get(i).get(Constants.STATUS));
                    Assert.assertEquals("activateAt of GET credentials API should be equal to activateAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.ACTIVATE_AT), items.get(i).get(Constants.ACTIVATE_AT));
                    Assert.assertEquals("expireAt of GET credentials API should be equal to expireAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.EXPIRE_AT), items.get(i).get(Constants.EXPIRE_AT));
                    Assert.assertEquals("createdBy of GET credentials API should be equal to createdBy of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREATED_BY), items.get(i).get(Constants.CREATED_BY));
                    Assert.assertEquals("lastUpdatedBy of GET credentials API should be equal to lastUpdatedBy of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.LAST_UPDATED_BY), items.get(i).get(Constants.LAST_UPDATED_BY));
                    Assert.assertEquals("createdAt of GET credentials API should be equal to createdAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREATED_AT), items.get(i).get(Constants.CREATED_AT));
                    Assert.assertEquals("lastUpdatedAt of GET credentials API should be equal to lastUpdatedAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.LAST_UPDATED_AT), items.get(i).get(Constants.LAST_UPDATED_AT));

                    //validating signingKey details
                    Assert.assertEquals("signingKey_Id of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.Id"), signingKey.get(Constants.ID));
                    Assert.assertEquals("signingKey_keyId of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.keyId"), signingKey.get(Constants.KEY_ID));
                    Assert.assertEquals("signingKey_alg of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.alg"), signingKey.get(Constants.ALG));
                    Assert.assertEquals("signingKey_type of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.type"), signingKey.get(Constants.TYPE));
                    Assert.assertEquals("signingKey_size of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.size"), signingKey.get(Constants.SIZE));

                    //validating secret details
                    Assert.assertEquals("secret_Id of GET credentials API should be equal to secret_Id of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("secret.Id"), secret.get(Constants.ID));
                    Assert.assertEquals("secret_clientId of GET credentials API should be equal to secret_clientId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("secret.clientId"), secret.get(Constants.CLIENT_ID));

                } else if (i == items.size() - 1) {

                    //Validating last created record

                    Assert.assertEquals("credentialId of GET credentials API should be equal to credentialId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_ID), items.get(i).get(Constants.CREDENTIAL_ID));
                    Assert.assertEquals("credentialName of GET credentials API should be equal to credentialName of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_NAME), items.get(i).get(Constants.CREDENTIAL_NAME));
                    Assert.assertEquals("applicationId of GET credentials API should be equal to applicationId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.APPLICATION_ID), items.get(i).get(Constants.APPLICATION_ID));
                    Assert.assertEquals("status of GET credentials API should be equal to status of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.STATUS), items.get(i).get(Constants.STATUS));
                    Assert.assertEquals("activateAt of GET credentials API should be equal to activateAt of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.ACTIVATE_AT), items.get(i).get(Constants.ACTIVATE_AT));
                    Assert.assertEquals("expireAt of GET credentials API should be equal to expireAt of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.EXPIRE_AT), items.get(i).get(Constants.EXPIRE_AT));
                    Assert.assertEquals("createdBy of GET credentials API should be equal to createdBy of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.CREATED_BY), items.get(i).get(Constants.CREATED_BY));
                    Assert.assertEquals("lastUpdatedBy of GET credentials API should be equal to lastUpdatedBy of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.LAST_UPDATED_BY), items.get(i).get(Constants.LAST_UPDATED_BY));
                    Assert.assertEquals("createdAt of GET credentials API should be equal to createdAt of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.CREATED_AT), items.get(i).get(Constants.CREATED_AT));
                    Assert.assertEquals("lastUpdatedAt date should be today's date", getDateHelper().getCurrentDate(), items.get(i).get(Constants.LAST_UPDATED_AT).toString().substring(0, 10));

                    //validating signingKey details
                    Assert.assertEquals("signingKey_Id of GET credentials API should be equal to signingKey_Id of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.Id"), signingKey.get(Constants.ID));
                    Assert.assertEquals("signingKey_keyId of GET credentials API should be equal to signingKey_keyId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.keyId"), signingKey.get(Constants.KEY_ID));
                    Assert.assertEquals("signingKey_alg of GET credentials API should be equal to signingKey_keyId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.alg"), signingKey.get(Constants.ALG));
                    Assert.assertEquals("signingKey_type of GET credentials API should be equal to signingKey_keyId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.type"), signingKey.get(Constants.TYPE));
                    Assert.assertEquals("signingKey_size of GET credentials API should be equal to signingKey_keyId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.size"), signingKey.get(Constants.SIZE));


                    //validating secret details
                    Assert.assertEquals("secret_Id of GET credentials API should be equal to secret_Id of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("secret.Id"), secret.get(Constants.ID));
                    Assert.assertEquals("secret_clientId of GET credentials API should be equal to secret_clientId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("secret.clientId"), secret.get(Constants.CLIENT_ID));

                }

            } else {
                if (i == 0) {

                    Assert.assertEquals("credentialId of GET credentials API should be equal to credentialId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_ID), items.get(i).get(Constants.CREDENTIAL_ID));
                    Assert.assertEquals("credentialName of GET credentials API should be equal to credentialName of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.CREDENTIAL_NAME), items.get(i).get(Constants.CREDENTIAL_NAME));
                    Assert.assertEquals("applicationId of GET credentials API should be equal to applicationId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.APPLICATION_ID), items.get(i).get(Constants.APPLICATION_ID));
                    Assert.assertEquals("status of GET credentials API should be equal to status of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.STATUS), items.get(i).get(Constants.STATUS));
                    Assert.assertEquals("activateAt of GET credentials API should be equal to activateAt of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.ACTIVATE_AT), items.get(i).get(Constants.ACTIVATE_AT));
                    Assert.assertEquals("expireAt of GET credentials API should be equal to expireAt of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.EXPIRE_AT), items.get(i).get(Constants.EXPIRE_AT));
                    Assert.assertEquals("createdBy of GET credentials API should be equal to createdBy of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.CREATED_BY), items.get(i).get(Constants.CREATED_BY));
                    Assert.assertEquals("lastUpdatedBy of GET credentials API should be equal to lastUpdatedBy of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.LAST_UPDATED_BY), items.get(i).get(Constants.LAST_UPDATED_BY));
                    Assert.assertEquals("createdAt of GET credentials API should be equal to createdAt of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path(Constants.CREATED_AT), items.get(i).get(Constants.CREATED_AT));
                    Assert.assertEquals("lastUpdatedAt date should be today's date", getDateHelper().getCurrentDate(), items.get(i).get(Constants.LAST_UPDATED_AT).toString().substring(0, 10));

                    //validating signingKey details
                    Assert.assertEquals("signingKey_Id of GET credentials API should be equal to signingKey_Id of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.Id"), signingKey.get(Constants.ID));
                    Assert.assertEquals("signingKey_keyId of GET credentials API should be equal to signingKey_keyId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.keyId"), signingKey.get(Constants.KEY_ID));
                    Assert.assertEquals("signingKey_alg of GET credentials API should be equal to signingKey_keyId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.alg"), signingKey.get(Constants.ALG));
                    Assert.assertEquals("signingKey_type of GET credentials API should be equal to signingKey_keyId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.type"), signingKey.get(Constants.TYPE));
                    Assert.assertEquals("signingKey_size of GET credentials API should be equal to signingKey_keyId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("signingKey.size"), signingKey.get(Constants.SIZE));


                    //validating secret details
                    Assert.assertEquals("secret_Id of GET credentials API should be equal to secret_Id of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("secret.Id"), secret.get(Constants.ID));
                    Assert.assertEquals("secret_clientId of GET credentials API should be equal to secret_clientId of POST Credentials API", testContext.getApiManager().postCredentialsMerchants().getResponse().path("secret.clientId"), secret.get(Constants.CLIENT_ID));

                } else if (i == items.size() - 1) {

                    //Validating record created by POST Onboarding API

                    Assert.assertEquals("credentialId of GET credentials API should be equal to credentialId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREDENTIAL_ID), items.get(i).get(Constants.CREDENTIAL_ID));
                    Assert.assertEquals("credentialName of GET credentials API should be equal to credentialName of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREDENTIAL_NAME), items.get(i).get(Constants.CREDENTIAL_NAME));
                    Assert.assertEquals("applicationId of GET credentials API should be equal to applicationId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.APPLICATION_ID), items.get(i).get(Constants.APPLICATION_ID));
                    Assert.assertEquals("status of GET credentials API should be equal to status of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.STATUS), items.get(i).get(Constants.STATUS));
                    Assert.assertEquals("activateAt of GET credentials API should be equal to activateAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.ACTIVATE_AT), items.get(i).get(Constants.ACTIVATE_AT));
                    Assert.assertEquals("expireAt of GET credentials API should be equal to expireAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.EXPIRE_AT), items.get(i).get(Constants.EXPIRE_AT));
                    Assert.assertEquals("createdBy of GET credentials API should be equal to createdBy of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREATED_BY), items.get(i).get(Constants.CREATED_BY));
                    Assert.assertEquals("lastUpdatedBy of GET credentials API should be equal to lastUpdatedBy of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.LAST_UPDATED_BY), items.get(i).get(Constants.LAST_UPDATED_BY));
                    Assert.assertEquals("createdAt of GET credentials API should be equal to createdAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.CREATED_AT), items.get(i).get(Constants.CREATED_AT));
                    Assert.assertEquals("lastUpdatedAt of GET credentials API should be equal to lastUpdatedAt of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path(Constants.LAST_UPDATED_AT), items.get(i).get(Constants.LAST_UPDATED_AT));

                    //validating signingKey details
                    Assert.assertEquals("signingKey_Id of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.Id"), signingKey.get(Constants.ID));
                    Assert.assertEquals("signingKey_keyId of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.keyId"), signingKey.get(Constants.KEY_ID));
                    Assert.assertEquals("signingKey_alg of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.alg"), signingKey.get(Constants.ALG));
                    Assert.assertEquals("signingKey_type of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.type"), signingKey.get(Constants.TYPE));
                    Assert.assertEquals("signingKey_size of GET credentials API should be equal to signingKey_keyId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("signingKey.size"), signingKey.get(Constants.SIZE));

                    //validating secret details
                    Assert.assertEquals("secret_Id of GET credentials API should be equal to secret_Id of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("secret.Id"), secret.get(Constants.ID));
                    Assert.assertEquals("secret_clientId of GET credentials API should be equal to secret_clientId of POST Onboarding API", testContext.getApiManager().getOneClickMerchantOnboarding().getResponse().path("secret.clientId"), secret.get(Constants.CLIENT_ID));
                }
            }
        }
    }

    @When("^I hit get credentials endpoint with filter sortDirection as \"([^\"]*)\"$")
    public void iHitGetCredentialsEndpointWithFilterSortDirectionAs(String sortDirection) {
        logger.info("********** GET Credentials Request *********** \n");
        String url;
        if (sortDirection.equalsIgnoreCase("space")) {
            url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?sortDirection= ";

        } else {

            url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?sortDirection=" + sortDirection;
        }
        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @When("^I query for a list of credentials with filter sortBy as \"([^\"]*)\"$")
    public void iQueryForAListOfCredentialsWithFilterSortByAs(String sortBy) {
        logger.info("********** GET Credentials Request *********** \n");
        if (sortBy.equalsIgnoreCase("space")) {
            String space = " ";
            String url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?sortBy=" + space;
            testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
        } else {
            String url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?sortBy=" + sortBy;
            testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
        }
    }

    @And("^validate GET credentials response by filter \"([^\"]*)\"$")
    public void validateGETCredentialsResponseByFilter(String sortBy) {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();

        HashMap page = response.path(Constants.PAGE);
        Assert.assertNotNull("page_current should not be null!", page.get(Constants.CURRENT));
        Assert.assertNotNull("page_totalItems should not be null!", page.get(Constants.TOTAL_ITEMS));
        Assert.assertNotNull("page_size should not be null!", page.get(Constants.SIZE));

        List<HashMap> items = response.path(Constants.ITEM);

        ArrayList<Object> list_add = new ArrayList<>();

        for (int i = 0; i <= items.size() - 1; i++) {
            Object expireAt_add = items.get(i).get(Constants.EXPIRE_AT);
            Object createdAt_add = items.get(i).get(Constants.CREATED_AT);
            Object lastUpdatedAt_add = items.get(i).get(Constants.LAST_UPDATED_AT);

            if (sortBy.equalsIgnoreCase("EXPIRY_DATE")) {
                list_add.add(i, expireAt_add);
            } else if (sortBy.equalsIgnoreCase("CREATION_DATE")) {
                list_add.add(i, createdAt_add);
            } else if (sortBy.equalsIgnoreCase("LAST_UPDATED_DATE")) {
                list_add.add(i, lastUpdatedAt_add);
            } else {
                list_add.add(i, lastUpdatedAt_add);
            }
        }

        System.out.println("list_add : " + list_add);
        String date1_list = list_add.get(0).toString();
        String date2_list = list_add.get(1).toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        try {
            Date date1 = sdf.parse(date1_list);
            Date date2 = sdf.parse(date2_list);

            Assert.assertTrue("Date 1 should be greater tha Date 2", date1.after(date2));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @When("^I query for a list of credentials with filter \"([^\"]*)\" and \"([^\"]*)\"$")
    public void iQueryForAListOfCredentialsWithFilterAnd(String status, String filter) {
        logger.info("********** GET Credentials Request *********** \n");

        String url;

        //credentialId from POST Credentials API
        String post_credentialId = testContext.getApiManager().postCredentialsMerchants().getResponse().getBody().path(Constants.CREDENTIAL_ID);
        String post_credentialName = testContext.getApiManager().postCredentialsMerchants().getResponse().getBody().path(Constants.CREDENTIAL_NAME);

        if (filter.equalsIgnoreCase("credentialId")) {
            url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?status=" + status + "&credentialId=" + post_credentialId;

            //Set credentialId for GET credentials API
            testContext.getApiManager().getCredentialsMerchants().setGetCredentialId(post_credentialId);
            testContext.getApiManager().getCredentialsMerchants().makeRequest(url);

        } else if (filter.equalsIgnoreCase("credentialName")) {
            url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?status=" + status + "&credentialName=" + post_credentialName;

            //Set credentialId for GET credentials API
            testContext.getApiManager().getCredentialsMerchants().setGetCredentialId(post_credentialId);
            testContext.getApiManager().getCredentialsMerchants().setGetCredentialName(post_credentialName);

            testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
        }
    }

    @And("^validate GET credentials response by multiple filter \"([^\"]*)\", \"([^\"]*)\"$")
    public void validateGETCredentialsResponseByMultipleFilter(String status, String filter) {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();

        HashMap page = response.path(Constants.PAGE);

        Assert.assertEquals("page_current should be 0.", 0, page.get(Constants.CURRENT));
        Assert.assertEquals("page_totalItems should be 1.", 1, page.get(Constants.TOTAL_ITEMS));
        Assert.assertEquals("page_size should be 20.", 20, page.get(Constants.SIZE));

        List<HashMap> items = response.path(Constants.ITEM);

        Assert.assertEquals("status of GET credentials API response should be equal to status provided in filter", status, items.get(0).get(Constants.STATUS));
        if (filter.equalsIgnoreCase("credentialId")) {
            Assert.assertEquals("credentialId of GET credentials API should be equal to credentialId provided in filter", testContext.getApiManager().getCredentialsMerchants().getGetCredentialId(), items.get(0).get(Constants.CREDENTIAL_ID));
        } else if (filter.equalsIgnoreCase("credentialName")) {
            Assert.assertEquals("credentialName of GET credentials API should be equal to credentialName provided in filter", testContext.getApiManager().getCredentialsMerchants().getGetCredentialName(), items.get(0).get(Constants.CREDENTIAL_NAME));
        }

    }

    @When("^I query for a list of credentials with filter status \"([^\"]*)\" and limit \"([^\"]*)\"$")
    public void iQueryForAListOfCredentialsWithFilterStatusAndLimit(String status, int limit) {
        logger.info("********** GET Credentials Request *********** \n");

        String url;
        url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?status=" + status + "&limit=" + limit;

        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @And("^validate GET credentials response by multiple filter status \"([^\"]*)\" and limit \"([^\"]*)\"$")
    public void validateGETCredentialsResponseByMultipleFilterStatusAndLimit(String status, int limit) {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();

        HashMap page = response.path(Constants.PAGE);

        Assert.assertEquals("page_current should be 0.", 0, page.get(Constants.CURRENT));
        Assert.assertNotNull("page_current should not be null!", page.get(Constants.CURRENT));
        Assert.assertNotNull("page_totalItems should not be null!", page.get(Constants.TOTAL_ITEMS));

        List<HashMap> items = response.path(Constants.ITEM);

        if (limit == 0) {
            Assert.assertEquals("page_size should be 1.", 1, page.get(Constants.SIZE));
            if (status.equalsIgnoreCase("A")) {
                Assert.assertTrue("List of item should contain 1 record", items.size() == 1);
                Assert.assertEquals("status of GET credentials API response should be equal to status provided in filter", status, items.get(0).get(Constants.STATUS));
            }
        } else if (limit <= 30) {
            Assert.assertEquals("page_size should be as provided in filter", limit, page.get(Constants.SIZE));
        } else {
            Assert.assertEquals("maximum page_size should be 30", 30, page.get(Constants.SIZE));
        }
    }

    @When("^I query for a list of credentials with filter status \"([^\"]*)\" and page \"([^\"]*)\"$")
    public void iQueryForAListOfCredentialsWithFilterStatusAndPage(String status, String page) {
        logger.info("********** GET Credentials Request *********** \n");

        String url;
        url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?status=" + status + "&page=" + page;

        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @And("^validate GET credentials response by multiple filter status \"([^\"]*)\" and page \"([^\"]*)\"$")
    public void validateGETCredentialsResponseByMultipleFilterStatusAndPage(String status, String pageFilter) {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();

        HashMap page = response.path(Constants.PAGE);

        Assert.assertEquals("page_current should be 0.", 0, page.get(Constants.CURRENT));
        Assert.assertNotNull("page_current should not be null!", page.get(Constants.CURRENT));
        Assert.assertNotNull("page_totalItems should not be null!", page.get(Constants.TOTAL_ITEMS));

        List<HashMap> items = response.path(Constants.ITEM);

        if (!items.isEmpty()) {
            Assert.assertEquals("status of GET credentials API response should be equal to status provided in filter", status, items.get(0).get(Constants.STATUS));
        } else {
            Assert.assertEquals("page_totalItems should be 0", 0, page.get(Constants.TOTAL_ITEMS));
            Assert.assertEquals("page_size should be 20", 20, page.get(Constants.SIZE));
        }
    }

    @When("^I query for a list of credentials with filter status \"([^\"]*)\" and sortDirection \"([^\"]*)\"$")
    public void iQueryForAListOfCredentialsWithFilterStatusAndSortDirection(String status, String sortDirection) {
        logger.info("********** GET Credentials Request *********** \n");

        String url;
        url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?status=" + status + "&sortDirection=" + sortDirection;

        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @And("^validate GET credentials response by multiple filter status \"([^\"]*)\" and sortDirection \"([^\"]*)\"$")
    public void validateGETCredentialsResponseByMultipleFilterStatusAndSortDirection(String status, String sortDirection) {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();

        HashMap page = response.path(Constants.PAGE);

        Assert.assertEquals("page_current should be 0.", 0, page.get(Constants.CURRENT));
        Assert.assertNotNull("page_current should not be null!", page.get(Constants.CURRENT));
        Assert.assertNotNull("page_totalItems should not be null!", page.get(Constants.TOTAL_ITEMS));

        List<HashMap> items = response.path(Constants.ITEM);
        ArrayList<Object> list_add = new ArrayList<>();

        for (int i = 0; i <= items.size() - 1; i++) {

            Object lastUpdatedAt_add = items.get(i).get(Constants.LAST_UPDATED_AT);

            list_add.add(i, lastUpdatedAt_add);
        }
        System.out.println("list_add : " + list_add);

        if (!items.isEmpty()) {
            Assert.assertEquals("status of GET credentials API response should be equal to status provided in filter", status, items.get(0).get(Constants.STATUS));
            String date1_list = list_add.get(0).toString();
            String date2_list = list_add.get(1).toString();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

            if (sortDirection.equalsIgnoreCase("ASC")) {
                try {
                    Date date1 = sdf.parse(date1_list);
                    Date date2 = sdf.parse(date2_list);

                    Assert.assertTrue("Date 1 should be less than Date 2", date1.before(date2));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    Date date1 = sdf.parse(date1_list);
                    Date date2 = sdf.parse(date2_list);

                    Assert.assertTrue("Date 1 should be greater than Date 2", date1.after(date2));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @When("^I query for a list of credentials with filter status \"([^\"]*)\" and sortBy \"([^\"]*)\"$")
    public void iQueryForAListOfCredentialsWithFilterStatusAndSortBy(String status, String sortBy) {
        logger.info("********** GET Credentials Request *********** \n");

        String url;
        url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?status=" + status + "&sortBy=" + sortBy;

        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @And("^validate GET credentials response by multiple filter status \"([^\"]*)\" and sortBy \"([^\"]*)\"$")
    public void validateGETCredentialsResponseByMultipleFilterStatusAndSortBy(String status, String sortBy) {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();

        HashMap page = response.path(Constants.PAGE);

        Assert.assertEquals("page_current should be 0.", 0, page.get(Constants.CURRENT));
        Assert.assertNotNull("page_current should not be null!", page.get(Constants.CURRENT));
        Assert.assertNotNull("page_totalItems should not be null!", page.get(Constants.TOTAL_ITEMS));

        List<HashMap> items = response.path(Constants.ITEM);
        ArrayList<Object> list_add = new ArrayList<>();

        for (int i = 0; i <= items.size() - 1; i++) {
            Object expireAt_add = items.get(i).get(Constants.EXPIRE_AT);
            Object createdAt_add = items.get(i).get(Constants.CREATED_AT);
            Object lastUpdatedAt_add = items.get(i).get(Constants.LAST_UPDATED_AT);

            if (sortBy.equalsIgnoreCase("EXPIRY_DATE")) {
                list_add.add(i, expireAt_add);
            } else if (sortBy.equalsIgnoreCase("CREATION_DATE")) {
                list_add.add(i, createdAt_add);
            } else if (sortBy.equalsIgnoreCase("LAST_UPDATED_DATE")) {
                list_add.add(i, lastUpdatedAt_add);
            }
        }
        System.out.println("list_add : " + list_add);

        if (!items.isEmpty() && items.size() != 1) {
            Assert.assertEquals("status of GET credentials API response should be equal to status provided in filter", status, items.get(0).get(Constants.STATUS));

            String date1_list = list_add.get(0).toString();
            String date2_list = list_add.get(1).toString();
            String date3_list = list_add.get(2).toString();
            String date4_list = list_add.get(3).toString();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {
                Date date1 = sdf.parse(date1_list);
                Date date2 = sdf.parse(date2_list);
                Date date3 = sdf.parse(date3_list);
                Date date4 = sdf.parse(date4_list);
                Assert.assertTrue("Date 1 should be greater tha Date 2", date1.after(date2));
                Assert.assertTrue("Date 2 should be greater tha Date 3", date2.after(date3));
                Assert.assertTrue("Date 3 should be greater tha Date 4", date3.after(date4));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @When("^I query for a list of credentials with filter status \"([^\"]*)\", sortBy \"([^\"]*)\", sortDirection \"([^\"]*)\"$")
    public void iQueryForAListOfCredentialsWithFilterStatusSortBySortDirection(String status, String sortBy, String sortDirection) {
        logger.info("********** GET Credentials Request *********** \n");

        String url;
        url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?status=" + status + "&sortBy=" + sortBy + "&sortDirection=" + sortDirection;

        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @And("^validate GET credentials response by multiple filter status \"([^\"]*)\", sortBy \"([^\"]*)\", sortDirection \"([^\"]*)\"$")
    public void validateGETCredentialsResponseByMultipleFilterStatusSortBySortDirection(String status, String sortBy, String sortDirection) {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();

        HashMap page = response.path(Constants.PAGE);

        Assert.assertEquals("page_current should be 0.", 0, page.get(Constants.CURRENT));
        Assert.assertNotNull("page_current should not be null!", page.get(Constants.CURRENT));
        Assert.assertNotNull("page_totalItems should not be null!", page.get(Constants.TOTAL_ITEMS));

        List<HashMap> items = response.path(Constants.ITEM);
        ArrayList<Object> list_add = new ArrayList<>();

        for (int i = 0; i <= items.size() - 1; i++) {
            Object expireAt_add = items.get(i).get(Constants.EXPIRE_AT);
            Object createdAt_add = items.get(i).get(Constants.CREATED_AT);
            Object lastUpdatedAt_add = items.get(i).get(Constants.LAST_UPDATED_AT);

            if (sortBy.equalsIgnoreCase("EXPIRY_DATE")) {
                list_add.add(i, expireAt_add);
            } else if (sortBy.equalsIgnoreCase("CREATION_DATE")) {
                list_add.add(i, createdAt_add);
            } else if (sortBy.equalsIgnoreCase("LAST_UPDATED_DATE")) {
                list_add.add(i, lastUpdatedAt_add);
            }
        }
        System.out.println("list_add : " + list_add);

        if (!items.isEmpty() && items.size() == 4) {
            Assert.assertEquals("status of GET credentials API response should be equal to status provided in filter", status, items.get(0).get(Constants.STATUS));

            String date1_list = list_add.get(0).toString();
            String date2_list = list_add.get(1).toString();
            String date3_list = list_add.get(2).toString();
            String date4_list = list_add.get(3).toString();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {
                Date date1 = sdf.parse(date1_list);
                Date date2 = sdf.parse(date2_list);
                Date date3 = sdf.parse(date3_list);
                Date date4 = sdf.parse(date4_list);

                if (sortDirection.equalsIgnoreCase("DESC")) {
                    Assert.assertTrue("Date 1 should be greater than Date 2", date1.after(date2));
                    Assert.assertTrue("Date 2 should be greater than Date 3", date2.after(date3));
                    Assert.assertTrue("Date 3 should be greater than Date 4", date3.after(date4));
                } else {
                    Assert.assertTrue("Date 1 should be lesser than Date 2", date1.before(date2));
                    Assert.assertTrue("Date 2 should be lesser than Date 3", date2.before(date3));
                    Assert.assertTrue("Date 3 should be lesser than Date 4", date3.before(date4));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (items.size() == 2) {
            Assert.assertEquals("status of GET credentials API response should be equal to status provided in filter", status, items.get(0).get(Constants.STATUS));

            String date1_list = list_add.get(0).toString();
            String date2_list = list_add.get(1).toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {
                Date date1 = sdf.parse(date1_list);
                Date date2 = sdf.parse(date2_list);
                if (sortDirection.equalsIgnoreCase("DESC")) {
                    Assert.assertTrue("Date 1 should be greater than Date 2", date1.after(date2));

                } else {
                    Assert.assertTrue("Date 1 should be lesser than Date 2", date1.before(date2));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @When("^I query for a list of credentials with filter status \"([^\"]*)\", sortBy \"([^\"]*)\", sortDirection \"([^\"]*)\", limit \"([^\"]*)\", page \"([^\"]*)\"$")
    public void iQueryForAListOfCredentialsWithFilterStatusSortBySortDirectionLimitPage(String status, String sortBy, String sortDirection, String limit, String page) {
        logger.info("********** GET Credentials Request *********** \n");

        String url;
        url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?status=" + status + "&sortBy=" + sortBy + "&sortDirection=" + sortDirection + "&limit=" + limit + "&page=" + page;

        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @And("^validate GET credentials response by multiple filter status \"([^\"]*)\", sortBy \"([^\"]*)\", sortDirection \"([^\"]*)\", limit \"([^\"]*)\", page \"([^\"]*)\"$")
    public void validateGETCredentialsResponseByMultipleFilterStatusSortBySortDirectionLimitPage(String status, String sortBy, String sortDirection, int limit, int page) throws Throwable {
        validateGETCredentialsResponseByMultipleFilterStatusSortBySortDirection(status, sortBy, sortDirection);
        validateGETCredentialsResponseByMultipleFilterLimitPageStatus(limit, page, status);
    }

    @When("^I query for a list of credentials with filter status \"([^\"]*)\", sortBy \"([^\"]*)\", sortDirection \"([^\"]*)\", limit \"([^\"]*)\", page \"([^\"]*)\", credentialId, credentialName$")
    public void iQueryForAListOfCredentialsWithFilterStatusSortBySortDirectionLimitPageCredentialIdCredentialName(String status, String sortBy, String sortDirection, int limit, String page) {
        logger.info("********** GET Credentials Request *********** \n");

        //credentialId and credentialName from POST Credentials API
        String post_credentialId = testContext.getApiManager().postCredentialsMerchants().getResponse().getBody().path(Constants.CREDENTIAL_ID);
        String post_credentialName = testContext.getApiManager().postCredentialsMerchants().getResponse().getBody().path(Constants.CREDENTIAL_NAME);

        String url;
        url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?status=" + status + "&sortBy=" + sortBy + "&sortDirection=" + sortDirection + "&limit=" + limit + "&page=" + page + "&credentialId=" + post_credentialId + "&credentialName=" + post_credentialName;

        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @And("^validate GET credentials response by multiple filter status \"([^\"]*)\", sortBy \"([^\"]*)\", sortDirection \"([^\"]*)\", limit \"([^\"]*)\", page \"([^\"]*)\", credentialId, credentialName$")
    public void validateGETCredentialsResponseByMultipleFilterStatusSortBySortDirectionLimitPageCredentialIdCredentialName(String status, String sortBy, String sortDirection, int limit, String page) {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();

        HashMap pageResponse = response.path(Constants.PAGE);

        Assert.assertEquals("page_current should be 0.", 0, pageResponse.get(Constants.CURRENT));
        Assert.assertEquals("page_totalItems should be 1.", 1, pageResponse.get(Constants.TOTAL_ITEMS));
        if (limit >= 30) {
            Assert.assertEquals("maximum page_size should be 30.", 30, pageResponse.get(Constants.SIZE));
        } else if (limit != 0) {
            Assert.assertEquals("page_size should be 20.", limit, pageResponse.get(Constants.SIZE));
        } else if (limit == 0) {
            Assert.assertEquals("minimum page_size should be 1.", 1, pageResponse.get(Constants.SIZE));
        }
        List<HashMap> items = response.path(Constants.ITEM);
        HashMap<Object, Object> signingKey = (HashMap) items.get(0).get(Constants.SIGNING_KEY);
        HashMap<Object, Object> secret = (HashMap) items.get(0).get(Constants.SECRET);

        Assert.assertTrue("Only 1 list of credentials should be returned!", items.size() == 1);
        Assert.assertEquals("credentialId of GET credentials API should be equal to credentialId provided in filter", testContext.getApiManager().getCredentialsMerchants().getGetCredentialId(), items.get(0).get(Constants.CREDENTIAL_ID));
        Assert.assertEquals("credentialName of GET credentials API should be equal to credentialName provided in filter", testContext.getApiManager().getCredentialsMerchants().getGetCredentialName(), items.get(0).get(Constants.CREDENTIAL_NAME));
        Assert.assertNotNull("applicationId cannot be null!", items.get(0).get(Constants.APPLICATION_ID));
        Assert.assertEquals("status of GET credentials API response should be equal to status provided in filter", status, items.get(0).get(Constants.STATUS));
        Assert.assertNotNull("activateAt cannot be null!", items.get(0).get(Constants.ACTIVATE_AT));
        Assert.assertNotNull("expireAt cannot be null!", items.get(0).get(Constants.EXPIRE_AT));
        Assert.assertNotNull("createdBy cannot be null!", items.get(0).get(Constants.CREATED_BY));
        Assert.assertNotNull("lastUpdatedBy cannot be null!", items.get(0).get(Constants.LAST_UPDATED_BY));
        Assert.assertNotNull("createdAt cannot be null!", items.get(0).get(Constants.CREATED_AT));
        Assert.assertNotNull("lastUpdatedAt cannot be null!", items.get(0).get(Constants.LAST_UPDATED_AT));
        Assert.assertNotNull("signingKey_Id cannot not be null!", signingKey.get(Constants.ID));
        Assert.assertNotNull("signingKey_keyId cannot not be null!", signingKey.get(Constants.KEY_ID));
        Assert.assertNotNull("signingKey_alg cannot not be null!", signingKey.get(Constants.ALG));
        Assert.assertNotNull("signingKey_type cannot not be null!", signingKey.get(Constants.TYPE));
        Assert.assertNotNull("signingKey_size cannot not be null!", signingKey.get(Constants.SIZE));
        Assert.assertNotNull("secret_Id cannot not be null!", secret.get(Constants.ID));
        Assert.assertNotNull("secret_clientId cannot not be null!", secret.get(Constants.CLIENT_ID));
    }

    @When("^I query for a list of credentials with filter \"([^\"]*)\", credentialName \"([^\"]*)\", credentialId \"([^\"]*)\"$")
    public void iQueryForAListOfCredentialsWithFilterCredentialNameCredentialId(String status, String credentialName, String credentialId) {
        logger.info("********** GET Credentials Request *********** \n");

        String url;
        url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?status=" + status + "&credentialName=" + credentialName + "&credentialId=" + credentialId;

        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @When("^I query for a list of credentials with filter \"([^\"]*)\", sortBy \"([^\"]*)\", sortDirection \"([^\"]*)\"$")
    public void iQueryForAListOfCredentialsWithFilterSortBySortDirection(String status, String sortBy, String sortDirection) {
        logger.info("********** GET Credentials Request *********** \n");
        String url;
        url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?status=" + status + "&sortBy=" + sortBy + "&sortDirection=" + sortDirection;

        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);

    }

    @When("^I query for a list of credentials with filter limit \"([^\"]*)\", page \"([^\"]*)\"$")
    public void iQueryForAListOfCredentialsWithFilterLimitPage(String limit, String page) {
        logger.info("********** GET Credentials Request *********** \n");

        String url;
        url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials?limit=" + limit + "&page=" + page;

        testContext.getApiManager().getCredentialsMerchants().makeRequest(url);
    }

    @And("^validate GET credentials response by multiple filter limit \"([^\"]*)\", page \"([^\"]*)\", status \"([^\"]*)\"$")
    public void validateGETCredentialsResponseByMultipleFilterLimitPageStatus(int limit, int page, String status) throws Throwable {
        Response response = testContext.getApiManager().getCredentialsMerchants().getResponse();

        HashMap page_response = response.path(Constants.PAGE);
        Assert.assertNotNull("page_current should not be null!", page_response.get(Constants.CURRENT));
        Assert.assertNotNull("page_current should not be null!", page_response.get(Constants.CURRENT));
        Assert.assertNotNull("page_size should not be null!", page_response.get(Constants.SIZE));

        List<HashMap> items = response.path(Constants.ITEM);

        if (page == 2 && limit == 2) {
            if (status.equalsIgnoreCase("D")) {
                Assert.assertEquals("page_current should be 0.", 0, page_response.get(Constants.CURRENT));
                Assert.assertEquals("page_size should be 2.", 2, page_response.get(Constants.SIZE));
                Assert.assertTrue("list of credentials should be equal to size", items.size() == 2);
            } else {
                Assert.assertEquals("page_current should be 2.", 2, page_response.get(Constants.CURRENT));
                Assert.assertEquals("page_size should be 2.", 2, page_response.get(Constants.SIZE));
                Assert.assertTrue("list of credentials should be equal to size", items.size() == 2);
            }
        } else if (page == 6 && limit == 5) {
            Assert.assertEquals("page_current should be 1.", 1, page_response.get(Constants.CURRENT));
            Assert.assertEquals("page_size should be 5.", 5, page_response.get(Constants.SIZE));
            Assert.assertTrue("list of credentials should be equal to current", items.size() == 1);
        } else {
            Assert.assertEquals("page_current should be 0.", 0, page_response.get(Constants.CURRENT));
            Assert.assertEquals("maximum page_size should be 30", 30, page_response.get(Constants.SIZE));
        }
    }
}
