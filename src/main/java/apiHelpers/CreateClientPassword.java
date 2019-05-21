package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.commons.lang3.StringUtils;
import utils.EnvHelper;
import utils.PropertyHelper;

import java.util.HashMap;

public class CreateClientPassword extends UtilManager {
    private String authToken;
    private String clientId;
    private String applicationId;
    private String activateAt;
    private String deactivateAt;
    private String description;
    private Response response = null;

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
        if (description.equalsIgnoreCase("longstring")) {
            this.description = StringUtils.repeat("*", 300);
        } else {
            this.description = description;
        }
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void makeRequest(String url) {
        HashMap<String, Object> body = new HashMap<>();

        response = getRestHelper().postRequestWithHeaderAndBody(url, returnRequestHeader(), returnRequestBody());
    }

    private HashMap<String, String> returnRequestBody() {
        HashMap<String, String> objReturn = new HashMap<>();

        if (this.activateAt != null && !activateAt.equalsIgnoreCase("null")) {
            objReturn.put("activateAt", this.activateAt);
        }
        if (this.deactivateAt != null && !deactivateAt.equalsIgnoreCase("null")) {
            objReturn.put("deactivateAt", this.deactivateAt);
        }
        if (this.description != null && !description.equalsIgnoreCase("null")) {
            objReturn.put("description", this.deactivateAt);
        }

        return objReturn;
    }

    private HashMap<String, String> returnRequestHeader() {
        HashMap<String, String> requestHeader = new HashMap<String, String>();
        requestHeader.put("Accept","application/json");
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Trace-Id",getGeneral().generateUniqueUUID());
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));

        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(requestHeader);
        }
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
        this.authToken = "Bearer "+ authToken;
    }
}
