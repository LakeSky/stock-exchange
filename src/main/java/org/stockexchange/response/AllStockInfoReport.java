package org.stockexchange.response;

import org.stockexchange.entity.Stock;
import org.stockexchange.entity.StockInfo;
import org.stockexchange.entity.Ticker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class AllStockInfoReport implements Serializable {
    private String status;   // Response status "Success" or error code
    private String message;  // Response message - used to provide details in error cases

    private double globalIndex;

    private Map<String, Ticker> tickers;
    //private List<String> symbols = null;
    //private List<StockInfo> stockInfoList = null;

    public Map<String, Ticker> getTickers() {
        return tickers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getGlobalIndex() {
        return globalIndex;
    }

    //public List<String> getSymbols() {
    //    return symbols;
    //}

    public void setGlobalIndex(double globalIndex) {
        this.globalIndex = globalIndex;
    }

    //public void setSymbols(List<String> symbols) {
    //    this.symbols = symbols;
    //}

    //public List<StockInfo> getStockInfoList() {
    //    return stockInfoList;
    //}

    //public void setStockInfoList(List<StockInfo> stockInfoList) {
    //    this.stockInfoList = stockInfoList;
    //}

    /**
     * Default constructor
     */
    public AllStockInfoReport() {
    }

    public AllStockInfoReport(String status, String message, double globalIndex, Map<String, Ticker> tickers) {
        this.status = status;
        this.message = message;
        this.globalIndex = globalIndex;
        this.tickers = tickers;
    }

    public String stockInfoListReport(){
        String ret = "\r\n--------------------------------------------------------------------------------------";

        ret = ret + "\r\nINDEX: " + String.format("%10.4f", globalIndex);

        ret = ret + "\r\n--------------------------------------------------------------------------------------";
        ret = ret + "\r\nStock                Price    Quantity         Volume(Mil)     Div.yield      PE Ratio";
        ret = ret + "\r\n--------------------------------------------------------------------------------------";

        // iterate map and fill stockInfoList and symbols
        for (Map.Entry<String, Ticker> entry : tickers.entrySet()){
            String symbol = entry.getKey();
            Ticker ticker = (Ticker)entry.getValue();
            StockInfo stockInfo = ticker.getStockInfo();
            ret = ret + "\r\n" + stockInfo.toReport(symbol);
            ret = ret + String.format("  %12.4f", ticker.getDividendYield());
            ret = ret + String.format("  %12.4f", ticker.getPERatio());
        }

        return ret;
    }

    @Override
    public String toString() {
        return "AllStockInfoReport{" +
                "globalIndex=" + globalIndex +
                ", stockInfoList=" + stockInfoListReport() +
                '}';
    }
}
