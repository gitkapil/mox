package utils;
import managers.UtilManager;
import utils.*;
import com.github.dzieciou.testing.curl.CurlLoggingRestAssuredConfigFactory;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.PrintStream;
import java.net.InetAddress;
import static io.restassured.RestAssured.given;
import static io.restassured.config.HeaderConfig.headerConfig;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static org.hamcrest.Matchers.lessThan;

public class ApiUtils extends UtilManager{

    public String payMeBaseUrl = "https://api-uat.allyoupayclouds.com/hsbcpayme_api/";
    
    public String paymentPath ="https://api-uat.dpwaf.com:443/payments/transactions/payment";
    
    public String refundPath = "https://api-uat.dpwaf.com:443/payments/transactions/refund";
    
    public String cashoutPath = "https://api-uat.dpwaf.com:443/payments/transactions/cashout";
    
    public String transactionPath= "https://api-uat.dpwaf.com:443/payments/transactions/transaction";
    
    public String qrcodePath = "https://api-uat.dpwaf.com:443/payments/transactions/transaction";
    
    public String reportingPath;
    
    public String operationTokenPath = "https://api-uat.dpwaf.com:443/on-boarding/user-profiles/users/{userId}/operations";
    
    public String searchBusinessProfilePath = "https://api-uat.dpwaf.com:443/on-boarding/business-profiles/business/profiles";
 //   @Value("${token.service.host}${token.service.resourcepath}")
    public String tokenPath ="https://adb2cfunpeak-userauth-api.dpwaf.com";
  //  @Value("${internal.service.host}:${internal.service.port}${cso.transaction.service.resourcepath}")
    public String csoTransactionPath;
   // @Value("${internal.service.host}:${internal.service.port}${cso.adjustment.service.resourcepath}")
    public String csoBalanceAdjustmentPath;
  //  @Value("${internal.service.host}:${internal.service.port}${cso.cashout.reversal.service.resourcepath}")
    public String csoCashoutReversalPath;
  //  @Value("${internal.service.host}:${internal.service.port}${cso.payment.reversal.service.resourcepath}")
    public String csoPaymentReversalPath;
   // @Value("${internal.service.host}:${internal.token.service.port}${cso.Payment.Status.service.resourcepath}")
    public String csoPaymentStatusPath;
  //  @Value("${internal.service.host}:${internal.token.service.port}${internal.token.service.resourcepath}")
    public String internalTokenPath;
    //@Value("${payments.service.host}:${payments.service.port}${qrcode.service.resourcepath}")// need to change parameters
    public String addressingEnquiryPath;
    //@Value("${payments.service.host}${fundout.service.resourcepath}")// need to change parameters
    public String fundOutPath;
    //@Value("${ledger.service.baseurl}")
    public String ledgerBasePath;
    //@Value("${consumer.service.baseurl}")
    public String consumerBasePath;
    //@Value("${adb2c.signin.url}")
    public String adb2cSigninUrl = "https://adb2csitdigitalpeak.b2clogin.com/adb2csitdigitalpeak.onmicrosoft.com/oauth2/v2.0/authorize?p=B2C_1A_SignIn&client_id=dcb3ba9a-9f3d-4ac2-b4c1-6eaaa13a18d9&nonce=defaultNonce&redirect_uri=com.hsbc.digial.peak.paymebiz://oauth/redirect&scope=openid%20offline_access&response_type=code&prompt=login&response_mode=query&DC=HKG";
    //@Value("${adb2c.signup.url}")
    public String adb2cSignupUrl;
    //@Value("${signin.token.service.url}")
    public String signInTokenServiceUrl = "https://adb2ccidigitalpeak.b2clogin.com/adb2ccidigitalpeak.onmicrosoft.com/oauth2/v2.0/token?p=B2C_1A_SignIn_v1&DC=HKG";
    //@Value("${signup.token.service.url}")
    public String signUpTokenServiceUrl;
    //@Value("${adb2c.clientid}")
    public String adb2cClientId = "cbf48534-459a-4652-9536-3a5955a56176";
   
    //@Value("${httpsScheme}")
    public String qrCodeHttpsScheme;
    
    public String contentType="application/json";
    
