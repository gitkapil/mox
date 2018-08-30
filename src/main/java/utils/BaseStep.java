package utils;

import apiHelpers.AccessTokenForMerchants;
import apiHelpers.OpenIdConfigForPEAK;
import apiHelpers.PaymentRequest;



public interface BaseStep {

    RestHelper restHelper= new RestHelper();
    AccessTokenForMerchants accessToken= new AccessTokenForMerchants();
    PaymentRequest paymentRequest= new PaymentRequest();
    FileHelper fileHelper= new FileHelper();
    DateHelper dateHelper= new DateHelper();
    JWTHelper jwtHelper= new JWTHelper();
    General general= new General();
    OpenIdConfigForPEAK openIdConfig= new OpenIdConfigForPEAK();




  /*  default Properties loadGeneralProperties(){
        System.out.println("version:: "+ System.getProperty("version"));
        String generalPropertiesFilePath=System.getProperty("user.dir")+"/src/test/resources/configs/"+System.getProperty("env")+".properties";
        return fileHelper.loadPropertiesFile(generalPropertiesFilePath);

    } */

}