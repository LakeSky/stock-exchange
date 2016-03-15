package org.stockexchange.process;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.stockexchange.entity.Stock;
import org.stockexchange.entity.StockType;
import org.stockexchange.exceptions.DuplicateStockException;
import org.stockexchange.util.Currency;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 * Load Stock Exchange Data from file
 */
public class StockExchangeLoader {

    private static final Logger log = LoggerFactory.getLogger(StockExchangeLoader.class);

    public String getString(String[] values, int position) throws ArrayIndexOutOfBoundsException {
        return values[position].trim();
    }

    public int getInt(String[] values, int position) throws NumberFormatException,
            ArrayIndexOutOfBoundsException {
        return Integer.parseInt(values[position].trim());
    }

    public double getDouble(String[] values, int position) throws NumberFormatException,
            ArrayIndexOutOfBoundsException {
        return Double.parseDouble(values[position].trim());
    }

    /**
     * loadFromFile is used for loading the listing from input file
     * @param fileName file to be loaded
     * @throws IOException I/O Exception  (other reason than File Not Found)
     * @throws FileNotFoundException File not found
     * @throws DuplicateStockException Duplicate Stock in input file: use different names
     */

    public List<Stock> loadFromFile(String fileName) throws IOException, FileNotFoundException, DuplicateStockException {

        List<Stock> listing = new ArrayList<>();

        //log.debug("Reading input file " + fileName);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            int currentIndex = 0;
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                // ignore empty lines
                if (line.length() == 0)
                    break;

                try {
                    String symbol = getString(values, 0);
                    String type = getString(values, 1);
                    Currency lastDividend = Currency.parse(getString(values,2));
                    Currency fixedDividend = Currency.parse(getString(values,3));
                    Currency parValue = Currency.parse(getString(values, 4));

                    StockType stockType;
                    if(type.equals("Common"))
                        stockType = StockType.COMMON;
                    else if(type.equals("Preferred"))
                        stockType = StockType.PREFERRED;
                    else
                        throw new IllegalArgumentException("Unknown Stock type: "+type);

                    Stock stock = new Stock(symbol,parValue,lastDividend,stockType, fixedDividend);

                    listing.add (stock);

                } catch (ArrayIndexOutOfBoundsException e) {
                    log.error("Exception: Invalid input line. The first three columns are mandatory: " + line);
                    return null;
                } catch (NumberFormatException e) {
                    log.error("Invalid input line. Columns tail, head must be int numbers: " + line);
                    return null;
                }

            }

        } catch (IOException e) {
            throw new IOException("IOException: " + e.getMessage());
        } finally {
            if (br != null) {
                br.close();
            }
        }

        return listing;
    }

}
