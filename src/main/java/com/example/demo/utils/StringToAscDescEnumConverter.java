package com.example.demo.utils;

import com.example.demo.enums.AscDescEnum;
import org.springframework.core.convert.converter.Converter;

public class StringToAscDescEnumConverter implements Converter<String, AscDescEnum> {

    @Override
    public AscDescEnum convert(String s) {
        return AscDescEnum.valueOf(s.toUpperCase());
    }
}
