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
	public static final String GAME_PREFERENCES_AVATAR = "Avatar"; // String URL 
	
    public static final String GAME_PREFERENCES_SCORE = "Score"; // Integer
    public static final String GAME_PREFERENCES_CURRENT_QUESTION = "CurQuestion"; // Integer
    public static final String GAME_PREFERENCES_FAV_PLACE_NAME = "FavePlace";
    public static final String GAME_PREFERENCES_FAV_PLACE_LONG = "Long"; // Integer
    public static final String GAME_PREFERENCES_FAV_PLACE_LAT = "Lat";
 
    
    // XML Tag Names
    public static final String XML_TAG_QUESTION_BLOCK = "questions";
    public static final String XML_TAG_QUESTION = "question";
    public static final String XML_TAG_QUESTION_ATTRIBUTE_NUMBER = "number";
    public static final String XML_TAG_QUESTION_ATTRIBUTE_TEXT = "text";
    public static final String XML_TAG_QUESTION_ATTRIBUTE_IMAGEURL = "imageUrl";
    public static final int QUESTION_BATCH_SIZE = 15;

    public static final String DEBUG_TAG = "Trivia Quiz Log";
	
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
