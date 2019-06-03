package apiHelpers;

import com.jayway.restassured.response.Response;
import managers.UtilManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import utils.EnvHelper;
import utils.PropertyHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateClient extends UtilManager {
    private String authToken;
    private String clientName;
    private String grantUrl;
    private Response response = null;
    private HashMap<String, String> requestHeader;

    public String getAuthToken() {
        return authToken;
    }

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CreateClient.class);

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void makeRequest(String url) {
        HashMap<String, Object> body = new HashMap<>();
        returnRequestHeader();

        if (this.getClientName() != null && !this.getClientName().equalsIgnoreCase("null")) {
            body.put("name", this.getClientName());
        }
        if (this.getGrantUrl() != null && !this.getGrantUrl().equalsIgnoreCase("null")) {
            ArrayList<String> urls = new ArrayList<>();
            urls.add(this.getGrantUrl());
            body.put("redirectUris", urls);
        }

        response = getRestHelper().postRequestWithHeaderAndBody(url, requestHeader, body);

        logger.info("********** Create Client Request Response *********** ----> "+ response.getBody().asString());
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        if (clientName.equalsIgnoreCase("longname")) {
            this.clientName = StringUtils.repeat("*", 300);
        } else if (clientName.equalsIgnoreCase("validname")) {
            String name = RandomStringUtils.random(10);
            String api = "sandbox";
            this.clientName = "app-hk-dragon-" + name + "-" + api + "-client-app";
        } else {
            this.clientName = clientName;
        }
    }

    public String getGrantUrl() {
        return grantUrl;
    }

    public void setGrantUrl(String grantUrl) {
        if (grantUrl.equalsIgnoreCase("longurl")) {
            this.grantUrl = StringUtils.repeat("*", 2000);
        } else {
            this.grantUrl = grantUrl;
        }
    }

    public void setAuthTokenWithBearer(String authToken) {
        this.authToken = "Bearer "+ authToken;
    }
}
