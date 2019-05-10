package steps;

import com.google.common.collect.Sets;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.collections.CollectionUtils;
import org.testng.Assert;

import java.util.*;

public class ManagementGetSigningKeys_StepDef extends UtilManager {
    TestContext testContext;
    ManagementCommon common;
    private static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";

    public ManagementGetSigningKeys_StepDef(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(this.testContext);
    }

    @Given("^I am a user with permissions to use signing key$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getGetSigningKey().setAuthTokenWithBearer(token));
    }

    @When("^I make a request with \"([^\"]*)\" as application id$")
    public void setApplicationId(String applicationId) {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                RESOURCE_ENDPOINT_PROPERTY_NAME) + "/";

        testContext.getApiManager().getGetSigningKey().setApplicationId(applicationId);
        testContext.getApiManager().getGetSigningKey().makeCall(url);
    }

    @Then("^I should receive a signing key response error \"([^\"]*)\" status with code \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void failedResponse(String httpStatus, String errorCode, String errorDescription) {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetSigningKey().getResponse()),
                Integer.parseInt(httpStatus),
                "Status code expected " + httpStatus + " but got " + getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetSigningKey().getResponse())
        );

        Assert.assertEquals(
                getRestHelper().getErrorCode(testContext.getApiManager().getGetSigningKey().getResponse()),
                errorCode,
                "Error code expected " + errorCode + " but got " +
                        getRestHelper().getErrorCode(testContext.getApiManager().getGetSigningKey().getResponse())
        );

        Assert.assertEquals(
                getRestHelper().getErrorDescription(testContext.getApiManager().getGetSigningKey().getResponse()),
                errorDescription,
                "Error description expected " + errorDescription + " but got " +
                        getRestHelper().getErrorDescription(testContext.getApiManager().getGetSigningKey().getResponse())
        );
    }

    @Then("^I should receive a successful signing key response$")
    public void successResponse() {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(
                        testContext.getApiManager().getGetSigningKey().getResponse()),
                200
        );

        String[] predefinedSet = {
                "keyId",
                "applicationId",
                "keyName",
                "value",
                "type",
                "size",
                "activateAt",
                "deactivateAt",
                "entityStatus",
                "description",
                "createdAt",
                "lastUpdatedAt",
                "alg"
        };

        ArrayList returnedObject = testContext.getApiManager().getGetSigningKey().getResponse().path(".");
        if (returnedObject != null) {
            returnedObject.stream().forEach(t -> {
                Set<String> keySet = ((HashMap) t).keySet();
                Collection<String> diff = CollectionUtils.disjunction(Arrays.asList(predefinedSet), keySet);

                if (diff.size() == 0) {
                } else {
                    Assert.assertEquals(true, false,
                            "Returned object contain fields that are not a subset (" +
                                    String.join(",", diff) + ")");
                }
            });
        }
    }
}
