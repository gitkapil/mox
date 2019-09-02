package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import utils.PropertyHelper;

import java.util.HashMap;

public class PutSigningKeys extends UtilManager {
    private String authToken;
    private Response response = null;
    private String applicationId;
    private String keyId;
    private String activateAt;
    private String deactivateAt;
    private String description;
    private String entityStatus;
    private static Logger logger = Logger.getLogger(PutSigningKeys.class);

    private final static String EXISTING_APPLICATION_ID = "";

    public void makeApiCall(String url) {
        response = getRestHelper().putRequestWithHeaderAndBody(url, returnHeader(), returnBody());
        logger.info("********** PUT Signing Key Response *********** ----> "+ response.getBody().asString());
    }

    private HashMap returnBody() {
        HashMap objReturn = new HashMap();
        if (activateAt != null && !activateAt.equalsIgnoreCase("null")) {
            objReturn.put("activateAt", activateAt);
        }
        if (deactivateAt != null && !deactivateAt.equalsIgnoreCase("null")) {
            objReturn.put("deactivateAt", deactivateAt);
        }
        if (entityStatus != null && !entityStatus.equalsIgnoreCase("null")) {
            objReturn.put("entityStatus", entityStatus);
        }

        return objReturn;
    }

    private HashMap<String, String> returnHeader() {
        HashMap<String, String> objReturn = new HashMap<>();
        objReturn.put("Accept", "application/json");
        objReturn.put("Content-Type", "application/json");
        objReturn.put("Authorization", authToken);
        objReturn.put("Trace-Id", getGeneral().generateUniqueUUID());
        objReturn.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));
        objReturn.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());

        return objReturn;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntityStatus() {
        return entityStatus;
    }

    public void setEntityStatus(String entityStatus) {
        this.entityStatus = entityStatus;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void setAuthTokenWithBearer(String authToken) {
        this.authToken = "Bearer "+ authToken;
    }
}
