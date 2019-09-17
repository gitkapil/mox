package steps;
import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import utils.Constants;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class ManagementPostSigningKey_StepDefs extends UtilManager {
    private static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final Set<String> APPLICATION_ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String SIG_HEADER_LIST_POST_APPLICATION = "header-list-post-application";
    private static final String VALID_BASE64_ENCODED_RSA_PUBLIC_KEY = "valid_base64_encoded_rsa_public_key";
    TestContext testContext;
    ManagementCommon common;

    public ManagementPostSigningKey_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }

    @And("^I create a new application id for signing key$")
    public void createApplication() {
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().getPostSigningKeys().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
    }

    @Given("^I am an authorized Signing Key DRAGON user$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPostSigningKeys().setAuthTokenWithBearer(token));
    }

    @And("^I have an activate date \"([^\"]*)\" and deactivate date \"([^\"]*)\", with entity status \"([^\"]*)\"$")
    public void setBody(String activateAt, String deactivateAt, String entityStatus) {
        testContext.getApiManager().getPostSigningKeys().setActivateAt(activateAt);
        testContext.getApiManager().getPostSigningKeys().setDeactivateAt(deactivateAt);
        testContext.getApiManager().getPostSigningKeys().setEntityStatus(entityStatus);

    }

    @When("^I make a request to create a new signing key with application id$")
    public void makeRequest() {
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().getPostSigningKeys().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().getPostSigningKeys().getApplicationId() + "/keys/signing";
        testContext.getApiManager().getPostSigningKeys().makeRequest(url);
    }


    @When("^I make a request to create a new signing key with \"([^\"]*)\"$")
    public void makeRequestWithInvalidApplicationID(String applicationId) {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + applicationId + "/keys/signing";
        testContext.getApiManager().getPostSigningKeys().makeRequest(url);
    }

    @Then("^the create signing key response should give a \"([^\"]*)\" http status with error code \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void invalidResponse(String httpStatus, String errorCode, String errorDescription) {
        Assert.assertEquals(
                Integer.parseInt(httpStatus),
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostSigningKeys().getResponse()),
                "Expected http status " + httpStatus + " but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostSigningKeys().getResponse())
        );
        Assert.assertEquals(
                errorCode,
                getRestHelper().getErrorCode(testContext.getApiManager().getPostSigningKeys().getResponse()),
                "Expected error code " + errorCode + " but got " +
                        getRestHelper().getErrorCode(testContext.getApiManager().getPostSigningKeys().getResponse())
        );

        if (!getRestHelper().getErrorDescription(testContext.getApiManager().getPostSigningKeys().getResponse()).contains(errorDescription)) {
            Assert.assertEquals(
                    errorDescription,
                    getRestHelper().getErrorDescription(testContext.getApiManager().getPostSigningKeys().getResponse()),
                    "Expected error description " + errorDescription + " but got " +
                            getRestHelper().getErrorDescription(testContext.getApiManager().getPostSigningKeys().getResponse())
            );
        }
    }


    @When("^I make a POST request to the post signing key endpoint with \"([^\"]*)\" missing in the header$")
    public void i_make_a_post_request_to_the_signing_key_endpoint_with_key_missing_in_the_header(String key) throws IOException {
        String url = getRestHelper().getBaseURI() + getFileHelper()
                .getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) +
                "/" + testContext.getApiManager().getPostSigningKeys().getApplicationId() + "/keys/signing/";
        testContext.getApiManager().getPostSigningKeys().executePostRequestWithMissingHeaderKeys(url, key);
    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" error code within the POST signing key response$")
    public void i_should_receive_an_error_response_with_error_description_and_error_code(int responseCode, String errorDesc, String errorCode) {
        Response response = testContext.getApiManager().getPostSigningKeys().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), responseCode, "Different response code being returned");
        if (getRestHelper().getErrorDescription(response) != null) {
            if (getRestHelper().getErrorDescription(response).contains("'")) {
            }
            Assert.assertTrue(
                    getRestHelper().getErrorDescription(response)
                            .replace("\"", "")
                            .contains(errorDesc),
                    "Different error description being returned..Expected: " + errorDesc + "Actual: " + getRestHelper().getErrorDescription(response));
        }
        Assert.assertEquals(getRestHelper().getErrorCode(response), errorCode, "Different error code being returned");
    }


    @Then("^error message should be \"([^\"]*)\" within the POST signing key response$")
    public void i_should_receive_a_error_message(String errorMessage) {
        Response response = testContext.getApiManager().getPostSigningKeys().getResponse();
        Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned..Expected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(response));

    }

    @Then("^the create signing key response should be successful$")
    public void validResponse() {
        Assert.assertEquals(
                HttpStatus.SC_CREATED,
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostSigningKeys().getResponse()),
                "Expected 201 but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostSigningKeys().getResponse())
        );
        Assert.assertNotNull(testContext.getApiManager().getPostSigningKeys().getResponse().path(Constants.PDF_PIN));
        Assert.assertNotNull(testContext.getApiManager().getPostSigningKeys().getResponse().path(Constants.PDF_URL));
        HashMap returnedObject = testContext.getApiManager().getPostSigningKeys().getResponse().path("signingKeyMetaData");
        if (returnedObject != null) {
            Assert.assertEquals(testContext.getApiManager().getPostSigningKeys().getApplicationId(), returnedObject.get(Constants.APPLICATION_ID), "Application Id didn't match");
            Assert.assertNotNull(returnedObject.get(Constants.ALG));
            Assert.assertNotNull(returnedObject.get(Constants.TYPE));
            Assert.assertNotNull(returnedObject.get(Constants.SIZE));
            Assert.assertNotNull(returnedObject.get(Constants.ACTIVATE_AT), "activate time is different as passed in input body");
            Assert.assertNotNull(returnedObject.get(Constants.DEACTIVATED_AT), "deActivated time is different as passed in input body");
            Assert.assertNotNull(returnedObject.get(Constants.ENTITY_STATUS));
            Assert.assertNotNull(returnedObject.get(Constants.CREATED_AT));
            Assert.assertNotNull(returnedObject.get(Constants.LAST_UPDATED_AT));
            Assert.assertEquals(returnedObject.size(), 10);
        } else {
            getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetSigningKey().getResponse());
        }
    }
}

