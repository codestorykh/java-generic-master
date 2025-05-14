package com.codestorykh.generics;

import java.util.ArrayList;
import java.util.List;

public class TypeSafetyExample {

    public static void main(String[] args) {
        // Without Generics
        List numbers = new ArrayList();
        numbers.add(1);
        numbers.add("2");
        int num = (Integer) numbers.get(0);
        System.out.println(num);
        String text = (String) numbers.get(1);
        System.out.println(text);
        int cashNum = (Integer) numbers.get(1);  // Runtime: ClassCastException

        // With Generics
        List<Integer> numbersWithGenerics = new ArrayList<>();
        numbersWithGenerics.add(1);
        //numbersWithGenerics.add("2");  // Compile Error! Type safety enforced
        System.out.println(numbersWithGenerics);
    }

}
