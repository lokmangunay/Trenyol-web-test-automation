package com.trendyol.step;

import com.github.javafaker.Faker;
import com.thoughtworks.gauge.Table;
import com.trendyol.Constants;
import com.trendyol.Drivers.LocalDriver;
import com.trendyol.page.Page;
import com.trendyol.utils.lists.ClassList;
import com.trendyol.utils.saving.SavedValue;
import org.apache.commons.lang.RandomStringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseSteps extends LocalDriver {
    int iterationCount = 0;
    Random random = new Random();
    public BaseSteps() {
        ClassList.getInstance().put(this);
    }

    public boolean areElementsDisplayed(By... locators) {
        for (By locator : locators) {
            if (!isDisplayedBy(locator)) {
                return false;
            }
        }
        return true;
    }

    public String getColumnValues(Table table, String columnName, int index) throws Exception {
        if (checkColumnNames(table, columnName)) {
            try {
                return table.getColumnValues(columnName).get(index);
            } catch (Exception e) {
                throw new Exception("\nIndex out of TABLE's bounds. " +
                        "\nTable size: " + table.getTableRows().size() +
                        "\nActual Index: " + index);
            }
        }
        throw new Exception("Table does not contain any column called: " + columnName);
    }

    public boolean checkColumnNames(Table table, String columnName) throws Exception {
        if (table.getColumnNames().contains(columnName)) {
            return true;
        } else {
            logger.error("Table does not contain any column called: " + columnName);
            return false;
        }
    }
    public WebElement findElement(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        By infoParam = Page.getPage(pageName).getBy(key);
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    public WebElement findElement(By infoParam) {
        logger.info("Entered. Parameters; infoParam: {}", infoParam);
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    public List<WebElement> findElements(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        return driver.findElements(Page.getPage(pageName).getBy(key));
    }

    public List<WebElement> findElements(By by) {
        logger.info("Entered. Parameters; by: {}", by);
        return driver.findElements(by);
    }

    public void clickElement(By by) {
        logger.info("Entered. Parameters; by: {}", by);
        try {
            clickElement(findElement(by));
        } catch (ElementClickInterceptedException exception) {
            scrollByJs(by);
            clickElement(findElement(by));
        }
    }

    public boolean clickIfElementExist(By by) {
        logger.info("Entered. Parameters; by: {}", by);
        waitForPageToCompleteState();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            scrollIntoKeyByJs(driver.findElement(by));
            clickElement(findElement(by));
            waitForPageToCompleteState();
            return true;
        } catch (TimeoutException e) {
            logger.info("Element not found");
        }
        return false;
    }




    public void clickElement(WebElement element) {
        logger.info("Entered. Parameters; element: {}", element);
        try {
            element.click();
            logger.info("Clicks on the element. Parameters; element: {}", element);
        } catch (ElementClickInterceptedException exception) {
            javaScriptClicker(element);
            logger.info("Caught. Clicks on the element with JS ");
        }
    }


    public void hoverOnTheElementAndClickBy(By by) {
        logger.info("Entered. Parameters; by: {}", by);
        try {
            hoverElement(findElement(by));
            clickElement(by);
            waitForPageToCompleteState();
        } catch (ElementClickInterceptedException exception) {
            logger.info(exception + " has caught");
            scrollByJs(by);
            hoverElement(findElement(by));
            clickElement(by);
            waitForPageToCompleteState();
        }
    }

    public void hoverAndClickElementByJs(By by) {
        logger.info("Entered. Parameters; by: {}", by);
        hoverElement(findElement(by));
        waitByMilliSeconds(1000);
        javaScriptClicker(findElement(by));
        waitForPageToCompleteState();
        logger.info("{} clicked", by);
    }

    public void hoverOnTheElementAndClickElement(WebElement element) {
        logger.info("Entered. Parameters; element: {}", element);
        try {
            hoverElement(element);
            clickElement(element);
            waitForPageToCompleteState();
        } catch (ElementClickInterceptedException | StaleElementReferenceException exception) {
            logger.info("Error: {} has caught", exception.toString());
            scrollByJs(element);
            hoverElement(element);
            clickElement(element);
            waitForPageToCompleteState();
        }
    }

    public void clickByJs(WebElement webElement) {
        logger.info("Entered. Parameters; webElement: {}", webElement);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", webElement);
    }

    public void doubleClick(WebElement elementLocator) {
        logger.info("Entered. Parameters; elementLocator: {}", elementLocator);
        actions.doubleClick(elementLocator).perform();
    }

    public void hoverElement(WebElement element) {
        logger.info("Entered. Parameters; element: {}", element);
        actions.moveToElement(element).build().perform();
    }

    public void hoverElementBy(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        WebElement webElement = findElement(key, pageName);
        actions.moveToElement(webElement).build().perform();
    }

    public void sendKeys(By by, String text) {
        logger.info("Entered. Parameters; by: {}, text: {}", by, text);
        findElement(by).sendKeys(text);
    }

    public boolean isInputValid(WebElement element, int minTextLength) {
        logger.info("Entered. Parameters; element: {}", element);

        String inputText = element.getAttribute("value");

        return isNotBlank(inputText) && inputText.length() >= minTextLength;
    }

    private boolean isNotBlank(String text) {
        return text != null && !text.isEmpty();
    }


    public void sendKeyESC(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        findElement(key, pageName).sendKeys(Keys.ESCAPE);
    }

    public void sendKeyESC(By by) {
        logger.info("Entered. Parameters; by: {}", by);
        actions.sendKeys(findElement(by), Keys.ESCAPE).perform();
    }

    public void sendKeyESCByAction() {
        logger.info("Entered");
        actions.sendKeys(Keys.ESCAPE).perform();
        logger.info("Clicked to the Escape Key");
    }

    public void sendKeysJavascript(String text, String key, String pageName) {
        logger.info("Entered. Parameters; text: {}, key: {}, pageName: {}", text, key, pageName);
        WebElement webElement = driver.findElement(Page.getPage(pageName).getBy(key));
        JavascriptExecutor ex = (JavascriptExecutor) driver;
        ex.executeScript("arguments[0].value='" + text + "';", webElement);
    }

    public void sendKeysJavascript(String text, By by) {
        logger.info("Entered. Parameters; text: {}, by: {}", text, by);
        WebElement webElement = driver.findElement(by);
        JavascriptExecutor ex = (JavascriptExecutor) driver;
        ex.executeScript("arguments[0].value='" + text + "';", webElement);
    }


    public void sendKeysWithoutElement(String text) {
        logger.info("Entered. Parameters; text: {}", text);
        actions.sendKeys(text).perform();
    }

    public void writeToElementByJs(WebElement webElement, String text) {
        logger.info("Entered. Parameters; webElement: {}, text: {}", webElement, text);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value=arguments[1]", webElement, text);
    }

    public void javaScriptClicker(WebDriver driver, WebElement element) {
        logger.info("Entered. Parameters; driver: {}, element: {}", driver, element);
        JavascriptExecutor jse = ((JavascriptExecutor) driver);
        jse.executeScript("var evt = document.createEvent('MouseEvents');"
                + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
                + "arguments[0].dispatchEvent(evt);", element);
    }

    public void javaScriptClicker(WebElement element) {
        logger.info("Entered. Parameters; element: {}", element);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    public boolean isDisplayed(WebElement element) {
        logger.info("Entered. Parameters; element: {}", element);
        return element.isDisplayed();
    }

    public boolean isDisplayed(By by) {
        logger.info("Entered. Parameters; element: {}", by);
        return findElement(by).isDisplayed();
    }

    public boolean isDisplayedBy(By by) {
        logger.info("Entered. Parameters; by: {}", by);
        try {
            waitByMilliSeconds(1000);
            return driver.findElement(by).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            logger.info(e + " caught");
            return false;
        }
    }

    public boolean isEnabledBy(By by) {
        logger.info("Entered. Parameters; by: {}", by);
        try {
            return driver.findElement(by).isEnabled();
        } catch (TimeoutException | NoSuchElementException e) {
            logger.info(e + " caught");
            return false;
        }
    }

    public String getElementAttributeValue(String key, String attribute, String pageName) {
        logger.info("Entered. Parameters; key: {}, attribute: {}, pageName: {}", key, attribute, pageName);
        return findElement(key, pageName).getAttribute(attribute);
    }

    public String getElementAttributeValue(By by, String attribute) {
        logger.info("Entered. Parameters; By: {}, attribute: {}", by, attribute);
        return findElement(by).getAttribute(attribute);
    }

    public void waitByMilliSeconds(long milliseconds) {
        logger.info("Entered. Parameters; milliseconds: {}", milliseconds);
        try {
            logger.info("Waiting for " + milliseconds  + " milliseconds");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * An element will be randomly selected from the list.
     * @param key
     * @param pageName
     */
    public void randomPick(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        List<WebElement> elements = findElements(key, pageName);
        int index = random.nextInt(elements.size());
        elements.get(index).click();
    }

    public void randomPick(By by) {
        logger.info("Entered. Parameters; key: {}", by);
        List<WebElement> elements = findElements(by);
        int index = random.nextInt(elements.size());
        hoverElement(elements.get(index));
        elements.get(index).click();
    }

    public void randomPickTimes(By by, int times) {
        logger.info("Entered. Parameters; by: {},times: {}", by, times);
        for (int i = 0; i < times; i++) {
            List<WebElement> elements = findElements(by);
            int index = random.nextInt(elements.size());
            hoverElement(elements.get(index));
            clickElement(elements.get(index));
            waitByMilliSeconds(500);
        }
    }

    //Javascript driverın başlatılması
    public JavascriptExecutor getJSExecutor() {
        logger.info("Entered.");
        return (JavascriptExecutor) driver;
    }

    //Javascript scriptlerinin çalışması için gerekli fonksiyon
    public Object executeJS(String script, boolean wait) {
        logger.info("Entered. Parameters; script: {}, wait: {}", script, wait);
        return wait ? getJSExecutor().executeScript(script, "") : getJSExecutor().executeAsyncScript(script, "");
    }

    //Belirli bir locasyona sayfanın kaydırılması
    public void scrollTo(int x, int y) {
        logger.info("Entered. Parameters; x: {}, y: {}", x, y);
        String script = String.format("window.scrollTo(%d, %d);", x, y);
        executeJS(script, true);
    }

    public static void scrollToTop(WebDriver driver) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("window.scrollTo(0, 0);");
    }

    //?
    public boolean shouldSeeText(String text) {
        logger.info("Entered. Parameters; text: {}", text);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='" + text + "']")));
            return true;
        } catch (TimeoutException e) {
            logger.warn(text + " is not visible");
        }
        return false;
    }

    //?
    public boolean shouldSeeContainingText(String text) {
        logger.info("Entered. Parameters; text: {}", text);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(.,'" + text + "')]")));
            return true;
        } catch (TimeoutException e) {
            logger.warn(text + " is not visible");
        }
        return false;
    }

    public WebElement scrollToElementToBeVisible(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        return scrollToElementToBeVisible(Page.getPage(pageName).getBy(key));
    }

    public WebElement scrollToElementToBeVisible(By by) {
        logger.info("Entered. Parameters; by: {}", by);
        return scrollToElementToBeVisible(findElement(by));
    }

    public WebElement scrollToElementToBeVisible(WebElement webElement) {
        logger.info("Entered. Parameters; webElement: {}", webElement);
        if (webElement != null) {
            scrollTo(webElement.getLocation().getX(), webElement.getLocation().getY() - 200);
        }
        waitByMilliSeconds(500);
        return webElement;
    }

    public void scrollIntoKeyByJs(WebElement webElement) {
        logger.info("Entered. Parameters; webElement: {}", webElement);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
    }

    public Long getTimestamp() {
        logger.info("Entered.");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return (timestamp.getTime());
    }

    public int chooseDate(int number) {
        logger.info("Entered. Parameters; number: {}", number);
        LocalDateTime date = LocalDateTime.now().plusDays(number);
        return date.getDayOfMonth();
    }

    public void waitForTheElement(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.until(ExpectedConditions.presenceOfElementLocated(Page.getPage(pageName).getBy(key)));
    }

    public void waitForTheElement(By by) {
        logger.info("Entered. Parameters; by: {}", by);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public void waitForTheElement(By by, int sec) {
        logger.info("Entered. Parameters; by: {}, minute: {}", by, sec);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(sec));
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public void waitForTheElement(WebElement webElement) {
        logger.info("Entered. Parameters; webElement: {}", webElement);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.until(ExpectedConditions.visibilityOf(webElement));
    }

    public void waitForInvisibilityOfTheElement(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(Page.getPage(pageName).getBy(key)));
    }

    public void waitForInvisibilityOfTheElement(By by) {
        logger.info("Entered. Parameters; by: {}", by);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public void waitForInvisibilityOfTheElement(By by, int sec) {
        logger.info("Entered. Parameters; by: {}, minute: {}", by, sec);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(sec));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public void waitForInvisibilityOfTheElement(WebElement webElement) {
        logger.info("Entered. Parameters; webElement: {}", webElement);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        wait.until(ExpectedConditions.invisibilityOf(webElement));
    }

    public void goToUrl(String url) {
        logger.info("Entered. Parameters; url: {}", url);
        driver.get(url);
        try {
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException ignore) {

        }
        logger.info("Connecting to the URL: " + url );
    }

    public String getCurrentUrl() {
        logger.info("Entered.");
        return driver.getCurrentUrl();
    }

    public void setAttribute(WebElement webElement, String attributeName, String value) {
        logger.info("Entered. Parameters; webElement: {}, attributeName: {}, value: {}", webElement, attributeName, value);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('" + attributeName + "', '" + value + "')", webElement);
    }

    public void refreshPage() {
        logger.info("Entered.");
        driver.navigate().refresh();
    }

    public void chromeZoomOut(String value) {
        logger.info("Entered. Parameters; value: {}", value);
        JavascriptExecutor jsExec = (JavascriptExecutor) driver;
        jsExec.executeScript("document.body.style.zoom = '" + value + "%'");
    }

    public void chromeOpenNewTab() {
        logger.info("Entered.");
        ((JavascriptExecutor) driver).executeScript("window.open()");
        switchNewWindow();
    }

    public void chromeFocusTabWithNumber(int number) {
        logger.info("Entered. Parameters; number: {}", number);
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(number - 1));
    }


    public void acceptChromeAlertPopup() {
        logger.info("Entered.");
        driver.switchTo().alert().accept();
    }


    public void scrollByJs(WebElement element) {
        logger.info("Entered. Parameters; element: {}", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        waitForPageToCompleteState();
    }

    public void scrollByJs(By by) {
        logger.info("Entered. Parameters; element: {}", by);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", findElement(by));
        waitForPageToCompleteState();
    }


    public boolean waitUntilElementClickable(By by) {
        logger.info("Entered. Parameters; by: {}", by);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
            logger.info("Element is clickable.");
            return true;
        } catch (TimeoutException e) {
            logger.warn("Element is not clickable.");        }
        return false;
    }

    public boolean waitUntilElementClickable(WebElement webElement) {
        logger.info("Entered. Parameters; by: {}", webElement);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(webElement));
            logger.info("Element is clickable.");
            return true;
        } catch (TimeoutException e) {
            logger.warn("Element is not clickable.");
        }
        return false;
    }

    public boolean ifElementExistsClickByJS(By by) {
        logger.info("Entered. Parameters; by: {}", by);
        waitForPageToCompleteState();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            scrollIntoKeyByJs(driver.findElement(by));
            javaScriptClicker(driver.findElement(by));
            logger.info(by.toString() + " clicked by JS");
            waitForPageToCompleteState();
            return true;
        } catch (TimeoutException e) {
            logger.info("Element not found");
        }
        return false;
    }

    public boolean ifElementExistsClick(WebElement element) {
        logger.info("Entered. Parameters; by: {}", element);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            javaScriptClicker(element);
            waitForPageToCompleteState();
            return true;
        } catch (TimeoutException e) {
            logger.info("Element not found");
        }
        return false;
    }


    public void clearByJs(WebElement webElement) {
        logger.info("Entered. Parameters; webElement: {}", webElement);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].value ='';", webElement);
    }


    public void waitForPageToCompleteState() {
        logger.info("Entered.");
        int counter = 0;
        int maxNoOfRetries = Constants.DEFAULT_MAX_ITERATION_COUNT;
        while (counter < maxNoOfRetries) {
            waitByMilliSeconds(Constants.DEFAULT_MILLISECOND_WAIT_AMOUNT);
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                if (js.executeScript("return document.readyState").toString().equals("complete")) {
                    break;
                }
            } catch (Exception ignored) {
            }
            counter++;
        }
    }

    public void navigateBack() {
        logger.info("Entered.");
        driver.navigate().back();
    }

    public void navigateForward() {
        logger.info("Entered.");
        driver.navigate().forward();
    }

    public void navigateBack(int times) {
        logger.info("Entered.");
        for (int i = 0; i < times; i++) {
            driver.navigate().back();
            waitByMilliSeconds(999);
        }
    }

    public void navigateForward(int times) {
        logger.info("Entered.");
        for (int i = 0; i < times; i++) {
            driver.navigate().forward();
            waitByMilliSeconds(987);
        }
    }

    public void getRecordsOnTheNetworkFromTheUrl() {
        logger.info("Entered.");
        String scriptToExecute = "var performance = window.performance || window.mozPerformance || window.msPerformance || window.webkitPerformance || {}; var network = performance.getEntries() || {}; return network;";
        String netData = ((JavascriptExecutor) driver).executeScript(scriptToExecute).toString();
        logger.info(netData);
    }

    public void switchNewWindow() {
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
    }

    public void switchTo() {
        logger.info("Entered.");
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
    }

    public void chromeFocusLastTab() {
        logger.info("Entered.");
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));
    }

    public void chromeFocusFrameWithWebElement(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        WebElement webElement = findElement(key, pageName);
        driver.switchTo().frame(webElement);
    }

    public void chromeFocusFrameWithBy(By by) {
        logger.info("Entered. Parameters; by: {}", by);
        WebElement webElement = findElement(by);
        driver.switchTo().frame(webElement);
    }

    public void switchDefaultContent() {
        logger.info("Entered.");
        driver.switchTo().defaultContent();
    }

    public String getPageSource() {
        logger.info("Entered.");
        return driver.switchTo().alert().getText();
    }

    public void clickElementSpecificTimes(By by, int times) {
        logger.info("Entered. Parameters; by: {}, times: {}", by, times);
        for (int i = 0; i < times; i++) {
            clickByJs(findElement(by));
        }
    }


    public void closeTheLastTabAndSwitchToNewWindow() {
        logger.info("Current window will close and switch to the new window");
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        driver.close();
        executor.executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0));
    }

    public void clearBrowserCache() {
        logger.info("All caches will delete.");
        driver.manage().deleteAllCookies();
    }

    public static String generateRandom(int count, boolean letters, boolean numbers) {
        return RandomStringUtils.random(count, letters, numbers);
    }

    public static Faker fakerRandom() {
        return fakerRandom();
    }

    public String generateRandomInteger(int count) {
        return RandomStringUtils.randomNumeric(count);
    }


    public WebElement returnElementIfEqualText(String text, By by) {
        logger.info("Entered. Parameters; text: {}, by: {}", text, by);
        List<WebElement> elements = findElements(by);
        WebElement searchedElement = null;
        for (WebElement element : elements) {
            if (text.equalsIgnoreCase(element.getText())) {
                searchedElement = element;
                break;
            }
        }
        return searchedElement;
    }

    public void chooseElementFromComboBox(String text, By by) {
        logger.info("Entered. Parameters; text: {}, by: {}", text, by);
        List<WebElement> comboBoxElement = findElements(by);
        boolean result;
        for (int i = 0; i < comboBoxElement.size(); i++) {
            if (comboBoxElement.get(i).getText().equalsIgnoreCase(text)) {
                clickElement(comboBoxElement.get(i));
                result = true;
                break;
            } else result = false;
        }
        logger.info("Comboboxdan " + text + " değeri seçildi");
    }

    public Double extractDigitsAndDots(String priceCurrency) {
        logger.info("Entered. Parameters; priceCurrency: {}", priceCurrency);
        return Double.parseDouble(priceCurrency.replaceAll("[^\\d\\.]", ""));
    }

    public String extractDigitsFromString(String str) {
        return str.replaceAll("[^0-9]", "").trim();
    }

    public long doubleToLong(double decimalNumber) {
        logger.info("Entered. Parameters; decimalNumber: {}", decimalNumber);
        return Math.round(decimalNumber);
    }

    public Double makeSavedValueDouble(String savedValueKey) {
        logger.info("Entered. Parameters; savedValueKey: {}", savedValueKey);
        return extractDigitsAndDots(String.valueOf(SavedValue.getInstance().getSavedValue(savedValueKey)));
    }

    public void getClientIP(String url) throws UnknownHostException {
        InetAddress myIp = InetAddress.getByName(url);
        logger.info("**********" + url + "****************= " + myIp.toString());
        logger.info("**********" + url + "****************= " + myIp.getCanonicalHostName());

    }

    public boolean isClickable(WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean clickIfClickable(String key, String pageName) {
        WebElement element = findElement(key, pageName);
        boolean isClickable = Stream.of(element)
                .map(e -> isClickable(e))
                .findFirst()
                .orElse(false);
        if (isClickable) {
            element.click();
            logger.info("Element with key '{}' is clickable and clicked on page '{}'", key, pageName);
        } else {
            logger.info("Element with key '{}' is not clickable on page '{}'", key, pageName);
        }
        return isClickable;
    }

    public String sendKeysAndGetText(By locator) {
        WebElement element = findElement(locator);
        String text = element.getAttribute("value");
        element.sendKeys("");
        return text;
    }

    public void selectATextFromTheList(By by, String text) {
        logger.info("Entered. Parameters; by: {}, text: {}", by, text);
        List<WebElement> comboBoxElement = findElements(by);
        if (!isDisplayed(findElement(by))) {
            waitForTheElement(by);
        }
        if (comboBoxElement.isEmpty()) {
            throw new RuntimeException("Couldn't find the element");
        }
        boolean result = false;
        for (int i = 0; i < comboBoxElement.size(); i++) {
            String texts = comboBoxElement.get(i).getText();
            if (texts.contains(text)) {
                clickElement(comboBoxElement.get(i));
                result = true;
                break;
            }
        }
        assertTrue(result, "Could not find the text");
        logger.info(by + " comboboxından " + text + " değeri seçildi");
    }

    public void writeATextToTheFieldAndSelectTheText(String text, By searchField, By comboBox) {
        logger.info("Entered. Parameters; text: {}, searchField: {}, comboBox: {}", text, searchField, comboBox);
        findElements(searchField).clear();
        findElement(searchField).sendKeys(text);
        waitByMilliSeconds(1000);
        List<WebElement> comboBoxElement = findElements(comboBox);
        if (!(isDisplayed(comboBoxElement.get(0)))) {
            waitForTheElement(comboBoxElement.get(0));
        }
        if (comboBoxElement.size() != 0) {
            String savedText = comboBoxElement.get(0).getText();
            clickElement(comboBoxElement.get(0));
            logger.info("Text: {} has selected on the key: {}", savedText, comboBox);
        }
    }

    public void selectTextFromDropDown(String text, By by) {
        logger.info("Entered. Parameters; text: {}, by: {}", text, by);
        if (!isDisplayed(findElement(by))) {
            waitForTheElement(by);
        }
        Select select = new Select(findElement(by));
        select.selectByVisibleText(text);
    }

    public String generateRandomEmail() {
        return Faker.instance().name().firstName() + "@sgveteris.com";
    }

    public void throwExceptionIfElementVisible(By by) throws Exception {
        logger.info("Entered. Parameters; by: {}", by);
        if (isDisplayed(by)) {
            throw new Exception(by + " --- is visible on the current page.");
        }
    }

    public boolean canConvertToInteger(String stringValue) {
        try {
            Integer.parseInt(stringValue);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int[] splitAndConvertToIntArray(String input, String regexCharacter) {
        return Arrays.stream(input.split(regexCharacter))
                .map(Integer::parseInt)
                .mapToInt(Integer::intValue)
                .toArray();
    }
    public boolean isRandom(String value){
        return value.equalsIgnoreCase("random");
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}