package com.trendyol.stepdefinition;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.trendyol.step.BaseSteps;
import com.trendyol.step.LoginSteps;
import com.trendyol.utils.lists.ClassList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginStepDef extends CommonStepDefinitions{

   private final LoginSteps loginSteps = ClassList.getInstance().get(LoginSteps.class);
   private final Logger logger = LoggerFactory.getLogger(BaseSteps.class);

    @Step({"User Login <table>"})
    public void login(Table table) throws Exception {
        logger.info("Entered.");
        loginSteps.login(table);
    }

}
