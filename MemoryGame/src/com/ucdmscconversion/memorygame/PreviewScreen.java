package com.ucdmscconversion.memorygame;



import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class PreviewScreen extends Activity {

	private static int SPLASH_TIME_OUT = 2500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* FULL SCREEN FEATURE IMPLEMENTED BELOW */
		// // Removes Title Bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.splash);

		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				Intent i = new Intent(PreviewScreen.this, MainMenu.class);
				startActivity(i);

				// close this activity
				finish();
			}
		}, SPLASH_TIME_OUT);

	}
	
	  @Override
	  public void onStart() {
	    super.onStart();
	    //GOOGLE ANALYTICS
	    EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	  }

	  @Override
	  public void onStop() {
	    super.onStop();
	    //GOOGLE ANALYTICS
	    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	  }

}
