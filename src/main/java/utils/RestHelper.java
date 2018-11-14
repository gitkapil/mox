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



public class RestHelper {
    final static Logger logger = Logger.getLogger(RestHelper.class);

    public  void setBaseURI(String baseURI){
        RestAssured.baseURI = baseURI;
    }

    public  String getBaseURI(){
        return RestAssured.baseURI;
    }


    /**
     * This method GETS a request (no headers)
     * @param url
     * @return
     */
    public  Response getResponse(String url){

        Response response= null;
        try{
            response= given().log().all().get(url);
        }
        catch (Exception e){
            Assert.assertTrue(e.getMessage(), false);
        }

        return response;
    }

    /**
     * This method GETS a request with headers
     * @param url
     * @param headers
     * @return
     */
    public  Response getRequestWithHeaders(String url, HashMap<String, String> headers){

        Response res=null;

        try{
            res= given().log().all()
                    .headers(headers)
                    .when()
                    .get(url);
        }
        catch(Exception e){

            Assert.assertTrue(e.getMessage(), false);
        }


        return res;

    }


    /**
     * This method POSTS a request with a body
     * @param url
     * @param body
     * @return
     */
    public Response postRequestWithBody(String url, HashMap<String, String> body){

        Response response=null;
        try{
            response = given().body(body).when().post(url);

        }catch(Exception e){

            Assert.assertTrue(e.getMessage(), false);
        }

        return response;
    }

    /**
     * This method POSTS a request with a header and body
     * @param url
     * @param body
     * @return
     */
    public Response postRequestWithHeaderAndBody(String url, HashMap headers,HashMap body){
        Response response=null;
        try{
            String jsonBody = new ObjectMapper().writeValueAsString(body);
            response = given().log().all().headers(headers).body(body).when().post(url);

        }catch(Exception e){

            Assert.assertTrue(e.getMessage(), false);
        }

        return response;
    }


    /**
     * This method POSTS a request with an encoded body
     * @param url
     * @param requestSpecification
     * @return
     */
    public Response postRequestWithEncodedBody(String url, RequestSpecification requestSpecification){
        Response response=null;
        try{
            System.out.println("Request Log::: "+ requestSpecification.log().all().toString());
            response = requestSpecification.post(url);
        }catch(Exception e){

            Assert.assertTrue(e.getMessage(), false);
        }

        return response;
    }


    /**
     * This method returns the response as json string
     * @param res
     * @return
     */
    public  JsonPath getJsonPath (Response res) {
        String json = res.asString();
        return new JsonPath(json);
    }


    /**
     * This method returns a specific value from the response header
     * @param res
     * @param headerName
     * @return
     */
    public String getResponseHeaderValue(Response res, String headerName){
        String value=null;

        try{
            value= res.getHeader(headerName);
        }
        catch(NullPointerException e){

        }
        catch (IllegalArgumentException e){

        }

        return value;
    }


    /**
     * This method returns a specific value from the response body
     * @param res
     * @param key
     * @return
     */
    public String getResponseBodyValue(Response res, String key){
        String value=null;

        try{
            value= res.path(key).toString();
        }
        catch(NullPointerException e){

        }
        catch (IllegalArgumentException e){

        }

        return value;
    }


    /**
     * This method returns a specific value from the response body as Json Array
     * @param res
     * @param key
     * @return
     */
    public List getJsonArray(Response res, String key){
        List<HashMap<String, String>> list= null;
       try {
          list  = res.jsonPath().getList(key);
       }
       catch (NullPointerException e){

       }

        return list;
    }

    /**
     * This method returns a status code of the response
     * @param res
     * @return
     */
    public int getResponseStatusCode(Response res){
        return res.statusCode();
    }

    /**
     * This method returns error message within the response
     * @param res
     * @return
     */
    public String getErrorMessage(Response res){
        return getResponseBodyValue(res, "message");

    }

    /**
     * This method returns error description within the response
     * @param res
     * @return
     */
    public String getErrorDescription(Response res){

        List<HashMap<String, String>> errorDetails= getJsonArray(res, "errors");

        String errorDesc=null;


        try{
            errorDesc= errorDetails.get(0).get("errorDescription");

        } catch (NullPointerException e){
            return null;
        }
        catch (IllegalArgumentException e){

        }

        return errorDesc;
    }


    /**
     * This method returns error code within the response
     * @param res
     * @return
     */
    public String getErrorCode(Response res){

        List<HashMap<String, String>> errorDetails= getJsonArray(res, "errors");

        String errorCode=null;


        try{
            errorCode= errorDetails.get(0).get("errorCode");

        } catch (NullPointerException e){
            return null;
        }
        catch (IllegalArgumentException e){

        }

        return errorCode;
    }


    /**
     * This method logs the response header values
     * @param response
     */
    public void logResponseHeaders(Response response){
        Headers allHeaders = response.headers();

        // Iterate over all the Headers
        for(Header header : allHeaders)
        {
            System.out.println(header.getName() + " = " + header.getValue());
        }
    }
    
}
