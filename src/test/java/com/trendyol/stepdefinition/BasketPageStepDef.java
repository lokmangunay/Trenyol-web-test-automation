package com.trendyol.stepdefinition;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.trendyol.page.BasketPage;
import com.trendyol.step.BaseSteps;
import com.trendyol.step.BasketPageSteps;
import com.trendyol.step.ProductSearchSteps;
import com.trendyol.utils.TrendyolExceptions.ProductOutOfStockException;
import com.trendyol.utils.lists.ClassList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasketPageStepDef extends CommonStepDefinitions{

   private final BasketPageSteps basketPageSteps = ClassList.getInstance().get(BasketPageSteps.class);
   private final Logger logger = LoggerFactory.getLogger(BaseSteps.class);

    @Step({"User Compare Prices in Product Search Page vs Basket Page"})
    public void comparePrices()  {
        logger.info("Entered.");
        basketPageSteps.comparePrices();
    }


    @Step({"User Empty basket and checks if basket is empty"})
    public void emptyBasket() {
        logger.info("Entered.");
        basketPageSteps.emptyBasket();
    }

    @Step({"User Try to increase product count in basket page and check if it is increased"})
    public void increaseProductCount() throws ProductOutOfStockException {
        logger.info("Entered.");
        basketPageSteps.increaseProductCount();
    }



}
