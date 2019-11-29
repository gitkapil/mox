package utils;

public class GetTokenTest {

    public static String mobileNo = "85282822828";
    public static String pin = "142434";

    public static String payMeBaseUrl_SIT = "https://api-uat5.allyoupayclouds.com/hsbcpayme_api/";
    public static String payMeBaseUrl_UAT = "https://api-uat.allyoupayclouds.com/hsbcpayme_api/";

    public static void main(String[] args) {

        System.out.println("JWT Token:  ********** --->>  "+ new GetTokenTest().getJWTToken(mobileNo,payMeBaseUrl_SIT));
        System.out.println("Operation Token: ********** --->>  "+ new GetTokenTest().operationToken(pin));
    }

    public  String getJWTToken(String mobileNo, String payMeBaseUrl){
        String jwtToken = null;
        CreateTransaction createTransaction = new CreateTransaction();
        jwtToken = createTransaction.getAuthentication(mobileNo, payMeBaseUrl);
        return jwtToken;
    }

    public  String operationToken(String pin){
        String optToken = null;
        CreateTransaction createTransaction = new CreateTransaction();
        optToken = createTransaction.getOperationToken(pin, "PostPaymentService");
        return optToken;
    }
}
