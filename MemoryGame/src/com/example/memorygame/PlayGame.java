package com.example.memorygame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockActivity;

public class PlayGame extends SherlockActivity implements OnClickListener {
	
	//starting values
	int sequenceLength = 4;
	int numButtons = 4;
	int lives = 4;
	int patternPosition = 0;
	int scores = 0;
	long timeBetweenChangesMs = 500;
	int difficultyType = 0;
	int roundCounter= 0;
		
	//Two ArrayLists for pattern to guess and user guess
	ArrayList<Integer> pattern = new ArrayList<Integer>();
	ArrayList<Integer> userGuess = new ArrayList<Integer>();
	
	// The buttons will have different background colours
	final int[] buttonsOn = new int[]{R.drawable.blue_button_on, R.drawable.orange_button_on, R.drawable.yellow_button_on, R.drawable.purple_button_on, R.drawable.green_button_on, R.drawable.red_button_on, R.drawable.black_button_on, R.drawable.pink_button_on};
	final int[] buttonsOff = new int[]{R.drawable.blue_button_off, R.drawable.orange_button_off, R.drawable.yellow_button_off, R.drawable.purple_button_off, R.drawable.green_button_off, R.drawable.red_button_off, R.drawable.black_button_off, R.drawable.pink_button_off};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Custom Themes */
		themeUtils.onActivityCreateSetTheme(this);
		setContentView(R.layout.activity_play_game);
		int hello = themeUtils.getcTheme();
		if (hello == 0) {
			getSupportActionBar().setBackgroundDrawable(
					getResources().getDrawable(R.drawable.backgroundactionbar));
		}
		if (hello == 1) {
			getSupportActionBar().setBackgroundDrawable(
					getResources().getDrawable(R.drawable.blue_background));
		}

		//extract bundled extras for difficulty increase
		Bundle extras = getIntent().getExtras();
		if (extras != null){		
			//set vals do logic here
			scores = extras.getInt("currentScore");
			sequenceLength = extras.getInt("seqLen");
			numButtons = extras.getInt("numBut");
			timeBetweenChangesMs = extras.getLong("speed");		
			roundCounter= extras.getInt("roundCtr");
			difficultyType = extras.getInt("diffType");
			//set difficulty values here 
			if(roundCounter ==1){
				
				if (difficultyType ==0){
						timeBetweenChangesMs -=25;
							difficultyType++;
					}
				else if (difficultyType ==1){
						sequenceLength++;
						if(numButtons==8){
								difficultyType=0;
							}else{
								difficultyType++;
								}
					}
				else if (difficultyType == 2){
						numButtons++;
						difficultyType=0;
				}
			}
		}
		
		generateLayout(numButtons, buttonsOff);
		pattern = newPattern(sequenceLength, numButtons);
		
		Timer myTimer = new Timer();
		Handler myHandler = new Handler();
		
