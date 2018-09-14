package steps;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import cucumber.api.java.Before;
import org.junit.AssumptionViolatedException;
import utils.BaseStep;

import java.util.Properties;

public class Hooks implements BaseStep {

    static Properties generalProperties=new Properties();

  @Before
    public void setUp(){
      RestAssured.defaultParser = Parser.JSON;
      String generalPropertiesFilePath=System.getProperty("user.dir")+"/src/test/resources/configs/"+System.getProperty("env")+".properties";
      generalProperties= fileHelper.loadPropertiesFile(generalPropertiesFilePath);
    }

  @Before("@skiponcimerchant")
  public void beforeScenario() {
    if(System.getProperty("env").equalsIgnoreCase("ci") && System.getProperty("usertype").equalsIgnoreCase("merchant")) {
      throw new AssumptionViolatedException("Not supported on CI merchant env");
    }
  }

    @Before("@skiponsitmerchant")
    public void beforeScenario2() {
        if(System.getProperty("env").equalsIgnoreCase("sit") && System.getProperty("usertype").equalsIgnoreCase("merchant")) {
            throw new AssumptionViolatedException("Not supported on SIT merchant env");
        }
    }

}
