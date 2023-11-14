package com.trendyol.Drivers;

import com.thoughtworks.gauge.*;
import com.trendyol.utils.OsCheck;
import com.trendyol.utils.ReadProperties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.time.Duration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalDriver {

    protected static WebDriver driver;
    public static Actions actions;
    DesiredCapabilities capabilities;
    public static ResourceBundle ConfigBrowserSetup;
    public static ResourceBundle ConfigUsers;
    String currentPlatform = OsCheck.getOperatingSystemType().name();
    protected Logger logger = LoggerFactory.getLogger(getClass());
    String browserNameLocale;

    public void setConfigFiles(){
        ConfigBrowserSetup = ReadProperties.readProp("ConfigBrowserSetup.properties");
        ConfigUsers = ReadProperties.readProp(("ConfigUsers.properties"));
    }

    @BeforeSuite
    public void beforeSuite(ExecutionContext executionContext) {

        logger.info("*************************************************************************" + "\r\n");
        logger.info("------------------------TEST PLAN-------------------------");
    }

    @BeforeSpec
    public void beforeSpec(ExecutionContext executionContext) {

        logger.info("=========================================================================" + "\r\n");
        logger.info("------------------------SPEC-------------------------");
        logger.info("SPEC FILE NAME: " + executionContext.getCurrentSpecification().getFileName());
        logger.info("SPEC NAME: " + executionContext.getCurrentSpecification().getName());
    }

    @BeforeScenario
    public void setUp() {
        setBrowserType();
        logger.info("************************************  BeforeScenario  ************************************");

        logger.info("Test will run in your local machine, OS Type: "+ currentPlatform +
                                                    "\nBrowser: " + browserNameLocale);
        driver = Drivers.getDriver(browserNameLocale).getLocale(capabilities);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(3));
        driver.manage().window().maximize();
        actions = new Actions(driver);
    }

    /**
     *  Chrome is the default driver for this project in case there is no such object called browserNameLocale in ConfigBrowserSetup.properties.
     */
    public void setBrowserType(){
        setConfigFiles();
        try {
            browserNameLocale = ConfigBrowserSetup.getString("browserNameLocale");
        }catch (MissingResourceException exception){
            logger.info(exception.toString() + "\nbrowserNameLocale is missing in ConfigBrowserSetup.properties. Chrome will run as default.");
            browserNameLocale = "chrome";
        }
    }

    @BeforeStep
    public void beforeStep(ExecutionContext executionContext) {

        //logger.info(executionContext.getCurrentStep().getText());
        logger.info("═════════  " + executionContext.getCurrentStep().getDynamicText() + "  ═════════");
    }

    @AfterStep
    public void afterStep(ExecutionContext executionContext) throws IOException {

        if (executionContext.getCurrentStep().getIsFailing()) {
            logger.error("\n\n\n" + driver.getPageSource());
            logger.error(executionContext.getCurrentSpecification().getFileName());
            logger.error("Message: " + executionContext.getCurrentStep().getErrorMessage() + "\r\n\n"
                    + executionContext.getCurrentStep().getStackTrace());
        }
        logger.info("══════════════════════════════════════════════════════════════════════════════════════════════════════");
        System.out.println("\r\n\n");
    }

    @AfterScenario
    public void afterScenario(ExecutionContext executionContext) {

        tearDown();
        if (executionContext.getCurrentScenario().getIsFailing()) {

            logger.info("\n\nTEST BAŞARISIZ");
        } else {

            logger.info("\n\nTEST BAŞARILI");
        }

        logger.info("_________________________________________________________________________" + "\r\n");
    }

    @AfterSpec
    public void afterSpec(ExecutionContext executionContext) {

        logger.info("=========================================================================" + "\r\n");
    }

    @AfterSuite
    public void afterSuite(ExecutionContext executionContext) {

        logger.info("*************************************************************************" + "\r\n");
    }


    public void tearDown() {
        if (getDriver() != null) {
          //  driver.quit();
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }

}



