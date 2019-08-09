package apiHelpers;

import managers.UtilManager;
import com.jayway.restassured.response.Response;
import org.apache.log4j.Logger;
import utils.EnvHelper;
import utils.PropertyHelper;

import java.util.HashMap;

import static com.jayway.restassured.RestAssured.given;

public class GetSigningKey extends UtilManager {
    private String authToken;
    private String applicationId;
    private Response response = null;
    private HashMap<String, String> requestHeader;
    private static Logger logger = Logger.getLogger(GetPublicKey.class);

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
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

    public void makeCallWithApplicationID(String url, String applicationID) {
        returnRequestHeader();
        response =
                getRestHelper().getRequestWithHeaders(url + applicationID + "/keys/signing", requestHeader);
        logger.info("********** GET Public Key Response *********** ----> "+ response.getBody().asString());
    }

    public void makeCallWithoutTraceID(String url, String applicationID) {
        returnRequestHeaderWithNoTraceID();
        response =
                getRestHelper().getRequestWithHeaders(url + applicationID + "/keys/signing", returnRequestHeaderWithNoTraceID());
        logger.info("********** GET Public Key Response *********** ----> "+ response.getBody().asString());
    }


    public void makeCall(String url) {

        response = getRestHelper().getRequestWithHeaders(url + applicationId + "/keys/signing", requestHeader);
        logger.info("********** GET Public Key Response *********** ----> "+ response.getBody().prettyPrint());
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

    private HashMap<String,String> returnRequestHeaderWithNoTraceID() {
        requestHeader = new HashMap<String, String>();
        requestHeader.put("Accept","application/json");
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("Authorization", authToken);
        requestHeader.put("Accept-Language", "en-US");
        requestHeader.put("Request-Date-Time", getDateHelper().getUTCNowDateTime());
        requestHeader.put("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"));

        if (EnvHelper.getInstance().isLocalDevMode()) {
            EnvHelper.getInstance().addMissingHeaderForLocalDevMode(requestHeader);
        }
        return requestHeader;
    }
}

