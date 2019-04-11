package steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.log4j.Logger;

public class ManagementGetApplications_StepDefs extends UtilManager {
    TestContext testContext;

    public ManagementGetApplications_StepDefs(TestContext testContext) {
        this.testContext = testContext;
    }

    final static Logger logger = Logger.getLogger(ManagementGetApplications_StepDefs.class);

    @And("^I then clean and create 30 test applications$")
    public void clean_and_create_applications() {
        for (int x = 0; x < 30; x++) {

        }
    }

    @When("^I get a list of applications without any filters$")
    public void list_of_applications_without_any_filters() {

    }

    @Then("^I should receive a successful response$")
    public void successful_response() {

    }

    @And("^the response should have a list of ([^\"]*) applications$")
    public void should_have_number_of_applications(int numberOfResponses) {

    }

    @And("^the response should have more than ([^\"]*) in total$")
    public void should_have_more_than_responses_in_total(int numberOfResponses) {

    }

    @When("^I get a list of applications using filters to filter \"([^\"]*)\" with \"([^\"]*)\"$")
    public void get_a_list_of_applications_using_filters(String filterName, String filterValue) {

    }

    @When("^I get a list of applications using filters to filter \"([^\"]*)\" with \"([^\"]*)\" with ([^\"]*) limits$")
    public void get_a_list_of_apps_using_filters_and_limits(String filterName, String filerValue, int limit) {

    }

    @And("^the response should have a ([^\"]*) number of total pages$")
    public void response_should_have_number_of_total_pages(int totalNumberOfPages) {

    }

    @And("^the response should be on page ([^\"]*)$")
    public void response_should_be_on_page(int currentPageNumber) {

    }

    @When("^I move to page ([^\"]*)$")
    public void move_tot_page(int nextPageNumber) {

    }
}
