package com.trendyol.utils.saving;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavedValue {
    private static SavedValue initiate;

    public static SavedValue getInstance() {
        if (initiate == null) initiate = new SavedValue();
        return initiate;
    }

    private SavedValue() {
    }

    private final HashMap<String, Object> saveSlot = new HashMap<>();

    public void putValue(String key, Object value) {
        saveSlot.put(key, value);
    }

    public <T> T getSavedValue(Class<T> tClass, String key) {
        Object o = getSavedValue(key);
        if (!tClass.isInstance(o))
            Assert.fail("Can not casted " + o.getClass().getSimpleName() + " to " + tClass.getSimpleName());
        return (T) o;

    }

    public void printAllKeys() {
        for (Map.Entry<String, Object> entry : saveSlot.entrySet()) {
            System.out.println("Entry" + entry.getValue() + "," + entry.getKey());
        }
    }

    public <G> List<G> getSavedListValue(Class<G> gClass, String key) {
        if (!saveSlot.containsKey(key)) {
            saveSlot.put(key, new ArrayList<G>());
        }
        Object o = saveSlot.get(key);
        try {
            return (List<G>) o;
        } catch (ClassCastException e) {
            throw new AssertionError("Can not casted " + o.getClass().getSimpleName() + " to List");
        }
    }

    public Object getSavedValue(String key) {
        if (!saveSlot.containsKey(key))
            Assert.fail("Expected key hasn't got value. Key: " + key);
        return saveSlot.get(key);
    }

    public void clear() {
        saveSlot.clear();
    }

    public static final String CURRENT_EMAIL = "current_email";
    public static final String CURRENT_PASSWORD = "current_password";
    public static final String DROPDOWN_TEXT = "dropdown_text";
    public static final String PRODUCT_PRICE_ON_SEARCH_PAGE = "product_price_on_search_page";
    public static final String PRODUCT_PRICE_ON_BASKET_PAGE = "product_price_on_basket_page";



}
