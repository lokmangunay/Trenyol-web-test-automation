package com.trendyol.page;

import org.openqa.selenium.By;

public class BasketPage extends Page {

    public static final String PAGE_NAME = "Basket Page";

    public static final By PRODUCT_PRICE_IN_BASKET = By.className("pb-basket-item-price");
    public static final By REMOVE_PRODUCTS = By.className("i-trash");
    public static final By TOOL_TIP_CONTEXT = By.xpath("//button[text()='Anladım']");
    public static final By INCREASE_PRODUCT = By.xpath("//*[@class='ty-numeric-counter-button']//*[@height='9']");
    public static final By PRODUCT_COUNT = By.xpath("//*[@class='counter-content']");
    public static final By PRODUCTS = By.className("pb-merchant-group");


    @Override
    public String getPageName() {
        return PAGE_NAME;
    }

}
