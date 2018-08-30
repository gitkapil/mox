package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.BaseStep;




public class AccessTokenForMerchants_StepDefs implements BaseStep{
    final static Logger logger = Logger.getLogger(AccessTokenForMerchants_StepDefs.class);


    @Given("^I am a merchant$")
    public void i_am_a_merchant() {

        accessToken.setMerchantDetails(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "developer-client-id"),
                                       fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "developer-client-secret"));

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
        accessToken.retrieveAccessToken(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_base_path") +System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));

    }



    @Then("^I recieve an access_token$")
    public void i_recieve_a_valid_access_token() {

        Assert.assertEquals("Access Token Not generated. Error Description: "+ accessToken.getAccessTokenResponse().path("error_description"), 200,accessToken.getAccessTokenResponse().getStatusCode());

        //logger.info("Access Token--> "+ accessToken.getAccessToken());

    }

    @Then("^it should be a valid JWT$")
    public void valid_jwt_token() {

        Assert.assertNotNull("Generated access token is not valid", accessToken.retrieveClaimSet(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "jwks_uri_idp")));

        Assert.assertTrue("Claim Set not valid", accessToken.validateClaimSet(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "sandbox-server-application-id"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "merchant-server-application-id")));

    }

    @Then("^I shouldnot recieve an access_token$")
    public void i_should_not_recieve_an_access_token() {

       // Assert.assertNotEquals("Access Token generated", 200,response.getStatusCode());

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
       accessToken.sendBodyInJsonFormat(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_base_path") +System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));
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



}



