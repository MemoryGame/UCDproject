package com.example.memorygame;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class PlayGame extends SherlockActivity implements OnClickListener, OnTouchListener {
	
	GameData gameDataObj;
	GameAlerter gameAlerterObj;
	Timer gameTimer;
	Handler gameHandler;
	PlayGame thisPlayGame;
	long delay;
	
	// The buttons will have different background colours
	int[] buttonsOn = new int[8];
	int[] buttonsOff = new int[8];
	int[] buttonSound = new int[8];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		gameDataObj = new GameData(this);
		gameAlerterObj = new GameAlerter(this, gameDataObj);
		gameTimer = new Timer();
		gameHandler = new Handler();
		thisPlayGame = this;	
		delay = 1;		
		
		buttonsOff = new int[] { R.drawable.blue_button_off,
				R.drawable.orange_button_off, R.drawable.yellow_button_off,
				R.drawable.purple_button_off, R.drawable.green_button_off,
				R.drawable.red_button_off, R.drawable.black_button_off,
				R.drawable.pink_button_off };		
		
		buttonsOn = new int[] { R.drawable.blue_button_on,
				R.drawable.orange_button_on, R.drawable.yellow_button_on,
				R.drawable.purple_button_on, R.drawable.green_button_on,
				R.drawable.red_button_on, R.drawable.black_button_on,
				R.drawable.pink_button_on };	
		
		buttonSound = new int[] { R.raw.button1a, R.raw.button2a, 
				R.raw.button3a, R.raw.button4a, R.raw.button5a, 
				R.raw.button6a, R.raw.buttona7a, R.raw.button8a };

		/* Custom Themes */
		themeUtils.onActivityCreateSetTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_game);
		getSupportActionBar().setBackgroundDrawable(null);
		
		//handling replay button
		if( gameDataObj.getPatternString() == null ){
			gameDataObj.setDifficulty();
			gameDataObj.newPattern();
			gameDataObj.setLives(4);
		} else {
			gameDataObj.reusePattern();
			
		}
		
		generateLayout(gameDataObj.getNumButtons(), buttonsOff);
	
		/*
		 * Play the sequence
		 * 
		 */
		for (int i = 0; i < gameDataObj.getPattern().size(); i++) {
			
			final Button b = ((Button) findViewById(gameDataObj.getPattern().get(i)));
			final int bNum = gameDataObj.getPattern().get(i);
			
			delay += gameDataObj.getTimeBetweenChangesMs();
			gameTimer.schedule(new TimerTask() {
				public void run() {
					gameHandler.post(new Runnable() {
						public void run() {
							b.setBackgroundResource(buttonsOn[bNum]);
						}
					});
				}
			}, delay);
			
			delay += gameDataObj.getTimeBetweenChangesMs();
			gameTimer.schedule(new TimerTask() {
				public void run() {
					gameHandler.post(new Runnable() {
						public void run() {
							b.setBackgroundResource(buttonsOff[bNum]);
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
							Button b = ((Button) findViewById(i));
							b.setOnTouchListener(thisPlayGame);
							b.setOnClickListener(thisPlayGame);
						}
						Button replayButton = ((Button) findViewById(R.id.replayButton));
						replayButton.setBackgroundResource(R.drawable.ic_launcher);
						replayButton.setText("");
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
    public boolean onTouch(View v, MotionEvent event) {
    	int buttonNum = v.getId();
    	 if (event.getAction() == MotionEvent.ACTION_DOWN ) {
            v.setBackgroundResource(buttonsOn[buttonNum]);
            return false;
        }
        if (event.getAction() == MotionEvent.ACTION_UP ) {
            v.setBackgroundResource(buttonsOff[buttonNum]);
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
	    	MediaPlayer currentSound = MediaPlayer.create(this, buttonSound[buttonSelected]);
	    	currentSound.setVolume(1.0f, 1.0f);
	        currentSound.start();
	        // currentSound.release();
	        
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
				String message = "Your score is: " + gameDataObj.getScore() + "\nenter your score?";
				gameAlerterObj.alertGameOver("GAME OVER", message, gameDataObj.getScore());
				

			} else {
				
				MediaPlayer currentSound = MediaPlayer.create(this, R.raw.wronganswer);
		    	currentSound.setVolume(1.0f, 1.0f);
		        currentSound.start();
		        
				// display the number of lives left to the user
				String message = gameDataObj.getLives() + " lives left.";
				gameAlerterObj.alert("Nope", message);
				
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
		LayoutParams vParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.50f);
		// add the buttons to each linearLayout row
		// each button created will be given a unique number starting at 0
		int buttonNum = 0;
		for (int i = 0; i < numRows; i++) {
			rows[i] = new LinearLayout(this);
			// set parameters for the current linearLayout row
			rows[i].setLayoutParams(llParams);
			// if we've reached the last linearLayout row and there is an odd
			// number of buttons, add only one button
			if (i == (numRows - 1) && odd) {
				FixedAspectRatioButton btn = new FixedAspectRatioButton(this);
				btn.setLayoutParams(vParams);
				btn.setBackgroundResource(colours[buttonNum]);
				btn.setId(buttonNum);
				rows[i].addView(btn);
				buttonNum++;
				// create an empty text view that fills up the remaining space
				TextView fake = new TextView(this);
				fake.setLayoutParams(vParams);
				rows[i].addView(fake);		
			}
			// otherwise add two buttons to the current linearLayout row
			else {
				FixedAspectRatioButton[] rowButtons = new FixedAspectRatioButton[2];
				for (FixedAspectRatioButton btn : rowButtons) {
					btn = new FixedAspectRatioButton(this);
					btn.setLayoutParams(vParams);
					btn.setBackgroundResource(colours[buttonNum]);
					btn.setId(buttonNum);
					rows[i].addView(btn);
					buttonNum++;
				}
			}
			mainLayout.addView(rows[i]);
		}
	}

}