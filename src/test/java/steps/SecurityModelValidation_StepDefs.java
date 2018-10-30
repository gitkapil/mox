package steps;

import apiHelpers.TestContext;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.junit.Assert;


public class SecurityModelValidation_StepDefs extends UtilManager {

    TestContext testContext;

    public SecurityModelValidation_StepDefs(TestContext testContext) {
        this.testContext = testContext;
    }
    
    final static Logger logger = Logger.getLogger(SecurityModelValidation_StepDefs.class);
    private String merchantApiManagementUrl= getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-api-management-url");
    private String sandboxApiManagementUrl= getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "sandbox-api-management-url");
    private String basePathToken= getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_Token");
    private String basePathAPI= getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_APIs");


    @Given("^I am a merchant with no testContext.getApiManager().getPaymentRequest() role$")
    public void i_am_a_merchant_with_no_paymentrequest_role(){

        testContext.getApiManager().getAccessToken().setMerchantDetails(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-id-no-testContext.getApiManager().getPaymentRequest()-role"),
                getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-secret-no-testContext.getApiManager().getPaymentRequest()-role"));

        testContext.getApiManager().getAccessToken().createBody_RetrieveAccessToken();
    }


    @Given("^I am a developer with no testContext.getApiManager().getPaymentRequest() role$")
    public void i_am_a_developer_with_no_paymentrequest_role(){

        testContext.getApiManager().getAccessToken().setMerchantDetails(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "developer-client-id-no-testContext.getApiManager().getPaymentRequest()-role"),
                getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "developer-client-secret-no-testContext.getApiManager().getPaymentRequest()-role"));

        testContext.getApiManager().getAccessToken().createBody_RetrieveAccessToken();
    }



    @Given("^I am a merchant$")
    public void i_am_a_merchant() {

        testContext.getApiManager().getAccessToken().setMerchantDetails(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-id"),
                getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-client-secret"));

        testContext.getApiManager().getAccessToken().createBody_RetrieveAccessToken();


    }

    @Given("^I am a developer$")
    public void i_am_a_developer() {

        testContext.getApiManager().getAccessToken().setMerchantDetails(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "developer-client-id"),
                getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "developer-client-secret"));

        testContext.getApiManager().getAccessToken().createBody_RetrieveAccessToken();

    }

    @Given("^I am a client app with access to both sandbox & merchant server apps$")
    public void client_app_access_to_both_servers() {

        testContext.getApiManager().getAccessToken().setMerchantDetails(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "client-id-access-both-servers"),
                getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "client-secret-access-both-servers"));

        testContext.getApiManager().getAccessToken().createBody_RetrieveAccessToken();


    }



    @When("^I make a request to the sandbox Dragon ID Manager$")
    public void i_make_a_request_to_the_sandbox_Dragon_ID_Manager() {
        logger.info("********* Hitting Sandbox APIM ****************");

        testContext.getApiManager().getAccessToken().setEndpoint(sandboxApiManagementUrl +basePathToken);

        logger.info("********** Retrieving Access Token***********");
        testContext.getApiManager().getAccessToken().retrieveAccessToken(testContext.getApiManager().getAccessToken().getEndpoint() +getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));

    }

    @When("^I make a request to the merchant Dragon ID Manager$")
    public void i_make_a_request_to_the_merchant_Dragon_ID_Manager() {
        logger.info("********* Hitting Merchant (Live) APIM ****************");

        testContext.getApiManager().getAccessToken().setEndpoint(merchantApiManagementUrl +basePathToken);

        logger.info("********** Retrieving Access Token***********");
        testContext.getApiManager().getAccessToken().retrieveAccessToken(testContext.getApiManager().getAccessToken().getEndpoint() +getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "retrieve_access_token_resource"));

    }

    @Then("^the role should be \"([^\"]*)\"$")
    public void the_role_should_be(String expectedRole) {
        Assert.assertEquals("Incorrect Role!", expectedRole, testContext.getApiManager().getAccessToken().checkDevOrMerchantRoleInClaimSet());
    }

    @Then("^the aud should be sandbox server app id$")
    public void the_aud_should_be_sandbox_server_app_id() {
        Assert.assertEquals("Incorrect Aud", getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "sandbox-server-application-id"), testContext.getApiManager().getAccessToken().retrieveAudInClaimSet());

    }

    @Then("^the aud should be merchant server app id$")
    public void the_aud_should_be_merchant_server_app_id() {
        Assert.assertEquals("Incorrect Aud", getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-server-application-id"), testContext.getApiManager().getAccessToken().retrieveAudInClaimSet());

    }

    @Then("^there should be no role$")
    public void there_should_be_no_role() {
        Assert.assertEquals("Check roles within claim set ", "no \"developer\" or \"merchant\" role present within roles", testContext.getApiManager().getAccessToken().checkDevOrMerchantRoleInClaimSet());

    }

    @When("^the role should have paymentrequest$")
    public void role_has_payment_request() {
        Assert.assertTrue("testContext.getApiManager().getPaymentRequest() role not within the roles! ", testContext.getApiManager().getAccessToken().isPaymentRequestRolePresentInClaimSet());

    }

    @When("^the role should not have paymentrequest$")
    public void role_does_not_have_payment_request() {
        Assert.assertFalse("testContext.getApiManager().getPaymentRequest() present within the roles! ", testContext.getApiManager().getAccessToken().isPaymentRequestRolePresentInClaimSet());

    }

    @When("^I hit sandbox APIs$")
    public void I_hit_sandbox_APIs() {
        testContext.getApiManager().getPaymentRequest().setAuthToken(testContext.getApiManager().getAccessToken().getAccessToken());
        testContext.getApiManager().getPaymentRequest().setAuthTokenwithBearer(testContext.getApiManager().getPaymentRequest().getAuthToken());

        testContext.getApiManager().getPaymentStatus().setAuthToken(testContext.getApiManager().getAccessToken().getAccessToken());
        testContext.getApiManager().getPaymentStatus().setAuthTokenwithBearer();

        getRestHelper().setBaseURI(sandboxApiManagementUrl +basePathAPI);

    }


    @When("^I hit merchant APIs$")
    public void I_hit_merchant_APIs() {
        testContext.getApiManager().getPaymentRequest().setAuthToken(testContext.getApiManager().getAccessToken().getAccessToken());
        testContext.getApiManager().getPaymentRequest().setAuthTokenwithBearer(testContext.getApiManager().getPaymentRequest().getAuthToken());

        testContext.getApiManager().getPaymentStatus().setAuthToken(testContext.getApiManager().getAccessToken().getAccessToken());
        testContext.getApiManager().getPaymentStatus().setAuthTokenwithBearer();

        getRestHelper().setBaseURI(merchantApiManagementUrl +basePathAPI);

    }

}



