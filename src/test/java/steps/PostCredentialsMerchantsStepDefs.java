package steps;

import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import managers.TestContext;
import managers.UtilManager;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import utils.Constants;

import java.util.HashMap;
import java.util.Set;

public class PostCredentialsMerchantsStepDefs extends UtilManager {
    private static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final Set<String> APPLICATION_ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";

    TestContext testContext;
    ManagementCommon common;

    public PostCredentialsMerchantsStepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }

    @Given("^I am an authorized to create credentials as DRAGON user$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().postCredentialsMerchants().setAuthTokenWithBearer(token));
    }

    @And("^I hit the post credentials endpoint with credential name \"([^\"]*)\"$")
    public void setBody(String credentialName) {
        testContext.getApiManager().postCredentialsMerchants().setCredentialName(credentialName);
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().postCredentialsMerchants().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().postCredentialsMerchants().getApplicationId() + "/credentials";
        testContext.getApiManager().postCredentialsMerchants().makeRequest(url, credentialName);
    }



    @Then("^the create credentials response should be successful$")
    public void validResponse() {
        Assert.assertEquals(
                HttpStatus.SC_CREATED,
                getRestHelper().getResponseStatusCode(testContext.getApiManager().postCredentialsMerchants().getResponse()),
                "Expected 201 but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().postCredentialsMerchants().getResponse())
        );

        Response response = testContext.getApiManager().postCredentialsMerchants().getResponse();
        Assert.assertNotNull(response.path(Constants.PDF_PIN));
        Assert.assertNotNull(response.path(Constants.PDF_URL));
        Assert.assertNotNull(response.path(Constants.CREDENTIAL_ID));
        Assert.assertEquals(response.path(Constants.CREDENTIAL_NAME),testContext.getApiManager().postCredentialsMerchants().getCredentialName(),"Credential Name should be same as provided in input body");
        Assert.assertEquals(response.path(Constants.STATUS),"A","Credential status should be active A ");
        Assert.assertEquals(response.path(Constants.ACTIVATE_AT),"A","Credential status should be active A ");
        Assert.assertEquals(response.path(Constants.DEACTIVATE_AT),"A","Credential status should be active A ");
        Assert.assertEquals(response.path(Constants.CREATED_BY),"A","Credential status should be active A ");
        Assert.assertEquals(response.path(Constants.LAST_UPDATED_BY),"A","Credential status should be active A ");
        Assert.assertEquals(response.path(Constants.CREATED_AT),"A","Credential status should be active A ");
        Assert.assertEquals(response.path(Constants.LAST_UPDATED_AT),"A","Credential status should be active A ");

        HashMap signingKey = response.path(Constants.SIGNING_KEY);
        HashMap secret = response.path(Constants.SECRET);

        if (signingKey != null) {
            Assert.assertNotNull(signingKey.get(Constants.ALG));
            Assert.assertNotNull(signingKey.get(Constants.TYPE));
            Assert.assertNotNull(signingKey.get(Constants.SIZE));
            Assert.assertNotNull(signingKey.get(Constants.KEY_ID));
            Assert.assertNotNull(signingKey.get(Constants.VALUE));
        }
            if (secret != null) {
            Assert.assertNotNull(secret.get(Constants.ID));
            Assert.assertNotNull(secret.get(Constants.CLIENT_ID));
            Assert.assertNotNull(secret.get(Constants.VALUE));
        } else {
            getRestHelper().getResponseStatusCode(testContext.getApiManager().postCredentialsMerchants().getResponse());
        }
    }
    }


