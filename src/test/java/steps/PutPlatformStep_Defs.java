package steps;

import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PutPlatformStep_Defs extends UtilManager {
    private static final Set<String> ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final Set<String> INCORRECT_ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_platforms";

    TestContext testContext;
    ManagementCommon common;

    Response response;

    final static Logger logger = Logger.getLogger(PutPlatformStep_Defs.class);

    public PutPlatformStep_Defs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }


    @Given("^I am a PUT platform authorized DRAGON user with Platform\\.ReadWrite\\.All$")
    public void i_am_a_PUT_platform_authorized_DRAGON_user_with_Platform_ReadWrite_All() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPutPlatform().setAuthTokenWithBearer(token));
    }

    @Given("I have set the platformId from existing get platform request$")
    public void setThePlatformIdFromExistingGetPlatformRequest() {
        Map platformResponse = new ManagementGetPlatformStep_Defs(testContext).getListOfPlatforms();
        testContext.getApiManager().getGetPlatform().setPlatformId(platformResponse.get(Constants.PLATFORM_ID).toString());
        testContext.getApiManager().getGetPlatform().setPlatformDescription(platformResponse.get(Constants.PLATFORM_DESCRIPTION).toString());
        testContext.getApiManager().getGetPlatform().setPlatformStatus(platformResponse.get(Constants.PLATFORM_STATUS).toString());
        testContext.getApiManager().getGetPlatform().setPlatformName(platformResponse.get(Constants.PLATFORM_NAME).toString());
    }


    @Given("^I have set \"([^\"]*)\", \"([^\"]*)\" and platform \"([^\"]*)\"for PUT platform$")
    public void i_have_set_and_platform_for_PUT_platform(String platformName, String platformDescription, String platformStatus) {
        testContext.getApiManager().getPutPlatform().setPlatformName(platformName);
        testContext.getApiManager().getPutPlatform().setPlatformDescription(platformDescription);
        testContext.getApiManager().getPutPlatform().setPlatformStatus(platformStatus);
    }

    @When("^I make a PUT request to the PUT platform endpoint$")
    public void i_make_a_PUT_request_to_the_PUT_platform_endpoint() {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) + "/" + testContext.getApiManager().getGetPlatform().getPlatformId();
        testContext.getApiManager().getPutPlatform().makeRequest(url);
    }

    @When("^I make a PUT request to the PUT platform endpoint with missing body \"([^\"]*)\"$")
    public void i_make_a_PUT_request_to_the_PUT_platform_endpointWithMissingOptionBody(String missingBody) {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) + "/" + testContext.getApiManager().getGetPlatform().getPlatformId();
        testContext.getApiManager().getPutPlatform().makeRequest(url, missingBody);
    }

    @When("^I make a PUT request to the PUT platform endpoint with only one field \"([^\"]*)\"$")
    public void i_make_a_PUT_request_to_the_PUT_platform_endpointWitOnlyOneBodyField(String onlyBodyField) {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) + "/" + testContext.getApiManager().getGetPlatform().getPlatformId();
        testContext.getApiManager().getPutPlatform().makeRequestWithOneFiled(url, onlyBodyField);
    }

    @Then("^I should receive a successful PUT platform response$")
    public void i_should_receive_a_successful_PUT_platform_response() {
        response = testContext.getApiManager().getPutPlatform().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), 200, "Request was not successful! \n Actual Status: " + getRestHelper().getResponseStatusCode(response) + "Expected Status: 200");
        Assert.assertEquals(getRestHelper().getResponseHeaderValue(response, "X-Application-Context "), null, "Expects X-Application-Context header to not exists");
        Assert.assertNotNull(response, "The response for Post Platform Request was null");
    }

    @Then("^validate the PUT platform response$")
    public void validate_the_PUT_platform_response() {
        HashMap response = testContext.getApiManager().getPutPlatform().getResponse().path(".");
        Assert.assertEquals(response.get(Constants.PLATFORM_ID), testContext.getApiManager().getGetPlatform().getPlatformId(), "platformId didn't match");
        Assert.assertEquals(response.get(Constants.PLATFORM_NAME), testContext.getApiManager().getPutPlatform().getPlatformName().toUpperCase(), "platformName didn't match");
        Assert.assertEquals(response.get(Constants.PLATFORM_DESCRIPTION), testContext.getApiManager().getPutPlatform().getPlatformDescription(), "platformDescription didn't match");
        Assert.assertEquals(response.get(Constants.PLATFORM_STATUS), testContext.getApiManager().getPutPlatform().getPlatformStatus(), "platform status didn't match");
        Assert.assertNotNull(response.get(Constants.CREATED_AT), "createAt shouldn't be null");
        Assert.assertNotNull(response.get(Constants.LAST_UPDATED_AT), "lastUpdatedAt shouldn't be null");
        Assert.assertNotNull(response.get(Constants.CREATED_BY), "createBy shouldn't be null");
        Assert.assertNotNull(response.get(Constants.UPDATED_BY), "updatedBy shouldn't be null");
        Assert.assertEquals(response.size(), 8, "response size is not same");
    }

    @Then("^validate the PUT platform response with space \"([^\"]*)\"$")
    public void validate_the_PUT_platform_responseWithSpace(String space) {
        HashMap response = testContext.getApiManager().getPutPlatform().getResponse().path(".");
        Assert.assertEquals(response.get(Constants.PLATFORM_ID), testContext.getApiManager().getGetPlatform().getPlatformId(), "platformId didn't match");
        if (space.equalsIgnoreCase("platformName")) {
            Assert.assertEquals(response.get(Constants.PLATFORM_NAME), testContext.getApiManager().getGetPlatform().getPlatformName().toUpperCase(), "platformName didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_DESCRIPTION), testContext.getApiManager().getPutPlatform().getPlatformDescription(), "platformDescription didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_STATUS), testContext.getApiManager().getPutPlatform().getPlatformStatus(), "platform status didn't match");
        } else if (space.equalsIgnoreCase("description")) {
            Assert.assertEquals(response.get(Constants.PLATFORM_DESCRIPTION), testContext.getApiManager().getGetPlatform().getPlatformDescription(), "platform description didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_NAME), testContext.getApiManager().getPutPlatform().getPlatformName().toUpperCase(), "platform name didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_STATUS), testContext.getApiManager().getPutPlatform().getPlatformStatus(), "platform status didn't match");
        } else if (space.equalsIgnoreCase("status")) {
            Assert.assertEquals(response.get(Constants.PLATFORM_STATUS), testContext.getApiManager().getGetPlatform().getPlatformStatus(), "platform status didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_NAME), testContext.getApiManager().getPutPlatform().getPlatformName().toUpperCase(), "platform name didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_DESCRIPTION), testContext.getApiManager().getPutPlatform().getPlatformDescription(), "platform description didn't match");
        }
        Assert.assertNotNull(response.get(Constants.CREATED_AT), "createAt shouldn't be null");
        Assert.assertNotNull(response.get(Constants.LAST_UPDATED_AT), "lastUpdatedAt shouldn't be null");
        Assert.assertNotNull(response.get(Constants.CREATED_BY), "createBy shouldn't be null");
        Assert.assertNotNull(response.get(Constants.UPDATED_BY), "updatedBy shouldn't be null");
        Assert.assertEquals(response.size(), 8, "response size is not same");
    }

    @Then("^validate the PUT platform response with empty space \"([^\"]*)\"$")
    public void validate_the_PUT_platform_responseWithEmptySpace(String emptySpace) {
        HashMap response = testContext.getApiManager().getPutPlatform().getResponse().path(".");
        Assert.assertEquals(response.get(Constants.PLATFORM_ID), testContext.getApiManager().getGetPlatform().getPlatformId(), "platformId didn't match");
        if (emptySpace.equalsIgnoreCase("platformName")) {
            Assert.assertEquals(response.get(Constants.PLATFORM_NAME), testContext.getApiManager().getGetPlatform().getPlatformName().toUpperCase(), "platformName didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_DESCRIPTION), testContext.getApiManager().getPutPlatform().getPlatformDescription(), "platformDescription didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_STATUS), testContext.getApiManager().getPutPlatform().getPlatformStatus(), "platform status didn't match");
        } else if (emptySpace.equalsIgnoreCase("description")) {
            Assert.assertEquals(response.get(Constants.PLATFORM_DESCRIPTION), testContext.getApiManager().getGetPlatform().getPlatformDescription(), "platform description didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_NAME), testContext.getApiManager().getPutPlatform().getPlatformName().toUpperCase(), "platform name didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_STATUS), testContext.getApiManager().getPutPlatform().getPlatformStatus(), "platform status didn't match");
        } else if (emptySpace.equalsIgnoreCase("status")) {
            Assert.assertEquals(response.get(Constants.PLATFORM_STATUS), testContext.getApiManager().getGetPlatform().getPlatformStatus(), "platform status didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_NAME), testContext.getApiManager().getPutPlatform().getPlatformName().toUpperCase(), "platform name didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_DESCRIPTION), testContext.getApiManager().getPutPlatform().getPlatformDescription(), "platform description didn't match");
        }
        Assert.assertNotNull(response.get(Constants.CREATED_AT), "createAt shouldn't be null");
        Assert.assertNotNull(response.get(Constants.LAST_UPDATED_AT), "lastUpdatedAt shouldn't be null");
        Assert.assertNotNull(response.get(Constants.CREATED_BY), "createBy shouldn't be null");
        Assert.assertNotNull(response.get(Constants.UPDATED_BY), "updatedBy shouldn't be null");
        Assert.assertEquals(response.size(), 8, "response size is not same");
    }


    @Then("^validate the PUT platform response with missing body \"([^\"]*)\"$")
    public void validate_the_PUT_platform_responseWithMissingBody(String missingBody) {
        HashMap response = testContext.getApiManager().getPutPlatform().getResponse().path(".");
        Assert.assertEquals(response.get(Constants.PLATFORM_ID), testContext.getApiManager().getGetPlatform().getPlatformId(), "platformId didn't match");
        if (missingBody.equalsIgnoreCase("platformName")) {
            Assert.assertEquals(response.get(Constants.PLATFORM_NAME), testContext.getApiManager().getGetPlatform().getPlatformName().toUpperCase(), "platformName didn't match");
        } else {
            Assert.assertEquals(response.get(Constants.PLATFORM_NAME), testContext.getApiManager().getPutPlatform().getPlatformName().toUpperCase(), "platformName didn't match");
        }
        if (missingBody.equalsIgnoreCase("description")) {
            Assert.assertEquals(response.get(Constants.PLATFORM_DESCRIPTION), testContext.getApiManager().getGetPlatform().getPlatformDescription(), "platformDescription didn't match");
        } else {
            Assert.assertEquals(response.get(Constants.PLATFORM_DESCRIPTION), testContext.getApiManager().getPutPlatform().getPlatformDescription(), "platformDescription didn't match");
        }
        if (missingBody.equalsIgnoreCase("status")) {
            Assert.assertEquals(response.get(Constants.PLATFORM_STATUS), testContext.getApiManager().getGetPlatform().getPlatformStatus(), "platform status didn't match");
        } else {
            Assert.assertEquals(response.get(Constants.PLATFORM_STATUS), testContext.getApiManager().getPutPlatform().getPlatformStatus(), "platform status didn't match");
        }
        Assert.assertNotNull(response.get(Constants.CREATED_AT), "createAt shouldn't be null");
        Assert.assertNotNull(response.get(Constants.LAST_UPDATED_AT), "lastUpdatedAt shouldn't be null");
        Assert.assertNotNull(response.get(Constants.CREATED_BY), "createBy shouldn't be null");
        Assert.assertNotNull(response.get(Constants.UPDATED_BY), "updatedBy shouldn't be null");
        Assert.assertEquals(response.size(), 8, "response size is not same");
    }

    @Then("^validate the PUT platform response with only one body input \"([^\"]*)\"$")
    public void validate_the_PUT_platform_responseWithOneInputgBody(String oneInputBody) {
        HashMap response = testContext.getApiManager().getPutPlatform().getResponse().path(".");
        Assert.assertEquals(response.get(Constants.PLATFORM_ID), testContext.getApiManager().getGetPlatform().getPlatformId(), "platformId didn't match");
        if (oneInputBody.equalsIgnoreCase("platformName")) {
            Assert.assertEquals(response.get(Constants.PLATFORM_NAME), testContext.getApiManager().getPutPlatform().getPlatformName().toUpperCase(), "platformName didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_DESCRIPTION), testContext.getApiManager().getGetPlatform().getPlatformDescription(), "platformDescription didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_STATUS), testContext.getApiManager().getGetPlatform().getPlatformStatus(), "platform status didn't match");
        } else if (oneInputBody.equalsIgnoreCase("description")) {
            Assert.assertEquals(response.get(Constants.PLATFORM_DESCRIPTION), testContext.getApiManager().getPutPlatform().getPlatformDescription(), "platform description didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_NAME), testContext.getApiManager().getGetPlatform().getPlatformName().toUpperCase(), "platform name didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_STATUS), testContext.getApiManager().getGetPlatform().getPlatformStatus(), "platform status didn't match");
        } else if (oneInputBody.equalsIgnoreCase("status")) {
            Assert.assertEquals(response.get(Constants.PLATFORM_STATUS), testContext.getApiManager().getPutPlatform().getPlatformStatus(), "platform status didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_NAME), testContext.getApiManager().getGetPlatform().getPlatformName().toUpperCase(), "platform name didn't match");
            Assert.assertEquals(response.get(Constants.PLATFORM_DESCRIPTION), testContext.getApiManager().getGetPlatform().getPlatformDescription(), "platform description didn't match");
        }
        Assert.assertNotNull(response.get(Constants.CREATED_AT), "createAt shouldn't be null");
        Assert.assertNotNull(response.get(Constants.LAST_UPDATED_AT), "lastUpdatedAt shouldn't be null");
        Assert.assertNotNull(response.get(Constants.CREATED_BY), "createBy shouldn't be null");
        Assert.assertNotNull(response.get(Constants.UPDATED_BY), "updatedBy shouldn't be null");
        Assert.assertEquals(response.size(), 8, "response size is not same");
    }


    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" error code within the PUT platform response$")
    public void i_should_receive_a_error_response_with_error_description_and_error_code_within_the_PUT_platform_response(String responseCode, String errorDesc, String arg3) {
        Response response = testContext.getApiManager().getPutPlatform().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), Integer.parseInt(responseCode), "Different response code being returned");
        if (getRestHelper().getErrorDescription(response) != null) {
            if (getRestHelper().getErrorDescription(response).contains("'")) {
            }
            Assert.assertTrue(
                    getRestHelper().getErrorDescription(response)
                            .replace("\"", "")
                            .contains(errorDesc),
                    "Different error description being returned..Expected: " + errorDesc + "Actual: " + getRestHelper().getErrorDescription(response));
        }
        Assert.assertEquals(getRestHelper().getErrorCode(response), arg3, "Different error code being returned");
    }


    @Then("^error message should be \"([^\"]*)\" within the PUT platform response$")
    public void error_message_should_be_within_the_PUT_platform_response(String errorMessage) {
        Response response = testContext.getApiManager().getPutPlatform().getResponse();
        org.testng.Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned..Expected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(response));


    }

    @When("^I make a PUT request to the Platform endpoint with \"([^\"]*)\" missing in the header$")
    public void i_make_a_PUT_request_to_the_Platform_endpoint_with_missing_in_the_header(String key) {
        Map platformResponse = new ManagementGetPlatformStep_Defs(testContext).getListOfPlatforms();
        testContext.getApiManager().getGetPlatform().setPlatformId(platformResponse.get(Constants.PLATFORM_ID).toString());
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) + "/" + testContext.getApiManager().getGetPlatform().getPlatformId();
        testContext.getApiManager().getPutPlatform().makeRequestWithMissingHeader(url, key);
    }


    @Given("^I am a PUT dragon DRAGON user with Platform\\.ReadWrite\\.All with invalid \"([^\"]*)\"$")
    public void i_am_a_PUT_dragon_DRAGON_user_with_Platform_ReadWrite_All_with_invalid(String token) {
        common.iAmADragonUserWithToken(token, tokenArg -> testContext.getApiManager().getPutPlatform().setAuthTokenWithoutBearer(tokenArg));
    }


}
