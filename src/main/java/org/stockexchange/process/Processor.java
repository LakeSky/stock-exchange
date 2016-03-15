package org.stockexchange.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stockexchange.entity.Stock;
import org.stockexchange.entity.StockInfo;
import org.stockexchange.entity.Ticker;
import org.stockexchange.entity.Trade;
import org.stockexchange.util.Currency;

import java.time.Instant;
import java.util.*;

/**
 * This class implements the processing related to a stock symbol:
 *
 * initialization: Processor()
 * processTrade()
 * processTick()
 * updatePrice()
 *
 */

public class Processor {

    private static final Logger log = LoggerFactory.getLogger(Processor.class);

    // reference to the Ticker of the current Processor
    private Ticker ticker;

    // the transactions (trades) queue
    private Queue<Trade> queue = new PriorityQueue<>(Trade.byTime);

    private int quantity = 0;
    private Currency accumulated = Currency.ZERO;
    private Currency volume = Currency.ZERO;

    public Stock getStock() {
        return ticker.getStock();
    }

    public void setStock(Stock stock) {
        ticker.setStock(stock);
    }

    public Processor(Ticker ticker) { this.ticker = ticker; }

    public int getQueueSize() { return queue.size();};

    public void processTrade(Trade trade){

        log.debug("processor.processTrade() symbol:" + this.getStock().getSymbol());

        quantity = quantity + trade.quantity;
        Currency value = trade.price.multiply(trade.quantity);
        accumulated = accumulated.add(value);
        volume = volume.add(value);

        // add trade to queue
        queue.add(trade);

        updatePrice();

        log.debug ("processTrade() completed: " + trade.toString() + "   "+ ticker.getStockInfo().toString());
    }

    public void processTick(Instant last){

        log.debug("processor.processTick() symbol:" + this.getStock().getSymbol());

        Trade trade = queue.peek();
        while (trade != null && (trade.timestamp.isBefore(last))){
            quantity = quantity - trade.quantity;
            accumulated = accumulated.subtract(trade.price.multiply(trade.quantity));
            queue.poll();
            trade = queue.peek();
        }

        updatePrice();

        log.debug ("processTick() completed: [" + this.getStock().getSymbol() + "] "+ ticker.getStockInfo().toString());
    }

    private void updatePrice(){
        if(quantity > 0){
            Currency price = accumulated.divide(quantity);

            StockInfo stockInfo = new StockInfo(price,quantity,volume);

            ticker.setStockInfo(stockInfo);

            log.debug("updatePrice completed: " + stockInfo.toString());

        } else {
            ticker.setStockInfo(new StockInfo());
        }
    }

}
