package com.example.memorygame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
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
	
	HashMap<String,Object> gameData = new HashMap<String,Object>();
	ArrayList<Integer> pattern = new ArrayList<Integer>();

	// The buttons will have different background colours
	int[] buttonsOn = new int[8];
	int[] buttonsOff = new int[8];
	int[] buttonSound = new int[8];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		/* Custom Themes */
		themeUtils.onActivityCreateSetTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_game);
		getSupportActionBar().setBackgroundDrawable(null);
		
		
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
		
		gameData = reset(gameData);
		
		// If a preferences file by this name does not exist, it will be created when you retrieve an editor 
		SharedPreferences settings = getSharedPreferences("settings", 0);
		int sequenceLength = settings.getInt("sequenceLength", (Integer)gameData.get("sequenceLength"));
		int numButtons = settings.getInt("numButtons", (Integer)gameData.get("numButtons"));
		int score = settings.getInt("score", (Integer)gameData.get("score"));
		int difficultyType = settings.getInt("difficultyType", (Integer)gameData.get("difficultyType"));
		int roundCounter = settings.getInt("roundCounter", (Integer)gameData.get("roundCounter"));
		long timeBetweenChangesMs = settings.getLong("timeBetweenChangesMs", (Long)gameData.get("timeBetweenChangesMs"));
		String patternString = settings.getString("patternString", null);
		
		if( patternString==null ){
			//game difficulty logic based on number of rounds sucessfull
			if (roundCounter == 2) {
				if (difficultyType == 0) {
					timeBetweenChangesMs -= 15;		//speed increase
					difficultyType++;
				} else if (difficultyType == 1) {
					sequenceLength++;				//pat len increase
					if (numButtons == 6) {
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
			pattern = newPattern(sequenceLength, numButtons);
			System.out.println("New pattern generated");
		} else {
			pattern = StringToIntArrayList(patternString);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("patternStr", null);
			editor.commit();
		}


		generateLayout(numButtons, buttonsOff);
		
		Timer myTimer = new Timer();
		Handler myHandler = new Handler();

		long delay = playSequence(myTimer, myHandler, pattern, timeBetweenChangesMs, buttonsOn, buttonsOff);
		setListeners(this, myTimer, myHandler, numButtons, delay);

		gameData.put("sequenceLength", sequenceLength);
		gameData.put("numButtons", numButtons);
		gameData.put("score", score);
		gameData.put("difficultyType", difficultyType);
		gameData.put("roundCounter", roundCounter);
		gameData.put("timeBetweenChangesMs", timeBetweenChangesMs);
		

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
		
		if(userGuess == R.id.replayButton)
		{
			String patternString = IntArrayListToString(pattern);

			SharedPreferences settings = getSharedPreferences("settings", 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("score", (Integer)gameData.get("score"));
			editor.putInt("sequenceLength", (Integer)gameData.get("sequenceLength"));
			editor.putInt("numButtons", (Integer)gameData.get("numButtons"));
			editor.putInt("difficultyType", (Integer)gameData.get("difficultyType"));
			editor.putInt("roundCounter", (Integer)gameData.get("roundCounter"));
			editor.putLong("timeBetweenChangesMs", (Long)gameData.get("timeBetweenChangesMs"));
			editor.putString("patternString", patternString);
			editor.commit();

			Intent i = getIntent();							
			finish();
			startActivity(i);

		} else if (userGuess == pattern.get((Integer)gameData.get("patternPosition"))) {
			// if the current number in the pattern sequence equals the current userGuess
			
			//play button sound 
	    	MediaPlayer currentSound = MediaPlayer.create(this, buttonSound[userGuess]);
	    	currentSound.setVolume(1.0f, 1.0f);
	        currentSound.start();
	       // currentSound.release();
			
			// if we have reached the end of the pattern sequence then the user
			// has guessed the complete sequence correctly
	        
			if ((Integer)gameData.get("patternPosition") == (pattern.size() - 1)) {
				// restart this activity again so new pattern is generated for
				// the user to guess

				

				gameData.put("score", ((Integer)gameData.get("score")+100));

				SharedPreferences settings = getSharedPreferences("settings", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("score", (Integer)gameData.get("score"));
				editor.putInt("sequenceLength", (Integer)gameData.get("sequenceLength"));
				editor.putInt("numButtons", (Integer)gameData.get("numButtons"));
				editor.putInt("difficultyType", (Integer)gameData.get("difficultyType"));
				editor.putInt("roundCounter", (Integer)gameData.get("roundCounter"));
				editor.putLong("timeBetweenChangesMs", (Long)gameData.get("timeBetweenChangesMs"));
				editor.putString("patternString", null);
				editor.commit();				
				
				Intent i = getIntent();
				currentSound = MediaPlayer.create(this, R.raw.correct);
		    	currentSound.setVolume(1.0f, 1.0f);
		        currentSound.start();
								
				finish();
				startActivity(i);
			}
			// otherwise move on to the next item in the pattern sequence
	        gameData.put("patternPosition", ((Integer)gameData.get("patternPosition")+1));
		} else {
			// if the user incorrectly guesses the current item in the pattern reduce lives
	        gameData.put("lives", ((Integer)gameData.get("lives")-1));
			if ((Integer)gameData.get("lives") == 0) {
				// start game over activity
				String message = "Your score is: "
						+ (Integer)gameData.get("score")
						+ "\nenter your score?";
				alertGameOver("GAME OVER", message, (Integer)gameData.get("score"));

			} else {
				// display the number of lives left to the user
				MediaPlayer currentSound = MediaPlayer.create(this, R.raw.wronganswer);
		    	currentSound.setVolume(1.0f, 1.0f);
		        currentSound.start();
				String message = (Integer)gameData.get("lives") + " lives left.";
				alert("Nope", message);
				// keep the current pattern but start the user guess from the
				// beginning
		        gameData.put("patternPosition", Integer.valueOf(0));
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
		LayoutParams bParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 0.50f);
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
				btn.setLayoutParams(bParams);
				btn.setBackgroundResource(colours[buttonNum]);
				btn.setId(buttonNum);
				rows[i].addView(btn);
				buttonNum++;
				
				/*
				FixedAspectRatioButton fakeBtn = new FixedAspectRatioButton(this);
				fakeBtn.setLayoutParams(bParams);
				fakeBtn.setBackgroundResource(R.drawable.no_button);
				rows[i].addView(btn);
				*/				
			}
			// otherwise add two buttons to the current linearLayout row
			else {
				FixedAspectRatioButton[] rowButtons = new FixedAspectRatioButton[2];
				for (FixedAspectRatioButton btn : rowButtons) {
					btn = new FixedAspectRatioButton(this);
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
						Button replayButton = ((Button) findViewById(R.id.replayButton));
						replayButton.setBackgroundResource(R.drawable.replay_button);
						replayButton.setText("");
						replayButton.setOnClickListener(p);
					}
				});
			}
		}, d);
	}

	public void alertGameOver(String title, String message, int score) {
		final int sc = score;
		AlertDialog.Builder builder = new AlertDialog.Builder(PlayGame.this);
		builder.setTitle(title)
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent go = new Intent(PlayGame.this,
								InsertScores.class);
						go.putExtra("Score", sc);
						gameData = reset(gameData);
						setDefaults(gameData);
						startActivity(go);
						finish();
					}
				})
				.setNegativeButton("Nah",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								gameData = reset(gameData);
								setDefaults(gameData);
								startActivity(new Intent(PlayGame.this,
										MainMenu.class));
								finish();
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
	private void setDefaults( HashMap<String,Object> hm ){
		SharedPreferences settings = getSharedPreferences("settings", 0);
	    SharedPreferences.Editor editor = settings.edit();			    
	    editor.putInt("score", (Integer)hm.get("score"));
	    editor.putInt("sequenceLength", (Integer)hm.get("sequenceLength"));
	    editor.putInt("numButtons", (Integer)hm.get("numButtons"));
	    editor.putLong("timeBetweenChangesMs", (Long)hm.get("timeBetweenChangesMs"));
	    editor.putInt("difficultyType", (Integer)hm.get("difficultyType"));
	    editor.putInt("roundCounter", (Integer)hm.get("rounCounter"));
	    editor.commit();
	}
	
	HashMap<String,Object> reset( HashMap<String,Object> hm ){
		hm.put("sequenceLength", Integer.valueOf(4));
		hm.put("numButtons", Integer.valueOf(6));
		hm.put("lives", Integer.valueOf(4));
		hm.put("patternPosition", Integer.valueOf(0));
		hm.put("score", Integer.valueOf(0));
		hm.put("difficultyType", Integer.valueOf(0));
		hm.put("roundCounter", Integer.valueOf(0));
		hm.put("timeBetweenChangesMs", Long.valueOf(500));
		hm.put("patternString", null);		
		return hm;
	}
	
	String IntArrayListToString( ArrayList<Integer> a ){
		String s = "";
		for (Integer i: a){
			s = s + i.toString();
		}
		return s;
	}
	
	ArrayList<Integer> StringToIntArrayList( String s ){
		ArrayList<Integer> a = new ArrayList<Integer>();
		for (char c: s.toCharArray()){
			int n = c - 48; 
			a.add(n);
		}
		return a;
	}

}

