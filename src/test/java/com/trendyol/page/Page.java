package com.trendyol.page;


import com.trendyol.utils.OsCheck;
import com.trendyol.utils.bymap.ByKey;
import com.trendyol.utils.bymap.ByMap;
import com.trendyol.utils.bymap.ByText;
import org.openqa.selenium.By;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public abstract class Page {
    public ByMap byMap;

    @ByKey("Any element whose text is '*'")
    public static final ByText GET_FROM_TEXT = new ByText("");

    protected Page() {
        byMap = new ByMap(this.getClass(), getPageName());
    }

    public abstract String getPageName();

    public By getBy(String key) {
        return byMap.get(key);
    }

    public static List<Page> pages;

    public static Page getPage(String string) {
        if (pages == null)
            pages = setPages();
        String modified = string.replaceAll("[ _]", "").toUpperCase();
        for (Page page : pages) {
            if (modified.equals(page.getPageName().replaceAll("[ _]", "").toUpperCase()))
                return page;
        }
        throw new RuntimeException("Not found given page name: [" + string + "]");
    }

    public static List<Page> setPages() {
        try {
            List<Page> pages = new ArrayList<>();
            List<String> classNames = new ArrayList<>();
            File page;
            OsCheck.OSType osType = OsCheck.getOperatingSystemType();
            System.out.println(osType);
            if (osType.name().equals("Windows")) {
                page = new File(new URI(System.getProperty("user.dir").replace("\\", "/") + "/src/test/java/com/trendyol/page").getPath());
            } else {
                page = new File(new URI(System.getProperty("user.dir") + "/src/test/java/com/trendyol/page").getPath());
            }

            getDirectories(classNames, "com.trendyol.page", page);
            for (String name : classNames) {
                Class<?> c = ClassLoader.getSystemClassLoader().loadClass(name);
                if (c.getSuperclass() == Page.class) {
                    pages.add(((Class<? extends Page>) c).getDeclaredConstructor().newInstance());
                }
            }
            return pages;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    static void getDirectories(List<String> classes, String path, File file) {
        if (file.isDirectory()) {
            for (File fileEach : file.listFiles()) {
                getDirectories(classes, path + "." + fileEach.getName(), fileEach);
            }
        } else if (file.getName().endsWith(".java")) {
            classes.add(path.substring(0, path.lastIndexOf('.')));
        }
    }
}

