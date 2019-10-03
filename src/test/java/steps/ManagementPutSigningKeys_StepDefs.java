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


public class ManagementPutSigningKeys_StepDefs extends UtilManager {
    private TestContext testContext;
    private ManagementCommon common;
    private static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final Set<String> APPLICATION_ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";


    public ManagementPutSigningKeys_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(this.testContext);
    }

    @Given("^I am a PUT signing key authorized user$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPutSigningKeys().setAuthTokenWithBearer(token));
    }

    @And("^I make PUT signing key request with invalid applicationId \"([^\"]*)\" and valid keyId$")
    public void setInvalidApplicationIdAndValidKeyId(String applicationId) throws IOException {
        testContext.getApiManager().getPutSigningKeys().setApplicationId(applicationId);
        String url = getRestHelper().getBaseURI() + getFileHelper()
                .getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) +
                "/" + applicationId + "/keys/signing/" + testContext.getApiManager().getGetSigningKey().getKeyId();
        testContext.getApiManager().getPutSigningKeys().makePutSigningKeyApiCall(url);
    }


    @And("^I update the signing key with activate at \"([^\"]*)\", deactivate at \"([^\"]*)\" and entity status \"([^\"]*)\"$")
    public void updateBody(String activateAt, String deactivateAt, String entityStatus) {
        testContext.getApiManager().getPutSigningKeys().setActivateAt(activateAt);
        testContext.getApiManager().getPutSigningKeys().setDeactivateAt(deactivateAt);
        testContext.getApiManager().getPutSigningKeys().setEntityStatus(entityStatus);
    }

    @Then("^the PUT signing key response should have error status \"([^\"]*)\" with error code \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void errorResponse(String httpStatus, String errCode, String errDescription) {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutSigningKeys().getResponse()),
                Integer.parseInt(httpStatus),
                "Http status is expected to be " + httpStatus + " but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutSigningKeys().getResponse())
        );

        Assert.assertEquals(
                getRestHelper().getErrorCode(testContext.getApiManager().getPutSigningKeys().getResponse()),
                errCode,
                "Error code is expected to be " + errCode + " but got " +
                        getRestHelper().getErrorCode(testContext.getApiManager().getPutSigningKeys().getResponse())
        );

        if (!getRestHelper().getErrorDescription(testContext.getApiManager().getPutSigningKeys().getResponse()).contains(errDescription)) {
            Assert.assertEquals(
                    getRestHelper().getErrorDescription(testContext.getApiManager().getPutSigningKeys().getResponse()),
                    errDescription,
                    "Error description is expected to be " + errDescription + " but got " +
                            getRestHelper().getErrorDescription(testContext.getApiManager().getPutSigningKeys().getResponse())
            );
        }
    }

    @When("^I make a PUT request to the PUT signing key endpoint with \"([^\"]*)\" missing in the header$")
    public void i_make_a_put_request_to_the_signing_key_endpoint_with_key_missing_in_the_header(String keys) throws IOException {
        String url = getRestHelper().getBaseURI() + getFileHelper()
                .getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) +
                "/" + testContext.getApiManager().getGetSigningKey().getApplicationId() + "/keys/signing/" +
                testContext.getApiManager().getGetSigningKey().getKeyId();
        testContext.getApiManager().getPutSigningKeys().makeApiCallWithMissingHeader(url, keys);
    }

    @When("^I make PUT signing key request to endpoint$")
    public void makePutSigningKeyRequestToEndpoint() throws IOException {
        String url = getRestHelper().getBaseURI() + getFileHelper()
                .getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) +
                "/" + testContext.getApiManager().getGetSigningKey().getApplicationId() + "/keys/signing/" +
                testContext.getApiManager().getGetSigningKey().getKeyId();
        testContext.getApiManager().getPutSigningKeys().makePutSigningKeyApiCall(url);
    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" error code within the PUT signing key response$")
    public void i_should_receive_an_error_response_with_error_description_and_error_code(int responseCode, String errorDesc, String errorCode) {
        Response response = testContext.getApiManager().getPutSigningKeys().getResponse();
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

    @Then("^error message should be \"([^\"]*)\" within the PUT signing key response$")
    public void i_should_receive_a_error_message(String errorMessage) {
        Response response = testContext.getApiManager().getPutSigningKeys().getResponse();
        Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned..Expected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(response));
    }

    @Then("^the PUT signing key response should return success$")
    public void successResponse() {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutSigningKeys().getResponse()),
                HttpStatus.SC_OK,
                "Http status is expected to be " + 200 + " but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutSigningKeys().getResponse())
        );
        HashMap returnedTransactions = testContext.getApiManager().getPutSigningKeys().getResponse().path(".");
        Assert.assertEquals(returnedTransactions.get(Constants.APPLICATION_ID), testContext.getApiManager().getGetSigningKey().getApplicationId(), "ApplicationId didn't match!");
        Assert.assertEquals(returnedTransactions.get(Constants.KEY_ID), testContext.getApiManager().getGetSigningKey().getKeyId(), "keyId didn't match!");
        Assert.assertNotNull(returnedTransactions.get(Constants.ALG), "alg is null");
        Assert.assertNotNull(returnedTransactions.get(Constants.TYPE), "type is null");
        Assert.assertNotNull(returnedTransactions.get(Constants.SIZE), "size is null");
        Assert.assertNotNull(returnedTransactions.get(Constants.ACTIVATE_AT), "activateAt is null");
        Assert.assertNotNull(returnedTransactions.get(Constants.DEACTIVATE_AT), "deactivateAt is null");
        Assert.assertEquals(returnedTransactions.get(Constants.ENTITY_STATUS), testContext.getApiManager().getPutSigningKeys().getEntityStatus(), "deactivateAt is null");
        Assert.assertNotNull(returnedTransactions.get(Constants.CREATED_AT), "createdAt is null");
        Assert.assertNotNull(returnedTransactions.get(Constants.LAST_UPDATED_AT), "lastUpdatedAt is null");
        Assert.assertEquals(returnedTransactions.size(), 10, "response element didn't match!");
    }
}


