package com.example.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class Feedback extends SherlockActivity {
	ImageButton clearForm, sendForm;
	Boolean continueMusic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Custom Themes */
		themeUtils.onActivityCreateSetTheme(this);
		setContentView(R.layout.feedback);
		int hello = themeUtils.getcTheme();
		if (hello == 1) {
			getSupportActionBar().setBackgroundDrawable(
					getResources().getDrawable(R.drawable.backgroundactionbar));
		}
		else if (hello == 2) {
			getSupportActionBar().setBackgroundDrawable(
					getResources().getDrawable(R.drawable.ocean_ab));
		}
		else if (hello == 0) {
			getSupportActionBar().setBackgroundDrawable(
					getResources().getDrawable(R.drawable.xmas_ab));
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
		new MenuInflater(this).inflate(R.menu.feedback_menu, menu);

		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {

			return (true);
		} else if (itemId == R.id.about) {
			abInfo();
			return (true);
		} else if (itemId == R.id.cancel) {
			clearText();
			return (true);
		} else if (itemId == R.id.send_email) {
			sendMail();
			return (true);
		}

		else if (itemId == R.id.gohome) {
			abGoHome();
			return (true);
		}
		return (super.onOptionsItemSelected(item));
	}

	public void abInfo() {
		continueMusic = true;
		startActivity(new Intent(this, About.class));

	}

	/* Method to clear text from actionbar/options menu */
	private void clearText() {
		ViewGroup group = (ViewGroup) findViewById(R.id.rootLayout);
		clearForm(group);
	}

	// Home activity launched from the actionBar
	public void abGoHome() {
		continueMusic = true;
		startActivity(new Intent(this, MainMenu.class));

	}

	/* Method to send feedback from actionbar/options menu */
	private void sendMail() {
		final EditText name = (EditText) findViewById(R.id.edit_text1);
		final EditText mailaddress = (EditText) findViewById(R.id.edit_text2);
		final EditText comment = (EditText) findViewById(R.id.edit_text3);

		Intent email2 = new Intent(android.content.Intent.ACTION_SEND);
		email2.setType("plain/text");
		email2.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { "andrew.doyle@ucdconnect.ie",
						mailaddress.getText().toString() });
		email2.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"Memory Game Feedback Form");
		email2.putExtra(android.content.Intent.EXTRA_TEXT, "Name: "
				+ name.getText().toString() + '\n' + "Email Address: "
				+ mailaddress.getText().toString() + '\n' + "Comment: "
				+ comment.getText().toString() + '\n' + '\n'
				+ "Sent from the Memory Game Timetable Android App.");

		/* Send it off to the Activity-Chooser */
		startActivity(Intent.createChooser(email2, "Send mail..."));
	}

	/*
	 * ViewGroup group = (ViewGroup) findViewById(R.id.rootLayout);
	 * clearForm(group);
	 */

	private void clearForm(ViewGroup group) {
		for (int i = 0, count = group.getChildCount(); i < count; ++i) {
			View view = group.getChildAt(i);
			if (view instanceof EditText) {
				((EditText) view).setText("");
			}

			if (view instanceof ViewGroup
					&& (((ViewGroup) view).getChildCount() > 0))
				clearForm((ViewGroup) view);
		}
	}

}
