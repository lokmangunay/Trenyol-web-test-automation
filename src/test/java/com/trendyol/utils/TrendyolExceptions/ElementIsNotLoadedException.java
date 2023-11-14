package com.trendyol.utils.TrendyolExceptions;

import org.openqa.selenium.TimeoutException;

public class ElementIsNotLoadedException extends TimeoutException {
    public ElementIsNotLoadedException(String message) {
        super(message);
    }
}
