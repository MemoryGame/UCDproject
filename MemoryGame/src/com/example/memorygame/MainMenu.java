package com.example.memorygame;

import java.util.List;

import android.preference.PreferenceManager;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class MainMenu extends SherlockActivity {

	Boolean continueMusic = true;// = true;

	Boolean yo;

	Button btPlayNow, btInstructions, btOptions, btHighScores;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Custom Themes */
		themeUtils.onActivityCreateSetTheme(this);

		setContentView(R.layout.activity_main_menu);
		getSupportActionBar().setBackgroundDrawable(null);
		int hello = themeUtils.getcTheme();

		SharedPreferences sharedPrefs = getSharedPreferences(null, MODE_PRIVATE);
		yo = sharedPrefs.getBoolean("tgref", true);

		btPlayNow = (Button) findViewById(R.id.button_play);
		btInstructions = (Button) findViewById(R.id.button_instructions);
		btOptions = (Button) findViewById(R.id.button_options);
		btHighScores = (Button) findViewById(R.id.button_high_score);

		btPlayNow.setBackgroundColor(Color.TRANSPARENT);
		btInstructions.setBackgroundColor(Color.TRANSPARENT);
		btOptions.setBackgroundColor(Color.TRANSPARENT);
		btHighScores.setBackgroundColor(Color.TRANSPARENT);
		
		/*Big first letters*/
		switch (hello){
		case 2:
			// Letter P for "Play Now"
			String playLetter = (String) btPlayNow.getText();
			SpannableString bigLetter = new SpannableString(playLetter);
			Drawable bigA = getResources().getDrawable(R.drawable.letter_p);
			bigA.setBounds(0, 0, bigA.getIntrinsicWidth(),
					bigA.getIntrinsicHeight());
			ImageSpan span = new ImageSpan(bigA, ImageSpan.ALIGN_BASELINE);
			bigLetter.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			
			btPlayNow.setText(bigLetter, BufferType.SPANNABLE);
			
			// Letter I for "Instructions"
			
			// Letter O for "Options"
			String optionsLetter = (String) btOptions.getText();
			SpannableString oLetter = new SpannableString(optionsLetter);
			Drawable bigO = getResources().getDrawable(R.drawable.letter_o);
			bigO.setBounds(0, 0, bigO.getIntrinsicWidth(),
					bigO.getIntrinsicHeight());
			ImageSpan spanO = new ImageSpan(bigO, ImageSpan.ALIGN_BASELINE);
			oLetter.setSpan(spanO, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			
			btOptions.setText(oLetter, BufferType.SPANNABLE);
			
			// Letter H for "High Scores"
			break;
		case 1:
			
			break;
		case 0:
			// Letter P for "Play Now"
			String xmasplayLetter = (String) btPlayNow.getText();
			SpannableString xmasbigLetter = new SpannableString(xmasplayLetter);
			Drawable xmasbigA = getResources().getDrawable(R.drawable.letter_p_xmas);
			xmasbigA.setBounds(0, 0, xmasbigA.getIntrinsicWidth(),
					xmasbigA.getIntrinsicHeight());
			ImageSpan xmasspan = new ImageSpan(xmasbigA, ImageSpan.ALIGN_BASELINE);
			xmasbigLetter.setSpan(xmasspan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			
			btPlayNow.setText(xmasbigLetter, BufferType.SPANNABLE);
			
			// Letter I for "Instructions"
			
			String xmasinstructionsLetter = (String) btInstructions.getText();
			SpannableString xmasiLetter = new SpannableString(xmasinstructionsLetter);
			Drawable xmasbigi = getResources().getDrawable(R.drawable.letter_i_xmas);
			xmasbigi.setBounds(0, 0, xmasbigi.getIntrinsicWidth(),
					xmasbigi.getIntrinsicHeight());
			ImageSpan xmasspani = new ImageSpan(xmasbigi, ImageSpan.ALIGN_BASELINE);
			xmasiLetter.setSpan(xmasspani, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			
			btInstructions.setText(xmasiLetter, BufferType.SPANNABLE);
			
			// Letter O for "Options"
			String xmasoptionsLetter = (String) btOptions.getText();
			SpannableString xmasoLetter = new SpannableString(xmasoptionsLetter);
			Drawable xmasbigO = getResources().getDrawable(R.drawable.letter_o_xmas);
			xmasbigO.setBounds(0, 0, xmasbigO.getIntrinsicWidth(),
					xmasbigO.getIntrinsicHeight());
			ImageSpan xmasspanO = new ImageSpan(xmasbigO, ImageSpan.ALIGN_BASELINE);
			xmasoLetter.setSpan(xmasspanO, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			
			btOptions.setText(xmasoLetter, BufferType.SPANNABLE);
			
			// Letter H for "High Scores"
			
			String xmasscoresLetter = (String) btHighScores.getText();
			SpannableString xmashLetter = new SpannableString(xmasscoresLetter);
			Drawable xmasbigH = getResources().getDrawable(R.drawable.letter_h_xmas);
			xmasbigH.setBounds(0, 0, xmasbigH.getIntrinsicWidth(),
					xmasbigH.getIntrinsicHeight());
			ImageSpan xmasspanH = new ImageSpan(xmasbigH, ImageSpan.ALIGN_BASELINE);
			xmashLetter.setSpan(xmasspanH, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			
			btHighScores.setText(xmashLetter, BufferType.SPANNABLE);
			
			break;
		}
		
		

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
		if (yo) // if (yo) may be enough, not sure
		{
			continueMusic = false;
			MusicManager.start(this, MusicManager.MUSIC_MENU);
		}

		else {
			MusicManager.pause();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100) {
			startActivity(new Intent(this, MainMenu.class));
			this.finish();
		}
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
		continueMusic = true;
		startActivity(new Intent(this, PlayGame.class));

	}

	public void btInstructions(View btInstructions) {

		continueMusic = true;
		startActivity(new Intent(this, Instructions.class));

	}

	public void btOptions(View btOptions) {
		continueMusic = true;
		startActivityForResult(new Intent(this, Options.class), 100);

	}

	public void btHighScores(View btHighScores) {

		continueMusic = true;
		startActivity(new Intent(this, HighScores.class));

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
