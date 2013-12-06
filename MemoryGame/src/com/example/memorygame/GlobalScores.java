package com.example.memorygame;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class GlobalScores extends SherlockActivity implements
		OnItemClickListener {

	Boolean continueMusic;
	TextView scoreName, scores, scoreDate;
	ListView lv;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = this.getSharedPreferences("settings", 0);
		int theme = settings.getInt("theme", 0);				
		themeUtils.onActivityCreateSetTheme(this, theme);
		setContentView(R.layout.activity_global_scores);
		int hello = themeUtils.getcTheme();
		getSupportActionBar().setBackgroundDrawable(null);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		lv = (ListView) findViewById(R.id.listView2);
		new Task().execute();

	}

	class Task extends AsyncTask<String, String, Void> {
		InputStream is = null;
		String result = "";
		private ProgressDialog progressDialog = new ProgressDialog(
				GlobalScores.this);

		protected void onPreExecute() {
			progressDialog.setMessage("Downloading Latest Scores...");
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(String... urls) {
			// TODO Auto-generated method stub
			String url_select = "http://andrewdoyle.pw/memorygame/global.php";

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url_select);

			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(param));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();

				// read content
				is = httpEntity.getContent();

			} catch (Exception e) {

				Log.e("log_tag", "Error in http connection " + e.toString());
			}
			try {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = sb.toString();
				System.out.println(result);
				Log.d("log_tag", result);

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("log_tag", "Error converting result " + e.toString());
			}

			return null;
		}

		protected void onPostExecute(Void v) {
			try {

				JSONArray Jarray = new JSONArray(result);

				ArrayList<String> arrayNames = null;
				ArrayList<String> arrayScores = null;
				ArrayList<String> arrayDates = null;

				/*
				 * String[] arrayNames = new String[Jarray.length()]; String[]
				 * arrayScores = new String[Jarray.length()]; String[]
				 * arrayDates = new String[Jarray.length()];
				 */

				for (int i = 0; i < Jarray.length(); i++) {

					JSONObject Jasonobject = Jarray.getJSONObject(i);

					String name = Jasonobject.getString("KEY_NAME");
					String score = Jasonobject.getString("KEY_SCORE");
					String date = Jasonobject.getString("KEY_DATE");

					arrayNames.add(name);
					arrayScores.add(score);
					arrayDates.add(date);

					this.progressDialog.dismiss();
				}

			}

			catch (JSONException e1) {
				Toast.makeText(getBaseContext(), "No Data Found",
						Toast.LENGTH_LONG).show();
				this.progressDialog.dismiss();
			}

			catch (Exception e) {
				// TODO: handle exception
				Log.e("log_tag", "Error parsing data " + e.toString());
				this.progressDialog.dismiss();
			}

		}

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
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.global_scores_menu, menu);

		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			continueMusic = true;
		
			return (true);
		
		case R.id.local:
			finish();
			abGoHome();
			return (true);

	

		
		}
		return (super.onOptionsItemSelected(item));
	}

	// Options activity launched from the actionBar
	public void abOptions() {
		continueMusic = true;
		startActivity(new Intent(this, Options.class));

	}

	public void abInfo() {
		continueMusic = true;
		startActivity(new Intent(this, About.class));

	}

	// Home activity launched from the actionBar
	public void abGoHome() {
		continueMusic = true;
		startActivity(new Intent(this, HighScores.class));

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

}
