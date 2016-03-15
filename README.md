# stock-exchange
## Stock Exchange Simulator

## AUTHOR:
* Sorin Surdu Bob

## Prerequisites
    * java 1.8
    * gradle

## Build
    gradle assemble

## Server run

    gradle bootRun

or, after gradle assemble:

    ./runServerJar.sh

## Client run after gradle assemble:

    ./runClientJar.sh

# Project description

The server implements:

* a Stock Exchange simulator (the StockExchange and Processor classes)
* a scheduler for several periodic tasks (the ScheduledTasks class)
* several web services (StockExchangeService class), as follows:

          /listing   : stock exchange listing in JSON format
          /report    : the report (http://localhost:8080/report/)
          /watch     : returns ticker information for one stock symbol
          /trade     : perform trade [mandatory parameters]

After starting the server, a periodic report is displayed in the server console.

After starting the client, it will generate trade requests.

The client is a Web Service Client application based on Spring RestTemplate.
It produces trade requests on the URI http://localhost:8080/trade/

The client can be started in multiple instances.
