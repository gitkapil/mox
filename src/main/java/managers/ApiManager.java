package managers;

import apiHelpers.*;


public class ApiManager {
    private AccessTokenForMerchants accessToken;
    private PaymentRequest paymentRequest;
    private PaymentStatus paymentStatus;
    private Refunds refunds;
    private OpenIdConfigForPEAK openIdConfig;

    public AccessTokenForMerchants getAccessToken() {
        return (accessToken == null) ? accessToken = new AccessTokenForMerchants() : accessToken;
    }

    public PaymentRequest getPaymentRequest() {
        return (paymentRequest == null) ? paymentRequest = new PaymentRequest() : paymentRequest;
    }

    public PaymentStatus getPaymentStatus() {
        return (paymentStatus == null) ? paymentStatus = new PaymentStatus() : paymentStatus;
    }

    public Refunds getRefunds() {
        return (refunds == null) ? refunds = new Refunds() : refunds;
    }

    public OpenIdConfigForPEAK getOpenIdConfig() {
        return (openIdConfig == null) ? openIdConfig = new OpenIdConfigForPEAK() : openIdConfig;
    }
}
