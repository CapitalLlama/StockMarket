package com.android.semreh;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Vector;

public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStreamReader is = new InputStreamReader(getAssets().open("companylist.csv"));

        BufferedReader reader = new BufferedReader(is);
        reader.readLine(); //Skip the header
        String line;

        while((line = reader.readLine() != null)
        {
            //line gets the symbol, the first value in a row
            //get the stock information
            //Update the dynamic 2d array
        }



    }
}
