package org.stockexchange.process;

import org.junit.Test;
import org.stockexchange.entity.Stock;
import org.stockexchange.entity.StockInfo;
import org.stockexchange.entity.StockType;
import org.stockexchange.entity.Ticker;
import org.stockexchange.util.Currency;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by sorin on 15.03.2016.
 */
public class StockExchangeTest {

    @Test
    public void testTradeAndTick() throws Exception {

        StockExchange stx = new StockExchange();
        stx.getListing().clear();
        Stock stock = new Stock(
                "GIN",
                new Currency(new BigDecimal(100)),
                new Currency(new BigDecimal(8)),
                StockType.PREFERRED,
                new Currency(new BigDecimal(2)));

        stx.getListing().add(stock);

        Ticker ticker = new Ticker(stock, new StockInfo());

        // add ticker for stock
        stx.addTicker(stock.getSymbol(), ticker);

        // add processor for stock, pass the same ticker as in tickers

        stx.addProcessor(stock.getSymbol(), new Processor(ticker));


        assertEquals(1, stx.getListing().size());

        assertEquals("GIN", stx.getListing().get(0).getSymbol());

        stx.trade("buy", "GIN",  100, new BigDecimal(10));
        stx.trade("sell", "GIN",  50, new BigDecimal(10));
        stx.trade("buy", "GIN",  200, new BigDecimal(10));
        stx.trade("buy", "GIN",  100, new BigDecimal(10));
        stx.trade("sell", "GIN",  50, new BigDecimal(10));

        // check stock processor queue size
        assertEquals(5, stx.getProcessor("GIN").getQueueSize());

        stx.doTick();

        assertEquals(10, (int)stx.getGlobalIndex());

        Ticker ticker2 = stx.getTickers().get("GIN");

        // check volume for ticker
        assertEquals("5000", ticker2.getVolume().toString());

        BigDecimal value = ticker2.getVolume().getValue();
        assertEquals(5000, value.intValue());

    }
}

