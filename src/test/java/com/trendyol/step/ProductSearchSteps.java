package com.trendyol.step;

import com.thoughtworks.gauge.Table;
import com.trendyol.page.CommonElements;
import com.trendyol.page.ProductSearchPage;
import com.trendyol.utils.TrendyolExceptions.UndefinedProductIndexException;
import com.trendyol.utils.saving.SavedValue;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductSearchSteps extends BaseSteps{

    String productToBeSearched = null;
    int productIndex;
    public void searchAProduct(Table table) throws Exception {
        productToBeSearched = getColumnValues(table, "Product", 0);
        sendKeys(ProductSearchPage.PRODUCT_SEARCH_INPUT_BOX, productToBeSearched);
        clickElement(ProductSearchPage.PRODUCT_SEARCH_ICON);
    }

    public void selectAProduct(Table table) throws UndefinedProductIndexException {
        String productIndex = table.getColumnValues("Product Index").get(0);

        if(isRandom(productIndex)){
            randomPick(ProductSearchPage.PRODUCT_LIST);
        }

        else if (isNumeric(productIndex)){
            int index = Integer.parseInt(productIndex); // Assuming that 0 < index < productList.size
            clickElement(ProductSearchPage.PRODUCT_LIST_BY_INDEX.get(index));
        }
        else  throw new UndefinedProductIndexException(productIndex + " is undefined value.");
    }

    @Override
    public void randomPick(By by) {
        logger.info("Entered. Parameters; key: {}", by);
        scrollToElementToBeVisible(CommonElements.POPULAR_BRANDS_WRAPPER);
        refreshPage();
        List<WebElement> elements = findElements(by);
        int index = random.nextInt(elements.size());
        hoverElement(elements.get(index));
        saveProductPrice(index);
        elements.get(index).click();
        switchNewWindow();
    }

    public void saveProductPrice(int index){
        String productPrice = findElement(ProductSearchPage.PRODUCT_PRICE_BY_INDEX.get(index+1)).getText();
        String priceAsNumeric = String.valueOf(extractDigitsAndDots(productPrice));
        SavedValue.getInstance().putValue( SavedValue.PRODUCT_PRICE_ON_SEARCH_PAGE, priceAsNumeric);
    }

}
