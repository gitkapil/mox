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

        accessToken.setMerchantDetails(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "merchant-client-id"),
                                       fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "merchant-client-secret"));


        accessToken.setType("merchant");

        if (System.getProperty("env").equals("playpen"))
            accessToken.setEndpoint(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_base_path"));
        else
            accessToken.setEndpoint(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_URI_Part_1")
                +accessToken.getType()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_URI_Part_2")
                +fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_Token"));

        accessToken.createBody_RetrieveAccessToken();


    }

    @Given("^I am a developer")
    public void i_am_a_developer() {

        accessToken.setMerchantDetails(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "developer-client-id"),
                fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "developer-client-secret"));

        accessToken.setType("sandbox");

        if (System.getProperty("env").equals("playpen"))
            accessToken.setEndpoint(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_base_path"));
        else
            accessToken.setEndpoint(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_URI_Part_1")
                +accessToken.getType()+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_URI_Part_2")
                +fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_Token"));

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
        accessToken.retrieveAccessToken(accessToken.getEndpoint() +System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));

    }



    @Then("^I recieve an access_token$")
    public void i_recieve_a_valid_access_token() {
        Assert.assertEquals("Access Token Not generated. Error Description: "+ accessToken.getAccessTokenResponse().path("error_description"), 200,accessToken.getAccessTokenResponse().getStatusCode());


    }

    @Then("^it should be a valid JWT$")
    public void valid_jwt_token() {

        Assert.assertNotNull("Generated access token is not valid", accessToken.retrieveClaimSet(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "jwks_uri_idp")));

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


    @Then("^I should recieve a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within token response$")
    public void i_should_recieve_a_error_response_with_error_description_and_errorcode_within_token_response(String responseCode, String errorDesc, String errorCode) {
        Assert.assertEquals("Different response code being returned ",restHelper.getResponseStatusCode(accessToken.getAccessTokenResponse()), responseCode);

        Assert.assertEquals(restHelper.getErrorCode(accessToken.getAccessTokenResponse()), errorCode,"Different error code being returned");

        Assert.assertTrue("Different error description being returned..Expected: "+ errorDesc+ "Actual: "+ restHelper.getErrorDescription(accessToken.getAccessTokenResponse()), restHelper.getErrorDescription(accessToken.getAccessTokenResponse()).contains(errorDesc));

    }

    @Then("^error message should be \"([^\"]*)\" within token response$")
    public void error_message_should_be_within_token_response(String errorMessage)  {
       Assert.assertTrue("Different error message being returned", restHelper.getErrorMessage(accessToken.getAccessTokenResponse()).contains(errorMessage) );

    }

    @When("^I make a request to the sandbox Dragon ID Manager$")
    public void i_make_a_request_to_the_sandbox_Dragon_ID_Manager() {
        accessToken.setEndpoint(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_URI_Part_1")
                    +"sandbox"+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_URI_Part_2")
                    +fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_Token"));

        logger.info("********** Retrieving Access Token***********");
        accessToken.retrieveAccessToken(accessToken.getEndpoint() +System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));

    }

    @When("^I make a request to the merchant Dragon ID Manager$")
    public void i_make_a_request_to_the_merchant_Dragon_ID_Manager() {
        accessToken.setEndpoint(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_URI_Part_1")
                    +"merchant"+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_URI_Part_2")
                    +fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_Token"));

        logger.info("********** Retrieving Access Token***********");
        accessToken.retrieveAccessToken(accessToken.getEndpoint() +System.getProperty("version")+fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));

    }

    @Then("^the role should be \"([^\"]*)\"$")
    public void the_role_should_be(String expectedRole) {
      Assert.assertEquals("Role is not developer!", expectedRole, accessToken.checkDevOrMerchantRoleInClaimSet());
    }

    @Then("^the aud should be sandbox server app id$")
    public void the_aud_should_be_sandbox_server_app_id() {
        Assert.assertEquals("Incorrect Aud", fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "sandbox-server-application-id"), accessToken.retrieveAudInClaimSet());

    }

    @Then("^the aud should be merchant server app id$")
    public void the_aud_should_be_merchant_server_app_id() {
        Assert.assertEquals("Incorrect Aud", fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "merchant-server-application-id"), accessToken.retrieveAudInClaimSet());

    }

}



