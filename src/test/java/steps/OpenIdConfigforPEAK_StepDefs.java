package steps;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.testng.Assert;

public class OpenIdConfigforPEAK_StepDefs extends UtilManager{
    TestContext testContext;
    
    public OpenIdConfigforPEAK_StepDefs(TestContext testContext) {
        this.testContext = testContext;
    }
    
    @When("^I hit the openid config URI$")
    public void i_hit_the_openid_config_URI()  {
        testContext.getApiManager().getOpenIdConfig().retrieveOpenIdConfigResponse(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "openid-configuration-for-PEAK"));
    }

    @Then("^I receive a successful response$")
    public void i_should_receive_a_successful_response() {
        Assert.assertEquals(testContext.getApiManager().getOpenIdConfig().getOpenIdConfigResponse().getStatusCode(), 200, "OpenId Config request was not successful");
    }

    @Then("^the response body should have valid values$")
    public void the_response_body_should_have_valid_values()  {

        Assert.assertNotNull(testContext.getApiManager().getOpenIdConfig().issuerInResponse(), "Incorrect Issuer Returned!");
        Assert.assertNotNull(testContext.getApiManager().getOpenIdConfig().jwksUriInResponse(), "Incorrect jwks Returned!");

        Assert.assertEquals(testContext.getApiManager().getOpenIdConfig().responseTypesSupportedInResponse(), "id_token token", "Value for Response Types supported within response is incorrect!");
        Assert.assertEquals(testContext.getApiManager().getOpenIdConfig().scopesSupportedInResponse(), "openid", "Value for scopes supported within response is incorrect!");
        Assert.assertEquals(testContext.getApiManager().getOpenIdConfig().subjectTypesSupportedInResponse(), "pairwise", "Value for subject types supported within response is incorrect!");
        Assert.assertEquals(testContext.getApiManager().getOpenIdConfig().algValueInResponse(), "RS256", "Value for alg supported within response is incorrect!");
        Assert.assertEquals(testContext.getApiManager().getOpenIdConfig().claimsSupportedInResponse(), "sub", "Value for claims supported within response is incorrect!");


    }


    @When("^I obtain the JWKS URI$")
    public void i_obtain_the_JWKS_URI()  {
        testContext.getApiManager().getOpenIdConfig().setJwksURI(testContext.getApiManager().getOpenIdConfig().jwksUriInResponse());
    }

    @When("^I hit the JWKS URI$")
    public void i_hit_the_JWKS_URI()  {
       testContext.getApiManager().getOpenIdConfig().retrieveJwksUriResponse();
    }

    @Then("^I should receive a successful response from JWKS URI Request$")
    public void i_should_receive_a_successful_response_from_JWKS_URI_Request()  {
        Assert.assertEquals(testContext.getApiManager().getOpenIdConfig().getJwksUriResponse().getStatusCode(), 200, "JWKS URI request was not successful");
    }

    @Then("^the response body should have valid keys values$")
    public void the_response_body_should_have_valid_keys_values()  {
        testContext.getApiManager().getOpenIdConfig().retrieveKeysValues();

        Assert.assertEquals(testContext.getApiManager().getOpenIdConfig().algInJWKSResponse(), "RS256", "Value for alg supported within JWKS URI response is incorrect!");
        Assert.assertEquals(testContext.getApiManager().getOpenIdConfig().ktyInJWKSResponse(), "RSA", "Value for kty within JWKS URI response is incorrect!");
        Assert.assertEquals(testContext.getApiManager().getOpenIdConfig().useInJWKSResponse(), "sig", "Value for use within JWKS URI response is incorrect!");


    }
}
