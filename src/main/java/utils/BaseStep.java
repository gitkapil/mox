package utils;

import apiHelpers.AccessTokenForMerchants;
import apiHelpers.CheckStatus;
import apiHelpers.PaymentRequest;
import apiHelpers.Refund;

import java.util.Properties;

public interface BaseStep {

   // QRCodeHelper qrCodeHelper = new QRCodeHelper();

    RestHelper restHelper= new RestHelper();
    AccessTokenForMerchants accessToken= new AccessTokenForMerchants();
    PaymentRequest paymentRequest= new PaymentRequest();
    CheckStatus checkStatus= new CheckStatus();
    Refund refund= new Refund();
    FileHelper fileHelper= new FileHelper();
    JWTHelper jwtHelper= new JWTHelper();



  /*  default Properties loadGeneralProperties(){
        System.out.println("version:: "+ System.getProperty("version"));
        String generalPropertiesFilePath=System.getProperty("user.dir")+"/src/test/resources/configs/"+System.getProperty("env")+".properties";
        return fileHelper.loadPropertiesFile(generalPropertiesFilePath);

    } */

}