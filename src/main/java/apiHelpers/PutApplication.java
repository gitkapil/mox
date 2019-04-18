package apiHelpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.junit.Assert;
import utils.EnvHelper;
import utils.PropertyHelper;

import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;


public class PutApplication extends UtilManager{
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PutApplication.class);

    private String authToken;
    private String applicationId;
    private String clientId;
    private String peakId;
    private String subUnitId;
    private String organisationId;
    private String traceId;
    private String requestDateTime;
    private HashMap<String, String> requestHeader;
    private HashMap requestBody = new HashMap();
    private Response response = null;

    /**
     *
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

    public Response getResponse() {
        return response;
    }

    public String getRequestDateTime() { return requestDateTime; }

    public String getAuthToken() { return authToken; }

    public String getApplicationId() {
        return applicationId;
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

    /**
     *
     * Setters
     */
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public void setAuthToken(String authToken) { this.authToken = authToken; }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setPeakId(String peakId) {
        this.peakId = peakId;
    }

    public void setSubUnitId(String subUnitId) {
        this.subUnitId = subUnitId;
    }

    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

    /**
     * Prefixes authToken with "Bearer"
     * @param authToken
     */
    public void setAuthTokenWithBearer(String authToken) {

        this.authToken = "Bearer "+ authToken;
    }


    /**
     * This method creates valid header for the PUT Application Request
     * @param method
     * @param url
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public HashMap<String,String> returnRequestHeader(String method, String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {
        requestHeader = new HashMap<String, String>();
        requestHeader.put("Accept","application/json");
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id",traceId);
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Request-Date-Time", getRequestDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));

        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(requestHeader);
        }

        try {
           requestHeader.put("Digest", getSignatureHelper().calculateContentDigestHeader(new ObjectMapper().writeValueAsBytes(requestBody)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating Digest!", false);
        }

//        try{
//            byte[] sigKey = Base64.getDecoder().decode(signingKey);
//            String signature = getSignatureHelper().calculateSignature(method, url, sigKey, signingAlgorithm, signingKeyId, headerElementsForSignature, requestHeader);
//            requestHeader.put("Signature", signature);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//           Assert.assertTrue("Trouble creating Signature!", false);
//        }
        return requestHeader;
    }

    /**
     * This method creates valid body for the PUT update client request.
     * @return
     */
    public HashMap<String,HashMap> returnRequestBody(){
        requestBody.clear();


        populateRequestBody("clientId", getClientId());
        populateRequestBody("peakId", getPeakId());
        populateRequestBody("subUnitId", getSubUnitId());
        populateRequestBody("organisationId", getOrganisationId());

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
     * This method hits PUT update client Request endpoint with an existing header and body
     * @param url
     * @param header
     * @param body
     * @return
     */
    public Response retrieveExistingHeaderBody(String url, HashMap header, HashMap body) {

        response = getRestHelper().putRequestWithHeaderAndBody(url, header, body);

        logger.info("********** PUT Application Response *********** ----> "+ response.getBody().asString());

        return response;
    }


    /**
     * This method hits PUT payment request endpoint and creates header and body values
     * @param url
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public Response executeRequest(String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {

        try{
            returnRequestBody();
            response = getRestHelper().putRequestWithHeaderAndBody(url,
                    returnRequestHeader("PUT", new URL(url).getPath(), signingKeyId, signingAlgorithm, signingKey, headerElementsForSignature),
                    requestBody);

            //testContext.getUtilManager().getSignatureHelper().verifySignature(paymentRequestResponse, "PUT", url, Base64.getDecoder().decode(signingKey), signingAlgorithm);
            logger.info("********** PUT Application Response *********** ----> "+ response.getBody().asString());
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue("Verification of signature failed!", false);

        }
        return response;
    }


    /**
     * This method hits PUT payment request endpoint with invalid header values. "key" values are missing from the header.
     * @param url
     * @param key
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public Response executeRequestWithMissingHeaderKeys(String url, String key, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature)  {

        try {
            returnRequestBody();
            HashMap<String, String> header = returnRequestHeader("PUT", new URL(url).getPath(), signingKeyId, signingAlgorithm, signingKey, headerElementsForSignature);
            header.remove(key);
            response = getRestHelper().putRequestWithHeaderAndBody(url, header, requestBody);

            //testContext.getUtilManager().getSignatureHelper().verifySignature(paymentRequestResponse, "GET", url, Base64.getDecoder().decode(signingKey), signingAlgorithm);
            logger.info("Response: "+ response.getBody().asString());
        }
        catch (Exception e){
            Assert.assertTrue("Verification of signature failed!", false);
        }

        return response;
    }


    /**
     *
     * @returns applicationId from the response
     */
    public String applicationIdInResponse(){
        return getRestHelper().getResponseBodyValue(response, "applicationId");

    }

    /**
     *
     * @returns webLink from the response
     */
    public String peakIdInResponse(){
        return getRestHelper().getResponseBodyValue(response, "peakId");

    }

    /**
     *
     * @returns subUnitId from the response
     */
    public String subUnitIdInResponse(){
        return getRestHelper().getResponseBodyValue(response, "subUnitId");

    }

    /**
     *
     * @returns organisationId from the response
     */
    public String organisationIdInResponse(){
        return getRestHelper().getResponseBodyValue(response, "organisationId");

    }

    /**
     *
     * @returns clientId from the response
     */
    public String clientIdInResponse(){
        return getRestHelper().getResponseBodyValue(response, "clientId");

    }

    /**
     *
     * @returns notificationHost from the response
     */
    public String notificationHostInResponse(){
        return getRestHelper().getResponseBodyValue(response, "notificationHost");

    }

    /**
     *
     * @returns notificationPath from the response
     */
    public String notificationPathInResponse(){
        return getRestHelper().getResponseBodyValue(response, "notificationPath");

    }

    /**
     * This method hits PUT payment request and the "digest" is not included for signature calculation
     * @param url
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public Response executeRequestWithoutDigest(String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature)  {

        try {
            returnRequestBody();
            HashMap<String, String> header = returnRequestHeaderWithoutDigest("PUT", new URL(url).getPath(), signingKeyId, signingAlgorithm, signingKey, headerElementsForSignature);
            response = getRestHelper().putRequestWithHeaderAndBody(url, header, requestBody);

            logger.info("Response: "+ response.getBody().asString());
        }
        catch (Exception e){
            Assert.assertTrue("Verification of signature failed!", false);
        }

        return response;
    }


    /**
     * This method creates a valid header but without digest
     * @param method
     * @param url
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public HashMap<String,String> returnRequestHeaderWithoutDigest(String method, String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {
        requestHeader = new HashMap<String, String>();
        requestHeader.put("Accept","application/json");
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id",traceId);
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Request-Date-Time", getRequestDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));

        try{
            requestHeader.put("Signature", getSignatureHelper().calculateSignature(method, url,
                    Base64.getDecoder().decode(signingKey), signingAlgorithm, signingKeyId,
                    headerElementsForSignature, requestHeader));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating Signature!", false);
        }
        return requestHeader;
    }

}