package org.stockexchange.client;

/**
 * Client utility functions
 */


public class ClientUtils {

    public static void print(String msg){
        System.out.println(msg);
    }

    public static int randomQuantity(int maxQuantity){
        return (int) Math.round(Math.random() * maxQuantity + 1);
    }
}
