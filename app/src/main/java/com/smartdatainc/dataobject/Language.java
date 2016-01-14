package com.smartdatainc.dataobject;

/**
 * Created by anjanikumar on 7/1/16.
 */
public class Language {


    String text;
    Integer imageId;

    public void setText(String text) {
        this.text = text;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }
    public String getText(){
        return text;
    }

    public Integer getImageId(){
        return imageId;
    }
}
