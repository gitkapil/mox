package utils;

import io.restassured.authentication.FormAuthConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.CapabilityType;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

public class CreateTransaction {

    private static String END_POINT = "";
    private static final String VERSION_NAME = "2.0.0";
    public static String publicKey = "";
    public static String sessionId = "";
    public static String sessionKey = "";
    public static String iv = "";
    public static long serverTimestamp;
    public static long localTimestamp;
    public static String payMeUserId;
    public static String payMeMemberId;
    public static String payMeUserImage;
    public static String payMeUserName;
    public static String accessToken;
    public static String smsVerifyId;
    public static String verifyCode;
    private static String deviceId = "oad8asdahs0";
    private static String deviceType = "2";
    private static String locale = "en_US";
    private RequestSpecification postRequest, getRequest;
    private Response postResponse, getResponse;
    private ApiUtils apiUtils = new ApiUtils();
    private String serviceURL, qrCode, payerAuthentication, walletId, qrcodeStatus, peakDeviceId, ipAddress, paymentMessage, paymentMemo, rqstTransactionId;
    private int qrcodeExpiry;
    public static String transactionId, authentication, businessId, userId;


    public String getRefundOperationToken(String authentication, String userId, String pin) {
        serviceURL = apiUtils.getOperationTokenPath();
        String jsonBody = "{\"operationType\":\"PostRefundService\",\"pinCodeAttempt\":\"" + pin + "\"}";
        postRequest = apiUtils.postOperationToken().header("Authorization", authentication).pathParam("userId", userId).body(jsonBody);
        postResponse = apiUtils.getPostResponse(postRequest, serviceURL);
        return postResponse.path("operationToken");
    }

    public String getAdb2cAuthentication(String merchantPhoneNumber) {
        String oAuthCode = null, oAuthToken = null;
        String redirect_url = "com.hsbc.digial.peak.paymebiz://oauth/redirect";
        WebDriver driver = null;
        try {
            String url = apiUtils.adb2cSigninUrl.concat("&deviceId=" + apiUtils.deviceId + "");
            BrowserMobProxy proxy = new BrowserMobProxyServer();


            proxy.start();
            Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
            ChromeOptions options = new ChromeOptions();
            options.setCapability(CapabilityType.PROXY, seleniumProxy);
            options.setAcceptInsecureCerts(true);
            options.addArguments("--allow-insecure-localhost");
            options.addArguments("disable-infobars");
            System.setProperty("webdriver.chrome.driver", "C:\\Peak\\sandbox\\GetTokenMaven\\src\\main\\resources\\chromedriver.exe");
            proxy.newHar("google.com");
            driver = new ChromeDriver(options);
            driver.get("https://www.google.com");
            List<LogEntry> entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();
            System.out.println(entries.size() + " " + LogType.PERFORMANCE + " log entries found");
            driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
            driver.findElement(By.id("input__signin-signup-business-mobile-number")).sendKeys(merchantPhoneNumber);
            driver.findElement(By.id("button_send-verification-code")).click();
            driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
            driver.findElement(By.id("input__signup-otp")).sendKeys("123456");
            Thread.sleep(20000);
            Har har = proxy.getHar();
            System.out.println("Har Entries :" + har.getLog().getEntries().size());
            int totalsize = har.getLog().getEntries().size();
            System.out.println("Redirect URL :" + har.getLog().getEntries().get(totalsize - 1).getResponse().getRedirectURL());
            String redirecturl = har.getLog().getEntries().get(totalsize - 1).getResponse().getRedirectURL();
            oAuthCode = redirecturl.split("=")[1];
            driver.quit();
            oAuthToken = given().log().all()
                    .contentType(ContentType.URLENC.withCharset("UTF-8"))
                    .formParams("client_id", apiUtils.adb2cClientId, "scope", "openid offline_access", "grant_type", "authorization_code", "code", oAuthCode)
                    .when()
                    .get(apiUtils.signInTokenServiceUrl)
                    .then().log().all()
                    .statusCode(200)
                    .extract().path("id_token");
        } catch (InterruptedException e) {
            e.printStackTrace();
            driver.quit();
        }
        return oAuthToken;
    }


