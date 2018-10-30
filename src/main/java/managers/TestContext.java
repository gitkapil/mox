package managers;

public class TestContext {
    ApiManager apiManager;

    public TestContext() {
        apiManager= new ApiManager();
    }

    public ApiManager getApiManager() {
        return apiManager;
    }
}
