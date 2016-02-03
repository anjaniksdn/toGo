package com.smartdatainc.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.smartdatainc.toGo.R;

import java.util.ArrayList;

/**
 * Created by anjanikumar on 20/1/16.
 */

public class Cardadapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> cardList;
    public Cardadapter(Context context, ArrayList<String> cardList) {

        super(context, 0, cardList);
        this.context = context;
        this.cardList = cardList;

    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
// TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
// TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
// TODO Auto-generated method stub
//return super.getView(position, convertView, parent);

        LayoutInflater inflater= ((Activity)context).getLayoutInflater();;
        View row=inflater.inflate(R.layout.cardadpter_item, parent, false);
        TextView label=(TextView)row.findViewById(R.id.txtcardName);
        label.setText(cardList.get(position));



        return row;
    }
}
