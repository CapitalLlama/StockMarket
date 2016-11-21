package com.android.semreh;

import java.math.BigDecimal;

/**
 * Created by Max on 11/19/2016.
 */

public class stockStuff {
    private String symbol;
    private BigDecimal currentPrice;

    void setSymbol(String newSymbol)
    {
        symbol = newSymbol;
    }

    void setCurrentPrice(BigDecimal newPrice)
    {
        currentPrice = newPrice;
    }


    String getSymbol()
    {
        return symbol;
    }
    BigDecimal getCurrentPrice()
    {
        return currentPrice;
    }

    @Override
    public String toString(){
        return this.symbol;
    }
}
