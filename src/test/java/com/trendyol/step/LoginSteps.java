package com.trendyol.step;

import com.thoughtworks.gauge.Table;
import com.trendyol.Drivers.LocalDriver;
import com.trendyol.page.HomePage;
import com.trendyol.page.LoginPage;
import com.trendyol.utils.saving.SavedValue;
import lombok.extern.java.Log;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class LoginSteps extends BaseSteps{

    public String email;
    public String password;

    public enum LoginOptions {
        SAVED_VALUE, PARAMETRIC, MANUAL
    }

    public void setParametricLoginCredentials(Table table) throws Exception {
        logger.info("Entered. ");
        String loginParameter = getColumnValues(table, "Email", 0);
        email = LocalDriver.ConfigUsers.getString(loginParameter + "_UserEmail").trim();
        password = LocalDriver.ConfigUsers.getString(loginParameter + "_Password").trim();
    }


    public void setLoginCredentials(Table table) throws Exception {
        logger.info("Entered. ");
        LoginOptions loginOptions = LoginOptions.valueOf(getColumnValues(table, "Parameter", 0).toUpperCase(Locale.ROOT));

        switch (loginOptions) {
            case SAVED_VALUE: //values that are already saved during runtime.
                email = SavedValue.getInstance().getSavedValue(String.class, SavedValue.CURRENT_EMAIL);
                password = SavedValue.getInstance().getSavedValue(String.class, SavedValue.CURRENT_PASSWORD);
                break;

            case PARAMETRIC:  //Values for specific cases
                setParametricLoginCredentials(table);
                break;

            case MANUAL: // Manual Login Credentials from table
           //     setManualLoginCredentials(table);
                break;

            default:
                throw new Exception("Invalid Login Parameter Argument: " + loginOptions);
        }
    }

    public void login(Table table) throws Exception {
        logger.info("Entered. parameters:{} \n ", table);
        waitForTheElement(HomePage.LOGIN_BUTTON);
        hoverOnTheElementAndClickBy(HomePage.LOGIN_BUTTON);
        isLoginPageLoaded();
        setLoginCredentials(table);
        fillLoginForm(email, password);
        checkLoginStatus(table);
  //      Assert.assertTrue("Login Failed. BolBol Member name is not visible.",
    //            isElementInvisible(elementWithText("GİRİŞ YAP")) || isElementVisible(elementWithText("Yolcu ve İletişim Bilgileri")));
    }

    public void fillLoginForm(String phoneNumber, String password) throws Exception {
        logger.info("Entered. ");
        fillEmail(email);
        fillPassword(password);
        clickElement(LoginPage.LOGIN_BUTTON);
    }

    private void isLoginPageLoaded() {
        Assert.assertTrue("Failed to load Login Page.", getCurrentUrl().contains("https://www.trendyol.com/giris"));
        Assert.assertTrue(areElementsDisplayed(
                LoginPage.LOGIN_BUTTON,
                LoginPage.EMAIL_INPUT,
                LoginPage.PASSWORD_INPUT
        ));
    }

    private void fillEmail(String email){
        sendKeys(LoginPage.EMAIL_INPUT, email);
        logger.info("\nWritten Email: " + email);
    }
    private void fillPassword(String password){
        sendKeys(LoginPage.PASSWORD_INPUT, password);
        logger.info("\nWritten Password: " + password);
    }

    private void checkLoginStatus(Table table) throws Exception {
        LOGIN_STATUS loginStatus = parseLoginStatus(table);

        switch (loginStatus) {
            case UNSUCCESSFUL_LOGIN:
                assertUnsuccessfulLogin();
                break;

            case SUCCESSFUL_LOGIN:
                // Placeholder for any future handling of successful login
                break;
        }
    }

    private LOGIN_STATUS parseLoginStatus(Table table) throws Exception {
        String loginStatusString = getColumnValues(table, "Expected Login Status", 0).toUpperCase(Locale.ROOT);
        return LOGIN_STATUS.valueOf(loginStatusString);
    }

    private void assertUnsuccessfulLogin() {
        Assert.assertTrue("Invalid credentials are sent. Login is expected to fail.", isDisplayed(LoginPage.UNSUCCESSFUL_LOGIN_MESSAGE));
        logger.info("Login is failed as expected. Login Credentials:  email {} , password {}", email, password);
    }

    public enum LOGIN_STATUS {
        UNSUCCESSFUL_LOGIN, SUCCESSFUL_LOGIN
    }



}
