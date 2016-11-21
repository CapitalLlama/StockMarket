package com.android.semreh;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import au.com.bytecode.opencsv.CSVReader;

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
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends ListActivity {

    private TextView display;
    private TextView symbolDisplay;
    private ArrayList<stockStuff> words;  //List of User's saved words
    private ArrayAdapter<stockStuff> adapter; //binds words to ListView
    int stockIndex = 0;
    String next[];
    List<String> StockSymbols = new ArrayList<String>();
    int spot = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = (TextView) findViewById(R.id.portfolioTextView);
        symbolDisplay = (TextView) findViewById(R.id.symbolTextView);

        words = new ArrayList<stockStuff>();

        adapter = new ArrayAdapter<stockStuff>(this, R.layout.list_item, words);
        setListAdapter(adapter);

        ImageButton testButton = (ImageButton) findViewById(R.id.testingButton);
        testButton.setOnClickListener(testButtonListiner);

        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open("companylist.csv")));  //Open up that sweet stock symbol file.  It's in the assets folder.

            for(;;)
            {
                next = (reader.readNext());
                if(next != null)
                {
                    StockSymbols.add(next[0]);
                }
                else
                {
                    break;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
    Comrades, it seems that in order to have the adapter bind the arraylist of stockStuff objects, the adapter class will have to first be modified.
      After working on this for a while, I kind of gave up.
     */
    public View.OnClickListener testButtonListiner = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {/*
           try
            {

                stockStuff newitem = new stockStuff();
                String stockSymbol = (StockSymbols.get(stockIndex));
                stockIndex++;

                Stock stock = YahooFinance.get(stockSymbol);
                BigDecimal currentPrice = stock.getQuote().getPrice();
                newitem.setCurrentPrice(currentPrice);
                newitem.setSymbol(stockSymbol);

                //display.setText((newitem.getCurrentPrice()).toString());
                symbolDisplay.setText(newitem.getSymbol());
                addStock(newitem);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        */
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        stockStuff newitem = new stockStuff();
                        String stockSymbol = (StockSymbols.get(stockIndex));
                        stockIndex++;

                        Stock stock = YahooFinance.get(stockSymbol);
                        BigDecimal currentPrice = stock.getQuote().getPrice();
                        newitem.setCurrentPrice(currentPrice);
                        newitem.setSymbol(stockSymbol);

                        //display.setText((newitem.getCurrentPrice()).toString());
                        symbolDisplay.setText(newitem.getSymbol());
                        addStock(newitem);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    };

    private void addStock(stockStuff newitem)
    {
        words.add(newitem);
        adapter.notifyDataSetChanged();
    }

}
