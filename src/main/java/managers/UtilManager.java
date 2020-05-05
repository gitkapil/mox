package managers;

import utils.*;

public class UtilManager {

    private RestHelper restHelper;
    private FileHelper fileHelper;

    public RestHelper getRestHelper() {
        return (restHelper == null) ? restHelper = new RestHelper() : restHelper;
    }

    public FileHelper getFileHelper() {
        return (fileHelper == null) ? fileHelper = new FileHelper() : fileHelper;
    }

}
