package com.example.memorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class Options extends SherlockActivity {

	Boolean continueMusic;
	ToggleButton soundnotify;
	SharedPreferences preferences; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundactionbar));

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
							MusicManager.start(Options.this,
									MusicManager.MUSIC_MENU);
							  SharedPreferences.Editor editor = preferences.edit();
				                editor.putBoolean("tgpref", true); // value to store
				                editor.commit();
						} else {
							// Sound Notifications is disabled
							
							SharedPreferences.Editor editor = preferences.edit();
			                editor.putBoolean("tgpref", false); // value to store
			                editor.commit();
							MusicManager.pause();
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
		//case R.id.toggle_sound:
		case R.id.toggle_sound:
			if (soundOnOff) {
				// 
				item.setIcon(R.drawable.ic_action_volume_muted);
				// item.setTitle(title1)
				MusicManager.pause();
				soundOnOff = false;
			} else {
				// 
				item.setIcon(R.drawable.ic_action_volume_on);
				// item.setTitle(title2)
				MusicManager.start(Options.this, MusicManager.MUSIC_MENU);

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
}
