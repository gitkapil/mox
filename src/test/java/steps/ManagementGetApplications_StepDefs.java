package steps;

import com.jayway.restassured.path.json.JsonPath;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.util.Arrays;
import java.util.HashSet;

public class ManagementGetApplications_StepDefs extends UtilManager {
    TestContext testContext;

    public ManagementGetApplications_StepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    private String currentUrl;

    final static Logger logger = Logger.getLogger(ManagementGetApplications_StepDefs.class);

    @When("^I get a list of applications without any filters$")
    public void list_of_applications_without_any_filters() {
        testContext.getApiManager().getGetApplication().getListOfApplications(
                getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        "header-list-get").split(","))),
                testContext.getApiManager().getPostApplication().getAuthToken()
        );
    }

    @Then("^I should receive a successful response$")
    public void successful_response() {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetApplication().getResponse())
                , 200);
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

    @Then("^I should get an error message with status ([^\"]*) error code \"([^\"]*)\" and error description \"([^\"]*)\"$")
    public void then_should_get_error(int httpStatus, String errorCode, String errorDescription) {
        org.testng.Assert.assertEquals(getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetApplication().getResponse()),
                httpStatus,"Different response code being returned");

        org.testng.Assert.assertEquals(getRestHelper().getErrorCode(
                testContext.getApiManager().getGetApplication().getResponse()),
                errorCode,"Different error code being returned");

        org.testng.Assert.assertTrue(
                getRestHelper().getErrorDescription(
                        testContext.getApiManager().getGetApplication().getResponse()).contains(errorDescription) ,
                "Different error description being returned..Expected: "+ errorDescription
                        + "  Actual: "+
                        getRestHelper().getErrorDescription(testContext.getApiManager().getGetApplication().getResponse()));
    }


    @When("^I get a list of application using multiple filters$")
    public void when_i_get_list_of_application_using_multi_filters() {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource");

        url = url + "?peakId=00&clientId=11";

        testContext.getApiManager().getGetApplication().getListOfApplications(
                url,
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        "header-list-get").split(","))),
                testContext.getApiManager().getPostApplication().getAuthToken());
    }

    @When("^I get a list of application using multiple filters with correct uuids$")
    public void when_i_get_list_of_application_using_multi_filters_with_uuid() {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource");

        url = url + "?peakId=00000001-0000-0000-0000-000000000000&clientId=00000001-0000-0000-0000-000000000000";

        testContext.getApiManager().getGetApplication().getListOfApplications(
                url,
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        "header-list-get").split(","))),
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

    @When("^I get a list of applications using filters to filter \"([^\"]*)\" with \"([^\"]*)\"$")
    public void get_a_list_of_applications_using_filters(String filterName, String filterValue) {

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource");

        url = url + "?" + filterName + "=" + filterValue;

        testContext.getApiManager().getGetApplication().getListOfApplications(
                url,
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        "header-list-get").split(","))),
                testContext.getApiManager().getPostApplication().getAuthToken());
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
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        "header-list-get").split(","))),
                testContext.getApiManager().getPostApplication().getAuthToken());
    }

    @And("^the response should have a ([^\"]*) number of total pages$")
    public void response_should_have_number_of_total_pages(int totalNumberOfPages) {
        JsonPath objItem = getRestHelper().getJsonPath(testContext.getApiManager().getGetApplication().getResponse());
        int total = 0;
        total = Integer.parseInt("" + objItem.getMap("page").get("total"));


        Assert.assertTrue(
                "There are not " + totalNumberOfPages + " in the list",
                total == totalNumberOfPages
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
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        "header-list-get").split(","))),
                testContext.getApiManager().getPostApplication().getAuthToken());
    }
}
