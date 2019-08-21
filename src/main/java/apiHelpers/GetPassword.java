package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.DateHelper;
import utils.General;
import utils.PropertyHelper;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class GetPassword extends UtilManager {

    private final static Logger logger = Logger.getLogger(GetPassword.class);
    String authToken;
    DateHelper dateHelper;
    private String keyId;
    private String clientId;
    private String applicationId;
    private String activateAt;
    private String deactivateAt;
    private String createdAt;
    private String lastUpdatedAt;
    private Response response= null;


    General general = new General();


    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
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

    private HashMap<String, String> returnRequestHeader(String method, String url, String authToken) {

        HashMap<String,String> header = new HashMap<>();
        header.put("accept", "application/json");
        header.put("Authorization", authToken);
        header.put("Trace-Id", general.generateUniqueUUID());
        header.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        header.put("Request-Date-Time", dateHelper.getUTCNowDateTime());
        header.put("Content-Type", "application/json");
        return header;

    }


    public void getPasswordRequest(String url, String authToken){
        try {
            response = getRestHelper().getRequestWithHeaders(url,
                    returnRequestHeader("GET",new URL(url).getPath(),
                            authToken));
            logger.info("****************List of application response ******************** --> " + response.getBody().asString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Assert.assertTrue("Unable to get URL" , false);
        }
    }

    private HashMap<String, String> returnRequestHeaderWithNullHeaderValues(String method, String url, String authToken, String nullHeaderValues) {

        HashMap<String,String> header = new HashMap<>();
        header.put("accept", "application/json");
        header.put("Authorization", authToken);
        header.put("Trace-Id", general.generateUniqueUUID());
        header.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        header.put("Request-Date-Time", dateHelper.getUTCNowDateTime());
        header.put("Content-Type", "application/json");
        header.remove(nullHeaderValues);
        return header;

    }

    public void getPasswordWithNullHeaderValues(String url, String authToken, String nullHeaderValue){
        try {
            response = getRestHelper().getRequestWithHeaders(url,
                    returnRequestHeaderWithNullHeaderValues("GET",new URL(url).getPath(),
                            authToken, nullHeaderValue));
            logger.info("****************List of application response ******************** --> " + response.getBody().asString());
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
