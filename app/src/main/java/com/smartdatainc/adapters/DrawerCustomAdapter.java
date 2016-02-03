package com.smartdatainc.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartdatainc.toGo.R;;

import java.util.ArrayList;

/**
 * Created by Anjani Kumar
 */
public class DrawerCustomAdapter extends ArrayAdapter<String> {

    LayoutInflater inflater;
    ViewHolder holder;
    private int resource;
    private ArrayList<String> objects;


    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a country_list_item file containing a TextView to use when
     */
    public DrawerCustomAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);

        this.objects = objects;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custom_drawer_adpter, parent, false);

            holder.text = (TextView) convertView.findViewById(R.id.drawerTextView);
            holder.image = (ImageView) convertView.findViewById(R.id.drawerImageView);
            holder.drawerBackGround = (LinearLayout) convertView.findViewById(R.id.drawerBackGround);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.drawerBackGround.setBackgroundColor(getContext().getResources().getColor(R.color.profile));
            holder.image.setBackgroundResource(R.drawable.dashboard);
        }
        if (position == 1) {
            holder.drawerBackGround.setBackgroundColor(getContext().getResources().getColor(R.color.order));
            //holder.image.setBackgroundResource(R.drawable.order_interpreter);
            holder.image.setBackgroundResource(R.drawable.profile_icon);
        }
        if (position == 2) {
            holder.drawerBackGround.setBackgroundColor(getContext().getResources().getColor(R.color.callhistory));
            //holder.image.setBackgroundResource(R.drawable.call_history_icon);
            holder.image.setBackgroundResource(R.drawable.call_history_icon);
        }
        if (position == 3) {
            holder.drawerBackGround.setBackgroundColor(getContext().getResources().getColor(R.color.purchase));
            holder.image.setBackgroundResource(R.drawable.fav_interpreter_icon);
        }
        if (position == 4) {
            holder.drawerBackGround.setBackgroundColor(getContext().getResources().getColor(R.color.favinterpreter));
            holder.image.setBackgroundResource(R.drawable.purchase_icon);
        }
        if (position == 5) {
            holder.drawerBackGround.setBackgroundColor(getContext().getResources().getColor(R.color.seetings));
            holder.image.setBackgroundResource(R.drawable.settings_icon);
        }
        holder.text.setText(objects.get(position));

        return convertView;
    }

    public static class ViewHolder {
        TextView text;
        ImageView image;
        LinearLayout drawerBackGround;
    }
}
