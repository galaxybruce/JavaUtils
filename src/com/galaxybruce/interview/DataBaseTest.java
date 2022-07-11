package com.galaxybruce.interview;

/**
 * 进制转换
 */
public class DataBaseTest {

    public static void main(String[] args) {
        // N进制转为十进制
        // Integer.parseInt("AA", 8)

//        System.out.println(value2BaseN(10, 16));
//        System.out.println(value2BaseN(33, 16));
        System.out.println(value2BaseN(1700, 16));
    }

    /**
     * 十进制转N进制
     * @param value
     * @param base
     * @return
     */
    public static String value2BaseN(int value, int base) {
        final String baseValue = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz@-";
        if (value < base) {
            return String.valueOf(baseValue.charAt(value));
        } else {
            String a = String.valueOf(baseValue.charAt(value % base));
            return value2BaseN(value / base, base).concat(a);
        }
    }
}
