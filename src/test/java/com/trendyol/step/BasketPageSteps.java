package com.trendyol.step;

import com.trendyol.page.BasketPage;
import com.trendyol.utils.TrendyolExceptions.ProductOutOfStockException;
import com.trendyol.utils.saving.SavedValue;
import org.junit.Assert;

public class BasketPageSteps extends BaseSteps{

    int productCountBefore;
    int productCountAfter;

    public void saveProductPrice(){
        String productPrice = findElement(BasketPage.PRODUCT_PRICE_IN_BASKET).getText();
        String priceAsNumeric = String.valueOf(extractDigitsAndDots(productPrice));
        SavedValue.getInstance().putValue(SavedValue.PRODUCT_PRICE_ON_BASKET_PAGE, priceAsNumeric);
    }

    public void comparePrices() {
        saveProductPrice();
        String expectedPrice = (String) SavedValue.getInstance().getSavedValue(SavedValue.PRODUCT_PRICE_ON_SEARCH_PAGE);
        String actualPrice  = (String) SavedValue.getInstance().getSavedValue(SavedValue.PRODUCT_PRICE_ON_BASKET_PAGE);
        Assert.assertEquals("Prices are not equal! ",expectedPrice , actualPrice);
    }

   public void emptyBasket() {
            clickElement(BasketPage.REMOVE_PRODUCTS);
            waitForPageToCompleteState();
        logger.info("All products are removed from the basket.");
    }

    public void checkIfBasketIsEmpty(){
         isDisplayed(BasketPage.PRODUCTS);
    }

    public void increaseProductCount() throws ProductOutOfStockException {
            productCountBefore = getProductCount();
            clickIfElementExist(BasketPage.INCREASE_PRODUCT);
            checkIfNumberIncreased();
      /*  try{
            productCountBefore = getProductCount();
            clickElement(BasketPage.INCREASE_PRODUCT);
            checkIfNumberIncreased();
        }
        catch (Exception productOutOfStockException){
            throw new ProductOutOfStockException("There is not enough product to add.");
        }    */
    }

    private void checkIfNumberIncreased() {
        productCountAfter = getProductCount();
        int comparisonResult = Integer.compare(productCountBefore, productCountAfter);

        if (comparisonResult < 0) {
            logger.info("The number has increased.");

        } else if (comparisonResult == 0) {
            logger.info("The number remains the same.");

        } else {
            logger.info("The number has decreased.");

        }
    }

    public int getProductCount(){
      return  Integer.parseInt(getElementAttributeValue(BasketPage.PRODUCT_COUNT, "value"));
    }
}
