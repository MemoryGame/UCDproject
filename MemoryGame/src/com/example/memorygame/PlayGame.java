package com.example.memorygame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.actionbarsherlock.app.SherlockActivity;

public class PlayGame extends SherlockActivity implements OnClickListener, OnTouchListener {

	// starting values
	int sequenceLengthPARENT = 4;
	int numButtonsPARENT = 4;
	int lives = 4;
	int patternPosition = 0;
	int scoresPARENT = 0;
	long timeBetweenChangesMsPARENT = 500;
	int difficultyTypePARENT = 0;
	int roundCounterPARENT = 0;

	ArrayList<Integer> pattern = new ArrayList<Integer>();

	// The buttons will have different background colours
	int[] buttonsOn = new int[] { R.drawable.blue_button_on,
			R.drawable.orange_button_on, R.drawable.yellow_button_on,
			R.drawable.purple_button_on, R.drawable.green_button_on,
			R.drawable.red_button_on, R.drawable.black_button_on,
			R.drawable.pink_button_on };
	int[] buttonsOff = new int[] { R.drawable.blue_button_off,
			R.drawable.orange_button_off, R.drawable.yellow_button_off,
			R.drawable.purple_button_off, R.drawable.green_button_off,
			R.drawable.red_button_off, R.drawable.black_button_off,
			R.drawable.pink_button_off };

