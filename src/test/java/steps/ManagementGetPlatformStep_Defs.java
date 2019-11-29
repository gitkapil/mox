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
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Constants;

import java.util.*;

public class ManagementGetPlatformStep_Defs extends UtilManager {
    TestContext testContext;
    ManagementCommon common;
    ArrayList<Map> arrayList;
    Response applicationResponse;
    Response response;
    String getPlatformURL;
    private static final Set<String> ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
        private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_platforms";

    final static Logger logger = Logger.getLogger(ManagementGetPlatformStep_Defs.class);

    public ManagementGetPlatformStep_Defs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }

    public void makeRequest(String url) {
        this.getPlatformURL = url;
        testContext.getApiManager().getGetPlatform().executeGetPlatformRequest(url);
    }

    @Given("^I am a GET platform authorized DRAGON user with Platform\\.ReadWrite\\.All$")
    public void i_am_a_GET_platform_authorized_DRAGON_user_with_Platform_ReadWrite_All() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getGetPlatform().setAuthToken(token));
    }

    @When("^I make a GET request to the platform endpoint$")
    public void i_make_a_GET_request_to_the_platform_endpoint() {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME);
        testContext.getApiManager().getGetPlatform().executeGetPlatformRequest(url);
    }

    @Then("^I should receive a successful GET platform response$")
    public void     i_should_receive_a_successful_GET_platform_response() {
        Assert.assertEquals(
                200
                , getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetPlatform().getResponse()));
    }

    @Then("^validate the response from GET platform API$")
    public void validate_the_response_from_GET_platform_API() {
        Assert.assertEquals(getListOfPlatforms().size(), 8, "response didn't match! ");
        Assert.assertNotNull(getListOfPlatforms().get(Constants.PLATFORM_ID), "platformId is not present!");
        Assert.assertNotNull(getListOfPlatforms().get(Constants.PLATFORM_NAME), "platformName is not present!");
        Assert.assertNotNull(getListOfPlatforms().get(Constants.STATUS), "status is not present!");
        Assert.assertNotNull(getListOfPlatforms().get(Constants.DESCRIPTION), "description is not present!");
        Assert.assertNotNull(getListOfPlatforms().get(Constants.CREATED_AT), "createAt is not present!");
        Assert.assertNotNull(getListOfPlatforms().get(Constants.LAST_UPDATED_AT), "lastUpdatedAt is not present!");
        Assert.assertNotNull(getListOfPlatforms().get(Constants.CREATED_BY), "organisationId is not present!");
        Assert.assertNotNull(getListOfPlatforms().get(Constants.UPDATED_BY), "organisationId is not present!");
    }

    @When("^I make a GET request to the Platform endpoint with \"([^\"]*)\" missing in the header$")
    public void i_make_a_GET_request_to_the_Platform_endpoint_with_missing_in_the_header(String keys) {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME);
        testContext.getApiManager().getGetPlatform().makeRequestWithMissingHeader(url, keys);
    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" error code within the GET platform response$")
    public void i_should_receive_a_error_response_with_error_description_and_error_code_within_the_GET_platform_response(String responseCode, String errorDesc, String errorCode) {
        Response response = testContext.getApiManager().getGetPlatform().getResponse();
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
        Assert.assertEquals(getRestHelper().getErrorCode(response), errorCode, "Different error code being returned");
    }


    @Then("^error message should be \"([^\"]*)\" within the GET platform response$")
    public void error_message_should_be_within_the_GET_platform_response(String errorMessage) {
        Response response = testContext.getApiManager().getGetPlatform().getResponse();
        org.testng.Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned..Expected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(response));



    }


    @Given("^I am a GET dragon DRAGON user with Platform\\.ReadWrite\\.All with invalid \"([^\"]*)\"$")
    public void i_am_a_GET_dragon_DRAGON_user_with_Platform_ReadWrite_All_with_invalid(String token) {
        common.iAmADragonUserWithToken(token, tokenArg -> testContext.getApiManager().getGetPlatform().setAuthTokenWithoutBearer(tokenArg));
    }


    @When("^I get a list of platform \"([^\"]*)\" using filter \"([^\"]*)\"$")
    public void i_get_a_list_of_platform_using_filters_to_filter_with(int noOfResponse, String filterName) {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME);
        String filterValue = null;
        if (filterName.equalsIgnoreCase("platformId")) {
            filterValue = getListOfPlatforms().get(Constants.PLATFORM_ID).toString();
        } else if (filterName.equalsIgnoreCase("platformName")) {
            filterValue = getListOfPlatforms().get(Constants.PLATFORM_NAME).toString();
        }
        url = url + "?" + filterName + "=" + filterValue;
        testContext.getApiManager().getGetPlatform().executeGetPlatformRequest(url);
        String responseString = testContext.getApiManager().getGetPlatform().getResponse().getBody().prettyPrint();
        Map<String, Object> retMap = new Gson().fromJson(
                responseString, new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );
       arrayList = (ArrayList) retMap.get(Constants.ITEM);
        Map firstElement = arrayList.get(0);
        Assert.assertEquals(arrayList.size(),1,"response is not same");

    }

    @When("^I get a list of platform using filter \"([^\"]*)\" and filter value \"([^\"]*)\"$")
    public void i_get_a_list_of_platform_using_filter(String filterName, String filterValue) {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME);
        url = url + "?" + filterName + "=" + filterValue;
        testContext.getApiManager().getGetPlatform().executeGetPlatformRequest(url);
        String responseString = testContext.getApiManager().getGetPlatform().getResponse().getBody().prettyPrint();
        Map<String, Object> retMap = new Gson().fromJson(
                responseString, new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );
        arrayList = (ArrayList) retMap.get(Constants.ITEM);
        if(arrayList.size()!=0) {
            Map firstItem = arrayList.get(0);
            Assert.assertEquals(firstItem.get(Constants.STATUS), "A", "platform is not active");
        } else if(arrayList.size()==1){
            Map secondItem = arrayList.get(1);
            Assert.assertEquals(secondItem.get(Constants.STATUS), "A", "platform is not active");
        } else if(arrayList.size()==2){
            Map thirdItem = arrayList.get(2);
            Assert.assertEquals(thirdItem.get(Constants.STATUS), "A", "platform is not active");
        }else if(arrayList.size()==3){
            Map fourthItem = arrayList.get(3);
            Assert.assertEquals(fourthItem.get(Constants.STATUS), "A", "platform is not active");
        }
    }

    @Then("^validate the all items list of platform should have active status$")
    public void validate_the_all_items_list_of_platform_should_have_active_status() {
        String responseString = testContext.getApiManager().getGetPlatform().getResponse().getBody().prettyPrint();
        Map<String, Object> retMap = new Gson().fromJson(
                responseString, new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );
       arrayList = (ArrayList) retMap.get(Constants.ITEM);
        if(arrayList.size()!=0) {
            Map firstItem = arrayList.get(0);
            Assert.assertEquals(firstItem.get(Constants.STATUS), "A", "platform is not active");
        } else if(arrayList.size()==1){
            Map secondItem = arrayList.get(1);
            Assert.assertEquals(secondItem.get(Constants.STATUS), "A", "platform is not active");
        } else if(arrayList.size()==2){
            Map thirdItem = arrayList.get(2);
            Assert.assertEquals(thirdItem.get(Constants.STATUS), "A", "platform is not active");
        }else if(arrayList.size()==3){
            Map fourthItem = arrayList.get(3);
            Assert.assertEquals(fourthItem.get(Constants.STATUS), "A", "platform is not active");
        }
    }


    @Then("^the response should have a list of \"([^\"]*)\" platform$")
    public void the_response_should_have_a_list_of_platform(int numberOfResponses) {
        List<Object> list = getRestHelper().getJsonArray(testContext.getApiManager().getGetPlatform().getResponse(), Constants.ITEM);
        Assert.assertEquals(list.size(), numberOfResponses, "no of response aren't same");

    }

    @When("^I get a list of platform using filters to filter \"([^\"]*)\" with \"([^\"]*)\" with (\\d+) limits$")
    public void i_get_a_list_of_platform_using_filters_to_filter_with_with_limits(String arg1, String arg2, int arg3) {

    }

    @Then("^I should receive a successful response for platform$")
    public void i_should_receive_a_successful_response_for_platform() {

    }

    @Then("^the response should have a list of \"([^\"]*)\" platform response$")
    public void the_response_should_have_a_list_of_platform_response(String arg1) {

    }

    @Then("^the response should have more than or equal to (\\d+) in total for platform$")
    public void the_response_should_have_more_than_or_equal_to_in_total_for_platform(int arg1) {

    }

    @Then("^the response should have a (\\d+) number of total items for platform$")
    public void the_response_should_have_a_number_of_total_items_for_platform(int arg1) {

    }

    @Then("^the response should be on page \"([^\"]*)\" for platform$")
    public void the_response_should_be_on_page_for_platform(String arg1) {

    }

    @When("^I move to page \"([^\"]*)\" for platform$")
    public void i_move_to_page_for_platform(String arg1) {

    }

    @Then("^the response should have a list of \"([^\"]*)\" for platform$")
    public void the_response_should_have_a_list_of_for_platform(String arg1) {

    }

    @Then("^the response should have more than or equal to \"([^\"]*)\" in total for platform$")
    public void the_response_should_have_more_than_or_equal_to_in_total_for_platform(String arg1) {

    }

    @Then("^the response should have a \"([^\"]*)\" number of total items for platform$")
    public void the_response_should_have_a_number_of_total_items_for_platform(String arg1) {

    }

    public Map getListOfPlatforms() {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME);
        testContext.getApiManager().getGetPlatform().executeGetPlatformRequest(url);
        String platformResponse = testContext.getApiManager().getGetPlatform().getResponse().getBody().prettyPrint();
        Map<String, Object> retMap = new Gson().fromJson(
                platformResponse, new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );
        ArrayList<Map> arrayList = (ArrayList) retMap.get(Constants.ITEM);
        Map firstElement = arrayList.get(0);
        return firstElement;
    }

    @When("^I make a GET request to the platform endpoint with platformId of created platform$")
    public void iMakeAGETRequestToThePlatformEndpointWithPlatformIdOfCreatedPlatform() {
        String url = getRestHelper().getBaseURI() + "platforms?platformId=" + testContext.getApiManager().postPlatform().getPlatformId();
        System.out.println("get platform url: " + url);
        makeRequest(url);


        String responseString = testContext.getApiManager().getGetPlatform().getResponse().getBody().prettyPrint();
        Map<String, Object> retMap = new Gson().fromJson(
                responseString, new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );
        ArrayList<Map> arrayList = (ArrayList) retMap.get(Constants.ITEM);
        Map firstElement = arrayList.get(0);

        //Setting platformId, platformName, status and description for validating response with POST Platform API
        testContext.getApiManager().getGetPlatform().setPlatformId(firstElement.get(Constants.PLATFORM_ID).toString());
        testContext.getApiManager().getGetPlatform().setPlatformName(firstElement.get(Constants.PLATFORM_NAME).toString());
        testContext.getApiManager().getGetPlatform().setPlatformStatus(firstElement.get(Constants.STATUS).toString());
        testContext.getApiManager().getGetPlatform().setPlatformDescription(firstElement.get(Constants.DESCRIPTION).toString());

    }
}