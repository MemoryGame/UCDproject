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
	ToggleButton soundnotify, soundnotify2;
	SharedPreferences preferences; 
	SharedPreferences sharedPrefs;
	Button blueButton, blackButton, xmasButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Custom Themes */
		themeUtils.onActivityCreateSetTheme(this);
		setContentView(R.layout.activity_options);
		int hello = themeUtils.getcTheme();
		if (hello == 1) {
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundactionbar));
		}	
		else if (hello == 2) {
			getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ocean_ab));
		}
		else if (hello == 0) {
			getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.xmas_ab));
		}
	
		/* Make buttons transparent */
		blueButton = (Button) findViewById(R.id.bluebutton);
		blackButton = (Button) findViewById(R.id.blackbutton);
		xmasButton = (Button) findViewById(R.id.xmasbutton);
		blueButton.setBackgroundColor(Color.TRANSPARENT);
		blackButton.setBackgroundColor(Color.TRANSPARENT);
		xmasButton.setBackgroundColor(Color.TRANSPARENT);
		
		
		
		blueButton.setOnClickListener(this);

		blackButton.setOnClickListener(this);
		xmasButton.setOnClickListener(this);
		
	
		/* Toggle sound on/off with Button - option from action bar too: to be decided*/
		preferences = getPreferences(MODE_PRIVATE);

		soundnotify = (ToggleButton) findViewById(R.id.toggleButton1);
		soundnotify2 = (ToggleButton) findViewById(R.id.toggleButton2);
		soundnotify
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
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
		
		soundnotify2
		.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
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
			
			else
			{
				continueMusic = false;
				soundnotify.setChecked(false);
			}
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
		int hello = themeUtils.getcTheme();
		if (hello == 1) {
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundactionbar));
		}	
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
		//case R.id.toggle_sound:
		case R.id.toggle_sound:
			if (soundOnOff) {
				// 
				item.setIcon(R.drawable.ic_action_volume_muted);
				// item.setTitle(title1)
				MusicManager.stop();
				soundOnOff = false;
			} else {
				// 
				item.setIcon(R.drawable.ic_action_volume_on);
				// item.setTitle(title2)
				try {
					MusicManager.startagain(Options.this, MusicManager.MUSIC_MENU);
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				soundOnOff = true;
			}
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
	
			break;

			case R.id.bluebutton:
		
			themeUtils.changeToTheme(this, themeUtils.BLUE);
		
	
				

			break;
			
			case R.id.xmasbutton:
				
				themeUtils.changeToTheme(this, themeUtils.XMAS);
			
		
					

				break;
		}
		
	}
	

	
}
