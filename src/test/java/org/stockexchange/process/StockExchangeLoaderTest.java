package org.stockexchange.process;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stockexchange.entity.Stock;
import org.stockexchange.entity.StockType;
import org.stockexchange.util.Currency;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for StockExchangeLoader
 */
public class StockExchangeLoaderTest {
    private static final Logger log = LoggerFactory.getLogger(StockExchangeLoaderTest.class);

    private String[] testStrings = {"Test","15","3.1415"};
    private List<Stock> listing;
    private StockExchangeLoader loader = new StockExchangeLoader();

    @Test
    public void testGetString() throws Exception {
        assertEquals("Test", loader.getString(testStrings, 0));
    }

    @Test
    public void testGetInt() throws Exception {
        assertEquals(15, loader.getInt(testStrings, 1));
    }

    @Test
    public void testLoadFromFile() throws Exception {
        try {
            listing = loader.loadFromFile("listing.txt");
        } catch (IOException e){
            log.error("IOException in StockExchangeLoader.loadFromFile():",e);
        }

        assertEquals(5, listing.size());

        Stock stock = listing.get(3);
        Stock expectedStock = new Stock(
                "GIN",
                new Currency(new BigDecimal(100)),
                new Currency(new BigDecimal(8)),
                StockType.PREFERRED,
                new Currency(new BigDecimal(2))
        );

        assertEquals(expectedStock.getSymbol(), stock.getSymbol());
        assertEquals(expectedStock.getType(), stock.getType());

        // test also the Stock comparison function
        assertEquals(true, expectedStock.equals(stock));
    }
}