    //@Value("${payments.accept.language}")
    public String acceptLanguage;
    //@Value("${payments.X-HSBC-locale}")
    public String xHsbcLocale;
    //@Value("${payments.X-HSBC-Chnl-CountryCode}")
    public String xHsbcChnlCountryCode;
    //@Value("${payments.X-HSBC-Chnl-Group-Member}")
    public String xHsbcChnlGroupMember;
    //@Value("${payments.deviceType}")
    public String deviceType;
    //@Value("${payments.correlationId}")
    public String correlationId;
    //@Value("${payments.channelId}")
    public String channelId;
    //@Value("${payments.X-HSBC-Operation-Token}")
    public String xHSBCOperationToken;
    //@Value("${payments.X-forwarded-for}")
    public String xForwardedFor;
    //@Value("${payments.jdbc.url}")
    public String jdbcUrl;
    //@Value("${payments.jdbc.user}")
    public String userName;
    //@Value("${payments.jdbc.pass}")
    public String password;
    //@Value("${userprofile.jdbc.url}")
    public String onboardingJdbcUrl;
    //@Value("${userprofile.jdbc.user}")
    public String onboardingUserName;
    //@Value("${userprofile.jdbc.pass}")
    public String onboardingPassword;
    //@Value("${payme.jdbc.url}")
    public String paymeJdbcUrl;
    //@Value("${payme.jdbc.user}")
    public String paymeUserName;
    //@Value("${payme.jdbc.pass}")
    public String paymePassword;
    //@Value("${payments.qrCodeSchema.file}")
    public String qrCodeSchema;
    //@Value("${payments.transactionsSchema.file}")
    public String transactionsSchema;
    //@Value("${payments.reportingSchema.file}")
    public String reportingSchema;
    //@Value("${payments.cashoutSchema.file}")
    public String cashoutSchema;
    //public String deviceId = UUID.nameUUIDFromBytes("123456789".getBytes()).toString();
    public String deviceId="XXXX001";
    public String ipAddress="13.94.60.228";

    private RequestSpecification request;
    private Response response;

	private String payme_username = "qa";
	private String payme_password = "2019Payme!!";
    private static final Logger logger = LoggerFactory.getLogger(ApiUtils.class);

    public String getPaymentPath() {
        return paymentPath;
    }

    public String getRefundPath() {
        return refundPath;
    }

    public String getTransactionPath() {
        return transactionPath;
    }

    public String getCashoutPath() {
        return cashoutPath;
    }

    public String getQrcodePath() {
        return qrcodePath;
    }

    public String getReportingPath() {
        return reportingPath;
    }

    public String getOperationTokenPath() {
        return operationTokenPath;
    }

    public String getSearchBusinessProfilePath() {
        return searchBusinessProfilePath;
    }

    public String getTokenPath() {
        return tokenPath;
    }

    public String getCsoTransactionPath() {
        return csoTransactionPath;
    }

    public String getCsoBalanceAdjustmentPath() {
        return csoBalanceAdjustmentPath;
    }

    public String getCsoCashoutReversalPath() {
        return csoCashoutReversalPath;
    }

    public String getCsoPaymentReversalPath(){return csoPaymentReversalPath;}

    public String getCsoPaymentStatusPath(){return csoPaymentStatusPath;}

    public String getInternalTokenPath() {
        return internalTokenPath;
    }

    public String getLedgerPath() {
        return ledgerBasePath;
    }

    public String getAddressingEnquiryPath() {
        return addressingEnquiryPath;
    }

    public String getFundOutPath() { return fundOutPath;}

    public String getConsumerPath() {return consumerBasePath;}

    public String getChannelId() { return channelId;}



