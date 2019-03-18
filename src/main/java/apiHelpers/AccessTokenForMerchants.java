package apiHelpers;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.nimbusds.jwt.JWTClaimsSet;
import managers.UtilManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import utils.EnvHelper;
import utils.PropertyHelper;

import java.text.ParseException;
import java.util.List;

import static com.jayway.restassured.config.EncoderConfig.encoderConfig;

public class AccessTokenForMerchants extends UtilManager{
    final static Logger logger = Logger.getLogger(AccessTokenForMerchants.class);

    private String clientId, clientSecret, type, endpoint;
    private RequestSpecification request=null;
    private Response accessTokenResponse=null;
    private JWTClaimsSet accessTokenClaimSet=null;

    /**
     *
     * Getters
     */
    public String getType() {
        return type;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public JWTClaimsSet getAccessTokenClaimSet() {
        return accessTokenClaimSet;
    }

    public Response getAccessTokenResponse() {
        return accessTokenResponse;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    /**
     *
     * Setters
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setType(String type) {
        this.type = type;
    }


    public void setAccessTokenClaimSet(JWTClaimsSet accessTokenClaimSet) {
        this.accessTokenClaimSet = accessTokenClaimSet;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
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
                    .header("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"))
                    .formParam("client_id", clientId)
                    .formParam("client_secret", clientSecret)
                    .request();
        }
        catch(Exception e){
            Assert.assertTrue(e.getMessage(), false);
        }

    }

    /**
     * This method creates a JSON body and includes it within the request. This is to test a negative scenario.
     * @param url
     */
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
                .header("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"))
                .body(body)
                .request();

        accessTokenResponse= getRestHelper().postRequestWithEncodedBody(url,request);
        logger.info("response --> "+ accessTokenResponse.getBody().asString());

    }


    /**
     * This method creates an invalid body for the request. The "missing key" parameter is not included as a part of the request.
     * @param missingKey
     */
    public void createInvalidBody(String missingKey){
        try{
            if (missingKey.equalsIgnoreCase("clientId"))
               request = RestAssured.given().config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                    .encodeContentTypeAs("x-www-form-urlencoded",
                            ContentType.URLENC)))
                       .config(RestAssured.config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                    .contentType("application/x-www-form-urlencoded")
                       .accept("application/json")
                       .header("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"))
                    .formParam("client_secret", clientSecret)
                    .request();

