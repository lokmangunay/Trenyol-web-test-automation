package com.trendyol.page;

import org.openqa.selenium.By;

public class HomePage extends Page {
    public static final String PAGE_NAME = "Home Page";

    public static final By TRENDYOL_LOGO = By.xpath("//*[@id='logo' and @title='trendyol']");
    public static final By NAVIGATION_WRAPPER = By.id("navigation-wrapper");

    public static final By LOGIN_BUTTON = By.xpath("//div[@id='account-navigation-container']//p[@class='link-text' and text()='Giriş Yap']");
    public static final By MY_BASKET_BUTTON = By.xpath("//*[@class='link-text' and text()='Sepetim']");



    @Override
    public String getPageName() {
        return PAGE_NAME;
    }
}
