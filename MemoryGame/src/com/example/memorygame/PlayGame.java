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
	
	int sequenceLength = 4;
	int numButtons = 8;
	int lives = 4;
	int patternPosition = 0;
	int scores = 0;
	long timeBetweenChangesMs = 500;
	
	//Two ArrayLists for pattern to guess and user guess
	ArrayList<Integer> pattern = new ArrayList<Integer>();
	ArrayList<Integer> userGuess = new ArrayList<Integer>();
	
	//random num generator
	Random r = new Random();
	
	//create an array of buttons that will be added to the layout
	//Button[] buttons = new Button[numButtons];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_game);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundactionbar));

		scores = getIntent().getIntExtra("currentScore", 0);
		
		// start generating the layout 
		LinearLayout mainLayout = ((LinearLayout)findViewById(R.id.mainLayout));
		
	    /*
	     * The layout will be made up of rows of two buttons side-by-side
	     * so if there are 6 buttons then there will be 3 rows
	     * if there are 5 buttons there will also be 3 rows, the last row will only have one button
	     * */
		
		// based on the number of buttons figure out how many rows are needed
		int numRows = numButtons/2;
		// if there is an odd number of buttons we need an extra row and set odd == true
		boolean odd = false;
		if (numButtons%2 != 0){
			numRows++;
			odd=true;
		}

		// The buttons will have different background colours
		final int[] coloursOn = new int[]{R.drawable.blue_button_on, R.drawable.orange_button_on, R.drawable.yellow_button_on, R.drawable.purple_button_on, R.drawable.green_button_on, R.drawable.red_button_on, R.drawable.black_button_on, R.drawable.pink_button_on};
		final int[] coloursOff = new int[]{R.drawable.blue_button_off, R.drawable.orange_button_off, R.drawable.yellow_button_off, R.drawable.purple_button_off, R.drawable.green_button_off, R.drawable.red_button_off, R.drawable.black_button_off, R.drawable.pink_button_off};

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
	        	btn.setBackgroundResource(coloursOff[buttonNum]);
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
		        	btn.setBackgroundResource(coloursOff[buttonNum]);
		        	btn.setId(buttonNum);
		        	rows[i].addView(btn);
		        	buttonNum++;
				}
			} 	
			mainLayout.addView(rows[i]);
		}
		// finish generating the layout 
		
		// generate a new pattern 
		for(int i =0; i<sequenceLength;i++){
			int x = r.nextInt(numButtons);
    		pattern.add(x);
    	}
		
		// display this new number pattern to the user using buttons
		// i.e. for each number in the pattern, select the button that has a matching id
		// temporarily change the text of this button for a few seconds
		// then change it back again and move onto the next button		
		final Timer t = new Timer();
		long delay = 1;
		final Handler h = new Handler();
		for(int i = 0; i < sequenceLength;i++){
			final Button b = ((Button)findViewById(pattern.get(i)));
			final int bNum = pattern.get(i);
			delay += timeBetweenChangesMs;
			t.schedule(new TimerTask(){
				public void run(){
					h.post(new Runnable(){
						public void run(){
				        	b.setBackgroundResource(coloursOn[bNum]);
				        }
				    });
				}
			}, delay);
			delay += timeBetweenChangesMs;
			t.schedule(new TimerTask(){
				public void run(){
					h.post(new Runnable(){
						public void run(){
				        	b.setBackgroundResource(coloursOff[bNum]);
				        }
				    });
				}
			}, (delay));
    	}
		
		delay += timeBetweenChangesMs;
		t.schedule(new TimerTask(){
			public void run(){
				h.post(new Runnable(){
					public void run(){
						for(int i =0; i<numButtons;i++){
							Button b = ((Button)findViewById(i));
							b.setOnClickListener(PlayGame.this);
				    	}
				    }
				});
			}
		}, delay);

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
				i.putExtra("currentScore", scores);
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
		

}



//package com.example.memorygame;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.actionbarsherlock.app.SherlockActivity;
//import com.actionbarsherlock.view.Menu;
//import com.actionbarsherlock.view.MenuInflater;
//import com.actionbarsherlock.view.MenuItem;
//
//public class PlayGame extends SherlockActivity {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_play_game);
//
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		new MenuInflater(this).inflate(R.menu.main_menu, menu);
//
//		return (super.onCreateOptionsMenu(menu));
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//
//		case R.id.action_settings:
//			abOptions();
//			return (true);
//
//		case R.id.action_info:
//			abInfo();
//			return (true);
//
//		}
//		return (super.onOptionsItemSelected(item));
//	}
//
//	// Options activity launched from the actionBar
//	public void abOptions() {
//
//		startActivity(new Intent(this, Options.class));
//
//	}
//
//	public void abInfo() {
//
//		startActivity(new Intent(this, About.class));
//
//	}
//
//}
