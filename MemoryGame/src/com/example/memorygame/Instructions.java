package com.example.memorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class Instructions extends SherlockActivity {

	Boolean continueMusic;
	TextView textview2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/* Custom Themes */
		SharedPreferences settings = this.getSharedPreferences("settings", 0);
		int theme = settings.getInt("theme", 0);				
		themeUtils.onActivityCreateSetTheme(this, theme);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_instructions);

		getSupportActionBar().setBackgroundDrawable(null);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		/* Decide what background to put on the textviews */

		int whatTheme = themeUtils.getcTheme();
		textview2 = (TextView) findViewById(R.id.textview2);

		int sdk = android.os.Build.VERSION.SDK_INT;
		switch (whatTheme) {
		case 2:
			// textview3.setBackground(getResources().getDrawable(R.drawable.kidscorners));

			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {

				textview2.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.kidscorners));

			} else {

				textview2.setBackground(getResources().getDrawable(
						R.drawable.kidscorners));

			}

			textview2.setTextColor(getResources().getColor(
					R.color.Grey_gunmetal));

			textview2.setPadding(10, 10, 10, 10);

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
		new MenuInflater(this).inflate(R.menu.instructions_menu, menu);

		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			finish();
			continueMusic = true;

			return (true);

		}
		return (super.onOptionsItemSelected(item));
	}

	// Options activity launched from the actionBar
	public void abOptions() {
		continueMusic = true;
		startActivity(new Intent(this, Options.class));

	}

	public void abInfo() {
		continueMusic = true;
		startActivity(new Intent(this, About.class));

	}

}
