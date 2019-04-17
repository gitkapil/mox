package utils;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import org.joda.time.DateTime;
import org.mockito.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

public class EnvHelper {

    public static final String LOCAL_DEV_MODE_ACCESS_TOKEN = "localDevMode";

    public static final String LOCAL_DEV_MODE_X_HSBC_Request_Correlation_Id = "localDevMode";

    public static final String LOCAL_DEV_MODE_CLAIM_SET_STR = "{\"sub\":\"86372206-cefa-45ea-9346-163bbabc7706\",\"ver\":\"1.0\",\"aio\":\"42ZgYEj6+vnYomn80fyF36+sfdB3EQA=\",\"roles\":[\"paymentRequest\",\"refund\",\"developer\",\"Application.ReadWrite.All\"],\"iss\":\"https:\\/\\/sts.windows.net\\/2ff93cce-efd1-4e16-92ba-afb53e9e09fc\\/\",\"oid\":\"86372206-cefa-45ea-9346-163bbabc7706\",\"uti\":\"vCuBCT1C7ESiOC1-nEl1AA\",\"tid\":\"2ff93cce-efd1-4e16-92ba-afb53e9e09fc\",\"aud\":\"5173a5a9-4212-4877-8621-7bc164cf178b\",\"nbf\":1554789468,\"idp\":\"https:\\/\\/sts.windows.net\\/2ff93cce-efd1-4e16-92ba-afb53e9e09fc\\/\",\"appidacr\":\"1\",\"appid\":\"109323e3-c55e-424c-b712-2e82f1695e98\",\"exp\":1554793368,\"iat\":1554789468}";

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
        DateTime tomm = DateTime.now().plusDays(1);
        ResponseBody responseBody = Mockito.mock(ResponseBody.class);
        Response response = Mockito.mock(Response.class);
        Mockito.when(responseBody.asString()).thenReturn("{\"accessToken\":\""+
                LOCAL_DEV_MODE_ACCESS_TOKEN+
                "\",\"expiresOn\":\""+
                tomm +
                "\",\"tokenType\":\"Bearer\"}");
        Mockito.when(response.getBody()).thenReturn(responseBody);
        Mockito.when(response.path(Mockito.eq("accessToken"))).thenReturn(LOCAL_DEV_MODE_ACCESS_TOKEN);
        Mockito.when(response.path(Mockito.eq("error_description"))).thenReturn("LocalDevMode: Error");
        Mockito.when(response.path(Mockito.eq("expiresOn"))).thenReturn(tomm);
        Mockito.when(response.path(Mockito.eq("tokenType"))).thenReturn("Bearer");
        Mockito.when(response.getStatusCode()).thenReturn(200);
        return response;
    }

    public void addMissingHeaderForLocalDevMode(HashMap<String, String> paymentRequestHeader) {
        paymentRequestHeader.put("X-HSBC-Request-Correlation-Id", LOCAL_DEV_MODE_X_HSBC_Request_Correlation_Id);
    }
}
