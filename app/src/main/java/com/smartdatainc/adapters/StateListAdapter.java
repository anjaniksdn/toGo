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

import com.smartdatainc.dataobject.State;
import com.smartdatainc.toGo.R;

import java.util.ArrayList;

public class StateListAdapter extends ArrayAdapter<State> {
    Context context;
    ArrayList<State> stateList;
    public StateListAdapter(Context context, ArrayList<State> stateList) {

        super(context, 0, stateList);
        this.context = context;
        this.stateList = stateList;

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
        View row=inflater.inflate(R.layout.state_list, parent, false);
        TextView label=(TextView)row.findViewById(R.id.txtViewStateName);
        label.setText(stateList.get(position).getStateName());



        return row;
    }
}
