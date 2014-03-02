package com.androidbook.triviaquiz;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class QuizMenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		ListView menuList = (ListView) findViewById(R.id.ListView_Menu);
		
		String[] items = {getResources().getString(R.string.menu_option1_PlayGame),
				getResources().getString(R.string.menu_option2_ViewScores),
				getResources().getString(R.string.menu_option3_Settings),
				getResources().getString(R.string.menu_option4_Help)};
		
		ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, R.layout.menu_item, items);
		menuList.setAdapter(adapt);
		
		menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
					long id) {
				TextView textView = (TextView) itemClicked;
				String strText = textView.getText().toString();	
				
				if (strText.equalsIgnoreCase(getResources().getString(R.string.menu_option1_PlayGame))){
					startActivity(new Intent(QuizMenuActivity.this,QuizGameActivity.class));
				} else if (strText.equalsIgnoreCase(getResources().getString(R.string.menu_option2_ViewScores))){
					startActivity(new Intent(QuizMenuActivity.this,QuizScoresActivity.class));
				} else if (strText.equalsIgnoreCase(getResources().getString(R.string.menu_option3_Settings))){
					startActivity(new Intent(QuizMenuActivity.this,QuizSettingsActivity.class));
				} else if (strText.equalsIgnoreCase(getResources().getString(R.string.menu_option4_Help))){
					startActivity(new Intent(QuizMenuActivity.this,QuizHelpActivity.class));

				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz_menu, menu);
		return true;
	}

}
