package apiHelpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.junit.Assert;
import utils.EnvHelper;
import utils.PropertyHelper;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import static apiHelpers.PutPublicKeys.logger;

public class PostSigningKeys extends UtilManager {

    private String authToken;
    private String applicationId;
    private String activateAt;
    private String deactivateAt;
    private String entityStatus;
    private String traceId;
    private String requestDateTime;

    public String getRequestDateTime() {
        return requestDateTime;
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

        if (activateAt != null) {
            requestBody.put("activateAt", activateAt);
        }
        if (deactivateAt != null) {
            requestBody.put("deactivateAt", deactivateAt);
        }
        if (entityStatus != null) {
            requestBody.put("entityStatus", entityStatus);
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
        System.out.println("response after make request: " + response.getBody().asString());
    }

    public void setAuthTokenWithBearer(String authToken) {
        this.authToken = "Bearer " + authToken;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getActivateAt() {
        return activateAt;
    }

    public void setActivateAt(String activateAt) {
        this.activateAt = activateAt;
    }

    public String getDeactivateAt() {
        return deactivateAt;
    }

    public void setDeactivateAt(String deactivateAt) {
        this.deactivateAt = deactivateAt;
    }

    public String getEntityStatus() {
        return entityStatus;
    }

    public void setEntityStatus(String entityStatus) {
        this.entityStatus = entityStatus;
    }

    public Response executePostRequestWithMissingHeaderKeys(String url, String keys) {

        try {
            returnRequestBody();
            HashMap<String, String> header = returnRequestHeaderWithMissingKeys("PUT", new URL(url).getPath(), keys);

            response = getRestHelper().postRequestWithHeaderAndBody(url, header, requestBody);


            logger.info("Response: " + response.getBody().asString());
        } catch (Exception e) {
            Assert.assertTrue("Verification of signature failed!", false);
        }

        return response;
    }

    public HashMap<String, String> returnRequestHeaderWithMissingKeys(String method, String url, String keys) throws IOException {
        requestHeader = new HashMap<String, String>();
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", traceId);
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Request-Date-Time", getRequestDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        requestHeader.remove(keys);
        return  requestHeader;
}
}