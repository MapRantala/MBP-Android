package com.androidbook.triviaquiz;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class QuizGameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.gameoptions, menu);
		menu.findItem(R.id.help_menu_item).setIntent(
				new Intent(this, QuizHelpActivity.class));
		menu.findItem(R.id.settings_menu_item).setIntent(
				new Intent(this, QuizSettingsActivity.class));
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);
		startActivity(item.getIntent());
		return true;
	}

}
