package com.trendyol.utils.bymap;

import com.trendyol.Drivers.LocalDriver;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public class ByMultipleIndex implements Function<String[], By> {
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

    private String[] startWith;

    public ByMultipleIndex(String... strings) {
        this.startWith = strings;
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

    public By get(int... index) {
        StringBuilder builder = new StringBuilder();
        builder.append(startWith[0]);
        for (int i = 0; i < index.length - 1; i++) {
            if (index[i] == 0)
                builder.append(startWith[i + 1]);
            else if (index[i] > 0)
                builder.append("[" + index[i] + "]" + startWith[i]);
            else
                builder.append("[(last() - " + (index[i] + 1) + ")]" + startWith[i]);
        }
        return By.xpath(builder.toString());

    }

    @Override
    public By apply(String[] strings) {
        System.out.println(strings[0]);
        int[] result = new int[strings.length];
        for (int i = 0; i < result.length; i++) {
            if (strings[i].equalsIgnoreCase("Random")) {
                int max = LocalDriver.getDriver().findElements(get(result)).size();
                int index = RANDOM.nextInt(max) + 1;
                result[i] = index;
            } else {
                int finalI = i;
                if (TEXT_TO_INDEX.keySet().stream().anyMatch(s -> s.equalsIgnoreCase(strings[finalI]))) {
                    for (Map.Entry<String, Integer> entry : TEXT_TO_INDEX.entrySet()) {
                        if (entry.getKey().equalsIgnoreCase(strings[i])) {
                            result[i] = entry.getValue();
                        }
                    }
                } else {
                    result[i] = convertToInteger(strings[0]);
                }
            }
        }
        return get(result);

    }
}
