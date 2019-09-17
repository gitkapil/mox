package apiHelpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import utils.EnvHelper;
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

    public void setPlatformDescription(String platformDescription) {
        this.platformDescription = platformDescription;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(String lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(String platformStatus) {
        this.platformStatus = platformStatus;
    }

    public HashMap<String, String> getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(HashMap<String, String> requestHeader) {
        this.requestHeader = requestHeader;
    }

    public HashMap<String, String> getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(HashMap<String, String> requestBody) {
        this.requestBody = requestBody;
    }

    public String getPlatformDescription() {
        return platformDescription;
    }

    public void setPlatformNameDescription(String description)
    {
        if (description.equalsIgnoreCase("bigDescription")) {
            this.platformDescription = StringUtils.repeat("*", 300);
        } else {
            this.platformDescription = description;
        }
    }

    private Response response = null;
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

    private void returnRequestHeader() {
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
        try {
            requestHeader.put("Digest", getSignatureHelper().calculateContentDigestHeader(
                    new ObjectMapper().writeValueAsBytes(requestBody)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating Digest!", false);
        }
    }

    public void makeRequest(String url) {
        returnRequestBody();
        returnRequestHeader();
        response = getRestHelper().getRequestWithHeaders(url, requestHeader);

    }


}
