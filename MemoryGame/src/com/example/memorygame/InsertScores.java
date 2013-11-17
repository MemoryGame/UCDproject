package com.example.memorygame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;

public class InsertScores extends SherlockActivity {

	EditText playerName;
	Button insertScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_scores);

		//Intent go = getIntent();
		//int highScore = go.getIntExtra("Score", 0);

		playerName = (EditText) findViewById(R.id.playerName);

	}

	public void submitScore(View v) {
		// TODO Auto-generated method stub
		boolean didItWork = true;
		try {
			String name = playerName.getText().toString();
			Intent go = getIntent();
			int highScore = go.getIntExtra("Score", 0);

			DatabaseScores enterScore = new DatabaseScores(InsertScores.this);
			enterScore.open();
			enterScore.createEntry(name, highScore);
			enterScore.close();
		} catch (Exception e) {
			didItWork = false;
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
										Intent toMainScreen = new Intent(InsertScores.this, MainMenu.class);
										toMainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									    startActivity(toMainScreen);
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
	}

}
