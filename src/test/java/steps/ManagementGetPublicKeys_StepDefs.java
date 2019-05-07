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

public class ManagementGetPublicKeys_StepDefs extends UtilManager {
    public static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    public static final Set<String> INCORRECT_ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String EXISTING_PUBLIC_KEY = "public_key_application_id";

    TestContext testContext;
    ManagementCommon common;

    public ManagementGetPublicKeys_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(this.testContext);
    }

    @Given("^I am a GET create keys authorized DRAGON user with the correct privileges$")
    public void getUser() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getGetPublicKey().setAuthTokenWithBearer(token));
    }

    @Given("^I am a GET create keys authorized DRAGON user with the incorrect privileges$")
    public void getInvalidUser() {
        common.iAmAnAuthorizedDragonUser(INCORRECT_ROLE_SET, token -> testContext.getApiManager().getGetPublicKey().setAuthTokenWithBearer(token));
    }


    @When("^I am able to get a list of public keys using an existing application key$")
    public void getList() {
        String applicationId = getFileHelper().getValueFromPropertiesFile(Hooks.envProperties,
                EXISTING_PUBLIC_KEY);

        testContext.getApiManager().getGetPublicKey().setApplicationId(applicationId);
        getListOfKeys();
    }

    @When("^I get a list of public keys using \"([^\"]*)\" as application key$")
    public void getListKeys(String applicationId) {
        testContext.getApiManager().getGetPublicKey().setApplicationId(applicationId);
        getListOfKeys();
    }

    @Then("^the get public keys response should have a \"([^\"]*)\" error with \"([^\"]*)\" description and \"([^\"]*)\" error code$")
    public void failedResponse(String httpStatus, String errorDescription, String errorCode) {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetPublicKey().getResponse()),
                Integer.parseInt(httpStatus),
                "Http status expected " + httpStatus + " but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetPublicKey().getResponse())
        );

        if (errorCode.length() > 0) {
            Assert.assertEquals(
                    getRestHelper().getErrorCode(testContext.getApiManager().getGetPublicKey().getResponse()),
                    errorCode,
                    "Error code expected " + errorCode + " but got " +
                            getRestHelper().getErrorCode(testContext.getApiManager().getGetPublicKey().getResponse())
            );
        }

        if (errorDescription.length() > 0) {
            if (!getRestHelper().getErrorDescription(testContext.getApiManager().getGetPublicKey().getResponse()).contains(errorDescription)) {
                Assert.assertEquals(
                        getRestHelper().getErrorDescription(testContext.getApiManager().getGetPublicKey().getResponse()),
                        errorDescription,
                        "Error description expected '" + errorDescription + "' but got '" +
                                getRestHelper().getErrorDescription(testContext.getApiManager().getGetPublicKey().getResponse())
                );
            }

        }
    }


    @Then("^I should receive a successful get public key response$")
    public void successCall() {
        Assert.assertEquals(
                getRestHelper().getResponseStatusCode(
                        testContext.getApiManager().getGetPublicKey().getResponse()),
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
                "lastUpdatedAt"
        };

        ArrayList returnedObject = testContext.getApiManager().getGetPublicKey().getResponse().path(".");
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

    private void getListOfKeys() {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                RESOURCE_ENDPOINT_PROPERTY_NAME) + "/";
        testContext.getApiManager().getGetPublicKey().getKeys(url);
    }
}
