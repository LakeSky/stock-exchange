package org.stockexchange.util;

/**
 * Static configuration information
 */

public class Config {

    // TODO obtain configuration from properties || xml || yml

    public final static String listingFileName = "listing.txt";

    public final static int priceCalcIntervalMinutes = 15;

    public final static int simulateTradeIntervalMilliseconds = 200;

    public final static int tickIntervalMilliseconds = 1000;

    public final static int reportIntervalMilliseconds = 5000;

    public final static double sellProbability = 0.4999;  // "buyProbability" will be "1-sellProbability"

    public final static int maxTradeQuantity = 500;    // max quantity of shares in a simulated trade

    public final static double marginLimit = 0.05; // price margin for a trade

}

