package steps;

import com.google.common.collect.Sets;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.collections.CollectionUtils;
import org.testng.Assert;

import java.util.*;

public class ManagementPostSigningKey_StepDefs extends UtilManager {
    private static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final Set<String> INCORRECT_ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    TestContext testContext;
    ManagementCommon common;

    public ManagementPostSigningKey_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }

    @Given("^I am an authorized Signing Key DRAGON user$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPostSigningKeys().setAuthTokenWithBearer(token));
    }

    @Given("^I am not an authorized Signing key DRAGON user$")
    public void invalidLogin() {
        common.iAmAnAuthorizedDragonUser(INCORRECT_ROLE_SET, token -> testContext.getApiManager().getPostSigningKeys().setAuthTokenWithBearer(token));
    }

    @And("^I have a \"([^\"]*)\" application id$")
    public void setApplicationId(String applicationId) {
        testContext.getApiManager().getPostSigningKeys().setApplicationId(applicationId);
    }

    @And("^I have an activate date \"([^\"]*)\" and deactivate date \"([^\"]*)\", with entity status \"([^\"]*)\" and a description \"([^\"]*)\"$")
    public void setBody(String activateAt, String deactivateAt, String entityStatus, String description) {
        testContext.getApiManager().getPostSigningKeys().setActivateAt(activateAt);
        testContext.getApiManager().getPostSigningKeys().setDeactivateAt(deactivateAt);
        testContext.getApiManager().getPostSigningKeys().setEntityStatus(entityStatus);
        testContext.getApiManager().getPostSigningKeys().setDescription(description);
    }

    @When("^I make a request to create a new signing key$")
    public void makeRequest() {
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().getPostSigningKeys().getApplicationId() + "/keys/signing";
        testContext.getApiManager().getPostSigningKeys().makeRequest(url);
    }

    @Then("^the create signing key response should give a \"([^\"]*)\" http status with error code \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void invalidResponse(String httpStatus, String errorCode, String errorDescription) {
        Assert.assertEquals(
                httpStatus,
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostSigningKeys().getResponse()),
                "Expected http status " + httpStatus + " but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostSigningKeys().getResponse())
        );

        Assert.assertEquals(
                errorCode,
                getRestHelper().getErrorCode(testContext.getApiManager().getPostSigningKeys().getResponse()),
                "Expected error code " + errorCode + " but got " +
                        getRestHelper().getErrorCode(testContext.getApiManager().getPostSigningKeys().getResponse())
        );

        Assert.assertEquals(
                errorDescription,
                getRestHelper().getErrorDescription(testContext.getApiManager().getPostSigningKeys().getResponse()),
                "Expected error description " + errorDescription + " but got " +
                        getRestHelper().getErrorDescription(testContext.getApiManager().getPostSigningKeys().getResponse())
        );
    }

    @Then("^the create signing key response should be successful$")
    public void validResponse() {
        Assert.assertEquals(
                201,
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostSigningKeys().getResponse()),
                "Expected 201 but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostSigningKeys().getResponse())
        );

        String[] predefinedSet = {
                "keyId","keyName","applicationId",
                "value","alg","type","size","activateAt","deactivateAt","entityStatus",
                "description","createdAt","lastUpdatedAt"
        };

        ArrayList returnedTransactions = testContext.getApiManager().getPostSigningKeys().getResponse().path(".");
        returnedTransactions.stream().forEach(t -> {
            Set<String> keySet = ((HashMap)t).keySet();
            Collection<String> diff = CollectionUtils.disjunction(Arrays.asList(predefinedSet), keySet);
            System.out.println("JT Diff " + String.join(",", diff));
            if (diff.size() == 0) {
            } else {
                    Assert.assertEquals(true, false,
                            "Returned transaction object contain fields that are not a subset (" +
                                    String.join(",", diff) + ")");
            }
        });
    }
}
