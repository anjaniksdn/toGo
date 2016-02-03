package com.smartdatainc.app;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import com.oovoo.core.LoggerListener.LogLevel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Hashtable;

public class ApplicationSettings extends Hashtable<String, String> {
//"12349983355077
	//"MDAxMDAxAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAZUYVW%2BB1MwyBDpt22C0WvOeMPW7fH6mMOv8d%2FAPeFZ2QeCOguU288bRzsChrixFyZ%2BKzm9nrLmfOkZwyPrAO%2BDP8wgDiVtL%2F0w9mZQ78Az5Hk6imDbhYGNGRFMqo0H2virlVE4Q%2Bpf5S%2Fm50MO%2BMh"
private static final String TOKEN = "MDAxMDAxAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAZUYVW%2BB1MwyBDpt22C0WvOeMPW7fH6mMOv8d%2FAPeFZ2QeCOguU288bRzsChrixFyZ%2BKzm9nrLmfOkZwyPrAO%2BDP8wgDiVtL%2F0w9mZQ78Az5Hk6imDbhYGNGRFMqo0H2virlVE4Q%2Bpf5S%2Fm50MO%2BMh";
	// Babul private static final String TOKEN = "MDAxMDAxAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACs5wargiqXmlwM3bTZvfNOocpZHMgFFy9TaEfqCu4GrTO7y6TKXQZXtPLNmO1fWi4w1oUzY5wcXlSuiLl5YHFJx2%2FZP6baqkSrDP5ywPkbVGsHlvRUHkLmE%2B6%2BeY4LVVAxLwTliCn%2FDCPSve1wV6hT";
	//private static final String TOKEN = "MDAxMDAxAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABmOAqTZF1VChoHNfkxcVGd2j9O0nO1GOt3ONvzZ5AMIwo%2FksmUL5lzYzt%2FRddEvi53U2yg%2FZqiH3XAhQFaBlN11HdwRojDwnDqSK%2BoRrcNMqweaZD9y4manGLi9StdzVI%3D";
	//private static final String TOKEN = "MDAxMDAxAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABNzmf8uyXrdfAAqDGXfbG12LYdamctujWj9mfuSLwwJbvdN%2Fga71LW1hcf%2FZk6pE76s9%2BILLpsabfcDTIzRAJJPDMp9uyd5AzV0PATwNjEYUauu8s5jW1IwcvofJDi7Ao%3D";
	// Put your application token here
	public static final String Token				  = "token";
	public static final String Username	          = "username";
	public static final String ResolutionLevel	      = "resolution_level";
	public static final String AvsSessionId	      = "avs_session_id";
	public static final String RandomAvsSessionId	  = "random_avs_session_id";
	public static final String AvsSessionDisplayName	= "avs_session_display_name";
	public static final String LogLevelKey			  = "log_level_key";
	public static final String UseCustomRender		  = "use_custom_render";
	public static final String SecurityState		  = "security_state";
	private static final long	serialVersionUID	  = 1L;
	public static final String TAG	                  = "ApplicationSettings";
	public static final String SENT_TOKEN_TO_SERVER  = "sentTokenToServer";
	public static final String REGISTRATION_COMPLETE = "registrationComplete";
	public static final String PREVIEW_ID			  ="12349983355077";
	//babul public static final String PREVIEW_ID			  = "12349983355392";
	//public static final String PREVIEW_ID			  = "12349983355075";
	private Context appcontext	          = null;

	public ApplicationSettings(Context appcontext) {
		this.appcontext = appcontext;
		load();

		if (get(ApplicationSettings.Token) == null) {
			put(ApplicationSettings.Token, TOKEN);
		}
		
		if (get(ApplicationSettings.LogLevelKey) == null) {
			put(ApplicationSettings.LogLevelKey, LogLevel.Debug.toString());
		}
	}

	public void load() {
		try {
			FileInputStream stream = appcontext.openFileInput("ApplicationSettings");
			JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(stream)));
			// String val = reader.toString();
			reader.beginObject();

			while (reader.hasNext()) {
				String key = reader.nextName();
				String value = reader.nextString();
				this.put(key, value);
				Log.d(TAG, "Settings [" + key + " = " + value + "]");
			}

			reader.endObject();

			reader.close();
			stream.close();

		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	public void save() {
		try {
			try {
				appcontext.deleteFile("ApplicationSettings");
			} catch (Exception err) {
			}

			FileOutputStream stream = appcontext.openFileOutput("ApplicationSettings", Context.MODE_PRIVATE);
			JsonWriter writer = new JsonWriter(new BufferedWriter(new OutputStreamWriter(stream)));
			writer.setIndent("  ");
			writer.beginObject();
			Enumeration<String> keys = this.keys();
			while (keys.hasMoreElements()) {
				String value = keys.nextElement();
				writer.name(value).value(get(value));
			}
			writer.endObject();
			stream.flush();
			writer.close();
			stream.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
	}
}
