package com.android.semreh;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import au.com.bytecode.opencsv.CSVReader;

import android.app.Activity;
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

public class MainActivity extends Activity {

    private TextView quoteDisplay;
    private TextView symbolDisplay;
    private ArrayList<Stock> words;  //List of Stocks.
    private ArrayAdapter<Stock> adapter; //binds Stocks to the List.  I'll have to extend the ArrayAdapter class to handle Stock class
    int stockIndex = 0;
    String next[];
    List<String> StockSymbols = new ArrayList<String>();
    int spot = 0;
    int numStocks = 0;
    int updateSpot = 0;
    int ActuallyRegisteredStocks = 0;
    int begin = 0;
    int end = 50;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quoteDisplay = (TextView) findViewById(R.id.QuoteTextView);
        symbolDisplay = (TextView) findViewById(R.id.SymbolTextView);

        words = new ArrayList<Stock>();

        //adapter = new ArrayAdapter<stockStuff>(this, R.layout.list_item, words);
        //setListAdapter(adapter);

        ImageButton testButton = (ImageButton) findViewById(R.id.testingButton);
        testButton.setOnClickListener(testButtonListener);

        ImageButton updateButton = (ImageButton) findViewById(R.id.UpdateTextButton);
        updateButton.setOnClickListener(updateButtonListener);

        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open("companylist.csv")));  //Open up that sweet stock symbol file.  It's in the assets folder.
            next = (reader.readNext()); //Skip the first line

            for(;;)
            {
                next = (reader.readNext());
                if(next != null)
                {
                    StockSymbols.add(next[0]);
                    numStocks++; //Keeps count of all the stocks
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
      Worked on this a while, will return
     */

    //On click, go through the list of NASDAQ symbols.  Each symbol will get its information obtained in a Stock class (see the JavaDoc for this class).
    //The Stock object will be stored into the list "words".  This list should have all the information in Stock objects.
    //Currently only gets 50 stocks per click.  Yahoo Finance tracks the requests per hour and limits us to 2000 requests.  There are just above 3000 NASDAQ stocks.  :(
    public View.OnClickListener testButtonListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {

            for (int i = begin; i < end; i++) { //Should be for the numStocks value
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            String stockSymbol = (StockSymbols.get(spot));
                            spot++;

                            Stock stock = YahooFinance.get(stockSymbol);
                            words.add(stock);
                            ActuallyRegisteredStocks++;


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            symbolDisplay.setText(numStocks);
            quoteDisplay.setText(ActuallyRegisteredStocks);

            //Update the indexes
            begin+=50;
            end+=50;

        }

    };

    //Updates the text views with the information
    public View.OnClickListener updateButtonListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            symbolDisplay.setText((words.get(updateSpot).getSymbol()));
            quoteDisplay.setText(words.get(updateSpot).getQuote().getPrice().toString()); //Some error about String formatting here.  BigDecimal.toString() seems to cause problems
            updateSpot++;

        }
    };


}
