package apiHelpers;

import managers.ApiManager;
import managers.UtilManager;

public class TestContext {
    private ApiManager apiManager;
    private UtilManager utilManager;

    public TestContext() {
        this.apiManager = new ApiManager();
        this.utilManager = new UtilManager();
    }

    public ApiManager getApiManager() {
        return apiManager;
    }

    public UtilManager getUtilManager() {
        return utilManager;
    }
}
