package com.trendyol.stepdefinition;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.trendyol.page.ProductSearchPage;
import com.trendyol.step.BaseSteps;
import com.trendyol.step.LoginSteps;
import com.trendyol.step.ProductSearchSteps;
import com.trendyol.utils.lists.ClassList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductSearchStepDef extends CommonStepDefinitions{

   private final ProductSearchSteps productSearchSteps = ClassList.getInstance().get(ProductSearchSteps.class);
   private final Logger logger = LoggerFactory.getLogger(BaseSteps.class);

    @Step({"User Search a product <table>"})
    public void searchAProduct(Table table) throws Exception {
        logger.info("Entered.");
        productSearchSteps.searchAProduct(table);
    }


    @Step({"User Select a product in Product Search Page <table>"})
    public void selectAProduct(Table table) throws Exception {
        logger.info("Entered.");
        productSearchSteps.selectAProduct(table);
    }



}
