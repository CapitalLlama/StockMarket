package com.android.semreh;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import au.com.bytecode.opencsv.CSVReader;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.widget.AbsListView;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    private static HashMap<String, ArrayList<Float>> ownedStocks;  //List of Stocks.




    //private static HashMap<String, ArrayList<Float>> ownedStocks;  //List of Stocks.
    //private ArrayAdapter<HashMap<String, ArrayList<Float>>> adapter; //binds Stocks to the List.  I'll have to extend the ArrayAdapter class to handle Stock class

    int stockIndex = 0;
    String next[];
    public static List<String> StockSymbols = new ArrayList<String>();
    int spot = 0;
    int numStocks = 0;
    int updateSpot = 0;
    int ActuallyRegisteredStocks = 0;
    int begin = 0;
    int end = 50;

    int detailLocation;

    public static float totalMoney = 0;

    private static final String MYSTOCKS = "myStocks";  //List of stocks you own
    private static final String STOCKQUANTITY = "stockQuantity";  //List of the number of stocks you own
    private static final String STOCKTOTALS = "stockTotals";  //List of the total money you spent on certain stocks
    private static final String MONEY = "money";  //A single variable representing your money.
    //private static final String USED = "used";
    private static final String FIRST_TIME_USED_KEY = "first_time_used_key";

    public SharedPreferences myStocks;  //Since myStocks only has the symbols of stocks you own, create an array adapter and bind it to the list view in portfolio
    private SharedPreferences stockQuantity;//Holds keys=symbols and values=quantity of stocks you own
    private SharedPreferences stockTotals; //key=symbol value=total spent on that stock
    public SharedPreferences money;  //Hold total money, assets, networth, profit,
    private SharedPreferences mPrefs; //Experimental method of getting an alertDialog/other UI to appear only once to set the money to 10k.
    private SharedPreferences.Editor mPrefsEditor;

    //New Max section
    private ArrayList<String> coolSymbols;
    private ArrayAdapter<String> adapter;



    public coolAdapter maxAdapts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);















        //ArrayList<float[]> something = new ArrayList<float[]>();
        //something.add(new float[]{5,2});

        ownedStocks = new HashMap<String, ArrayList<Float>>();

        //adapter = new ArrayAdapter<HashMap<String, ArrayList<Float>>>(this, R.layout.stocklist_item, ownedStocks);

        //ownedStocks.put("Nooo",something);
        myStocks = getSharedPreferences(MYSTOCKS, MODE_PRIVATE);

        coolSymbols = new ArrayList<String>(myStocks.getAll().keySet());//The coolSymbols array should have all the coolSymbols we collected
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, coolSymbols);

        ListView list = (ListView) findViewById(R.id.stockListView);
        list.setAdapter(adapter);












        /*

        coolAdapter maxAdapts = new coolAdapter(ownedStocks);

        list.setAdapter(maxAdapts);

        Another solution to the adapter issue?
        ArrayList<String> maxArray = new ArrayList<String>();

        Set<String> coolSet = ownedStocks.keySet();

        maxArray.addAll(coolSet);
        //maxArray = coolSet.toArray(new String[coolSet.size()]);

        ListAdapter maxList = new ArrayAdapter<String>(this, R.layout.list_item, maxArray);
        list.setAdapter(maxList);
        */

        //myStocks = getSharedPreferences(MYSTOCKS, MODE_PRIVATE);
        stockQuantity = getSharedPreferences(STOCKQUANTITY, MODE_PRIVATE);
        stockTotals = getSharedPreferences(STOCKTOTALS, MODE_PRIVATE);
        money = getSharedPreferences(MONEY, MODE_PRIVATE);

        //getSharedPreferences(MYSTOCKS, 0).edit().clear().apply();
        getSharedPreferences(STOCKQUANTITY, 0).edit().clear().apply();
        getSharedPreferences(STOCKTOTALS, 0).edit().clear().apply();
        getSharedPreferences(MONEY, 0).edit().clear().apply();
        //getSharedPreferences(FIRST_TIME_USED_KEY, 0).edit().clear().apply();

        totalMoney = money.getFloat("money", 0);



        mPrefs = getSharedPreferences(FIRST_TIME_USED_KEY, MODE_PRIVATE);
        boolean ifUsedOnce = mPrefs.getBoolean(FIRST_TIME_USED_KEY, false); //Retrieve the firsttimeusedkey, it's default is false if the key doesn't exist
        if(ifUsedOnce == true)
        {}
        else //Build an alertDialog indicating free money and give the user free money
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Congratulations!!!\n You have received 10,000 dollars to invest in the stock market");
            builder.setPositiveButton("Got it!", null);
            AlertDialog initialDialog = builder.create();
            initialDialog.show();


            money.edit().putFloat("TotalMoney", 10000);
            money.edit().commit();

            totalMoney = 10000;

            mPrefsEditor = mPrefs.edit();
            mPrefsEditor.putBoolean(FIRST_TIME_USED_KEY, true);
            mPrefsEditor.apply(); //I realise commit() will take priority over apply()
        }

        //adapter = new ArrayAdapter<stockStuff>(this, R.layout.list_item, words);
        //setListAdapter(adapter);

        Button tradeButton = (Button) findViewById(R.id.tradeButton);
        tradeButton.setOnClickListener(tradeButtonListener);

        Button netWorthButton = (Button) findViewById(R.id.netWorthButton);
        netWorthButton.setOnClickListener(netWorthButtonListener);

        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open("companylist.csv")));  //Open up that sweet stock symbol file.  It's in the assets folder.
            //next = (reader.readNext()); //Skip the first line

            for (; ; ) {
                next = (reader.readNext());
                if (next != null) {
                    StockSymbols.add(next[0]);
                    numStocks++; //Keeps count of all the stocks
                } else {
                    break;
                }
            }
        } catch (IOException e) {
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



            /*
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
            quoteDisplay.setText(Integer.toString(ActuallyRegisteredStocks));
            symbolDisplay.setText(Integer.toString(numStocks));

            //Update the indexes
            begin+=50;
            end+=50;
            */

        }

    };

    public View.OnClickListener netWorthButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            maxAdapts.notifyDataSetChanged();
        }
    };

    //Go to Trade Screen
    public View.OnClickListener tradeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(view.getContext(), SearchActivity.class));
        }

    };

    /*
    //Updates the text views with the information
    public View.OnClickListener updateButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            symbolDisplay.setText((words.get(updateSpot).getSymbol()));
            if (words.get(updateSpot).getQuote().getPrice() != null)
                quoteDisplay.setText(words.get(updateSpot).getQuote().getPrice().toString());
            else
                quoteDisplay.setText("Error");

            updateSpot++;

            if (updateSpot >= words.size()) {
                updateSpot = 0;
            }
        }
    };
    */

    public static String getSymbol(int i) {
        return StockSymbols.get(i);
    }

    public static void getPrice(final String s) {
        DetailActivity.setStockPrice(-1);
        new Thread(new Runnable() {
            double d = -1.337;
            Stock stock;
            @Override
            public void run() {
                try {
                    stock = YahooFinance.get(s);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                DetailActivity.setStockPrice(stock.getQuote().getPrice().floatValue());
            }

        }).start();
    }

    public static int updateOwnedStocks(String s, float q, float p){
        if(ownedStocks.containsKey(s)){
            ArrayList<Float> ownedList = ownedStocks.get(s);
            float ownedQ = ownedList.get(0);
            float ownedP = ownedList.get(1);

            ownedQ += q;
            ownedP += p;

            ownedList = new ArrayList<Float>();
            ownedList.add(ownedQ);
            ownedList.add(ownedP);

            ownedStocks.put(s,ownedList);
        }
        else{
            ArrayList<Float> ownedList = new ArrayList<Float>();
            ownedList.add(q);
            ownedList.add(p);

            ownedStocks.put(s,ownedList);
        }

        updateStuff(ownedStocks);

        return 1;
    }

    private void updateStuff(HashMap<String, ArrayList<Float>> BigDog)
    {
        Set<String> SymbolsOnly = BigDog.keySet();
        ArrayList<String> Henry = new ArrayList<String>();
        Henry.addAll(SymbolsOnly);  //This is now a list of all the Strings so far.

        for(int i = 0; i < Henry.size(); i++)
        {
            if(!myStocks.contains(Henry.get(i)))
            {
                myStocks.edit().putString(Henry.get(i), Henry.get(i));  //This is the part where we would have added the new symbol to the sharedPref file.  But we can't because of static interference.
            }
        }

    }

}

