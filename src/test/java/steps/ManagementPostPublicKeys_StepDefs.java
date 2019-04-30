package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;

import java.util.*;

import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.EnvHelper;
import utils.PropertyHelper;

public class ManagementPostPublicKeys_StepDefs extends UtilManager {
    public static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String SIG_HEADER_LIST_POST_PUBLIC_KEYS = "header-list-post-public-keys";
    private final static Logger logger = Logger.getLogger(ManagementPostPublicKeys_StepDefs.class);

    TestContext testContext;
    ManagementCommon common;

    public ManagementPostPublicKeys_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }

    @Given("^I am a POST create keys authorized DRAGON user with the correct privileges$")
    public void createUser() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPostPublicKey().setAuthTokenWithBearer(token));
    }

    @When("^I create a new public key based on using an existing application key$")
    public void createPublicKey() {

    }

    @When("I create a new public key based on using \"([^\"]*)\" application key$")
    public void createPublicKey(String applicationKey) {

    }

    @And("^I give it in a valid JSON in the body$")
    public void generateValidJSONBody() {

    }

    @Then("^I should receive a successful create public key response$")
    public void successfulResponse() {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostPublicKey().getResponse())
                , 200);

        String[] predefinedSet = {
                "keyId",
                "applicationId",
                "value",
                "type",
                "size",
                "activateAt",
                "deactivateAt",
                "entityStatus",
                "description",
                "createdAt",
                "lastUpdatedAt"
        };

        ArrayList returnedObject = testContext.getApiManager().getPostPublicKey().getResponse().path(".");
        returnedObject.stream().forEach(t -> {
            Set<String> keySet = ((HashMap)t).keySet();
            Collection<String> diff = CollectionUtils.disjunction(Arrays.asList(predefinedSet), keySet);

            if (diff.size() == 0) {
            } else {
                    Assert.assertEquals(true, false,
                            "Returned object contain fields that are not a subset (" +
                                    String.join(",", diff) + ")");
            }
        });
    }

    @Then("^the public keys response should receive a \"([^\"]*)\" error with \"([^\"]*)\" description and \"([^\"]*)\" error code$")
    public void failedResponse(String httpStatus, String errorDescription, String errorCode) {

    }

    @And("^I have \"([^\"]*)\" value, activate at \"([^\"]*)\", deactivate at \"([^\"]*)\", \"([^\"]*)\" as entity status and description is \"([^\"]*)\"$")
    public void generateJsonBody(String value, String activateAt, String deactivateAt, String entityStatus, String description) {

    }


}
