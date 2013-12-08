package com.ucdmscconversion.memorygame;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;


public class GameAlerter {


	GameData gameDataObj;
	PlayGame playGamObj;
	
	GameAlerter(PlayGame p, GameData gd){
		gameDataObj = gd;
		playGamObj = p;
	}
	
	public void alertGameOver(String title, String message, int score) {
		final int sc = score;
		AlertDialog.Builder builder = new AlertDialog.Builder(playGamObj);
		builder.setTitle(title)
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent go = new Intent(playGamObj, InsertScores.class);
						go.putExtra("Score", sc);
						gameDataObj.reset();
						gameDataObj.commit();
						playGamObj.startActivity(go);
						playGamObj.finish();
					}
				})
				.setNegativeButton(R.string.nothanks,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								gameDataObj.reset();
								gameDataObj.commit();
								playGamObj.startActivity(new Intent(playGamObj, MainMenu.class));
								playGamObj.finish();
							}
						});
		AlertDialog alertGameOver = builder.create();
		alertGameOver.show();
	}

	public void alertQuit(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(playGamObj);
		builder.setTitle(title).setMessage(message).setCancelable(false)
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				})
				.setPositiveButton(R.string.quit, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int which) {
								gameDataObj.reset();
								gameDataObj.commit();
								//playGamObj.onDestroy();
								playGamObj.finish();
								playGamObj.startActivity(new Intent(playGamObj, MainMenu.class));
								
							}
						});
		AlertDialog alertQuit = builder.create();
		alertQuit.show();
	}	
	
	public void alert(String title, String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(playGamObj);
		builder.setTitle(title).setMessage(message).setCancelable(false)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
	});
	AlertDialog alert = builder.create();
	alert.show();
	}
	
}
