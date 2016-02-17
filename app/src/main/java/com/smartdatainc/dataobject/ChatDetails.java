package com.smartdatainc.dataobject;

/**
 * Created by anjanikumar on 12/2/16.
 */
public class ChatDetails {


    String interpreterNickname;
    String callDuration;
    String toLanguage;
    String fromLanguage;
    String fav;
    String imageurl;


    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getInterpreterNickname() {
        return interpreterNickname;
    }

    public void setInterpreterNickname(String interpreterNickname) {
        this.interpreterNickname = interpreterNickname;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public String getToLanguage() {
        return toLanguage;
    }

    public void setToLanguage(String toLanguage) {
        this.toLanguage = toLanguage;
    }

    public String getFromLanguage() {
        return fromLanguage;
    }

    public void setFromLanguage(String fromLanguage) {
        this.fromLanguage = fromLanguage;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

}