            else if (missingKey.equalsIgnoreCase("clientsecret"))
                request = RestAssured.given().config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs("x-www-form-urlencoded",
                                ContentType.URLENC)))
                        .config(RestAssured.config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                        .contentType("application/x-www-form-urlencoded")
                        .accept("application/json")
                        .header("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"))
                        .formParam("client_id", clientId)
                        .request();

            else if (missingKey.equalsIgnoreCase("clientid&clientsecret"))
                request = RestAssured.given().config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs("x-www-form-urlencoded",
                                ContentType.URLENC)))
                        .config(RestAssured.config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                        .contentType("application/x-www-form-urlencoded")
                        .header("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"))
                        .accept("application/json")
                        .request();

            else if (missingKey.equalsIgnoreCase("Api-Version"))
                request = RestAssured.given().config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs("x-www-form-urlencoded",
                                ContentType.URLENC)))
                        .config(RestAssured.config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                        .contentType("application/x-www-form-urlencoded")
                        .accept("application/json")
                        .formParam("client_id", clientId)
                        .formParam("client_secret", clientSecret)
                        .request();
            else if (missingKey.equalsIgnoreCase("accept")) {
                request = RestAssured.given().config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs("x-www-form-urlencoded",
                                ContentType.URLENC)))
                        .config(RestAssured.config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                        .contentType("application/x-www-form-urlencoded")
                        .header("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"))
                        .formParam("client_id", clientId)
                        .formParam("client_secret", clientSecret)
                        .request();
            } else if (missingKey.equalsIgnoreCase("content-type")) {

                request = RestAssured.given()
                        .header("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"))
                        .accept("application/json")
                        .formParam("client_id", clientId)
                        .formParam("client_secret", clientSecret)
                        .request();
            }
        }
        catch(Exception e){
            Assert.assertTrue(e.getMessage(), false);
        }



    }

    /**
     * This method creates an invalid header for the request. The "key" parameter is not included as a part of the request.
     * @param key
     */
    public void createInvalidHeader(String key, String value){
        try{
            if (key.equalsIgnoreCase("Accept"))
                request = RestAssured.given().config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs("x-www-form-urlencoded",
                                ContentType.URLENC)))
                        .config(RestAssured.config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                        .contentType("application/x-www-form-urlencoded")
                        .header("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"))
                        .accept(value)
                        .formParam("client_id", clientId)
                        .formParam("client_secret", clientSecret)
                        .request();

            else if (key.equalsIgnoreCase("content-type"))
                request = RestAssured.given()
                        .contentType(value)
                        .header("Api-Version", PropertyHelper.getInstance().getPropertyCascading("version"))
                        .accept("application/json")
                        .request();

            else if (key.equalsIgnoreCase("Api-Version"))
                request = RestAssured.given().config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs("x-www-form-urlencoded",
                                ContentType.URLENC)))
                        .config(RestAssured.config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                        .contentType("application/x-www-form-urlencoded")
                        .header("Api-Version", "")
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


    /**
     *
     * @return POST Access Token endpoint
     */
    public Response retrieveAccessToken(String endPoint)
    {
        if (EnvHelper.getInstance().isLocalDevMode()) {
            accessTokenResponse = EnvHelper.getInstance().getLocalDevModeAccessTokenResponse();
            return accessTokenResponse;
        }
        accessTokenResponse= getRestHelper().postRequestWithEncodedBody(endPoint,request);
        logger.info("********** Access Token Response *********** --> "+ accessTokenResponse.getBody().asString());
        return accessTokenResponse;

    }

    /**
     *
     * @param jwks_uri
     * @return claimset within the JWT access token generated
     */
    public JWTClaimsSet retrieveClaimSet(String jwks_uri){
       setAccessTokenClaimSet(getJwtHelper().validateJWT(getAccessToken(), jwks_uri));
       return accessTokenClaimSet;
    }

    /**
     *
     * @returns if the role within claim set is developer or merchant
     */
    public String checkDevOrMerchantRoleInClaimSet(){
        try {
            List<String> roles= accessTokenClaimSet.getStringListClaim("roles");

            if(roles.contains("developer"))
            {
                 return "developer";
            }
            else if (roles.contains("merchant"))
            {
                 return "merchant";
            }
            else
                return "no \"developer\" or \"merchant\" role present within roles";

        } catch (ParseException e) {
            return e.getMessage();
        }
        catch (NullPointerException e) {
            return "no \"developer\" or \"merchant\" role present within roles";
        }

    }

    /**
     *
     * @returns if the role within claim set has paymentrequest
     */
    public boolean isPaymentRequestRolePresentInClaimSet(){
        try {
            List<String> roles= accessTokenClaimSet.getStringListClaim("roles");

            if(roles.contains("paymentRequest"))
            {
                return true;
            }
            else
            {
                return false;
            }

        } catch (ParseException e) {
            return false;
        }
        catch (NullPointerException e) {
            return false;
        }

    }


    /**
     *
     * @returns aud's value within response claim set
     */
    public String retrieveAudInClaimSet(){

        try {
            List<String> aud= accessTokenClaimSet.getStringListClaim("aud");
            return aud.get(0);

        } catch (ParseException e) {
            return e.getMessage();
        }
        catch (NullPointerException e) {
            return "no aud found within claim set";
        }

    }


    /**
     *
     * @returns expiresOn from the response
     */
    public String expiresOnInResponse(){
        return getRestHelper().getResponseBodyValue(accessTokenResponse, "expiresOn");
    }


    /**
     *
     * @returns tokenType from the response
     */
    public String tokenTypeInResponse(){
        return getRestHelper().getResponseBodyValue(accessTokenResponse, "tokenType");
    }


    /**
     *
     * @returns accessToken from the response
     */
    public String getAccessToken() {
        String accessToken= null;
        try{
            accessToken= accessTokenResponse.path("accessToken").toString();
        }
        catch (Exception e){

        }
        return accessToken;
    }



}
