package com.vetri.poc.java8.interfaces.functional;

import org.springframework.core.convert.converter.Converter;

public class ConverterExample {
    public static void main(String [] args){
        Converter<String,Integer> converter = from -> Integer.valueOf(from);
        Converter<String,Integer> converterMethodReference = Integer::valueOf;

        System.out.println(converter.convert("10"));
        System.out.println(converterMethodReference.convert("10"));
    }
}
