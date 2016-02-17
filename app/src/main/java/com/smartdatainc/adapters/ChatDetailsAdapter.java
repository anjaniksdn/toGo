package com.smartdatainc.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smartdatainc.dataobject.ChatDetails;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.CircularNoBorderImageView;

import java.util.ArrayList;

/**
 * Created by anjanikumar on 12/2/16.
 */
public class ChatDetailsAdapter extends ArrayAdapter<ChatDetails> {
    Context context;
    ArrayList<ChatDetails> chatDetailsList;
    private AQuery aq;

    public ChatDetailsAdapter(Context context, ArrayList<ChatDetails> chatDetailsList) {

        super(context, 0, chatDetailsList);
        this.context = context;
        this.chatDetailsList = chatDetailsList;
        aq = new AQuery(context);

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

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        ;
        View row = inflater.inflate(R.layout.customer_call_feedback_item, parent, false);
        TextView interpreterNickname = (TextView) row.findViewById(R.id.interpreterNickname);
        TextView toLanguage = (TextView) row.findViewById(R.id.toLanguage);
        TextView fromLanguage = (TextView) row.findViewById(R.id.fromLanguage);
        TextView callDuration = (TextView) row.findViewById(R.id.callDuration);
        ImageView fav = (ImageView) row.findViewById(R.id.fav);
        CircularNoBorderImageView interpreterimage = (CircularNoBorderImageView) row.findViewById(R.id.interpreterimage);


        interpreterNickname.setText(chatDetailsList.get(position).getInterpreterNickname());
        toLanguage.setText(chatDetailsList.get(position).getToLanguage());
        fromLanguage.setText(chatDetailsList.get(position).getFromLanguage());
        callDuration.setText(chatDetailsList.get(position).getCallDuration()+"m");
       /* if(chatDetailsList.get(position).getFav().equalsIgnoreCase("true"))
        {
            fav.setBackgroundResource(R.drawable.fav_interpreter_icon);
        }else
        {
            fav.setBackgroundResource(R.drawable.profile_icon);
        }*/
        String url = chatDetailsList.get(position).getImageurl();
        if (url != null && url.length() > 0) {
            aq.id(interpreterimage).image(url);
        }
        else
        {
            aq.id(R.id.interpreterimage).image(R.drawable.default_pic);
        }

        return row;
    }
}
