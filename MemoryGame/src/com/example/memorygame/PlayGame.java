package com.example.memorygame;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class PlayGame extends SherlockActivity implements OnClickListener, OnTouchListener {
	
	GameData gameDataObj;
	GameAlerter gameAlerterObj;
	Timer gameTimer;
	Handler gameHandler;
	PlayGame thisPlayGame;
	MediaPlayer currentSound;
	long delay;
	
	Boolean continueMusic = true;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		gameDataObj = new GameData(this);
		gameAlerterObj = new GameAlerter(this, gameDataObj);
		gameTimer = new Timer();
		gameHandler = new Handler();
		thisPlayGame = this;	
		delay = 1;	
		
		SharedPreferences sharedPrefs = getSharedPreferences(null, MODE_PRIVATE);
		
		
		/* Custom Themes */
		SharedPreferences settings = this.getSharedPreferences("settings", 0);
		int theme = settings.getInt("theme", 0);				
		themeUtils.onActivityCreateSetTheme(this, theme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_game);
		getSupportActionBar().setBackgroundDrawable(null);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		//handling replay button
		if( gameDataObj.getPatternString() == null ){
			gameDataObj.setDifficulty();
			gameDataObj.newPattern();
			gameDataObj.setLives(4);
		} else {
			gameDataObj.reusePattern();
			gameDataObj.setScore(gameDataObj.getScore()-100);
		}
		
		generateLayout(gameDataObj.getNumButtons(), GameButton.buttonsOff);
	
		/*
		 * Play the sequence
		 * 
		 */
		for (int i = 0; i < gameDataObj.getPattern().size(); i++) {
			
			final GameButton b = ((GameButton)findViewById(gameDataObj.getPattern().get(i)));
			
			delay += gameDataObj.getTimeBetweenChangesMs();
			gameTimer.schedule(new TimerTask() {
				public void run() {
					gameHandler.post(new Runnable() {
						public void run() {
							b.turnOn();
						}
					});
				}
			}, delay);
			
			delay += gameDataObj.getTimeBetweenChangesMs();
			gameTimer.schedule(new TimerTask() {
				public void run() {
					gameHandler.post(new Runnable() {
						public void run() {
							b.turnOff();
						}
					});
				}
			}, (delay));
		}
		delay += gameDataObj.getTimeBetweenChangesMs();	

		/*
		 * Set the listeners
		 * 
		 */
		gameTimer.schedule(new TimerTask() {
			public void run() {
				gameHandler.post(new Runnable() {
					public void run() {
						for (int i = 0; i < gameDataObj.getNumButtons(); i++) {
							GameButton b = ((GameButton) findViewById(i));
							b.setOnTouchListener(thisPlayGame);
							b.setOnClickListener(thisPlayGame);
						}
						Button replayButton = ((Button)findViewById(R.id.replayButton));
						replayButton.setBackgroundResource(R.drawable.replay_button);
						replayButton.setOnClickListener(thisPlayGame);
					}
				});
			}
		}, delay);
		
		TextView tvScores = (TextView) findViewById(R.id.ScoreVal);
		tvScores.setText(String.valueOf(gameDataObj.getScore()));
		TextView tvLives = (TextView) findViewById(R.id.LivesVals);
		tvLives.setText(String.valueOf(gameDataObj.getLives()));
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
    public boolean onTouch(View v, MotionEvent event) {
    	GameButton b = (GameButton)v;
    	 if (event.getAction() == MotionEvent.ACTION_DOWN ) {
            b.turnOn();
            return false;
        }
        if (event.getAction() == MotionEvent.ACTION_UP ) {
            b.turnOff();
            return false;
        }
        return false;
    }

    
	@Override
	public void onClick(View v) {
		// get the button selected i.e. the number of the button that the user has clicked
		int buttonSelected = v.getId();
		
    	
    	if(buttonSelected == R.id.replayButton)
		{
			gameDataObj.copyPatternArrayListIntoPatternString();
			gameDataObj.commit();
			Intent i = getIntent();		
			
			finish();
			startActivity(i);
			
		} else if (buttonSelected == gameDataObj.getNumberAtCurrentPosition()) {
			
			// the current number in the pattern sequence equals the current buttonSelected
			// in other words the user has guessed correctly
			
			//play button sound 
	    	currentSound = MediaPlayer.create(this, GameButton.buttonSound[buttonSelected]);
	    	//currentSound.release();
	    	currentSound.setVolume(1.0f, 1.0f);
	        currentSound.start();
	        
	        if (gameDataObj.getPatternPosition() == (gameDataObj.getSequenceLength()-1)) {
	        	
				// we have reached the end of the pattern sequence 
				// set the current pattern to null and restart this activity 
				gameDataObj.increaseScoreBy(100);
				gameDataObj.setPatternString(null);
				gameDataObj.commit();
				
				Intent i = getIntent();
				currentSound = MediaPlayer.create(this, R.raw.correct);
		    	currentSound.setVolume(1.0f, 1.0f);
		        currentSound.start();
		       								
				finish();
				startActivity(i);
			}
			// increase patternPosition so next item in the sequence will be checked next
			gameDataObj.incrementPatternPosition();
			
		} else {
			
			// if the user incorrectly guesses the current item in the pattern so reduce lives
			gameDataObj.decrementLives();
			// reset the text of the TextView that displays the lives to the user
			TextView tvLives = (TextView) findViewById(R.id.LivesVals);
			tvLives.setText(String.valueOf(gameDataObj.getLives()));
	        
			if(gameDataObj.getLives()==0){
				
				// the game is over so show the GameOver alert dialogue box
				// IMPORTANT: this dialogue box will redirect the user to the main menu or insert scores activity
				String message = (getString(R.string.tellscore) +" "+ gameDataObj.getScore() + getString(R.string.enterscore));
				gameAlerterObj.alertGameOver(getString(R.string.gameover), message, gameDataObj.getScore());
				

			} else {
				
				currentSound = MediaPlayer.create(this, R.raw.wronganswer);
		    	currentSound.setVolume(1.0f, 1.0f);
		        currentSound.start();
		        currentSound.release();
		        
				// display the number of lives left to the user
				String message = gameDataObj.getLives() +" "+ getString(R.string.livesleft);
				gameAlerterObj.alert(getString(R.string.incorrect), message);
				
				// start the user guess from the beginning
				gameDataObj.setPatternPosition(0);
				
			}
		}

	}

	private void generateLayout(int numBtns, final int[] colours) {		
		// start generating the layout
		LinearLayout mainLayout = ((LinearLayout) findViewById(R.id.mainLayout));
		/*
		 * The layout will be made up of rows of two buttons side-by-side so if
		 * there are 6 buttons then there will be 3 rows if there are 5 buttons
		 * there will also be 3 rows, the last row will only have one button
		 */
		// based on the number of buttons figure out how many rows are needed
		int numRows = numBtns / 2;
		// if there is an odd number of buttons we need an extra row and set odd
		// == true
		boolean odd = false;
		if (numBtns % 2 != 0) {
			numRows++;
			odd = true;
		}
		// each "row" talked about above is actually a linearLayout
		LinearLayout[] rows = new LinearLayout[numRows];
		// create a set of default linearLayout parameters to be given to every
		// linearLayout
		LayoutParams llParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		// add the buttons to each linearLayout row
		// each button created will be given a unique number starting at 0
		int buttonNumber = 0;
		for (int i = 0; i < numRows; i++) {
			
			rows[i] = new LinearLayout(this);
			// set parameters for the current linearLayout row
			rows[i].setLayoutParams(llParams);
			// if we've reached the last linearLayout row and there is an odd
			// number of buttons, add only one button
			if (i == (numRows - 1) && odd) {
				
				GameButton btn = new GameButton(this, buttonNumber);
				rows[i].addView(btn);
				buttonNumber++;
				
				// create an empty text view that fills up the remaining space
				TextView fake = new TextView(this);
				fake.setLayoutParams(GameButton.bParams);
				rows[i].addView(fake);		
			}
			// otherwise add two buttons to the current linearLayout row
			else {
				GameButton[] rowButtons = new GameButton[2];
				for (GameButton btn : rowButtons) {
					btn = new GameButton(this, buttonNumber);
					rows[i].addView(btn);
					buttonNumber++;
				}
			}
			mainLayout.addView(rows[i]);
		}
	}
	
	/* for back button for action bar */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.game_menu, menu);
		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			gameAlerterObj.alertQuit(getString(R.string.quitgame), getString(R.string.rusure));
			return (true);
		}
		return (super.onOptionsItemSelected(item));
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//continueMusic = true;
			gameAlerterObj.alertQuit(getString(R.string.quitgame), getString(R.string.rusure));
			//startActivity(new Intent(this,MainMenu.class));
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/*
protected void onDestroy(){
	super.onDestroy();
	currentSound.release();
	this.finish();
}
*/
}


