package org.stockexchange.util;

import java.util.regex.Pattern;

/**
 * Utility functions
 */
public class Utils {

    public static int randomQuantity(int maxQuantity){
        return (int) Math.round(Math.random() * maxQuantity + 1);
    }

    /**
     * Function used for printing the AllStockInfoReport to console
     * @param s
     */
    public static void print(String s){
        System.out.println(s);
    }

}
