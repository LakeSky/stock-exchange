package org.stockexchange.entity;


import org.stockexchange.util.Currency;

/**
 * StockInfo implementation
 */

public class StockInfo {

    public Currency price;
    public int quantity;
    public Currency volume;

    public StockInfo(Currency price, int quantity, Currency volume){
        this.price = price;
        this.quantity = quantity;
        this.volume = volume;
    }

    public void setPrice(Currency price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setVolume(Currency volume) {
        this.volume = volume;
    }

    public Currency getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Currency getVolume() {
        return volume;
    }

    public StockInfo(){
        this(Currency.UNDEFINED,0, Currency.UNDEFINED);
    }

    @Override
    public String toString() {
        return "StockInfo {" +
                "price=" + price +
                ", quantity=" + quantity +
                ", volume=" + volume +
                '}';
    }

    public String toReport(String symbol){
        return String.format("%6s  %18s  %10d  %18s",
                symbol, price.toString(), quantity, volume.divide(1000000).toString());
    }

}
