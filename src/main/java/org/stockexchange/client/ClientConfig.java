package org.stockexchange.client;


/**
 * Configuration parameters used for client application development
 */

public class ClientConfig {

    public static String baseurl = "http://localhost:8080";

    public static int sleepMilliseconds = 100;    // sleep interval between trades series

    public static double sellProbability = 0.499;  // "buyProbability" will be "1-sellProbability"

    public static int maxTradeQuantity = 1000;    // max quantity of shares in a simulated trade

    public final static double marginLimit = 0.05; // price margin for a trade

}
