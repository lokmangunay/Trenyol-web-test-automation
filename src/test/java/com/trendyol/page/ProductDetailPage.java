package com.trendyol.page;

import com.trendyol.utils.bymap.ByIndex;
import org.openqa.selenium.By;

public class ProductDetailPage extends Page {

    public static final String PAGE_NAME = "Product Detail Page";

    public static final By ADD_TO_CART_BUTTON = By.className("add-to-basket-button-text");


    @Override
    public String getPageName() {
        return PAGE_NAME;
    }

}
