package apiHelpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import utils.EnvHelper;
import utils.PropertyHelper;

import java.util.HashMap;

public class PutPlatform extends UtilManager {
    private String authToken;
    private String platformName;
    private String platformDescription;
    private String platformStatus;


    private String platformId;


    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
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

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
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

    private HashMap<String, String> returnRequestBody() {
        requestBody.clear();
        requestBody.put("description", getPlatformDescription());
        requestBody.put("platformName", getPlatformName());
        requestBody.put("status", getPlatformStatus());
        return requestBody;
    }

    private HashMap<String, String> returnRequestBodyWithOneField(String onlyInputBodyField) {
        requestBody.clear();
        if (onlyInputBodyField.equalsIgnoreCase("description")) {
            requestBody.put("description", getPlatformDescription());
        } else if (onlyInputBodyField.equalsIgnoreCase("status")) {
            requestBody.put("status", getPlatformStatus());
        } else {
            requestBody.put("platformName", getPlatformName());
        }

        return requestBody;
    }

    private HashMap<String, String> returnRequestBodyWithMissingOptionalFields(String missingBody) {
        requestBody.clear();
        requestBody.put("description", getPlatformDescription());
        requestBody.put("platformName", getPlatformName());
        requestBody.put("status", getPlatformStatus());
        requestBody.remove(missingBody);
        return requestBody;
    }

    public void setPlatformName(String platformName) {
        if (platformName.equalsIgnoreCase("longName")) {
            this.platformName = StringUtils.repeat("*", 300);
        } else if (platformName.equalsIgnoreCase("validName")) {
            this.platformName = RandomStringUtils.randomAlphabetic(10);
            ;
        } else if (platformName.equalsIgnoreCase("onlySpecialCharacter")) {
            this.platformName = RandomStringUtils.random(10, 33, 47, false, false);
            ;
        } else if (platformName.equalsIgnoreCase("existingPlatformName")) {
            this.platformName = "DRAGON PLATFORM";
        } else if (platformName.equalsIgnoreCase("space")) {
            this.platformName = " ";
        } else if (platformName.equalsIgnoreCase("emptySpace")) {
            this.platformName = "";
        } else {
            this.platformName = platformName;
        }
    }

    public void setPlatformDescription(String platformDescription) {
        if (platformDescription.equalsIgnoreCase("validDescription")) {
            this.platformDescription = RandomStringUtils.randomAlphabetic(20);
        } else if (platformDescription.equalsIgnoreCase("longDescription")) {
            this.platformDescription = StringUtils.repeat("*", 300);
        } else if (platformDescription.equalsIgnoreCase("space")) {
            this.platformDescription = " ";
        } else if (platformDescription.equalsIgnoreCase("emptySpace")) {
            this.platformDescription = "";
        } else {
            this.platformDescription = platformDescription;
        }
    }

    public void setPlatformStatus(String platformStatus) {
        if (platformStatus.equalsIgnoreCase("active")) {
            this.platformStatus = "A";
        } else if (platformStatus.equalsIgnoreCase("deactivated")) {
            this.platformStatus = "D";
        } else if (platformStatus.equalsIgnoreCase("invalidStatus")) {
            this.platformStatus = "P";
        } else if (platformStatus.equalsIgnoreCase("space")) {
            this.platformStatus = " ";
        } else if (platformStatus.equalsIgnoreCase("emptySpace")) {
            this.platformStatus = "";
        }
    }

    private HashMap<String, String> returnRequestHeader() {
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
        return requestHeader;
    }

    private HashMap<String, String> returnRequestHeaderWithMissingKey(String keys) {
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
        requestHeader.remove(keys);
        return requestHeader;
    }

    public void makeRequest(String url) {

        response = getRestHelper().putRequestWithHeaderAndBody(url,
                returnRequestHeader(), returnRequestBody());
        System.out.println("response after make request: " + response.getBody().prettyPrint());
    }

    public void makeRequest(String url, String missingBody) {

        response = getRestHelper().putRequestWithHeaderAndBody(url,
                returnRequestHeader(), returnRequestBodyWithMissingOptionalFields(missingBody));
        System.out.println("response after make request: " + response.getBody().prettyPrint());
    }

    public void makeRequestWithMissingHeader(String url, String keys) {

        response = getRestHelper().putRequestWithHeaderAndBody(url,
                returnRequestHeaderWithMissingKey(keys), returnRequestBody());
        System.out.println("response after make request: " + response.getBody().prettyPrint());
    }

    public void makeRequestWithOneFiled(String url, String onlyBodyField) {

        response = getRestHelper().putRequestWithHeaderAndBody(url,
                returnRequestHeader(), returnRequestBodyWithOneField(onlyBodyField));
        System.out.println("response after make request: " + response.getBody().prettyPrint());
    }

    public void setAuthTokenWithBearer(String authToken) {
        this.authToken = "Bearer " + authToken;
    }
    public void setAuthTokenWithoutBearer(String authToken) {
        this.authToken =authToken;
    }
}
