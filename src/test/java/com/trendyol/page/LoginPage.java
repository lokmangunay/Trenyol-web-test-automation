package com.trendyol.page;

import org.openqa.selenium.By;

public class LoginPage extends Page {
    public static final String PAGE_NAME = "Login Page";

    public static final By LOGIN_BUTTON = By.xpath("//button[@type='submit']//*[text()='Giriş Yap']");
    public static final By EMAIL_INPUT = By.xpath("//input[@data-testid='email-input']");
    public static final By PASSWORD_INPUT = By.xpath("//input[@data-testid='password-input']");
    public static final By UNSUCCESSFUL_LOGIN_MESSAGE = By.xpath("//div[@id='error-box-wrapper']//*[text()='E-posta adresiniz ve/veya şifreniz hatalı.']");


    @Override
    public String getPageName() {
        return PAGE_NAME;
    }

}
