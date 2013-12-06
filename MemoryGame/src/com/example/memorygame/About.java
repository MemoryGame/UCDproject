package com.example.memorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class About extends SherlockActivity {

	Boolean continueMusic;
	TextView textview1, textview2, textview3, textview4;
	LinearLayout sub_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/* Custom Themes */
		
		SharedPreferences settings = this.getSharedPreferences("settings", 0);
		int theme = settings.getInt("theme", 0);				
		themeUtils.onActivityCreateSetTheme(this, theme);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.about);

		getSupportActionBar().setBackgroundDrawable(null);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		/* Decide what background to put on the textviews */

		int whatTheme = themeUtils.getcTheme();
		textview1 = (TextView) findViewById(R.id.textview1);
		textview2 = (TextView) findViewById(R.id.textview2);
		textview3 = (TextView) findViewById(R.id.textview3);
		textview4 = (TextView) findViewById(R.id.textview4);
		sub_back = (LinearLayout) findViewById(R.id.rootLayout);

		int sdk = android.os.Build.VERSION.SDK_INT;
		switch (whatTheme) {
		case 2:

			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				sub_back.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.kidscorners));

			} else {
				sub_back.setBackground(getResources().getDrawable(
						R.drawable.kidscorners));

			}

			textview1.setTextColor(getResources().getColor(
					R.color.Grey_gunmetal));
			textview2.setTextColor(getResources().getColor(
					R.color.Grey_gunmetal));
			textview3.setTextColor(getResources().getColor(
					R.color.Grey_gunmetal));
			textview4.setTextColor(getResources().getColor(
					R.color.Grey_gunmetal));
			textview1.setPadding(10, 10, 10, 10);
			textview2.setPadding(10, 10, 10, 10);
			textview3.setPadding(10, 10, 10, 10);
			textview4.setPadding(10, 10, 10, 10);

			break;
		case 1:

		case 0:
		}

	}

	/* require override to continue music on back button pressed */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			continueMusic = true;

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (!continueMusic) {
			MusicManager.pause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		continueMusic = false;
		MusicManager.start(this, MusicManager.MUSIC_MENU);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.about_menu, menu);

		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
			continueMusic = true;

			return (true);
		}

		else if (itemId == R.id.feedback) {
			abFeedback();
			return (true);
		}

		return (super.onOptionsItemSelected(item));
	}

	// Options activity launched from the actionBar
	public void abOptions() {
		continueMusic = true;
		startActivity(new Intent(this, Options.class));

	}

	// Feedback activity launched from the actionBar
	public void abFeedback() {
		continueMusic = true;
		startActivity(new Intent(this, Feedback.class));

	}

	// Home activity launched from the actionBar
	public void abGoHome() {
		continueMusic = true;
		startActivity(new Intent(this, MainMenu.class));

	}

}
