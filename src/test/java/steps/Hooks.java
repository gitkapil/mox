package steps;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import managers.UtilManager;
import org.junit.AssumptionViolatedException;
import utils.PropertyHelper;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Properties;

public class Hooks extends UtilManager {
    static Properties envProperties = new Properties();
    static Properties generalProperties = new Properties();
    static String hostIP = null;

    @Before
    public void setUp() {
        RestAssured.defaultParser = Parser.JSON;
        String generalPropertiesFilePath = PropertyHelper.getInstance().getPropertyCascading("user.dir") + "/src/test/resources/configs/" + PropertyHelper.getInstance().getPropertyCascading("env") + ".properties";
        envProperties = getFileHelper().loadPropertiesFile(generalPropertiesFilePath);
        generalProperties = getFileHelper().loadPropertiesFile(PropertyHelper.getInstance().getPropertyCascading("user.dir") + "/src/test/resources/configs/general.properties");
        try {
            hostIP = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    @Before("@skiponsitmerchant")
    public void beforeScenario2() {
        if (PropertyHelper.getInstance().getPropertyCascading("env").contains("sit")
                && PropertyHelper.getInstance().getPropertyCascading("usertype").equalsIgnoreCase("merchant")) {
            throw new AssumptionViolatedException("Not supported on SIT merchant env");
        }
    }

    @Before("@skiponversionten")
    public void beforeScenario5() {
        if (PropertyHelper.getInstance().getPropertyCascading("version").contains(".10")) {
            throw new AssumptionViolatedException("Not supported on version 10");
        }
    }

    @Before("@skiponversioneleven")
    public void beforeScenario6() {
        if (PropertyHelper.getInstance().getPropertyCascading("version").contains(".11")) {
            throw new AssumptionViolatedException("Not supported on version 11");
        }
    }

    @Before("@skiponversiontwelve")
    public void beforeScenario7() {
        if (PropertyHelper.getInstance().getPropertyCascading("version").contains(".12")) {
            throw new AssumptionViolatedException("Not supported on version 12");
        }
    }

    @Before("@skiponsandbox")
    public void beforeScenario3() {
        if (PropertyHelper.getInstance().getPropertyCascading("usertype").equalsIgnoreCase("developer")) {
            throw new AssumptionViolatedException("Not to be executed in Sandbox");
        }
    }

    @Before("@skiponmerchant")
    public void beforeScenario4(Scenario scenario) {
        if (PropertyHelper.getInstance().getPropertyCascading("usertype").equalsIgnoreCase("merchant")) {
            if (scenario.getSourceTagNames().equals("skipOnMerchant")) {
                System.out.println(scenario.getName());
                if (scenario.getStatus().equalsIgnoreCase("pending")) {
                }
            }
            throw new AssumptionViolatedException("Not to be executed in Merchant");

        }
    }

}
