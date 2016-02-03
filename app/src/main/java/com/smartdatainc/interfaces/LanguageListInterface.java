package com.smartdatainc.interfaces;

import com.smartdatainc.dataobject.Language;

import java.util.ArrayList;

/**
 * Created by anjanikumar on 25/1/16.
 */
public interface LanguageListInterface {
    public void onLanguageRemove(int position);
    public void onLanguageSelectObj(ArrayList<Language> items);
    public void onLanguageSelect(ArrayList<String> items, ArrayList<String> itemImage);
}
