package org.stockexchange.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.stockexchange.Application;
import org.stockexchange.util.Config;

/**
 *  Class for task scheduling (spring based)
 */

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private boolean initialized = false;    // set to true at initialization
    Application application = null;         // application member will be set after initialization
    StockExchange stockExchange=null;

    /**
     * scheduled task for stockExchange.doTick();
     */
    @Scheduled(fixedRate = Config.tickIntervalMilliseconds)

    public void tickTask() {
        if (initialized) {
            stockExchange.doTick();
        }
    }

    /**
     * scheduled task for stockExchange.simulateTrade();
     */
    @Scheduled(fixedRate = Config.simulateTradeIntervalMilliseconds)

    public void tradeTask() {
        if (initialized) {
            stockExchange.simulateTrade();
        }
    }

    /**
     * scheduled task for stockExchange.doReport();
     */
    @Scheduled(fixedRate = Config.reportIntervalMilliseconds)

    public void reportTask() {
        if (initialized) {
            stockExchange.doReport();
        }
    }


    /**
     * Class members initialization
     */
    public void init(Application application, StockExchange stockExchange){

        this.application = application;
        this.stockExchange = stockExchange;

        initialized = true;
    }

    /**
     * Constructor
     */
    public ScheduledTasks() {
    }

}
