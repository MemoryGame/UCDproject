package com.example.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class Feedback extends SherlockActivity {
	ImageButton clearForm, sendForm;
	Boolean continueMusic;
	LinearLayout sub_back;
	TextView textview2;
	EditText textview1, textview4, textview3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/* Custom Themes */
		themeUtils.onActivityCreateSetTheme(this);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.feedback);

		getSupportActionBar().setBackgroundDrawable(null);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		int whatTheme = themeUtils.getcTheme();
		textview1 = (EditText) findViewById(R.id.edit_text1);
		textview2 = (TextView) findViewById(R.id.textview2);
		textview3 = (EditText) findViewById(R.id.edit_text2);
		textview4 = (EditText) findViewById(R.id.edit_text3);
		sub_back = (LinearLayout) findViewById(R.id.rootLayout2);
		// textview4 = (TextView)findViewById(R.id.textview4);
		int sdk = android.os.Build.VERSION.SDK_INT;
		switch (whatTheme) {
		case 2:
			// textview3.setBackground(getResources().getDrawable(R.drawable.kidscorners));

			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				sub_back.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.kidscorners));
				//
			} else {
				sub_back.setBackground(getResources().getDrawable(
						R.drawable.kidscorners));

			}

			textview1.setTextColor(getResources().getColor(
					R.color.Grey_gunmetal));
			textview1.setHintTextColor(getResources().getColor(
					R.color.Grey_gunmetal));
			textview2.setTextColor(getResources().getColor(
					R.color.Grey_gunmetal));
			textview3.setTextColor(getResources().getColor(
					R.color.Grey_gunmetal));
			textview3.setHintTextColor(getResources().getColor(
					R.color.Grey_gunmetal));
			textview4.setTextColor(getResources().getColor(
					R.color.Grey_gunmetal));
			textview4.setHintTextColor(getResources().getColor(
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
		new MenuInflater(this).inflate(R.menu.feedback_menu, menu);

		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
			continueMusic = true;

			return (true);
		} else if (itemId == R.id.cancel) {
			clearText();
			return (true);
		} else if (itemId == R.id.send_email) {
			sendMail();
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
				+ "Sent from the Memory Game Android App.");

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
