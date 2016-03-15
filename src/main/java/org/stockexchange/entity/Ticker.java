package org.stockexchange.entity;

import org.stockexchange.util.Currency;

/**
 * Ticker implementation
 */
public class Ticker {

    private Stock stock;
    private StockInfo stockInfo;

    public Ticker(Stock stock, StockInfo stockInfo) {
        this.stock = stock;
        this.stockInfo = stockInfo;
    }

    public Ticker() {
        stock = new Stock();
        stockInfo = new StockInfo();
    }

    public Stock getStock() {
        return stock;
    }

    public Currency getPrice() {
        return stockInfo.getPrice();
    }

    public Currency getVolume() { return stockInfo.getVolume(); }

    public int getQuantity() {
        return stockInfo.getQuantity();
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public void setStockInfo(StockInfo stockInfo) {
        this.stockInfo = stockInfo;
    }

    public StockInfo getStockInfo() {
        return stockInfo;
    }

    @Override
    public String toString() {
        return "Ticker{" +
                "stock=" + stock.toString() +
                ", stockInfo=" + stockInfo.toString() +
                '}';
    }

}
