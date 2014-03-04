package com.androidbook.triviaquiz;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class QuizSettingsActivity extends Activity {
	SharedPreferences mGameSettings;
	public static final String GAME_PREFERENCES = "GamePrefs";
	public static final String GAME_PREFERENCES_NICKNAME = "Nickname";
	public static final String GAME_PREFERENCES_EMAIL = "Email";
	public static final String GAME_PREFERENCES_PASSWORD = "Password";
	public static final String GAME_PREFERENCES_DOB = "DOB";
	public static final String GAME_PREFERENCES_GENDER = "Gender";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
        // Retrieve the shared preferences
        mGameSettings = getSharedPreferences(GAME_PREFERENCES, Context.MODE_PRIVATE);
        // Initialize the nickname entry
        initNicknameEntry();
        // Initialize the email entry
        initEmailEntry();
        // Initialize the Password chooser
        initPasswordChooser();
        // Initialize the Date picker
        initDatePicker();
        // Initialize the spinner
        initGenderSpinner();
		
	}

    @Override
    protected void onDestroy() {
    	String DEBUG_TAG = "Quiz Activity";
    	
        Log.d(DEBUG_TAG, "SHARED PREFERENCES");
        Log.d(DEBUG_TAG, "Nickname is: " + mGameSettings.getString(GAME_PREFERENCES_NICKNAME, "Not set"));
        Log.d(DEBUG_TAG, "Email is: " + mGameSettings.getString(GAME_PREFERENCES_EMAIL, "Not set"));
        Log.d(DEBUG_TAG, "Gender (M=1, F=2, U=0) is: " + mGameSettings.getInt(GAME_PREFERENCES_GENDER, 0));
        // We are not saving the password yet
        Log.d(DEBUG_TAG, "Password is: " + mGameSettings.getString(GAME_PREFERENCES_PASSWORD, "Not set"));
        // We are not saving the date of birth yet
        Log.d(DEBUG_TAG, "DOB is: "
                + DateFormat.format("MMMM dd, yyyy", mGameSettings.getLong(GAME_PREFERENCES_DOB, 0)));
        super.onDestroy();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz_settings, menu);
		return true;
	}
	
	private void initNicknameEntry(){
		final EditText nicknameText = (EditText) findViewById(R.id.EditText_NickName);
        if (mGameSettings.contains(GAME_PREFERENCES_NICKNAME)) {
            nicknameText.setText(mGameSettings.getString(GAME_PREFERENCES_NICKNAME, ""));
        }
		nicknameText.setOnKeyListener(new View.OnKeyListener(){
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
					(keyCode == KeyEvent.KEYCODE_ENTER)){
				String strNicknameToSave = nicknameText.getText().toString();
				Editor editor = mGameSettings.edit();
                editor.putString(GAME_PREFERENCES_NICKNAME, strNicknameToSave);
                editor.commit();
                return true;
		       }
			return false;
		}
		});
	}
	
    /**
     * Initialize the email entry
     */
    private void initEmailEntry() {
        // Save Email
        final EditText emailText = (EditText) findViewById(R.id.EditText_Email);
        if (mGameSettings.contains(GAME_PREFERENCES_EMAIL)) {
            emailText.setText(mGameSettings.getString(GAME_PREFERENCES_EMAIL, ""));
        }
        emailText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Editor editor = mGameSettings.edit();
                    editor.putString(GAME_PREFERENCES_EMAIL, emailText.getText().toString());
                    editor.commit();
                    return true;
                }
                return false;
            }
        });
    }
    
	private void initDatePicker(){
        // Set password info
        TextView dobInfo = (TextView) findViewById(R.id.TextView_DOB_Info);
        if (mGameSettings.contains(GAME_PREFERENCES_DOB)) {
            dobInfo.setText(DateFormat.format("MMMM dd, yyyy", mGameSettings.getLong(GAME_PREFERENCES_DOB, 0)));
        } else {
            dobInfo.setText(R.string.settings_dob_not_set);
        }
        
		Button pickDate = (Button) findViewById(R.id.Button_DOB);
		pickDate.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				//ToDo: Handle onClick
				Toast.makeText(QuizSettingsActivity.this, "Date Picker", Toast.LENGTH_LONG).show();
			}
		}); //pickDate.SetOnClickListener
	} //private void initDAtePicker
	
	private void initPasswordChooser(){
		Button setPassword = (Button) findViewById(R.id.Button_Password);
		setPassword.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				//ToDo: Handle onClick
				Toast.makeText(QuizSettingsActivity.this, "Password", Toast.LENGTH_LONG).show();
			}
		}); //pickDate.SetOnClickListener
	} //private void initDAtePicker
	
	private void initGenderSpinner(){
		final Spinner spinner = (Spinner) findViewById(R.id.Spinner_Gender);
		
		ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, 
				R.array.genders, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(0);
        if (mGameSettings.contains(GAME_PREFERENCES_GENDER)) {
            spinner.setSelection(mGameSettings.getInt(GAME_PREFERENCES_GENDER, 0));
        }		
		// Handle spinner selections
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition,
                    long selectedId) {
               Editor editor = mGameSettings.edit();
               editor.putInt(GAME_PREFERENCES_GENDER, selectedItemPosition);
               editor.commit();
            	Toast.makeText(QuizSettingsActivity.this, itemSelected.toString(), Toast.LENGTH_LONG).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

	}
}
