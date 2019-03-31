package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.junit.Assert;
import utils.EnvHelper;
import utils.PropertyHelper;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;

public class Transaction extends UtilManager {
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Transaction.class);

    private HashMap<String,String> transactionListHeader = new HashMap<>();
    private Response transactionListResponse = null;

    private String authToken;
    private String traceId;
    private String requestDateTime;

    private String from;
    private String to;
    private String startingAfter;
    private Integer limit;

    public Response getTransactionListResponse() {
        return transactionListResponse;
    }

    public void setAuthTokenwithBearer() {
        this.authToken = "Bearer "+ authToken;
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
            transactionListResponse = getRestHelper().getRequestWithHeadersAndQueryStringParams(url, returnTransactionListHeader(
                    "GET",
                    new URL(url).getPath(),
                    signingKeyId,
                    signingAlgorithm,
                    signingKey,
                    headerElementsForSignature),
                    queryStringParams);

            logger.info("********** Transaction List Response *********** ---> "+ transactionListResponse.getBody().asString());
        } catch (Exception e) {
            Assert.assertTrue("Verification of signature failed!", false);
        }
        return transactionListResponse;
    }

    public HashMap<String,String> returnTransactionListHeader(String method,
                                                              String url,
                                                              String signingKeyId,
                                                              String signingAlgorithm,
                                                              String signingKey,
                                                              HashSet headerElementsforSignature) {
        transactionListHeader.put("Accept", "application/json");
        transactionListHeader.put("Content-Type", "application/json");
        transactionListHeader.put("Authorization", authToken);
        transactionListHeader.put("Trace-Id", traceId);
        transactionListHeader.put("Accept-Language", "en-US");
        transactionListHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        transactionListHeader.put("Request-Date-Time", requestDateTime);

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

}
