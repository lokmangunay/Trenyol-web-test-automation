package com.trendyol.page;

import org.openqa.selenium.By;

public class CommonElements extends Page {

    public static final String PAGE_NAME = "Common Elements";

    public static final By ACCEPT_COOKIES = By.xpath("//div[@id='onetrust-button-group']//button[text()='KABUL ET']");
    public static final By CLOSE_GENDER_POP_UP = By.xpath("//div[@class='modal-close' or @title='Kapat']");
    public static final By POPULAR_BRANDS_WRAPPER = By.className("popularBrands__wrapper");




    @Override
    public String getPageName() {
        return PAGE_NAME;
    }

}
