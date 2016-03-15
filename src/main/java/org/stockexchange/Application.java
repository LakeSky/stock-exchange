package org.stockexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.stockexchange.process.ScheduledTasks;
import org.stockexchange.process.StockExchange;

import java.util.Arrays;

@SpringBootApplication
@EnableScheduling

public class Application {

    private static String name = "stockexchange";

    public static String getName() {
        return name;
    }

    /**
     * Beans initialization
     * @param ctx
     */

    private void initializeBeans(ApplicationContext ctx){

        StockExchange stx = (StockExchange) ctx.getBean("stockExchange");

        ScheduledTasks scheduledTasks = (ScheduledTasks) ctx.getBean("scheduledTasks");

        // initialize scheduled tasks class
        scheduledTasks.init(this, stx);

        // List all beans
        /*
        System.out.println("Let's inspect all available beans:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
        */
    }

    private void run(String[] args){
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        initializeBeans(ctx);
    }

    /**
     * Application entry point
     * @param args
     */
    public static void main(String[] args) {
        Application app = new Application();
        app.run(args);
    }
}