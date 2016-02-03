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
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smartdatainc.dataobject.Language;
import com.smartdatainc.interfaces.ILanguageList;
import com.smartdatainc.interfaces.LanguageListInterface;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.CircularNoBorderImageView;
import com.smartdatainc.utils.Constants;

import java.util.ArrayList;

public class LanguageListAdapter extends ArrayAdapter<Language> implements ILanguageList {
    Context context;
   // ArrayList<String> languageList;
    //ArrayList<String> languageIconList;
    LanguageListInterface languageListInterface;
    private AQuery mAQuery;
    private ArrayList<Language> mSelectedLanguageList;

    public LanguageListAdapter(Context context, ArrayList<Language> mSelectedLanguageList, LanguageListInterface languageListInterface) {

        super(context, 0, mSelectedLanguageList);
        this.context = context;
        //this.languageList = languageList;
        this.mSelectedLanguageList = mSelectedLanguageList;
        this.languageListInterface = languageListInterface;
        mAQuery = new AQuery(context);
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

    public View getCustomView(final int position, View convertView, ViewGroup parent) {
// TODO Auto-generated method stub
//return super.getView(position, convertView, parent);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.language_list_item, parent, false);
        TextView textView = (TextView) row.findViewById(R.id.language_text_view);
        CircularNoBorderImageView imageView = (CircularNoBorderImageView) row.findViewById(R.id.language_image_view);
        ImageView closeImageView = (ImageView) row.findViewById(R.id.close_image_view);
        //Language language = languageList.get(position);
        textView.setText(mSelectedLanguageList.get(position).getLanguage());
        //imageView.setTag(position);
        String url = mSelectedLanguageList.get(position).getIcon();
        url = url.replace("<img src='", "");
        url = url.replace("'  />", "");
        mAQuery.id(imageView).image(Constants.WebServices.WS_BASE_URL + url);


        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mSelectedLanguageList.remove(position);
                languageListInterface.onLanguageRemove(position);
            }
        });
        return row;
    }

    @Override
    public ArrayList<Language> getLanguageList() {
        return mSelectedLanguageList;
    }
}
