package com.ucdmscconversion.memorygame;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;


public class HighScores extends SherlockActivity implements OnItemClickListener {

	Boolean continueMusic;
	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/* Custom Themes */
		SharedPreferences settings = this.getSharedPreferences("settings", 0);
		int theme = settings.getInt("theme", 0);				
		themeUtils.onActivityCreateSetTheme(this, theme);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_high_scores);

		getSupportActionBar().setBackgroundDrawable(null);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// Create listView to display results from database
		lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(new VivzAdapter(this));

	}

	/* Require override to continue music on back button pressed */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			continueMusic = true;

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.high_scores_menu, menu);

		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			continueMusic = true;

			return (true);
		case R.id.discard:

			clearScores();
			return (true);

		case R.id.global:
			finish();
			abGlobal();
			return (true);

		}
		return (super.onOptionsItemSelected(item));
	}

	// Options activity launched from the actionBar
	public void abOptions() {
		continueMusic = true;
		startActivity(new Intent(this, Options.class));

	}

	public void abGlobal() {
		continueMusic = true;
		startActivity(new Intent(this, GlobalScores.class));

	}

	// Home activity launched from the actionBar
	public void abGoHome() {
		continueMusic = true;
		startActivity(new Intent(this, MainMenu.class));

	}

	// ERASE THE HIGH SCORES
	public boolean clearScores() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				HighScores.this);

		// Set Title
		alertDialogBuilder.setTitle("Erase High Scores");

		// Set dialog message
		alertDialogBuilder
				.setMessage("Are you sure you want to delete your highscores?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// open the database and call delete method
								deleteData();
								// refresh the activity
								finish();
								startActivity(getIntent());
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});

		// Create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// Show it
		alertDialog.show();

		return (true);

	}

	/* Method for deleting the High Score database */
	public void deleteData() {
		DatabaseScores deleteScores = new DatabaseScores(this);
		deleteScores.open();
		deleteScores.deleteModule();
		deleteScores.close();

	}

	/* Start of Custom ListView Layout */
	class SingleRow {
		String name;
		String score;
		int image;
		String id;

		SingleRow(String name, String score, int image) {
			this.name = name;
			this.score = score;
			this.image = image;

		}
	}

	class VivzAdapter extends BaseAdapter {

		ArrayList<SingleRow> list;
		Context context;

		VivzAdapter(Context c) {
			context = c;
			list = new ArrayList<SingleRow>();
			DatabaseScores info = new DatabaseScores(getBaseContext());
			info.open();
			ArrayList<String> names2 = info.getNameData();
			ArrayList<String> scores2 = info.getScoreData();

			String[] names = new String[names2.size()];
			String[] scores = new String[scores2.size()];

			names = names2.toArray(names);
			scores = scores2.toArray(scores);

			int length = names.length;

			int[] images = new int[length];

			for (int i = 0; i < scores.length; i++) {

				images[i] = R.drawable.rank_image01 + i;
			}

			for (int i = 0; i < scores.length; i++) {
				list.add(new SingleRow(names[i], scores[i], images[i]));
			}
			info.close();

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View row = inflater.inflate(R.layout.single_row, parent, false);
			TextView name = (TextView) row.findViewById(R.id.textView1);
			TextView score = (TextView) row.findViewById(R.id.textView2);
			ImageView image = (ImageView) row.findViewById(R.id.imageView1);

			SingleRow temp = list.get(position);

			name.setText(temp.name);
			score.setText(temp.score);
			image.setImageResource(temp.image);

			return row;
		}

	}

	/* End of Custom ListView Layout */

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

}
