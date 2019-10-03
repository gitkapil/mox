package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.General;
import utils.PropertyHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class GetPassword extends UtilManager {

    private final static Logger logger = Logger.getLogger(GetPassword.class);
    String authToken;
    private String keyId;
    private String clientId;
    private String applicationId;
    private String activateAt;
    private String deactivateAt;
    private String entityStatus;
    private Response response= null;
    private HashMap<String, String> requestHeader = new HashMap<>();

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getEntityStatus() {
        return entityStatus;
    }

    public void setEntityStatus(String entityStatus) {
        this.entityStatus = entityStatus;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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


    public void getPasswordRequest(String url, String authToken){
        try {
            response = getRestHelper().getRequestWithHeaders(url,
                    returnRequestHeader("GET",new URL(url).getPath(),
                            authToken));
            logger.info("****************List of application response ******************** --> " + response.getBody().prettyPrint());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Assert.assertTrue("Unable to get URL" , false);
        }
    }

    private HashMap<String, String> returnRequestHeaderWithNullHeaderValues(String method, String url, String authToken, String nullHeaderValues) {
        requestHeader.put("Accept", "application/json");
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id", getGeneral().generateUniqueUUID());
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        requestHeader.remove(nullHeaderValues);
        return requestHeader;

    }

    public void getPasswordWithNullHeaderValues(String url, String authToken, String nullHeaderValue){
        try {
            response = getRestHelper().getRequestWithHeaders(url,
                    returnRequestHeaderWithNullHeaderValues("GET",new URL(url).getPath(),
                            authToken, nullHeaderValue));
            logger.info("****************List of application response ******************** --> " + response.getBody().prettyPrint());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Assert.assertTrue("Unable to get URL" , false);
        }
    }

    public void setAuthToken(String authToken) {
        this.authToken = "Bearer "+ authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}
