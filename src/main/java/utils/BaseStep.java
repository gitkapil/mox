package utils;

import apiHelpers.AccessTokenForMerchants;
import apiHelpers.CheckStatus;
import apiHelpers.PaymentRequest;
import apiHelpers.Refund;


public interface BaseStep {

    RestHelper restHelper= new RestHelper();
    AccessTokenForMerchants accessToken= new AccessTokenForMerchants();
    PaymentRequest paymentRequest= new PaymentRequest();
    CheckStatus checkStatus= new CheckStatus();
    Refund refund= new Refund();
    FileHelper fileHelper= new FileHelper();
    DateHelper dateHelper= new DateHelper();
    JWTHelper jwtHelper= new JWTHelper();
    General general= new General();



  /*  default Properties loadGeneralProperties(){
        System.out.println("version:: "+ System.getProperty("version"));
        String generalPropertiesFilePath=System.getProperty("user.dir")+"/src/test/resources/configs/"+System.getProperty("env")+".properties";
        return fileHelper.loadPropertiesFile(generalPropertiesFilePath);

    } */

}