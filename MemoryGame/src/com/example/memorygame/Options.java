package com.example.memorygame;

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
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class Options extends SherlockActivity implements OnClickListener {

	Boolean continueMusic;
	Boolean musicIsXmas, musicIsHorror, musicIsOcean;
	ToggleButton soundnotify;
	SharedPreferences preferences; 
	SharedPreferences sharedPrefs;
	Button blueButton, blackButton, xmasButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/* Custom Themes */
		themeUtils.onActivityCreateSetTheme(this);
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_options);
	
		getSupportActionBar().setBackgroundDrawable(null);
	
		/* Make buttons transparent */
		blueButton = (Button) findViewById(R.id.bluebutton);
		blackButton = (Button) findViewById(R.id.blackbutton);
		xmasButton = (Button) findViewById(R.id.xmasbutton);
		blueButton.setBackgroundColor(Color.TRANSPARENT);
		blackButton.setBackgroundColor(Color.TRANSPARENT);
		xmasButton.setBackgroundColor(Color.TRANSPARENT);
		
		/* Decide what music will be started again */
		int whatTheme = themeUtils.getcTheme();
		
		switch (whatTheme){
		case 2:   // ***************** OCEAN THEME ************************ //
		musicIsXmas = false;
		musicIsHorror = false;
		musicIsOcean = true;
	
		break;
		case 1:  // ***************** HORROR THEME ************************ //
		musicIsXmas = false;
		musicIsHorror = true;
		musicIsOcean = false;
		
		
			
			break;
		case 0:   // ***************** XMAS THEME ************************ //
		musicIsXmas = true;
		musicIsHorror = false;
		musicIsOcean = false;
	
			
			break;
		}
		
		
		blueButton.setOnClickListener(this);

		blackButton.setOnClickListener(this);
		xmasButton.setOnClickListener(this);
		
	
		/* Toggle sound on/off with Button - option from action bar too: to be decided*/
		preferences = getPreferences(MODE_PRIVATE);

		soundnotify = (ToggleButton) findViewById(R.id.toggleButton1);
		
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

		case R.id.action_info:
			abInfo();
			return (true);
		case R.id.gohome:
			abGoHome();
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
//	public void blueTheme(){
//		themeUtils.changeToTheme(Options.this, themeUtils.BLUE);
//	}
//	
//	public void blackTheme(){
//		themeUtils.changeToTheme(Options.this, themeUtils.BLACK);
//	}
//	
//	public void xmasTheme(){
//		themeUtils.changeToTheme(Options.this, themeUtils.XMAS);
//	}


	@Override
	public void onClick(View v) {
		switch (v.getId())

		{
		case R.id.blackbutton:

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
