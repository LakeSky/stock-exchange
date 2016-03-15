package org.stockexchange.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.stockexchange.process.StockExchangeLoader;
import org.stockexchange.entity.Stock;
import org.stockexchange.entity.Ticker;

import java.io.IOException;
import java.lang.Thread;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Client application, which simulate trade operations buy/sell
 *
 */

public class ClientApplication {
    private static final Logger log = LoggerFactory.getLogger(ClientApplication.class);
    private static final ClientConfig clientConfig = new ClientConfig();

    private List<Stock> listing;

    // the tickers map will use the stock symbols as key (String)
    private final Map<String, Ticker> tickers = new HashMap<>();

    private String[] args;

    public List<Stock> getListing() {
        return listing;
    }

    public void setListing(List<Stock> listing) {
        this.listing = listing;
    }

    public void putTicker(String symbol, Ticker ticker){
        tickers.put(symbol, ticker);
    }

    public Map<String, Ticker> getTickers() {
        return tickers;
    }

    public Ticker getTicker(String symbol){
        Ticker ticker = null;
        if (tickers.containsKey(symbol)) {
            ticker = tickers.get(symbol);
        }
        return ticker;
    }


    private void printListing(){
        for (Stock stock: listing){
            ClientUtils.print(stock.toString());
        }
    }


    public ClientApplication(String[] args) {
        this.args = args;

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("client.xml");
        ClientConfig clientConfig = (ClientConfig)applicationContext.getBean("clientConfig");

        //log.debug("ClientConfig.baseurl: " + clientConfig.baseurl);

        StockExchangeLoader loader = new StockExchangeLoader();
        try {
            listing = loader.loadFromFile("listing.txt");
        } catch (IOException e){
            log.error("IOException in StockExchangeLoader.loadFromFile():",e);
        }

        //printListing();

        ClientTrader clientTrader = new ClientTrader(this);

        // DEMO: infinite trading loop
        while(true){
            try {
                Thread.sleep(1000);
                clientTrader.doTrade();
            } catch (InterruptedException e){
                log.error("InterruptedException", e);
            }
        }
    }


    /**
     * Entry point for client application
     * @param args
     */
    public static void main(String[] args){

        new ClientApplication(args);
    }

}
