package steps;
import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import utils.Constants;
import java.util.Set;

public class PutCredentials_StepDefs extends UtilManager {
    private static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final Set<String> APPLICATION_ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";

    TestContext testContext;
    ManagementCommon common;

    public PutCredentials_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }

    @Given("^I am an authorized to put credentials as DRAGON user$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPutCredentialsMerchants().setAuthTokenWithBearer(token));
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().postCredentialsMerchants().setAuthTokenWithBearer(token));
    }

    @And("^I hit the put credentials endpoint with new credential name \"([^\"]*)\"$")
    public void hitPostCredentialsWithCredentialsName(String credentialName) {

        testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialName);
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        testContext.getApiManager().getOneClickMerchantOnboarding().setSubUnitId(applicationResponse.getBody().path("application.subUnitId"));

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, testContext.getApiManager().postCredentialsMerchants().getCredentialName());

//
//        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();
//
//        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);
//
//        String putCredentialEndPoint = getRestHelper().getBaseURI() +
//                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
//                + "/" + applicationResponse.getBody().path("application.applicationId") + "/credentials" + "/" + credentialId ;
//
//        testContext.getApiManager().getPutCredentialsMerchants().makeRequest(putCredentialEndPoint,testContext.getApiManager().postCredentialsMerchants().getCredentialName());

    }

    @Then("^put credentials response should be successful$")
    public void verifySuccessResponse() {
        Assert.assertEquals(
                HttpStatus.SC_CREATED,
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutCredentialsMerchants().getResponse()),
                "Expected 200 but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutCredentialsMerchants().getResponse()));

        Response response = testContext.getApiManager().getPutCredentialsMerchants().getResponse();
        Assert.assertEquals(response.path(Constants.CREDENTIAL_NAME).toString(), testContext.getApiManager().getPutCredentialsMerchants().getCredentialName(), "credential name didn't match");
    }

    public String credentialId() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPutCredentialsMerchants().setAuthTokenWithBearer(token));
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        String credentialName = RandomStringUtils.randomAlphabetic(10);
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + applicationResponse.getBody().path("application.applicationId") + "/credentials";
        testContext.getApiManager().getPutCredentialsMerchants().makeRequest(url, credentialName);
        Response credentialResponse = testContext.getApiManager().postCredentialsMerchants().getResponse();
        String credentialId = credentialResponse.path(Constants.CREDENTIAL_ID);
        return credentialId;
    }
}


