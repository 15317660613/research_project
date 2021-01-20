package com.adc.da.budget.utils;
import org.dozer.DozerConverter;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToDateConverterUtil extends DozerConverter<String, Date> {

    public StringToDateConverterUtil() {
        super(String.class, Date.class);
    }

    @Override
    public String convertFrom(Date source, String destination) {
        if (null == source){
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(source);
    }

    @Override
    public Date convertTo(String source, Date destination) {
        if (null == source){
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(source, pos);
    }

}
