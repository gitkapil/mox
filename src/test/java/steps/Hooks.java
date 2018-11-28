package steps;

import com.cucumber.listener.Reporter;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import managers.UtilManager;
import org.junit.AssumptionViolatedException;
import java.io.File;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Properties;

public class Hooks extends UtilManager{
    static Properties envProperties=new Properties();
    static Properties generalProperties=new Properties();
    static String hostIP=null;


    @Before
    public void setUp(){
      RestAssured.defaultParser = Parser.JSON;
      String generalPropertiesFilePath=System.getProperty("user.dir")+"/src/test/resources/configs/"+System.getProperty("env")+".properties";
      envProperties= getFileHelper().loadPropertiesFile(generalPropertiesFilePath);
      generalProperties = getFileHelper().loadPropertiesFile(System.getProperty("user.dir")+"/src/test/resources/configs/general.properties");
        try {
            hostIP= Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }


    @Before("@skiponsitmerchant")
    public void beforeScenario2() {
        if(System.getProperty("env").contains("sit") && System.getProperty("usertype").equalsIgnoreCase("merchant")) {
            throw new AssumptionViolatedException("Not supported on SIT merchant env");
        }
    }

    @Before("@skiponsandbox")
    public void beforeScenario3() {
        if(System.getProperty("usertype").equalsIgnoreCase("developer")) {
            throw new AssumptionViolatedException("Not to be executed in Sandbox");
        }
    }

    @Before("@skiponmerchant")
    public void beforeScenario4() {
        if(System.getProperty("usertype").equalsIgnoreCase("merchant")) {
            throw new AssumptionViolatedException("Not to be executed in Merchant");
        }
    }


   /* @After
    public static void writeExtentReport() {
        Reporter.loadXMLConfig(new File(System.getProperty("user.dir")+"/src/test/resources/extent-config.xml"));
        Reporter.setSystemInfo("Environment", System.getProperty("env"));
        Reporter.setSystemInfo("API Version", System.getProperty("version"));
        Reporter.setSystemInfo("User Type", System.getProperty("usertype"));

    } */

}
