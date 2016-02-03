package com.smartdatainc.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartdatainc.dataobject.Media;
import com.smartdatainc.dataobject.SwipeRefresh;
import com.smartdatainc.dataobject.User;
import com.smartdatainc.dataobject.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anurag Sethi
 * The class is used for parsing the data received from the web service
 */
public class DataParser {


    /**
     * This method will parse the JSON String
     * @since 2014-08-20
     * @param data output received from the web service
     * @param parseDataFor parameter to differentiate, value of which key needs to be fetched
     * @return parsed String based on the key for which data needs to be fetched
     * @exception JSONException
     */

    public String parseJsonString(String data, int parseDataFor) {
        String parsedString = "";

        try {
            JSONObject jsonObj = new JSONObject(data);
            switch(parseDataFor) {
                case 1: //return messageId key data
                    parsedString = Integer.toString(jsonObj.getInt("messageId"));
                    break;
                case 2://return result key data
                    parsedString = jsonObj.getString("result");
                    break;
                case 3://return result key data
                    parsedString = jsonObj.getString("code");

                    break;
                case 4://return result key data
                    parsedString = jsonObj.getString("message");
                    break;
                case 5://return result key data
                    parsedString = jsonObj.getString("token");
                    break;
                case 6://return result key data
                    boolean completionstatus = jsonObj.optBoolean("completion");
                    if(completionstatus)
                    {
                        parsedString = "true";
                    }
                    else
                    {
                        parsedString = "false";
                    }
                    break;
                case 7://return result key data
                    String uid = jsonObj.optString("uid");
                    parsedString= uid;

                    break;



            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return parsedString;
    }

    public UserProfile parseUserProfile(String data, int parseDataFor) {
        UserProfile userProfile = new UserProfile();



        switch(parseDataFor) {
            case 1: //return messageId key data
                try {

                    JSONObject jsonObj = new JSONObject(data);
                    JSONObject jsonobj = jsonObj.getJSONObject("user");
                    String country = jsonobj.getString("email");
                    String email = jsonobj.getString("email");
                    String password = jsonobj.getString("password");
                    String phone_number = jsonobj.getString("phone_number");
                    String uid = jsonobj.getString("uid");
                    String address = jsonobj.getString("address");
                    String imageurl = jsonobj.getJSONObject("profile_img").getString("url");
                    String first_name = jsonobj.getJSONObject("name").getString("first_name");
                    String last_name = jsonobj.getJSONObject("name").getString("last_name");
                    userProfile.setAddress(address);
                    userProfile.setEmail(email);
                    userProfile.setImageurl(imageurl);
                    userProfile.setPhonenumber(phone_number);
                    userProfile.setPassword(password);
                    if(first_name!=null) {
                        userProfile.setName(first_name + " " + last_name);
                    }
                    // String country = jsonobj.getString("email");
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }

                //parsedString = Integer.toString(jsonObj.getInt("messageId"));
                break;


        }



        return userProfile;
    }

    /**
     * The method will be used to parse the data received from the web service
     * and will return the object of the required type
     *
     * @param jsonString the string data received from the web service
     * @param objName is the name of the dataobject for which the data is parsed     *
     * @return dataObject of the required type e.g. User class
     */
    public Object parseDataForObject(String jsonString, String objName) {
        Gson gson = new Gson();

        if(objName.equalsIgnoreCase("user")) {
            return gson.fromJson(jsonString, User.class);
        }

        if(objName.equalsIgnoreCase("mediaUrl")) {
            Type listType = new TypeToken<ArrayList<Media>>(){}.getType();
            return gson.fromJson(jsonString, listType);
        }
        if(objName.equalsIgnoreCase("swipeRefresh")) {
            return gson.fromJson(jsonString, SwipeRefresh.class);
        }
        return null;
    }

    /**
     * The method will be used to parse the data received from the web service
     * where the data return is in the form of jsonArray
     *
     * @param jsonStringArray the string data received from the web service in the form of jsonArray
     * @param typeOfDataList is the type of arrayList for which the data is parsed
     *
     * @return List of the parsed data
     */
    public List parseJsonArray(String jsonStringArray, Type typeOfDataList) {
        Gson gson = new Gson();

        return gson.fromJson(jsonStringArray, typeOfDataList);
    }
}