	int[] buttonSound = new int[] { R.raw.button1a, R.raw.button2a, 
			R.raw.button3a, R.raw.button4a, R.raw.button5a, 
			R.raw.button6a, R.raw.buttona7a, R.raw.button8a };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/* Custom Themes */
		themeUtils.onActivityCreateSetTheme(this);
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_play_game);
	
		getSupportActionBar().setBackgroundDrawable(null);
		

		SharedPreferences settings = getSharedPreferences("settings", 0);
		int sequenceLength = settings.getInt("seqLen", 4);
		int numButtons = settings.getInt("numBut", 4);
		int scores = settings.getInt("currentScore", 0);
		long timeBetweenChangesMs = settings.getLong("speed", 500);
		int difficultyType = settings.getInt("diffType", 0);
		int roundCounter = settings.getInt("roundCtr", 0);

		//game difficulty logic based on number of rounds sucessfull
		if (roundCounter == 2) {
			if (difficultyType == 0) {
				timeBetweenChangesMs -= 15;		//speed increase
				difficultyType++;
			} else if (difficultyType == 1) {
				sequenceLength++;				//pat len increase
				if (numButtons == 8) {
					difficultyType = 0;
				} else {
					difficultyType++;
				}
			} else if (difficultyType == 2) {
				numButtons++;					//num but increase
				difficultyType = 0;
			}	
			roundCounter = 0;
		} else {
			roundCounter++;
		}

		generateLayout(numButtons, buttonsOff);
		pattern = newPattern(sequenceLength, numButtons);

		Timer myTimer = new Timer();
		Handler myHandler = new Handler();

		long delay = playSequence(myTimer, myHandler, pattern, timeBetweenChangesMs, buttonsOn, buttonsOff);
		setListeners(this, myTimer, myHandler, numButtons, delay);

		sequenceLengthPARENT = sequenceLength;
		numButtonsPARENT = numButtons;
		scoresPARENT = scores;
		timeBetweenChangesMsPARENT = timeBetweenChangesMs;
		difficultyTypePARENT = difficultyType;
		roundCounterPARENT = roundCounter;

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
		// get the user's guess i.e. the number of the button that the user has
		// clicked
		int userGuess = v.getId();
		
		//play button sound 
    	MediaPlayer currentSound = MediaPlayer.create(this, buttonSound[userGuess]);
    	currentSound.setVolume(1.0f, 1.0f);
        currentSound.start();
        currentSound.release();
        
        
		// if the current number in the pattern sequence equals the current userGuess
		if (userGuess == pattern.get(patternPosition)) {
			// if we have reached the end of the pattern sequence then the user
			// has guessed the complete sequence correctly
			if (patternPosition == (pattern.size() - 1)) {
				// restart this activity again so new pattern is generated for
				// the user to guess
				scoresPARENT += 100;

				SharedPreferences settings = getSharedPreferences("settings", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("currentScore", scoresPARENT);
				editor.putInt("seqLen", sequenceLengthPARENT);
				editor.putInt("numBut", numButtonsPARENT);
				editor.putLong("speed", timeBetweenChangesMsPARENT);
				editor.putInt("diffType", difficultyTypePARENT);
				editor.putInt("roundCtr", roundCounterPARENT);
				editor.commit();

				Intent i = getIntent();
				finish();
				startActivity(i);
			}
			// otherwise move on to the next item in the pattern sequence
			patternPosition++;
		}
		// if the user incorrectly guesses the current item in the pattern
		// sequence
		else {
			// reduce lives
			lives--;
			if (lives == 0) {
				// start game over activity
				String message = "Your score is: "
						+ Integer.toString(scoresPARENT)
						+ "\nenter your score?";
				alertGameOver("GAME OVER", message, scoresPARENT);

			} else {
				// display the number of lives left to the user
				currentSound = MediaPlayer.create(this, R.raw.wronganswer);
		    	currentSound.setVolume(1.0f, 1.0f);
		        currentSound.start();
				String message = Integer.toString(lives) + " lives left.";
				alert("Nope", message);
				// keep the current pattern but start the user guess from the
				// beginning
				patternPosition = 0;
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
		final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
		int pixels = (int) (90 * scale + 0.5f);
		LayoutParams bParams = new LayoutParams(pixels, pixels);
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
				Button btn = new Button(this);
				btn.setLayoutParams(bParams);
				btn.setBackgroundResource(colours[buttonNum]);
				btn.setId(buttonNum);
				rows[i].addView(btn);
				buttonNum++;
			}
			// otherwise add two buttons to the current linearLayout row
			else {
				Button[] rowButtons = new Button[2];
				for (Button btn : rowButtons) {
					btn = new Button(this);
					btn.setLayoutParams(bParams);
					btn.setBackgroundResource(colours[buttonNum]);
					btn.setId(buttonNum);
					rows[i].addView(btn);
					buttonNum++;
				}
			}
			mainLayout.addView(rows[i]);
		}
		// finish generating the layout
	}

	private ArrayList<Integer> newPattern(int length, int n) {
		ArrayList<Integer> newPattern = new ArrayList<Integer>();
		// random number generator
		Random r = new Random();
		for (int i = 0; i < length; i++) {
			int x = r.nextInt(n);
			newPattern.add(x);
		}
		return newPattern;
	}

	private long playSequence(Timer t, final Handler h,
			ArrayList<Integer> newPattern, long milliseconds, final int[] on,
			final int[] off) {
		// display this new number pattern to the user using buttons
		// i.e. for each number in the pattern, select the button that has a
		// matching id
		// temporarily change the text of this button for a few seconds
		// then change it back again and move onto the next button
		long delay = 1;
		for (int i = 0; i < newPattern.size(); i++) {
			final Button b = ((Button) findViewById(newPattern.get(i)));
			final int bNum = newPattern.get(i);
			delay += milliseconds;
			t.schedule(new TimerTask() {
				public void run() {
					h.post(new Runnable() {
						public void run() {
							b.setBackgroundResource(on[bNum]);
						}
					});
				}
			}, delay);
			delay += milliseconds;
			t.schedule(new TimerTask() {
				public void run() {
					h.post(new Runnable() {
						public void run() {
							b.setBackgroundResource(off[bNum]);
						}
					});
				}
			}, (delay));
		}
		delay += milliseconds;
		return delay;
	}

	private void setListeners(final PlayGame p, Timer t, final Handler h, final int n, long d) {
		t.schedule(new TimerTask() {
			public void run() {
				h.post(new Runnable() {
					public void run() {
						for (int i = 0; i < n; i++) {
							Button b = ((Button) findViewById(i));
							b.setOnTouchListener(p);
							b.setOnClickListener(p);
						}
					}
				});
			}
		}, d);
	}

	public void alertGameOver(String title, String message, int scores) {
		final int sc = scores;
		AlertDialog.Builder builder = new AlertDialog.Builder(PlayGame.this);
		builder.setTitle(title)
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent go = new Intent(PlayGame.this,
								InsertScores.class);
						go.putExtra("Score", sc);
						setDefaults();
						startActivity(go);
					}
				})
				.setNegativeButton("Nah",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								setDefaults();
								startActivity(new Intent(PlayGame.this,
										MainMenu.class));

							}
						});
		AlertDialog alertGameOver = builder.create();
		alertGameOver.show();
	}

	public void alert(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(PlayGame.this);
		builder.setTitle(title).setMessage(message).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	//set defaults clears pref of game setting once game over	
	private void setDefaults(){
		SharedPreferences settings = getSharedPreferences("settings", 0);
	    SharedPreferences.Editor editor = settings.edit();			    
	    editor.putInt("currentScore", 0);
	    editor.putInt("seqLen", 4);
	    editor.putInt("numBut", 4);
	    editor.putLong("speed", 500);
	    editor.putInt("diffType", 0);
	    editor.putInt("roundCtr", 0);
	    editor.commit();
	}

}
