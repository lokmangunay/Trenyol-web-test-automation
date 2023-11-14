package com.trendyol.utils.TrendyolExceptions;

import org.openqa.selenium.TimeoutException;

public class UndefinedProductIndexException extends Exception {
    public UndefinedProductIndexException(String message) {
        super(message);
    }
}
