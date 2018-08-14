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
import java.util.List;
import java.util.concurrent.*;


public class AccessTokenForMerchants implements BaseStep {
    final static Logger logger = Logger.getLogger(AccessTokenForMerchants.class);
    private String clientId, clientSecret, appId, grantType, endPoint;

    Response accessToken=null;  JWTClaimsSet accessTokenClaimSet=null;

    public JWTClaimsSet getAccessTokenClaimSet() {
        return accessTokenClaimSet;
    }

    public void setAccessTokenClaimSet(JWTClaimsSet accessTokenClaimSet) {
        this.accessTokenClaimSet = accessTokenClaimSet;
    }

    public String getClientId() {
        return clientId;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
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

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    /**
     *
     * @return Encoded request specification for retrieve access Token endpoint
     */

    public RequestSpecification createBody_RetrieveAccessToken(){
        RequestSpecification request=null;

        try{
            request = RestAssured.given().config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                    .encodeContentTypeAs("x-www-form-urlencoded",
                            ContentType.URLENC)))
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .formParam("client_id", clientId)
                    .formParam("client_secret", clientSecret)
                    .formParam("grant_type", grantType)
                    .formParam("resource", appId)
                    .request();
        }
        catch(Exception e){
            Assert.assertTrue(e.getMessage(), false);
        }

        return request;

    }

    /**
     * Sets valid details for a client/ merchant
     * @param clientId
     * @param clientSecret
     * @param appId
     * @param grantType
     * @param endPoint
     */
    public void setMerchantDetails(String clientId, String clientSecret, String appId, String grantType, String endPoint){
        setClientId(clientId);
        setClientSecret(clientSecret);
        setAppId(appId);
        setGrantType(grantType);
        setEndPoint(endPoint);
    }

    public Response getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Response accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * This method triggers multiple accesstoken post requests
     * @param noOfRequests needed to be triggered
     * @return
     */
    public List<Response> triggerMultipleRequests(int noOfRequests){
        ExecutorService ex= Executors.newFixedThreadPool(noOfRequests);
        List<Future<Response>> futureList= new ArrayList<>() ;
        List<Response> responseList= new ArrayList<>();

        Callable<Response> callable = () -> {
            return restHelper.postRequestWithEncodedBody(getEndPoint(),createBody_RetrieveAccessToken());
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
    public Response retrieveAccessToken()
    {
        accessToken= restHelper.postRequestWithEncodedBody(getEndPoint(),createBody_RetrieveAccessToken());
        return accessToken;

    }

    /**
     *
     * @param jwks_uri
     * @return claimset within the JWT access token generated
     */
    public JWTClaimsSet retrieveClaimSet(String jwks_uri){
       setAccessTokenClaimSet(jwtHelper.validateJWT(restHelper.getResponseBodyValue(accessToken,"access_token"), jwks_uri));
       validateClaimSet();
       return accessTokenClaimSet;
    }

    /**
     *
     * @returns true only if the claim set has the following:
     * - aud should be equals to the app id
     * - roles should have "Basic"
     */
    public boolean validateClaimSet(){
        try {
            List<String> aud= accessTokenClaimSet.getStringListClaim("aud");
            List<String> roles=  accessTokenClaimSet.getStringListClaim("roles");

            if (roles.contains("Basic") && aud.get(0).equalsIgnoreCase(appId))
            {
                return true;
            }

        } catch (ParseException e) {
            return false;
        }

      return false;

    }



}
