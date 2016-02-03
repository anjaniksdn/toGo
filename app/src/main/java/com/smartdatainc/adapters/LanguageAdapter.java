package com.smartdatainc.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smartdatainc.dataobject.Language;
import com.smartdatainc.interfaces.LanguageListInterface;
import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anjanikumar on 7/1/16.
 */
public class LanguageAdapter extends ArrayAdapter<Language> {
    int groupid;
    Activity context;
    ArrayList<Language> list;
    LayoutInflater inflater;
    private AQuery mAQuery;
    boolean isCheck;
    LanguageListInterface languageListInterface;
    ArrayList<String> mSelectedItems = new ArrayList<String>();
    ArrayList<String> mSelectedItemImage = new ArrayList<String>();
    ArrayList<Language> selectedLanguageItems = new ArrayList<Language>();
    Map <String,Language> selectedLanguageItemsMap =  new HashMap<>();
    String mylanguage;
    public LanguageAdapter(Activity context, int id, ArrayList<Language>
            list,String mylanguage, boolean isCheck, LanguageListInterface languageListInterface) {
        super(context, id, list);
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid = groupid;
        this.isCheck = isCheck;
        this.mylanguage = mylanguage;
        this.languageListInterface = languageListInterface;
        mAQuery = new AQuery(context);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.multi_select_language_list_items, parent, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.language_icon_image_view);
        TextView textView = (TextView) itemView.findViewById(R.id.language_text_view);
        CheckBox languageCheckBox = (CheckBox) itemView.findViewById(R.id.select_language_check_box);
        final Language language = list.get(position);
        //String selectedlanguge = language.getMySelectedlanuguages();
        if(mylanguage!=null) {
            String selectedLangugeArray[] = mylanguage.split(",");
            if (selectedLangugeArray != null) {
                for (int l = 0; l < selectedLangugeArray.length; l++) {

                    if (selectedLangugeArray[l].equalsIgnoreCase(language.getLanguageId())) {
                        languageCheckBox.setChecked(true);
                        //sparseBooleanArray.put(position, true);

                        mSelectedItems.add(list.get(position).getLanguage());
                        mSelectedItemImage.add(list.get(position).getIcon());
                        selectedLanguageItems.add(list.get(position));
                        selectedLanguageItemsMap.put(list.get(position).getLanguage(), list.get(position));

                    } else {
                        selectedLanguageItems.remove(list.get(position));
                    }
                }
            }
        }
        textView.setText(language.getLanguage());
        String url = language.getIcon();
        url = url.replace("<img src='", "");
        url = url.replace("'  />", "");
        mAQuery.id(imageView).image(Constants.WebServices.WS_BASE_URL + url);
        if (isCheck) {
            languageCheckBox.setVisibility(View.VISIBLE);
        } else {
            languageCheckBox.setVisibility(View.INVISIBLE);
        }

        languageCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
                sparseBooleanArray.put(position, b);
                for (int i = 0; i < sparseBooleanArray.size(); i++) {
                    if (sparseBooleanArray.valueAt(i)) {
                        mSelectedItems.add(list.get(position).getLanguage());
                        mSelectedItemImage.add(list.get(position).getIcon());
                        selectedLanguageItemsMap.put(list.get(position).getLanguage(), list.get(position));
                        selectedLanguageItems.add(list.get(position));
                    }
                    else
                    {
                        selectedLanguageItems.remove(list.get(position));
                        selectedLanguageItemsMap.remove(list.get(position).getLanguage());
                    }
                }
                selectedLanguageItems = new ArrayList<Language>();
                for (Map.Entry<String,Language> entry : selectedLanguageItemsMap.entrySet()) {
                    String key = entry.getKey();
                    Language language = entry.getValue();
                    selectedLanguageItems.add(language);

                }
                languageListInterface.onLanguageSelectObj(selectedLanguageItems);
                languageListInterface.onLanguageSelect(mSelectedItems, mSelectedItemImage);
            }
        });

        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup
            parent) {
        return getView(position, convertView, parent);

    }
}