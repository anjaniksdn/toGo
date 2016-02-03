package com.smartdatainc.adapters;

/**
 * Created by anjanikumar on 20/1/16.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.smartdatainc.dataobject.Country;
import com.smartdatainc.toGo.R;

import java.util.ArrayList;

public class CountriesListAdapter extends  ArrayAdapter<Country> {
    Context context;
    ArrayList<Country> countryList;
    public CountriesListAdapter(Context context, ArrayList<Country> countryList) {

    super(context, 0, countryList);
        this.context = context;
        this.countryList = countryList;

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
        View row=inflater.inflate(R.layout.country_list_item, parent, false);
        TextView label=(TextView)row.findViewById(R.id.txtViewCountryName);
        label.setText(countryList.get(position).getCountryName());



        return row;
    }
}
