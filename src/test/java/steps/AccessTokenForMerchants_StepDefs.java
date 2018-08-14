package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.BaseStep;

import java.util.*;



public class AccessTokenForMerchants_StepDefs implements BaseStep{
    final static Logger logger = Logger.getLogger(AccessTokenForMerchants_StepDefs.class);

   // Properties generalProperties= loadGeneralProperties();


    @Given("^I am a merchant$")
    public void i_am_a_merchant() {

        accessToken.setMerchantDetails(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "client-id"),
                                       fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "client-secret"),
                                       fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "application-id"),
                                       "client_credentials",
                                       fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_url"));

    }

    @Given("^I have an invalid client id$")
    public void invalid_client_id() {
        accessToken.setClientId("random client id");

    }

    @Given("^I have an invalid client secret$")
    public void invalid_client_secret() {
        accessToken.setClientSecret("random client secret");

    }

    @Given("^I pass an invalid grant type$")
    public void invalid_grant_type() {
        accessToken.setGrantType("random grant type");

    }

    @Given("^I pass an invalid application id$")
    public void invalid_app_id() {
        accessToken.setAppId("random app id");

    }


    @When("^I make a request to the Dragon ID Manager$")
    public void i_make_a_request_to_the_Dragon_ID_Manager()  {

        accessToken.retrieveAccessToken();

    }



    @Then("^I recieve an access_token$")
    public void i_recieve_a_valid_access_token() {

        Assert.assertEquals("Access Token Not generated. Error Description: "+ accessToken.getAccessToken().path("error_description"), 200,accessToken.getAccessToken().getStatusCode());

        logger.info("Access Token--> "+ accessToken.getAccessToken().path("access_token"));

    }

    @Then("^it should be a valid JWT$")
    public void valid_jwt_token() {

        Assert.assertNotNull("Generated access token is not valid", accessToken.retrieveClaimSet(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "jwks_uri")));

        Assert.assertTrue("Claim Set not valid", accessToken.validateClaimSet());

    }

    @Then("^I shouldnot recieve an access_token$")
    public void i_should_not_recieve_an_access_token() {

       // Assert.assertNotEquals("Access Token generated", 200,response.getStatusCode());

          Assert.assertNull("Access Token generated",accessToken.getAccessToken().path("access_token"));

          logger.info("Response failed because of Error: "+ accessToken.getAccessToken().path("error"));


    }


    }



