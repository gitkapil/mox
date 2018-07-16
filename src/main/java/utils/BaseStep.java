package utils;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public interface BaseStep {
   // WireMockRule wireMockRule=new WireMockRule(8090);
   // MockedServices mockedServices= new MockedServices();
   // QRCodeHelper qrCodeHelper = new QRCodeHelper();

    RestHelper restHelper= new RestHelper();
    Helper helper= new Helper();
    FileHelper fileHelper= new FileHelper();
    JWTHelper jwtHelper= new JWTHelper();

}