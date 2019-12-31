package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.log4j.Logger;
import utils.EnvHelper;
import utils.PropertyHelper;

import java.util.HashMap;

public class GetCredentialsMerchants extends UtilManager {

    private String authToken;
    private String applicationId;
    private String activateAt;
    private String deactivateAt;
    private String entityStatus;
    private String credentialName;
    private String getCredentialsURL;
    private String getFilterStatus;
    private String getGetCredentialId;
    private String getGetCredentialName;
    private Response response = null;
    private HashMap<String, String> requestHeader = new HashMap();
    final static Logger logger = Logger.getLogger(GetCredentialsMerchants.class);
    private HashMap requestBody = new HashMap();

    public Response getResponse() {
        return response;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getGetCredentialsUrl() {
        return getCredentialsURL;
    }

    public void setGetCredentialsUrl(String url) {
        this.getCredentialsURL = url;
    }

    public String getFilterStatus() {
        return getFilterStatus;
    }

    public void setFilterStatus(String filterStatus) {
        this.getFilterStatus = filterStatus;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    private HashMap<String, String> getListHeaderWithMissingValues(String method, String url, String authToken, String nullHeader) {
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        requestHeader.remove(nullHeader);
        return requestHeader;
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

    private HashMap<String, String> returnRequestHeaderWithInvalidValues(String keys, String headerValues) {
        requestHeader.clear();
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(requestHeader);
        }
        if (requestHeader.containsKey(keys)) {
            requestHeader.remove(keys);
            requestHeader.put(keys, headerValues);
        }
        return requestHeader;
    }

    public void makeRequest(String url) {
        returnRequestHeader();
        response = getRestHelper().getRequestWithHeaders(url, requestHeader);
        logger.info("********** GET Credentials Response *********** \n" + response.prettyPrint());
    }

    public void makeRequestWithInvalidHeaders(String url, String keys, String headerValue) {
        returnRequestHeaderWithInvalidValues(keys, headerValue);
        response = getRestHelper().getRequestWithHeaders(url, requestHeader);
        logger.info("********** GET Credentials Response *********** \n" + response.prettyPrint());
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

    public String getGetCredentialId() {
        return getGetCredentialId;
    }

    public void setGetCredentialId(String getGetCredentialId) {
        this.getGetCredentialId = getGetCredentialId;
    }

    public String getGetCredentialName() {
        return getGetCredentialName;
    }

    public void setGetCredentialName(String getGetCredentialName) {
        this.getGetCredentialName = getGetCredentialName;
    }
}
