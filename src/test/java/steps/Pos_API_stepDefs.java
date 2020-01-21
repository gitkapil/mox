package steps;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import managers.UtilManager;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import utils.ApiUtils;

import java.io.BufferedReader;
import java.io.FileReader;

import static org.junit.Assert.assertEquals;

public class Pos_API_stepDefs extends UtilManager {

    private RequestSpecification getRequest;
    private Response getResponse;

    @Autowired
    private ApiUtils apiUtils;

    public  String authentication;;
    public String serviceURL;
    private Scenario scenario;


    @Before
    public void before(Scenario scenario) {
        this.scenario = scenario;
    }


    public String getSecurityTokenForPos() {
        String serviceName="on-boarding.user-profiles";
        String accessCode="55581FA9F309E7622757F6AD51F85CFA";
        serviceURL = apiUtils.getInternalTokenPath();
        getRequest = apiUtils.postOperationToken().headers("serviceName", serviceName,"accessCode",accessCode);
        getResponse = apiUtils.getResponse(getRequest, serviceURL);
        return getResponse.path("token");
    }
    @Given("^user provides valid data \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\" in header to get merchants list under given platform id$")
    public void user_provides_valid_data_in_header_to_get_merchants_list_under_given_platform_id(String arg1, String apiVersion, String traceId) throws Throwable {

        authentication = getSecurityTokenForPos();
        FilterableRequestSpecification filterableRequestSpecification = (FilterableRequestSpecification) apiUtils.getRequest();
        getRequest = filterableRequestSpecification.removeHeader("X-HSBC-Local").removeHeader("X-HSBC-Chnl-CountryCode").removeHeader("X-HSBC-Chnl-Group-Member").removeHeader("X-HSBC-Device-Type").removeHeader("X-HSBC-Request-Correlation-Id").removeHeader("X-HSBC-Request-Correlation-Id").removeHeader("X-HSBC-Channel-Id").removeHeader("X-HSBC-Device-Id").header("Authorization", authentication).header("Api-Version", apiVersion).header("Trace-Id", traceId);
    }

    @Given("^user remove the header \"([^\"]*)\"$")
    public void userRemoveTheHeader(String header) throws Throwable {
        FilterableRequestSpecification filterableRequestSpecification=(FilterableRequestSpecification)getRequest;
        getRequest = filterableRequestSpecification.removeHeader(header);
    }


    @Given("^user provides \"([^\"]*)\" in path to get subunit ids mapped to the same$")
    public void user_provides_in_path_to_get_subunit_ids_mapped_to_the_same(String platformId) throws Throwable {
       String path = "http://localhost:8021/pos/report";
       serviceURL= path.concat("?platformId=" + platformId);
    }

    @When("^user triggers the get merchants by platform id$")
    public void user_triggers_the_get_merchants_by_platform_id() throws Throwable {

        getResponse = apiUtils.getResponse(getRequest, serviceURL);
        getResponse.prettyPrint();

    }

    @Then("^verify the response status code \"([^\"]*)\" for get merchants by platform id$")
    public void verify_the_response_status_code_for_get_merchants_by_platform_id(int statusCode) throws Throwable {
        String currentCurlCommand, lastCurlCommand = "";
        BufferedReader br = new BufferedReader(new FileReader("target/ConsoleOutput.txt"));
        while ((currentCurlCommand = br.readLine()) != null) {
            lastCurlCommand = lastCurlCommand+currentCurlCommand+"\n";
        }
        scenario.write(lastCurlCommand);
        assertEquals(getResponse.statusCode(), statusCode);

    }

    @Then("^verify the response contains all the expected merchants details under the given \"([^\"]*)\"$")
    public void verify_the_response_contains_all_the_merchants_details_under_the_given(String platformId) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        Assert.assertNotNull(getResponse.path("items.subUnitId[0]"));
        Assert.assertNotNull(getResponse.path("items.organisationId[0]"));
        Assert.assertNotNull(getResponse.path("items.platformId[0]"));
        Assert.assertNotNull(getResponse.path("items.platformName[0]"));
        Assert.assertEquals(platformId, getResponse.path("items.platformId[0]"));

    }
    @And("^check error code and error description matches \"([^\"]*)\",\"([^\"]*)\"$")
    public void checkEnrollStampCardErrorCodeMatches(String errorCode,String errorDescription) throws Throwable {
        String errCode = getResponse.path("errors[0].errorCode");
        assertEquals(errorCode, errCode);
        String errDescription = getResponse.path("errors[0].errorDescription");
        Assert.assertTrue(errDescription.contains(errorDescription));
    }

}