    public String parseJWT(String jwt) {
        String[] split_string = jwt.split("\\.");
        String base64EncodedBody = split_string[1];
        Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(base64EncodedBody));
        try {
            JSONObject json = new JSONObject(body);
            userId = json.getString("sub");
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return userId;
    }

    public String getAuthentication(String mobileNo, String url) {
        END_POINT = url;
        serviceURL = END_POINT.concat("publicKey");
        getRequest = apiUtils.getTokenRequest().queryParams("lang", "" + locale + "", "deviceType", "" + deviceType + "", "deviceId", "" + deviceId + "");
        getResponse = apiUtils.getResponse(getRequest, serviceURL);
        publicKey = getResponse.path("publicKey");
        try {
            String cKey = getcKey();
            String cIv = getcIv();
            System.out.println("***************************");
            System.out.println("cKey=" + cKey + ",cIv=" + cIv);
            getSessionKey(getcKey(), getcIv());
            serverTimestamp = getServerTimestamp();
            localTimestamp = System.currentTimeMillis();
            smsVerifyId = sendSMSOTP(mobileNo);
            verifyCode = getOtp(mobileNo);
            Thread.sleep(1000);
            verifyOTP(smsVerifyId, verifyCode);
            login(mobileNo, smsVerifyId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        payerAuthentication = getJwtToken();
        return payerAuthentication;
    }

    public String getOperationToken(String payMeUserPin, String action) {
        HashMap<String, String> param = getBaseParamMap();
        param = checkEncryption(param);
        param.put("userId", payMeUserId);
        param.put("accessToken", accessToken);
        param.put("pin", payMeUserPin);
        param.put("generateToken", "2");
        param.put("deviceId", "XXXX001");
        param = checkEncryption(param);
        serviceURL = END_POINT.concat("pin/verify");
        postRequest = apiUtils.getPayMeRequest().headers("Accept", "application/vnd.hsbcpayme.app-" + VERSION_NAME + "+json", "AppVersion", VERSION_NAME, "SESSIONID", sessionId, "CSRFToken", getCSRFToken()).queryParam("encData", param.get("encData"));
        postResponse = apiUtils.getPostResponse(postRequest, serviceURL);
        String encryptedResponseData = postResponse.path("encData");
        System.out.println("encryptedResponseData : " + encryptedResponseData);
        String result = decrypt(encryptedResponseData, sessionKey, iv);
        System.out.println("pinToken : " + result);
        String keyValueArray[] = result.split(",");
        String pinToken = keyValueArray[2].split(":")[1].replaceAll("\"", "").replace("}", "");
        String operationToken = null;
        // operationToken=getOptnToken(action);
        System.out.println("optToken : " + operationToken);
        return pinToken;
    }

    public String getOptnToken(String actionName) {
        HashMap<String, String> param = getBaseParamMap();
        param = checkEncryption(param);
        param.put("userId", payMeUserId);
        param.put("accessToken", accessToken);
        //PostCashoutService
        param.put("action", actionName);
        param.put("verifyType", "0");
        param = checkEncryption(param);
        serviceURL = END_POINT.concat("operationToken");
        String jsonBody = "{\"accessToken\":\"" + param.get("accessToken") + "\",\"action\":\"" + param.get("action") + "\",\"userId\":\"" + param.get("userId") + "\"}";
        postRequest = apiUtils.getPayMeRequest().headers("Accept", "application/vnd.hsbcpayme.app-" + VERSION_NAME + "+json", "AppVersion", VERSION_NAME, "SESSIONID", sessionId, "CSRFToken", getCSRFToken()).queryParam("encData", param.get("encData"));
        postResponse = apiUtils.getPostResponse(postRequest, serviceURL);
        String encryptedResponseData = postResponse.path("encData");
        System.out.println("Encrypted Op token::" + encryptedResponseData);
        String result = decrypt(encryptedResponseData, sessionKey, iv);
        System.out.println("result is::" + result);
        //String keyValueArray[]=result.split(",");
        //System.out.println("Length is::"+keyValueArray.length);
        //String pinToken=keyValueArray[2].split(":")[1].replaceAll("\"","").replace("}","");
        String jwtToken_operation = result.split(":")[1].replaceAll("\"", "").replace("}", "");
        //System.out.println("jwtToken : "+jwtToken_operation);
        return jwtToken_operation;
    }

    public void getSessionKey(String cKey, String cIv) {
        String rawData = "deviceType=" + deviceType + "&deviceId=" + deviceId + "&cKey=" + cKey + "&cIv=" + cIv;
        byte[] rsaData = RSAEncryptionByPublicKey(rawData);
        String encData = encodeBase64(rsaData);
        serviceURL = END_POINT.concat("session/key");
        postRequest = apiUtils.getPayMeRequest().headers("Accept", "application/vnd.hsbcpayme.app-" + VERSION_NAME + "+json", "AppVersion", VERSION_NAME, "SESSIONID", sessionId, "CSRFToken", getCSRFToken()).queryParams("encData", encData, "deviceId", deviceId);
//        postRequest = apiUtils.getPayMeRequest().queryParams("encData",encData,"deviceId",deviceId);
        postResponse = apiUtils.getPostResponse(postRequest, serviceURL);
        String encryptedResponseData = postResponse.path("encData");
        String result = decrypt(encryptedResponseData, cKey, cIv);
        String sKey = "";
        try {
            String[] params = result.split("&");
            for (String param : params) {
                if (param.contains("sKey")) {
                    sKey = param.replace("sKey=", "");
                }

                if (param.contains("iv")) {
                    iv = param.replace("iv=", "");
                }
                if (param.contains("SESSIONID")) {
                    sessionId = param.replace("SESSIONID=", "");
                }
            }
            sessionKey = buildSessionKey(cKey, sKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getServerTimestamp() {
        HashMap<String, String> params = getBaseParamMap();
        String encData = doParamsEncryption(params);
        serviceURL = END_POINT.concat("timestamp");
        getRequest = apiUtils.getPayMeRequest().headers("Accept", "application/vnd.hsbcpayme.app-" + VERSION_NAME + "+json", "AppVersion", VERSION_NAME, "SESSIONID", sessionId, "CSRFToken", getCSRFToken()).queryParam("encData", encData);
        getResponse = apiUtils.getResponse(getRequest, serviceURL);
        String encryptedResponseData = getResponse.path("encData");
        String result = decrypt(encryptedResponseData, sessionKey, iv);
        String resultArray[] = result.split(":");
        return Long.valueOf(resultArray[1].replaceAll("\"", "").replace("}", ""));
    }

    private static String getCSRFToken() {
        SecureRandom random = new SecureRandom();
        String randomString = new BigInteger(130, random).toString(20);

        if (iv.length() > 0 && sessionKey.length() > 0) {
            return encrypt("ts=" + getTimeStampInLocal() + "&clientRef=" + randomString, sessionKey, iv);
        } else {
            return "";
        }
    }

    private static String getTimeStampInLocal() {
        long updatedTimeStamp = serverTimestamp + (System.currentTimeMillis() - localTimestamp) / 1000;
        return String.valueOf(updatedTimeStamp);
    }

    public String sendSMSOTP(String mobileNo) {
        HashMap<String, String> param = getBaseParamMap();
        param.put("mobileNo", mobileNo);
        param.put("verifyType", "2");
        param = checkEncryption(param);
        serviceURL = END_POINT.concat("sms/otp");
        postRequest = apiUtils.getFormUrlEncodedPayMeRequest().headers("Accept", "application/vnd.hsbcpayme.app-" + VERSION_NAME + "+json", "AppVersion", VERSION_NAME, "SESSIONID", sessionId, "CSRFToken", getCSRFToken()).formParams(param);
        System.out.println(postRequest);
        postResponse = apiUtils.getPostResponse(postRequest, serviceURL);
        String encryptedResponseData = postResponse.path("encData");
        String result = decrypt(encryptedResponseData, sessionKey, iv);
        String resultArray[] = result.split(":");
        return resultArray[1].replaceAll("\"", "").replace("}", "");
    }

    /*public String getOtp(String mobileNo) {
        serviceURL = END_POINT.concat("otp/mobileNo");
        getRequest = apiUtils.getTokenRequest().headers("Accept", "application/vnd.hsbcpayme.app-" + VERSION_NAME + "+json","AppVersion", VERSION_NAME,"SESSIONID", sessionId,"CSRFToken", getCSRFToken()).queryParam("mobileNo",mobileNo);
        getResponse = apiUtils.getResponse(getRequest, serviceURL);
        return getResponse.path("verifyCode");
    }*/

    public String getOtp(String mobileNo) {
        serviceURL = END_POINT.concat("otp/mobileNo");
        getRequest = apiUtils.getTokenRequest().auth().form("qa", "2019Payme!!", new FormAuthConfig("/hsbcpayme_api/j_spring_security_check", "username", "password")).headers("Accept", "application/vnd.hsbcpayme.app-" + VERSION_NAME + "+json", "AppVersion", VERSION_NAME, "SESSIONID", sessionId, "CSRFToken", getCSRFToken()).queryParam("mobileNo", mobileNo);
        getResponse = apiUtils.getResponse(getRequest, serviceURL);
        return getResponse.path("verifyCode");
    }

    public void verifyOTP(String smsID, String verifyCode) {
        HashMap<String, String> param = getBaseParamMap();
        param.put("smsVerifyId", smsID);
        param.put("verifyCode", verifyCode);
        param = checkEncryption(param);
        serviceURL = END_POINT.concat("sms/verify");
        postRequest = apiUtils.getFormUrlEncodedPayMeRequest().headers("Accept", "application/vnd.hsbcpayme.app-" + VERSION_NAME + "+json", "AppVersion", VERSION_NAME, "SESSIONID", sessionId, "CSRFToken", getCSRFToken()).formParams(param);
        System.out.println(decrypt(param.get("encData"), sessionKey, iv));
        postResponse = apiUtils.getPostResponse(postRequest, serviceURL);
        System.out.println(postResponse.getStatusCode());
        //assertEquals(200,postResponse.getStatusCode());
    }

    public void login(final String mobileNo, final String smsVerifyId) {
        HashMap<String, String> param = getBaseParamMap();
        param.put("loginType", "3");
        param.put("mobileNo", mobileNo);
        param.put("smsVerifyId", smsVerifyId);
        param = checkEncryption(param);
        serviceURL = END_POINT.concat("login");
        postRequest = apiUtils.getFormUrlEncodedPayMeRequest().headers("Accept", "application/vnd.hsbcpayme.app-" + VERSION_NAME + "+json", "AppVersion", VERSION_NAME, "SESSIONID", sessionId, "CSRFToken", getCSRFToken()).formParams(param);
        postResponse = apiUtils.getPostResponse(postRequest, serviceURL);
        String encryptedResponseData = postResponse.path("encData");
        String result = decrypt(encryptedResponseData, sessionKey, iv);
        String keyValueArray[] = result.split(",");
        payMeUserId = keyValueArray[0].split(":")[1].replaceAll("\"", "").replace("}", "");
        accessToken = keyValueArray[1].split(":")[1].replaceAll("\"", "").replace("}", "");
        payMeUserName = keyValueArray[3].split(":")[1].replaceAll("\"", "").replace("}", "") + " " + keyValueArray[4].split(":")[1].replaceAll("\"", "").replace("}", "");
        payMeUserImage = keyValueArray[5].split("^([^:]+):")[1].replaceAll("\"", "").replace("}", "");
        ;
        payMeMemberId = keyValueArray[6].split(":")[1].replaceAll("\"", "").replace("}", "");
        for (int i = 0; i < keyValueArray.length; i++) {
            System.out.println(keyValueArray[i]);
        }
    }

    public String getPayMeUserName(final String mobileNo, final String smsVerifyId) {
        HashMap<String, String> param = getBaseParamMap();
        param.put("loginType", "3");
        param.put("mobileNo", mobileNo);
        param.put("smsVerifyId", smsVerifyId);
        param = checkEncryption(param);
        serviceURL = END_POINT.concat("login");
        postRequest = apiUtils.getFormUrlEncodedPayMeRequest().headers("Accept", "application/vnd.hsbcpayme.app-" + VERSION_NAME + "+json", "AppVersion", VERSION_NAME, "SESSIONID", sessionId, "CSRFToken", getCSRFToken()).formParams(param);
        postResponse = apiUtils.getPostResponse(postRequest, serviceURL);
        String encryptedResponseData = postResponse.path("encData");
        String result = decrypt(encryptedResponseData, sessionKey, iv);
        String keyValueArray[] = result.split(",");
        payMeUserName = keyValueArray[3].split(":")[1].replaceAll("\"", "").replace("}", "") + " " + keyValueArray[4].split(":")[1].replaceAll("\"", "").replace("}", "");
        return payMeUserName;
    }


    public String getPayMeMemberId(final String mobileNo, final String smsVerifyId) {
        HashMap<String, String> param = getBaseParamMap();
        param.put("loginType", "3");
        param.put("mobileNo", mobileNo);
        param.put("smsVerifyId", smsVerifyId);
        param = checkEncryption(param);
        serviceURL = END_POINT.concat("login");
        postRequest = apiUtils.getFormUrlEncodedPayMeRequest().headers("Accept", "application/vnd.hsbcpayme.app-" + VERSION_NAME + "+json", "AppVersion", VERSION_NAME, "SESSIONID", sessionId, "CSRFToken", getCSRFToken()).formParams(param);
        postResponse = apiUtils.getPostResponse(postRequest, serviceURL);
        String encryptedResponseData = postResponse.path("encData");
        String result = decrypt(encryptedResponseData, sessionKey, iv);
        String keyValueArray[] = result.split(",");
        payMeUserId = keyValueArray[0].split(":")[1].replaceAll("\"", "").replace("}", "");
        return payMeMemberId;
    }

    public String getPayMeUserId(final String mobileNo, final String smsVerifyId) {
        HashMap<String, String> param = getBaseParamMap();
        param.put("loginType", "3");
        param.put("mobileNo", mobileNo);
        param.put("smsVerifyId", smsVerifyId);
        param = checkEncryption(param);
        serviceURL = END_POINT.concat("login");
        postRequest = apiUtils.getFormUrlEncodedPayMeRequest().headers("Accept", "application/vnd.hsbcpayme.app-" + VERSION_NAME + "+json", "AppVersion", VERSION_NAME, "SESSIONID", sessionId, "CSRFToken", getCSRFToken()).formParams(param);
        postResponse = apiUtils.getPostResponse(postRequest, serviceURL);
        String encryptedResponseData = postResponse.path("encData");
        String result = decrypt(encryptedResponseData, sessionKey, iv);
        String keyValueArray[] = result.split(",");
        payMeUserId = keyValueArray[0].split(":")[1].replaceAll("\"", "").replace("}", "");
        return payMeUserId;
    }


    public String getJwtToken() {
        HashMap<String, String> param = getBaseParamMap();
        param = checkEncryption(param);
        param.put("userId", payMeUserId);
        param.put("accessToken", accessToken);
        param = checkEncryption(param);
        serviceURL = END_POINT.concat("jwtToken");
        postRequest = apiUtils.getPayMeRequest().headers("Accept", "application/vnd.hsbcpayme.app-" + VERSION_NAME + "+json", "AppVersion", VERSION_NAME, "SESSIONID", sessionId, "CSRFToken", getCSRFToken()).queryParam("encData", param.get("encData"));
        postResponse = apiUtils.getPostResponse(postRequest, serviceURL);
        String encryptedResponseData = postResponse.path("encData");
        String result = decrypt(encryptedResponseData, sessionKey, iv);
        System.out.println("result :" + result);
        String jwtToken = result.split(":")[1].replaceAll("\"", "").replace("}", "");
        System.out.println("jwtToken : " + jwtToken);
        return jwtToken;
    }

    public static String buildSessionKey(final String cKey, final String sKey) throws Exception {
        final byte[] key = buildSessionKey(Hex.decodeHex(cKey.toCharArray()), Hex.decodeHex(sKey.toCharArray()));
        String hexString = new String(Hex.encodeHex(key));

        return hexString.toUpperCase(Locale.ENGLISH);
    }

    public static byte[] buildSessionKey(final byte[] cKey, final byte[] sKey) {
        final byte[] result = new byte[cKey.length];
        int i = 0;
        for (final byte b1 : cKey) {
            final byte b2 = sKey[i];
            result[i] = (byte) (b1 ^ b2);
            i++;
        }

        return result;
    }

    public static String encodeBase64(byte[] data) {
        return Base64.encodeBase64String(data);
    }

    public static byte[] decodeBase64(String data) {
        return Base64.decodeBase64(data.getBytes(Charset.forName("UTF-8")));
    }

    public static byte[] RSAEncryptionByPublicKey(final String plain) {
        try {
            byte[] decodedBytes = decodeBase64(publicKey);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey key = keyFactory.generatePublic(spec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(plain.getBytes(Charset.forName("UTF-8")));
            return encryptedBytes;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "".getBytes();
    }

    public static String getcKey() {
        char[] CHARSET_AZ_09 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        SecureRandom random = new SecureRandom();
        char[] result = new char[16];
        for (int i = 0; i < result.length; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(CHARSET_AZ_09.length);
            result[i] = CHARSET_AZ_09[randomCharIndex];
        }
        String key = new String(result);
        String cKey = hexAndUpperCase(key);
        return cKey;
    }

    public static String getcIv() {
        SecureRandom r = new SecureRandom();
        StringBuffer sb = new StringBuffer();
        while (sb.length() < 16) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        String iv = sb.toString().substring(0, 16).toUpperCase();
        String cIv = hexAndUpperCase(iv);

        return cIv;
    }

    public static String hexAndUpperCase(String data) {
        return new BigInteger(1, data.getBytes(Charset.forName("UTF-8"))).toString(16).toUpperCase();
    }

    public static HashMap<String, String> getBaseParamMap() {
        HashMap<String, String> param = new HashMap<>();
        param.put("lang", "en_US");
        param.put("deviceType", "2");
        param.put("deviceId", "oad8asdahs0");
        param.put("deviceToken", "aisbdiuabsod");
        param.put("entry", "1");
        param.put("appVersion", "1.5.0");
        param.put("deviceModel", "aisbdaos");
        param.put("osVersion", "ojasndoas0d8isan08");
        return param;
    }

    public static HashMap<String, String> checkEncryption(HashMap<String, String> param) {
        String encData = doParamsEncryption(param);
        param.clear();
        param.put("encData", encData);
        return param;
    }

    public static String doParamsEncryption(Map<String, String> param) {
        String data = "";

        String encryptedData = "";
        try {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                data += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&";
            }

            // remove last char &
            data = data.substring(0, data.length() - 1);

            encryptedData = encrypt(data, sessionKey, iv);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedData;
    }

    public static String encrypt(final String data, final String key, final String iv) {
        String encData = "";

        try {
            encData = encrypt(data.getBytes(), Hex.decodeHex(key.toCharArray()), Hex.decodeHex(iv.toCharArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encData;
    }

    public static String encrypt(final byte[] data, final byte[] key, final byte[] iv) throws Exception {
        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            final int blockSize = cipher.getBlockSize();

            int plaintextLength = data.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            final byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(data, 0, plaintext, 0, data.length);

            final SecretKeySpec keyspec = new SecretKeySpec(key, "AES");
            final IvParameterSpec ivspec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            final byte[] encrypted = cipher.doFinal(plaintext);

            String result = new String(Hex.encodeHex(encrypted));
            return result.toUpperCase(Locale.ENGLISH);

        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(final String data, final String key, final String iv) {
        try {
            return decrypt(Hex.decodeHex(data.toCharArray()), Hex.decodeHex(key.toCharArray()), Hex.decodeHex(iv.toCharArray()));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(final byte[] data, final byte[] key, final byte[] iv) throws Exception {
        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            final int blockSize = cipher.getBlockSize();

            int plaintextLength = data.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            final byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(data, 0, plaintext, 0, data.length);

            final SecretKeySpec keyspec = new SecretKeySpec(key, "AES");
            final IvParameterSpec ivspec = new IvParameterSpec(iv);

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            final byte[] original = cipher.doFinal(plaintext);
            return new String(original).trim();

        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String createEncryptedRequest(String request) {
        System.out.println("Session Key: " + sessionKey);
        String encData = encrypt(request, getcKey(), getcIv());
        StringBuilder str = new StringBuilder();
        str.append("Session Key: " + sessionKey);
        str.append("encData: " + encData);
        return str.toString();
    }
}

