package steps;

import com.google.common.collect.Sets;
import com.jayway.restassured.path.json.JsonPath;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ManagementGetApplications_StepDefs extends UtilManager {
    TestContext testContext;

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
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
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
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
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
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
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
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
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
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
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
    public void i_am_an_authorized_DRAGON_user_with_role()  {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPostApplication().setAuthTokenWithBearer(token));
    }

    @Given("^I am a GET application authorized DRAGON user with the ApplicationKey.ReadWrite.All privilege$")
    public void i_am_an_authorized_DRAGON_user_with_incorrect_role()  {
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
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        "header-list-get").split(","))),
                testContext.getApiManager().getPostApplication().getAuthToken());
    }
}
