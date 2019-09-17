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

public class PostPlatform extends UtilManager {
    private String authToken;
    private String platformName;
    private String platformDescription;

    private Response response = null;

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
        response = getRestHelper().postRequestWithHeaderAndBody(url,
                requestHeader, requestBody);
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
}
