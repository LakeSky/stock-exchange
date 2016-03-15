package org.stockexchange.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.stockexchange.entity.*;
import org.stockexchange.response.AllStockInfoReport;
import org.stockexchange.util.Config;
import org.stockexchange.util.Currency;
import org.stockexchange.util.Utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Stock Exchange implementation
 */

@Component
public class StockExchange {
    private static final Logger log = LoggerFactory.getLogger(StockExchange.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    // the stock listing
    private List<Stock> listing;

    // the tickers map will use the stock symbols as key (String)
    private Map<String, Ticker> tickers = new HashMap<>();

    // the processors map will use the stock symbols as key (String)
    private Map<String, Processor> processors = new HashMap<>();

    private double globalIndex;
    private Duration priceCalcInterval = Duration.ofMinutes(Config.priceCalcIntervalMinutes);

    public List<Stock> getListing() {
        return listing;
    }

    public Map<String, Ticker> getTickers() {
        return tickers;
    }

    public double getGlobalIndex() {
        return globalIndex;
    }

    public Ticker getTicker(String symbol) {
        Ticker ticker = null;
        if (tickers.containsKey(symbol)) {
            ticker = tickers.get(symbol);
        }
        return ticker;
    }

    public void addTicker(String symbol, Ticker ticker) {
        tickers.put(symbol, ticker);
    }

    public Map<String, Processor> getProcessors() {
        return processors;
    }

    public Processor getProcessor(String symbol) {
        Processor processor = null;
        if (processors.containsKey(symbol)) {
            processor = processors.get(symbol);
        }
        return processor;
    }


    public void addProcessor(String symbol, Processor processor) {
        processors.put(symbol, processor);
    }

    public Stock getStockBySymbol(String symbol) {
        for (Stock s : listing) {
            if (s.getSymbol().equals(symbol)) {
                return s;
            }
        }
        return null;
    }


    /**
     * scheduled task doTick - invoked as configured in Config.tickIntervalMilliseconds
     */

    public void doTick() {
        globalIndex = calcGlobalIndex();

        Instant now = Instant.now();

        log.debug("doTick()  globalIndex:" + globalIndex);

        for (Map.Entry<String, Processor> entry : processors.entrySet()) {
            String key = entry.getKey();
            Processor processor = entry.getValue();
            processor.processTick(now.minus(priceCalcInterval));
        }
    }

    public Double calcGlobalIndex() {
        List<Double> prices = tickers.values()
                .stream()
                .map(t -> t.getPrice().doubleValue())
                .filter(d -> !Double.isNaN(d) && d > 0)
                .collect(Collectors.toList());

        if (prices.size() > 0) {
            Double accumulated = prices.stream().reduce(1d, (acc, price) -> acc * price);
            Double order = 1d / prices.size();
            return Math.pow(accumulated, order);
        } else {
            return Double.NaN;
        }
    }


    /**
     * Creates a Trade based on input parameters and passes it to the appropriate processor
     *
     * @param tradeTypeStr
     * @param symbol
     * @param quantity
     * @param price
     */
    public void trade(String tradeTypeStr, String symbol, int quantity, BigDecimal price) {

        TradeType tradeType;
        Instant instantNow = Instant.now();

        if (tradeTypeStr.compareTo("sell") == 0) {
            tradeType = TradeType.SELL;
        } else if (tradeTypeStr.compareTo("buy") == 0) {
            tradeType = TradeType.BUY;
        } else {
            log.error("Invalid tradeType: must be 'buy' or 'sell'");
            return;
        }

        Stock stock = getStockBySymbol(symbol);

        log.debug("Trading (" + tradeTypeStr + "): " + symbol + " qty:" + quantity + "    price:" + price);

        Trade trade = new Trade(Instant.now(), tradeType, stock, quantity, new Currency(price));

        // pass trade to currwent stock's processor
        Processor processor = getProcessor(symbol);
        processor.processTrade(trade);
    }


    /**
     * Simulate trade (buy or sell) for each stock symbol, using
     * sellProbability and some random nenerated numbers (amount and price)
     * <p>
     * scheduled task, invoked as configured in Config.tradeIntervalMilliseconds
     */

    public void simulateTrade() {

        log.debug("simulateTrade() " + dateFormat.format(new Date()));

        double margin = 1.0 + Config.marginLimit * Math.random();
        for (Stock stock : listing) {

            // update local ticker from server for given stock symbol
            Ticker ticker = getTicker(stock.getSymbol());

            double rnd = Math.random();

            if (rnd < Config.sellProbability) {
                // sell
                Currency price = ticker.getPrice().multiply(margin);
                if (!price.isDefined()) {
                    price = Currency.random(stock.getParValue());
                }

                int quantity = Utils.randomQuantity(Config.maxTradeQuantity);
                trade("sell", stock.getSymbol(), quantity, price.getValue());
            } else {
                // buy
                Currency price = ticker.getPrice().divide(margin);
                if (!price.isDefined()) {
                    price = Currency.random(stock.getParValue());
                }

                int quantity = Utils.randomQuantity(Config.maxTradeQuantity);
                trade("buy", stock.getSymbol(), quantity, price.getValue());
            }
        }
    }


    /**
     * Scheduled task for reporting, invoked as configured in Config.reportIntervalMilliseconds
     */

    public void doReport() {
        AllStockInfoReport report = new AllStockInfoReport("Success", "", globalIndex);
        report.fillLists(getTickers());
        String output = report.stockInfoListReport();

        // print the report
        Utils.print(output);
    }

    public StockExchange() {

        // loads the listing from input file
        StockExchangeLoader loader = new StockExchangeLoader();
        try {
            listing = loader.loadFromFile(Config.listingFileName);
        } catch (IOException e) {
            log.error("IOException in StockExchangeLoader.loadFromFile():", e);
        }

        log.debug("Input file loaded.");

        // create tickers and processors map
        for (Stock stock : listing) {
            StockInfo stockInfo = new StockInfo();

            Ticker ticker = new Ticker(stock, stockInfo);

            // add ticker for stock
            addTicker(stock.getSymbol(), ticker);

            // add processor for stock, pass the same ticker as in tickers
            addProcessor(stock.getSymbol(), new Processor(ticker));
        }

    }
}
