package org.stockexchange.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


public final class Currency {

    public static final Currency ZERO = new Currency(BigDecimal.ZERO);
    public static final Currency UNDEFINED = new Currency();

    private BigDecimal value = null;

    public BigDecimal getValue() {
        return value;
    }

    public int getValueInt(){
        return value.intValue();
    }

    public Currency(int value){
        if(value < 0) throw new IllegalArgumentException("Value cannot be lower than zero.");
        this.value = new BigDecimal(value);
    }


    public Currency(BigDecimal value){
        if(value==null) throw new IllegalArgumentException("Value cannot be null.");
        if(value.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Value cannot be lower than zero.");
        this.value = value.setScale(DIGITS, RoundingMode.HALF_UP);
    }

    private Currency(){
        this.value = null;
    }

    public boolean isDefined(){
        return this!=UNDEFINED && this.value.compareTo(BigDecimal.ZERO)>0;
    }

    public Currency multiply(int value){
        return multiply(new BigDecimal(value));
    }

    public Currency multiply(double value){
        return multiply(new BigDecimal(value));
    }

    public Currency multiply(BigDecimal value){
        if(this==UNDEFINED || value==null) return UNDEFINED;
        if(value.equals(BigDecimal.ZERO)) return ZERO;
        return new Currency(this.value.multiply(value));
    }

    public Currency multiply(Currency that){
        if(this==UNDEFINED || value==null) return UNDEFINED;
        if(value.equals(Currency.ZERO)) return ZERO;
        return new Currency(this.value.multiply(that.value));
    }

    public Currency divide(BigDecimal value){
        if(this==UNDEFINED || value == null || value.equals(BigDecimal.ZERO)) return UNDEFINED;
        return new Currency(this.value.divide(value, MathContext.DECIMAL32));
    }

    public Currency divide(int value){
        return divide(new BigDecimal(value));
    }

    public Currency divide(double value){
        return divide(new BigDecimal(value));
    }

    public Double divide(Currency that){
        if(this==UNDEFINED || that==UNDEFINED) return Double.NaN;
        if(that.equals(ZERO)) return Double.NaN;
        else {
            BigDecimal result = this.value.divide(that.value,MathContext.DECIMAL32);
            if(result.scale()>8) return result.setScale(8,RoundingMode.HALF_UP).doubleValue();
            else return result.doubleValue();
        }
    }

    public Currency add(Currency that){
        if(this==UNDEFINED || that==UNDEFINED) return UNDEFINED;
        else return new Currency(this.value.add(that.value));
    }

    public Currency subtract(Currency that){
        if(this==UNDEFINED || that==UNDEFINED) return UNDEFINED;
        else return new Currency(this.value.subtract(that.value));
    }

    public double doubleValue(){
        if(this==UNDEFINED) return Double.NaN;
        else return value.doubleValue();
    }

    public static Currency parse(String maybeMoney){
        try {
            return new Currency((BigDecimal) FORMAT.parse(maybeMoney.trim()));
        } catch (Exception e){
            return ZERO;
        }
    }

    @Override
    public String toString(){
        if(this==UNDEFINED) return "X";
        else if(this.equals(ZERO)) return "-";
        else return FORMAT.format(value);
    }

    @Override
    public int hashCode(){
        return value.hashCode();
    }

    public boolean equals(int that){
        return this.value.equals(new BigDecimal(that));
    }

    public boolean equals(Currency that){
        if(that != null){
            return (that.value.equals(this.value));
        } else {
            return false;
        }
    }

    private static final int DIGITS = 4;
    public static final DecimalFormat FORMAT = new DecimalFormat("0.####",
            new DecimalFormatSymbols(Locale.forLanguageTag("US_us")));

    static {
        FORMAT.setParseBigDecimal(true);
    }

    public static Currency random(int max){
        return new Currency((int) Math.round(Math.random() * max + 0.0001));
    }

    public static Currency random(Currency max){
        return new Currency((int) Math.round(Math.random() * max.doubleValue() + 0.0001));
    }

}