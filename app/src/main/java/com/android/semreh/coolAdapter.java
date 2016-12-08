package com.android.semreh;

/**
 * Created by Max on 12/7/2016.
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class coolAdapter extends BaseAdapter{
    private final ArrayList mData;

    public coolAdapter(HashMap<String, ArrayList<Float>> map)
    {
        mData = new ArrayList();
        mData.addAll(map.entrySet());

    }

    @Override
    public int getCount()
    {
        return mData.size();
    }

    @Override
    public HashMap.Entry<String, ArrayList<Float>> getItem(int position)
    {
        return (HashMap.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final View result;

        if(convertView == null)
        {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        }
        else
        {
            result = convertView;
        }

        HashMap.Entry<String, ArrayList<Float>> item = getItem(position);

        ((TextView) result.findViewById(R.id.symbolTextView)).setText(item.getKey());
        //((TextView) result.findViewById(R.id.quantityTextView)).setText(item.getValue().get(0).toString()); //getValue will get the corresponding Arraylist.  get(0) gets the first float in the list.  toString() makes that float a string.
        //((TextView) result.findViewById(R.id.investedTextView)).setText(item.getValue().get(1).toString());

        return result;
    }
}
