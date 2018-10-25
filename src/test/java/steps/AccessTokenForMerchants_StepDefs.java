package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.BaseStep;


public class AccessTokenForMerchants_StepDefs implements BaseStep{
    final static Logger logger = Logger.getLogger(AccessTokenForMerchants_StepDefs.class);


    @Given("^I am an user$")
    public void i_am_an_user() {
        try {
            logger.info("********* User Type: " + System.getProperty("usertype") + " ****************");

            if (System.getProperty("usertype").equalsIgnoreCase("merchant")) {
                logger.info("********* Hitting Merchant (Live) APIM ****************");

                accessToken.setType("merchant");
                accessToken.setMerchantDetails(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-id"),
                        fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-secret"));

                if (System.getProperty("env").equalsIgnoreCase("playpen"))
                    accessToken.setEndpoint(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "retrieve_access_token_base_path"));
                else
                    accessToken.setEndpoint(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "merchant-api-management-url")
                            +fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_Token"));

            } else {
                logger.info("********* Hitting Sandbox APIM ****************");

                accessToken.setType("sandbox");
                accessToken.setMerchantDetails(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "developer-client-id"),
                        fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "developer-client-secret"));

                if (System.getProperty("env").equalsIgnoreCase("playpen"))
                    accessToken.setEndpoint(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "retrieve_access_token_base_path"));
                else
                    accessToken.setEndpoint(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "sandbox-api-management-url")
                            +fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_Token"));

            }
        }
        catch (NullPointerException e){
            logger.info("********* Hitting Sandbox APIM ****************");

            accessToken.setType("sandbox");
            accessToken.setMerchantDetails(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "developer-client-id"),
                    fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "developer-client-secret"));

            if (System.getProperty("env").equalsIgnoreCase("playpen"))
                accessToken.setEndpoint(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "retrieve_access_token_base_path"));
            else
                accessToken.setEndpoint(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "sandbox-api-management-url")
                        +fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_Token"));

        }

        accessToken.createBody_RetrieveAccessToken();

    }


    @Given("^I have \"([^\"]*)\" as client id$")
    public void i_have_as_client_id(String invalidClientId)  {
        accessToken.setClientId(invalidClientId);
        accessToken.createBody_RetrieveAccessToken();
    }

    @Given("^I have \"([^\"]*)\" as client secret$")
    public void i_have_as_client_secret(String invalidClientSecret)  {
        accessToken.setClientSecret(invalidClientSecret);
        accessToken.createBody_RetrieveAccessToken();
    }


    @When("^I make a request to the Dragon ID Manager$")
    public void i_make_a_request_to_the_Dragon_ID_Manager()  {
        logger.info("********** Retrieving Access Token***********");
        accessToken.retrieveAccessToken(accessToken.getEndpoint() +fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));

    }



    @Then("^I recieve an access_token$")
    public void i_recieve_a_valid_access_token() {
        Assert.assertEquals("Access Token Not generated. Error Description: "+ accessToken.getAccessTokenResponse().path("error_description"), 200,accessToken.getAccessTokenResponse().getStatusCode());


    }

    @Then("^it should be a valid JWT$")
    public void valid_jwt_token() {

        Assert.assertNotNull("Generated access token is not valid", accessToken.retrieveClaimSet(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "jwks_uri_idp")));

    }

    @Then("^I shouldnot recieve an access_token$")
    public void i_should_not_recieve_an_access_token() {


        Assert.assertNull("Access Token generated",accessToken.getAccessToken());

        logger.info("Response failed because of Error: "+ restHelper.getErrorMessage(accessToken.getAccessTokenResponse()));


    }


    @Given("^I dont provide \"([^\"]*)\"$")
    public void i_dont_provide(String key) {
        accessToken.createInvalidBody(key);
    }

    @Then("^I should get a \"([^\"]*)\"$")
    public void i_should_get_a_error_code(String responseCode)  {
       Assert.assertEquals("Different response code is returned!",Integer.parseInt(responseCode),  restHelper.getResponseStatusCode(accessToken.getAccessTokenResponse()));


    }

    @When("^I make a request to the Dragon ID Manager with body in JSON format$")
    public void i_make_a_request_to_the_Dragon_ID_Manager_with_body_in_JSON_format() {

        logger.info("********** Retrieving Access Token***********");
        accessToken.sendBodyInJsonFormat(accessToken.getEndpoint() +fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));


    }

    @Then("^response should also have expiresOn, token type$")
    public void response_should_also_have_expiresOn_token_type() {
        Assert.assertNotNull("Expires On field is null in the response!",accessToken.expiresOnInResponse());
        Assert.assertEquals("Token Type is not Bearer or is null in the Response..Please check!", accessToken.tokenTypeInResponse(), "Bearer");

    }

    @Given("^I have invalid_value for the header \"([^\"]*)\"$")
    public void i_have_invalid_value_for_the_header(String key)  {
        accessToken.createInvalidHeader(key);
    }


    @Then("^I should recieve a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within token response$")
    public void i_should_recieve_a_error_response_with_error_description_and_errorcode_within_token_response(String responseCode, String errorDesc, String errorCode) {
        Assert.assertEquals("Different response code being returned ", Integer.parseInt(responseCode), restHelper.getResponseStatusCode(accessToken.getAccessTokenResponse()));

        Assert.assertEquals("Different error code being returned", errorCode, restHelper.getErrorCode(accessToken.getAccessTokenResponse()));

        Assert.assertTrue("Different error description being returned..Expected: "+ errorDesc+ "Actual: "+ restHelper.getErrorDescription(accessToken.getAccessTokenResponse()), restHelper.getErrorDescription(accessToken.getAccessTokenResponse()).contains(errorDesc));

    }

    @Then("^error message should be \"([^\"]*)\" within token response$")
    public void error_message_should_be_within_token_response(String errorMessage)  {
       Assert.assertTrue("Different error message being returned...Expected: "+ errorMessage+ "Actual: "+ restHelper.getErrorMessage(accessToken.getAccessTokenResponse()), restHelper.getErrorMessage(accessToken.getAccessTokenResponse()).contains(errorMessage) );

    }




}



