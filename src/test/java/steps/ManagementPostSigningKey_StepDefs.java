package steps;

import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.TestContext;
import managers.UtilManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import utils.Constants;

import javax.xml.ws.Response;
import java.util.*;

public class ManagementPostSigningKey_StepDefs extends UtilManager {
    private static final Set<String> ROLE_SET = Sets.newHashSet("ApplicationKey.ReadWrite.All");
    private static final Set<String> APPLICATION_ROLE_SET = Sets.newHashSet("Application.ReadWrite.All");
    private static final String RESOURCE_ENDPOINT_PROPERTY_NAME = "create_application_resource";
    private static final String SIG_HEADER_LIST_POST_APPLICATION = "header-list-post-application";
    private static final String VALID_BASE64_ENCODED_RSA_PUBLIC_KEY = "valid_base64_encoded_rsa_public_key";
    TestContext testContext;
    ManagementCommon common;

    public ManagementPostSigningKey_StepDefs(TestContext testContext) {
        this.testContext = testContext;
        common = new ManagementCommon(testContext);
    }

    @And("^I create a new pubic key for signing key purposes$")
    public void createPublicKey() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET,
                token -> testContext.getApiManager().getPostPublicKey().setAuthTokenWithBearer(token));
        testContext.getApiManager().getPostPublicKey().setApplicationId(
                testContext.getApiManager().getPostSigningKeys().getApplicationId()
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

    @And("^I create a new application id for signing key$")
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
                getRestHelper().getBaseURI() + getFileHelper()
                        .getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME),
                testContext.getApiManager().getMerchantManagementSigningKeyId(),
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "signing_algorithm"),
                testContext.getApiManager().getMerchantManagementSigningKey(),
                Sets.newHashSet(getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties,
                        SIG_HEADER_LIST_POST_APPLICATION).split(",")));
        testContext.getApiManager().getPostSigningKeys().setApplicationId(
                testContext.getApiManager().getPostApplication().applicationIdInResponse()
        );
    }

    @Given("^I am an authorized Signing Key DRAGON user$")
    public void login() {
        common.iAmAnAuthorizedDragonUser(ROLE_SET, token -> testContext.getApiManager().getPostSigningKeys().setAuthTokenWithBearer(token));
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

    @When("^I make a request to create a new signing key with \"([^\"]*)\"$")
    public void makeRequest(String applicationID) {

        String url = getRestHelper().getBaseURI() +
                getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, RESOURCE_ENDPOINT_PROPERTY_NAME)
                + "/" + applicationID + "/keys/signing";

        testContext.getApiManager().getPostSigningKeys().makeRequest(url);
    }

    @Then("^the create signing key response should give a \"([^\"]*)\" http status with error code \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void invalidResponse(String httpStatus, String errorCode, String errorDescription) {
        Assert.assertEquals(
                Integer.parseInt(httpStatus),
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

        if (!getRestHelper().getErrorDescription(testContext.getApiManager().getPostSigningKeys().getResponse()).contains(errorDescription)) {
            Assert.assertEquals(
                    errorDescription,
                    getRestHelper().getErrorDescription(testContext.getApiManager().getPostSigningKeys().getResponse()),
                    "Expected error description " + errorDescription + " but got " +
                            getRestHelper().getErrorDescription(testContext.getApiManager().getPostSigningKeys().getResponse())
            );
        }
    }

    @Then("^the create signing key response should be successful$")
    public void validResponse() {
        Assert.assertEquals(
                HttpStatus.SC_CREATED,
                getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostSigningKeys().getResponse()),
                "Expected 201 but got " +
                        getRestHelper().getResponseStatusCode(testContext.getApiManager().getPostSigningKeys().getResponse())
        );
        String returnedObject = testContext.getApiManager().getPostSigningKeys().getResponse().getBody().prettyPrint();
        System.out.println("response String: " + returnedObject);

        if (returnedObject != null) {
            Assert.assertTrue(returnedObject.contains(Constants.KEY_ID));
            Assert.assertTrue(returnedObject.contains(Constants.APPLICATION_ID));
            Assert.assertTrue(returnedObject.contains(Constants.VALUE));
            Assert.assertTrue(returnedObject.contains(Constants.ALG));
            Assert.assertTrue(returnedObject.contains(Constants.TYPE));
            Assert.assertTrue(returnedObject.contains(Constants.SIZE));
            Assert.assertTrue(returnedObject.contains(Constants.ACTIVATE_AT));
            Assert.assertTrue(returnedObject.contains(Constants.DEACTIVAT_AT));
            Assert.assertTrue(returnedObject.contains(Constants.ENTITY_STATUS));
            Assert.assertTrue(returnedObject.contains(Constants.DESCRIPTION));
            Assert.assertTrue(returnedObject.contains(Constants.CREATED_AT));
            Assert.assertTrue(returnedObject.contains(Constants.LAST_UPDATED_AT));
            Assert.assertEquals(returnedObject.split(",").length, 12);
        } else {
            getRestHelper().getResponseStatusCode(testContext.getApiManager().getGetSigningKey().getResponse());
        }
    }
}

