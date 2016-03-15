package org.stockexchange.entity;

import org.stockexchange.util.Currency;

import java.time.Instant;
import java.util.Comparator;

/**
 * Trade class implementation
 */

public class Trade {

    public Instant timestamp;
    public TradeType type;
    public Stock stock;
    public int quantity;
    public Currency price;

    public Trade (Instant timestamp, TradeType type, Stock stock, int quantity, Currency price){
        this.timestamp = timestamp;
        this.type = type;
        this.stock = stock;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String toString(){
        return type.toString() + "(stock=" + stock
                + ", quantity=" + quantity
                + ", price=" + price
                + ", time=" + timestamp+")";
    }

    public static final Comparator<Trade> byTime = new Comparator<Trade>(){
        public int compare(Trade trade1, Trade trade2){
            return trade1.timestamp.compareTo(trade2.timestamp);
        }
    };

}
