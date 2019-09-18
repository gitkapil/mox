package apiHelpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import managers.UtilManager;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import utils.EnvHelper;
import utils.PropertyHelper;

import java.util.HashMap;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;

public class PostPlatform extends UtilManager {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PostPlatform.class);
    private String authToken;
    private String traceId;
    private String requestDateTime;
    private String platformName;
    private String platformDescription;
    private String platformId;
    public String platformStatus;
    private HashMap<String, String> requestHeader;
    private HashMap requestBody = new HashMap();
    private Response response = null;

    /**
     * Getters
     */
    public HashMap<String, String> getRequestHeader() {
        return requestHeader;
    }

    public HashMap getRequestBody() {
        return requestBody;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getRequestDateTime() {
        return requestDateTime;
    }

    public Response getResponse() {
        return response;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getPlatformStatus() {
        return platformStatus;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getPlatformDescription() {

        return platformDescription;
    }

    /**
     * Setters
     */

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setPlatformName(String platformName) {
        if (platformName.equalsIgnoreCase("longname")) {
            this.platformName = StringUtils.repeat("*", 300);
        } else if (platformName.equalsIgnoreCase("validname")) {
            String name = RandomStringUtils.randomAlphabetic(10);
            this.platformName = name;
        } else if (platformName.equalsIgnoreCase("existingname")) {
            this.platformName = "SHOPOHOLIC";
        } else {
            this.platformName = platformName;
        }
    }

    public void setPlatformDescription(String platformDescription) {
        if (platformDescription.equalsIgnoreCase("validDescription")) {
            this.platformDescription = RandomStringUtils.randomAlphabetic(20);
        } else {
            this.platformDescription = platformDescription;
        }
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public void setPlatformStatus(String platformStatus) {
        this.platformStatus = platformStatus;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    /**
     * Prefixes authToken with "Bearer"
     *
     * @param authToken
     */
    public void setAuthTokenWithBearer(String authToken) {

        this.authToken = "Bearer " + authToken;
    }

    /**
     * This method populates request body
     */
    private void populateRequestBody(String field, String value) {
        if (!"".equals(value)) {
            if (!"no_value".equals(value)) {
                requestBody.put(field, value);
            } else {
                requestBody.put(field, "");
            }
        }
    }

    /**
     * This method creates valid body for POST Platform request.
     *
     * @return
     */
    private HashMap<String, HashMap> returnPOSTPlatformRequestBody() {
        requestBody.clear();
        populateRequestBody("platformName", getPlatformName());
        populateRequestBody("description", getPlatformDescription());
        return requestBody;

    }

    private HashMap<String, String> returnPOSTPlatformRequestHeader() {
        requestHeader = new HashMap<String, String>();
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));

        /*if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(requestHeader);
        }*/
        try {
            requestHeader.put("Digest", getSignatureHelper().calculateContentDigestHeader(
                    new ObjectMapper().writeValueAsBytes(requestBody)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating Digest!", false);
        }
        return requestHeader;
    }

    public void makeRequest(String url) {
        returnPOSTPlatformRequestHeader();
        returnPOSTPlatformRequestBody();

        response = getRestHelper().postRequestWithHeaderAndBody(url, requestHeader, requestBody);
        logger.info("********** POST Platform Response *********** ----> \n" + response.getBody().prettyPrint());
    }


    /**
     * This method hits POST Platform endpoint with invalid header values. "key" values are missing from the header.
     *
     * @param url
     * @param key
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public Response executeRequestWithMissingHeaderKeys(String url, String key, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {

        try {
            returnPOSTPlatformRequestBody();
            HashMap<String, String> header = returnPOSTPlatformRequestHeader();
            header.remove(key);
            response = getRestHelper().postRequestWithHeaderAndBody(url, header, requestBody);

            //testContext.getUtilManager().getSignatureHelper().verifySignature(paymentRequestResponse, "GET", url, Base64.getDecoder().decode(signingKey), signingAlgorithm);
            logger.info("Response: " + response.getBody().asString());
        } catch (Exception e) {
            Assert.assertTrue("Verification of signature failed!", false);
        }

        return response;
    }

    public void makeInvalidPOSTPlatformRequest(String url, String key, String invalidValue) {
        HashMap<String, Object> body = new HashMap<>();
        returnInvalidRequestHeader(key, invalidValue);

        if (this.getPlatformName() != null && !this.getPlatformName().equalsIgnoreCase("null")) {
            body.put("platformName", this.getPlatformName());
        }
        if (this.getPlatformDescription() != null && !this.getPlatformDescription().equalsIgnoreCase("null")) {
            body.put("description", this.getPlatformDescription());
        }
        response = getRestHelper().postRequestWithInvalidHeaderAndBody(url, requestHeader, body);
        logger.info("********** POST Platform Request Response *********** ----> \n" + response.getBody().prettyPrint());
    }

    private HashMap<String, String> returnInvalidRequestHeader(String key, String invalidValue) {
        requestHeader = new HashMap<String, String>();
        if (key.equalsIgnoreCase("Accept")) {
            requestHeader.put("Accept", invalidValue);
            requestHeader.put("Content-Type", "application/json");
            requestHeader.put("Authorization", authToken);
            requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
            requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
            requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        } else if (key.equalsIgnoreCase("Content-Type")) {
            requestHeader.put("Content-Type", invalidValue);
            requestHeader.put("Accept", "application/json");
            requestHeader.put("Authorization", authToken);
            requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
            requestHeader.put("Accept-Language", "en-US");
            requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
            requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        } else if (key.equalsIgnoreCase("Authorization")) {
            requestHeader.put("Authorization", invalidValue);
            requestHeader.put("Accept", "application/json");
            requestHeader.put("Content-Type", "application/json");
            requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
            requestHeader.put("Accept-Language", "en-US");
            requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
            requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        } else if (key.equalsIgnoreCase("Trace-Id")) {
            requestHeader.put("Trace-Id", invalidValue);
            requestHeader.put("Accept", "application/json");
            requestHeader.put("Content-Type", "application/json");
            requestHeader.put("Authorization", authToken);
            requestHeader.put("Accept-Language", "en-US");
            requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
            requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        } else if (key.equalsIgnoreCase("Request-Date-Time")) {
            requestHeader.put("Request-Date-Time", invalidValue);
            requestHeader.put("Accept", "application/json");
            requestHeader.put("Content-Type", "application/json");
            requestHeader.put("Authorization", authToken);
            requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
            requestHeader.put("Accept-Language", "en-US");
            requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        } else if (key.equalsIgnoreCase("Api-Version")) {
            requestHeader.put("Api-Version", invalidValue);
            requestHeader.put("Accept", "application/json");
            requestHeader.put("Content-Type", "application/json");
            requestHeader.put("Authorization", authToken);
            requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
            requestHeader.put("Accept-Language", "en-US");
            requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        }
        requestHeader.put("Accept-Language", "en-US");

        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(requestHeader);
        }
        return requestHeader;
    }

    /**
     * This method hits POST Platform endpoint and creates header and body values
     *
     * @param url
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public Response executeRequest(String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {

        try {
            returnPOSTPlatformRequestBody();
            response = getRestHelper().postRequestWithHeaderAndBody(url,
                    returnPOSTPlatformRequestHeader(),
                    requestBody);

            logger.info("********** POST Platform Response *********** ----> " + response.getBody().asString());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue("Verification of signature failed!", false);

        }
        return response;
    }



    /*public void setPlatformNameDescription(String description) {
        if (description.equalsIgnoreCase("bigDescription")) {
            this.platformDescription = StringUtils.repeat("*", 300);
        } else {
            this.platformDescription = description;
        }
    }*/

}
