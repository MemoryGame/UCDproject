package com.example.memorygame;
//I put this comment here
//comment peter
//and here
//and here also
//again
// PULL TEsT. EDITING THIS ONLINE AND GOING TO TRY AND PULL IT DOWN TO ECLIPSE

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
//bla
public class About extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

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

			return (true);
		} else if (itemId == R.id.action_settings) {
			abOptions();
			return (true);
		}

		else if (itemId == R.id.feedback) {
			abFeedback();
			return (true);
		}

		else if (itemId == R.id.gohome) {
			abGoHome();
			return (true);
		}

		return (super.onOptionsItemSelected(item));
	}

	// Options activity launched from the actionBar
	public void abOptions() {

		startActivity(new Intent(this, Options.class));

	}

	// Feedback activity launched from the actionBar
	public void abFeedback() {

		startActivity(new Intent(this, Feedback.class));

	}

	// Home activity launched from the actionBar
	public void abGoHome() {

		startActivity(new Intent(this, MainMenu.class));

	}

}
