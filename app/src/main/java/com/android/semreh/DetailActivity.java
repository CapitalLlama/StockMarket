package com.android.semreh;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import au.com.bytecode.opencsv.CSVReader;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DetailActivity extends Activity {

    static DetailActivity activity;
    static int stockPosition;
    static double stockPrice;
    static String stockSymbol;

    private static TextView stockTitleTextView;
    private static TextView actualPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        stockTitleTextView = (TextView) findViewById(R.id.stockTitleTextView);
        actualPriceTextView = (TextView) findViewById(R.id.actualPriceTextView);

        activity = DetailActivity.this;

        init();
    }

    public static void setStockPosition(int b, int p){
        stockPosition = b + p;
    }

    public static void setStockPrice(double p){
        stockPrice = p;
        updateInfo();
    }

    static void init(){
        stockSymbol = MainActivity.getSymbol(stockPosition);
        MainActivity.getPrice(stockSymbol);
        updateInfo();
    }

    static void updateInfo(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // This code will always run on the UI thread, therefore is safe to modify UI elements.
                if(stockSymbol.length() > 0)
                    stockTitleTextView.setText(stockSymbol);
                else
                    stockTitleTextView.setText(R.string.loading);

                if(stockPrice > 0)
                    actualPriceTextView.setText("$" + Double.toString(stockPrice));
                else
                    actualPriceTextView.setText(R.string.loading);
            }
        });
    }

}
