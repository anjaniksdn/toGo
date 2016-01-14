package com.smartdatainc.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.smartdatainc.dataobject.AppInstance;
import com.smartdatainc.helpers.TransparentProgressDialog;
import com.smartdatainc.toGo.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Created by Anurag Sethi
 * This class is used to define the common functions to be used in the application
 */
public class Utility {

    Context context;
    TransparentProgressDialog progressDialogObj;

    /**
     * Constructor
     * @param contextObj  The Context from where the method is called
     * @return none
     */

    public Utility(Context contextObj) {
        context = contextObj;
    }


    /**
     * The method validates email address
     * @param email email address to validate
     * @return true if the email entered is valid
     */
    public boolean checkEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        //final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        // final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final String EMAIL_PATTERN = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputstr = email;
        pattern = Pattern.compile(EMAIL_PATTERN,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(inputstr);
        return matcher.matches();
    }

    /**
     * This method will convert object to Json String
     * @param obj Object whose data needs to be converted into JSON String
     * @return object data in JSONString
     */
    public String convertObjectToJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }


    /**
     * The method will start the loader till the AsynTask completes the assigned task
     * @since 2014-08-20
     * @param context The Context from where the method is called
     * @param image_for_rotation_resource_id resourceID of the image to be displayed as loader
     * @return none
     */
    public void startLoader(Context context, int image_for_rotation_resource_id) {
        progressDialogObj = new TransparentProgressDialog(this.context, image_for_rotation_resource_id);
        AppInstance.logObj.printLog("startLoader=" + progressDialogObj);
        progressDialogObj.show();
    }

    /**
     * The method will start the loader till the AsynTask completes the assigned task
     * @since 2014-08-20
     * @return none
     */
    public void stopLoader() {
        AppInstance.logObj.printLog("stopLoader=" + progressDialogObj);
        progressDialogObj.dismiss();
    }


    /**
     * The method will create an alertDialog box
     * @param context The Context from where the method is called
     * @param title  of the dialog box
     * @param msgToShow message to be shown in the dialog box
     * @param positiveBtnText text to be shown in the positive button
     * @param negativeBtnText text to be shown in the negative button
     * @param btnTagName to differentiate the action on the displayed activity
     * @param data to be posted on click of a button
     * @return none
     */
    public void showAlertDialog(final Context context, String title, String msgToShow, String positiveBtnText, String negativeBtnText, final String btnTagName, final int data) {

        AlertDialog.Builder dialogObj = new AlertDialog.Builder(context);
        dialogObj.setIcon(android.R.drawable.ic_dialog_alert);
        dialogObj.setTitle(title);
        dialogObj.setMessage(msgToShow);
        dialogObj.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // TODO Auto-generated method stub
                // Show location settings when the user acknowledges the alert dialog
                if(btnTagName.equalsIgnoreCase("network services")) {
                    Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                    context.startActivity(intent);
                }

                if(btnTagName.equalsIgnoreCase("location services")) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }

            }

        });

        if(negativeBtnText != null) {
            dialogObj.setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                }
            });
        }

        dialogObj.show();

    }

    /**
     * The method will return the date and time in requested format
     * @param selectedDateTime to be converted to requested format
     * @param requestedFormat  the format in which the provided datetime needs to be changed
     * @param formatString differentiate parameter to format date or time
     * @return formated date or time
     */
    public String formatDateTime(String selectedDateTime, String requestedFormat, String formatString) {

        if(formatString.equalsIgnoreCase("time")) {
            SimpleDateFormat ft = new SimpleDateFormat ("hh:mm");
            Date dateObj = null;

            try {
                dateObj = ft.parse(selectedDateTime);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            long millis = dateObj.getTime();
            SimpleDateFormat simpleDateFormatObj = new SimpleDateFormat (requestedFormat);
            return simpleDateFormatObj.format(millis);

        }//if
        else if(formatString.equalsIgnoreCase("date")) {
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-mm-dd");
            Date dateObj = null;

            try {
                dateObj = ft.parse(selectedDateTime);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            SimpleDateFormat simpleDateFormatObj = new SimpleDateFormat (requestedFormat);
            return simpleDateFormatObj.format(dateObj);


        }
        return null;

    }

    /**
     * The method will return current time
     * @return current time
     */
    public String getCurrentTime() {
        String am_pm = "";
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        if(minutes < 12) {
            String modifiedMinutes = "0" + minutes;
            minutes = Integer.parseInt(modifiedMinutes);
        }
        AppInstance.logObj.printLog("minutes=" + minutes);

        if(c.get(Calendar.AM_PM) == Calendar.AM) {
            am_pm = "AM";
        }
        else if(c.get(Calendar.AM_PM) == Calendar.PM) {
            am_pm = "PM";
        }

        String currentTime = Integer.toString(hour) + ":" + Integer.toString(minutes) + " " + am_pm;
        return currentTime;
    }

    /**
     * The method will return current date
     * @return current date
     */
    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        String currentDate = Integer.toString(day) + "-" + Integer.toString(month) + "-" + Integer.toString(year);
        return currentDate;
    }

    /**
     * The method show the error
     * @param contextObj context of the activity from where the method is called
     * @param message message to be displayed
     * @param textViewObj object of the textView where the error message will be shown
     * @param editTextObj object of the editText containing the error
     */
    public void showError(Context contextObj, String message, TextView textViewObj, EditText editTextObj) {
        if(message == null || message.equalsIgnoreCase("")) {
            textViewObj.setVisibility(View.GONE);
        }
        else {
            textViewObj.setVisibility(View.VISIBLE);
            textViewObj.setText(message);
            textViewObj.setTextColor(contextObj.getResources().getColor(R.color.error_text_color));
        }

    }

    /**
     * This method will convert json String to ArrayList
     * @since 2014-08-13
     * @param jsonString string which needs to be converted to ArrayList
     * @return ArrayList of type String
     * @throws JSONException
     */
    private ArrayList<String> convertJsonStringToArray(String jsonString) throws JSONException {

        ArrayList<String> stringArray = new ArrayList<String>();

        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray.add(jsonArray.getString(i));
        }

        return stringArray;
    }


    /**
     * The method will save the data in shared preferences defined by "sharedPrefName" and the key provided by "key" parameter
     * @since 2014-08-13
     * @param sharedPrefName name of the container
     * @param mode private
     * @param key name of the key in which values are saved
     * @param value data to be saved associated to the particular key
     * @return none
     */

    public void saveDataInSharedPreferences(String sharedPrefName, int mode, String key, String value){
        SharedPreferences prefsObj = context.getSharedPreferences(sharedPrefName, mode);
        SharedPreferences.Editor editorObj = prefsObj.edit();
        editorObj.putString(key, value);
        editorObj.commit();
    }

    /**
     * The method will read the data in shared preferences defined by "sharedPrefName" and the key provided by "key" parameter
     * @param sharedPrefName name of the container
     * @param mode private
     * @param key name of the key in which values are saved
     * @return String
     */

    public String readDataInSharedPreferences(String sharedPrefName, int mode, String key) {
        SharedPreferences prefsObj = context.getSharedPreferences(sharedPrefName, mode);
        return prefsObj.getString(key, "");
    }

    /**
     * The method will remove the data stored in shared preferences defined by "sharedPrefName" and the key provided by "key" parameter
     * @param sharedPrefName name of the container
     * @param mode private
     * @param key name of the key in which values are saved
     * @param removeAll if true will remove all the values stored in shared preferences else remove as specified by key
     */
    public void removeKeyFromSharedPreferences(String sharedPrefName, int mode, String key, boolean removeAll) {

        SharedPreferences prefsObj = context.getSharedPreferences(sharedPrefName, mode);
        SharedPreferences.Editor editorObj = prefsObj.edit();
        if(removeAll) {
            editorObj.clear();
        }
        else {
            editorObj.remove(key);
        }
        editorObj.commit();
    }

    /**
     * show message to user using showToast
     *
     * @param mContext contains context of application
     * @param message  contains text/message to show
     * @param durationForMessageToAppear 1 will show the message for short duration else long duration
     */
    public void showToast(Context mContext, String message, int durationForMessageToAppear) {
        if(durationForMessageToAppear == 1) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * This method will hide virtual keyboard if opened
     *
     * @param mContext contains context of application
     */
    public void hideVirtualKeyboard(Context mContext) {

        try {

            IBinder binder = ((Activity) mContext).getWindow().getCurrentFocus()
                    .getWindowToken();

            if (binder != null) {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binder, 0);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    /**
     * This method will show virtual keyboard where ever required
     *
     * @param mContext contains context of application
     */
    public void showVirtualKeyboard(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Format value up to 2 decimal places
     * @param num - number to be formatted
     */
    public float formatValueUpTo2Decimals(double num) {

        try {

            DecimalFormat df = new DecimalFormat("#.##");

            String value = df.format(num);
            String decimalPlace = ",";

            if (value.contains(decimalPlace)) {
                value = value.replace(decimalPlace, ".");
            }

            return Float.parseFloat(value);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return (float) num;

    }

    /**
     * The method validates if GooglePlayServices are available or not
     * @param context - contains the context of the activity from where it is called
     * @return true if GooglePlayServices exists else false
     */

    public boolean validateGooglePlayServices(Context context) {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity)context,
                        Constants.GooglePlayService.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                AppInstance.logObj.printLog(context.getResources().getString(R.string.device_not_supported));

            }
            return false;
        }
        return true;

    }

    private static StyleSpan sBoldSpan = new StyleSpan(Typeface.BOLD);

    /**
     * Method to check the facebook login instance
     * return the status of access token
     */
 /*   public static boolean isLoggedInWithFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }*/

    /**
     * Populate the given {@link TextView} with the requested text, formatting
     * through {@link Html#fromHtml(String)} when applicable. Also sets
     * {@link TextView#setMovementMethod} so inline links are handled.
     */
    public static void setTextMaybeHtml(TextView view, String text) {
        if (TextUtils.isEmpty(text)) {
            view.setText("");
            return;
        }
        if (text.contains("<") && text.contains(">")) {
            view.setText(Html.fromHtml(text));
            view.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            view.setText(text);
        }
    }


    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }


    /**
     * Given a snippet string with matching segments surrounded by curly
     * braces, turn those areas into bold spans, removing the curly braces.
     */
    public static Spannable buildStyledSnippet(String snippet) {
        final SpannableStringBuilder builder = new SpannableStringBuilder(snippet);

        // Walk through string, inserting bold snippet spans
        int startIndex = -1, endIndex = -1, delta = 0;
        while ((startIndex = snippet.indexOf('{', endIndex)) != -1) {
            endIndex = snippet.indexOf('}', startIndex);

            // Remove braces from both sides
            builder.delete(startIndex - delta, startIndex - delta + 1);
            builder.delete(endIndex - delta - 1, endIndex - delta);

            // Insert bold style
            builder.setSpan(sBoldSpan, startIndex - delta, endIndex - delta - 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            delta += 2;
        }

        return builder;
    }

    private static final int BRIGHTNESS_THRESHOLD = 130;

    /**
     * Calculate whether a color is light or dark, based on a commonly known
     * brightness formula.
     *
     * @see {@literal http://en.wikipedia.org/wiki/HSV_color_space%23Lightness}
     */
    public static boolean isColorDark(int color) {
        return ((30 * Color.red(color) +
                59 * Color.green(color) +
                11 * Color.blue(color)) / 100) <= BRIGHTNESS_THRESHOLD;
    }

    public static boolean isHoneycomb() {
        // Can use static final constants like HONEYCOMB, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= 11;//Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isHoneycombTablet(Context context) {
        return isHoneycomb() && isTablet(context);
    }

    public static boolean isAmazonDevice() {
        return Build.MANUFACTURER.equalsIgnoreCase("Amazon");
    }

    public static long getCurrentTime(final Context context) {
        return System.currentTimeMillis();
    }

    public static Drawable getIconForIntent(final Context context, Intent i) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);
        if (infos.size() > 0) {
            return infos.get(0).loadIcon(pm);
        }
        return null;
    }

    /**
     * Method to set the custom font to supplied {@link View} with appropriate attribute set.
     */
    public static void setCustomFont(View textViewOrButton, Context ctx, AttributeSet attrs,
                                     int[] attributeSet, int fontId) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, attributeSet);
        String customFont = a.getString(fontId);
        setCustomFont(textViewOrButton, ctx, customFont);
        a.recycle();
    }

    /**
     * Method to set the custom font to supplied {@link View} with appropriate attribute set.
     */
    public static boolean setCustomFont(View textViewOrButton, Context ctx, String asset) {
        if (TextUtils.isEmpty(asset)) {
            return false;
        }
        Typeface tf = null;
        try {
            tf = getFont(ctx, asset);
            if (textViewOrButton instanceof TextView) {
                ((TextView) textViewOrButton).setTypeface(tf);
            } else if (textViewOrButton instanceof Button) {
                ((Button) textViewOrButton).setTypeface(tf);
            } else if (textViewOrButton instanceof EditText) {
                ((EditText) textViewOrButton).setTypeface(tf);
            }
        } catch (Exception e) {
            Log.e("UiUtils", "Could not get typeface: " + asset, e);
            return false;
        }

        return true;
    }

    private static final Hashtable<String, SoftReference<Typeface>> fontCache = new Hashtable<String, SoftReference<Typeface>>();

    /**
     * Method to get the custom font from cache if exists or create new instance and
     * cache the newly created font to future use.
     */
    public static Typeface getFont(Context c, String name) {
        synchronized (fontCache) {
            if (fontCache.get(name) != null) {
                SoftReference<Typeface> ref = fontCache.get(name);
                if (ref.get() != null) {
                    return ref.get();
                }
            }

            Typeface typeface = Typeface.createFromAsset(
                    c.getAssets(),
                    "fonts/" + name
            );
            fontCache.put(name, new SoftReference<Typeface>(typeface));

            return typeface;
        }
    }

    /**
     * Convert the price value to $##.## format.
     *
     * @param originalValue actual price value that needs to converted in above
     *                      mentioned format.
     * @param roundChars    digits after decimal.
     * @return formatted price string.
     */
    public static String getPreetyPrice(String originalValue, final int roundChars) {
        if (TextUtils.isEmpty(originalValue)) {
            return "$0.00";
        } else {
            try {
                originalValue = originalValue.replace('$', ' ').trim();
                originalValue = originalValue.replace(",", "").trim();
                DecimalFormat twoDForm = new DecimalFormat("#.00");
                twoDForm.setMinimumIntegerDigits(1);
                String doubleVal = twoDForm.format(Double.parseDouble(originalValue));
                double val = Double.valueOf(doubleVal);
                if (val <= 0) {
                    originalValue = "$0.00";
                } else {
                    return "$" + doubleVal;
                }
            } catch (Exception e) {
                originalValue = "$0.00";
            }
        }
        return originalValue;
    }

    /**
     * Method to hide the virtual keyboard with respect to the {@link EditText}.
     */
    public static void hideKeyboard(final Context context, final EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    /**
     * Call this method to unbind the drawable attached with the given View.
     */
    public static void unbindDrawables(View view) {
        if (view != null) {
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }
            if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); ++i) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
                ((ViewGroup) view).removeAllViews();
            }
        }
    }

    /**
     * Method to get the screen orientation.
     */
    public static int getScreenOrientation(Activity activity) {
        Display getOrient = activity.getWindowManager().getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if (getOrient.getWidth() == getOrient.getHeight()) {
            orientation = Configuration.ORIENTATION_SQUARE;
        } else {
            if (getOrient.getWidth() < getOrient.getHeight()) {
                orientation = Configuration.ORIENTATION_PORTRAIT;
            } else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }

    /**
     * A method to check Token is of four digits or not. *
     */
    public static boolean isValidTokenCode(String zipCode) {
        return zipCode.matches("\\d{4}?");
    }

    /**
     * Given a snippet string with matching segments surrounded by angle
     * braces, turn those areas into bold spans, removing the angle braces.
     */
    public static Spanned buildDecorativeSnippet(String snippet, String decorations) {
        final SpannableStringBuilder builder = new SpannableStringBuilder(snippet);
        if (TextUtils.isEmpty(snippet)) {
            return builder;
        }
        if (!TextUtils.isEmpty(decorations)) {
            decorations = decorations.replace("[", "").replace("]", "");
            if (!TextUtils.isEmpty(decorations)) {
                List<String> array = Arrays.asList(decorations.split(","));
                int size = array.size();
                for (int i = 0; i < size; ++i) {
                    String str = array.get(i);
                    // take following special approach to make $ sign bold.
                    if (str.contains("$")) {
                        // Check for dollar sign presence.
                        int dollarIndex = str.indexOf('$');
                        if (dollarIndex != -1) {
                            StringBuilder stringBuilder = new StringBuilder(str);
                            stringBuilder.insert(dollarIndex, "\\");
                            str = stringBuilder.toString();
                        }
                    }
                    snippet = snippet.replaceAll(str, "<b>" + str + "</b>");
                }
            }
        }
        return Html.fromHtml(snippet);
    }

    /**
     * A method to convert String in to String ArrayList. *
     */
    public static ArrayList<String> stringToArrayList(String data) {
        List<String> array = new ArrayList<String>();
        if (!TextUtils.isEmpty(data)) {
            data = data.replace("[", "").replace("]", "");
            if (!TextUtils.isEmpty(data)) {
                array = Arrays.asList(data.split(", "));
            }
        }
        return new ArrayList<String>(array);
    }

    /**
     * So if you want something unique to the device itself, TM.getDeviceId()
     * should be sufficient. But it might be useful to hash 1 or more of these
     * identifiers, so that the string is still virtually unique to the device,
     * but does not explicitly identify the user's actual device. For example,
     * using String.hashCode(), combined with a UUID.
     *
     * @param context
     * @return String.hashCode() combined with a UUID.
     */
    public static String getDeviceUniqueId(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(
                context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

    /**
     * To check if device is having large screen.
     */
    public static boolean isLargeScreenDevice(Resources resource) {
        int screenSize = resource.getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE
                || screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method to calculate the {@link TimeZone} offset.
     */
    public static double getUTCTimeOffset() {
        TimeZone tz = TimeZone.getDefault();
        Date now = new Date();
        double offsetFromUtc = tz.getOffset(now.getTime()) / 1000;
        offsetFromUtc = (double) offsetFromUtc / (double) 60;
        offsetFromUtc = (double) offsetFromUtc / (double) 60;
        return offsetFromUtc;
    }

    /**
     * Method to get the {@link Drawable} instance to set as list row background
     * gradient color.
     */
    public static Drawable getListRowBgGradientDrawable(final View view) {
        ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient lg = new LinearGradient(0, 0, 0,
                        view.getHeight(),
                        new int[]{
                                Color.parseColor("#F4F5F5"),
                                Color.parseColor("#F1F1F2"),
                                Color.parseColor("#EDEEEF"),
                                Color.parseColor("#EBECED")},
                        new float[]{
                                0, 0.35f, 0.75f, 1},
                        Shader.TileMode.REPEAT);
                return lg;
            }
        };
        PaintDrawable p = new PaintDrawable();
        p.setShape(new RectShape());
        p.setShaderFactory(sf);
        p.setCornerRadius(1.0f);
        return p;
    }

    /**
     * Method to get the {@link Drawable} instance to set as list selected row background
     * gradient color.
     */
    public static Drawable getListSelectedRowBgGradientDrawable(final View view) {
        ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient lg = new LinearGradient(0, 0, 0,
                        view.getHeight(),
                        new int[]{
                                Color.parseColor("#E3E4E5"),
                                Color.parseColor("#DADBDD"),
                                Color.parseColor("#D0D1D3"),
                                Color.parseColor("#CBCCCE")},
                        new float[]{
                                0, 0.35f, 0.75f, 1},
                        Shader.TileMode.REPEAT);
                return lg;
            }
        };
        PaintDrawable p = new PaintDrawable();
        p.setShape(new RectShape());
        p.setShaderFactory(sf);
        p.setCornerRadius(1.0f);
        return p;
    }

    /**
     * What is checksum or check digit or barcode validation? Ans: A check digit
     * is a form of redundancy check used for error detection, the decimal
     * equivalent of a binary checksum. It consists of a single digit computed
     * from the other digits in the message. With a check digit, one can detect
     * simple errors in the input of a series of digits, such as a single
     * mistyped digit or some permutations of two successive digits. for more
     * details: http://en.wikipedia.org/wiki/Check_digit
     */
    public static boolean isValidBarcode(String barcode) {
        // size of the barcode digits (possiblly 12 or 13)
        int size = barcode.length();
        // integer array creation, it will hold the individual barcode digits
        int barcodeDigits[] = new int[size];
        // loop index variable
        int i;
        // placing each barcode character from barcode string to this newly allocated integer array
        for (i = 0; i < size; i++) {
            barcodeDigits[i] = barcode.charAt(i) - 48; // - 48 means; ascii to integer conversion
        }
        // few required variables
        int evenSum = 0;
        int oddSum = 0;
        int totalSum = 0;
        int reminder = 0;
        int checkDigit = 0;

        // adding even digits after multiplying by 3
        for (i = size - 2; i >= 0; i -= 2) {
            evenSum = evenSum + barcodeDigits[i] * 3;
        }
        // adding odd digits
        for (i = size - 3; i >= 0; i -= 2) {
            oddSum = oddSum + barcodeDigits[i];
        }
        // further calculation for getting check digit
        totalSum = evenSum + oddSum;
        reminder = totalSum % 10;
        checkDigit = reminder != 0 ? (10 - reminder) : reminder;
        // return true if valid barcode
        if (checkDigit == barcodeDigits[size - 1]) {
            return true;
        }
        // return false if invalid barcode
        return false;
    }


    /**
     * Invokes to check network availability on device.
     *
     * @param context Application context
     * @return True - Yes network is available. <br>False - Network not available.
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();

        // Network is available then return true
        if (info != null && info.isAvailable()) {
            return true;
        }

        // network is not available then return false
        return false;
    }

    /**
     * This method will check the internet connection availability
     *
     * @return server up status.
     */
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    /**
     * Method to encode the last part of url which may contains the file name with space
     * or character encoded.
     */
    public static String getEncodedUrl(final String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        int index = url.lastIndexOf("/");
        if (index == -1) {
            return url;
        }
        String lastPart = url.substring(index + 1, url.length());
        String urlPart = url.substring(0, index + 1);
        try {
            lastPart = URLDecoder.decode(lastPart, "UTF-8");
            return urlPart + URLEncoder.encode(lastPart, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return url;
    }
}
