package com.androidbook.triviaquiz;
import android.app.Activity;
import android.content.SharedPreferences;

public class QuizActivity extends Activity {
	public static final String GAME_PREFERENCES = "GamePrefs";
	public static final String GAME_PREFERENCES_NICKNAME = "Nickname";
	public static final String GAME_PREFERENCES_EMAIL = "Email";
	public static final String GAME_PREFERENCES_PASSWORD = "Password";
	public static final String GAME_PREFERENCES_DOB = "DOB";
	public static final String GAME_PREFERENCES_GENDER = "Gender";
	
	protected void setPref(){
		SharedPreferences settings = getSharedPreferences(GAME_PREFERENCES, MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = settings.edit();
		prefEditor.putString("UserName", "janeDoe");
		prefEditor.putInt("userAge",  22);
		prefEditor.commit();
	}
	protected String getAppPreferences(){
		SharedPreferences settings = getSharedPreferences(GAME_PREFERENCES, MODE_PRIVATE);
		String user = "";
		if (settings.contains("UserName") == true) {
			user = settings.getString("UserName","Default");
		}
		return user;
	}
}
