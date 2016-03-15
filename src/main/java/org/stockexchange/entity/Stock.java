package org.stockexchange.entity;

import org.stockexchange.util.Currency;

import java.io.Serializable;

/**
 * Stock implementation
 */

public class Stock implements Serializable {

    private String symbol;
    private Currency parValue;
    private Currency lastDividend;
    private Currency fixedDividend;
    private StockType type; // COMMON, PREFERRED

    public Stock(){

    }

    public Stock(String symbol, Currency parValue, Currency lastDividend, StockType type, Currency fixedDividend) {
        this.symbol = symbol;
        this.parValue = parValue;
        this.lastDividend = lastDividend;
        this.type = type;
        this.fixedDividend = fixedDividend;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Currency getParValue() {
        return parValue;
    }

    public void setParValue(Currency parValue) {
        this.parValue = parValue;
    }

    public Currency getLastDividend() {
        return lastDividend;
    }

    public void setLastDividend(Currency lastDividend) {
        this.lastDividend = lastDividend;
    }

    public Currency getFixedDividend() {
        return fixedDividend;
    }

    public void setFixedDividend(Currency fixedDividend) {
        this.fixedDividend = fixedDividend;
    }

    public StockType getType() {
        return type;
    }

    public void setType(StockType type) {
        this.type = type;
    }

    /**
     * Calculate dividend Yield
     * @param tickerPrice
     * @return the dividend Yield
     */
    public double calculateDividendYield(Currency tickerPrice){
        if (type==StockType.COMMON)
            return lastDividend.divide(tickerPrice);
        else { // PREFERRED
            return (parValue.multiply(fixedDividend)).divide(tickerPrice);
        }
    }


    /**
     * Calculate PE Ratio
     * @param tickerPrice
     * @return the PE ratio
     */
    public double calculatePERatio(Currency tickerPrice){
        return tickerPrice.divide(lastDividend);
    }


    /**
     * Comparison function for Stock objects
     * @param that
     * @return
     */
    public final boolean equals(Stock that){
        if(that != null){
            return this.symbol.equals(that.symbol)
                    && this.type.equals(that.type)
                    && this.parValue.equals(that.parValue);
        } else {
            return false;
        }

    }


    /**
     * String representation of class members
     * @return
     */

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", parValue=" + parValue +
                ", lastDividend=" + lastDividend +
                ", fixedDividend=" + fixedDividend +
                ", type=" + type.toString() +
                '}';
    }

}
