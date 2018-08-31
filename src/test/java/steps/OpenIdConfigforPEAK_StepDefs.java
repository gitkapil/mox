package steps;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.testng.Assert;
import utils.BaseStep;

public class OpenIdConfigforPEAK_StepDefs implements BaseStep {
    @When("^I hit the openid config URI$")
    public void i_hit_the_openid_config_URI()  {
        openIdConfig.retrieveOpenIdConfigResponse(fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "openid-configuration-for-PEAK"));
    }

    @Then("^I recieve a successful response$")
    public void i_should_recieve_a_successful_response() {
        Assert.assertEquals(openIdConfig.getOpenIdConfigResponse().getStatusCode(), 200, "OpenId Config request was not successful");
    }

    @Then("^the response body should have valid values$")
    public void the_response_body_should_have_valid_values()  {

        Assert.assertNotNull(openIdConfig.issuerInResponse(), "Incorrect Issuer Returned!");
        Assert.assertNotNull(openIdConfig.jwksUriInResponse(), "Incorrect jwks Returned!");

        Assert.assertEquals(openIdConfig.responseTypesSupportedInResponse(), "id_token token", "Value for Response Types supported within response is incorrect!");
        Assert.assertEquals(openIdConfig.scopesSupportedInResponse(), "openid", "Value for scopes supported within response is incorrect!");
        Assert.assertEquals(openIdConfig.subjectTypesSupportedInResponse(), "pairwise", "Value for subject types supported within response is incorrect!");
        Assert.assertEquals(openIdConfig.algValueInResponse(), "RS256", "Value for alg supported within response is incorrect!");
        Assert.assertEquals(openIdConfig.claimsSupportedInResponse(), "sub", "Value for claims supported within response is incorrect!");


    }


    @When("^I obtain the JWKS URI$")
    public void i_obtain_the_JWKS_URI()  {
        openIdConfig.setJwksURI(openIdConfig.jwksUriInResponse());
    }

    @When("^I hit the JWKS URI$")
    public void i_hit_the_JWKS_URI()  {
       openIdConfig.retrieveJwksUriResponse();
    }

    @Then("^I should recieve a successful response from JWKS URI Request$")
    public void i_should_recieve_a_successful_response_from_JWKS_URI_Request()  {
        Assert.assertEquals(openIdConfig.getJwksUriResponse().getStatusCode(), 200, "JWKS URI request was not successful");
    }

    @Then("^the response body should have valid keys values$")
    public void the_response_body_should_have_valid_keys_values()  {
        openIdConfig.retrieveKeysValues();

        Assert.assertEquals(openIdConfig.algInJWKSResponse(), "RS256", "Value for alg supported within JWKS URI response is incorrect!");
        Assert.assertEquals(openIdConfig.ktyInJWKSResponse(), "RSA", "Value for kty within JWKS URI response is incorrect!");
        Assert.assertEquals(openIdConfig.useInJWKSResponse(), "sig", "Value for use within JWKS URI response is incorrect!");


    }
}
