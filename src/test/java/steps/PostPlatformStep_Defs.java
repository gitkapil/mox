package steps;

import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Constants;
import utils.DataBaseConnector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PostPlatformStep_Defs extends UtilManager {
    // NB: These are the dragon token (for testing) roles {"roles": ["Application.ReadWrite.All"]}.  CSO tokens use claim {"role": "user"}
    private static final Set<String> ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final Set<String> INCORRECT_ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String SIG_HEADER_LIST_POST_APPLICATION = "header-list-post-application";
    public static String platformID;
    TestContext testContext;
    ManagementCommon common;

    Response response;
    String platformUrl = null;

    final static Logger logger = Logger.getLogger(PostPlatformStep_Defs.class);

    public PostPlatformStep_Defs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }


    @Given("^I am a POST platform authorized DRAGON user with Platform\\.ReadWrite\\.All$")
    public void i_am_a_post_platform_authorized_DRAGON_user_with_Platform_ReadWrite_All() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().postPlatform().setAuthTokenWithBearer(token));
    }

    public void makeRequest() {
        String url = getRestHelper().getBaseURI() + "platforms";
        this.platformUrl = url;
        testContext.getApiManager().postPlatform().makeRequest(url);
    }

    public void makeInvalidRequest(String key, String invalidValue) {
        String url = getRestHelper().getBaseURI() + "platforms";
        this.platformUrl = url;
        testContext.getApiManager().postPlatform().makeInvalidPOSTPlatformRequest(url, key, invalidValue);
    }

    @When("^I make request for POST platform API with \"([^\"]*)\" platformName and \"([^\"]*)\" platformDescription in request body$")
    public void iMakeRequestForPOSTPlatformAPIWithPlatformNameAndPlatformDescriptionInRequestBody(String platformName, String platformDescription) {
        testContext.getApiManager().postPlatform().setPlatformName(platformName);
        testContext.getApiManager().postPlatform().setPlatformDescription(platformDescription);
        testContext.getApiManager().postPlatform().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().postPlatform().setTraceId(getGeneral().generateUniqueUUID());
        makeRequest();
    }

    @Then("^I should receive a successful POST platform response$")
    public void i_should_receive_a_successful_POST_platform_response() {
        response = testContext.getApiManager().postPlatform().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), 200, "Request was not successful! \n Actual Status: " + getRestHelper().getResponseStatusCode(response) + "Expected Status: 200");
        Assert.assertEquals(getRestHelper().getResponseHeaderValue(response, "X-Application-Context "), null, "Expects X-Application-Context header to not exists");
        Assert.assertNotNull(response, "The response for Post Platform Request was null");
    }

    @Then("^validate the post platform response$")
    public void validate_the_post_platform_response() {
        Assert.assertNotNull(response.path(Constants.PLATFORM_ID), "platformId cannot be null");
        Assert.assertNotNull(response.path(Constants.PLATFORM_NAME), "platformName cannot be null");
        Assert.assertEquals(response.path(Constants.PLATFORM_NAME), testContext.getApiManager().postPlatform().getPlatformName().toUpperCase(), "response platformName should be equal to request platformName!");
        Assert.assertEquals(response.path(Constants.PLATFORM_STATUS), "A", "New Platform Created should always be in A (ACTIVE) state!!");
        Assert.assertNotNull(response.path(Constants.PLATFORM_DESCRIPTION), "description cannot be null");
        Assert.assertEquals(response.path(Constants.PLATFORM_DESCRIPTION), testContext.getApiManager().postPlatform().getPlatformDescription(), "response platform description should be equal to request platform description!");

        //Setting platformId, status, description from POST response for query parameter in GET Platform API
        testContext.getApiManager().postPlatform().setPlatformId(response.path(Constants.PLATFORM_ID));
        testContext.getApiManager().postPlatform().setPlatformStatus(response.path(Constants.PLATFORM_STATUS));
    }


    @Given("^I have set \"([^\"]*)\", \"([^\"]*)\" for post platform with missing body \"([^\"]*)\" value$")
    public void i_have_set_for_post_platform_with_missing_body_value(String arg1, String arg2, String arg3) {

    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode within the POST platform response$")
    public void i_should_receive_a_error_response_with_error_description_and_errorcode_within_the_POST_platform_response(String arg1, String arg2, String arg3) {

    }

    @When("^I make a POST request to the Platform endpoint with \"([^\"]*)\" missing in the header$")
    public void i_make_a_POST_request_to_the_Platform_endpoint_with_missing_in_the_header(String key) {
        testContext.getApiManager().postPlatform().executeRequestWithMissingHeaderKeys(getRestHelper().getBaseURI() + "platforms", key, testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                Sets.newHashSet(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, SIG_HEADER_LIST_POST_APPLICATION).split(",")));
    }

    @Then("^I should receive \"([^\"]*)\" status code in response$")
    public void iShouldReceiveStatusCodeInResponse(int statusCode) {
        Response response = testContext.getApiManager().postPlatform().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), statusCode, "Different statusCode being returned");
    }

    @When("^error message should be \"([^\"]*)\" within the POST platform response$")
    public void error_message_should_be_within_the_POST_platform_response(String errorMessage) {
        Response response = testContext.getApiManager().postPlatform().getResponse();
        Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned. \nExpected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(response));
    }

    @Given("^I am a POST dragon DRAGON user with Platform\\.ReadWrite\\.All with invalid \"([^\"]*)\"$")
    public void i_am_a_POST_dragon_DRAGON_user_with_Platform_ReadWrite_All_with_invalid(String arg1) {

    }


    @Then("^I should receive \"([^\"]*)\" error status with \"([^\"]*)\" error description and \"([^\"]*)\" errorcode in response$")
    public void iShouldReceiveErrorStatusWithErrorDescriptionAndErrorcodeInResponse(int responseCode, String errorDesc, String errorCode) {
        Response response = testContext.getApiManager().postPlatform().getResponse();
        Assert.assertEquals(getRestHelper().getResponseStatusCode(response), responseCode, "Different response code returned. Expected = " + responseCode + "Actual = " + getRestHelper().getResponseStatusCode(response));

        if (getRestHelper().getErrorDescription(response) != null) {
            if (getRestHelper().getErrorDescription(response).contains("'")) {
                System.out.println("Actual API Response: " + getRestHelper().getErrorDescription(response));
                System.out.println("Expected: " + errorDesc);
            }
            Assert.assertTrue(
                    getRestHelper().getErrorDescription(response)
                            .replace("\"", "")
                            .contains(errorDesc),
                    "Different error description being returned..Expected: " + errorDesc + "Actual: " + getRestHelper().getErrorDescription(response));
        }
        Assert.assertEquals(getRestHelper().getErrorCode(response), errorCode, "Different error code being returned");

    }

    @And("^error message should be \"([^\"]*)\" in the response$")
    public void errorMessageShouldBeInTheResponse(String errorMessage) {
        Response response = testContext.getApiManager().postPlatform().getResponse();
        Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message returned! \nExpected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(response));
    }


    @When("^I provide \"([^\"]*)\" platformName and \"([^\"]*)\" platformDescription in request body with invalid key \"([^\"]*)\" for \"([^\"]*)\" in header$")
    public void iProvidePlatformNameAndPlatformDescriptionInRequestBodyWithInvalidKeyForInHeader(String platformName, String platformDescription, String invalidValue, String key) throws Throwable {
        testContext.getApiManager().postPlatform().setPlatformName(platformName);
        testContext.getApiManager().postPlatform().setPlatformDescription(platformDescription);
        testContext.getApiManager().postPlatform().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().postPlatform().setTraceId(getGeneral().generateUniqueUUID());

        makeInvalidRequest(key, invalidValue);
    }

    @When("^I make a POST request to the post platform endpoint$")
    public void iMakeAPOSTRequestToThePostPlatformEndpoint() {
        logger.info("********** Executing POST Platform Request ***********");

        testContext.getApiManager().postPlatform().executeRequest(
                getRestHelper().getBaseURI() + "platforms",
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                Sets.newHashSet(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        SIG_HEADER_LIST_POST_APPLICATION).split(",")));

    }

    @And("^validate the response from POST Platform API is present in GET platform API$")
    public void validateTheResponseFromPOSTPlatformAPIIsPresentInGETPlatformAPI() {
        System.out.println(testContext.getApiManager().postPlatform().getPlatformName().toUpperCase());
        System.out.println(testContext.getApiManager().getGetPlatform().getPlatformName());

        //Validate platform response details
        Assert.assertEquals(testContext.getApiManager().postPlatform().getPlatformId(), testContext.getApiManager().getGetPlatform().getPlatformId(), "platformId from POST Platform API response doesn't exist in GET Platform API !");
        Assert.assertEquals(testContext.getApiManager().postPlatform().getPlatformName().toUpperCase(), testContext.getApiManager().getGetPlatform().getPlatformName(), "platformName from POST Platform API response doesn't exist in GET Platform API !");
        Assert.assertEquals(testContext.getApiManager().postPlatform().getPlatformStatus(), testContext.getApiManager().getGetPlatform().getPlatformStatus(), "status from POST Platform API response doesn't exist in GET Platform API !");
        Assert.assertEquals(testContext.getApiManager().postPlatform().getPlatformDescription(), testContext.getApiManager().getGetPlatform().getPlatformDescription(), "description from POST Platform API response doesn't exist in GET Platform API !");

    }
}

