package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.BaseStep;


public class SecurityModelValidation_StepDefs implements BaseStep{
    final static Logger logger = Logger.getLogger(SecurityModelValidation_StepDefs.class);
    private String merchantApiManagementUrl= fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "merchant-api-management-url");
    private String sandboxApiManagementUrl= fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "sandbox-api-management-url");
    private String basePathToken= fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_Token");
    private String basePathAPI= fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_APIs");


    @Given("^I am a merchant with no paymentrequest role$")
    public void i_am_a_merchant_with_no_paymentrequest_role(){

        accessToken.setMerchantDetails(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-id-no-paymentrequest-role"),
                fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-secret-no-paymentrequest-role"));

        accessToken.createBody_RetrieveAccessToken();
    }


    @Given("^I am a developer with no paymentrequest role$")
    public void i_am_a_developer_with_no_paymentrequest_role(){

        accessToken.setMerchantDetails(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "developer-client-id-no-paymentrequest-role"),
                fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "developer-client-secret-no-paymentrequest-role"));

        accessToken.createBody_RetrieveAccessToken();
    }



    @Given("^I am a merchant$")
    public void i_am_a_merchant() {

        accessToken.setMerchantDetails(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-id"),
                                       fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-secret"));

        accessToken.createBody_RetrieveAccessToken();


    }

    @Given("^I am a developer$")
    public void i_am_a_developer() {

        accessToken.setMerchantDetails(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "developer-client-id"),
                fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "developer-client-secret"));

        accessToken.createBody_RetrieveAccessToken();

    }

    @Given("^I am a client app with access to both sandbox & merchant server apps$")
    public void client_app_access_to_both_servers() {

        accessToken.setMerchantDetails(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "client-id-access-both-servers"),
                fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "client-secret-access-both-servers"));

        accessToken.createBody_RetrieveAccessToken();


    }



    @When("^I make a request to the sandbox Dragon ID Manager$")
    public void i_make_a_request_to_the_sandbox_Dragon_ID_Manager() {
        logger.info("********* Hitting Sandbox APIM ****************");

        if (System.getProperty("env").equalsIgnoreCase("playpen"))
            accessToken.setEndpoint(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "retrieve_access_token_base_path"));
        else
            accessToken.setEndpoint(sandboxApiManagementUrl +basePathToken);

        logger.info("********** Retrieving Access Token***********");
        accessToken.retrieveAccessToken(accessToken.getEndpoint() +fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));

    }

    @When("^I make a request to the merchant Dragon ID Manager$")
    public void i_make_a_request_to_the_merchant_Dragon_ID_Manager() {
        logger.info("********* Hitting Merchant (Live) APIM ****************");

        if (System.getProperty("env").equalsIgnoreCase("playpen"))
            accessToken.setEndpoint(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "retrieve_access_token_base_path"));
        else
            accessToken.setEndpoint(merchantApiManagementUrl +basePathToken);

        logger.info("********** Retrieving Access Token***********");
        accessToken.retrieveAccessToken(accessToken.getEndpoint() +fileHelper.getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));

    }

    @Then("^the role should be \"([^\"]*)\"$")
    public void the_role_should_be(String expectedRole) {
      Assert.assertEquals("Incorrect Role!", expectedRole, accessToken.checkDevOrMerchantRoleInClaimSet());
    }

    @Then("^the aud should be sandbox server app id$")
    public void the_aud_should_be_sandbox_server_app_id() {
        Assert.assertEquals("Incorrect Aud", fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "sandbox-server-application-id"), accessToken.retrieveAudInClaimSet());

    }

    @Then("^the aud should be merchant server app id$")
    public void the_aud_should_be_merchant_server_app_id() {
        Assert.assertEquals("Incorrect Aud", fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "merchant-server-application-id"), accessToken.retrieveAudInClaimSet());

    }

    @Then("^there should be no role$")
    public void there_should_be_no_role() {
        Assert.assertEquals("Check roles within claim set ", "no \"developer\" or \"merchant\" role present within roles", accessToken.checkDevOrMerchantRoleInClaimSet());

    }

    @When("^the role should have paymentrequest$")
    public void role_has_payment_request() {
        Assert.assertTrue("PaymentRequest role not within the roles! ", accessToken.isPaymentRequestRolePresentInClaimSet());

    }

    @When("^the role should not have paymentrequest$")
    public void role_does_not_have_payment_request() {
        Assert.assertFalse("PaymentRequest present within the roles! ", accessToken.isPaymentRequestRolePresentInClaimSet());

    }

    @When("^I hit sandbox APIs$")
    public void I_hit_sandbox_APIs() {
        paymentRequest.setAuthToken(accessToken.getAccessToken());
        paymentRequest.setAuthTokenwithBearer(paymentRequest.getAuthToken());

        paymentStatus.setAuthToken(accessToken.getAccessToken());
        paymentStatus.setAuthTokenwithBearer();

        if (System.getProperty("env").equals("playpen"))
            restHelper.setBaseURI(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "Base_URI"));
        else
            restHelper.setBaseURI(sandboxApiManagementUrl +basePathAPI);

    }


    @When("^I hit merchant APIs$")
    public void I_hit_merchant_APIs() {
        paymentRequest.setAuthToken(accessToken.getAccessToken());
        paymentRequest.setAuthTokenwithBearer(paymentRequest.getAuthToken());

        paymentStatus.setAuthToken(accessToken.getAccessToken());
        paymentStatus.setAuthTokenwithBearer();

        if (System.getProperty("env").equals("playpen"))
            restHelper.setBaseURI(fileHelper.getValueFromPropertiesFile(Hooks.envProperties, "Base_URI"));
        else
            restHelper.setBaseURI(merchantApiManagementUrl +basePathAPI);

    }

}



