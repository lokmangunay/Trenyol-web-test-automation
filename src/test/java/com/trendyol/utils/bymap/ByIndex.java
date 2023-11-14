package com.trendyol.utils.bymap;

import com.trendyol.Drivers.LocalDriver;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public class ByIndex implements Function<String[], By> {
    public final static HashMap<String, Integer> TEXT_TO_INDEX = new HashMap<>();
    private final static Random RANDOM = new Random();

    static {
        TEXT_TO_INDEX.put("LAST", -1);
        TEXT_TO_INDEX.put("ANY", 0);
        TEXT_TO_INDEX.put("FIRST", 1);
        TEXT_TO_INDEX.put("SECOND", 2);
        TEXT_TO_INDEX.put("THIRD", 3);
        TEXT_TO_INDEX.put("FOURTH", 4);
        TEXT_TO_INDEX.put("FIFTH", 5);
    }

    private static int convertToInteger(String number) {
        if (!number.endsWith(".")) {
            throw new RuntimeException("Invalid number");
        }
        String s = number.substring(0, number.length() - 1);
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new RuntimeException("Invalid number" + s);
            }
        }
        return Integer.parseInt(s);
    }

    private String startWith;
    private String endWith = "";

    public ByIndex(String startWith, String endWith) {
        this.startWith = startWith;
        this.endWith = endWith;
    }

    public ByIndex(String startWith) {
        this.startWith = startWith;
    }

    public By get(int index) {
        if (false) {
            int maxCapacity = LocalDriver.getDriver().findElements(By.xpath(startWith + endWith)).size();
            if (index > maxCapacity) {
                throw new RuntimeException("Invalid number. Max capacity: " + maxCapacity + " Expected: " + index);
            }
        }
        if (index == 0)
            return By.xpath(startWith + endWith);
        else if (index > 0)
            return By.xpath(startWith + "[" + index + "]" + endWith);
        else
            return By.xpath(startWith + "[(last() - " + (index + 1) + ")]" + endWith);
    }

    @Override
    public By apply(String[] strings) {
        System.out.println(strings[0]);
        if (strings[0].equalsIgnoreCase("Random")) {
            int max = LocalDriver.getDriver().findElements(get(0)).size();
            int index = RANDOM.nextInt(max) + 1;
            return get(index);
        } else if (TEXT_TO_INDEX.keySet().stream().anyMatch(s -> s.equalsIgnoreCase(strings[0]))) {
            for (Map.Entry<String, Integer> entry : TEXT_TO_INDEX.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(strings[0])) {
                    return get(entry.getValue());
                }
            }
            throw new RuntimeException("");
        } else {
            return get(convertToInteger(strings[0]));
        }
    }
}