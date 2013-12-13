package com.ucdmscconversion.memorygame;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class InsertScores extends SherlockActivity {

	EditText playerName;
	TextView textView1;
	Button insertScore;
	Boolean continueMusic;

	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
			.permitAll().build();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/* Custom Themes */
		SharedPreferences settings = this.getSharedPreferences("settings", 0);
		int theme = settings.getInt("theme", 0);				
		themeUtils.onActivityCreateSetTheme(this, theme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_scores);
		getSupportActionBar().setBackgroundDrawable(null);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		/* Decide what background to put on the textviews */

		
		playerName = (EditText) findViewById(R.id.playerName);
		textView1 = (TextView) findViewById(R.id.textView1);
		insertScore = (Button) findViewById(R.id.button1);

		int sdk = android.os.Build.VERSION.SDK_INT;

		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			textView1.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.kidscornersgrey));
			playerName.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.kidscorners));
			insertScore.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.kidscornersgrey));

		} else {
			textView1.setBackground(getResources().getDrawable(
					R.drawable.kidscornersgrey));
			playerName.setBackground(getResources().getDrawable(
					R.drawable.kidscorners));
			insertScore.setBackground(getResources().getDrawable(
					R.drawable.kidscornersgrey));

		}

		textView1.setTextColor(getResources().getColor(R.color.white));
		playerName.setTextColor(getResources().getColor(R.color.Grey_gunmetal));
		insertScore.setTextColor(getResources().getColor(R.color.white));

		textView1.setPadding(10, 10, 10, 10);
		playerName.setPadding(10, 10, 10, 10);
		insertScore.setPadding(10, 10, 10, 10);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.insert_scores, menu);

		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
			continueMusic = true;
			startActivity(new Intent(this,MainMenu.class));

			return (true);
		}

	

		return (super.onOptionsItemSelected(item));
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		    if (keyCode == KeyEvent.KEYCODE_BACK) {
		      finish();
		      startActivity(new Intent(this,MainMenu.class));
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


	public void submitScore(View v) {
		// TODO Auto-generated method stub
		boolean didItWork = true;
		try {
			String name = playerName.getText().toString();
			Intent go = getIntent();
			int highScore = go.getIntExtra("Score", 0);
			String sHighScore = Integer.toString(highScore);
			
			DatabaseScores enterScore = new DatabaseScores(InsertScores.this);
			enterScore.open();
			enterScore.createEntry(name, highScore);
			enterScore.close();

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("name", name));
			nameValuePairs.add(new BasicNameValuePair("score", sHighScore));

			StrictMode.setThreadPolicy(policy);

			try {				
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://andrewdoyle.pw/memorygame/update-global.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httpclient.execute(httppost);

			} catch (Exception e) {
				didItWork = false;
				Toast.makeText(getApplicationContext(), "Connection fail",
						Toast.LENGTH_SHORT).show();

			}

		} finally {
			if (didItWork) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Success");
				builder.setMessage("HighScore Added Successfully")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										finish();
										Intent toMainScreen = new Intent(
												InsertScores.this,
												MainMenu.class);
										toMainScreen
												.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(toMainScreen);
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
	}

}
