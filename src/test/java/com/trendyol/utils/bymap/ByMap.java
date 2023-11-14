package com.trendyol.utils.bymap;

import com.trendyol.page.Page;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;


public class ByMap extends HashMap<String, Function<String[], By>> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private final String pageName;
    private static final int DESIRED_MODIFIER_FIELD = Modifier.FINAL | Modifier.STATIC | Modifier.PUBLIC;

    public ByMap(Class<? extends Page> page, String pageName) {
        this.pageName = pageName;
        try {
            for (Class<?> c = page; c != Object.class; c = c.getSuperclass()) {
                construction(c);
                for (Class<?> inner : c.getDeclaredClasses()) {
                    construction(inner);
                }
                for (Class<?> inner : c.getInterfaces()) {
                    construction(inner);
                }
            }
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(pageName + " sayfasında hata var." + exception.getMessage());
        }
    }

    private void construction(Class<?> c) throws IllegalAccessException {
        for (Field field : c.getDeclaredFields()) {
            if (field.getType().equals(By.class) && ((field.getModifiers() & DESIRED_MODIFIER_FIELD) == DESIRED_MODIFIER_FIELD)) {
                put(field.getName(), (s) -> {
                    try {
                        return (By) field.get(null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Error happened");
                    }
                });
            }
            if (field.isAnnotationPresent(ByKey.class) && ((field.getModifiers() & DESIRED_MODIFIER_FIELD) == DESIRED_MODIFIER_FIELD) && field.isAnnotationPresent(ByKey.class)) {
                put(field.getAnnotation(ByKey.class).value(), (Function<String[], By>) field.get(null));
            }
        }
    }

    public By get(String value) {
        List<String> s = Arrays.asList(value.split("'"));
        String[] inputs = new String[s.size() / 2];
        for (int i = 0; i < s.size() / 2; i++) {
            inputs[i] = s.get(i * 2 + 1);
        }
        String key = transform(value);
        if (!containsKey(key)) {
            throw new RuntimeException("Not found " + key + " in keys at " + pageName);
        }
        By by = super.get(key).apply(inputs);
        logger.info("Value: {} ,By : {}, Page Name: {}", value, by.toString(), pageName);
        return by;
    }

    @Override
    public Function<String[], By> put(String key, Function<String[], By> value) {
        return super.put(transform(key), value);
    }

    private String transform(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] pieces = string.split("'");
        for (int i = 0; i < pieces.length; i += 2) {
            stringBuilder.append(i % 4 == 0 ? "" : "''").append(pieces[i]);
        }
        return stringBuilder.toString().replaceAll("[ _]", "").toUpperCase();
    }
}