package steps;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import cucumber.api.java.Before;
import utils.BaseStep;

import java.util.Properties;

public class Hooks implements BaseStep {

    static Properties generalProperties=new Properties();

  @Before
    public void setUp(){
      RestAssured.defaultParser = Parser.JSON;
      String generalPropertiesFilePath=System.getProperty("user.dir")+"/src/test/resources/configs/"+System.getProperty("env")+".properties";
      generalProperties= fileHelper.loadPropertiesFile(generalPropertiesFilePath);

      restHelper.setBaseURI(fileHelper.getValueFromPropertiesFile(generalProperties, "Base_URI"));

    }

}
