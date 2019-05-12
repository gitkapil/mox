package apiHelpers;

import managers.UtilManager;
import com.jayway.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import utils.PropertyHelper;

import java.util.HashMap;

public class PutPublicKeys extends UtilManager {

    private String authToken;
    private String applicationId;
    private String keyId;
    private String description;
    private String value;
    private String activateAt;
    private String deactivateAt;
    private String entityStatus;
    private Response response = null;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void makeApiCall(String url) {
        response = getRestHelper().putRequestWithHeaderAndBody(url, returnHeader(), returnBody());
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
        if (description != null && description.equalsIgnoreCase("bigbigvalue")) {
            objReturn.put("description", StringUtils.repeat("a", 1000));
        } else {
            if (description != null && !description.equalsIgnoreCase("null")) {
                objReturn.put("description", description);
            }
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public void setAuthTokenWithBearer(String authToken) {
        this.authToken = "Bearer "+ authToken;
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
}
