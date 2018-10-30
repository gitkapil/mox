package managers;

import apiHelpers.*;

public class ApiManager {
    AccessTokenForMerchants accessToken;
    PaymentRequest paymentRequest;
    PaymentStatus paymentStatus;
    Refunds refunds;
    OpenIdConfigForPEAK openIdConfig;

    public ApiManager() {
        accessToken= new AccessTokenForMerchants();
        paymentRequest= new PaymentRequest();
        paymentStatus= new PaymentStatus();
        refunds= new Refunds();
        openIdConfig= new OpenIdConfigForPEAK();
    }

    public AccessTokenForMerchants getAccessToken() {
        return accessToken;
    }

    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public Refunds getRefunds() {
        return refunds;
    }

    public OpenIdConfigForPEAK getOpenIdConfig() {
        return openIdConfig;
    }
}
