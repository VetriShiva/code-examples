package com.vetri.poc.java8.interfaces.functional;

@FunctionalInterface
public interface Converter<F,T> {
    T convert(F from);
}
