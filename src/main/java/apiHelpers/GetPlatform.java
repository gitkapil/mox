package apiHelpers;
import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.commons.lang3.StringUtils;
import utils.PropertyHelper;
import java.util.HashMap;

public class GetPlatform extends UtilManager {
    private String authToken;
    private String platformName;
    private String platformDescription;
    private String platformStatus;
    private String platformId;
    private String createdAt;
    private String lastUpdatedAt;
    private String createdBy;
    private String updatedBy;
    private String traceId;
    private String requestDateTime;

    /**
     * Getters
     */

    public String getTraceId() {
        return traceId;
    }

    public String getRequestDateTime() {
        return requestDateTime;
    }

    public String getPlatformDescription() {
        return platformDescription;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getPlatformStatus() {
        return platformStatus;
    }

    public HashMap<String, String> getRequestHeader() {
        return requestHeader;
    }

    public HashMap<String, String> getRequestBody() {
        return requestBody;
    }


    /**
     * Setters
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public void setPlatformStatus(String platformStatus) {
        this.platformStatus = platformStatus;
    }

    public void setLastUpdatedAt(String lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public void setRequestHeader(HashMap<String, String> requestHeader) {
        this.requestHeader = requestHeader;
    }

    public void setRequestBody(HashMap<String, String> requestBody) {
        this.requestBody = requestBody;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setPlatformDescription(String platformDescription) {
        this.platformDescription = platformDescription;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public void setPlatformNameDescription(String description) {
        if (description.equalsIgnoreCase("bigDescription")) {
            this.platformDescription = StringUtils.repeat("*", 300);
        } else {
            this.platformDescription = description;
        }
    }

    public Response response = null;
    private HashMap<String, String> requestHeader = new HashMap();
    private HashMap<String, String> requestBody = new HashMap();

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    private void returnRequestBody() {
        requestBody.clear();

        if (platformDescription != null) {
            requestBody.put("platformDescription", platformDescription);
        }
        if (platformName != null) {
            requestBody.put("platformName", platformName);
        }
        if (platformStatus != null) {
            requestBody.put("status", platformStatus);
        }
    }

    private HashMap<String, String> returnGETPlatformRequestHeader() {
        requestHeader = new HashMap<String, String>();
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
       return requestHeader ;
    }

    public void makeRequest(String url) {

        response = getRestHelper().getRequestWithHeaders(url, returnGETPlatformRequestHeader());
        //System.out.println("GET Platform Response : \n" + response.getBody().asString());
    }

    /**
     * Prefixes authToken with "Bearer"
     *
     * @param authToken
     */
    public void setAuthTokenWithBearer(String authToken) {

        this.authToken = "Bearer " + authToken;
        System.out.println("this.authToken: " +this.authToken);
    }


}
