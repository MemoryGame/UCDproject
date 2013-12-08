package com.ucdmscconversion.memorygame;

import java.io.IOException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class Options extends SherlockActivity implements OnClickListener {

	Boolean continueMusic;
	TextView textview1, textview2;
	Boolean musicIsXmas, musicIsHorror, musicIsOcean;
	ToggleButton soundnotify;
	SharedPreferences preferences; 
	SharedPreferences sharedPrefs;
	Button blueButton, blackButton, xmasButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/* Custom Themes */
		SharedPreferences settings = this.getSharedPreferences("settings", 0);
		int theme = settings.getInt("theme", 0);				
		themeUtils.onActivityCreateSetTheme(this, theme);
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_options);
	
		getSupportActionBar().setBackgroundDrawable(null);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
		/* Make buttons transparent */
		blueButton = (Button) findViewById(R.id.bluebutton);
		blackButton = (Button) findViewById(R.id.blackbutton);
		xmasButton = (Button) findViewById(R.id.xmasbutton);
		blueButton.setBackgroundColor(Color.TRANSPARENT);
		blackButton.setBackgroundColor(Color.TRANSPARENT);
		xmasButton.setBackgroundColor(Color.TRANSPARENT);
		
		/* Decide what music will be started again */
		int whatTheme = themeUtils.getcTheme();
		textview1 = (TextView) findViewById(R.id.textView1);
		textview2 = (TextView) findViewById(R.id.textView2);
		int sdk = android.os.Build.VERSION.SDK_INT;
		
		switch (whatTheme){
		case 0:   // ***************** OCEAN THEME ************************ //
			musicIsXmas = false;
			musicIsHorror = false;
			musicIsOcean = true;
			
			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				blueButton.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.kidscornersgrey));
				blackButton.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.kidscorners));
				xmasButton.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.kidscorners));
				textview1.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.kidscornersgrey));
				textview2.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.kidscornersgrey));

			} else {
				blueButton.setBackground(getResources().getDrawable(
						R.drawable.kidscornersgrey));
				blackButton.setBackground(getResources().getDrawable(
						R.drawable.kidscorners));
				xmasButton.setBackground(getResources().getDrawable(
						R.drawable.kidscorners));
				textview1.setBackground(getResources().getDrawable(
						R.drawable.kidscornersgrey));
				textview2.setBackground(getResources().getDrawable(
						R.drawable.kidscornersgrey));

			}

			blueButton.setTextColor(getResources().getColor(
					R.color.white));
			blackButton.setTextColor(getResources().getColor(
					R.color.Grey_gunmetal));
			xmasButton.setTextColor(getResources().getColor(
					R.color.Grey_gunmetal));
			textview1.setTextColor(getResources().getColor(
					R.color.white));
			textview2.setTextColor(getResources().getColor(
					R.color.white));
		
			blueButton.setPadding(10, 10, 10, 10);
			blackButton.setPadding(10, 10, 10, 10);
			xmasButton.setPadding(10, 10, 10, 10);
			textview1.setPadding(10, 10, 10, 10);
			textview2.setPadding(10, 10, 10, 10);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)textview2.getLayoutParams();
			params.setMargins(15, 160, 15, 16); //substitute parameters for left, top, right, bottom
			textview2.setLayoutParams(params);
			
		
		
			break;
			case 1:  // ***************** HORROR THEME ************************ //
			musicIsXmas = false;
			musicIsHorror = true;
			musicIsOcean = false;
			
			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				blueButton.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.kidscorners));
				blackButton.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.kidscornersgrey));
				xmasButton.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.kidscorners));

			} else {
				blueButton.setBackground(getResources().getDrawable(
						R.drawable.kidscorners));
				blackButton.setBackground(getResources().getDrawable(
						R.drawable.kidscornersgrey));
				xmasButton.setBackground(getResources().getDrawable(
						R.drawable.kidscorners));

			}

			blueButton.setTextColor(getResources().getColor(
					R.color.Grey_gunmetal));
			blackButton.setTextColor(getResources().getColor(
					R.color.white));
			xmasButton.setTextColor(getResources().getColor(
					R.color.Grey_gunmetal));
		
			blueButton.setPadding(10, 10, 10, 10);
			blackButton.setPadding(10, 10, 10, 10);
			xmasButton.setPadding(10, 10, 10, 10);
			
				
				break;
			case 2:   // ***************** XMAS THEME ************************ //
			musicIsXmas = true;
			musicIsHorror = false;
			musicIsOcean = false;
		
			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				blueButton.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.kidscorners));
				blackButton.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.kidscorners));
				xmasButton.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.kidscornersgrey));

			} else {
				blueButton.setBackground(getResources().getDrawable(
						R.drawable.kidscorners));
				blackButton.setBackground(getResources().getDrawable(
						R.drawable.kidscorners));
				xmasButton.setBackground(getResources().getDrawable(
						R.drawable.kidscornersgrey));

			}

			blueButton.setTextColor(getResources().getColor(
					R.color.Grey_gunmetal));
			blackButton.setTextColor(getResources().getColor(
					R.color.Grey_gunmetal));
			xmasButton.setTextColor(getResources().getColor(
					R.color.white));
		
			blueButton.setPadding(10, 10, 10, 10);
			blackButton.setPadding(10, 10, 10, 10);
			xmasButton.setPadding(10, 10, 10, 10);
				
				break;
			}
		
		
		blueButton.setOnClickListener(this);

		blackButton.setOnClickListener(this);
		xmasButton.setOnClickListener(this);
		
	
		/* Toggle sound on/off with Button - option from action bar too: to be decided*/
		preferences = getPreferences(MODE_PRIVATE);

		soundnotify = (ToggleButton) findViewById(R.id.toggleButton1);
		soundnotify.setText(null);
		soundnotify.setTextOn(null);
		soundnotify.setTextOff(null);
		
		soundnotify
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							// Sound Notifications is enabled
							try {
							if (musicIsXmas)
								{
									MusicManager.startagainXmas(Options.this,
											MusicManager.MUSIC_MENU);
								}
							else if(musicIsHorror){
								MusicManager.startagainHorror(Options.this,
										MusicManager.MUSIC_MENU);
							}
							
							else{
								MusicManager.startagain(Options.this,
										MusicManager.MUSIC_MENU);
							}
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							  SharedPreferences.Editor editor = preferences.edit();
				                editor.putBoolean("tgpref", true); // value to store
				                editor.commit();
						} else {
							// Sound Notifications is disabled
							
							SharedPreferences.Editor editor = preferences.edit();
			                editor.putBoolean("tgpref", false); // value to store
			                editor.commit();
							MusicManager.stop();
						}
					}
				});
		
		

	}

	/* require override to continue music on back button pressed */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			boolean tgpref = preferences.getBoolean("tgpref", true);
			if (tgpref) //if (tgpref) may be enough, not sure
			{
			continueMusic = true;
			soundnotify.setChecked(true);
			}
			
