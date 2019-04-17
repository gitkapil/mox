package steps;

import managers.TestContext;
import managers.UtilManager;
import utils.EnvHelper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static org.junit.Assert.assertTrue;

public class ManagementCommon extends UtilManager {

    private TestContext testContext;

    public ManagementCommon(TestContext testContext) {
        this.testContext = testContext;
    }

    public void iAmAnAuthorizedDragonUser(Set<String> roleSet, Consumer<String> tokenConsumer)  {

        boolean isAuthorisedInCsoRole = Optional.ofNullable(testContext.getApiManager().getAccessToken().retrieveClaimSet(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "jwks_uri_idp")).getClaim("roles"))
                .filter(v -> v instanceof List)
                .map(v -> (List)v)
                .orElse(Collections.emptyList())
                .stream()
                .anyMatch(v -> roleSet.contains(v));

        assertTrue(String.format("Expected token to have one of the CSO roles %s", roleSet.toString()), isAuthorisedInCsoRole);

        tokenConsumer.accept(testContext.getApiManager().getAccessToken().getAccessToken());

        if(testContext.getApiManager().getAccessToken().getType().equalsIgnoreCase("merchant")){
            getRestHelper().setBaseURI(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-api-management-url")
                    +getEnvSpecificBasePathAPIs());
        } else {
            getRestHelper().setBaseURI(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "sandbox-api-management-url")
                    +getEnvSpecificBasePathAPIs());
        }
    }

    public void iAmADragonUserWithToken(String token, Consumer<String> tokenConsumer)  {
        tokenConsumer.accept(token);

        if(testContext.getApiManager().getAccessToken().getType().equalsIgnoreCase("merchant")){
            getRestHelper().setBaseURI(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "merchant-api-management-url")
                    +getEnvSpecificBasePathAPIs());
        } else {
            getRestHelper().setBaseURI(getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "sandbox-api-management-url")
                    +getEnvSpecificBasePathAPIs());
        }
    }

    public String getEnvSpecificBasePathAPIs() {
        if (EnvHelper.getInstance().isLocalDevMode()) {
            return getFileHelper().getValueFromPropertiesFile(Hooks.envProperties, "Base_Path_Management");
        }
        return getFileHelper().getValueFromPropertiesFile(Hooks.generalProperties, "Base_Path_Management");
    }

}
