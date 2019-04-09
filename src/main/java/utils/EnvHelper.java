package utils;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import org.joda.time.DateTime;
import org.mockito.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class EnvHelper {

    public static final String LOCAL_DEV_MODE_ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ik4tbEMwbi05REFMcXdodUhZbkhRNjNHZUNYYyIsImtpZCI6Ik4tbEMwbi05REFMcXdodUhZbkhRNjNHZUNYYyJ9.eyJhdWQiOiI1MTczYTVhOS00MjEyLTQ4NzctODYyMS03YmMxNjRjZjE3OGIiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yZmY5M2NjZS1lZmQxLTRlMTYtOTJiYS1hZmI1M2U5ZTA5ZmMvIiwiaWF0IjoxNTU0Nzc5NjQ5LCJuYmYiOjE1NTQ3Nzk2NDksImV4cCI6MTU1NDc4MzU0OSwiYWlvIjoiNDJaZ1lGQ2FaSGJ4ZmFJYys2ZUNONDV6SGpUOUJRQT0iLCJhcHBpZCI6IjEwOTMyM2UzLWM1NWUtNDI0Yy1iNzEyLTJlODJmMTY5NWU5OCIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzJmZjkzY2NlLWVmZDEtNGUxNi05MmJhLWFmYjUzZTllMDlmYy8iLCJvaWQiOiI4NjM3MjIwNi1jZWZhLTQ1ZWEtOTM0Ni0xNjNiYmFiYzc3MDYiLCJyb2xlcyI6WyJwYXltZW50UmVxdWVzdCIsIkFwcGxpY2F0aW9uLlJlYWRXcml0ZS5BbGwiLCJyZWZ1bmQiLCJkZXZlbG9wZXIiXSwic3ViIjoiODYzNzIyMDYtY2VmYS00NWVhLTkzNDYtMTYzYmJhYmM3NzA2IiwidGlkIjoiMmZmOTNjY2UtZWZkMS00ZTE2LTkyYmEtYWZiNTNlOWUwOWZjIiwidXRpIjoiQTBBTm1lUzNIVXE1eXctcWM4WnVBQSIsInZlciI6IjEuMCJ9.isxZeVdW00RzB0ta1ocQ2GXPoVojdi2wvleVHTM4nW6bvu1Pybld-XUdNLWoWpCEOIyQZAMfAQDWZQdkX1xGUAQx37GUkQTVzr3nvlvz5XC4spMsueP5Zuihoj22_It26Vdw_XsonwwcG5u4Y9LmT4RRdW0RpMKTXVvBWV15hNwLDEXp-ioRypmJiFbwwDmvlMGTlHgGyjML-KboDoIeFx8Lx648VFo6F9XQFbHmH7GhUku8zvmHafjlmp65yY71cTBl2ANmTiORPSWtg1aqcMvKXedYwejw6ZWj4oM1hKtQvizUCh36CqOvzHM4Fiu8nPkofhdH_7xLaGBZU51v5w";

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
