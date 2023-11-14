package com.trendyol.Drivers;

import com.thoughtworks.gauge.BeforeScenario;
import com.trendyol.utils.OsCheck;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

    public enum Drivers {
        CHROME {
            @Override
            public RemoteWebDriver getLocale(DesiredCapabilities capabilities, Map<String, String> additionalParameters) {
                ChromeOptions chromeOptions = new ChromeOptions();
                Map<String, String> prefs = new HashMap<>();
                //prefs.put("profile.default_content_setting_values.notifications", 2);
                prefs.putAll(additionalParameters);
                chromeOptions.setExperimentalOption("mobileEmulation", prefs);
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
                //chromeOptions.setExperimentalOption("excludeSwitches", Arrays.asList("disable-popup-blocking", "enable-automation"));
                // chromeOptions.addArguments("--start-fullscreen");
                // chromeOptions.addArguments("start-maximized");
                //chromeOptions.addArguments("-AppleLanguages","(tr)");
                chromeOptions.addArguments("--lang=tr");
                chromeOptions.addArguments("--disable-popup-blocking", "disable-automation", "--disable-blink-features", "--disable-blink-features=AutomationControlled"
                        , "--disable-gpu", "--no-sandbox", "disable-infobars", "disable-plugins", "ignore-certificate-errors",
                        "disable-translate", "disable-extensions", "--disable-notifications", "--remote-allow-origins=*");
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("credentials_enable_service", false);
                parameters.put("password_manager_enabled", false);
                chromeOptions.setExperimentalOption("prefs", parameters);
                chromeOptions.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                //System.setProperty("webdriver.chrome.driver", "web_driver/chromedriver");
                //chromeOptions.merge(capabilities);
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver(chromeOptions);
            }
        }
,
        FIREFOX {
            @Override
            public RemoteWebDriver getLocale(DesiredCapabilities capabilities, Map<String, String> additionalParameters) {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("profile.default_content_setting_values.notifications", 2);
                prefs.putAll(additionalParameters);
                ///setExperimentalOption("prefs", prefs);
                firefoxOptions.addArguments("--kiosk");
                firefoxOptions.addArguments("--disable-notifications");
                firefoxOptions.addArguments("--start-fullscreen");
                FirefoxProfile profile = new FirefoxProfile();
                firefoxOptions.setProfile(profile);
                firefoxOptions.setCapability("marionette", true);
                firefoxOptions.setCapability("prefs", prefs);
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver(firefoxOptions);

            }

        },
        Safari {
            @Override
            public RemoteWebDriver getLocale(DesiredCapabilities capabilities, Map<String, String> additionalParameters) {
                SafariOptions safariOptions = new SafariOptions();
                //capabilities = DesiredCapabilities.safari();
                Map<String, Object> prefs = new HashMap<>();
                prefs.putAll(additionalParameters);
                safariOptions.setCapability("prefs", prefs);
                safariOptions.setAutomaticProfiling(false);
                return new SafariDriver(safariOptions);
            }
        };


    public RemoteWebDriver getLocale(DesiredCapabilities capabilities) {
        return getLocale(capabilities, new HashMap<>());
    }

    public abstract RemoteWebDriver getLocale(DesiredCapabilities capabilities, Map<String, String> additionalParameters);


    public static Drivers getDriver(String driverName) {
        for (Drivers drivers : Drivers.values()) {
            if (drivers.toString().equalsIgnoreCase(driverName)) {
                return drivers;
            }
        }
        throw new RuntimeException();
    }

}
