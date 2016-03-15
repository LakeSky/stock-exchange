package org.stockexchange.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.stockexchange.entity.Stock;

import org.stockexchange.entity.Ticker;
import org.stockexchange.response.ListingResponse;
import org.stockexchange.response.TradeResponse;
import org.stockexchange.response.WatchResponse;
import org.stockexchange.util.Currency;

/**
 *
 */
public class ClientTrader {
    private static final Logger log = LoggerFactory.getLogger(ClientTrader.class);

    private ClientConfig clientConfig;
    private ClientApplication app;

    public ClientTrader(ClientApplication app) {
        this.app = app;
    }

    /**
     * Web service client for "/listing"
     */

    public void updateListing(){
        String uri="";
        try {
            RestTemplate restTemplate = new RestTemplate();
            uri = clientConfig.baseurl + "/listing";
            ListingResponse response = restTemplate.getForObject(uri, ListingResponse.class);

            // update local listing using server listing
            app.setListing(response.getListing());

            log.debug("listing: " + app.getListing().toString());
        } catch (ResourceAccessException e){
            log.error("ResourceAccessException: please check if the server is started and accessible: " + uri );
            System.exit(-1);
        }
    }

    /**
     * Web service client for /watch
     */

    public Ticker getTicker(String symbol){
        RestTemplate restTemplate = new RestTemplate();
        String uri = clientConfig.baseurl + "/watch"
                + "?"
                + "symbol=" + symbol ;
        WatchResponse response = restTemplate.getForObject(uri, WatchResponse.class);

        if (response.getStatus().compareTo("Success")==0){
            Ticker ticker = response.getTicker();
            log.debug ("ticker (" + symbol + "): " + ticker.toString());

            // update local ticker
            app.putTicker(symbol, ticker);

            return ticker;
        } else {
            log.error("getTicker() failed for symbol: " + symbol);
            return null;
        }
    }


    /**
     * Web service client for /trade
     */

    public TradeResponse trade(String tradeType, String symbol, int amount, Currency price){  // TradeResponse

        // tradeType is "sell" or "buy"

        RestTemplate restTemplate = new RestTemplate();
        String uri = clientConfig.baseurl + "/trade"
                + "?"
                + "&"+ "tradeType=" + tradeType
                + "&"+ "symbol=" + symbol
                + "&"+ "amount=" + amount
                + "&"+ "price=" + price.getValue()
                ;

        log.info("trade: " + uri);

        TradeResponse response = restTemplate.getForObject(uri, TradeResponse.class);

        if (response.getStatus().compareTo("Success")==0){
            return response;
        } else {
            log.error("trade() failed for symbol: " + symbol);
            return null;
        }
    }

    /*
    *
    * performs trading :
    *  - updates the listing from server (service /listing)
    *  - for each stock symbol
    *      - decide to sell or buy (based on givedn probability)
    *      - perform sell or buy, invoke service /trade on server
    */


    public void doTrade(){
        try {
            Thread.sleep(ClientConfig.sleepMilliseconds);
        } catch (InterruptedException e){
            System.out.println("*** InterruptedException ***");
        }

        double margin = 1.0 + ClientConfig.marginLimit * Math.random();

        // update local listing from server (call /listing service)
        updateListing();

        for (Stock stock: app.getListing()){

            // update local ticker from server for given stock symbol
            Ticker ticker = getTicker(stock.getSymbol());

            double rnd = Math.random();

            if (rnd < ClientConfig.sellProbability){
                // sell
                Currency price = ticker.getPrice().multiply(margin);
                if(!price.isDefined()){
                    price = Currency.random(stock.getParValue());
                }

                int quantity = ClientUtils.randomQuantity(ClientConfig.maxTradeQuantity);
                trade("sell", stock.getSymbol(), quantity, price);

                log.debug("Trading (sell): " + stock.getSymbol() + " qty:" + quantity + "    price:" + price.getValue());
            }
            else {
                // buy
                Currency price = ticker.getPrice().divide(margin);
                if(!price.isDefined()){
                    price = Currency.random(stock.getParValue());
                }

                int quantity = ClientUtils.randomQuantity(ClientConfig.maxTradeQuantity);
                trade("buy", stock.getSymbol(), quantity, price);

                log.debug("Trading (buy): " + stock.getSymbol() + " qty:" + quantity + "    price:" + price.getValue());
            }
        }
    }

}
