package com.androidbook.triviaquiz;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.Animation.AnimationListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.content.Intent;

public class QuizSplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		TextView logo1 = (TextView) findViewById(R.id.TextViewTopTitle);
		Animation fade1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		logo1.startAnimation(fade1);
		
		TextView logo2 = (TextView) findViewById(R.id.TextViewBottomTitle);
		Animation fade2 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		logo2.startAnimation(fade2);
		
		fade2.setAnimationListener(new AnimationListener(){
			public void onAnimationEnd(Animation animation){
				startActivity(new Intent(QuizSplashActivity.this, QuizMenuActivity.class));
				QuizSplashActivity.this.finish();
			}

			public void onAnimationStart(Animation animation){	
			}

			public void onAnimationRepeat(Animation animation){	
			}

		});

		Animation spinin = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
		LayoutAnimationController controller = new LayoutAnimationController(spinin);
		TableLayout table = (TableLayout) findViewById(R.id.TableLayout01);
		for (int i = 0; i < table.getChildCount(); i++){
			TableRow row = (TableRow) table.getChildAt(i);
			row.setLayoutAnimation(controller);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz_splash, menu);
		return true;
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		TextView logo1 = (TextView) findViewById(R.id.TextViewTopTitle);
		logo1.clearAnimation();
		
		TextView logo2 = (TextView) findViewById(R.id.TextViewBottomVersion);
		logo2.clearAnimation();
		
		TableLayout table = (TableLayout) findViewById(R.id.TableLayout01);
		for (int i = 0; i <table.getChildCount(); i++){
			TableRow row = (TableRow) table.getChildAt(i);
			row.clearAnimation();
		}
	}

}
