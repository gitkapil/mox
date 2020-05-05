package utils;

import com.github.dzieciou.testing.curl.CurlLoggingRestAssuredConfigFactory;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import managers.UtilManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static io.restassured.config.HeaderConfig.headerConfig;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

public class ApiUtils extends UtilManager {


    private RequestSpecification request;
    private Response response;

    private static final Logger logger = LoggerFactory.getLogger(ApiUtils.class);


    public RequestSpecification getRequest() {
        try {
            logger.debug("ApiUtils Test");
            RestAssuredConfig configuration = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)).headerConfig(headerConfig().overwriteHeadersWithName("Content-Type"));
            configuration = CurlLoggingRestAssuredConfigFactory.updateConfig(configuration);
            request = given()
                    .config(configuration)
                    .urlEncodingEnabled(false)
                    .headers("contentType", "application/json")
                    .log()
                    .all();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }


    public Response getResponse(RequestSpecification request, String url) {
        response = request
                .when()
                .get(url);
        response.then().log().all();
        return response;
    }

}
