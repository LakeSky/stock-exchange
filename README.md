# stock-exchange
Stock Exchange Simulator

Developed by Sorin Surdu Bob
email: sorin.surdu@castalia.ro
phone: +40 744 386 726

Notice: For development, relevant information and code samples found on the internet has been used.

Project information:

- Idea IntelliJ project (v.15)
- The gradle build system is a prerequisite

Build:
    gradle assemble

Server run:

    gradle bootRun

  or, after assemble:

    ./runServerJar.sh

Client run (after assemble):

    ./runClientJar.sh

The server implements:
    a Stock Exchange simulator (the StockExchange and Processor classes)
    a scheduler for several periodic tasks (the ScheduledTasks class)
    several web services (StockExchangeService class), as follows:
        /listing   : stock exchange listing in JSON format
        /report    : the report
        /watch
        /trade [with mandatory parameters]

After starting the server, a periodic report is displayed in the server console.

After starting the client, it will generate trade requests.

The client is a Web Service Client application based on Spring RestTemplate.
It produces trade requests on the url server:port/trade/

The client can be started in multiple instances.
