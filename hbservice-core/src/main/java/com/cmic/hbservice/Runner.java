package com.cmic.hbservice;

import java.math.BigDecimal;

public class Runner {
    public static void main(String[] args) {
        BigDecimal decimal1 = new BigDecimal(1);
        BigDecimal decimal2 = new BigDecimal(2);
        System.out.println(decimal1.compareTo(decimal1));
        System.out.println(decimal1.compareTo(decimal2));
        System.out.println(decimal2.compareTo(decimal1));
    }
}
