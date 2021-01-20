package com.adc.da.budget.utils;

import com.adc.da.util.utils.StringUtils;
import org.dozer.DozerConverter;


public class StringToDoubleConverterUtil extends DozerConverter<String, Double> {

    public StringToDoubleConverterUtil() {
        super(String.class, Double.class);
    }

    @Override
    public String convertFrom(Double source, String destination) {
        if(null == source){
            return null;
        }
        return String.valueOf(source);
    }

    @Override
    public Double convertTo(String source, Double destination) {
        if(StringUtils.isEmpty(source)){
            return Double.valueOf(0);
        }
        return Double.parseDouble(source);
    }

}
