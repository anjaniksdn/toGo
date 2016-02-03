package com.smartdatainc.dataobject;

/**
 * Created by anjanikumar on 7/1/16.
 */
public class Language {

    String id;
    String createdAt;
    String icon;
    String languageId;
    String language;


    String mySelectedlanuguageStatus;


    String mySelectedlanuguages;
    public String getMySelectedlanuguageStatus() {
        return mySelectedlanuguageStatus;
    }

    public void setMySelectedlanuguageStatus(String mySelectedlanuguageStatus) {
        this.mySelectedlanuguageStatus = mySelectedlanuguageStatus;
    }


    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getIcon() {
        return icon;
    }

    public String getLanguage() {
        return language;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    public String getMySelectedlanuguages() {
        return mySelectedlanuguages;
    }

    public void setMySelectedlanuguages(String mySelectedlanuguages) {
        this.mySelectedlanuguages = mySelectedlanuguages;
    }
}