//Source: aleb on GitHub src/com/triposo/barone/FixedAspectRatioFrameLayout.java
class FixedAspectRatioButton extends Button{
	
	private float aspectRatio;
	
	FixedAspectRatioButton(Context context){
		super(context);
	}	
	
  @Override protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){
  	
  	aspectRatio = 1.0000f;
  	
  	int widthMode = MeasureSpec.getMode(widthMeasureSpec);
  	int heightMode = MeasureSpec.getMode(heightMeasureSpec);
  	int receivedWidth = MeasureSpec.getSize(widthMeasureSpec);
  	int receivedHeight = MeasureSpec.getSize(heightMeasureSpec);
  	
  	int measuredWidth;
  	int measuredHeight;
  	boolean widthDynamic;
  	
  	if (heightMode == MeasureSpec.EXACTLY) {
  		if (widthMode == MeasureSpec.EXACTLY) {
  			widthDynamic = receivedWidth == 0;
  		} else {
  			widthDynamic = true;
  	    }
  	} else if (widthMode == MeasureSpec.EXACTLY) {
  		widthDynamic = false;
  	} else {
  	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  	    return;
  	}
  	
  	if (widthDynamic) {
  		// Width is dynamic.
  		int w = (int) (receivedHeight * aspectRatio);
  		measuredWidth = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
  		measuredHeight = heightMeasureSpec;
  	} else {
  		// Height is dynamic.
  		measuredWidth = widthMeasureSpec;
  		int h = (int) (receivedWidth / aspectRatio);
  		measuredHeight = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
  	}
  	super.onMeasure(measuredWidth, measuredHeight);
  }
  
}
