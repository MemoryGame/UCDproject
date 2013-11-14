package com.example.memorygame;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class HighScores extends SherlockActivity {

	Boolean continueMusic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_high_scores);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundactionbar));
		
		//Create instance of DatabaseScores class
		DatabaseScores highScores = new DatabaseScores(this);
		
		highScores.open();
		
		//Initialise ArrayList to store the results from the getData() method
		ArrayList<String> scores = highScores.getData();
		
		//Create listView to display results from database
		ListView lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, scores));
		
		highScores.close();

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
		new MenuInflater(this).inflate(R.menu.high_scores_menu, menu);

		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

		case R.id.discard:

			clearScores();
			return (true);

		case R.id.action_settings:
			abOptions();
			return (true);

		case R.id.action_info:
			abInfo();
			return (true);

		case R.id.gohome:
			abGoHome();
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

	// Home activity launched from the actionBar
	public void abGoHome() {
		continueMusic = true;
		startActivity(new Intent(this, MainMenu.class));

	}

	// ERASE THE HIGH SCORES
	public boolean clearScores() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				HighScores.this);

		// set title
		alertDialogBuilder.setTitle("High Scores will be erased!");

		// set dialog message
		alertDialogBuilder
				.setMessage("Are you sure?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// open the database and call delete method

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

		return (true);

	}

}
