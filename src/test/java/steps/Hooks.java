package steps;

import cucumber.api.java.Before;
import utils.BaseStep;

import java.util.Properties;

public class Hooks implements BaseStep {

    static Properties generalProperties=new Properties();

  @Before
    public void setUp(){
      String generalPropertiesFilePath=System.getProperty("user.dir")+"/src/test/resources/configs/"+System.getProperty("env")+".properties";
      generalProperties= fileHelper.loadPropertiesFile(generalPropertiesFilePath);

      restHelper.setBaseURI(fileHelper.getValueFromPropertiesFile(generalProperties, "Base_URI"));

    }

}
