package apiHelpers;
import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.PropertyHelper;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

public class GetPlatform extends UtilManager {
    private String authToken;
    private String platformName;
    private String platformDescription;
    private String status;
    private String platformId;
    private String createdAt;
    private String lastUpdatedAt;
    private String createdBy;
    private String updatedBy;

    final static Logger logger = Logger.getLogger(GetPlatform.class);

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
        this.authToken = "Bearer "+ authToken;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformStatus() {
        return status;
    }

    public void setPlatformStatus(String platformStatus) {
        this.status = platformStatus;
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

    public String getPlatformDescription() {
        return platformDescription;
    }

    public void setPlatformNameDescription(String description) {
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


    private HashMap<String, String> returnRequestHeader(String method, String url, String authToken) {
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        return requestHeader;

    }

    private HashMap<String, String> returnRequestHeaderWithMissingHeaderKey(String method, String url, String authToken, String keys) {
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        requestHeader.remove(keys);
        return requestHeader;

    }

    public void executeGetPlatformRequest(String url) {
        try {
            response = getRestHelper().getRequestWithHeaders(url,
                    returnRequestHeader("GET", new URL(url).getPath(),
                            authToken));
            logger.info("List of platform response ******-->  " + response.getBody().prettyPrint());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Assert.assertTrue("Unable to get URL", false);
        }
    }

    public void makeRequestWithMissingHeader(String url, String keys) {
        try {
            response = getRestHelper().getRequestWithHeaders(url,
                    returnRequestHeaderWithMissingHeaderKey("GET", new URL(url).getPath(),
                            authToken, keys));
            logger.info("List of platform response ******-->  " + response.getBody().prettyPrint());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Assert.assertTrue("Unable to get URL", false);
        }
    }

    public void setAuthTokenWithoutBearer(String authToken) {
        this.authToken =authToken;
    }
}
