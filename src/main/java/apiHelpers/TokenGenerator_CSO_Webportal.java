package apiHelpers;

//package com.hsbc.digital.bdd.selenium;​

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import utils.Constants;

import javax.xml.bind.Element;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TokenGenerator_CSO_Webportal {

    public static void getCode() {

        JavascriptExecutor js;
        DesiredCapabilities capabilities;
        WebElement elmPeakClients, elmViewPM4BClients;
        try {
            System.setProperty("webdriver.chrome.driver", "./src/test/resources/Drivers/chromedriver.exe");
            System.out.println("Set Property cleared ----------->");
            // String UAT_url = "https://cms-uat3.allyoupayclouds.com/cs/#/login";
            String UAT_url = "https://cms-uat3.allyoupayclouds.com/cs";
            String CI_url = "https://cms-dev.allyoupayclouds.com/cs/#/login";
            String SIT_url = "https://cms-uat5.allyoupayclouds.com/cs/#/login";

            System.out.println("SIT_url: " + SIT_url);

            BrowserMobProxy proxy = new BrowserMobProxyServer();
            proxy.start(0);
            // get the Selenium proxy object
            Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);


            proxy.enableHarCaptureTypes(CaptureType.REQUEST_HEADERS);
            ChromeOptions options = new ChromeOptions();
            //options.setCapability(CapabilityType.PROXY, seleniumProxy);
            options.setAcceptInsecureCerts(true);
            //options.addArguments("headless");
            options.addArguments("test-type");
            options.addArguments("--start-maximized");
            options.addArguments("--disable-web-security");
            options.addArguments("--allow-running-insecure-content");

            capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
            //capabilities.setCapability("chrome.binary", "chromedriver.exe");


            WebDriver driver = new ChromeDriver(options);
            //WebDriver driver = new ChromeDriver(capabilities);

            // enable more detailed HAR capture, if desired (see CaptureType for the complete list)
            proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

            proxy.newHar("test-har");

            driver.get(SIT_url);
            driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
            driver.findElement(By.name("userName")).sendKeys("admin.megha");
            driver.findElement(By.name("password")).sendKeys("Megh@123");
            driver.findElement(By.xpath("//*[text()='Sign in']")).click();
            Thread.sleep(10000);

            js = (JavascriptExecutor) driver;
            elmPeakClients = driver.findElement(By.xpath("//a[@ng-show='true']//span[contains(text(),'Peak - Clients')]"));
            elmPeakClients.click();
            System.out.println("PEAK CLIENTS");
            Thread.sleep(10000);

            elmViewPM4BClients = driver.findElement(By.xpath("//span[contains(text(),'View PM4B Clients')]"));
            elmViewPM4BClients.click();
            System.out.println("View PM4B Clients CLICKED");

            //js.executeScript("arguments[0].scrollIntoView();", Element);

            By firstRecord = By.xpath("//table/tbody/tr[1]/td[4]");
            try {
                Thread.sleep(3000);
                Actions actions = new Actions(driver);
                actions.doubleClick(driver.findElement(firstRecord)).perform();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(3000);
            System.out.println("Business Profile Page !!");

            //Try pressing F12
            /*Actions actions = new Actions(driver);
            actions.sendKeys(Keys.F12).perform();
            Thread.sleep(30000);
            System.out.println("successfuly pressed key F12");
            driver.getCurrentUrl();
            Thread.sleep(30000);*/

            int start = 0;
            Thread.sleep(10000);
            Har har = proxy.getHar();
            StringWriter writer = new StringWriter();
            System.out.println("Har Entries :" + proxy.getHar().getLog().getEntries());
            System.out.println("Har Browser :" + proxy.getHar().getLog().getBrowser());
            System.out.println("proxy started: " + proxy.isStarted());
            System.out.println("proxy.getAllHeaders : " + proxy.getAllHeaders());

            int totalsize = proxy.getHar().getLog().getEntries().size();
            System.out.println("Har Entries Authorization :" + har.getLog().getEntries().get(totalsize - 1).getRequest().getHeaders().get(3));
            String Authorization = String.valueOf(har.getLog().getEntries().get(totalsize - 1).getRequest().getHeaders().get(3));
            String Authorizationurl = null;
            if (Authorization.contains("Authorization=")) {
                Authorizationurl = Authorization.split("=")[1];
            }
            /*Constants.JSON_RESPONSE_IDTOKEN = Authorizationurl;
            System.out.println("Authorization is " + Constants.JSON_RESPONSE_IDTOKEN);*/

            driver.close();
            driver.quit();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        getCode();
    }
}