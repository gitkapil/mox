package apiHelpers;
import com.jayway.restassured.response.Response;
import managers.UtilManager;
import utils.EnvHelper;
import utils.PropertyHelper;
import java.util.HashMap;

public class PostPassword extends UtilManager {
    private String authToken;
    private String clientId;
    private String applicationId;
    private String activateAt;
    private String deactivateAt;
    private String entityStatus;
    private String subUnitId;
    private Response response = null;

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PostPassword.class);

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

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
    public String getSubUnitId() {
        return subUnitId;
    }

    public void setSubUnitId(String subUnitId) {
        this.subUnitId = subUnitId;
    }

    public void makeRequest(String url) {
        response = getRestHelper().postRequestWithHeaderAndBody(url, returnRequestHeader(), returnRequestBody());
        logger.info("********** Create Client Password Request Response *********** ----> " + response.getBody().prettyPrint());
    }


    public void makeRequestWithNullHeader(String url, String nullHeaderValue) {
        response = getRestHelper().postRequestWithHeaderAndBody(url, returnRequestHeaderWithNullValues(nullHeaderValue), returnRequestBody());
        logger.info("********** Create Client Password Request Response *********** ----> " + response.getBody().prettyPrint());
    }

    private HashMap<String, String> returnRequestBody() {
        HashMap<String, String> objReturn = new HashMap<>();

        if (this.activateAt != null && !activateAt.equalsIgnoreCase("null")) {
            objReturn.put("activateAt", this.activateAt);
        }
        if (this.deactivateAt != null && !deactivateAt.equalsIgnoreCase("null")) {
            objReturn.put("deactivateAt", this.deactivateAt);
        }
        if (this.entityStatus != null && !entityStatus.equalsIgnoreCase("null")) {
            objReturn.put("entityStatus", this.entityStatus);
        }
        return objReturn;
    }

    private HashMap<String, String> returnRequestHeader() {
        HashMap<String, String> requestHeader = new HashMap<String, String>();
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


    private HashMap<String, String> returnRequestHeaderWithNullValues(String nullHeaderValue) {
        HashMap<String, String> requestHeader = new HashMap<String, String>();
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
        requestHeader.remove(nullHeaderValue);
        return requestHeader;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setAuthTokenWithBearer(String authToken) {
        this.authToken = "Bearer " + authToken;
    }
}
