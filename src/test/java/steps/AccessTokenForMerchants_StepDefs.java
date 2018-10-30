package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import managers.TestContext;


public class AccessTokenForMerchants_StepDefs extends UtilManager{
    TestContext testContext;

    public AccessTokenForMerchants_StepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    final static Logger logger = Logger.getLogger(AccessTokenForMerchants_StepDefs.class);


    @Given("^I am an user$")
    public void i_am_an_user() {
        try {
            logger.info("********* User Type: " + System.getProperty("usertype") + " ****************");

            if (System.getProperty("usertype").equalsIgnoreCase("merchant")) {
                logger.info("********* Hitting Merchant (Live) APIM ****************");

                testContext.getApiManager().getAccessToken().setType("merchant");
                testContext.getApiManager().getAccessToken().setMerchantDetails(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-id"),
                        getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-secret"));

                testContext.getApiManager().getAccessToken().setEndpoint(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-api-management-url")
                        +getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_Token"));

            } else {
                logger.info("********* Hitting Sandbox APIM ****************");

                testContext.getApiManager().getAccessToken().setType("sandbox");
                testContext.getApiManager().getAccessToken().setMerchantDetails(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "developer-client-id"),
                        getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "developer-client-secret"));

                testContext.getApiManager().getAccessToken().setEndpoint(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "sandbox-api-management-url")
                        +getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_Token"));

            }
        }
        catch (NullPointerException e){
            logger.info("********* Hitting Sandbox APIM ****************");

            testContext.getApiManager().getAccessToken().setType("sandbox");
            testContext.getApiManager().getAccessToken().setMerchantDetails(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "developer-client-id"),
                    getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "developer-client-secret"));

            testContext.getApiManager().getAccessToken().setEndpoint(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "sandbox-api-management-url")
                    +getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_Token"));

        }

        testContext.getApiManager().getAccessToken().createBody_RetrieveAccessToken();

    }


    @Given("^I have \"([^\"]*)\" as client id$")
    public void i_have_as_client_id(String invalidClientId)  {
        testContext.getApiManager().getAccessToken().setClientId(invalidClientId);
        testContext.getApiManager().getAccessToken().createBody_RetrieveAccessToken();
    }

    @Given("^I have \"([^\"]*)\" as client secret$")
    public void i_have_as_client_secret(String invalidClientSecret)  {
        testContext.getApiManager().getAccessToken().setClientSecret(invalidClientSecret);
        testContext.getApiManager().getAccessToken().createBody_RetrieveAccessToken();
    }


    @When("^I make a request to the Dragon ID Manager$")
    public void i_make_a_request_to_the_Dragon_ID_Manager()  {
        logger.info("********** Retrieving Access Token***********");
        testContext.getApiManager().getAccessToken().retrieveAccessToken(testContext.getApiManager().getAccessToken().getEndpoint() +getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));

    }



    @Then("^I recieve an access_token$")
    public void i_recieve_a_valid_access_token() {
        Assert.assertEquals("Access Token Not generated. Error Description: "+ testContext.getApiManager().getAccessToken().getAccessTokenResponse().path("error_description"), 200,testContext.getApiManager().getAccessToken().getAccessTokenResponse().getStatusCode());


    }

    @Then("^it should be a valid JWT$")
    public void valid_jwt_token() {

        Assert.assertNotNull("Generated access token is not valid", testContext.getApiManager().getAccessToken().retrieveClaimSet(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "jwks_uri_idp")));

    }

    @Then("^I shouldnot recieve an access_token$")
    public void i_should_not_recieve_an_access_token() {
        Assert.assertNull("Access Token generated",testContext.getApiManager().getAccessToken().getAccessToken());

        logger.info("Response failed because of Error: "+ getRestHelper().getErrorMessage(testContext.getApiManager().getAccessToken().getAccessTokenResponse()));


    }


    @Given("^I dont provide \"([^\"]*)\"$")
    public void i_dont_provide(String key) {
        testContext.getApiManager().getAccessToken().createInvalidBody(key);
    }

    @Then("^I should get a \"([^\"]*)\"$")
    public void i_should_get_a_error_code(String responseCode)  {
        Assert.assertEquals("Different response code is returned!",Integer.parseInt(responseCode),  getRestHelper().getResponseStatusCode(testContext.getApiManager().getAccessToken().getAccessTokenResponse()));


    }

    @When("^I make a request to the Dragon ID Manager with body in JSON format$")
    public void i_make_a_request_to_the_Dragon_ID_Manager_with_body_in_JSON_format() {

        logger.info("********** Retrieving Access Token***********");
        testContext.getApiManager().getAccessToken().sendBodyInJsonFormat(testContext.getApiManager().getAccessToken().getEndpoint() +getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));


    }

    @Then("^response should also have expiresOn, token type$")
    public void response_should_also_have_expiresOn_token_type() {
        Assert.assertNotNull("Expires On field is null in the response!",testContext.getApiManager().getAccessToken().expiresOnInResponse());
        Assert.assertEquals("Token Type is not Bearer or is null in the Response..Please check!", testContext.getApiManager().getAccessToken().tokenTypeInResponse(), "Bearer");

    }

    @Given("^I have invalid_value for the header \"([^\"]*)\"$")
    public void i_have_invalid_value_for_the_header(String key)  {
        testContext.getApiManager().getAccessToken().createInvalidHeader(key);
    }


    @Then("^I should recieve a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within token response$")
    public void i_should_recieve_a_error_response_with_error_description_and_errorcode_within_token_response(String responseCode, String errorDesc, String errorCode) {
        Assert.assertEquals("Different response code being returned ", Integer.parseInt(responseCode), getRestHelper().getResponseStatusCode(testContext.getApiManager().getAccessToken().getAccessTokenResponse()));

        Assert.assertEquals("Different error code being returned", errorCode, getRestHelper().getErrorCode(testContext.getApiManager().getAccessToken().getAccessTokenResponse()));

        Assert.assertTrue("Different error description being returned..Expected: "+ errorDesc+ "Actual: "+ getRestHelper().getErrorDescription(testContext.getApiManager().getAccessToken().getAccessTokenResponse()), getRestHelper().getErrorDescription(testContext.getApiManager().getAccessToken().getAccessTokenResponse()).contains(errorDesc));

    }

    @Then("^error message should be \"([^\"]*)\" within token response$")
    public void error_message_should_be_within_token_response(String errorMessage)  {
        Assert.assertTrue("Different error message being returned...Expected: "+ errorMessage+ "Actual: "+ getRestHelper().getErrorMessage(testContext.getApiManager().getAccessToken().getAccessTokenResponse()), getRestHelper().getErrorMessage(testContext.getApiManager().getAccessToken().getAccessTokenResponse()).contains(errorMessage) );

    }




}



