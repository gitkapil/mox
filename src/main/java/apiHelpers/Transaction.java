package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.junit.Assert;
import utils.EnvHelper;
import utils.PropertyHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;

public class Transaction extends UtilManager {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Transaction.class);

    private HashMap<String, String> transactionListHeader = new HashMap<>();
    private Response transactionListResponse = null;
    public Response transactionIdResponse = null;

    private String authToken;
    private String traceId;
    private String requestDateTime;
    private String transactionId;
    private String from;
    private String to;
    private String startingAfter;
    private Integer limit;

    public HashMap<String, String> getRequestHeader() {
        return requestHeader;
    }

    private HashMap<String, String> requestHeader;

    public Response getTransactionListResponse() {
        return transactionListResponse;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setAuthTokenwithBearer() {
        this.authToken = "Bearer " + authToken;
        this.authToken = authToken;
    }

    public void setAuthTokenWithoutBearer() {
        this.authToken = authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public String getRequestDateTime() {
        return requestDateTime;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getStartingAfter() {
        return startingAfter;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setStartingAfter(String startingAfter) {
        this.startingAfter = startingAfter;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Response retrieveTransactionList(String url,
                                            String signingKeyId,
                                            String signingAlgorithm,
                                            String signingKey,
                                            HashSet headerElementsForSignature,
                                            HashMap<String, String> queryStringParams) {
        try {
            transactionListResponse = getRestHelper().getRequestWithHeadersAndQueryStringParams(url,
                    returnTransactionListHeader(
                            "GET",
                            new URL(url).getPath(),
                            signingKeyId,
                            signingAlgorithm,
                            signingKey,
                            headerElementsForSignature,
                            queryStringParams),
                    queryStringParams);

            logger.info("********** Transaction List Response *********** ---> " + transactionListResponse.getBody().asString());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue("Verification of signature failed!", false);

        }
        return transactionListResponse;
    }

    public Response retrieveTransactionListWithoutQueryParam(String url,
                                                             String signingKeyId,
                                                             String signingAlgorithm,
                                                             String signingKey,
                                                             HashSet headerElementsForSignature) {
        try {
            transactionListResponse = getRestHelper().getRequestWithHeaders(url,
                    returnTransactionListHeaderWithoutQueryParam(
                            "GET",
                            new URL(url).getPath(),
                            signingKeyId,
                            signingAlgorithm,
                            signingKey,
                            headerElementsForSignature));

            logger.info("********** Transaction List Response *********** ---> " + transactionListResponse.getBody().prettyPrint());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue("Verification of signature failed!", false);

        }
        return transactionListResponse;
    }

    public HashMap<String, String> returnTransactionListHeader(String method,
                                                               String url,
                                                               String signingKeyId,
                                                               String signingAlgorithm,
                                                               String signingKey,
                                                               HashSet headerElementsforSignature,
                                                               HashMap<String, String> queryStringParams) {
        transactionListHeader.put("Accept", "application/json");
        transactionListHeader.put("Content-Type", "application/json");
        transactionListHeader.put("Authorization", authToken);
        transactionListHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        transactionListHeader.put("Accept-Language", "en-US");
        transactionListHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        transactionListHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());

        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(transactionListHeader);
        }

        String urlWithQueryParams = url;
        try {
            if (!queryStringParams.isEmpty()) {
                urlWithQueryParams = urlWithQueryParams + "?";

                for (String s : queryStringParams.keySet()) {
                    String keyValue = s + "=" + URLEncoder.encode(queryStringParams.get(s), "utf-8") + "&";
                    urlWithQueryParams = urlWithQueryParams + keyValue;
                }

                if (urlWithQueryParams.endsWith("&")) {
                    urlWithQueryParams = urlWithQueryParams.substring(0, urlWithQueryParams.length() - 1);
                }
            }
        } catch (UnsupportedEncodingException e) {
            //Ignore this
        }

        try {
            transactionListHeader.put(
                    "Signature",
                    getSignatureHelper().calculateSignature(
                            method,
                            urlWithQueryParams,
                            Base64.getDecoder().decode(signingKey),
                            signingAlgorithm,
                            signingKeyId,
                            headerElementsforSignature,
                            transactionListHeader));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating signature!", false);
        }

        return transactionListHeader;
    }

    public HashMap<String, String> returnTransactionListHeaderWithoutQueryParam(String method,
                                                                                String url,
                                                                                String signingKeyId,
                                                                                String signingAlgorithm,
                                                                                String signingKey,
                                                                                HashSet headerElementsforSignature) {
        transactionListHeader.put("Accept", "application/json");
        transactionListHeader.put("Content-Type", "application/json");
        transactionListHeader.put("Authorization", authToken);
        transactionListHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        transactionListHeader.put("Accept-Language", "en-US");
        transactionListHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        transactionListHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());

        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(transactionListHeader);
        }

        try {
            transactionListHeader.put(
                    "Signature",
                    getSignatureHelper().calculateSignature(
                            method,
                            url,
                            Base64.getDecoder().decode(signingKey),
                            signingAlgorithm,
                            signingKeyId,
                            headerElementsforSignature,
                            transactionListHeader));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating signature!", false);
        }

        return transactionListHeader;
    }

    /**
     * This method hits Get transactionId endpoint with "key" values missing from the header.
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
            HashMap<String, String> header = returnTransactionListHeaderWithoutQueryParam("GET", url, signingKeyId,
                    signingAlgorithm, signingKey,
                    headerElementsForSignature);
            header.remove(key);
            transactionIdResponse = getRestHelper().getRequestWithHeaders(url, header);
            logger.info("Response: " + transactionIdResponse.getBody().asString());
        } catch (Exception e) {
            Assert.assertTrue("Verification of signature failed!", false);
        }

        return transactionIdResponse;
    }

    private HashMap<String, String> returnInvalidRequestHeader(String method,
                                                               String url,
                                                               String signingKeyId,
                                                               String signingAlgorithm,
                                                               String signingKey,
                                                               HashSet headerElementsforSignature,
                                                               String key, String invalidValue) {
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
        try {
            requestHeader.put(
                    "Signature",
                    getSignatureHelper().calculateSignature(
                            method,
                            url,
                            Base64.getDecoder().decode(signingKey),
                            signingAlgorithm,
                            signingKeyId,
                            headerElementsforSignature,
                            requestHeader));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating signature!", false);
        }
        return requestHeader;
    }

    /**
     * This method hits Get transactionId endpoint with invalid header values.
     *
     * @param url
     * @param key
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public Response executeRequestWithInvalidHeaderKeys(String url, String key, String invalidValue, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature) {

        try {
            HashMap<String, String> header = returnInvalidRequestHeader("GET",
                    url,
                    signingKeyId,
                    signingAlgorithm,
                    signingKey,
                    headerElementsForSignature,
                    key,
                    invalidValue);
            transactionIdResponse = getRestHelper().getRequestWithHeaders(url, header);

            logger.info("Response: " + transactionIdResponse.getBody().prettyPrint());
        } catch (Exception e) {
            Assert.assertTrue("Verification of signature failed!", false);
        }
        return transactionIdResponse;
    }

    public Response retrieveTransactionIdResponseWithoutQueryParam(String url,
                                                                   String signingKeyId,
                                                                   String signingAlgorithm,
                                                                   String signingKey,
                                                                   HashSet headerElementsForSignature) {
        try {
            transactionIdResponse = getRestHelper().getRequestWithHeaders(url,
                    returnTransactionListHeaderWithoutQueryParam(
                            "GET",
                            new URL(url).getPath(),
                            signingKeyId,
                            signingAlgorithm,
                            signingKey,
                            headerElementsForSignature));

            logger.info("********** GET TransactionId API Response *********** ---> " + transactionIdResponse.getBody().prettyPrint());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue("Verification of signature failed!", false);

        }
        return transactionIdResponse;
    }

    /**
     * This method creates a valid header with invalid request date time and hits the GET transaction request endpoint
     *
     * @param url
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsForSignature
     * @return
     */
    public Response retrieveTransactionInvalidDate(String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsForSignature, String value) {
        try {
            //url = appendPaymentIdInURL(url);

            transactionIdResponse = getRestHelper().getRequestWithHeaders(url, returnTransactionHeaderInvalidDate("GET", new URL(url).getPath(), signingKeyId, signingAlgorithm, signingKey, headerElementsForSignature, value));

            logger.info("********** GET Transaction by ID Response *********** ---> " + transactionIdResponse.prettyPrint());
        } catch (Exception e) {
            Assert.assertTrue("Verification of signature failed!", false);

        }
        return transactionIdResponse;
    }

    /**
     * This method creates and returns a valid header with invalid Request-Date-Time for the GET payment request
     *
     * @param method
     * @param url
     * @param signingKeyId
     * @param signingAlgorithm
     * @param signingKey
     * @param headerElementsforSignature
     * @return
     */
    public HashMap<String, String> returnTransactionHeaderInvalidDate(String method, String url, String signingKeyId, String signingAlgorithm, String signingKey, HashSet headerElementsforSignature, String value) {
        requestHeader = new HashMap<String, String>();
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        requestHeader.put("Request-Date-Time", value);
        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(requestHeader);
        }
        try {
            requestHeader.put("Signature", getSignatureHelper().calculateSignature(method, url, Base64.getDecoder().decode(signingKey), signingAlgorithm, signingKeyId,
                    headerElementsforSignature, requestHeader)
            );
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue("Trouble creating signature!", false);
        }
        return requestHeader;
    }

}
