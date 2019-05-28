package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;
import utils.EnvHelper;
import utils.PropertyHelper;

import java.util.HashMap;

public class GetPublicKey extends UtilManager {
    private String authToken;
    private String applicationId;
    private HashMap<String, String> requestHeader;
    private Response response = null;
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GetPublicKey.class);

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
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

    public void getKeys(String url) {
        returnRequestHeader();
        response = getRestHelper().getRequestWithHeaders(url + applicationId + "/keys/public", requestHeader);

        logger.info("********** Get Public Keys Request Response *********** ----> "+ response.getBody().asString());
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
