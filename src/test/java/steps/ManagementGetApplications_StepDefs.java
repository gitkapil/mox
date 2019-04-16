package steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ManagementGetApplications_StepDefs extends UtilManager {
    TestContext testContext;

    public ManagementGetApplications_StepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    final static Logger logger = Logger.getLogger(ManagementGetApplications_StepDefs.class);

    @When("^I get a list of applications without any filters$")
    public void list_of_applications_without_any_filters() {
        testContext.getApiManager().getGetApplication().getListOfApplications(
                getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource"),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        "header_list_get").split(",")))
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
                "There are items in items",
                getRestHelper().getJsonArray(testContext.getApiManager().getGetApplication().getResponse(), "items")
                        .size() == numberOfResponses
        );
    }

    @And("^the response should have more than ([^\"]*) in total$")
    public void should_have_more_than_responses_in_total(int numberOfResponses) {

        Assert.assertTrue(
                "There are less responses than " + numberOfResponses,
                getRestHelper().getJsonArray(testContext.getApiManager().getGetApplication().getResponse(), "items")
                        .size() < numberOfResponses
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
                        "header_list_get").split(","))));
    }

    @When("^I get a list of applications using filters to filter \"([^\"]*)\" with \"([^\"]*)\" with ([^\"]*) limits$")
    public void get_a_list_of_apps_using_filters_and_limits(String filterName, String filterValue, int limit) {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "create_application_resource");

        url = url + "?" + filterName + "=" + filterValue;
        url = url + "&limit=" + limit;

        testContext.getApiManager().getGetApplication().getListOfApplications(
                url,
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                new HashSet(Arrays.asList(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        "header_list_get").split(","))));
    }

    @And("^the response should have a ([^\"]*) number of total pages$")
    public void response_should_have_number_of_total_pages(int totalNumberOfPages) {
        List<HashMap<String, String>> objItem = getRestHelper().getJsonArray(testContext.getApiManager().getGetApplication().getResponse(), "page");
        int total = 0;
        for (HashMap<String, String> objKV : objItem) {
            if (objKV.get("total") != null) {
                total = Integer.parseInt(objKV.get("total"));
            }
        }

        Assert.assertTrue(
                "There are not " + totalNumberOfPages + " in the list",
                total == totalNumberOfPages
        );
    }

    @And("^the response should be on page ([^\"]*)$")
    public void response_should_be_on_page(int currentPageNumber) {
        List<HashMap<String, String>> objItem = getRestHelper().getJsonArray(testContext.getApiManager().getGetApplication().getResponse(), "page");
        int page = 0;
        for (HashMap<String, String> objKV : objItem) {
            if (objKV.get("current") != null) {
                page = Integer.parseInt(objKV.get("current"));
            }
        }

        Assert.assertTrue(
                "There are not on page " + currentPageNumber,
                page == currentPageNumber
        );
    }

    @When("^I move to page ([^\"]*)$")
    public void move_tot_page(int nextPageNumber) {
        //TODO
    }
}
