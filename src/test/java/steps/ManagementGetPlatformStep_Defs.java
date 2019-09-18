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
import org.junit.Assert;
import utils.Constants;

import java.util.*;

public class ManagementGetPlatformStep_Defs extends UtilManager {
    TestContext testContext;
    ManagementCommon common;
    Response applicationResponse;
    private static final Set<String> ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String VALID_BASE64_ENCODED_RSA_PUBLIC_KEY = "valid_base64_encoded_rsa_public_key";
    final static Logger logger = Logger.getLogger(ManagementGetPlatformStep_Defs.class);

    public ManagementGetPlatformStep_Defs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }


    @Given("^I am a GET platform authorized DRAGON user with Platform\\.ReadWrite\\.All$")
    public void i_am_a_GET_platform_authorized_DRAGON_user_with_Platform_ReadWrite_All()  {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getGetPlatform().setAuthToken(token));
    }

    @When("^I make a GET request to the platform endpoint$")
    public void i_make_a_GET_request_to_the_platform_endpoint()  {
            String url = getRestHelper().getBaseURI() +
                    getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                    + "/management/platforms";
            testContext.getApiManager().getGetPlatform().executeGetPlatformRequest(url);
    }

    @Then("^I should receive a successful GET platform response$")
    public void i_should_receive_a_successful_GET_platform_response()  {
        Assert.assertEquals(
                200
                , getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetPlatform().getResponse()));
    }

    @Then("^validate the response from GET platform API$")
    public void validate_the_response_from_GET_platform_API() {
        String list = getRestHelper().getJsonArray(testContext.getApiManager().getGetPlatform().getResponse(), Constants.ITEM).toString();

        Map<String, Object> map = new Gson().fromJson(
                list, new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );
        System.out.println("map: "+ map);
    }
    @When("^I make a GET request to the Platform endpoint with \"([^\"]*)\" missing in the header$")
    public void i_make_a_GET_request_to_the_Platform_endpoint_with_missing_in_the_header(String arg1)  {

    }

    @Then("^I should receive a \"([^\"]*)\" error response with \"([^\"]*)\" error description and \"([^\"]*)\" error code within the GET platform response$")
    public void i_should_receive_a_error_response_with_error_description_and_error_code_within_the_GET_platform_response(String arg1, String arg2, String arg3)  {

    }

    @Then("^error message should be \"([^\"]*)\" within the GET platform response$")
    public void error_message_should_be_within_the_GET_platform_response(String arg1)  {

    }

    @When("^I make a GET request to the platform endpoint with \"([^\"]*)\" missing in the header$")
    public void i_make_a_GET_request_to_the_platform_endpoint_with_missing_in_the_header(String arg1)  {

    }


    @Given("^I am a GET dragon DRAGON user with Platform\\.ReadWrite\\.All with invalid \"([^\"]*)\"$")
    public void i_am_a_GET_dragon_DRAGON_user_with_Platform_ReadWrite_All_with_invalid(String arg1)  {

    }


    @When("^I get a list of platform using filters to filter \"([^\"]*)\" with \"([^\"]*)\"$")
    public void i_get_a_list_of_platform_using_filters_to_filter_with(String arg1, String arg2) {

    }

    @Then("^validate the item list of platform from the response$")
    public void validate_the_item_list_of_platform_from_the_response() {

    }

    @Then("^validate the all items list of platform should have active status$")
    public void validate_the_all_items_list_of_platform_should_have_active_status() {

    }



    @Then("^the response should have a list of \"([^\"]*)\" platform$")
    public void the_response_should_have_a_list_of_platform(String arg1) {

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


}