		long delay = playSequence(myTimer, myHandler, pattern, timeBetweenChangesMs, buttonsOn, buttonsOff);
		setOnClickListeners(this, myTimer, myHandler, numButtons, delay);
		
      	}


	@Override
	public void onClick(View v) {
		// get the user's guess i.e. the number of the button that the user has clicked
		int userGuess = v.getId();
		// if the current number in the pattern sequence equals the current userGuess
		if(userGuess==pattern.get(patternPosition)){
			// if we have reached the end of the pattern sequence then the user has guessed the complete sequence correctly
			if(patternPosition==(pattern.size()-1)){
				// restart this activity again so new pattern is generated for the user to guess
				Intent i = getIntent();
				scores +=10;
				Bundle extras = new Bundle();
				extras.putInt("currentScore", scores);
				extras.putInt("seqLen", sequenceLength);
				extras.putInt("numBut", numButtons);
				extras.putLong("speed", timeBetweenChangesMs);
				extras.putInt("diffType", difficultyType);
				extras.putInt("roundCtr", roundCounter);
				i.putExtras(extras);
				finish();
				startActivity(i);
			}
			// otherwise move on to the next item in the pattern sequence
			patternPosition++;
		}
		// if the user incorrectly guesses the current item in the pattern sequence 
		else{
			// reduce lives
			lives--;
			if(lives==0){
				// start game over activity
				String m = "Your score is: "+ Integer.toString(scores)+"\nenter your score?";
				alerta("GAME OVER", m ,scores);
				
			}
			else{
			// display the number of lives left to the user
			String message = Integer.toString(lives) + " lives left.";
			alert("Nope", message);
			// keep the current pattern sequence but start the user guess from the beginning
			patternPosition = 0;
			}
		}
		
	}
	
	public void alert(String title, String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(PlayGame.this);
        builder.setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();	
	}
	
	public void alerta(String title, String message,int scores){
		AlertDialog.Builder builder = new AlertDialog.Builder(PlayGame.this);
		final int sc = scores;
        builder.setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent go = new Intent(PlayGame.this, InsertScores.class);
				go.putExtra("Score", sc);
				startActivity(go);		
			}
		})
		.setNegativeButton("Nah", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				startActivity(new Intent(PlayGame.this, MainMenu.class));
				
			}
		});
        AlertDialog alerta = builder.create();
        alerta.show();
	}

	
	private void generateLayout(int numBtns, final int[] colours) {
		// start generating the layout 
		LinearLayout mainLayout = ((LinearLayout)findViewById(R.id.mainLayout));	
	    /*
	     * The layout will be made up of rows of two buttons side-by-side
	     * so if there are 6 buttons then there will be 3 rows
	     * if there are 5 buttons there will also be 3 rows, the last row will only have one button
	     * */
		// based on the number of buttons figure out how many rows are needed
		int numRows = numBtns/2;
		// if there is an odd number of buttons we need an extra row and set odd == true
		boolean odd = false;
		if (numBtns%2 != 0){
			numRows++;
			odd=true;
		}
		// each "row" talked about above is actually a linearLayout
		LinearLayout[] rows = new LinearLayout[numRows];
		// create a set of default linearLayout parameters to be given to every linearLayout 
		LayoutParams llParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		LayoutParams bParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		// add the buttons to each linearLayout row
		// each button created will be given a unique number starting at 0
		int buttonNum = 0;
		for(int i=0; i<numRows; i++){	
			rows[i] = new LinearLayout(this);
			// set parameters for the current linearLayout row
			rows[i].setOrientation(LinearLayout.HORIZONTAL);
			rows[i].setLayoutParams(llParams);	
			// if we've reached the last linearLayout row and there is an odd number of buttons, add only one button
			if( i==(numRows-1) && odd){
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
				for(Button btn: rowButtons){
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
		//random number generator
		Random r = new Random();
		for(int i =0; i<length;i++){
			int x = r.nextInt(n);
    		newPattern.add(x);
    	}
		return newPattern;
	}

	private long playSequence(Timer t, final Handler h, ArrayList<Integer> newPattern, long milliseconds, final int[] on, final int[] off) {
		// display this new number pattern to the user using buttons
		// i.e. for each number in the pattern, select the button that has a matching id
		// temporarily change the text of this button for a few seconds
		// then change it back again and move onto the next button		
		long delay = 1;
		for(int i = 0; i < newPattern.size();i++){
			final Button b = ((Button)findViewById(newPattern.get(i)));
			final int bNum = newPattern.get(i);
			delay += milliseconds;
			t.schedule(new TimerTask(){
				public void run(){
					h.post(new Runnable(){
						public void run(){
				        	b.setBackgroundResource(on[bNum]);
				        }
				    });
				}
			}, delay);
			delay += milliseconds;
			t.schedule(new TimerTask(){
				public void run(){
					h.post(new Runnable(){
						public void run(){
				        	b.setBackgroundResource(off[bNum]);
				        }
				    });
				}
			}, (delay));
    	}
		delay += milliseconds;
		return delay;
	}
	
	private void setOnClickListeners(final PlayGame p, Timer t, final Handler h, final int n, long d) {
		t.schedule(new TimerTask(){
			public void run(){
				h.post(new Runnable(){
					public void run(){
						for(int i =0; i<n;i++){
							Button b = ((Button)findViewById(i));
							b.setOnClickListener(p);
				    	}
				    }
				});
			}
		}, d);
	}
	

}
