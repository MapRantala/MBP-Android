package com.androidbook.triviaquiz;

import java.io.File;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class QuizSettingsActivity extends Activity {
	SharedPreferences mGameSettings;
	GPSCoords mFavPlaceCoords;
	
//	public static final String GAME_PREFERENCES = "GamePrefs";
//	public static final String GAME_PREFERENCES_NICKNAME = "Nickname";
//	public static final String GAME_PREFERENCES_EMAIL = "Email";
//	public static final String GAME_PREFERENCES_PASSWORD = "Password";
//	public static final String GAME_PREFERENCES_DOB = "DOB";
//	public static final String GAME_PREFERENCES_GENDER = "Gender";
	static final int DATE_DIALOG_ID = 0;
	static final int PASSWORD_DIALOG_ID = 1;
	static final int PLACE_DIALOG_ID = 2;

	static final int TAKE_AVATAR_CAMERA_REQUEST = 1;
    static final int TAKE_AVATAR_GALLERY_REQUEST = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
        // Retrieve the shared preferences
        mGameSettings = getSharedPreferences(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES, Context.MODE_PRIVATE);
        // Initialize the avatar button
        initAvatar();
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
        // Initialize the favorite place picker
        initFavoritePlacePicker();		
	}

	  @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	        switch (requestCode) {
	        case TAKE_AVATAR_CAMERA_REQUEST:

	            if (resultCode == Activity.RESULT_CANCELED) {
	                // Avatar camera mode was canceled.
	            } else if (resultCode == Activity.RESULT_OK) {

	                // Took a picture, use the downsized camera image provided by default
	                Bitmap cameraPic = (Bitmap) data.getExtras().get("data");
	                if (cameraPic != null) {
	                    try {
	                        saveAvatar(cameraPic);
	                    } catch (Exception e) {
	                        Log.e(com.androidbook.triviaquiz.QuizActivity.DEBUG_TAG, "saveAvatar() with camera image failed.", e);
	                    }
	                }
	            }
	            break;
	        case TAKE_AVATAR_GALLERY_REQUEST:

	            if (resultCode == Activity.RESULT_CANCELED) {
	                // Avatar gallery request mode was canceled.
	            } else if (resultCode == Activity.RESULT_OK) {

	                // Get image picked
	                Uri photoUri = data.getData();
	                if (photoUri != null) {
	                    try {
	                        int maxLength = 75;
	                        // Full size image likely will be large. Let's scale the graphic to a more appropriate size for an avatar
	                        Bitmap galleryPic = Media.getBitmap(getContentResolver(), photoUri);
	                        Bitmap scaledGalleryPic = createScaledBitmapKeepingAspectRatio(galleryPic, maxLength);
	                        saveAvatar(scaledGalleryPic);
	                    } catch (Exception e) {
	                        Log.e(com.androidbook.triviaquiz.QuizActivity.DEBUG_TAG, "saveAvatar() with gallery picker failed.", e);
	                    }
	                }
	            }
	            break;
	        }
	    }
	  

	   /**
	     * Scale a Bitmap, keeping its aspect ratio
	     * 
	     * @param bitmap
	     *            Bitmap to scale
	     * @param maxSide
	     *            Maximum length of either side
	     * @return a new, scaled Bitmap
	     */
	    private Bitmap createScaledBitmapKeepingAspectRatio(Bitmap bitmap, int maxSide) {
	        int orgHeight = bitmap.getHeight();
	        int orgWidth = bitmap.getWidth();

	        // scale to no longer any either side than 75px
	        int scaledWidth = (orgWidth >= orgHeight) ? maxSide : (int) ((float) maxSide * ((float) orgWidth / (float) orgHeight));
	        int scaledHeight = (orgHeight >= orgWidth) ? maxSide : (int) ((float) maxSide * ((float) orgHeight / (float) orgWidth));

	        // create the scaled bitmap
	        Bitmap scaledGalleryPic = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
	        return scaledGalleryPic;
	    }

	    private void saveAvatar(Bitmap avatar) {
	        String strAvatarFilename = "avatar.jpg";
	        try {
	            avatar.compress(CompressFormat.JPEG, 100, openFileOutput(strAvatarFilename, MODE_PRIVATE));
	        } catch (Exception e) {
	            Log.e(com.androidbook.triviaquiz.QuizActivity.DEBUG_TAG, "Avatar compression and save failed.", e);
	        }

	        Uri imageUriToSaveCameraImageTo = Uri.fromFile(new File(QuizSettingsActivity.this.getFilesDir(), strAvatarFilename));

	        Editor editor = mGameSettings.edit();
	        editor.putString(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_AVATAR, imageUriToSaveCameraImageTo.getPath());
	        editor.commit();

	        // Update the settings screen
	        ImageButton avatarButton = (ImageButton) findViewById(R.id.ImageButton_Avatar);
	        String strAvatarUri = mGameSettings.getString(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_AVATAR, "android.resource://com.androidbook.triviaquiz13/drawable/avatar");
	        Uri imageUri = Uri.parse(strAvatarUri);
	        avatarButton.setImageURI(null); // Workaround for refreshing an ImageButton, which tries to cache the previous image Uri. Passing null effectively resets it.
	        avatarButton.setImageURI(imageUri);
	    }
	    
    private void initAvatar(){
    	ImageButton avatarButton = (ImageButton) findViewById(R.id.ImageButton_Avatar);
    	
    	avatarButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
                String strAvatarPrompt = "Take your picture to store as your avatar!";
                Intent pictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(Intent.createChooser(pictureIntent, strAvatarPrompt), TAKE_AVATAR_CAMERA_REQUEST);			
			}
		});
    	
    	avatarButton.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				String strAvatarPrompt = "Choose a picture to use as your avatar!";
				Intent pickPhoto = new Intent(Intent.ACTION_PICK);
				pickPhoto.setType("image/*");
				startActivityForResult(Intent.createChooser(pickPhoto, strAvatarPrompt), 
						TAKE_AVATAR_GALLERY_REQUEST);
				return true;
			}
		});
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
	
    /**
     * Initialize the Favorite Place picker
     */
	private void initFavoritePlacePicker(){
		TextView placeInfo = (TextView) findViewById(R.id.TextView_FavoritePlace_Info);
		if (mGameSettings.contains(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_FAV_PLACE_NAME)){
			placeInfo.setText(mGameSettings.getString
				(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_FAV_PLACE_NAME,""));
		} else {
			placeInfo.setText(R.string.settings_favoriteplace_not_set);
		}
		Button pickPlace = (Button) findViewById(R.id.Button_FavoritePlace);
		pickPlace.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				showDialog(PLACE_DIALOG_ID);
				
			}
		});
	}
	@Override
	protected Dialog onCreateDialog(int id){
		switch (id) {
	       case PLACE_DIALOG_ID:

	            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            final View dialogLayout = layoutInflater.inflate(R.layout.fav_place_dialog, (ViewGroup) findViewById(R.id.root));

	            final TextView placeCoordinates = (TextView) dialogLayout.findViewById(R.id.TextView_FavPlaceCoords_Info);
	            final EditText placeName = (EditText) dialogLayout.findViewById(R.id.EditText_FavPlaceName);
	            placeName.setOnKeyListener(new View.OnKeyListener() {

	                public boolean onKey(View v, int keyCode, KeyEvent event) {
	                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

	                        String strPlaceName = placeName.getText().toString();
	                        if ((strPlaceName != null) && (strPlaceName.length() > 0)) {
	                            // Try to resolve string into GPS coords
	                            resolveLocation(strPlaceName);

	                            Editor editor = mGameSettings.edit();
	                            editor.putString(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_FAV_PLACE_NAME, placeName.getText().toString());
	                            editor.putFloat(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_FAV_PLACE_LONG, mFavPlaceCoords.mLon);
	                            editor.putFloat(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_FAV_PLACE_LAT, mFavPlaceCoords.mLat);
	                            editor.commit();

	                            placeCoordinates.setText(formatCoordinates(mFavPlaceCoords.mLat, mFavPlaceCoords.mLon));
	                            return true;
	                        }
	                    }
	                    return false;
	                }
	            });

	            final Button mapButton = (Button) dialogLayout.findViewById(R.id.Button_MapIt);
	            mapButton.setOnClickListener(new View.OnClickListener() {
	                public void onClick(View v) {

	                    // Try to resolve string into GPS coords
	                    String placeToFind = placeName.getText().toString();
	                    resolveLocation(placeToFind);

	                    Editor editor = mGameSettings.edit();
	                    editor.putString(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_FAV_PLACE_NAME, placeToFind);
	                    editor.putFloat(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_FAV_PLACE_LONG, mFavPlaceCoords.mLon);
	                    editor.putFloat(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_FAV_PLACE_LAT, mFavPlaceCoords.mLat);
	                    editor.commit();

	                    placeCoordinates.setText(formatCoordinates(mFavPlaceCoords.mLat, mFavPlaceCoords.mLon));

	                    // Launch map with gps coords
	                    String geoURI = String.format("geo:%f,%f?z=10", mFavPlaceCoords.mLat, mFavPlaceCoords.mLon);
	                    Uri geo = Uri.parse(geoURI);
	                    Intent geoMap = new Intent(Intent.ACTION_VIEW, geo);
	                    startActivity(geoMap);
	                }
	            });

	            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
	            dialogBuilder.setView(dialogLayout);

	            // Now configure the AlertDialog
	            dialogBuilder.setTitle(R.string.settings_button_favoriteplace);

	            dialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    // We forcefully dismiss and remove the Dialog, so it cannot be used again (no cached info)
	                    QuizSettingsActivity.this.removeDialog(PLACE_DIALOG_ID);
	                }
	            });

	            dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {

	                    TextView placeInfo = (TextView) findViewById(R.id.TextView_FavoritePlace_Info);
	                    String strPlaceName = placeName.getText().toString();

	                    if ((strPlaceName != null) && (strPlaceName.length() > 0)) {
	                        Editor editor = mGameSettings.edit();
	                        editor.putString(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_FAV_PLACE_NAME, strPlaceName);
	                        editor.putFloat(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_FAV_PLACE_LONG, mFavPlaceCoords.mLon);
	                        editor.putFloat(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_FAV_PLACE_LAT, mFavPlaceCoords.mLat);
	                        editor.commit();

	                        placeInfo.setText(strPlaceName);
	                    }

	                    // We forcefully dismiss and remove the Dialog, so it cannot be used again
	                    QuizSettingsActivity.this.removeDialog(PLACE_DIALOG_ID);
	                }
	            });

	            // Create the AlertDialog and return it
	            AlertDialog placeDialog = dialogBuilder.create();
	            return placeDialog;
	 
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
			case PLACE_DIALOG_ID:

            // Handle any Favorite Place Dialog initialization here
            AlertDialog placeDialog = (AlertDialog) dialog;

            String strFavPlaceName;

            // Check for favorite place preference
            if (mGameSettings.contains(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_FAV_PLACE_NAME)) {

                // Retrieve favorite place from preferences
                strFavPlaceName = mGameSettings.getString(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_FAV_PLACE_NAME, "");
                mFavPlaceCoords = new GPSCoords(mGameSettings.getFloat(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_FAV_PLACE_LAT, 0), mGameSettings.getFloat(com.androidbook.triviaquiz.QuizActivity.GAME_PREFERENCES_FAV_PLACE_LONG, 0));

            } else {

                // No favorite place set, set coords to current location
                strFavPlaceName = getResources().getString(R.string.settings_favplace_currentlocation); // We do not name this place ("here"), but use it as a map point. User can supply the name if
                // they like
                calculateCurrentCoordinates();

            }

            // Set the placename text and coordinates either to the saved values, or just set the GPS coords to the current location
            final EditText placeName = (EditText) placeDialog.findViewById(R.id.EditText_FavPlaceName);
            placeName.setText(strFavPlaceName);

            final TextView placeCoordinates = (TextView) placeDialog.findViewById(R.id.TextView_FavPlaceCoords_Info);
            placeCoordinates.setText(formatCoordinates(mFavPlaceCoords.mLat, mFavPlaceCoords.mLon));

            return;
            
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
    /**
     * Helper to format coordinates for screen display
     * 
     * @param lat
     * @param lon
     * @return A string formatted accordingly
     */
    private String formatCoordinates(float lat, float lon) {
        StringBuilder strCoords = new StringBuilder();
        strCoords.append(lat).append(",").append(lon);
        return strCoords.toString();
    }
    /**
     * 
     * If location name can't be deterimed, try to determine location based on current coords
     * 
     * @param strLocation
     *            Location or place name to try
     */
    private void resolveLocation(String strLocation) {
        boolean bResolvedAddress = false;

        if (strLocation.equalsIgnoreCase(getResources().getString(R.string.settings_favplace_currentlocation)) == false) {
            bResolvedAddress = lookupLocationByName(strLocation);
        }

        if (bResolvedAddress == false) {
            // If String place name could not be determined (or matches the string for "current location", assume this is a custom name of the current location
            calculateCurrentCoordinates();
        }
    }	
    /**
     * Attempt to get the last known location of the device. Usually this is
     * the last value that a location provider set
     */
    private void calculateCurrentCoordinates() {
        float lat = 0, lon = 0;

        try {
            LocationManager locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location recentLoc = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lat = (float) recentLoc.getLatitude();
            lon = (float) recentLoc.getLongitude();
        } catch (Exception e) {
            Log.e(com.androidbook.triviaquiz.QuizActivity.DEBUG_TAG, "Location failed", e);
        }

        mFavPlaceCoords = new GPSCoords(lat, lon);
    }
    /**
     * 
     * Take a description of a location, store the coordinates in mFavPlaceCoords
     * 
     * @param strLocation
     *            The location or placename to look up
     * @return true if the address or place was recognized, otherwise false
     */
    private boolean lookupLocationByName(String strLocation) {
        final Geocoder coder = new Geocoder(getApplicationContext());
        boolean bResolvedAddress = false;

        try {

            List<Address> geocodeResults = coder.getFromLocationName(strLocation, 1);
            Iterator<Address> locations = geocodeResults.iterator();

            while (locations.hasNext()) {
                Address loc = locations.next();
                mFavPlaceCoords = new GPSCoords((float) loc.getLatitude(), (float) loc.getLongitude());
                bResolvedAddress = true;
            }
        } catch (Exception e) {
            Log.e(com.androidbook.triviaquiz.QuizActivity.DEBUG_TAG, "Failed to geocode location", e);
        }
        return bResolvedAddress;
    }
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
	
    private class GPSCoords {
        float mLat, mLon;

        GPSCoords(float lat, float lon) {
            mLat = lat;
            mLon = lon;

        }
    }
}
