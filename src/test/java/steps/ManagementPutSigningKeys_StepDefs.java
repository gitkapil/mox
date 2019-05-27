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

public class ManagementPutSigningKeys_StepDefs extends UtilManager {
    private TestContext testContext;
    private ManagementCommon common;
    private static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final Set<String> APPLICATION_ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String EXISTING_PUBLIC_KEY = "public_key_application_id";
    private static final String SIG_HEADER_LIST_POST_APPLICATION = "header-list-post-application";
    private static final String VALID_BASE64_ENCODED_RSA_PUBLIC_KEY = "valid_base64_encoded_rsa_public_key";

    public ManagementPutSigningKeys_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(this.testContext);
    }

    @Given("^I am a PUT signing key authorized user$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPutSigningKeys().setAuthTokenWithBearer(token));
    }


    @And("^I create a new public key for it$")
    public void createPublicKey() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET,
                token -> testContext.getApiManager().getPostPublicKey().setAuthTokenWithBearer(token));
        testContext.getApiManager().getPostPublicKey().setApplicationId(
                testContext.getApiManager().getPutSigningKeys().getApplicationId()
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

    @When("^I create a new application id for PUT signing key$")
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
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                Sets.newHashSet(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        SIG_HEADER_LIST_POST_APPLICATION).split(",")));
        testContext.getApiManager().getPutSigningKeys().setApplicationId(
                testContext.getApiManager().getPostApplication().applicationIdInResponse()
        );
    }

    @And("^I create a new signing key$")
    public void createSigningKeyExistingId() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET,
                token -> testContext.getApiManager().getPostSigningKeys().setAuthTokenWithBearer(token));
        testContext.getApiManager().getPostSigningKeys().setApplicationId(
                testContext.getApiManager().getPutSigningKeys().getApplicationId()
        );
        testContext.getApiManager().getPostSigningKeys().setActivateAt("2019-01-01T00:00:00Z");
        testContext.getApiManager().getPostSigningKeys().setDeactivateAt("2023-01-01T00:00:00Z");
        testContext.getApiManager().getPostSigningKeys().setEntityStatus("A");
        testContext.getApiManager().getPostSigningKeys().setDescription("Test");
        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + testContext.getApiManager().getPostSigningKeys().getApplicationId() + "/keys/signing";
        testContext.getApiManager().getPostSigningKeys().makeRequest(url);

        Assert.assertEquals(201,
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostSigningKeys().getResponse()),
                "Unable to create signing key");

    }

    @And("^I retrieve the applicationId and keyId from the signing key response$")
    public void getSigningKey() {
        HashMap dataMap = (HashMap)testContext.getApiManager().getPostSigningKeys().getResponse().path(".");
        String newKeyId = dataMap.get("keyId").toString();
        String returnedApplicationId = dataMap.get("applicationId").toString();
        testContext.getApiManager().getPutSigningKeys().setKeyId(newKeyId);
        testContext.getApiManager().getPutSigningKeys().setApplicationId(returnedApplicationId);
    }

    @And("^I update the key id to \"([^\"]*)\"$")
    public void updateKeyId(String keyId) {
        testContext.getApiManager().getPutSigningKeys().setKeyId(keyId);
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
                "alg",
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
