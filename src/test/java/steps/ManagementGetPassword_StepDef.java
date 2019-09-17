package steps;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Constants;
import java.util.*;

public class ManagementGetPassword_StepDef extends UtilManager {
    TestContext testContext;
    ManagementCommon common;
    Response applicationResponse;
    private static final Set<String> ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String VALID_BASE64_ENCODED_RSA_PUBLIC_KEY = "valid_base64_encoded_rsa_public_key";
    final static Logger logger = Logger.getLogger(ManagementGetPassword_StepDef.class);

    public ManagementGetPassword_StepDef(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }


    @Given("^I am logging in as a user with authorize Dragon user$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getGetPassword().setAuthToken(token));
    }


    @When("^I get the password request with newly created applicationId$")
    public void getPasswordRequest() {
        applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().getGetPassword().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        testContext.getApiManager().getGetPassword().setClientId(applicationResponse.getBody().path("application.clientId"));
        testContext.getApiManager().getGetPassword().setKeyId(applicationResponse.getBody().path("passwordMetadata.keyId"));
        testContext.getApiManager().getGetPassword().setEntityStatus(applicationResponse.getBody().path("passwordMetadata.entityStatus"));
        testContext.getApiManager().getGetPassword().setLastUpdatedAt(applicationResponse.getBody().path("application.lastUpdatedAt"));
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().getGetPassword().getApplicationId() + "/keys/passwords";
        testContext.getApiManager().getGetPassword().getPasswordRequest(url, testContext.getApiManager().getGetPassword().getAuthToken());
    }

    @When("^I get the password request with the same applicationId$")
    public void getPasswordRequestWithSameApplicationId() {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().getPostPasswordCreateClientPassword().getApplicationId() + "/keys/passwords";
        testContext.getApiManager().getGetPassword().getPasswordRequest(url, testContext.getApiManager().getGetPassword().getAuthToken());
    }

    @Then("^I should see the response from get password request$")
    public void getPasswordResponse() {
        Assert.assertEquals(testContext.getApiManager().getGetPassword().getResponse().statusCode(), HttpStatus.SC_OK);
        String returnedObject = testContext.getApiManager().getGetPassword().getResponse().getBody().prettyPrint();
        if (returnedObject != null) {
            String newString = "{\"response\":" + returnedObject + "}";
            Map<String, Object> retMap = new Gson().fromJson(
                    newString, new TypeToken<HashMap<String, Object>>() {
                    }.getType()
            );
            ArrayList<Map> arrayList = (ArrayList) retMap.get("response");
            Map firstElement = arrayList.get(0);
            Assert.assertEquals(firstElement.get(Constants.APPLICATION_ID), testContext.getApiManager().getGetPassword().getApplicationId(), "applicationId didn't match!");
            Assert.assertEquals(firstElement.get(Constants.CLIENT_ID), testContext.getApiManager().getGetPassword().getClientId(), "clientId didn't match!");
            Assert.assertEquals(firstElement.get(Constants.KEY_ID), testContext.getApiManager().getGetPassword().getKeyId(), "keyId didn't match!");
               Assert.assertNotNull(firstElement.get(Constants.LAST_UPDATED_AT),"lastUpdated date must be today");
            Assert.assertEquals(firstElement.get(Constants.ENTITY_STATUS), testContext.getApiManager().getGetPassword().getEntityStatus(), "entityStatus didn't match");
            Assert.assertNotNull(firstElement.get(Constants.ACTIVATE_AT), "activateAt must not be null");
            Assert.assertNotNull(firstElement.get(Constants.DEACTIVATE_AT), "deactivateAt must not be null");
            Assert.assertNotNull(firstElement.get(Constants.CREATED_AT),"createdAt must not be null");
            Assert.assertEquals(firstElement.size(), 8, "response is not as expected");
        }
    }

    @Then("^I should see the list of response from get password request$")
    public void getListOfPasswordResponse() {
        Assert.assertEquals(testContext.getApiManager().getGetPassword().getResponse().statusCode(), HttpStatus.SC_OK);
        String returnedObject = testContext.getApiManager().getGetPassword().getResponse().getBody().prettyPrint();
        if (returnedObject != null) {
            String newString = "{\"response\":" + returnedObject + "}";
            Map<String, Object> retMap = new Gson().fromJson(
                    newString, new TypeToken<HashMap<String, Object>>() {
                    }.getType()
            );
            ArrayList<Map> arrayList = (ArrayList) retMap.get("response");
            Assert.assertEquals(arrayList.size(), 2, "get password should return list of password metadata");
            Map firstElement = arrayList.get(0);
            Assert.assertEquals(firstElement.size(), 8,"password metadata didn't match");
        }
    }

    @When("^I get the password with null header \"([^\"]*)\"$")
    public void getPasswordRequestWithNullHeaderValues(String nullHeaderValues) {
        applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        testContext.getApiManager().getGetPassword().setApplicationId(applicationResponse.getBody().path("application.applicationId"));
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().getGetPassword().getApplicationId() + "/keys/passwords";
        testContext.getApiManager().getGetPassword().getPasswordWithNullHeaderValues(url, testContext.getApiManager().getGetPassword().getAuthToken(), nullHeaderValues);
    }

    @Then("^I GET request should have an error with status \"([^\"]*)\", error code as \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void failResponse(String httpStatus, String errorCode, String errorDescription) {
        Assert.assertEquals(
                Integer.parseInt(httpStatus),
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetPassword().getResponse()),
                "Expected http status " + httpStatus + " but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetPassword().getResponse())
        );
        Assert.assertEquals(
                errorCode,
                getRestHelper().getErrorCode(testContext.getApiManager().getGetPassword().getResponse()),
                "Expected error code " + errorCode + " but got " +
                        getRestHelper().getErrorCode(testContext.getApiManager().getGetPassword().getResponse())
        );
        if (!getRestHelper().getErrorDescription(testContext.getApiManager().getGetPassword().getResponse()).contains(errorDescription)) {
            Assert.assertEquals(
                    errorDescription,
                    getRestHelper().getErrorDescription(testContext.getApiManager().getGetPassword().getResponse()),
                    "Expected error description " + errorDescription + " but got " +
                            getRestHelper().getErrorDescription(testContext.getApiManager().getGetPassword().getResponse())
            );
        }
    }

    @Then("^error message should be \"([^\"]*)\" within the GET password response$")
    public void i_should_receive_a_error_message(String errorMessage) {
        Response response = testContext.getApiManager().getGetPassword().getResponse();
        Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned..Expected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(response));
    }

    @When("^I make the GET password request to the endpoint$")
    public void makeRequestToGetPasswordEndpoint() {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().getPostPasswordCreateClientPassword().getApplicationId() + "/keys/passwords";
        testContext.getApiManager().getGetPassword().getPasswordRequest(url, testContext.getApiManager().getGetPassword().getAuthToken());
    }


}