package com.trendyol.page;

import com.trendyol.utils.bymap.ByIndex;
import org.openqa.selenium.By;

public class ProductSearchPage extends Page {

    public static final String PAGE_NAME = "Product Search Page";

    public static final By PRODUCT_SEARCH_INPUT_BOX = By.xpath("//input[@data-testid='suggestion']");
    public static final By PRODUCT_SEARCH_ICON = By.xpath("//*[@data-testid='search-icon']");
    public static final By PRODUCT_LIST = By.xpath("//*[@class='p-card-wrppr with-campaign-view']");


    public static final ByIndex PRODUCT_LIST_BY_INDEX = new ByIndex("(//*[@class='p-card-wrppr with-campaign-view'])");
    public static final ByIndex PRODUCT_PRICE_BY_INDEX = new ByIndex("(//div[@class='prc-box-dscntd'])");




    @Override
    public String getPageName() {
        return PAGE_NAME;
    }

}
