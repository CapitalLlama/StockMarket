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


import org.w3c.dom.Text;

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
    static float stockPrice;
    static String stockSymbol;

    private static TextView stockTitleTextView;
    private static TextView actualPriceTextView;

    private EditText stockQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        stockTitleTextView = (TextView) findViewById(R.id.stockTitleTextView);
        actualPriceTextView = (TextView) findViewById(R.id.actualPriceTextView);

        stockQuantity = (EditText) findViewById(R.id.buyEditText);

        activity = DetailActivity.this;

        Button buyButton = (Button) findViewById(R.id.confirmBuyButton);
        buyButton.setOnClickListener(buyButtonListener);


        init();
    }

    public View.OnClickListener buyButtonListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            float quantaties = 0;
            float product = 0;

            if(stockQuantity.getText().toString().length() > 0) {
                quantaties = Integer.parseInt(stockQuantity.getText().toString());  //Get quantity

                product = quantaties * stockPrice;
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setMessage("Input Number Of Stocks To Purchase");
                builder.setPositiveButton("Got it!", null);
                AlertDialog initialDialog = builder.create();
                initialDialog.show();
            }

            if(MainActivity.totalMoney >= product) //If we have the money to buy the stocks, then buy the stocks.
            {
                //Buy Stocks, update the total dosh, add quantity, and add total stock money, and update stock profit.
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setMessage("You bought " + quantaties + " stocks of " + stockSymbol + "\n\nTotal Purchase: $" + product);
                builder.setPositiveButton("Got it!", null);
                AlertDialog initialDialog = builder.create();
                initialDialog.show();

                MainActivity.totalMoney -= product;
                MainActivity.updateOwnedStocks(stockSymbol, quantaties, product);
            }
        }
    };

    public static void setStockPosition(int b, int p){
        stockPosition = b + p;
    }

    public static void setStockPrice(float p){
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
                    actualPriceTextView.setText("   $" + String.format(java.util.Locale.US,"%.2f", stockPrice));
                else
                    actualPriceTextView.setText(R.string.loading);
            }
        });
    }

}
