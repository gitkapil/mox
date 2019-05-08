package steps;

import com.google.common.collect.Sets;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.collections.CollectionUtils;
import org.testng.Assert;
import utils.EnvHelper;
import utils.PropertyHelper;

import java.util.*;

public class PutPublicKeys_StepDefs extends UtilManager {
    private TestContext testContext;
    private ManagementCommon common;
    private static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final Set<String> INCORRECT_ROLE_SET = Sets.newHashSet("refund");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";


    public PutPublicKeys_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }

    @Given("^I am a PUT public keys Dragon user with incorrect role$")
    public void incorrectUser() {
        common.iAmAnAuthorizedDragonUser(INCORRECT_ROLE_SET, token -> testContext.getApiManager().getPutPublicKeys().setAuthTokenWithBearer(token));
    }

    @Given(("^I am a PUT public keys DRAGON user with correct role$"))
    public void correctUser() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPutPublicKeys().setAuthTokenWithBearer(token));
    }

    @And("^I have an \"([^\"]*)\" and \"([^\"]*)\" from an existing public key$")
    public void publicKeyParam(String applicationId, String keyId) {
        testContext.getApiManager().getPutPublicKeys().setApplicationId(applicationId);
        if (PropertyHelper.getInstance().getPropertyCascading("env").equalsIgnoreCase("ci")
                && PropertyHelper.getInstance().getPropertyCascading("usertype").equalsIgnoreCase("merchant") &&
                keyId.equalsIgnoreCase("af3177e4-6304-4c66-946c-de6e382b336c")) {
            testContext.getApiManager().getPutPublicKeys().setKeyId("49582d55-4e29-4e38-b31c-a070d5152e1e");
        } else if (PropertyHelper.getInstance().getPropertyCascading("env").equalsIgnoreCase("sit") &&
                PropertyHelper.getInstance().getPropertyCascading("usertype").equalsIgnoreCase("developer") &&
                keyId.equalsIgnoreCase("af3177e4-6304-4c66-946c-de6e382b336c")) {
            testContext.getApiManager().getPutPublicKeys().setKeyId("c5aefa23-bba6-4439-be67-6a6117e02beb");
        } else if (PropertyHelper.getInstance().getPropertyCascading("env").equalsIgnoreCase("sit") &&
                PropertyHelper.getInstance().getPropertyCascading("usertype").equalsIgnoreCase("merchant") &&
                keyId.equalsIgnoreCase("af3177e4-6304-4c66-946c-de6e382b336c")) {
            testContext.getApiManager().getPutPublicKeys().setKeyId("fd09de46-1599-45e8-850b-29908284c3cb");
        } else {
            testContext.getApiManager().getPutPublicKeys().setKeyId(keyId);
        }
    }

    @And("^I enter a \"([^\"]*)\" description with value \"([^\"]*)\", activate At \"([^\"]*)\", deactivate at \"([^\"]*)\" and entity status \"([^\"]*)\"$")
    public void enterBody(String description, String value, String activateAt, String deactivateAt, String entityStatus) {
        testContext.getApiManager().getPutPublicKeys().setDescription(description);
        testContext.getApiManager().getPutPublicKeys().setValue(value);
        testContext.getApiManager().getPutPublicKeys().setActivateAt(activateAt);
        testContext.getApiManager().getPutPublicKeys().setDeactivateAt(deactivateAt);
        testContext.getApiManager().getPutPublicKeys().setEntityStatus(entityStatus);

        String url = getRestHelper().getBaseURI() + getFileHelper()
                .getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME) +
                "/" + testContext.getApiManager().getPutPublicKeys().getApplicationId() + "/keys/public/" +
                testContext.getApiManager().getPutPublicKeys().getKeyId();

        testContext.getApiManager().getPutPublicKeys().makeApiCall(url);
    }

    @Then("^the PUT public key response should have error status \"([^\"]*)\" with error code \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void checkErrorResponse(String httpStatus, String errorCode, String errorDescription) {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutPublicKeys().getResponse()),
                Integer.parseInt(httpStatus),
                "Http status is expected to be " + httpStatus + " but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutPublicKeys().getResponse())
        );

        Assert.assertEquals(
                getRestHelper().getErrorCode(testContext.getApiManager().getPutPublicKeys().getResponse()),
                errorCode,
                "Error code is expected to be " + errorCode + " but got " +
                        getRestHelper().getErrorCode(testContext.getApiManager().getPutPublicKeys().getResponse())
        );

        if (!getRestHelper().getErrorDescription(testContext.getApiManager().getPutPublicKeys().getResponse()).contains(errorDescription)) {
            Assert.assertEquals(
                    getRestHelper().getErrorDescription(testContext.getApiManager().getPutPublicKeys().getResponse()),
                    errorDescription,
                    "Error description is expected to be " + errorDescription + " but got " +
                            getRestHelper().getErrorDescription(testContext.getApiManager().getPutPublicKeys().getResponse())
            );
        }
    }

    @Then("^the PUT public key response should return success$")
    public void successResponse() {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutPublicKeys().getResponse()),
                200,
                "Http status is expected to be " + 200 + " but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPutPublicKeys().getResponse())
        );

        String[] predefinedSet = {
                "keyId",
                "keyName",
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

        HashMap returnedTransactions = testContext.getApiManager().getPutPublicKeys().getResponse().path(".");
        Set<String> keySet = returnedTransactions.keySet();
        Collection<String> diff = CollectionUtils.disjunction(Arrays.asList(predefinedSet), keySet);
        System.out.println("JT Diff " + String.join(",", diff));
        if (diff.size() == 0) {
        } else {
            Assert.assertEquals(true, false,
                    "Returned transaction object contain fields that are not a subset (" +
                            String.join(",", diff) + ")");
        }
    }
}
