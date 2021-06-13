package com.vetri.poc.java8.foreach;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ForEachExample {
    public static void main(String [] args){
        List<Integer> integerList = IntStream.rangeClosed(1,5).boxed().collect(Collectors.toList());
        System.out.println(integerList);

        List<Integer> integerList1 = Stream.of(1,2).collect(Collectors.toList());
        System.out.println(integerList1);

        integerList.forEach(i->System.out.println(i));

        integerList.forEach(System.out::println);
    }
}
