package steps;

import com.google.common.collect.Sets;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.*;

public class ManagementPostPublicKeys_StepDefs extends UtilManager {
    public static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String EXISTING_PUBLIC_KEY = "public_key_application_id";
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
        String applicationId = getFileHelper().getValueFromPropertiesFile(Hooks.envProperties,
                EXISTING_PUBLIC_KEY);

        testContext.getApiManager().getPostPublicKey().setApplicationId(applicationId);
        doValidJsonBodyCall();
    }

    @When("I create a new public key based on using \"([^\"]*)\" application key$")
    public void createPublicKey(String applicationKey) {
        testContext.getApiManager().getPostPublicKey().setApplicationId(applicationKey);
    }

    @And("^I give it in a valid JSON in the body$")
    public void generateValidJSONBody() {
        doValidJsonBodyCall();
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
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostPublicKey().getResponse()),
                httpStatus,
                "Http status expected " + httpStatus + " but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostPublicKey().getResponse())
        );

        Assert.assertEquals(
                getRestHelper().getErrorCode(testContext.getApiManager().getPostPublicKey().getResponse()),
                errorCode,
        "Error code expected " + errorCode + " but got " +
                    getRestHelper().getErrorCode(testContext.getApiManager().getPostPublicKey().getResponse())
        );

        Assert.assertEquals(
                getRestHelper().getErrorDescription(testContext.getApiManager().getPostPublicKey().getResponse()),
                errorDescription,
                "Error description expected '" + errorDescription + "' but got '" +
                        getRestHelper().getErrorDescription(testContext.getApiManager().getPostPublicKey().getResponse())
        );
    }

    @And("^I have \"([^\"]*)\" value, activate at \"([^\"]*)\", deactivate at \"([^\"]*)\", \"([^\"]*)\" as entity status and description is \"([^\"]*)\"$")
    public void generateJsonBody(String value, String activateAt, String deactivateAt, String entityStatus, String description) {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                RESOURCE_ENDPOINT_PROPERTY_NAME) + "/";
        testContext.getApiManager().getPostPublicKey().postPublicKeys(
                url,
                value,
                activateAt,
                deactivateAt,
                entityStatus,
                description);
    }

    private void doValidJsonBodyCall() {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                RESOURCE_ENDPOINT_PROPERTY_NAME) + "/";
        String value = "abc";
        String activateAt = "01-01-2019T00:00:00Z";
        String deactivateAt = "03-03-2019T00:00:00Z";
        String entityStatus = "D";
        String description = "Test description";

        testContext.getApiManager().getPostPublicKey().postPublicKeys(
                url,
                value,
                activateAt,
                deactivateAt,
                entityStatus,
                description);
    }
}
