package utils;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import org.joda.time.DateTime;
import org.mockito.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class EnvHelper {

    public static final String LOCAL_DEV_MODE_ACCESS_TOKEN = "localDevMode";

    public static final String LOCAL_DEV_MODE_X_HSBC_Request_Correlation_Id = "localDevMode";

    private static final class LazyLoader {
        static final EnvHelper INSTANCE = new EnvHelper();
    }

    private EnvHelper() {}

    public static EnvHelper getInstance() { return LazyLoader.INSTANCE; }

    public boolean isLocalDevMode() {
        String localDevMode =  PropertyHelper.getInstance().getPropertyCascading("env");
        return localDevMode != null
                && localDevMode.equalsIgnoreCase("localDevMode");
    }

    public Response getLocalDevModeAccessTokenResponse() {
        ResponseBody responseBody = Mockito.mock(ResponseBody.class);
        Response response = Mockito.mock(Response.class);
        Mockito.when(responseBody.asString()).thenReturn("{\"accessToken\":\""+
                LOCAL_DEV_MODE_ACCESS_TOKEN+
                "\",\"expiresOn\":\""+
                DateTime.now().plusDays(1)+
                "\",\"tokenType\":\"Bearer\"}");
        Mockito.when(response.getBody()).thenReturn(responseBody);
        Mockito.when(response.path(Mockito.eq("accessToken"))).thenReturn(LOCAL_DEV_MODE_ACCESS_TOKEN);
        Mockito.when(response.path(Mockito.eq("error_description"))).thenReturn("LocalDevMode: Error");
        Mockito.when(response.getStatusCode()).thenReturn(200);
        return response;
    }

    public void addMissingHeaderForLocalDevMode(HashMap<String, String> paymentRequestHeader) {
        paymentRequestHeader.put("X-HSBC-Request-Correlation-Id", LOCAL_DEV_MODE_X_HSBC_Request_Correlation_Id);
    }
}
