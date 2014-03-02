package com.androidbook.triviaquiz;
import android.app.Activity;
import android.content.SharedPreferences;

public class QuizActivity extends Activity {
	public static final String GAME_PREFERENCES = "GamePrefs";

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
