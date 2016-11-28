package com.android.semreh;


import android.app.Activity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import android.text.TextWatcher;
import android.text.Editable;


/**
 * Created by Michael on 11/27/2016.
 */

public class SearchActivity extends ListActivity {

    private ArrayList<String> stocksList;
    private ArrayList<String> holderList;
    private ArrayAdapter<String> adapter;

    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.searchmode);

        holderList = new ArrayList<String>();
        stocksList = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, R.layout.stocklist_item, stocksList);
        setListAdapter(adapter);

        PopulateList();

        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(search_EditText_Watcher);

        //listener to search for tag's query
        getListView().setOnItemClickListener(itemClickListener);

        // listener to edit tag
        getListView().setOnItemLongClickListener(itemLongClickListener);
    }

    void PopulateList(){
        stocksList.addAll(MainActivity.StockSymbols);
        holderList.addAll(MainActivity.StockSymbols);
    }

    void ChangeList(int begin, int end){
        stocksList.clear();

        stocksList.addAll(holderList.subList(begin, end));

        if(stocksList.size() == 0)
            getListView().setEmptyView(findViewById(R.id.empty_list_item));

        adapter.notifyDataSetChanged();
    }

    private OnItemClickListener itemClickListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id)
        {}
    };

    //edits the tag/query save
    private OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener()
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id)
        {
            return true;
        }
    };

    private TextWatcher search_EditText_Watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() > 0) {
                int begin = 0;
                int end = 0;
                char c[];
                char v[];
                int listSize = holderList.size();
                int charSize = s.length();
                boolean found;

                for (int i = 0; i < listSize; i++) {
                    v = holderList.get(i).toCharArray();
                    found = true;
                        for(int j=0; j < charSize; j++) {
                            c = s.toString().toCharArray(); //s.charAt(j);
                            if(c[j] != v[j]){
                                j = charSize;
                                found = false;
                            }
                        }
                    if(found) {
                        begin = i;
                        i = listSize;
                    }
                }

                end = begin;
                for (int i = begin; i < listSize; i++) {
                    v = holderList.get(i).toCharArray();
                    found = false;
                    for(int j=0; j < charSize; j++) {
                        c = s.toString().toCharArray();
                        if(c[j] != v[j]){
                            j = charSize;
                            found = true;
                        }
                    }
                    if(found) {
                        end = i;
                        i = listSize;
                    }
                }
                if(end < begin)
                    end = begin;
                ChangeList(begin, end);
            }
            else{
                ChangeList(0, holderList.size());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    };
}
