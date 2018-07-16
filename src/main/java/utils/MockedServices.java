package utils;



import com.github.tomakehurst.wiremock.junit.WireMockRule;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class MockedServices {

    public void setupStub1(WireMockRule wireMockRule) {

        wireMockRule.stubFor(get(urlEqualTo("/an/endpoint"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{\"to_debit\": 500 }")));

    }

    public void setupStub2(WireMockRule wireMockRule) {

        wireMockRule.stubFor(get(urlEqualTo("/an/endpoint2"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{ hello }")));

    }

}
