package utils;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;
import org.junit.Assert;


public class Helper implements BaseStep {
    final static Logger logger = Logger.getLogger(Helper.class);


    /**
     *
     * @param client_id
     * @param client_secret
     * @return Encoded request specification for retrieve access Token endpoint
     */
    public RequestSpecification createBody_RetrieveAccessToken(String client_id, String client_secret, String grant_type, String resource){
        RequestSpecification request=null;

        try{
            request = RestAssured.given().log().all().config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                    .encodeContentTypeAs("x-www-form-urlencoded",
                            ContentType.URLENC)))
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .formParam("client_id", client_id)
                    .formParam("client_secret", client_secret)
                    .formParam("grant_type", grant_type)
                    .formParam("resource", resource)
                    .request();
        }
        catch(Exception e){
            Assert.assertTrue(e.getMessage(), false);
        }

        return request;

    }


}
