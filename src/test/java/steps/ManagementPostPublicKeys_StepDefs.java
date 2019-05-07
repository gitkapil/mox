package steps;

import com.google.common.collect.Sets;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.*;

public class ManagementPostPublicKeys_StepDefs extends UtilManager {
    public static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    public static final Set<String> INCORRECT_ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String EXISTING_PUBLIC_KEY = "public_key_application_id";

    String value;
    String activateAt;
    String deactivateAt;
    String entityStatus;
    String description;

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

    @Given("^I am a POST create key authorized DRAGON user with incorrect privileges$")
    public void createIncorrectUser() {
        common.iAmAnAuthorizedDragonUser(INCORRECT_ROLE_SET, token -> testContext.getApiManager().getPostPublicKey().setAuthTokenWithBearer(token));
    }

    @When("^I create a new public key based on using an existing application key$")
    public void createPublicKey() {
        String applicationId = getFileHelper().getValueFromPropertiesFile(Hooks.envProperties,
                EXISTING_PUBLIC_KEY);

        testContext.getApiManager().getPostPublicKey().setApplicationId(applicationId);

        // doValidJsonBodyCall();

        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                RESOURCE_ENDPOINT_PROPERTY_NAME) + "/";
        testContext.getApiManager().getPostPublicKey().postPublicKeys(
                url,
                this.value,
                this.activateAt,
                this.deactivateAt,
                this.entityStatus,
                this.description);
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
                , 201);

        String[] predefinedSet = {
                "keyId",
                "keyName",
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

        HashMap t = testContext.getApiManager().getPostPublicKey().getResponse().path(".");
        Set<String> keySet = ((HashMap)t).keySet();
        Collection<String> diff = CollectionUtils.disjunction(Arrays.asList(predefinedSet), keySet);

        if (diff.size() == 0) {
        } else {
            Assert.assertEquals(true, false,
                    "Returned object contain fields that are not a subset (" +
                            String.join(",", diff) + ")");
        }
    }

    @Then("^the public keys response should receive a \"([^\"]*)\" error with \"([^\"]*)\" description and \"([^\"]*)\" error code$")
    public void failedResponse(String httpStatus, String errorDescription, String errorCode) {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostPublicKey().getResponse()),
                Integer.parseInt(httpStatus),
                "Http status expected " + httpStatus + " but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostPublicKey().getResponse())
        );

        if (errorCode.length() > 0) {
            Assert.assertEquals(
                    getRestHelper().getErrorCode(testContext.getApiManager().getPostPublicKey().getResponse()),
                    errorCode,
                    "Error code expected " + errorCode + " but got " +
                            getRestHelper().getErrorCode(testContext.getApiManager().getPostPublicKey().getResponse())
            );
        }
        if (errorDescription.length() > 0) {
            if (!getRestHelper().getErrorDescription(testContext.getApiManager().getPostPublicKey().getResponse()).contains(errorDescription)) {
                Assert.assertEquals(
                        getRestHelper().getErrorDescription(testContext.getApiManager().getPostPublicKey().getResponse()),
                        errorDescription,
                        "Error description expected '" + errorDescription + "' but got '" +
                                getRestHelper().getErrorDescription(testContext.getApiManager().getPostPublicKey().getResponse())
                );
            }
        }
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
        String value = StringUtils.repeat("*", 2048);
        String activateAt = "2019-01-01T00:00:00Z";
        String deactivateAt = "2019-02-02T00:00:00Z";
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

    @And("^I have a value of (\\d+) characters, activate at \"([^\"]*)\", deactivate at \"([^\"]*)\", \"([^\"]*)\" as entity status and description is \"([^\"]*)\"$")
    public void iHaveAValueOfCharactersActivateAtDeactivateAtAsEntityStatusAndDescriptionIs(int numOfCharsInValue, String activateAt, String deactivateAt, String entityStatus, String description) throws Throwable {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < numOfCharsInValue; i++) {
            sb.append("A");
        }

        this.value = sb.toString();
        this.activateAt = activateAt;
        this.deactivateAt = deactivateAt;
        this.entityStatus = entityStatus;
        this.description = description;
    }
}
