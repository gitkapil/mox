package steps;

import com.google.common.collect.Sets;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.collections.CollectionUtils;
import org.testng.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class ManagementPutSigningKeys_StepDefs extends UtilManager {
    private TestContext testContext;
    private ManagementCommon common;
    private static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String EXISTING_PUBLIC_KEY = "public_key_application_id";

    public ManagementPutSigningKeys_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(this.testContext);
    }

    @Given("^I am a PUT signing key authorized user$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPutSigningKeys().setAuthTokenWithBearer(token));
    }

    @And("^I create a new signing key based on using an existing application key$")
    public void createSigningKey() {
        //TODO: THis needs to be done when we merge feature/DRAG-1568
        String applicationId = getFileHelper().getValueFromPropertiesFile(Hooks.envProperties,
                EXISTING_PUBLIC_KEY);

        testContext.getApiManager().getPostSigningKeys().setApplicationId(applicationId);
        //Do something here
    }

    @And("^I retrieve the applicationId and keyId from the signing key response$")
    public void getSigningKey() {
        HashMap dataMap = (HashMap)testContext.getApiManager().getPostSigningKeys().getResponse().path(".");
        String newKeyId = dataMap.get("keyId").toString();
        String returnedApplicationId = dataMap.get("applicationId").toString();
        testContext.getApiManager().getPutSigningKeys().setKeyId(newKeyId);
        testContext.getApiManager().getPutSigningKeys().setApplicationId(returnedApplicationId);
    }

    @And("^I have an \"([^\"]*)\" and \"([^\"]*)\" from an existing signing key$")
    public void setApplicationIdKeyId(String applicationId, String keyId) {
        testContext.getApiManager().getPutSigningKeys().setApplicationId(applicationId);
        testContext.getApiManager().getPutSigningKeys().setKeyId(keyId);
    }

    @And("^I update the signing key with description \"([^\"]*)\", activate at \"([^\"]*)\", deactivate at \"([^\"]*)\" and entity status \"([^\"]*)\"$")
    public void updateBody(String description, String activateAt, String deactivateAt, String entityStatus) {
        testContext.getApiManager().getPutSigningKeys().setDescription(description);
        testContext.getApiManager().getPutSigningKeys().setActivateAt(activateAt);
        testContext.getApiManager().getPutSigningKeys().setDeactivateAt(deactivateAt);
        testContext.getApiManager().getPutSigningKeys().setEntityStatus(entityStatus);

        String url = getRestHelper().getBaseURI() + getFileHelper()
                .getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) +
                "/" + testContext.getApiManager().getPutSigningKeys().getApplicationId() + "/keys/signing/" +
                testContext.getApiManager().getPutSigningKeys().getKeyId();

        testContext.getApiManager().getPutSigningKeys().makeApiCall(url);
    }

    @Then("^the PUT signing key response should have error status \"([^\"]*)\" with error code \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void errorResponse(String httpStatus, String errCode, String errDescription) {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutSigningKeys().getResponse()),
                Integer.parseInt(httpStatus),
                "Http status is expected to be " + httpStatus + " but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutSigningKeys().getResponse())
        );

        Assert.assertEquals(
                getRestHelper().getErrorCode(testContext.getApiManager().getPutSigningKeys().getResponse()),
                errCode,
                "Error code is expected to be " + errCode + " but got " +
                        getRestHelper().getErrorCode(testContext.getApiManager().getPutSigningKeys().getResponse())
        );

        if (!getRestHelper().getErrorDescription(testContext.getApiManager().getPutSigningKeys().getResponse()).contains(errDescription)) {
            Assert.assertEquals(
                    getRestHelper().getErrorDescription(testContext.getApiManager().getPutSigningKeys().getResponse()),
                    errDescription,
                    "Error description is expected to be " + errDescription + " but got " +
                            getRestHelper().getErrorDescription(testContext.getApiManager().getPutSigningKeys().getResponse())
            );
        }
    }

    @Then("^the PUT signing key response should return success$")
    public void successResponse() {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutSigningKeys().getResponse()),
                200,
                "Http status is expected to be " + 200 + " but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutSigningKeys().getResponse())
        );

        String[] predefinedSet = {
                "keyId",
                "applicationId",
                "value",
                "activateAt",
                "deactivateAt",
                "description",
                "createdAt",
                "lastUpdatedAt",
                "size",
                "entityStatus",
                "type"
        };

        HashMap returnedTransactions = testContext.getApiManager().getPutSigningKeys().getResponse().path(".");
        Set<String> keySet = returnedTransactions.keySet();
        Collection<String> diff = CollectionUtils.disjunction(Arrays.asList(predefinedSet), keySet);
        if (diff.size() == 0) {
        } else {
            Assert.assertEquals(true, false,
                    "Returned transaction object contain fields that are not a subset (" +
                            String.join(",", diff) + ")");
        }
    }
}
