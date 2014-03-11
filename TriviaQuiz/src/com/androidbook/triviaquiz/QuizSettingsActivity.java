package com.androidbook.triviaquiz;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class QuizSettingsActivity extends Activity {
	SharedPreferences mGameSettings;
//	public static final String GAME_PREFERENCES = "GamePrefs";
//	public static final String GAME_PREFERENCES_NICKNAME = "Nickname";
//	public static final String GAME_PREFERENCES_EMAIL = "Email";
//	public static final String GAME_PREFERENCES_PASSWORD = "Password";
//	public static final String GAME_PREFERENCES_DOB = "DOB";
//	public static final String GAME_PREFERENCES_GENDER = "Gender";
	static final int DATE_DIALOG_ID = 0;
	static final int PASSWORD_DIALOG_ID = 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
        // Retrieve the shared preferences
        mGameSettings = getSharedPreferences(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES, Context.MODE_PRIVATE);
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
        Log.d(DEBUG_TAG, "Nickname is: " + mGameSettings.getString(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_NICKNAME, "Not set"));
        Log.d(DEBUG_TAG, "Email is: " + mGameSettings.getString(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_EMAIL, "Not set"));
        Log.d(DEBUG_TAG, "Gender (M=1, F=2, U=0) is: " + mGameSettings.getInt(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_GENDER, 0));
        // We are not saving the password yet
        Log.d(DEBUG_TAG, "Password is: " + mGameSettings.getString(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_PASSWORD, "Not set"));
        // We are not saving the date of birth yet
        Log.d(DEBUG_TAG, "DOB is: "
                + DateFormat.format("MMMM dd, yyyy", mGameSettings.getLong(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_DOB, 0)));
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
        if (mGameSettings.contains(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_NICKNAME)) {
            nicknameText.setText(mGameSettings.getString(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_NICKNAME, ""));
        }
		nicknameText.setOnKeyListener(new View.OnKeyListener(){
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
					(keyCode == KeyEvent.KEYCODE_ENTER)){
				String strNicknameToSave = nicknameText.getText().toString();
				Editor editor = mGameSettings.edit();
                editor.putString(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_NICKNAME, strNicknameToSave);
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
        if (mGameSettings.contains(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_EMAIL)) {
            emailText.setText(mGameSettings.getString(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_EMAIL, ""));
        }
        emailText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Editor editor = mGameSettings.edit();
                    editor.putString(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_EMAIL, emailText.getText().toString());
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
        if (mGameSettings.contains(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_DOB)) {
            dobInfo.setText(DateFormat.format("MMMM dd, yyyy", mGameSettings.getLong(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_DOB, 0)));
        } else {
            dobInfo.setText(R.string.settings_dob_not_set);
        }
        
		Button pickDate = (Button) findViewById(R.id.Button_DOB);
		pickDate.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				//ToDo: Handle onClick
				//Toast.makeText(QuizSettingsActivity.this, "Date Picker", Toast.LENGTH_LONG).show();
				showDialog(DATE_DIALOG_ID);
			}
		}); //pickDate.SetOnClickListener
	} //private void initDAtePicker
	
	@Override
	protected Dialog onCreateDialog(int id){
		switch (id) {
		case DATE_DIALOG_ID:
			DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
					Time dateOfBirth = new Time();
					dateOfBirth.set(dayOfMonth, monthOfYear, year);
					long dtDOB = dateOfBirth.toMillis(true);
					//dob.setText(DateFormat.format("MMMM dd, yyyy",dtDOB));
					Editor editor = mGameSettings.edit();
					editor.putLong(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_DOB, dtDOB);
					editor.commit();
						} //public void
					},0,0,0);
			return dateDialog;
		case PASSWORD_DIALOG_ID:
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rootView = getWindow().getDecorView().getRootView();
			final View layout = inflater.inflate(R.layout.password_dialog, (ViewGroup) null);
            final EditText p1 = (EditText) layout.findViewById(R.id.EditText_Pwd1);
            final EditText p2 = (EditText) layout.findViewById(R.id.EditText_Pwd2);
            final TextView error = (TextView) layout.findViewById(R.id.TextView_PwdProblem);
            p2.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    String strPass1 = p1.getText().toString();
                    String strPass2 = p2.getText().toString();
                    if (strPass1.equals(strPass2)) {
                        error.setText(R.string.settings_pwd_equal);
                    } else {
                        error.setText(R.string.settings_pwd_not_equal);
                    }
                }

                // ... other required overrides do nothing
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(layout);
            // Now configure the AlertDialog
            builder.setTitle(R.string.settings_button_pwd);
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // We forcefully dismiss and remove the Dialog, so it
                    // cannot be used again (no cached info)
                    QuizSettingsActivity.this.removeDialog(PASSWORD_DIALOG_ID);
                }
            });
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    TextView passwordInfo = (TextView) findViewById(R.id.TextView_Password_Info);
                    String strPassword1 = p1.getText().toString();
                    String strPassword2 = p2.getText().toString();
                    if (strPassword1.equals(strPassword2)) {
                        Editor editor = mGameSettings.edit();
                        editor.putString(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_PASSWORD, strPassword1);
                        editor.commit();
                        passwordInfo.setText(R.string.settings_pwd_set);
                    } else {
                        Log.d("Quiz Activity", "Passwords do not match. Not saving. Keeping old password (if set).");
                    }
                    // We forcefully dismiss and remove the Dialog, so it
                    // cannot be used again
                    QuizSettingsActivity.this.removeDialog(PASSWORD_DIALOG_ID);
                }
            });
            // Create the AlertDialog and return it
            AlertDialog passwordDialog = builder.create();
            return passwordDialog;
		} //switch
	return null;
	} //protected Dialog onCreate
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		switch (id) {
		case DATE_DIALOG_ID:
			DatePickerDialog dateDialog = (DatePickerDialog) dialog;
			int iDay, iMonth, iYear;
			if (mGameSettings.contains(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_DOB)){
				long msBirthDate = mGameSettings.getLong(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_DOB, 0);
				Time dateOfBirth = new Time();
				dateOfBirth.set(msBirthDate);
				iDay = dateOfBirth.monthDay;
				iMonth = dateOfBirth.month;
				iYear = dateOfBirth.year;
			} else {
				Calendar cal = Calendar.getInstance();
				iDay = cal.get(Calendar.DAY_OF_MONTH);
				iMonth = cal.get(Calendar.MONTH);
				iYear = cal.get(Calendar.YEAR);
			} //if (mGameSettings
			dateDialog.updateDate(iYear, iMonth, iDay);
			return;
		} // switch id
	} // protected void
	
	private void initPasswordChooser(){
        // Set password info
        TextView passwordInfo = (TextView) findViewById(R.id.TextView_Password_Info);
        if (mGameSettings.contains(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_PASSWORD)) {
            passwordInfo.setText(R.string.settings_pwd_set);
        } else {
            passwordInfo.setText(R.string.settings_pwd_not_set);
        }
		Button setPassword = (Button) findViewById(R.id.Button_Password);
		setPassword.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				showDialog(PASSWORD_DIALOG_ID);
				//ToDo: Handle onClick
				//Toast.makeText(QuizSettingsActivity.this, "Password", Toast.LENGTH_LONG).show();
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
        if (mGameSettings.contains(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_GENDER)) {
            spinner.setSelection(mGameSettings.getInt(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_GENDER, 0));
        }		
		// Handle spinner selections
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition,
                    long selectedId) {
               Editor editor = mGameSettings.edit();
               editor.putInt(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_GENDER, selectedItemPosition);
               editor.commit();
            	Toast.makeText(QuizSettingsActivity.this, itemSelected.toString(), Toast.LENGTH_LONG).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

	}
}