    public RequestSpecification getRequest() {
        try {
            logger.debug("ApiUtils Test");
            RestAssuredConfig configuration = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)).headerConfig(headerConfig().overwriteHeadersWithName("Content-Type"));
            configuration = CurlLoggingRestAssuredConfigFactory.updateConfig(configuration);
            ipAddress = InetAddress.getLocalHost().getHostAddress();
            request = given()
            		.auth().basic("qa", "2019Payme!!")
                    .config(configuration)
                    .urlEncodingEnabled(false)
                    .contentType(contentType)
                    .headers("X-HSBC-Locale", xHsbcLocale, "X-HSBC-Chnl-CountryCode", xHsbcChnlCountryCode, "X-HSBC-Chnl-Group-Member", xHsbcChnlGroupMember, "X-HSBC-Device-Id", deviceId, "X-HSBC-Device-Type", deviceType, "X-HSBC-Request-Correlation-Id", correlationId, "X-HSBC-Channel-Id", "c548d06c-acf0-4ee8-89b5-86f9b7991a9a")
                    .log()
                    .all();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }


    public RequestSpecification getRequestForPOS_Role() {

        try {
            logger.debug("ApiUtils Test");
            File file = new File("target/ConsoleOutput.txt");
            PrintStream fileOutPutStream = new PrintStream(file);
            RestAssuredConfig configuration = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)).headerConfig(headerConfig().overwriteHeadersWithName("Content-Type")).logConfig(new LogConfig().defaultStream(fileOutPutStream));
            configuration = CurlLoggingRestAssuredConfigFactory.updateConfig(configuration);
            ipAddress=InetAddress.getLocalHost().getHostAddress();

            request = given().urlEncodingEnabled(false).config(configuration)
                    .contentType(contentType)
                    .headers("X-HSBC-Locale", "en-US", "X-HSBC-Chnl-CountryCode", "HK", "X-HSBC-Chnl-Group-Member", "hsbc", "X-HSBC-Device-Id", "25f9e794-323b-3538-85f5-181f1b624d0b", "X-HSBC-Device-Type", "abc", "X-HSBC-Request-Correlation-Id", getGeneral().generateUniqueUUID(), "X-HSBC-Channel-Id", "c548d06c-acf0-4ee8-89b5-86f9b7991a9a")
                    .log()
                    .all();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

    public RequestSpecification getRequestForPost() {
        try {
            logger.debug("ApiUtils Test");
            RestAssuredConfig configuration = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)).headerConfig(headerConfig().overwriteHeadersWithName("Content-Type"));
            configuration = CurlLoggingRestAssuredConfigFactory.updateConfig(configuration);
            ipAddress = InetAddress.getLocalHost().getHostAddress();
            request = given()
                    .auth().basic("qa", "2019Payme!!")
                    .config(configuration)
                    .urlEncodingEnabled(false)
                    .contentType(contentType)
                    .headers("X-HSBC-Locale", xHsbcLocale, "X-HSBC-Chnl-CountryCode", xHsbcChnlCountryCode, "X-HSBC-Chnl-Group-Member", xHsbcChnlGroupMember, "X-HSBC-Device-Id", deviceId, "X-HSBC-Device-Type", deviceType, "X-HSBC-Request-Correlation-Id", correlationId)
                    .log()
                    .all();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }


    public RequestSpecification getRequest(String apiName) {
        try {
            logger.debug("ApiUtils Test");
            PrintStream fileOutPutStream = new PrintStream(new File("target/Logs/"+apiName+".txt"));
            RestAssuredConfig configuration = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)).headerConfig(headerConfig().overwriteHeadersWithName("Content-Type")).logConfig(new LogConfig().defaultStream(fileOutPutStream));
            configuration = CurlLoggingRestAssuredConfigFactory.updateConfig(configuration);
            //ipAddress = InetAddress.getLocalHost().getHostAddress();
            request = given()
                    .config(configuration)
                    .urlEncodingEnabled(false)
                    .contentType(contentType)
                    .headers("X-HSBC-Locale", xHsbcLocale, "X-HSBC-Chnl-CountryCode", xHsbcChnlCountryCode, "X-HSBC-Chnl-Group-Member", xHsbcChnlGroupMember, "X-HSBC-Device-Id", deviceId, "X-HSBC-Device-Type", deviceType, "X-HSBC-Request-Correlation-Id", correlationId, "X-HSBC-Channel-Id", channelId,"x-forwarded-for", ipAddress)
                    .log()
                    .all();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

    public RequestSpecification postOperationToken() {
        request = given()
                .urlEncodingEnabled(false)
                .contentType(contentType)
                .headers("X-HSBC-Locale", xHsbcLocale, "X-HSBC-Chnl-CountryCode", xHsbcChnlCountryCode, "X-HSBC-Chnl-Group-Member", xHsbcChnlGroupMember, "X-HSBC-Request-Correlation-Id", correlationId)
                .log()
                .all();
        return request;
    }

    public RequestSpecification getTokenRequest() {
        request = given()
        		.auth().basic(payme_username, payme_password)
                .urlEncodingEnabled(false)
                .contentType(contentType)
                .log()
                .all();
        return request;
    }

    public RequestSpecification getLedgerRequest() {
        request = given()
                .urlEncodingEnabled(false)
                .contentType(contentType)
                .log()
                .all();
        return request;
    }

    public RequestSpecification getPayMeRequest() {
        request = given()
                .contentType(contentType)
                .headers("osType", "Android", "osVersion", "21")
                .log()
                .all();
        return request;
    }

    public RequestSpecification getFormUrlEncodedPayMeRequest() {
        request = given()
                .contentType(ContentType.URLENC)
                .headers("osType", "Android", "osVersion", "21")
                .log()
                .all();
        return request;
    }

    public Response getResponse(RequestSpecification request, String url) {
        response = request
                .when()
                .get(url);
        response.then().log().all();
        return response;
    }


    public Response getPostResponse(RequestSpecification request, String url) {
        response = request
                .when()
                .post(url);
        response.then().log().all();
        return response;
    }

    public Response getPutResponse(RequestSpecification request, String url) {
        response = request
                .when()
                .put(url);
//        response.then().log().all();
        return response;
    }

    public void validateStatusCode(int respCode) {
        response
                .then()
                .assertThat()
                .statusCode(respCode);
    }

    public void validateResponseTime(long respTime) {
        response
                .then()
                .time(lessThan(respTime));
    }

    public void validateQrcodeSchema(Response response) {
        response
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(qrCodeSchema));
    }

    public void validateTransactionsSchema(Response response) {
        response
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(transactionsSchema));
    }

    public void validateCashoutSchema(Response response) {
        response
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(cashoutSchema));
    }

    public void validateReportingSchema(Response response) {
        response
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(reportingSchema));
    }

    public RequestSpecification getInternalSecurityKeyReq() {
        try {
            RestAssuredConfig configuration = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)).headerConfig(headerConfig().overwriteHeadersWithName("Content-Type"));
            configuration = CurlLoggingRestAssuredConfigFactory.updateConfig(configuration);
            request = given()
                    .config(configuration)
                    .urlEncodingEnabled(false)
                    .contentType(contentType)
                    .headers("X-HSBC-Locale", xHsbcLocale, "X-HSBC-Chnl-CountryCode", xHsbcChnlCountryCode, "X-HSBC-Chnl-Group-Member", xHsbcChnlGroupMember, "X-HSBC-Request-Correlation-Id", correlationId)
                    .log()
                    .all();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

    public RequestSpecification getPaymentReversalRequest(String apiName) {
        try {
            logger.debug("ApiUtils Test");
            PrintStream fileOutPutStream = new PrintStream(new File("target/Logs/"+apiName+".txt"));
            RestAssuredConfig configuration = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)).headerConfig(headerConfig().overwriteHeadersWithName("Content-Type")).logConfig(new LogConfig().defaultStream(fileOutPutStream));
            configuration = CurlLoggingRestAssuredConfigFactory.updateConfig(configuration);
            //ipAddress = InetAddress.getLocalHost().getHostAddress();
            request = given()
                    .config(configuration)
                    .urlEncodingEnabled(false)
                    .contentType(contentType)
                    .headers("X-HSBC-Locale", xHsbcLocale, "X-HSBC-Chnl-CountryCode", xHsbcChnlCountryCode, "X-HSBC-Chnl-Group-Member", xHsbcChnlGroupMember, "X-HSBC-Request-Correlation-Id", correlationId)
                    .log()
                    .all();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

    public RequestSpecification getPM4CPaymentStatusRequest(String apiName) {
        try {
            logger.debug("ApiUtils Test");
            PrintStream fileOutPutStream = new PrintStream(new File("target/Logs/"+apiName+".txt"));
            RestAssuredConfig configuration = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)).headerConfig(headerConfig().overwriteHeadersWithName("Content-Type")).logConfig(new LogConfig().defaultStream(fileOutPutStream));
            configuration = CurlLoggingRestAssuredConfigFactory.updateConfig(configuration);
            //ipAddress = InetAddress.getLocalHost().getHostAddress();
            request = given()
                    .config(configuration)
                    .urlEncodingEnabled(false)
                    .contentType(contentType)
                    .headers( "X-HSBC-Request-Correlation-Id", correlationId)
                    .log()
                    .all();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

}
