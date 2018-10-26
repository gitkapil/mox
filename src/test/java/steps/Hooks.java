package steps;

import com.cucumber.listener.Reporter;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.junit.AssumptionViolatedException;
import apiHelpers.TestContext;
import java.io.File;
import java.util.Properties;

public class Hooks{
    TestContext testContext;
    static Properties envProperties=new Properties();
    static Properties generalProperties=new Properties();

    public Hooks(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before
    public void setUp(){
      RestAssured.defaultParser = Parser.JSON;
      String generalPropertiesFilePath=System.getProperty("user.dir")+"/src/test/resources/configs/"+System.getProperty("env")+".properties";
      envProperties= testContext.getFileHelper().loadPropertiesFile(generalPropertiesFilePath);
      generalProperties = testContext.getFileHelper().loadPropertiesFile(System.getProperty("user.dir")+"/src/test/resources/configs/general.properties");

    }


    @Before("@skiponsitmerchant")
    public void beforeScenario2() {
        if(System.getProperty("env").equalsIgnoreCase("sit") && System.getProperty("usertype").equalsIgnoreCase("merchant")) {
            throw new AssumptionViolatedException("Not supported on SIT merchant env");
        }
    }


    @After
    public static void writeExtentReport() {
        Reporter.loadXMLConfig(new File(System.getProperty("user.dir")+"/src/test/resources/extent-config.xml"));
        Reporter.setSystemInfo("Environment", System.getProperty("env"));
        Reporter.setSystemInfo("API Version", System.getProperty("version"));
        Reporter.setSystemInfo("User Type", System.getProperty("usertype"));

    }

}
