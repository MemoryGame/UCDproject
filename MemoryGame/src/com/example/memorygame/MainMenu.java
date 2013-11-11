package com.example.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class MainMenu extends SherlockActivity {

	Button btPlayNow, btInstructions, btOptions, btHighScores;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.main_menu, menu);

		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_settings) {
			abOptions();
			return (true);
		} else if (itemId == R.id.action_info) {
			abInfo();
			return (true);
		}
		return (super.onOptionsItemSelected(item));
	}

	// Methods for buttons which are referenced in activity_main_menu.xml with
	// android:onClick
	// OnClickListeners may be more appropriate for situations like fragments
	public void btPlayNow(View btPlayNow) {

		startActivity(new Intent(this, PlayGame.class));

	}

	public void btInstructions(View btInstructions) {

		startActivity(new Intent(this, Instructions.class));

	}

	public void btOptions(View btOptions) {

		startActivity(new Intent(this, Options.class));

	}

	public void btHighScores(View btHighScores) {

		startActivity(new Intent(this, HighScores.class));

	}

	// Options activity launched from the actionBar
	public void abOptions() {

		startActivity(new Intent(this, Options.class));

	}

	public void abInfo() {

		startActivity(new Intent(this, About.class));

	}

}
