package apiHelpers;
import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.EnvHelper;
import utils.PropertyHelper;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;


public class PostCredentialsMerchants extends UtilManager {

    private String authToken;
    private String applicationId;
    private String activateAt;
    private String deactivateAt;
    private String entityStatus;
    private String credentialName;
    private Response response = null;
    private HashMap<String, String> requestHeader = new HashMap();
    final static Logger logger = Logger.getLogger(PostSigningKeys.class);
    private HashMap requestBody = new HashMap();

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    private HashMap returnRequestBody(String credentialName) {
        requestBody.clear();
        requestBody.put("credentialName", credentialName);
        return requestBody;
    }

    private HashMap returnEmptyRequestBody(String credentialName) {
        requestBody.put("credentialName", credentialName);
        requestBody.clear();
        return requestBody;
    }

    private HashMap<String, String> returnRequestHeader() {
        requestHeader.clear();
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(requestHeader);
        }
        return requestHeader;
    }

    private HashMap<String, String> returnRequestHeaderWithInvalidValues(String keys,String headerValues) {
        requestHeader.clear();
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(requestHeader);
        }
        if(requestHeader.containsKey(keys)){
            requestHeader.remove(keys);
            requestHeader.put(keys,headerValues);
        }
        return requestHeader;
    }

    private HashMap<String, String> returnRequestHeaderWithMissingHeaderValues(String keys) {
        requestHeader.clear();
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(requestHeader);
        }
        if(requestHeader.containsKey(keys)){
            requestHeader.remove(keys);
        }
        return requestHeader;
    }
    public void makeRequest(String url, String credentialName) {
        returnRequestHeader();
        response = getRestHelper().postRequestWithHeaderAndBody(url, requestHeader,returnRequestBody(credentialName));
        logger.info("Create Credential Response -->>>>" + response.prettyPrint());
    }

    public void makeRequestWithInvalidHeaders(String url,String keys, String headerValue) {
        returnRequestHeaderWithInvalidValues(keys, headerValue);
        response = getRestHelper().postRequestWithHeaderAndBody(url, requestHeader,returnRequestBody(credentialName));
        logger.info("Create Credential Response -->>>>" + response.prettyPrint());
    }

    public void makeRequestWithMissingHeaderValues(String url,String keys) {
        returnRequestHeaderWithMissingHeaderValues(keys);
        response = getRestHelper().postRequestWithHeaderAndBody(url, requestHeader,returnRequestBody(credentialName));
        logger.info("Create Credential Response -->>>>" + response.prettyPrint());
    }

    public void makeRequestWithoutInputBody(String url) {
        returnRequestHeader();
        response = getRestHelper().postRequestWithHeaders(url, requestHeader);
        logger.info("Create Credential Response -->>>>" + response.prettyPrint());
    }

    public void makeRequestWithoutCredentialName(String url, String credentialName) {
        returnRequestHeader();
        response = getRestHelper().postRequestWithHeaderAndBody(url, requestHeader,returnEmptyRequestBody(credentialName));
        logger.info("Create Credential Response -->>>>" + response.prettyPrint());
    }

    public void setAuthTokenWithBearer(String authToken) {
        this.authToken = "Bearer " + authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;

    }
    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getActivateAt() {
        return activateAt;
    }

    public void setActivateAt(String activateAt) {
        this.activateAt = activateAt;
    }

    public String getDeactivateAt() {
        return deactivateAt;
    }

    public void setDeactivateAt(String deactivateAt) {
        this.deactivateAt = deactivateAt;
    }

    public String getEntityStatus() {
        return entityStatus;
    }

    public void setEntityStatus(String entityStatus) {
        this.entityStatus = entityStatus;
    }

    public String getCredentialName() { return credentialName; }

    public void setCredentialName(String credentialName) {
        if (credentialName.equalsIgnoreCase("validName")) {
            this.credentialName = RandomStringUtils.randomAlphabetic(10);
        }if (credentialName.equalsIgnoreCase("tooLong")) {
            this.credentialName = StringUtils.repeat("*", 256);
        }
            else    {
                this.credentialName =credentialName;
            }
        }

   public Response executePostRequestWithMissingHeaderKeys(String url, String keys, String credentialName) {
        try {
            HashMap<String, String> header = returnRequestHeaderWithMissingKeys("POST", new URL(url).getPath(), keys);
            response = getRestHelper().postRequestWithHeaderAndBody(url, header, returnRequestBody(credentialName));
            logger.info("Response: " + response.getBody().prettyPrint());
        } catch (Exception e) {
            Assert.assertTrue("Verification of signature failed!", false);
        }
        return response;
    }

    public HashMap<String, String> returnRequestHeaderWithMissingKeys(String method, String url, String keys) throws IOException {
        requestHeader.clear();
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(requestHeader);
        }
        requestHeader.remove(keys);
        return requestHeader;
    }


}