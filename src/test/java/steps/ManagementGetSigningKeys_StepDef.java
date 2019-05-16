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

public class ManagementGetSigningKeys_StepDef extends UtilManager {
    TestContext testContext;
    ManagementCommon common;
    private static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final Set<String> APPLICATION_ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String SIG_HEADER_LIST_POST_APPLICATION = "header-list-post-application";
    private static final String VALID_BASE64_ENCODED_RSA_PUBLIC_KEY = "valid_base64_encoded_rsa_public_key";

    public ManagementGetSigningKeys_StepDef(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(this.testContext);
    }

    @Given("^I am a user with permissions to use signing key$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getGetSigningKey().setAuthTokenWithBearer(token));
    }

    @And("^I create a public key for this application$")
    public void createPublicKey() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET,
                token -> testContext.getApiManager().getPostPublicKey().setAuthTokenWithBearer(token));
        testContext.getApiManager().getPostPublicKey().setApplicationId(
                testContext.getApiManager().getGetSigningKey().getApplicationId()
        );


        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                RESOURCE_ENDPOINT_PROPERTY_NAME) + "/";
        String value = getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, VALID_BASE64_ENCODED_RSA_PUBLIC_KEY);
        String activateAt = "2019-01-01T00:00:00Z";
        String deactivateAt = "2023-02-02T00:00:00Z";
        String entityStatus = "A";
        String description = "Test description";

        testContext.getApiManager().getPostPublicKey().postPublicKeys(
                url,
                value,
                activateAt,
                deactivateAt,
                entityStatus,
                description);

        Assert.assertEquals(201,
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostPublicKey().getResponse()),
                "Unable to create Post public key");
    }

    @When("^I create a new application id for get signing key$")
    public void createApplication() {
        common.iAmAnAuthorizedDragonUser(APPLICATION_ROLE_SET,
                token -> testContext.getApiManager().getPostApplication().setAuthTokenWithBearer(token));
        testContext.getApiManager().getPostApplication().setClientId(UUID.randomUUID().toString());
        testContext.getApiManager().getPostApplication().setSubUnitId(UUID.randomUUID().toString());
        testContext.getApiManager().getPostApplication().setPeakId(UUID.randomUUID().toString());
        testContext.getApiManager().getPostApplication().setOrganisationId(UUID.randomUUID().toString());
        testContext.getApiManager().getPostApplication().setRequestDateTime(getDateHelper().getUTCNowDateTime());
        testContext.getApiManager().getPostApplication().setTraceId(getGeneral().generateUniqueUUID());
        testContext.getApiManager().getPostApplication().executeRequest(
                getRestHelper().getBaseURI()+getFileHelper()
                        .getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME),
                testContext.getApiManager().getAccessToken().getClientId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_key"),
                Sets.newHashSet(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        SIG_HEADER_LIST_POST_APPLICATION).split(",")));
        testContext.getApiManager().getGetSigningKey().setApplicationId(
                testContext.getApiManager().getPostApplication().applicationIdInResponse()
        );
    }

    @And("^I make a request to get signing keys$")
    public void makeRequest() {
        String url = getRestHelper().getBaseURI() + getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                RESOURCE_ENDPOINT_PROPERTY_NAME) + "/";

        testContext.getApiManager().getGetSigningKey().makeCall(url);
    }

    @When("^I make a request with \"([^\"]*)\" as application id$")
    public void setApplicationId(String applicationId) {
        testContext.getApiManager().getGetSigningKey().setApplicationId(applicationId);

        makeRequest();
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

        if (!getRestHelper().getErrorDescription(testContext.getApiManager().getGetSigningKey().getResponse()).contains(errorDescription)) {
            Assert.assertEquals(
                    getRestHelper().getErrorDescription(testContext.getApiManager().getGetSigningKey().getResponse()),
                    errorDescription,
                    "Error description expected " + errorDescription + " but got " +
                            getRestHelper().getErrorDescription(testContext.getApiManager().getGetSigningKey().getResponse())
            );
        }
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
