package com.smartdatainc.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.smartdatainc.toGo.R;;

import java.util.ArrayList;

/**
 * Created by Anurag Sethi on 08-07-2015.
 */
public class ListAdapter extends ArrayAdapter<String> {

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
    public ListAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);

        this.objects = objects;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, parent, false);

            holder.text = (TextView) convertView.findViewById(R.id.mem_info_txt_id);
            holder.image = (ImageView) convertView.findViewById(R.id.mem_photo_img_id);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(objects.get(position));

        return convertView;
    }

    public static class ViewHolder {
        TextView text;
        ImageView image;
    }
}
