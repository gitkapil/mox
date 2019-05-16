package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import utils.EnvHelper;
import utils.PropertyHelper;

import java.util.HashMap;

public class PostPublicKey extends UtilManager {
    private static Logger logger = Logger.getLogger(PostPublicKey.class);
    private Response response = null;
    private String authToken;
    private HashMap<String, String> requestHeader;
    private final static String EXISTING_APPLICATION_KEY = "public_key_application_id";
    private String applicationId;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Response postPublicKeys(String url, String value, String activateAt,
                                   String deactivateAt, String entityStatus, String description) {
        HashMap<String, String> body = new HashMap<>();
        returnRequestHeader();

        if (value != null && value.equalsIgnoreCase("value")) {
            body.put("value", StringUtils.repeat("a", 2048));
        } else {
            if (value != null && !value.equalsIgnoreCase("null")) {
                body.put("value", value);
            }
        }
        if (activateAt != null && !activateAt.equalsIgnoreCase("null")) {
            body.put("activateAt", activateAt);
        }
        if (deactivateAt != null && !deactivateAt.equalsIgnoreCase("null")) {
            body.put("deactivateAt", deactivateAt);
        }
        if (entityStatus != null && !entityStatus.equalsIgnoreCase("null")) {
            body.put("entityStatus", entityStatus);
        }
        if (description != null && description.equalsIgnoreCase("bigvalue")) {
            body.put("description", StringUtils.repeat("a", 300));
        }else {
            if (description != null && !description.equalsIgnoreCase("null")) {
                body.put("description", description);
            }
        }

        response = getRestHelper().postRequestWithHeaderAndBody(url + applicationId + "/keys/public", requestHeader, body);
        logger.info("********** POST Public Key Response *********** ----> "+ response.getBody().asString());
        return response;
    }

    public void setAuthTokenWithBearer(String authToken) {
        this.authToken = "Bearer "+ authToken;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    private HashMap<String,String> returnRequestHeader() {
        requestHeader = new HashMap<String, String>();
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
}
