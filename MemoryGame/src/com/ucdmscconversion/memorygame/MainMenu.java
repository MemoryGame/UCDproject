package com.ucdmscconversion.memorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView.BufferType;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class MainMenu extends SherlockActivity {

	Boolean continueMusic = true;

	
	Button btPlayNow, btInstructions, btOptions, btHighScores;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/* Custom Themes */
		SharedPreferences settings = this.getSharedPreferences("settings", 0);
		int theme = settings.getInt("theme", 0);				
		themeUtils.onActivityCreateSetTheme(this, theme);
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_main_menu);
		getSupportActionBar().setBackgroundDrawable(null);
		int whatTheme = theme;

		SharedPreferences sharedPrefs = getSharedPreferences(null, MODE_PRIVATE);
		

		btPlayNow = (Button) findViewById(R.id.button_play);
		btInstructions = (Button) findViewById(R.id.button_instructions);
		btOptions = (Button) findViewById(R.id.button_options);
		btHighScores = (Button) findViewById(R.id.button_high_score);

		btPlayNow.setBackgroundColor(Color.TRANSPARENT);
		btInstructions.setBackgroundColor(Color.TRANSPARENT);
		btOptions.setBackgroundColor(Color.TRANSPARENT);
		btHighScores.setBackgroundColor(Color.TRANSPARENT);
		
		/*Big first letters*/
		switch (whatTheme){
		case 0:   // ***************** OCEAN OR KIDS LETTERS ************************ //
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
			
			String instructionsLetter = (String) btInstructions.getText();
			SpannableString iLetter = new SpannableString(instructionsLetter);
			Drawable bigi = getResources().getDrawable(R.drawable.letter_i);
			bigi.setBounds(0, 0, bigi.getIntrinsicWidth(),
					bigi.getIntrinsicHeight());
			ImageSpan spani = new ImageSpan(bigi, ImageSpan.ALIGN_BASELINE);
			iLetter.setSpan(spani, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			
			btInstructions.setText(iLetter, BufferType.SPANNABLE);
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
			
			String scoresLetter = (String) btHighScores.getText();
			SpannableString hLetter = new SpannableString(scoresLetter);
			Drawable bigH = getResources().getDrawable(R.drawable.letter_h);
			bigH.setBounds(0, 0, bigH.getIntrinsicWidth(),
					bigH.getIntrinsicHeight());
			ImageSpan spanH = new ImageSpan(bigH, ImageSpan.ALIGN_BASELINE);
			hLetter.setSpan(spanH, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			
			btHighScores.setText(hLetter, BufferType.SPANNABLE);
			break;
		case 1:  // ***************** HORROR LETTERS ************************ //
			
			// Letter P for "Play Now"
			String horrorplayLetter = (String) btPlayNow.getText();
			SpannableString horrorbigLetter = new SpannableString(horrorplayLetter);
			Drawable horrorbigA = getResources().getDrawable(R.drawable.letter_p_horror);
			horrorbigA.setBounds(0, 0, horrorbigA.getIntrinsicWidth(),
					horrorbigA.getIntrinsicHeight());
			ImageSpan horrorspan = new ImageSpan(horrorbigA, ImageSpan.ALIGN_BASELINE);
			horrorbigLetter.setSpan(horrorspan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			
			btPlayNow.setText(horrorbigLetter, BufferType.SPANNABLE);
			
			// Letter I for "Instructions"
			
			String horrorinstructionsLetter = (String) btInstructions.getText();
			SpannableString horroriLetter = new SpannableString(horrorinstructionsLetter);
			Drawable horrorbigi = getResources().getDrawable(R.drawable.letter_i_horror);
			horrorbigi.setBounds(0, 0, horrorbigi.getIntrinsicWidth(),
					horrorbigi.getIntrinsicHeight());
			ImageSpan horrorspani = new ImageSpan(horrorbigi, ImageSpan.ALIGN_BASELINE);
			horroriLetter.setSpan(horrorspani, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			
			btInstructions.setText(horroriLetter, BufferType.SPANNABLE);
			
			// Letter O for "Options"
			String horroroptionsLetter = (String) btOptions.getText();
			SpannableString horroroLetter = new SpannableString(horroroptionsLetter);
			Drawable horrorbigO = getResources().getDrawable(R.drawable.letter_o_horror);
			horrorbigO.setBounds(0, 0, horrorbigO.getIntrinsicWidth(),
					horrorbigO.getIntrinsicHeight());
			ImageSpan horrorspanO = new ImageSpan(horrorbigO, ImageSpan.ALIGN_BASELINE);
			horroroLetter.setSpan(horrorspanO, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			
			btOptions.setText(horroroLetter, BufferType.SPANNABLE);
			
			// Letter H for "High Scores"
			
			String horrorscoresLetter = (String) btHighScores.getText();
			SpannableString horrorhLetter = new SpannableString(horrorscoresLetter);
			Drawable horrorbigH = getResources().getDrawable(R.drawable.letter_h_horror);
			horrorbigH.setBounds(0, 0, horrorbigH.getIntrinsicWidth(),
					horrorbigH.getIntrinsicHeight());
			ImageSpan horrorspanH = new ImageSpan(horrorbigH, ImageSpan.ALIGN_BASELINE);
			horrorhLetter.setSpan(horrorspanH, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			
			btHighScores.setText(horrorhLetter, BufferType.SPANNABLE);
			
			break;
		case 2:   // ***************** XMAS LETTERS ************************ //
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
		continueMusic = false;
		MusicManager.start(this, MusicManager.MUSIC_MENU);
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
	     if (itemId == R.id.action_info) {
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
		finish();

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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
