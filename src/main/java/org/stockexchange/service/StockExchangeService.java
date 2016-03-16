package org.stockexchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.stockexchange.entity.Trade;
import org.stockexchange.entity.TradeType;
import org.stockexchange.process.StockExchange;
import org.stockexchange.entity.Stock;
import org.stockexchange.entity.Ticker;
import org.stockexchange.response.AllStockInfoReport;
import org.stockexchange.response.ListingResponse;
import org.stockexchange.response.TradeResponse;
import org.stockexchange.response.WatchResponse;
import org.stockexchange.util.Currency;
import org.stockexchange.process.Processor;

import java.math.BigDecimal;

import java.time.Instant;

/**
 * Stock Exchange web service implementation
 */

@RestController
@EnableAutoConfiguration
public class StockExchangeService {

    @Autowired
    private ApplicationContext ctx;

    /**
     * Sends the listing as ListingReponse object (JSON)
     * @return
     */
    @RequestMapping(value = "/listing", method = RequestMethod.GET)
    public ListingResponse listing() {
        StockExchange stx = (StockExchange) ctx.getBean("stockExchange");
        return new ListingResponse("Success", "", stx.getListing());
    }


    /**
     * Sends the report as AllStockInfoReport object (JSON)
     * @return
     */
    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public AllStockInfoReport allStockInfoReport() {
        StockExchange stx = (StockExchange) ctx.getBean("stockExchange");
        AllStockInfoReport report = new AllStockInfoReport(
                "Success", "", stx.getGlobalIndex(), stx.getTickers());
        return report;
    }


    /**
        Returns the ticker information for the given symbol
        @return
    */
    @RequestMapping(value = "/watch", method = RequestMethod.GET)
    public WatchResponse watch(
            @RequestParam(value = "symbol", defaultValue = "") String symbol
    ){
        StockExchange stx = (StockExchange) ctx.getBean("stockExchange");
        Ticker ticker = stx.getTicker(symbol);

        if (ticker!=null)
            return new WatchResponse("Success","", ticker);
        else
            return new WatchResponse("Error","Ticker not found for symbol: " + symbol);
    }

    /**
     * performs a trade with provided call parameters
     * Creates a Trade object and invokes the appropriate processor
     * @param tradeTypeStr
     * @param symbol
     * @param quantity
     * @param price
     * @return
     */
    @RequestMapping(value = "/trade", method = RequestMethod.GET)
    public TradeResponse trade(
            @RequestParam(value = "tradeType", defaultValue = "") String tradeTypeStr,
            @RequestParam(value = "symbol", defaultValue = "") String symbol,
            @RequestParam(value = "amount", defaultValue = "0") int quantity,
            @RequestParam(value = "price", defaultValue = "0") BigDecimal price
    ) {

        TradeType tradeType;
        Instant instantNow = Instant.now();

        if (tradeTypeStr.compareTo("sell") == 0) {
            tradeType = TradeType.SELL;
        } else if (tradeTypeStr.compareTo("buy") == 0) {
            tradeType = TradeType.BUY;
        } else {
            return new TradeResponse("Error", "Invalid tradeType: must be 'buy' or 'sell'");
        }

        StockExchange stx = (StockExchange) ctx.getBean("stockExchange");
        Stock stock = stx.getStockBySymbol(symbol);

        Trade trade = new Trade(Instant.now(), tradeType, stock, quantity, new Currency(price));

        // pass trade to currwent stock's processor
        Processor processor = stx.getProcessor(symbol);
        processor.processTrade(trade);

        return new TradeResponse("Success", "parameters:" +tradeType+",  "+symbol + ",  "+ quantity + ",  " + price
         + "   " + stock.getSymbol() + " " + trade.toString()
        + "   " + processor.getStock().toString());
    }

    /**
     * htmlListing : test html output <table> ... </table>
     * @return
     */

    @RequestMapping(value = "/htmlListing", method = RequestMethod.GET)
    public String htmlListing() {
        StockExchange stx = (StockExchange) ctx.getBean("stockExchange");

        String ret = "<html><body>";

        ret += "<table border='1' width='80%'>";

        ret += "<tr><td>Symbol</td>"
                + "<td>PAR</td>"
                + "<td>Last Dividend</td>"
                + "<td>Fixed Dividend</td>"
                + "</tr>";

        for (Stock stock: stx.getListing()) {
            //ret += "<p>" + stock.toString() + "<p>";

            ret += "<tr><td>" + stock.getSymbol()  + "</td>"
                    + "<td>" + stock.getParValue() + "</td>"
                    + "<td>" + stock.getLastDividend() + "</td>"
                    + "<td>" + stock.getFixedDividend() + "</td>"
                    + "</tr>";
        }

        ret += "</table>";
        ret += "</body></html>";

        return ret;
    }

}
