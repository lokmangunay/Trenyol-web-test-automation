package com.trendyol.utils.logger;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class CustomLayout extends PatternLayout {
    @Override
    public String format(LoggingEvent event) {
        return super.format(event).replace("var1",getPrefix());
    }

    public String getPrefix(){
        StringBuilder builder=new StringBuilder();
        for (int i = 27 ;i<Thread.currentThread().getStackTrace().length;i++)
            builder.append("——");
        return builder.toString();
    }
}
