package steps;

import com.jayway.restassured.http.ContentType;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.log4j.Logger;
import utils.BaseStep;

import java.util.Properties;

public class Hooks implements BaseStep {
    final static Logger logger = Logger.getLogger(Hooks.class);


 /*   @Before
    public void setUp(){

        restHelper.setBaseURI("http://localhost:3000/");
        restHelper.setBasePath("Customer");
        restHelper.setcontentType(ContentType.HTML);

        wireMockRule.start();


    }

    @After
    public void teardown()

    {
        wireMockRule.stop();
    } */


}
