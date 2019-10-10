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

import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;


public class OneClickMerchantOnboarding extends UtilManager {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(OneClickMerchantOnboarding.class);

    private String authToken;
    private String clientId;
    private String peakId;
    private String subUnitId;
    private String organisationId;
    private String description;
    private String traceId;
    private String platform;
    private String platformId;
    private String pdfChannel;
    private String passwordChannel;
    private String clientName;
    private String applicationName;
    private String grantUrl;
    private String requestDateTime;
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

    public Response getResponse() {
        return response;
    }

    public String getTraceId() {
        return traceId;
    }

    public Response getPostApplicationRequestResponse() {
        return response;
    }

    public Response getOneClickOnboardingRequestResponse() {
        return response;
    }

    public String getRequestDateTime() {
        return requestDateTime;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getClientId() {
        return clientId;
    }

    public String getPeakId() {
        return peakId;
    }

    public String getSubUnitId() {
        return subUnitId;
    }

    public String getOrganisationId() {
        return organisationId;
    }

    public String getDescription() {
        return description;
    }

    public String getPlatform() {
        return platform;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getPdfChannel() {
        return pdfChannel;
    }

    public String getPasswordChannel() {
        return passwordChannel;
    }

    public String getClientName() {
        return clientName;
    }

    public String getGrantUrl() {
        return grantUrl;
    }

    public String getApplicationName() {
        return applicationName;
    }


    /**
     * Setters
     */
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setPeakId(String peakId) {
        this.peakId = peakId;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public void setPdfChannel(String pdfChannel) {
        this.pdfChannel = pdfChannel;
    }

    public void setPasswordChannel(String passwordChannel) {
        this.passwordChannel = passwordChannel;
    }

    public void setSubUnitId(String subUnitId) {
        this.subUnitId = subUnitId;
    }

    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

    public void setDescription(String description) {
        if (description.equalsIgnoreCase("random")) {
            String desc = RandomStringUtils.randomAlphabetic(10);
            this.description = desc;
        } else if (description.equalsIgnoreCase("longdescription")) {
            this.description = StringUtils.repeat("*", 300);
        } else {
            this.description = description;
        }
    }

    public void setResponse(Response response) {
        this.response = response;
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
     * This method creates valid body for the POST create client request.
     *
     * @return
     */
    public HashMap<String, HashMap> returnRequestBody() {
        requestBody.clear();


        populateRequestBody("clientId", getClientId());
        populateRequestBody("peakId", getPeakId());
        populateRequestBody("subUnitId", getSubUnitId());
        populateRequestBody("organisationId", getOrganisationId());

        return requestBody;
    }

    /**
     * This method creates valid body for the POST one click merchant onboarding request.
     *
     * @return
     */
    public HashMap<String, HashMap> returnOneclickRequestBody() {
        requestBody.clear();

        populateRequestBody("name", getClientName());
        populateRequestBody("peakId", getPeakId());
        populateRequestBody("subUnitId", getSubUnitId());
        populateRequestBody("organisationId", getOrganisationId());
        populateRequestBody("platform", getPlatform());
        populateRequestBody("pdfChannel", getPdfChannel());
        populateRequestBody("passwordChannel", getPasswordChannel());
        populateRequestBody("applicationDescription", getDescription());

        return requestBody;
    }

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
     * This method hits POST create client Request endpoint with an existing header and body
     *
     * @param url
     * @param header
     * @param body
     * @return
     */
    public Response retrieveExistingHeaderBody(String url, HashMap header, HashMap body) {

        response = getRestHelper().postRequestWithHeaderAndBody(url, header, body);

        logger.info("********** POST Application Response *********** ----> " + response.getBody().asString());

        return response;
    }


    /**
     * This method hits Post one click merchant onboarding endpoint with invalid header values. "key" values are missing from the header.
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
            returnRequestBody();
            HashMap<String, String> header = returnRequestHeader();
            header.remove(key);
            response = getRestHelper().postRequestWithHeaderAndBody(url, header, requestBody);

            //testContext.getUtilManager().getSignatureHelper().verifySignature(paymentRequestResponse, "GET", url, Base64.getDecoder().decode(signingKey), signingAlgorithm);
            logger.info("Response: " + response.getBody().asString());
        } catch (Exception e) {
            Assert.assertTrue("Verification of signature failed!", false);
        }

        return response;
    }


    /**
     * This method hits Post one click merchant onboarding endpoint with invalid header values. "key" values are invalid values provided in header.
     *
     * @param url
     * @param key
     * @param invalidValue
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public Response executeRequestWithInvalidHeaderKeys(String url, String key, String invalidValue, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {

        try {
            returnOneclickRequestBody();
            HashMap<String, String> header = returnRequestHeader();
            header.replace(key, invalidValue);
            response = getRestHelper().postRequestWithHeaderAndBody(url, header, requestBody);

            //testContext.getUtilManager().getSignatureHelper().verifySignature(paymentRequestResponse, "GET", url, Base64.getDecoder().decode(signingKey), signingAlgorithm);
            logger.info("Response: " + response.getBody().asString());
        } catch (Exception e) {
            Assert.assertTrue("Verification of signature failed!", false);
        }

        return response;
    }

    /**
     * @returns applicationId from the response
     */
    public String applicationIdInResponse() {
        return getRestHelper().getResponseBodyValue(response, "applicationId");

    }

    /**
     * @returns webLink from the response
     */
    public String peakIdInResponse() {
        return getRestHelper().getResponseBodyValue(response, "peakId");

    }

    /**
     * @returns subUnitId from the response
     */
    public String subUnitIdInResponse() {
        return getRestHelper().getResponseBodyValue(response, "subUnitId");

    }

    /**
     * @returns organisationId from the response
     */
    public String organisationIdInResponse() {
        return getRestHelper().getResponseBodyValue(response, "organisationId");

    }

    /**
     * @returns clientId from the response
     */
    public String clientIdInResponse() {
        return getRestHelper().getResponseBodyValue(response, "clientId");

    }

    /**
     * @returns notificationHost from the response
     */
    public String notificationHostInResponse() {
        return getRestHelper().getResponseBodyValue(response, "notificationHost");

    }

    /**
     * @returns notificationPath from the response
     */
    public String notificationPathInResponse() {
        return getRestHelper().getResponseBodyValue(response, "notificationPath");

    }

    /**
     * This method hits POST payment request and the "digest" is not included for signature calculation
     *
     * @param url
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public Response executeRequestWithoutDigest(String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {

        try {
            returnRequestBody();
            HashMap<String, String> header = returnRequestHeaderWithoutDigest("POST", new URL(url).getPath(), signingKeyId, signingAlgorithm, signingKey, headerElementsForSignature);
            response = getRestHelper().postRequestWithHeaderAndBody(url, header, requestBody);

            logger.info("Response: " + response.getBody().asString());
        } catch (Exception e) {
            Assert.assertTrue("Verification of signature failed!", false);
        }

        return response;
    }


    /**
     * This method creates a valid header but without digest
     *
     * @param method
     * @param url
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public HashMap<String, String> returnRequestHeaderWithoutDigest(String method, String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {
        requestHeader = new HashMap<String, String>();
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", traceId);
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Request-Date-Time", getRequestDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));

        try {
            requestHeader.put("Signature", getSignatureHelper().calculateSignature(method, url,
                    Base64.getDecoder().decode(signingKey), signingAlgorithm, signingKeyId,
                    headerElementsForSignature, requestHeader));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating Signature!", false);
        }
        return requestHeader;
    }

    public void makeRequest(String url) {
        HashMap<String, Object> body = new HashMap<>();
        returnRequestHeader();

        if (this.getApplicationName() != null && !this.getApplicationName().equalsIgnoreCase("null")) {
            body.put("applicationName", this.getApplicationName());
        }
        if (this.getGrantUrl() != null && !this.getGrantUrl().equalsIgnoreCase("null")) {
            ArrayList<String> urls = new ArrayList<>();
            urls.add(this.getGrantUrl());
            body.put("redirectUris", urls);
        }
        if (this.getPeakId() != null && !this.getPeakId().equalsIgnoreCase("null")) {
            body.put("peakId", this.getPeakId());
        }
        if (this.getSubUnitId() != null && !this.getSubUnitId().equalsIgnoreCase("null")) {
            body.put("subUnitId", this.getSubUnitId());
        }
        if (this.getOrganisationId() != null && !this.getOrganisationId().equalsIgnoreCase("null")) {
            body.put("organisationId", this.getOrganisationId());
        }
        if (this.getPlatformId() != null && !this.getPlatformId().equalsIgnoreCase("null")) {
            body.put("platformId", this.getPlatformId());
        }
        if (this.getDescription() != null && !this.getDescription().equalsIgnoreCase("null")) {
            body.put("applicationDescription", this.getDescription());
        }

        response = getRestHelper().postRequestWithHeaderAndBody(url, requestHeader, body);

        logger.info("********** One Click Merchant Onboarding Request Response *********** ----> \n" + response.getBody().prettyPrint());
    }

    public void makeInvalidOnboardRequest(String url, String key, String invalidValue) {
        HashMap<String, Object> body = new HashMap<>();
        returnInvalidRequestHeader(key, invalidValue);

        if (this.getApplicationName() != null && !this.getApplicationName().equalsIgnoreCase("null")) {
            body.put("applicationName", this.getApplicationName());
        }
        if (this.getGrantUrl() != null && !this.getGrantUrl().equalsIgnoreCase("null")) {
            ArrayList<String> urls = new ArrayList<>();
            urls.add(this.getGrantUrl());
            body.put("redirectUris", urls);
        }
        if (this.getPeakId() != null && !this.getPeakId().equalsIgnoreCase("null")) {
            body.put("peakId", this.getPeakId());
        }
        if (this.getSubUnitId() != null && !this.getSubUnitId().equalsIgnoreCase("null")) {
            body.put("subUnitId", this.getSubUnitId());
        }
        if (this.getOrganisationId() != null && !this.getOrganisationId().equalsIgnoreCase("null")) {
            body.put("organisationId", this.getOrganisationId());
        }
        if (this.getPlatformId() != null && !this.getPlatformId().equalsIgnoreCase("null")) {
            body.put("platformId", this.getPlatformId());
        }
        if (this.getDescription() != null && !this.getDescription().equalsIgnoreCase("null")) {
            body.put("applicationDescription", this.getDescription());
        }

        response = getRestHelper().postRequestWithInvalidHeaderAndBody(url, requestHeader, body);

        logger.info("********** One Click Merchant Onboarding Request Response *********** ----> \n" + response.getBody().prettyPrint());
    }

    public void setApplicationName(String applicationName) {
        String usertype = PropertyHelper.getInstance().getPropertyCascading("usertype");
        String env = PropertyHelper.getInstance().getPropertyCascading("env");
        if (applicationName.equalsIgnoreCase("longname")) {
            this.applicationName = StringUtils.repeat("*", 300);
        } else if (applicationName.equalsIgnoreCase("validname")) {
            String name = RandomStringUtils.randomAlphabetic(10);
            if (usertype.equalsIgnoreCase("developer")) {
                this.applicationName = "app-hk-dragon-" + name + "-sandbox-client-app";
            } else {
                this.applicationName = "app-hk-dragon-" + name + "-" + usertype + "-client-app";
            }
        } else if (applicationName.equalsIgnoreCase("existingname")) {
            if (usertype.equalsIgnoreCase("developer")) {
                this.applicationName = "app-hk-dragon-" + env + "-sandbox-client-app";
            } else {
                this.applicationName = "app-hk-dragon-" + env + "-" + usertype + "-client-app";
            }
        } else {
            this.applicationName = applicationName;
        }
    }

    private HashMap<String, String> returnRequestHeader() {
        requestHeader = new HashMap<String, String>();
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));

        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(requestHeader);
        }
        return requestHeader;
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
     * This method hits POST One Click Merchant Onboarding endpoint and creates header and body values
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
            returnRequestBody();
            response = getRestHelper().postRequestWithHeaderAndBody(url,
                    returnRequestHeader(),
                    requestBody);

            logger.info("********** POST One Click Merchant Onboarding Response *********** ----> " + response.getBody().asString());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue("Verification of signature failed!", false);

        }
        return response;
    }

    /**
     * This method hits POST One Click Merchant Onboarding endpoint with null request body {}
     *
     * @param url
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public Response executeRequestNullBody(String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {

        try {
            response = getRestHelper().postRequestWithHeaderAndBody(url,
                    returnRequestHeader(),
                    requestBody);

            logger.info("********** POST One Click Merchant Onboarding Response *********** ----> " + response.getBody().asString());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue("Verification of signature failed!", false);

        }
        return response;
    }

    /**
     * This method hits POST One Click Merchant Onboarding endpoint without request body
     *
     * @param url
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public Response executeRequestWithoutBody(String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {

        try {
            response = getRestHelper().postRequestWithHeaders(url,
                    returnRequestHeader());

            logger.info("********** POST One Click Merchant Onboarding Response *********** ----> " + response.getBody().asString());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue("Verification of signature failed!", false);

        }
        return response;
    }

}