//			else
//			{
//				continueMusic = false;
//				soundnotify.setChecked(false);
//			}
			setResult(RESULT_OK);
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
		new MenuInflater(this).inflate(R.menu.options_menu, menu);

		return (super.onCreateOptionsMenu(menu));
	}

	/* Boolean Value for sound on/ off */
	boolean soundOnOff = true;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			boolean tgpref = preferences.getBoolean("tgpref", true);
			if (tgpref) //if (tgpref) may be enough, not sure
			{
			continueMusic = true;
			soundnotify.setChecked(true);
			
			continueMusic = true;
			soundnotify.setChecked(true);
			}setResult(RESULT_OK);
			return (true);

		}

		return (super.onOptionsItemSelected(item));
	}

	// Home activity launched from the actionBar
	public void abGoHome() {
		continueMusic = true;
		startActivity(new Intent(this, MainMenu.class));

	}

	public void abInfo() {
		continueMusic = true;
		startActivity(new Intent(this, About.class));

	}


	@Override
	public void onClick(View v) {
		
		SharedPreferences settings = this.getSharedPreferences("settings", 0);
		SharedPreferences.Editor editor = settings.edit();		
		
		switch (v.getId())

		{
		case R.id.blackbutton:
			

			editor.putInt("theme", themeUtils.BLACK);	
		    editor.commit();				
			
			themeUtils.changeToTheme(this, themeUtils.BLACK);
			
			MusicManager.stop();
			// Sound Notifications is enabled
			try {
				MusicManager.startagainHorror(Options.this,
						MusicManager.MUSIC_MENU);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			musicIsHorror = true;
	
			break;

			case R.id.bluebutton:
			
			editor.putInt("theme", themeUtils.BLUE);	
			editor.commit();					
		
			themeUtils.changeToTheme(this, themeUtils.BLUE);
			
			MusicManager.stop();
			// Sound Notifications is enabled
			try {
				MusicManager.startagain(Options.this,
						MusicManager.MUSIC_MENU);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			musicIsOcean = true;
	
				

			break;
			
			case R.id.xmasbutton:
				
				editor.putInt("theme", themeUtils.XMAS);	
				editor.commit();					
				
				themeUtils.changeToTheme(this, themeUtils.XMAS);
				MusicManager.stop();
				// Sound Notifications is enabled
				try {
					MusicManager.startagainXmas(Options.this,
							MusicManager.MUSIC_MENU);
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				musicIsXmas = true;
			
		
					

				break;
		}
		
	}
	

	
}
