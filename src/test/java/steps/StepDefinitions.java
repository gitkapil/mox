package steps;

import com.jayway.restassured.response.Response;
import com.nimbusds.jwt.JWTClaimsSet;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.BaseStep;

import java.util.Properties;


public class StepDefinitions implements BaseStep{
    final static Logger logger = Logger.getLogger(StepDefinitions.class);

    private String client_id, client_secret;
    private Response response;

    String generalPropertiesFilePath=System.getProperty("user.dir")+"/src/test/resources/configs/"+System.getProperty("env")+".properties";
    Properties generalProperties= fileHelper.loadPropertiesFile(generalPropertiesFilePath);


    @Given("^I am a valid merchant$")
    public void i_am_a_valid_merchant() {
      client_id= fileHelper.getValueFromPropertiesFile(generalProperties, "app-playpen-hk-dragon-client-merchant-app-client-id");
      client_secret= fileHelper.getValueFromPropertiesFile(generalProperties, "app-playpen-hk-dragon-client-merchant-app-client-secret");

    }

    @Given("^I am a merchant with invalid merchant id$")
    public void i_am_not_a_valid_merchant() {
        client_id= "random merchant id";
        client_secret= fileHelper.getValueFromPropertiesFile(generalProperties, "app-playpen-hk-dragon-client-merchant-app-client-secret");

    }

    @Given("^I am a merchant with invalid merchant secret$")
    public void i_am_a_merchant_with_invalid_merchnat_secret() {
        client_id= fileHelper.getValueFromPropertiesFile(generalProperties, "app-playpen-hk-dragon-client-merchant-app-client-id");
        client_secret= "random merchant secret";

    }



    @When("^I make a request to the Dragon ID Manager$")
    public void i_make_a_request_to_the_Dragon_ID_Manager()  {
        String endpoint= fileHelper.getValueFromPropertiesFile(generalProperties, "retrieve_access_token_url");
        String resource= fileHelper.getValueFromPropertiesFile(generalProperties, "app-playpen-hk-dragon-resource-server-api-management-application-id");

        response= restHelper.postResponseWithEncodedBody(helper.createBody_RetrieveAccessToken(client_id, client_secret,"client_credentials", resource), endpoint) ;

    }

    @When("^I make a request to the Dragon ID Manager with an incorrect grant type$")
    public void i_make_a_request_to_the_Dragon_ID_Manager_invalid_grant_type()  {
        String endpoint= fileHelper.getValueFromPropertiesFile(generalProperties, "retrieve_access_token_url");
        String resource= fileHelper.getValueFromPropertiesFile(generalProperties, "app-playpen-hk-dragon-resource-server-api-management-application-id");

        response= restHelper.postResponseWithEncodedBody(helper.createBody_RetrieveAccessToken(client_id, client_secret,"incorrect_granttype", resource), endpoint) ;

    }

    @Then("^I recieve an access_token$")
    public void i_recieve_a_valid_access_token() {

        Assert.assertEquals("Access Token Not generated", 200,response.getStatusCode());

        logger.info("Access Token--> "+ response.path("access_token"));
    }

    @Then("^it should be a valid JWT$")
    public void valid_jwt_token() {

        JWTClaimsSet claimSet= jwtHelper.validateJWT(response.path("access_token").toString(), fileHelper.getValueFromPropertiesFile(generalProperties, "jwks_uri"));

        Assert.assertNotNull("Generated access token is not valid", claimSet);

    }

    @Then("^I shouldnot recieve an access_token$")
    public void i_should_not_recieve_an_access_token() {

        Assert.assertNotEquals("Access Token generated", 200,response.getStatusCode());

        logger.info("Response failed because of: "+ response.path("error"));


    }

}

