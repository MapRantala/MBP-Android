package com.androidbook.triviaquiz;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class QuizScoresActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scores);
		
		TabHost host = (TabHost) findViewById(R.id.Tabhost1);
		
		
		/**TabWidget tabwidget=host.getTabWidget();
		for(int i=0;i<tabwidget.getChildCount();i++){
		    TextView tv=(TextView) tabwidget.getChildAt(i).findViewById(android.R.id.title);
		    tv.setTextColor(this.getResources().getColorStateList(R.color.menu_color));
		}*/
		host.setup();
		
		TabSpec allScoresTab = host.newTabSpec("allTab");
		allScoresTab.setIndicator(getResources().getString(R.string.all_scores));
		allScoresTab.setContent(R.id.ScrollViewAllScores);
		host.addTab(allScoresTab);

		TabSpec friendsScoresTab = host.newTabSpec("friendsTab");
		friendsScoresTab.setIndicator(getResources().getString(R.string.friends_scores),
				getResources().getDrawable(android.R.drawable.star_on));
		friendsScoresTab.setContent(R.id.ScrollViewFriendsScores);
		host.addTab(friendsScoresTab);
	
		
		
		/**final TabHost tabHost=(TabHost)findViewById(R.id.tabhost);
	    tabHost.setup();

	        final TabSpec spec1 = tabHost.newTabSpec("Tab1");
	        View view = LayoutInflater.from(this).inflate(R.layout.tabbar8, tabHost.getTabWidget(), false);
	        spec1.setIndicator(view);
	        spec1.setContent(R.id.tab1); */
	        
	        
		host.setCurrentTabByTag("allTab");
		
		TableLayout allScoreTable = (TableLayout) findViewById(R.id.TableLayout_AllScores);
		TableLayout friendScoreTable = (TableLayout) findViewById(R.id.TableLayout_FriendsScores);

        initializeHeaderRow(allScoreTable);
        initializeHeaderRow(friendScoreTable);
        
		XmlResourceParser allScores = getResources().getXml(R.xml.allscores);
		XmlResourceParser friendScores = getResources().getXml(R.xml.friendscores);

		
		try {
        	processScores(allScores, allScoreTable);
			processScores(friendScores, friendScoreTable);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
    private void processScores( XmlResourceParser scores, final TableLayout scoreTable) throws XmlPullParserException,
    IOException {
	int eventType = -1;
	boolean bFoundScores = false;
	 
	while (eventType != XmlResourceParser.END_DOCUMENT) {
		if (eventType == XmlResourceParser.START_TAG) {
			String strName = scores.getName();
			if (strName.equals("score")){
				bFoundScores = true;
				String scoreValue = scores.getAttributeValue(null, "score");
				String scoreRank = scores.getAttributeValue(null, "rank");
				String scoreUserName = 
						scores.getAttributeValue(null, "username");
				insertScoreRow(scoreTable, scoreValue, scoreRank, scoreUserName);
			}
		}
		try {
			eventType = scores.next();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    // Handle no scores available
    if (bFoundScores == false) {
        final TableRow newRow = new TableRow(this);
        TextView noResults = new TextView(this);
        noResults.setText(getResources().getString(R.string.no_scores));
        newRow.addView(noResults);
        scoreTable.addView(newRow);
    }
	}	

    /**
     * Add a header {@code TableRow} to the {@code TableLayout} (styled)
     * 
     * @param scoreTable
     *            the {@code TableLayout} that the header row will be added to
     */
    private void initializeHeaderRow(TableLayout scoreTable) {
        // Create the Table header row
        TableRow headerRow = new TableRow(this);
        int textColor = getResources().getColor(R.color.titlecolor);
        float textSize = getResources().getDimension(R.dimen.score_size);
        addTextToRowWithValues(headerRow, getResources().getString(R.string.username), textColor, textSize);
        addTextToRowWithValues(headerRow, getResources().getString(R.string.score), textColor, textSize);
        addTextToRowWithValues(headerRow, getResources().getString(R.string.rank), textColor, textSize);
        scoreTable.addView(headerRow);
    }	
	  /**
     * {@code processScores()} helper method -- Inserts a new score {@code
     * TableRow} in the {@code TableLayout}
     * 
     * @param scoreTable
     *            The {@code TableLayout} to add the score to
     * @param scoreValue
     *            The value of the score
     * @param scoreRank
     *            The ranking of the score
     * @param scoreUserName
     *            The user who made the score
     */
    private void insertScoreRow(final TableLayout scoreTable, String scoreValue, String scoreRank, String scoreUserName) {
        final TableRow newRow = new TableRow(this);
        int textColor =  getResources().getColor(R.color.score_color);
        float textSize = getResources().getDimension(R.dimen.score_size);
        addTextToRowWithValues(newRow, scoreUserName, textColor, textSize);
        addTextToRowWithValues(newRow, scoreValue, textColor, textSize);
        addTextToRowWithValues(newRow, scoreRank, textColor, textSize);
        scoreTable.addView(newRow);
    }

    /**
     * {@code insertScoreRow()} helper method -- Populate a {@code TableRow} with
     * three columns of {@code TextView} data (styled)
     * 
     * @param tableRow
     *            The {@code TableRow} the text is being added to
     * @param text
     *            The text to add
     * @param textColor
     *            The color to make the text
     * @param textSize
     *            The size to make the text
     */
    private void addTextToRowWithValues(final TableRow tableRow, String text, int textColor, float textSize) {
        TextView textView = new TextView(this);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        textView.setText(text);
        tableRow.addView(textView);
    }

    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz_scores, menu);
		return true;
	}

}
