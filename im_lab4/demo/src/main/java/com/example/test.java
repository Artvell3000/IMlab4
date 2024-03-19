package com.example;

import java.util.HashMap;
import java.util.Map;

import javafx.util.Pair;

public class test {
    static Pair<Integer, Integer> p1 = new Pair(1,1);
    static Pair<Integer, Integer> p2 = new Pair(1,1);



    
    public static void main(String[] args) {
        Map<Pair<Integer, Integer>, Boolean> m = new HashMap<>();
        m.put(p1, true);
        System.out.println(m.containsKey(new Pair(1,1)));
    }
}
