package com.trendyol.stepdefinition;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.trendyol.Constants;
import com.trendyol.page.Page;
import com.trendyol.step.BaseSteps;
import com.trendyol.utils.lists.ClassList;
import com.trendyol.utils.saving.SavedValue;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.trendyol.step.BaseSteps.actions;
import static org.junit.jupiter.api.Assertions.*;

public class CommonStepDefinitions {
    private final BaseSteps baseSteps = ClassList.getInstance().get(BaseSteps.class);


    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Step("Send by Js the text <text> on the element <key> in the <pageName>")
    public void sendTextByJsToTheKey(String text, String key, String pageName) {

        logger.info("Entered. Parameters; text: {}, key: {}, pageName: {}", text, key, pageName);
        baseSteps.sendKeysJavascript(text, key, pageName);
        baseSteps.waitForPageToCompleteState();
        logger.info(text + " sent to the " + key + " in the" + pageName);
    }

    @Step("Get <key> text in <pageName>")
    public String getElementText(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        return baseSteps.findElement(key, pageName).getText();
    }

    @Step("Print page source")
    public void printPageSource() {
        logger.info("Entered.");
        System.out.println(baseSteps.getPageSource());
    }

    @Step({"Wait <value> seconds",
            "<int> saniye bekle"})
    public void waitBySeconds(int seconds) {
        logger.info("Entered. Parameters; seconds: {}", seconds);
        try {
            logger.info("Program wait " + seconds + " seconds");
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step({"Wait <value> milliseconds",
            "<long> milisaniye bekle"})
    public void waitByMilliSeconds(long milliseconds) {
        logger.info("Entered. Parameters; milliseconds: {}", milliseconds);
        baseSteps.waitByMilliSeconds(milliseconds);
    }

    @Step({"Wait for element then click <key> in <pageName>",
            "Elementi bekle ve sonra tıkla <key> in <pageName>"})
    public void checkElementExistsThenClick(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        logger.info("Entered");
        getElementWithKeyIfExists(key, pageName);
        clickElement(key, pageName);
        baseSteps.waitForPageToCompleteState();
        logger.info(key + " Clicked.");
    }

    @Step({"Click to element <key> in <pageName>",
            "Elementine tıkla <key> in <pageName>"})
    public void clickElement(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        logger.info(key + " elementine tıklanacak.");
        baseSteps.hoverElement(baseSteps.findElement(key, pageName));
        waitBySeconds(1);
        baseSteps.javaScriptClicker(baseSteps.findElement(key, pageName));
        baseSteps.waitForPageToCompleteState();
        logger.info(key + " Clicked.");
    }

    @Step("Double click to element <key> in <pagaName>")
    public void doubleClick(String key, String pageName) {
        logger.info("Entered.");
        baseSteps.doubleClick(baseSteps.findElement(key, pageName));
    }

    @Step("If exist click to element <key> in <pageName>")
    public void clickElementIfExist(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        baseSteps.ifElementExistsClickByJS(Page.getPage(pageName).getBy(key));
    }


    @Step("Click to element <key>, <times> times in <pageName>")
    public void clickElementTimes(String key, int times, String pageName) {
        logger.info("Entered. Parameters; key: {}, times: {}, pageName: {}", key, times, pageName);
        baseSteps.clickElementSpecificTimes(Page.getPage(pageName).getBy(key), times);
    }

    @Step("Wait on the <key> using actions in the <pageName>")
    public void hoverOnElement(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        baseSteps.hoverElementBy(key, pageName);
        baseSteps.waitForPageToCompleteState();
    }

    @Step({"Click to element <key> with focus in <pageName>",
            "<key> elementine focus ile tıkla in <pageName>"})
    public void clickElementWithFocus(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        baseSteps.waitForTheElement(key, pageName);
        actions.moveToElement(baseSteps.findElement(key, pageName));
        actions.click();
        actions.build().perform();
        logger.info(key + " elementine focus ile tıklandı.");
    }

    @Step({"Check if element <key> exists in <pageName>",
            "Element var mı kontrol et <key> in <pageName>"})
    public WebElement getElementWithKeyIfExists(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        WebElement webElement;
        int loopCount = 0;
        while (loopCount < 3) {
            try {
                webElement = baseSteps.findElement(key, pageName);
                logger.info(key + " elementi bulundu.");
                return webElement;
            } catch (WebDriverException e) {
            }
            loopCount++;
            waitByMilliSeconds(60000);
        }
        assertFalse(Boolean.parseBoolean("Element: '" + key + "' doesn't exist."));
        return null;
    }

    @Step("<key> elementini kontrol et in <pageName>")
    public void checkElement(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        assertTrue(baseSteps.findElement(key, pageName).isDisplayed(), "Aranan element bulunamadı");
        if (!getElementText(key, pageName).isEmpty()) {
            logger.info(getElementText(key, pageName) + " değerli element bulundu.");
        } else {
            logger.info(key + " elementi bulundu.");
        }
    }

    @Step({"<text> texti sayfada bulunmamalı",
            "The <text> should not visible on the the current page"})
    public void checkElement(String text) {
        logger.info("Entered. Parameters; text: {}", text);
        assertFalse(baseSteps.shouldSeeText(text), "Beklenen element görünmektedir.");
    }

    @Step({"<text> texti sayfada bulunmalı",
            "The <text> should visible on the the current page"})
    public void checkNotElement(String text) {
        logger.info("Entered. Parameters; text: {}", text);
        assertTrue(baseSteps.shouldSeeText(text), "Beklenen element görünmemektedir.");
    }

    @Step({"Go to <url> address",
            "<url> adresine git"})
    public void goToUrl(String url) {
        logger.info("Entered. Parameters; key: {}", url);
        baseSteps.goToUrl(url);
        logger.info("Gidilen url = " + baseSteps.getCurrentUrl());
    }

    @Step({"Wait for the element <key> in <pageName>"})
    public void waitForElement(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        logger.info("Waiting for the " + key + " on the " + pageName);
        baseSteps.waitForTheElement(key, pageName);
        baseSteps.waitForPageToCompleteState();
        if (baseSteps.isDisplayed(baseSteps.findElement(key, pageName))) {
            logger.info(key + " has found on the " + pageName);
        } else {
            logger.info(key + " has not displayed on the " + pageName);
        }
    }

    @Step({"Wait for invisibility of the element <key> in <pageName>"})
    public void waitForInvisibilityOfTheElement(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        logger.info("Waiting for the " + key + " on the " + pageName);
        baseSteps.waitByMilliSeconds(3000);
        baseSteps.waitForInvisibilityOfTheElement(key, pageName);
        baseSteps.waitForPageToCompleteState();
    }

    @Step("Check if element <key> exists and log the current text in <pageName>")
    public void checkIfElementExistLogCurrentText(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        int loopCount = 0;
        while (loopCount < Constants.DEFAULT_MAX_ITERATION_COUNT) {
            if (baseSteps.findElements(key, pageName).size() > 0) {
                logger.info("Key: {}, text: {}", key, baseSteps.findElement(key, pageName).getText());
                return;
            }
            loopCount++;
            waitByMilliSeconds(Constants.DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail(key + " was not visible on the " + pageName);
    }

    @Step("Check if the element <key> is on the page and print to the log <pageName>")
    public void checkElementText(String key, String pageName) {
        logger.info("Checking element text. Parameters; key: {}, pageName: {}", key, pageName);
        WebElement element = baseSteps.findElement(key, pageName);
        String text = element.getText();

        Stream.of(text)
                .filter(t -> !t.trim().isEmpty())
                .findFirst()
                .ifPresentOrElse(
                        t -> logger.info("Element text: {}", t),
                        () -> logger.info("Element text is empty or null. Key: {}", key)
                );
    }

    @Step({"Check if element <key> not exists in <pageName>",
            "Element yok mu kontrol et <key> in <pageName>"})
    public void checkElementNotExists(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        int loopCount = 0;
        while (loopCount < Constants.DEFAULT_MAX_ITERATION_COUNT) {
            if (baseSteps.findElements(key, pageName).size() == 0) {
                logger.info(key + " elementinin olmadığı kontrol edildi.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(Constants.DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element '" + key + "' still exist.");
    }

    @Step({"Upload file in project <path> to element <key> in <pageName>",
            "Proje içindeki <path> dosyayı <key> elemente upload et in <pageName>"})
    public void uploadFile(String path, String key, String pageName) {
        logger.info("Entered. Parameters; path: {}, key: {}, pageName: {}", path, key, pageName);
        String pathString = System.getProperty("user.dir") + "/";
        pathString = pathString + path;
        baseSteps.findElement(key, pageName).sendKeys(pathString);
        logger.info(path + " dosyası " + key + " elementine yüklendi.");
    }

    @Step({"Write a value <text> to element <key> in <pageName>",
            "<text> textini <key> elemente yaz in <pageName>"})
    public void sendKeys(String text, String key, String pageName) {
        logger.info("Entered. Parameters; text: {}, key: {}, pageName: {}", text, key, pageName);
        baseSteps.findElement(key, pageName).clear();
        baseSteps.findElement(key, pageName).sendKeys(text);
        logger.info(key + " elementine " + text + " texti yazıldı.");
        baseSteps.waitForPageToCompleteState();
    }

    @Step({"Select a dropdown text <text> from <key> in <pageName>"})
    public void selectDropdown(String text, String key, String pageName) {
        baseSteps.clickElement(Page.getPage(pageName).getBy(key));
        List<WebElement> elementList = baseSteps.findElements(By.xpath("//*[@role='treeitem']"));
        for (WebElement element : elementList) {
            if (element.getText().equals(text)) {
                element.click();
                break;
            }
        }
    }

    @Step({"User type <text> text in <pageName>"})
    public void sendKeysWithoutElement(String text, String pageName) {
        logger.info("Entered. Parameters; text: {}, pageName: {}", text, pageName);
        baseSteps.sendKeysWithoutElement(text);
    }

    @Step({"User sends Escape key from the keyboard at <pageName>"})
    public void sendKeysWithoutElement(String pageName) {
        logger.info("Entered. Parameters; pageName: {}", pageName);
        baseSteps.sendKeyESCByAction();
    }

    @Step({"User sends Escape key to the element <key> from the keyboard at <pageName>"})
    public void sendKeysWithElement(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        baseSteps.sendKeyESC(key, pageName);
    }

    @Step({"Check if current URL contains the value <expectedURL>",
            "Şuanki URL <url> değerini içeriyor mu kontrol et"})
    public void checkURLContainsRepeat(String expectedURL) {
        logger.info("Entered. Parameters; expectedURL: {}", expectedURL);
        waitBySeconds(10);
        int loopCount = 0;
        String actualURL = baseSteps.getCurrentUrl();
        while (loopCount < Constants.DEFAULT_MAX_ITERATION_COUNT) {

            if (actualURL != null && actualURL.contains(expectedURL)) {
                logger.info("Şuanki URL: " + expectedURL + " değerini içeriyor.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(Constants.DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail(
                "Actual URL doesn't match the expected." + "Expected: " + expectedURL + ", Actual: "
                        + actualURL);
    }

    @Step({"Check if current URL contains the value <expectedURLTurkish> or <expectedURLEnglish>"})
    public void checkURLContainsValues(String expectedURLTurkish, String expectedURLEnglish) {
        logger.info("Entered. Parameters; expectedURLTurkish: {}, expectedURLEnglish: {}", expectedURLTurkish, expectedURLEnglish);
        int loopCount = 0;
        String actualURL = "";
        while (loopCount < Constants.DEFAULT_MAX_ITERATION_COUNT) {
            actualURL = baseSteps.getCurrentUrl();

            if ((actualURL != null) && ((actualURL.contains(expectedURLEnglish)) || (actualURL.contains(expectedURLTurkish)))) {
                logger.info("Current URL contains " + expectedURLEnglish + " or " + expectedURLTurkish + " values.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(Constants.DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail(
                "Actual URL doesn't match the expected." + "Expected: " + expectedURLTurkish + " or " + expectedURLEnglish + " Actual: "
                        + actualURL);
    }

    @Step({"Send TAB key to element <key> in <pageName>",
            "Elemente TAB keyi yolla <key> in <pageName>"})
    public void sendKeyToElementTAB(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        baseSteps.findElement(key, pageName).sendKeys(Keys.TAB);
        logger.info(key + " elementine TAB keyi yollandı.");
    }

    @Step({"Send BACKSPACE key to element <key> in <pageName>",
            "Elemente BACKSPACE keyi yolla <key> in <pageName>"})
    public void sendKeyToElementBACKSPACE(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        baseSteps.findElement(key, pageName).sendKeys(Keys.BACK_SPACE);
        logger.info(key + " elementine BACKSPACE keyi yollandı.");
    }

    @Step({"Send ESCAPE key to element <key> in <pageName>",
            "Elemente ESCAPE keyi yolla <key> in <pageName>"})
    public void sendKeyToElementESCAPE(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        baseSteps.findElement(key, pageName).sendKeys(Keys.ESCAPE);
        logger.info(key + " elementine ESCAPE keyi yollandı.");
    }

    @Step({"Check if element <key> has attribute <attribute> in <pageName>",
            "<key> elementi <attribute> niteliğine sahip mi in <pageName>"})
    public void checkElementAttributeExists(String key, String attribute, String pageName) {
        logger.info("Entered. Parameters; key: {}, attribute: {}, pageName: {}", key, attribute, pageName);
        WebElement element = baseSteps.findElement(key, pageName);
        int loopCount = 0;
        while (loopCount < Constants.DEFAULT_MAX_ITERATION_COUNT) {
            if (element.getAttribute(attribute) != null) {
                logger.info(key + " elementi " + attribute + " niteliğine sahip.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(Constants.DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element DOESN't have the attribute: '" + attribute + "'");
    }

    @Step({"Check if element <key> not have attribute <attribute> in <pageName>",
            "<key> elementi <attribute> niteliğine sahip değil mi in <pageName>"})
    public void checkElementAttributeNotExists(String key, String attribute, String pageName) {
        logger.info("Entered. Parameters; key: {}, attribute:{}, pageName: {}", key, attribute, pageName);
        WebElement element = baseSteps.findElement(key, pageName);

        int loopCount = 0;

        while (loopCount < Constants.DEFAULT_MAX_ITERATION_COUNT) {
            if (element.getAttribute(attribute) == null) {
                logger.info(key + " elementi " + attribute + " niteliğine sahip olmadığı kontrol edildi.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(Constants.DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element STILL have the attribute: '" + attribute + "'");
    }

    @Step({"Check if <key> element's attribute <attribute> equals to the value <expectedValue> in <pageName>",
            "<key> elementinin <attribute> niteliği <value> değerine sahip mi in <pageName>"})
    public void checkElementAttributeEquals(String key, String attribute, String expectedValue, String pageName) {
        logger.info("Entered. Parameters; key: {}, attribute: {}, expectedValue: {}, pageName: {}", key, attribute, expectedValue, pageName);
        WebElement element = baseSteps.findElement(key, pageName);
        String actualValue = "";
        int loopCount = 0;
        while (loopCount < Constants.DEFAULT_MAX_ITERATION_COUNT) {
            actualValue = element.getAttribute(attribute).trim();
            if (actualValue.equals(expectedValue)) {
                logger.info(
                        key + " elementinin " + attribute + " niteliği " + expectedValue + " değerine sahip.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(Constants.DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element's attribute value doesn't match expected value. It was " + actualValue);
    }

    @Step({"Check if <key> element's attribute <attribute> contains the value <expectedValue> in <pageName>",
            "<key> elementinin <attribute> niteliği <value> değerini içeriyor mu in <pageName>"})
    public void checkElementAttributeContains(String key, String attribute, String expectedValue, String pageName) {
        logger.info("Entered. Parameters; key: {}, attribute: {}, expectedValue: {}, pageName: {}", key, attribute, expectedValue, pageName);
        WebElement element = baseSteps.findElement(key, pageName);
        String actualValue;
        int loopCount = 0;
        while (loopCount < Constants.DEFAULT_MAX_ITERATION_COUNT) {
            actualValue = element.getAttribute(attribute).trim();
            if (actualValue.contains(expectedValue)) {
                logger.info("Entered. Parameters; expectedValue: {}, actualValue: {}", expectedValue, actualValue);
                return;
            }
            loopCount++;
            waitByMilliSeconds(Constants.DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element's attribute value doesn't contain expected value. Expected: " + expectedValue + "Actual: " + element.getAttribute(attribute).trim());
    }

    @Step({"Write <value> to <attributeName> of element <key> in <pageName>",
            "<value> değerini <attribute> niteliğine <key> elementi için yaz in <pageName>"})
    public void setElementAttribute(String value, String attributeName, String key, String pageName) {
        logger.info("Entered. Parameters; value: {}, attributeName: {}, key: {}, pageName: {}", value, attributeName, key, pageName);
        String attributeValue = baseSteps.findElement(key, pageName).getAttribute(attributeName);
        baseSteps.findElement(key, pageName).sendKeys(attributeValue, value);
    }

    @Step({"Write <value> to <attributeName> of element <key> with Js in <pageName>",
            "<value> değerini <attribute> niteliğine <key> elementi için JS ile yaz in <pageName>"})
    public void setElementAttributeWithJs(String value, String attributeName, String key, String pageName) {
        logger.info("Entered. Parameters; value: {}, attributeName: {}, key: {}, pageName: {}", value, attributeName, key, pageName);
        WebElement webElement = baseSteps.findElement(key, pageName);
        baseSteps.setAttribute(webElement, attributeName, value);
    }

    @Step({"Clear text of element <key> in <pageName>",
            "<key> elementinin text alanını temizle in <pageName>"})
    public void clearInputArea(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        baseSteps.findElement(key, pageName).clear();
    }

    @Step({"Clear text of element <key> with BACKSPACE in <pageName>",
            "<key> elementinin text alanını BACKSPACE ile temizle in <pageName>"})
    public void clearInputAreaWithBackspace(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        WebElement element = baseSteps.findElement(key, pageName);
        element.clear();
        element.sendKeys("a");
        actions.sendKeys(Keys.BACK_SPACE).build().perform();
    }

    @Step({"Check if element <key> contains text <expectedText> in <pageName>",
            "<key> elementi <text> değerini içeriyor mu kontrol et in <pageName>"})
    public void checkElementContainsText(String key, String expectedText, String pageName) {
        logger.info("Entered. Parameters; key: {}, expectedText: {}, pageName: {}", key, expectedText, pageName);
        boolean kontrol = false;
        if (baseSteps.findElement(key, pageName).getText().equalsIgnoreCase(expectedText)) {
            kontrol = true;
        } else if (baseSteps.findElement(key, pageName).getText().contains(expectedText)) {
            kontrol = true;
        } else {
            logger.info(baseSteps.findElement(key, pageName).getText());
        }
        logger.info(key + " elementi " + expectedText + " değerini içeriyor.");
        assertTrue(kontrol, "Expected text is not contained. Actual text was " + baseSteps.findElement(key, pageName).getText());
    }

    @Step({"Check if element <key> not contains text <expectedText> in <pageName>",
            "<key> elementi <text> değerini içermiyor mu kontrol et in <pageName>"})
    public void checkElementContainsNotText(String key, String expectedText, String pageName) {
        logger.info("Entered. Parameters; key: {}, expectedText: {}, pageName: {}", key, expectedText, pageName);
        boolean kontrol = false;

        if (baseSteps.findElement(key, pageName).getText().equalsIgnoreCase(expectedText)) {
            kontrol = true;
        } else if (baseSteps.findElement(key, pageName).getText().contains(expectedText)) {
            kontrol = true;
        } else {
            logger.info(baseSteps.findElement(key, pageName).getText());
        }
        assertFalse(kontrol, "Expected text is contained");
        logger.info(key + " elementi" + expectedText + "değerini içeriyor.");
    }

    @Step({"<key> elementi <TürkçeText> değerini veya <İngilizceText> değerini icermiyor mu? in <pageName>",
            "Check If the key <key> not contains the text <TurkishText> or the text <EnglishText> in the pageName <pageName>"})
    public void checkElementNotContainsTurkishOrEnglishText(String key, String expectedTurkishText, String expectedEnglishText, String pageName) {
        logger.info("Entered. Parameters; key: {}, expectedTurkishText: {}, expectedEnglishText: {}, pageName: {}", key, expectedTurkishText, expectedEnglishText, pageName);
        boolean containsText = false;
        expectedTurkishText = expectedTurkishText.replaceAll(" ", "");
        expectedEnglishText = expectedEnglishText.replaceAll(" ", "");
        String actualText = baseSteps.findElement(key, pageName).getText().replaceAll(" ", "");
        if (actualText.equalsIgnoreCase(expectedTurkishText) || actualText.equalsIgnoreCase(expectedEnglishText)) {
            containsText = true;
        } else if (actualText.contains(expectedTurkishText) || actualText.contains(expectedEnglishText)) {
            containsText = true;
        } else {
            System.out.println("girmemesi gereken kısıma  girdi.");
        }
        assertFalse(containsText, "Actual text: " + actualText + " is contained");
        logger.info("The " + key + "contains" + expectedTurkishText + " or " + expectedEnglishText + " It was: " + actualText);
    }

    @Step({"Check if url contains text <expectedText> in <pageName>",
            "Url <text> değerini içeriyor mu kontrol et in <pageName>"})
    public void checkUrlContainsText(String expectedText, String pageName) {
        logger.info("Entered. Parameters; expectedText: {}, pageName: {}", expectedText, pageName);

        String currentUrl = baseSteps.getCurrentUrl();
        boolean containsText = currentUrl.equalsIgnoreCase(expectedText) || currentUrl.contains(expectedText);

        if (!containsText) {
            logger.info("Current URL: {}", currentUrl);
        }

        assertTrue(containsText, "Current URL does not contain the path: " + expectedText + ".");
    }

    @Step({"Check if url equals to text <expectedText> in <pageName>"})
    public void checkExpectedUrl(String expectedText, String pageName) {
        logger.info("Entered. Parameters; expectedText: {}, pageName: {}", expectedText, pageName);

        String currentUrl = baseSteps.getCurrentUrl();
        boolean urlEqualsText = currentUrl.equalsIgnoreCase(expectedText);
        if (!urlEqualsText) {
            logger.info("Current URL: {}", currentUrl);
        }
        assertTrue(urlEqualsText, "Current URL does not contain the path: " + expectedText + "." + "  Current URL: " + currentUrl);
    }

    @Step({"<key> elementi <TürkçeText> değerini veya <İngilizceText> değerini iceriyor mu? in <pageName>",
            "Check If the key <key> contains the text <TurkishText> or the text <EnglishText> in the pageName <pageName>"})
    public void checkElementContainsTurkishOrEnglishText(String key, String expectedTurkishText, String expectedEnglishText, String pageName) {
        logger.info("Entered. Parameters; key: {}, expectedTurkishText: {}, expectedEnglishText: {}, pageName: {}", key, expectedTurkishText, expectedEnglishText, pageName);
        boolean containsText = false;
        expectedTurkishText = expectedTurkishText.replaceAll(" ", "");
        expectedEnglishText = expectedEnglishText.replaceAll(" ", "");
        String actualText = baseSteps.findElement(key, pageName).getText().replaceAll(" ", "");
        if (actualText.equalsIgnoreCase(expectedTurkishText) || actualText.equalsIgnoreCase(expectedEnglishText)) {
            containsText = true;
        } else if (actualText.contains(expectedTurkishText) || actualText.contains(expectedEnglishText)) {
            containsText = true;
        } else {
            System.out.println("girmemesi gereken kısıma  girdi.");
        }
        assertTrue(containsText, "Actual text: " + actualText + " is not contained");
        logger.info("The " + key + "contains" + expectedTurkishText + " or " + expectedEnglishText + " It was: " + actualText);
    }


    @Step({"Write value <string> to element <key> with focus in <pageName>",
            "<string> değerini <key> elementine focus ile yaz in <pageName>"})
    public void sendKeysWithFocus(String text, String key, String pageName) {
        logger.info("Entered. Parameters; text: {}, key: {}, pageName: {}", text, key, pageName);
        actions.moveToElement(baseSteps.findElement(key, pageName));
        actions.click();
        actions.sendKeys(text);
        actions.build().perform();
        logger.info(key + " elementine " + text + " değeri focus ile yazıldı.");
    }

    @Step({"Refresh page",
            "Sayfayı yenile"})
    public void refreshPage() {
        logger.info("Entered.");
        baseSteps.refreshPage();
    }

    @Step("Switch to new window")
    public void switchNewWindow() {
        logger.info("Entered.");
        baseSteps.switchNewWindow();
    }

    @Step({"Change page zoom to <value>%",
            "Sayfanın zoom değerini değiştir <value>%"})
    public void chromeZoomOut(String value) {
        logger.info("Entered. Parameters; value: {}", value);
        baseSteps.chromeZoomOut(value);
    }

    @Step({"Open new tab",
            "Yeni sekme aç"})
    public void chromeOpenNewTab() {
        logger.info("Entered");
        baseSteps.chromeOpenNewTab();
    }

    @Step({"Focus on tab number <number>",
            "<number> numaralı sekmeye odaklan"})//Starting from 1
    public void chromeFocusTabWithNumber(int number) {
        logger.info("Entered. Parameters; number: {}", number);
        baseSteps.chromeFocusTabWithNumber(number);
    }

    @Step("popupa gec")
    public void switchTo() {
        logger.info("Entered");
        baseSteps.switchTo();
    }

    @Step({"Focus on last tab",
            "Son sekmeye odaklan"})
    public void chromeFocusLastTab() {
        logger.info("Entered");
        baseSteps.chromeFocusLastTab();
    }

    @Step({"Focus on frame with <key> in <pageName>",
            "Frame'e odaklan <key> in <pageName>"})
    public void chromeFocusFrameWithNumber(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        baseSteps.chromeFocusFrameWithWebElement(key, pageName);
    }

    @Step("Return frame to default")
    public void switchToDefault() {
        logger.info("Entered.");
        baseSteps.switchDefaultContent();
    }


    @Step({"Accept Chrome alert popup",
            "Chrome uyarı popup'ını kabul et"})
    public void acceptChromeAlertPopup() {
        logger.info("Entered");
        baseSteps.acceptChromeAlertPopup();
    }


    @Step({"<key> alanına kaydır in <pageName>", "Scroll to the <key> area in the <pageName>"})
    public void scrollToElement(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        baseSteps.scrollToElementToBeVisible(key, pageName);
        logger.info(key + " elementinin olduğu alana kaydırıldı");

    }


    @Step({"<key> alanına js ile kaydır in <pageName>", "Scroll to the element <key> by js in <pageName>"})
    public void scrollToElementWithJs(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        WebElement element = baseSteps.findElement(key, pageName);
        if (!baseSteps.isDisplayed(element)) {
            baseSteps.waitForTheElement(key, pageName);
        }
        baseSteps.scrollByJs(element);
        baseSteps.waitForPageToCompleteState();
    }

    @Step({"Scroll into Key <key> by js in the Page Name <pageName>"})
    public void scrollIntoKeyByJs(String key, String pagaName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pagaName);
        baseSteps.scrollIntoKeyByJs(baseSteps.findElement(key, pagaName));
    }

    @Step({"<key> li elementi bul, temizle ve rasgele  email değerini yaz in <pageName>",
            "Find element by <key> clear the field and send a random email in <pageName>"})
    public void randomMail(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        Long timestamp = baseSteps.getTimestamp();
        WebElement webElement = baseSteps.findElement(key, pageName);
        webElement.clear();
        webElement.sendKeys("testotomasyon" + timestamp + "@sahabt.com");

    }

    @Step({"Select a text <text> on the combobox using the <key> in the <pageName>"})
    public void chooseATextFromTheList(String text, String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        List<WebElement> comboBoxElement = baseSteps.findElements(key, pageName);
        if (!baseSteps.isDisplayed(baseSteps.findElement(key, pageName))) {
            baseSteps.waitForTheElement(key, pageName);
        }
        if (comboBoxElement.isEmpty()) {
            throw new RuntimeException("Couldn't find the element");
        }
        boolean result = false;
        for (int i = 0; i < comboBoxElement.size(); i++) {
            String texts = comboBoxElement.get(i).getText();
            if (texts.contains(text)) {
                baseSteps.clickElement(comboBoxElement.get(i));
                result = true;
                break;
            }
        }
        assertTrue(result, "Could not find the text");
        logger.info(key + " comboboxından " + text + " değeri seçildi");
    }

    @Step({"Write a text <text> on the <searchFieldKey> and select the option using the <key> in the <pageName>"})
    public void writeATextToTheFieldAndSelectTheText(String text, String searchFieldKey, String key, String pageName) {
        logger.info("Entered. Parameters; text: {}, searchFieldKey: {}, key: {}, pageName: {}", text, searchFieldKey, key, pageName);
        baseSteps.findElement(searchFieldKey, pageName).click();
        baseSteps.findElements(searchFieldKey, pageName).clear();
        baseSteps.findElement(searchFieldKey, pageName).sendKeys(text);
        waitBySeconds(5);
        List<WebElement> comboBoxElement = baseSteps.findElements(key, pageName);
        if (!(baseSteps.isDisplayed(comboBoxElement.get(0)))) {
            baseSteps.waitForTheElement(comboBoxElement.get(0));
        }
        if (comboBoxElement.size() != 0) {
            String savedText = comboBoxElement.get(0).getText();
            baseSteps.clickElement(comboBoxElement.get(0));
            logger.info(key + " comboboxından " + savedText + " değeri seçildi");
            waitBySeconds(5);
        }
    }

    @Step({"<key> olarak comboboxdan bir değer seçilir in <pageName>",
            "Select a random value on the combobox from the <key> in the <pageName>"})
    public void comboBoxRandom(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        List<WebElement> comboBoxElement = baseSteps.findElements(key, pageName);
        System.out.println(comboBoxElement.size());
        int randomIndex = new Random().nextInt(comboBoxElement.size() - 1);
        if (!baseSteps.isDisplayed(baseSteps.findElement(key, pageName))) {
            baseSteps.waitForTheElement(key, pageName);
        }
        SavedValue.getInstance().putValue(SavedValue.DROPDOWN_TEXT, comboBoxElement.get(randomIndex).getText());
        logger.info(SavedValue.getInstance().getSavedValue(SavedValue.DROPDOWN_TEXT) + " has selected as random value");
        System.out.println(SavedValue.getInstance().getSavedValue(SavedValue.DROPDOWN_TEXT));
        baseSteps.clickByJs(comboBoxElement.get(randomIndex));
        logger.info(key + " comboboxından herhangi bir değer seçildi");
        baseSteps.waitForPageToCompleteState();
    }

    @Step({"Select a specific index <indexNo> on the combobox from the <key> in the <pageName>"})
    public void comboBoxWithSpecificIndexSelection(int index, String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        List<WebElement> comboBoxElement = baseSteps.findElements(key, pageName);
        int listSize = comboBoxElement.size();
        logger.info("Size of the list: " + listSize);
        if (!baseSteps.isDisplayed(baseSteps.findElement(key, pageName))) {
            baseSteps.waitForTheElement(key, pageName);
        }
        if (listSize > index - 1) {
            SavedValue.getInstance().putValue(SavedValue.DROPDOWN_TEXT, comboBoxElement.get(index - 1).getText());
            String savedText = SavedValue.getInstance().getSavedValue(SavedValue.DROPDOWN_TEXT).toString();
            logger.info(savedText + " has selected according to the index");
            System.out.println(SavedValue.getInstance().getSavedValue(SavedValue.DROPDOWN_TEXT));
            baseSteps.clickByJs(comboBoxElement.get(index - 1));
            logger.info("The " + savedText + " has selected on the " + key + " Combo box.");
            baseSteps.waitForPageToCompleteState();
        } else {
            Assert.fail("Size of list was less than the selected index. Selected was: " + index + " Actual was: " + listSize);
        }
    }

    @Step({"<key> elementine javascript ile tıkla in <pageName>",
            "Clicks on the <key> element by javascript in <pageName>"})
    public void clickToElementWithJavaScript(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        WebElement element = baseSteps.findElement(key, pageName);
        if (!baseSteps.isDisplayed(element)) {
            baseSteps.waitForTheElement(key, pageName);
        }
        baseSteps.javaScriptClicker(element);
        logger.info(key + " elementine javascript ile tıklandı");
        baseSteps.waitForPageToCompleteState();
    }

    @Step({"Eğer mevcutsa <key> elementine javascript ile tıkla in <pageName>",
            "If element exists clicks on the <key> by javascript in <pageName>"})
    public void ifExistsClickOnElementWithJavaScript(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        if (!baseSteps.ifElementExistsClickByJS(Page.getPage(pageName).getBy(key))) {
            logger.info(key + " is not available on the " + pageName + ". That's why the part has skipped");
        }
    }

    @Step("<key> alanını javascript ile temizle in <pageName>")
    public void clearWithJS(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        WebElement element = baseSteps.findElement(key, pageName);
        baseSteps.clearByJs(element);
    }

    @Step({"<listKey> menu listesinden rasgele seç in <pageName>",
            "Random selection from the list <listKey>  in <pageName>"})
    public void chooseRandomElementFromList(String listKey, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", listKey, pageName);
        for (int i = 0; i < 3; i++)
            baseSteps.randomPick(listKey, pageName);
    }

    @Step("Select <key> element at <index> index in <pageName>")
    public void selectElementAtIndex(String key, String index, String pageName) {
        logger.info("Entered. Parameters: key={}, index={}, pageName={}", key, index, pageName);
        try {
            int x = Integer.parseInt(index) - 1;
            Optional.ofNullable(baseSteps.findElements(key, pageName))
                    .map(List::stream)
                    .flatMap(stream -> stream.skip(x).findFirst())
                    .ifPresentOrElse(baseSteps::javaScriptClicker,
                            () -> logger.error("Index is out of bounds: {}", x));
        } catch (NumberFormatException e) {
            logger.error("Invalid index value: {}", index);
        }
    }

    @Step("Check <key> elements and <index> index contains <expectedText> in  <pageName>")
    public void choosingIndexFromDemandNo(String key, String index, String expactedText, String pageName) {
        logger.info("Entered. Parameters; key: {}, index: {},expectedText {}, pageName: {}", key, index, expactedText, pageName);
        List<WebElement> elements = baseSteps.findElements(key, pageName);
        WebElement element = elements.get(Integer.parseInt(index) - 1);
        Assert.assertEquals("Texts are not same. Expected text was " + expactedText + " but it was " + element.getText(), expactedText, element.getText());

    }

    @Step("<text> textini <key> elemente tek tek yaz in <pageName>")
    public void sendKeyOneByOne(String text, String key, String pageName) throws InterruptedException {
        logger.info("Entered. Parameters; text: {}, key: {}, pageName: {}", text, key, pageName);
        WebElement field = baseSteps.findElement(key, pageName);
        field.clear();
        if (!key.equals("")) {
            for (char ch : text.toCharArray())
                baseSteps.findElement(key, pageName).sendKeys(Character.toString(ch));
            Thread.sleep(10);
            logger.info(key + " elementine " + text + " texti karakterler tek tek girlilerek yazıldı.");
        }
    }

    @Step("<key> elementine <text> değerini js ile yaz in <pageName>")
    public void writeToKeyWithJavaScript(String key, String text, String pageName) {
        logger.info("Entered. Parameters; key: {}, text: {} pageName: {}", key, text, pageName);
        WebElement element = baseSteps.findElement(key, pageName);
        baseSteps.writeToElementByJs(element, text);
        logger.info(key + " elementine " + text + " değeri js ile yazıldı.");
    }

    @Step({"Select <text> in the <key> at the <pageName>"})
    public void selectDropDown(String text, String key, String pageName) {
        logger.info("Entered. Parameters; text: {}, key: {}, pageName: {}", text, key, pageName);
        if (!baseSteps.isDisplayed(baseSteps.findElement(key, pageName))) {
            baseSteps.waitForTheElement(key, pageName);
        }
        Select select = new Select(baseSteps.findElement(key, pageName));
        select.selectByVisibleText(text);
    }

    @Step({"Select index <index> in the <key> at the <pageName>"})
    public void selectDropDown(int index, String key, String pageName) {
        logger.info("Entered. Parameters; index: {}, key: {}, pageName: {}", index, key, pageName);
        Select select = new Select(baseSteps.findElement(key, pageName));
        select.selectByIndex(index);
    }

    @Step({"Select random element <key> list in the <pageName>"})
    public void randomPick(String key, String pageName) {
        logger.info("Entered. Parameters; key: {}, pageName: {}", key, pageName);
        List<WebElement> elements = baseSteps.findElements(key, pageName);
        Random random = new Random();
        int index = random.nextInt(elements.size());
        elements.get(index).click();
        logger.info("clicked " + key + " element index is :" + index);
    }

    @Step({"Select random element <key> list <times> times in the <pageName>"})
    public void randomPickTimes(String key, int times, String pageName) {
        logger.info("Entered. Parameters; key: {}, times: {}, pageName: {}", key, times, pageName);
        for (int i = 0; i < times; i++) {
            List<WebElement> elements = baseSteps.findElements(key, pageName);
            Random random = new Random();
            int index = random.nextInt(elements.size());
            elements.get(index).click();
        }
    }


    @Step({"User save the element's text <key> to slot <saveSlot> at <pageName>"})
    public void saveTextToSlot(String key, String saveSlot, String pageName) {
        logger.info("Entered. Parameters; key: {}, saveSlot: {}, pageName: {}", key, saveSlot, pageName);
        String elementText = "";
        elementText = baseSteps.findElement(key, pageName).getText();
        if (elementText.trim().equals("")) {
            elementText = baseSteps.findElement(key, pageName).getAttribute("value");
        }
        SavedValue.getInstance().putValue(saveSlot, elementText);
        logger.info(key + " text: " + elementText + " is saved into the " + saveSlot + " at the " + pageName);
        waitBySeconds(1);
    }

    @Step({"User save the element's text with trim <key> to slot <saveSlot> at <pageName>"})
    public void saveTextToSlotWithTrim(String key, String saveSlot, String pageName) {
        logger.info("Entered. Parameters; key: {}, saveSlot: {}, pageName: {}", key, saveSlot, pageName);
        String elementText = "";
        elementText = baseSteps.findElement(key, pageName).getText().trim();
        if (elementText.trim().equals("")) {
            elementText = baseSteps.findElement(key, pageName).getAttribute("value");
        }
        SavedValue.getInstance().putValue(saveSlot, elementText);
        logger.info(key + " text: " + elementText + " is saved into the " + saveSlot + " at the " + pageName);
        waitBySeconds(1);
    }

    @Step({"User concats the two saved key <savedKey> <savedKeySecond> to and save the slot <saveSlot>"})
    public void saveTextToSlotWithConcat(String savedKey, String savedKeySecond, String saveSlot) {
        logger.info("Entered. Parameters; savedKey: {}, savedKeySecond: {} saveSlot: {}", savedKey, savedKeySecond, saveSlot);
        String savedTextFirst = SavedValue.getInstance().getSavedValue(String.class, savedKey);
        String savedTextSecond = SavedValue.getInstance().getSavedValue(String.class, savedKeySecond);
        String unifiedText = savedTextFirst.trim().concat(savedTextSecond.trim()).trim();
        SavedValue.getInstance().putValue(saveSlot, unifiedText);
        logger.info("Parameters; savedTextFirst: {}, savedTextSecond: {} unifiedText: {}", savedTextFirst, savedTextSecond, unifiedText);
    }


    @Step({"User should check that <key> element's text and <savedKey> saved text are same at <pageName>"})
    public void compareTexts(String key, String savedKey, String pageName) {
        logger.info("Entered. Parameters; key: {}, savedKey: {}, pageName: {}", key, savedKey, pageName);
        logger.info("Entered");
        String savedText = SavedValue.getInstance().getSavedValue(savedKey).toString();
        String elementText = baseSteps.findElement(key, pageName).getText();
        logger.info(savedText + " and " + elementText + " have compared. They have to equal to each other.");
        Assert.assertEquals("Texts are not same. Expected text was " + savedText + " but it was " + elementText, savedText, elementText);
    }

    @Step({"User should check that <key> element's text and <savedKey> saved text are not same at <pageName>"})
    public void compareTextsNotSame(String key, String savedKey, String pageName) {
        logger.info("Entered. Parameters; key: {}, savedKey: {}, pageName: {}", key, savedKey, pageName);
        String savedText = SavedValue.getInstance().getSavedValue(savedKey).toString();
        String elementText = baseSteps.findElement(key, pageName).getText();
        logger.info(savedText + " and " + elementText + " have compared. They have to different from each other.");
        Assert.assertNotEquals("Texts are same and they were " + savedText, savedText, elementText);
    }

    @Step({"User writes the saved text <savedKey> to the element <key> at <pageName>"})
    public void writeTextToTheKey(String savedKey, String key, String pageName) {
        logger.info("Entered. Parameters; savedKey: {}, key: {}, pageName: {}", savedKey, key, pageName);
        String savedText = SavedValue.getInstance().getSavedValue(String.class, savedKey);
        sendKeys(savedText, key, pageName);
        logger.info("Text: " + savedText + " wrote into the " + key + " at the " + pageName);
    }

    @Step({"User writes by js the saved text <savedKey> to the element <key> at <pageName>"})
    public void writeTextToTheKeyByJs(String savedKey, String key, String pageName) {
        logger.info("Entered. Parameters; savedKey: {}, key: {}, pageName: {}", savedKey, key, pageName);
        String savedText = SavedValue.getInstance().getSavedValue(String.class, savedKey);
        sendKeys(savedText, key, pageName);
        logger.info("Text: " + savedText + " wrote into the " + key + " at the " + pageName);
    }

    @Step({"User writes <firstSevedKey> firstSavedKey and <secondSavedKey> secondSavedKey summed text to the element <key> at <pageName>"})
    public void writeCollectedText(String firstSavedKey, String secondSavedKey, String key, String pageName) {
        logger.info("Entered. Parameters; firstSavedKey: {}, secondSavedKey: {}, key: {}, pageName: {}", firstSavedKey, secondSavedKey, key, pageName);
        String firstSavedText = SavedValue.getInstance().getSavedValue(String.class, firstSavedKey);
        String secondSavedText = SavedValue.getInstance().getSavedValue(String.class, secondSavedKey);
        sendKeys(firstSavedText + secondSavedText, key, pageName);
        logger.info("Text: " + firstSavedText + secondSavedText + " wrote into the " + key + " at the " + pageName);
    }

    @Step({"User should return to the previous page"})
    public void returnToThePreviousPage() {
        logger.info("Entered.");
        //JavascriptExecutor js = (JavascriptExecutor) driver;
        baseSteps.navigateBack();
        logger.info("It has returned to the previous page or actions");
        //js.executeScript("window.history.go(-1); return false;");
    }

    @Step({"Get records on the network from the current url"})
    public void getRecordsOnTheNetworkFromTheUrl() {
        logger.info("Entered.");
        baseSteps.getRecordsOnTheNetworkFromTheUrl();
    }

    @Step({"Validating of the text <text> and check the saved text <savedKey> equal to each other"})
    public void checkTheTextIfItIsEqualsToTheSavedValue(String text, String savedKey) {
        logger.info("Entered. Parameters; text: {}, savedKey: {}", text, savedKey);
        String savedText = SavedValue.getInstance().getSavedValue(String.class, savedKey);
        Assert.assertEquals("Texts are not same. Expected text was " + text + " but it was " + savedText, text, savedText);
        logger.info("Texts are compared." + " Expected text : " + text +
                " Result : " + savedText);
    }

    @Step({"The saved key <savedKey> should not visible on the the current page"})
    public void savedTextShouldNotVisibleOnThePage(String savedKey) {
        String savedText = SavedValue.getInstance().getSavedValue(String.class, savedKey);
        logger.info("Entered. Parameters; savedKey: {} , savedText: {}", savedKey, savedText);
        assertFalse(baseSteps.shouldSeeText(savedText), "Beklenen element görünmektedir.");
    }

    @Step({"The saved key <savedKey> should visible on the the current page"})
    public void savedTextShouldVisibleOnThePage(String savedKey) {
        String savedText = SavedValue.getInstance().getSavedValue(String.class, savedKey);
        logger.info("Entered. Parameters; savedKey: {} , savedText: {}", savedKey, savedText);
        assertTrue(baseSteps.shouldSeeText(savedText), "Beklenen element görünmemektedir.");
    }

    @Step({"The saved key should visible on the the current page with table <table>"})
    public void savedTextShouldVisibleOnThePage(Table table) {
        logger.info("Entered. Parameters; table: {}", table.toString());
        if (table.getColumnNames().contains("Saved Key")) {
            for (int i = 0; i < table.getTableRows().size(); i++) {
                String savedKey = table.getColumnValues("Saved Key").get(i);
                String savedText = SavedValue.getInstance().getSavedValue(String.class, savedKey);
                logger.info("Entered. Parameters; savedKey: {} , savedText {}", savedKey, savedText);
                assertTrue(baseSteps.shouldSeeText(savedText), "Expected element is not visible: " + savedText);
                logger.info("The element visible on the current page, savedKey: {} , savedText {}", savedKey, savedText);
            }
        }
    }

    @Step({"Check if current page contains the text <text> or not"})
    public void whetherCurrentPageContainsTheTextOrNot(String text) {
        logger.info("Entered. Parameters; text: {}", text);
        assertTrue(baseSteps.shouldSeeContainingText(text), "Expected element is not visible: " + text);
        logger.info("The element visible on the current page , text {}", text);
    }

    @Step({"Check if current page contains the saved key <savedKey> or not"})
    public void whetherCurrentPageContainsTheSavedTextOrNot(String savedKey) {
        String savedText = SavedValue.getInstance().getSavedValue(String.class, savedKey);
        logger.info("Entered. Parameters; saved key: {} , saved text: {}", savedKey, savedText);
        assertTrue(baseSteps.shouldSeeContainingText(savedText), "Expected element is not visible: " + savedText);
        logger.info("The element visible on the current page saved key: {}, saved text: {}", savedKey, savedText);
    }

    @Step({"Check if current page contains the saved key or not on the table <table>"})
    public void whetherCurrentPageContainsTheSavedTextOrNotOnTheTable(Table table) {
        logger.info("Entered. Parameters; table: \n{}", table.toString());
        if (table.getColumnNames().contains("Saved Key")) {
            for (int i = 0; i < table.getTableRows().size(); i++) {
                String savedKey = table.getColumnValues("Saved Key").get(i);
                String savedText = SavedValue.getInstance().getSavedValue(String.class, savedKey);
                logger.info("Entered. Parameters; savedKey: {} , savedText {}", savedKey, savedText);
                assertTrue(baseSteps.shouldSeeContainingText(savedText), "Expected element is not visible: " + savedText);
                logger.info("The element visible on the current page, savedKey: {} , savedText {}", savedKey, savedText);
            }
        }
    }

    @Step({"Check if current page contains the text or not on the table <table>"})
    public void whetherCurrentPageContainsTheTextOrNotOnTheTable(Table table) {
        logger.info("Entered. Parameters; table: \n{}", table.toString());
        if (table.getColumnNames().contains("Text")) {
            for (int i = 0; i < table.getTableRows().size(); i++) {
                String text = table.getColumnValues("Text").get(i);
                logger.info("Entered. Parameters; text: {}", text);
                assertTrue(baseSteps.shouldSeeContainingText(text), "Expected element is not visible: " + text);
                logger.info("The element visible on the current page, text: {}", text);
            }
        }
    }

    @Step({"The keys should visible on the the current page with table <table>"})
    public void keyShouldVisibleOnThePage(Table table) {
        logger.info("Entered. Parameters; table: \n{}", table.toString());
        if (table.getColumnNames().contains("Key") && table.getColumnNames().contains("Page Name")) {
            for (int i = 0; i < table.getTableRows().size(); i++) {
                String key = table.getColumnValues("Key").get(i);
                String pageName = table.getColumnValues("Page Name").get(i);
                logger.info("Entered. Parameters; Key: {} Page: {}", key, pageName);
                boolean isElementExist = baseSteps.isDisplayed(baseSteps.findElement(key, pageName));
                Assert.assertTrue("The key: " + key + " has not displayed on the " + pageName, isElementExist);
                logger.info("The key is visible. Parameter; Key: {} Page: {} ", key, pageName);
            }
        }
    }

    @Step({"Delete all caches"})
    public void deleteAllCaches() {
        baseSteps.clearBrowserCache();
        logger.info("All caches have deleted");
    }

    @Step({"switch to the new tab"})
    public void closeWindowAndOpenNewWindow() {
        baseSteps.closeTheLastTabAndSwitchToNewWindow();
        logger.info("Current window has closed and switched to the new new window");
    }

    @Step({"User save the element's <key> attribute <attribute> to slot <saveSlot> at <pageName>"})
    public void saveAttributeValueToSlot(String key, String attributeName, String saveSlot, String pageName) {
        logger.info("Entered. Parameters; key: {}, attributeName: {} saveSlot: {}, pageName: {}", key, attributeName, saveSlot, pageName);
        String attributeValue = "";
        attributeValue = baseSteps.findElement(key, pageName).getAttribute(attributeName);
        if (attributeValue.trim().equals("")) {
            logger.info("AttributeName: {} didn't have any value. It was null", attributeName);
        }
        SavedValue.getInstance().putValue(saveSlot, attributeValue);
        logger.info(key + " text: " + attributeValue + " is saved into the " + saveSlot + " at the " + pageName);
    }

    @Step({"Check if current URL contains the saved value <savedKey>"})
    public void checkIfURLContainsSavedValue(String savedKey) {
        logger.info("Entered. Parameters; expectedURL: {}", savedKey);
        waitBySeconds(10);
        String actualURL = baseSteps.getCurrentUrl();
        String expectedDomain = SavedValue.getInstance().getSavedValue(String.class, savedKey).toUpperCase(new Locale("tr", "TR"));
        String[] actualUrlArray = actualURL.split("\\.");
        String actualDomain = actualUrlArray[1].toUpperCase(new Locale("tr", "TR"));
        Assert.assertEquals("Expected domain: " + expectedDomain + "didn't equal to actual domain: " + actualDomain, expectedDomain, actualDomain);
        logger.info("Parameters: expectedDomain: {}, actualDomain: {}", expectedDomain, actualDomain);
    }

    @Step({"Check if current URL contains the saved value <savedKey> with expected text <text>"})
    public void checkIfURLContainsSavedValue(String savedKey, String text) {
        logger.info("Entered. Parameters; expectedURL: {}", savedKey);
        waitBySeconds(10);
        String actualURL = baseSteps.getCurrentUrl();
        String expectedDomain = SavedValue.getInstance().getSavedValue(String.class, savedKey).toUpperCase(new Locale("tr", "TR"));
        String[] actualUrlArray = actualURL.split("\\.");
        String actualDomain = actualUrlArray[1].toUpperCase(new Locale("tr", "TR"));
        Assert.assertEquals("Expected domain: " + expectedDomain + "didn't equal to actual domain: " + actualDomain, expectedDomain, actualDomain);
        logger.info("Parameters: expectedDomain: {}, actualDomain: {}", expectedDomain, actualDomain);
    }

    @Step("Click on the element equal <expectedText> text in the <key> element list in the <pageName>")
    public void clickElementIfEqualText(String expectedText, String key, String pageName) {
        logger.info("Entered. Parameters; expectedText: {}, key: {}, pageName: {}", expectedText, key, pageName);
        baseSteps.clickElement(baseSteps.returnElementIfEqualText(expectedText, Page.getPage(pageName).getBy(key)));
    }

    @Step("Click on the element equal saved <expectedSavedKey> text in the <key> element list in the <pageName>")
    public void clickElementIfEqualSavedText(String expectedSavedKey, String key, String pageName) {
        logger.info("Entered. Parameters; expectedSavedKey: {}, key: {}, pageName: {}", expectedSavedKey, key, pageName);
        String savedText = SavedValue.getInstance().getSavedValue(String.class, expectedSavedKey);
        baseSteps.clickElement(baseSteps.returnElementIfEqualText(savedText, Page.getPage(pageName).getBy(key)));
    }

    @Step("Get element's <getValues> digits from its string and paste it <writableTextBox> in <pageName>")
    public void getDigitsAndPasteIt(String getValues, String writableTextBox, String pageName) {
        String text = baseSteps.getElementAttributeValue((Page.getPage(pageName).getBy(getValues)), "for");
        baseSteps.extractDigitsFromString(text);
        sendKeys(text, writableTextBox, pageName);
    }

    @Step("Get remoteAdress <url>")
    public void getRemoteAddress(String url) throws UnknownHostException {
        baseSteps.getClientIP(baseSteps.getCurrentUrl());
    }

    @Step("Click if the element <key> on the page is clickable <pageName>")
    public void elementClickable(String key, String pageName) {
        baseSteps.clickIfClickable(key, pageName);
    }

    @Step("Click on your element <key> periodically with a delay of <timeToWait> seconds <numberOfClicks> times <pageName>")
    public void clickAtIntervals(String key, int timeToWait, int numberOfClicks, String pageName) {
        logger.info("Entered. Parameters; key{}, timeToWait{}, pageName{}, numberOfClicks{} :", key, timeToWait, pageName, numberOfClicks);
        IntStream.range(0, numberOfClicks).forEach(i -> {
            clickElement(key, pageName);
            if (i < numberOfClicks - 1) {
                waitBySeconds(timeToWait);
            }
        });
    }

}