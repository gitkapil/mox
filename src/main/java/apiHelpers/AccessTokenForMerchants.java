package apiHelpers;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.nimbusds.jwt.JWTClaimsSet;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.BaseStep;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

import static com.jayway.restassured.config.EncoderConfig.encoderConfig;
import static com.jayway.restassured.config.RestAssuredConfig.config;


public class AccessTokenForMerchants implements BaseStep {
    final static Logger logger = Logger.getLogger(AccessTokenForMerchants.class);
    private String clientId, clientSecret, appId;
    RequestSpecification request=null;

    Response accessTokenResponse=null;  JWTClaimsSet accessTokenClaimSet=null;

    public JWTClaimsSet getAccessTokenClaimSet() {
        return accessTokenClaimSet;
    }

    public void setAccessTokenClaimSet(JWTClaimsSet accessTokenClaimSet) {
        this.accessTokenClaimSet = accessTokenClaimSet;
    }

    public String getClientId() {
        return clientId;
    }


    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String expiresOnInResponse(){
        return restHelper.getResponseBodyValue(accessTokenResponse, "expiresOn");
    }

    public String tokenTypeInResponse(){
        return restHelper.getResponseBodyValue(accessTokenResponse, "tokenType");
    }


    /**
     *
     * @return Encoded request specification for retrieve access Token endpoint
     */

    public void createBody_RetrieveAccessToken(){
        try{
            request = RestAssured.given().config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("x-www-form-urlencoded", ContentType.URLENC)))
                    .config(RestAssured.config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                    .contentType("application/x-www-form-urlencoded")
                    .accept("application/json")
                    .formParam("client_id", clientId)
                    .formParam("client_secret", clientSecret)
                    .request();
        }
        catch(Exception e){
            Assert.assertTrue(e.getMessage(), false);
        }

    }


    public void sendBodyInJsonFormat(String url){

        String body="{\n" +
                "\"client_id\"=\""+clientId+"\",\n" +
                "\"client_secret\"=\""+clientSecret+"\"\n" +
                "}";


        request = RestAssured.given().config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                .encodeContentTypeAs("x-www-form-urlencoded",
                        ContentType.URLENC)))
                .config(RestAssured.config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .contentType("application/x-www-form-urlencoded")
                .accept("application/json")
                .body(body)
                .request();

        accessTokenResponse= restHelper.postRequestWithEncodedBody(url,request);
        logger.info("response --> "+ accessTokenResponse.getBody().asString());

    }

    public void createInvalidBody(String missingKey){
        try{
            if (missingKey.equalsIgnoreCase("clientId"))
               request = RestAssured.given().config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                    .encodeContentTypeAs("x-www-form-urlencoded",
                            ContentType.URLENC)))
                       .config(RestAssured.config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                    .contentType("application/x-www-form-urlencoded")
                       .accept("application/json")
                    .formParam("client_secret", clientSecret)
                    .request();

            else if (missingKey.equalsIgnoreCase("clientsecret"))
                request = RestAssured.given().config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs("x-www-form-urlencoded",
                                ContentType.URLENC)))
                        .config(RestAssured.config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                        .contentType("application/x-www-form-urlencoded")
                        .accept("application/json")
                        .formParam("client_id", clientId)
                        .request();

            else if (missingKey.equalsIgnoreCase("clientid&clientsecret"))
                request = RestAssured.given().config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs("x-www-form-urlencoded",
                                ContentType.URLENC)))
                        .config(RestAssured.config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                        .contentType("application/x-www-form-urlencoded")
                        .accept("application/json")
                        .request();
        }
        catch(Exception e){
            Assert.assertTrue(e.getMessage(), false);
        }



    }


    public void createInvalidHeader(String key){
        try{
            if (key.equalsIgnoreCase("Accept"))
                request = RestAssured.given().config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs("x-www-form-urlencoded",
                                ContentType.URLENC)))
                        .config(RestAssured.config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                        .contentType("application/x-www-form-urlencoded")
                        .formParam("client_id", clientId)
                        .formParam("client_secret", clientSecret)
                        .request();

            else if (key.equalsIgnoreCase("content-type"))
                request = RestAssured.given().config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs("x-www-form-urlencoded",
                                ContentType.URLENC)))
                        .contentType("application/x-www-form-urlencoded")
                        .accept("application/json")
                        .formParam("client_id", clientId)
                        .formParam("client_secret", clientSecret)
                        .request();

        }
        catch(Exception e){
            Assert.assertTrue(e.getMessage(), false);
        }



    }



    /**
     * Sets valid details for a client/ merchant
     * @param clientId
     * @param clientSecret
     */
    public void setMerchantDetails(String clientId, String clientSecret){
        setClientId(clientId);
        setClientSecret(clientSecret);
    }

    public Response getAccessTokenResponse() {
        return accessTokenResponse;
    }

    public String getAccessToken() {
        String accessToken= null;
        try{
            accessToken= accessTokenResponse.path("accessToken").toString();
        }
        catch (Exception e){

        }
        return accessToken;
    }


    /**
     * This method triggers multiple accesstoken post requests
     * @param noOfRequests needed to be triggered
     * @return
     */
    public List<Response> triggerMultipleRequests(int noOfRequests, String url){
        ExecutorService ex= Executors.newFixedThreadPool(noOfRequests);
        List<Future<Response>> futureList= new ArrayList<>() ;
        List<Response> responseList= new ArrayList<>();

        Callable<Response> callable = () -> {
            return restHelper.postRequestWithEncodedBody(url,request);
        };


        for(int i=1;i<noOfRequests+1;i++)
        {

            futureList.add(ex.submit(callable));
        }
        ex.shutdown();


        for(Future<Response> fut : futureList){
            try {
                responseList.add(fut.get());
               // System.out.println(new Date()+ "-->  "+fut.get().path("access_token"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        return responseList;


    }

    /**
     *
     * @return the access token generated for the merchant.
     */
    public Response retrieveAccessToken(String endPoint)
    {
        accessTokenResponse= restHelper.postRequestWithEncodedBody(endPoint,request);
        logger.info("********** Access Token Response *********** --> "+ accessTokenResponse.getBody().asString());
        return accessTokenResponse;

    }


    /**
     *
     * @param jwks_uri
     * @return claimset within the JWT access token generated
     */
    public JWTClaimsSet retrieveClaimSet(String jwks_uri){
       setAccessTokenClaimSet(jwtHelper.validateJWT(getAccessToken(), jwks_uri));
       return accessTokenClaimSet;
    }

    /**
     *
     * @returns true only if the claim set has the following:
     * - aud should be equals to the app id
     * - roles should have "Basic"
     */
    public boolean validateClaimSet(String sandboxAPIAppId, String liveAPIAppId){
        String appId= null;
        try {
            List<String> aud= accessTokenClaimSet.getStringListClaim("aud");
            List<String> roles=  accessTokenClaimSet.getStringListClaim("roles");

            logger.info("Aud: " +aud.get(0));

            if(roles.contains("developer"))
                appId= sandboxAPIAppId;
            else if (roles.contains("merchant"))
                appId= liveAPIAppId;
            else
                return false;

            logger.info("App Id: "+ appId);

            if (aud.get(0).equals(appId))
                return true;

        } catch (ParseException e) {
            return false;
        }

      return false;

    }



}
