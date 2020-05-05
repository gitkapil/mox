package steps;

import cucumber.api.java.Before;
import managers.UtilManager;
import utils.PropertyHelper;

import java.util.Properties;

public class Hooks extends UtilManager {
    static Properties generalProperties = new Properties();

    @Before
    public void setUp() {
        System.out.println("Hooks to run before execution");
        generalProperties = getFileHelper().loadPropertiesFile(PropertyHelper.getInstance().getPropertyCascading("user.dir") + "/src/test/resources/configs/general.properties");
    }
}
