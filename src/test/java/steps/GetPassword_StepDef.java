package steps;

import com.google.common.collect.Sets;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GetPassword_StepDef extends UtilManager {
    TestContext testContext;
    ManagementCommon common;
    private static final Set<String> ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String VALID_BASE64_ENCODED_RSA_PUBLIC_KEY = "valid_base64_encoded_rsa_public_key";
    final static Logger logger = Logger.getLogger(ManagementCreateClient_StepDefs.class);

    public GetPassword_StepDef(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }


    @Given("^I am logging in as a user with authorize Dragon user$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPassword().setAuthToken(token));
    }


    @When("^I get the password request$")
    public void getPasswordRequest() {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().getPassword().getApplicationId() + "/keys/passwords";
        testContext.getApiManager().getPassword().getPasswordRequest(url, testContext.getApiManager().getPassword().getAuthToken());
    }


    @Then("^I should see the response from get password request$")
    public void getPasswordResponse() {
        Assert.assertEquals(testContext.getApiManager().getPassword().getResponse().statusCode(), HttpStatus.SC_OK);

        List<Object> list = getRestHelper().getJsonArray(testContext.getApiManager().getPassword().getResponse(), Constants.ITEM);
        list.stream().forEach(listObject -> System.out.println(listObject));
        List<String> strings = new ArrayList<>(list.size());
        for (Object object : list) {
            strings.add(Objects.toString(object, null));
        }
        Assert.assertTrue(strings.get(0).contains(Constants.APPLICATION_ID),testContext.getApiManager().getCreateClientPassword().getApplicationId());
        Assert.assertTrue(strings.get(0).contains(Constants.CLIENT_ID),testContext.getApiManager().getCreateClientPassword().getClientId());
        Assert.assertTrue(strings.get(0).contains(Constants.PEAK_ID),testContext.getApiManager().getCreateClientPassword().getPasswordChannel());
        Assert.assertTrue(strings.get(0).contains(Constants.SUB_UNIT_ID),testContext.getApiManager().getCreateClientPassword().getApplicationId());
        Assert.assertTrue(strings.get(0).contains(Constants.ORGANISATION_ID),testContext.getApiManager().getCreateClientPassword().getApplicationId());
        Assert.assertTrue(strings.get(0).contains(Constants.DESCRIPTION),testContext.getApiManager().getCreateClientPassword().getApplicationId());
    }

    @When("^I get the password with null header \"([^\"]*)\"$")
    public void getPasswordRequestWithNullHeaderValues(String nullHeaderValues) {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().getPassword().getApplicationId() + "/keys/passwords";
        testContext.getApiManager().getPassword().getPasswordWithNullHeaderValues(url, testContext.getApiManager().getPassword().getAuthToken(),nullHeaderValues);
    }




}