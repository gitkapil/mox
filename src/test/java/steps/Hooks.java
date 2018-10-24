package steps;

import com.cucumber.listener.Reporter;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import cucumber.api.java.Before;
import org.junit.AfterClass;
import org.junit.AssumptionViolatedException;
import utils.BaseStep;

import java.io.File;
import java.util.Properties;

public class Hooks implements BaseStep {

    static Properties envProperties=new Properties();
    static Properties generalProperties=new Properties();

  @Before
    public void setUp(){
      RestAssured.defaultParser = Parser.JSON;
      String generalPropertiesFilePath=System.getProperty("user.dir")+"/src/test/resources/configs/"+System.getProperty("env")+".properties";
      envProperties= fileHelper.loadPropertiesFile(generalPropertiesFilePath);
      generalProperties = fileHelper.loadPropertiesFile(System.getProperty("user.dir")+"/src/test/resources/configs/general.properties");

    }


    @Before("@skiponsitmerchant")
    public void beforeScenario2() {
        if(System.getProperty("env").equalsIgnoreCase("sit") && System.getProperty("usertype").equalsIgnoreCase("merchant")) {
            throw new AssumptionViolatedException("Not supported on SIT merchant env");
        }
    }


    @AfterClass
    public static void writeExtentReport() {
        Reporter.loadXMLConfig(new File(System.getProperty("user.dir")+"/src/test/resources/extent-config.xml"));
    }

}
