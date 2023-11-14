package com.trendyol.utils.bymap;

import com.trendyol.utils.saving.SavedValue;
import org.openqa.selenium.By;

import java.util.function.Function;

public class ByText implements Function<String[], By> {
    String startWith;
    String endWith;

    public ByText(String startWith, String endWith) {
        this.startWith = startWith;
        this.endWith = endWith;
    }

    public ByText(String endWith) {
        this("//*",endWith);
    }

    public By get(String text) {
        if (text.isEmpty())
            return By.xpath(startWith + endWith);
        if (text.startsWith("₺"))
            return By.xpath("//*" + "[@title='" + text.substring(1) + "']");
        if (text.startsWith("$"))
            text = SavedValue.getInstance().getSavedValue(String.class, text.substring(1));
        if (text.startsWith("#"))
            return By.xpath(startWith + ("[normalize-space()='" + text.substring(1) + "']") + endWith);
        return By.xpath(startWith + ("[text()='" + text + "']") + endWith);
    }

    @Override
    public By apply(String[] strings) {
        return get(strings[0]);
    }
}

