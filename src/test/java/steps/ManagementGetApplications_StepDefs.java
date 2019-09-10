package steps;

import com.google.common.collect.Sets;
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

import java.util.*;

public class ManagementGetApplications_StepDefs extends UtilManager {
    TestContext testContext;
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

    @When("^I get a list of applications without any filters$")
    public void list_of_applications_without_any_filters() {
        testContext.getApiManager().getGetApplication().getListOfApplications(
                getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource"),

                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))),
                testContext.getApiManager().getPostApplication().getAuthToken()
        );
    }

    @Then("^I should receive a successful response$")
    public void successful_response() {
        Assert.assertEquals(
                200
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
                System.out.println("here : " + getRestHelper().getErrorDescription(response));
                System.out.println("there: " + errorDesc);
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

                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))),
                testContext.getApiManager().getPostApplication().getAuthToken());
    }

    @When("^I get a list of application using multiple filters with correct uuids$")
    public void when_i_get_list_of_application_using_multi_filters_with_uuid() {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource");

        url = url + "?peakId=00000001-0000-0000-0000-000000000000&clientId=00000001-0000-0000-0000-000000000000";

        testContext.getApiManager().getGetApplication().getListOfApplications(
                url,

                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))),
                testContext.getApiManager().getPostApplication().getAuthToken());
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
        List<Object> list = getRestHelper().getJsonArray(testContext.getApiManager().getGetApplication().getResponse(), Constants.ITEM);
        list.stream().forEach(listObject -> System.out.println(listObject));
        List<String> strings = new ArrayList<>(list.size());
        for (Object object : list) {
            strings.add(Objects.toString(object, null));
            System.out.println(object);
        }
        if (list.size() != 0) {
            Assert.assertEquals( 8, strings.get(0).split(",").length);
            org.testng.Assert.assertTrue(strings.get(0).contains(Constants.APPLICATION_ID), testContext.getApiManager().getPutApplication().getApplicationId());
            org.testng.Assert.assertTrue(strings.get(0).contains(Constants.CLIENT_ID), testContext.getApiManager().getPutApplication().getClientId());
            org.testng.Assert.assertTrue(strings.get(0).contains(Constants.PEAK_ID), testContext.getApiManager().getPutApplication().getPeakId());
            org.testng.Assert.assertTrue(strings.get(0).contains(Constants.SUB_UNIT_ID), testContext.getApiManager().getPutApplication().getSubUnitId());
            org.testng.Assert.assertTrue(strings.get(0).contains(Constants.ORGANISATION_ID), testContext.getApiManager().getPutApplication().getOrganisationId());
            org.testng.Assert.assertTrue(strings.get(0).contains(Constants.PLATFORM_ID),testContext.getApiManager().getPutApplication().getPlatformId());
            org.testng.Assert.assertTrue(strings.get(0).contains(Constants.PLATFORM_NAME),testContext.getApiManager().getPutApplication().getApplicationId());
            org.testng.Assert.assertTrue(strings.get(0).contains(Constants.APPLICATION_ID), testContext.getApiManager().getPutApplication().getApplicationId());
            Assert.assertTrue("description is not present", strings.get(0).contains(Constants.DESCRIPTION));
        } else if (list.size() == 1) {

        } else {
            Assert.assertEquals(
                    HttpStatus.SC_OK
                    , getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetApplication().getResponse().prettyPeek()));
        }
    }

    @When("^I get a list of applications using filters to filter \"([^\"]*)\" with \"([^\"]*)\"$")
    public void get_a_list_of_applications_using_filters(String filterName, String filterValue) {

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource");

        url = url + "?" + filterName + "=" + filterValue;
        testContext.getApiManager().getGetApplication().getListOfApplications(
                url,
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        "header-list-get").split(","))),
                testContext.getApiManager().getPostApplication().getAuthToken());
    }

    @When("^I get a list of applications using filters to filter \"([^\"]*)\" with \"([^\"]*)\" and \"([^\"]*)\" values$")
    public void get_a_list_of_applications_usingNullHeaders(String filterName, String filterValue, String headerValues) {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource");
        url = url + "?" + filterName + "=" + filterValue;
        testContext.getApiManager().getGetApplication().getListOfApplication(
                url, testContext.getApiManager().getPostApplication().getAuthToken(), headerValues);

    }

    @When("^I get a list of applications using filters to filter \"([^\"]*)\" with \"([^\"]*)\" with ([^\"]*) limits$")
    public void get_a_list_of_apps_using_filters_and_limits(String filterName, String filterValue, int limit) {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource");
        url = url + "?" + filterName + "=" + filterValue;
        url = url + "&limit=" + limit;
        currentUrl = url;
        testContext.getApiManager().getGetApplication().getListOfApplications(
                url,

                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        "header-list-get").split(","))),
                testContext.getApiManager().getPostApplication().getAuthToken());
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

    @Given("^I am a GET application authorized DRAGON user with the Application.ReadWrite.All privilege$")
    public void i_am_an_authorized_DRAGON_user_with_role() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPostApplication().setAuthTokenWithBearer(token));
    }

    @Given("^I am a GET application authorized DRAGON user with the ApplicationKey.ReadWrite.All privilege$")
    public void i_am_an_authorized_DRAGON_user_with_incorrect_role() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPostApplication().setAuthTokenWithBearer(token));
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
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "header-list-get").split(","))),
                testContext.getApiManager().getPostApplication().getAuthToken());
    }

    @When("^I make a get request to the application endpoint with \"([^\"]*)\" missing in the header$")
    public void i_make_a_put_request_to_the_application_endpoint_with_key_missing_in_the_header(String key) {
        testContext.getApiManager().getGetApplication().getListOfApplication(
                getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) + "/" + testContext.getApiManager().getPutApplication().getApplicationId(),
                testContext.getApiManager().getPostApplication().getAuthToken(), key);
    }

}
