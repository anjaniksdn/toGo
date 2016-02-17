package com.smartdatainc.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smartdatainc.dataobject.ChatDetails;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.CircularImageView;

import java.util.ArrayList;

/**
 * Created by anjanikumar on 15/2/16.
 */
public class CustomerFeedbackAdapter extends ArrayAdapter<ChatDetails> {
    Context context;
    ArrayList<ChatDetails> chatDetailsList;
    private AQuery aq;
    public CustomerFeedbackAdapter(Context context, ArrayList<ChatDetails> chatDetailsList) {

        super(context, 0, chatDetailsList);
        this.context = context;
        this.chatDetailsList = chatDetailsList;
        aq = new AQuery(context);

    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

//return super.getView(position, convertView, parent);

        LayoutInflater inflater= ((Activity)context).getLayoutInflater();;
        View row=inflater.inflate(R.layout.customer_feedback_list_item, parent, false);
        TextView customername = (TextView)row.findViewById(R.id.customername);
        TextView customerfeedback = (TextView)row.findViewById(R.id.customerfeedback);
        RatingBar customerrating =(RatingBar)row.findViewById(R.id.customerrating);
        TextView pastdays =(TextView)row.findViewById(R.id.pastdays);
        ImageView customerimage = (ImageView)row.findViewById(R.id.customerimage);
        CircularImageView interpreterimage = (CircularImageView)row.findViewById(R.id.interpreterimage);


        customername.setText(chatDetailsList.get(position).getInterpreterNickname());
        customerfeedback.setText(chatDetailsList.get(position).getToLanguage());
        customerrating.setNumStars(3);

        pastdays.setText(chatDetailsList.get(position).getCallDuration());
        if(chatDetailsList.get(position).getFav().equalsIgnoreCase("true"))
        {
            customerimage.setBackgroundResource(R.drawable.fav_interpreter_icon);
        }else
        {
            customerimage.setBackgroundResource(R.drawable.profile_icon);
        }
        String url = chatDetailsList.get(position).getImageurl();
        if (url != null && url.length() > 0) {
            aq.id(R.id.interpreterimage).image(url);
        }

        return row;
    }
}
