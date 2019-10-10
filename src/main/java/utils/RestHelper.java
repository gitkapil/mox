package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.config.EncoderConfig.encoderConfig;


public class RestHelper {
    final static Logger logger = Logger.getLogger(RestHelper.class);

    public void setBaseURI(String baseURI) {
        RestAssured.baseURI = baseURI;
    }

    public String getBaseURI() {
        return RestAssured.baseURI;
    }


    /**
     * This method GETS a request (no headers)
     *
     * @param url
     * @return
     */
    public Response getResponse(String url) {

        Response response = null;
        try {
            response = given().log().all().get(url);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), false);
        }

        return response;
    }

    /**
     * This method GETS a request with headers
     *
     * @param url
     * @param headers
     * @return
     */
    public Response getRequestWithHeaders(String url, HashMap<String, String> headers) {

        Response res = null;

        try {
            res = given().log().all()
                    .headers(headers)
                    .when()
                    .get(url);
        } catch (Exception e) {

            Assert.assertTrue(e.getMessage(), false);
        }
        return res;
    }

    public Response getRequestWithHeadersWithEncode(String url, HashMap<String, String> headers) {

        Response res = null;

        try {
            res = given().config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs(Constants.CONTENT_TYPE, ContentType.TEXT)))
                    .log().all()
                    .headers(headers)
                    .when()
                    .get(url);
        } catch (Exception e) {

            Assert.assertTrue(e.getMessage(), false);
        }
        return res;
    }


    /**
     * This method GETS a request with headers
     *
     * @param url
     * @param headers
     * @return
     */
    public Response postRequestWithHeaders(String url, HashMap<String, String> headers) {

        Response res = null;

        try {
            res = given().log().all()
                    .headers(headers)
                    .when()
                    .post(url);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), false);
        }
        return res;
    }

    public Response getRequestWithHeadersAndQueryStringParams(String url, HashMap<String, String> headers,
                                                              HashMap<String, String> queryStringParams) {
        Response res = null;
        try {
            res = given()
                    .queryParameters(queryStringParams)
                    .log()
                    .all()
                    .headers(headers)
                    .when()
                    .get(url);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), false);
        }
        return res;
    }


    /**
     * This method POSTS a request with a body
     *
     * @param url
     * @param body
     * @return
     */
    public Response postRequestWithBody(String url, HashMap<String, String> body) {

        Response response = null;
        try {
            response = given().body(body).when().post(url);

        } catch (Exception e) {

            Assert.assertTrue(e.getMessage(), false);
        }

        return response;
    }

    /**
     * This method POSTS a request with a header and body
     *
     * @param url
     * @param body
     * @return
     */
    public Response postRequestWithHeaderAndBody(String url, HashMap headers, HashMap body) {
        Response response = null;
        try {
            //config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs(Constants.CONTENT_TYPE, ContentType.TEXT)))
            String jsonBody = new ObjectMapper().writeValueAsString(body);
            response = given()
                    .log()
                    .all()
                    .headers(headers)
                    .body(body)
                    .config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs(Constants.CONTENT_TYPE, ContentType.JSON)))
                    .when()
                    .relaxedHTTPSValidation()
                    .post(url)
            ;
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(e.getMessage(), false);
        }

        return response;
    }


    /**
     * This method POSTS a request with invalid header and body
     *
     * @param url
     * @param body
     * @return
     */
    public Response postRequestWithInvalidHeaderAndBody(String url, HashMap headers, HashMap body) {
        Response response = null;
        try {
            String jsonBody = new ObjectMapper().writeValueAsString(body);
            response = given().config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs(headers.get("Content-Type").toString(), ContentType.TEXT))).log().all().headers(headers).body(body).when().post(url);

        } catch (Exception e) {

            Assert.assertTrue(e.getMessage(), false);
        }

        return response;
    }


    /**
     * This method POSTS a request with an encoded body
     *
     * @param url
     * @param requestSpecification
     * @return
     */
    public Response postRequestWithEncodedBody(String url, RequestSpecification requestSpecification) {
        Response response = null;
        try {
            System.out.println("Request Log::: " + requestSpecification.log().all().toString());
            response = requestSpecification.post(url);
        } catch (Exception e) {

            Assert.assertTrue(e.getMessage(), false);
        }

        return response;
    }


    /**
     * This method returns the response as json string
     *
     * @param res
     * @return
     */
    public JsonPath getJsonPath(Response res) {
        String json = res.asString();
        return new JsonPath(json);
    }


    /**
     * This method returns a specific value from the response header
     *
     * @param res
     * @param headerName
     * @return
     */
    public String getResponseHeaderValue(Response res, String headerName) {
        String value = null;

        try {
            value = res.getHeader(headerName);
        } catch (NullPointerException e) {

        } catch (IllegalArgumentException e) {

        }

        return value;
    }


    /**
     * This method returns a specific value from the response body
     *
     * @param res
     * @param key
     * @return
     */
    public String getResponseBodyValue(Response res, String key) {
        String value = null;

        try {
            value = res.path(key).toString();
        } catch (NullPointerException e) {

        } catch (IllegalArgumentException e) {

        }

        return value;
    }


    /**
     * This method returns a specific value from the response body as Json Array
     *
     * @param res
     * @param key
     * @return
     */
    public static List getJsonArray(Response res, String key) {
        List<HashMap<String, String>> list = null;
        try {
            list = res.jsonPath().getList(key);
        } catch (NullPointerException e) {

        }

        return list;
    }

    /**
     * This method returns a status code of the response
     *
     * @param res
     * @return
     */
    public int getResponseStatusCode(Response res) {
        return res.statusCode();
    }

    /**
     * This method returns error message within the response
     *
     * @param res
     * @return
     */
    public String getErrorMessage(Response res) {
        return getResponseBodyValue(res, "message");

    }

    /**
     * This method returns error description within the response
     *
     * @param res
     * @return
     */
    public String getErrorDescription(Response res) {

        List<HashMap<String, String>> errorDetails = getJsonArray(res, "errors");

        String errorDesc = null;

        try {
            errorDesc = errorDetails.get(0).get("errorDescription");

        } catch (NullPointerException e) {
            return null;
        } catch (IllegalArgumentException e) {

        }

        return errorDesc;
    }

    /**
     * This method returns error description list within one click API response
     *
     * @param res
     * @return
     */
    public String getErrorDescriptionsOneClick(Response res) {

        List<HashMap<String, String>> errorDetails = getJsonArray(res, "errors");
        System.out.println("errorDetails: " + errorDetails);
        boolean flag = false;
        int count = 0;
        String errorDesc = null;

        try {
            for (int i = 0; i <= errorDetails.size() - 1; i++) {
                errorDesc = errorDetails.get(i).get("errorDescription");
                if (errorDesc.equalsIgnoreCase("Field error in object 'onboardingInputModel': field 'subUnitId' must not be null; rejected value [null]")) {
                    flag = true;
                    count++;
                } else if (errorDesc.equalsIgnoreCase("Field error in object 'onboardingInputModel': field 'peakId' must not be null; rejected value [null]")) {
                    flag = true;
                    count++;
                } else if (errorDesc.equalsIgnoreCase("Field error in object 'onboardingInputModel': field 'applicationDescription' must not be null; rejected value [null]")) {
                    flag = true;
                    count++;
                } else if (errorDesc.equalsIgnoreCase("Field error in object 'onboardingInputModel': field 'organisationId' must not be null; rejected value [null]")) {
                    flag = true;
                    count++;
                } else if (errorDesc.equalsIgnoreCase("Field error in object 'onboardingInputModel': field 'platformId' must not be null; rejected value [null]")) {
                    flag = true;
                    count++;
                } else if (errorDesc.equalsIgnoreCase("Field error in object 'onboardingInputModel': field 'applicationName' must not be null; rejected value [null]")) {
                    flag = true;
                    count++;
                }
            }
            System.out.println("count: " + count);
            Assert.assertTrue("Count should be 6. But actual: " + count, count == 6);
        } catch (NullPointerException e) {
            return null;
        } catch (IllegalArgumentException e) {
        }

        return errorDesc;
    }

    /**
     * This method returns error code within the response
     *
     * @param res
     * @return
     */
    public String getErrorCode(Response res) {

        List<HashMap<String, String>> errorDetails = getJsonArray(res, "errors");

        String errorCode = null;

        try {
            errorCode = errorDetails.get(0).get("errorCode");

        } catch (NullPointerException e) {
            return null;
        } catch (IllegalArgumentException e) {

        }

        return errorCode;
    }

    /**
     * This method returns error description list for long application name in one click API response
     *
     * @param res
     * @return
     */
    public String getErrorDescriptionsLongName(Response res) {

        List<HashMap<String, String>> errorDetails = getJsonArray(res, "errors");
        System.out.println("errorDetails: " + errorDetails);
        int count = 0;
        String errorDesc = null;

        try {
            for (int i = 0; i <= errorDetails.size() - 1; i++) {
                errorDesc = errorDetails.get(i).get("errorDescription");
                if (errorDesc.contains("Field error in object 'onboardingInputModel': field 'applicationName' must match \".*-(sandbox|merchant)-client-app\"; rejected value")) {
                    count++;
                } else if (errorDesc.contains("Field error in object 'onboardingInputModel': field 'applicationName' size must be between 0 and 100; rejected value")) {
                    count++;
                }
            }
            System.out.println("count: " + count);
            Assert.assertTrue("Count should be 2. But actual: " + count, count == 2);
        } catch (NullPointerException e) {
            return null;
        } catch (IllegalArgumentException e) {
        }

        return errorDesc;
    }


    /**
     * This method returns error code list within one click API response
     *
     * @param res
     * @return
     */
    public String getErrorCodeOneClick(Response res) {

        List<HashMap<String, String>> errorDetails = getJsonArray(res, "errors");

        String errorCode = null;

        try {
            for (int i = 0; i <= errorDetails.size() - 1; i++) {
                errorCode = errorDetails.get(i).get("errorCode");
                Assert.assertEquals("Error code should be EA002", "EA002", errorCode);
            }
        } catch (NullPointerException e) {
            return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return errorCode;
    }

    /**
     * This method logs the response header values
     *
     * @param response
     */
    public void logResponseHeaders(Response response) {
        Headers allHeaders = response.headers();

        // Iterate over all the Headers
        for (Header header : allHeaders) {
            System.out.println(header.getName() + " = " + header.getValue());
        }
    }

    /**
     * This method PUTS a request with a header and body
     *
     * @param url
     * @param body
     * @return
     */
    public Response putRequestWithHeaderAndBody(String url, HashMap headers, HashMap body) {
        Response response = null;
        try {
            //config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs(Constants.CONTENT_TYPE, ContentType.TEXT)))
            String jsonBody = new ObjectMapper().writeValueAsString(body);
            response = given().config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs(Constants.CONTENT_TYPE, ContentType.TEXT)))
                    .log()
                    .all()
                    .headers(headers)
                    .body(body)
                    .when()
                    .relaxedHTTPSValidation()
                    .put(url)
            ;
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(e.getMessage(), false);
        }

        return response;
    }


}
