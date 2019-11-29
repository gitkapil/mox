package steps;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ManagementGetApplications_StepDefs extends UtilManager {
    TestContext testContext;
    Response response;
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";

    public ManagementGetApplications_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }

    private String currentUrl;
    ManagementCommon common;
    private static final Set<String> ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final Set<String> INCORRECT_ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    final static Logger logger = Logger.getLogger(ManagementGetApplications_StepDefs.class);

    @Given("^I am a GET application authorized DRAGON user with the Application.ReadWrite.All privilege$")
    public void i_am_an_authorized_DRAGON_user_with_role() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getGetApplication().setAuthToken(token));
    }

    @When("^I get a list of applications without any filters$")
    public void list_of_applications_without_any_filters() {
        String url =getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource");
        System.out.println("URLs: "+ url);
        testContext.getApiManager().getGetApplication().getListOfApplications(
                getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource"),
                testContext.getApiManager().getGetApplication().getAuthToken()
        );
    }

    @When("^I create new application$")
    public void createNewApplication() {
        response = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
    }

    @When("^I get the application details of newly created application using filter \"([^\"]*)\"$")
    public void getTheApplicationDetails(String filterName) {
        String clientId = response.getBody().path("application.clientId");
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource");

        url = url + "?" + filterName + "=" + clientId;
        testContext.getApiManager().getGetApplication().getListOfApplications(
                url,
                testContext.getApiManager().getGetApplication().getAuthToken());
    }


    @Then("^I should receive a successful response$")
    public void successful_response() {
        Assert.assertEquals(
                HttpStatus.SC_OK
                , getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetApplication().getResponse()));
    }

    @And("^the response should have a list of ([^\"]*) applications$")
    public void should_have_number_of_applications(int numberOfResponses) {
        Assert.assertTrue(
                "The response should have a list of " + numberOfResponses + " applications. but found " +
                        getRestHelper().getJsonArray(testContext.getApiManager().getGetApplication().getResponse(), "items").size(),
                getRestHelper().getJsonArray(testContext.getApiManager().getGetApplication().getResponse(), "items")
                        .size() == numberOfResponses
        );
    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" errorCode within the get application response$")
    public void i_should_receive_an_error_response_with_error_description_and_errorcode(int responseCode, String errorDesc, String errorCode) {
        Response response = testContext.getApiManager().getGetApplication().getResponse();
        org.testng.Assert.assertEquals(getRestHelper().getResponseStatusCode(response), responseCode, "Different response code being returned");
        if (getRestHelper().getErrorDescription(response) != null) {
            if (getRestHelper().getErrorDescription(response).contains("'")) {
            }
            org.testng.Assert.assertTrue(
                    getRestHelper().getErrorDescription(response)
                            .replace("\"", "")
                            .contains(errorDesc),
                    "Different error description being returned..Expected: " + errorDesc + "Actual: " + getRestHelper().getErrorDescription(response));
        }
        org.testng.Assert.assertEquals(getRestHelper().getErrorCode(response), errorCode, "Different error code being returned");
    }

    @Then("^error message should be \"([^\"]*)\" within the get application response$")
    public void i_should_receive_a_error_message(String errorMessage) {
        Response response = testContext.getApiManager().getGetApplication().getResponse();
        org.testng.Assert.assertTrue(
                getRestHelper().getErrorMessage(response).contains(errorMessage),
                "Different error message being returned..Expected: " + errorMessage + " Actual: " +
                        getRestHelper().getErrorMessage(response));
    }

    @Then("^I should get an error message with status ([^\"]*) error code \"([^\"]*)\" and error description \"([^\"]*)\"$")
    public void then_should_get_error(int httpStatus, String errorCode, String errorDescription) {
        org.testng.Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetApplication().getResponse()),
                httpStatus, "Different response code being returned");

        org.testng.Assert.assertEquals(getRestHelper().getErrorCode(
                testContext.getApiManager().getGetApplication().getResponse()),
                errorCode, "Different error code being returned");

        org.testng.Assert.assertTrue(
                getRestHelper().getErrorDescription(
                        testContext.getApiManager().getGetApplication().getResponse()).contains(errorDescription),
                "Different error description being returned..Expected: " + errorDescription
                        + "  Actual: " +
                        getRestHelper().getErrorDescription(testContext.getApiManager().getGetApplication().getResponse()));
    }

    @When("^I get a list of application using multiple filters$")
    public void when_i_get_list_of_application_using_multi_filters() {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource");

        url = url + "?peakId=00&clientId=11";
        testContext.getApiManager().getGetApplication().getListOfApplications(
                url,
                testContext.getApiManager().getGetApplication().getAuthToken());
    }

    @When("^I get a list of application using multiple filters with correct uuids$")
    public void when_i_get_list_of_application_using_multi_filters_with_uuid() {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource");

        url = url + "?peakId=00000001-0000-0000-0000-000000000000&clientId=00000001-0000-0000-0000-000000000000";
        testContext.getApiManager().getGetApplication().getListOfApplications(
                url,
                testContext.getApiManager().getGetApplication().getAuthToken());
    }

    @And("^the response should have more than or equal to ([^\"]*) in total$")
    public void should_have_more_than_responses_in_total(int numberOfResponses) {
        Assert.assertTrue(
                "The response should have more than or equal to " + numberOfResponses + " in total. But got " +
                        getRestHelper().getJsonArray(testContext.getApiManager().getGetApplication().getResponse(), "items")
                                .size(),
                getRestHelper().getJsonArray(testContext.getApiManager().getGetApplication().getResponse(), "items")
                        .size() <= numberOfResponses
        );
    }

    @And("^validate the item list from the response$")
    public void validateItemListFomTheResponse() {
        String responseString = testContext.getApiManager().getGetApplication().getResponse().getBody().prettyPrint();
        Map<String, Object> retMap = new Gson().fromJson(
                responseString, new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );
        ArrayList<Map> arrayList = (ArrayList) retMap.get(Constants.ITEM);
            Map firstItem = arrayList.get(0);

        if (arrayList.size()!= 0) {
            Assert.assertEquals(firstItem.size(),11);
            Assert.assertNotNull(firstItem.get(Constants.APPLICATION_ID));
            Assert.assertNotNull(firstItem.get(Constants.CLIENT_ID));
            Assert.assertNotNull(firstItem.get(Constants.PEAK_ID));
            Assert.assertNotNull(firstItem.get(Constants.SUB_UNIT_ID));
            Assert.assertNotNull(firstItem.get(Constants.ORGANISATION_ID));
            Assert.assertNotNull(firstItem.get(Constants.PLATFORM_ID));
            Assert.assertNotNull(firstItem.get(Constants.PLATFORM_NAME));
            Assert.assertNotNull(firstItem.get(Constants.APPLICATION_DESCRIPTION));
            Assert.assertNotNull(firstItem.get(Constants.CREATED_AT));
            Assert.assertNotNull(firstItem.get(Constants.LAST_UPDATED_AT));
            Assert.assertNotNull(firstItem.get(Constants.APPLICATION_NAME));
        } else {
            Assert.assertEquals(
                    HttpStatus.SC_OK
                    , getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetApplication().getResponse().prettyPeek()));
        }
    }

    @When("^I get a list of applications using filters to filter \"([^\"]*)\"$")
    public void get_a_list_of_applications_using_filters(String filterName) {
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource");
        String filterValue = null;
        if (filterName.equalsIgnoreCase("clientId")) {
            filterValue = applicationResponse.getBody().path("application.clientId");
        } else if (filterName.equalsIgnoreCase("peakId")) {
            filterValue = applicationResponse.getBody().path("application.peakId");
        } else if (filterName.equalsIgnoreCase("subUnitId")) {
            filterValue = applicationResponse.getBody().path("application.subUnitId");
        } else if (filterName.equalsIgnoreCase("platformId")) {
            filterValue = applicationResponse.getBody().path("application.platformId");
        } else if (filterName.equalsIgnoreCase("platformName")) {
            filterValue = applicationResponse.getBody().path("application.platformName");
        }
        url = url + "?" + filterName + "=" + filterValue;
        testContext.getApiManager().getGetApplication().getListOfApplications(
                url,
                testContext.getApiManager().getGetApplication().getAuthToken());
    }

    @When("^I get a list of applications using filters to filter \"([^\"]*)\" and \"([^\"]*)\" values$")
    public void get_a_list_of_applications_usingNullHeaders(String filterName, String headerValues) {
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource");
        url = url + "?" + filterName + "=" + applicationResponse.getBody().path("application.clientId");
        ;
        testContext.getApiManager().getGetApplication().getListOfApplication(
                url, testContext.getApiManager().getGetApplication().getAuthToken(), headerValues);
    }

    @When("^I get a list of applications using filters to filter \"([^\"]*)\" with ([^\"]*) limits$")
    public void get_a_list_of_apps_using_filters_and_limits(String filterName, int limit) {
        Response applicationResponse = new OneClickMerchantOnboarding_StepDefs(testContext).createApplicationWithOneClickApi();
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource");
        url = url + "?" + filterName + "=" + applicationResponse.getBody().path("application.clientId");
        url = url + "&limit=" + limit;
        currentUrl = url;
        testContext.getApiManager().getGetApplication().getListOfApplications(
                url,
                testContext.getApiManager().getGetApplication().getAuthToken());
    }

    @And("^the response should have a ([^\"]*) number of total items$")
    public void response_should_have_number_of_total_pages(int totalNumberOfItems) {
        JsonPath objItem = getRestHelper().getJsonPath(testContext.getApiManager().getGetApplication().getResponse());
        int total = 0;
        total = Integer.parseInt("" + objItem.getMap("page").get("totalItems"));
        Assert.assertTrue(
                "response should have number of total items " + totalNumberOfItems + " but got " + total,
                total == totalNumberOfItems
        );
    }

    @And("^the response should be on page ([^\"]*)$")
    public void response_should_be_on_page(int currentPageNumber) {
        JsonPath objItem = getRestHelper().getJsonPath(testContext.getApiManager().getGetApplication().getResponse());
        int page = Integer.parseInt("" + objItem.getMap("page").get("current"));

        Assert.assertTrue(
                "response should be on page " + currentPageNumber + " but it is on page " + page,
                page == currentPageNumber
        );
    }

    @When("^I move to page ([^\"]*)$")
    public void move_tot_page(int nextPageNumber) {
        String url = currentUrl + "&page=" + nextPageNumber;
        testContext.getApiManager().getGetApplication().getListOfApplications(
                url,
                testContext.getApiManager().getGetApplication().getAuthToken());
    }

    @When("^I make a get request to the application endpoint with \"([^\"]*)\" missing in the header$")
    public void i_make_a_put_request_to_the_application_endpoint_with_key_missing_in_the_header(String key) {
        testContext.getApiManager().getGetApplication().getListOfApplication(
                getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) + "/" + testContext.getApiManager().getPutApplication().getApplicationId(),
                testContext.getApiManager().getGetApplication().getAuthToken(), key);
    }

}
