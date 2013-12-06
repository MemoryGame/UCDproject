package com.example.memorygame;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

public class InsertScores extends SherlockActivity {

	EditText playerName;
	Button insertScore;

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
		playerName = (EditText) findViewById(R.id.playerName);

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
