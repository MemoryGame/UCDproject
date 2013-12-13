package com.ucdmscconversion.memorygame;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.widget.SimpleAdapter;
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

	int[] rank = new int[] { R.drawable.rank_image01, R.drawable.rank_image02,
			R.drawable.rank_image03, R.drawable.rank_image04,
			R.drawable.rank_image05, R.drawable.rank_image06,
			R.drawable.rank_image07, R.drawable.rank_image08,
			R.drawable.rank_image09, R.drawable.rank_image10,
			R.drawable.rank_image11, R.drawable.rank_image12,
			R.drawable.rank_image13, R.drawable.rank_image14,
			R.drawable.rank_image15, R.drawable.rank_image16,
			R.drawable.rank_image17, R.drawable.rank_image18,
			R.drawable.rank_image19, R.drawable.rank_image20,
			R.drawable.rank_image21, R.drawable.rank_image22,
			R.drawable.rank_image23, R.drawable.rank_image24,
			R.drawable.rank_image25, R.drawable.rank_image26,
			R.drawable.rank_image27, R.drawable.rank_image28,
			R.drawable.rank_image29, R.drawable.rank_image30,
			R.drawable.rank_image31, R.drawable.rank_image32,
			R.drawable.rank_image33, R.drawable.rank_image34,
			R.drawable.rank_image35, R.drawable.rank_image36,
			R.drawable.rank_image37, R.drawable.rank_image38,
			R.drawable.rank_image39, R.drawable.rank_image40,
			R.drawable.rank_image41, R.drawable.rank_image42,
			R.drawable.rank_image43, R.drawable.rank_image44,
			R.drawable.rank_image45, R.drawable.rank_image46,
			R.drawable.rank_image47, R.drawable.rank_image48,
			R.drawable.rank_image49, R.drawable.rank_image50,
			R.drawable.rank_image51, R.drawable.rank_image52,
			R.drawable.rank_image53, R.drawable.rank_image54,
			R.drawable.rank_image55, R.drawable.rank_image56,
			R.drawable.rank_image57, R.drawable.rank_image58,
			R.drawable.rank_image59, R.drawable.rank_image60,
			R.drawable.rank_image61, R.drawable.rank_image62,
			R.drawable.rank_image63, R.drawable.rank_image64,
			R.drawable.rank_image65, R.drawable.rank_image66,
			R.drawable.rank_image67, R.drawable.rank_image68,
			R.drawable.rank_image69, R.drawable.rank_image70,
			R.drawable.rank_image71, R.drawable.rank_image72,
			R.drawable.rank_image73, R.drawable.rank_image74,
			R.drawable.rank_image75, R.drawable.rank_image76,
			R.drawable.rank_image77, R.drawable.rank_image78,
			R.drawable.rank_image79, R.drawable.rank_image80,
			R.drawable.rank_image81, R.drawable.rank_image82,
			R.drawable.rank_image83, R.drawable.rank_image84,
			R.drawable.rank_image85, R.drawable.rank_image86,
			R.drawable.rank_image87, R.drawable.rank_image88,
			R.drawable.rank_image89, R.drawable.rank_image90,
			R.drawable.rank_image91, R.drawable.rank_image92,
			R.drawable.rank_image93, R.drawable.rank_image94,
			R.drawable.rank_image95, R.drawable.rank_image96,
			R.drawable.rank_image97, R.drawable.rank_image98,
			R.drawable.rank_image99, R.drawable.rank_image100,

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		/* Custom Themes */
		SharedPreferences settings = this.getSharedPreferences("settings", 0);
		int theme = settings.getInt("theme", 0);				
		themeUtils.onActivityCreateSetTheme(this, theme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_global_scores);
		//int hello = themeUtils.getcTheme();
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
				Toast.makeText(getBaseContext(), "Cannot Connect To Leaderboard",
						Toast.LENGTH_LONG).show();
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

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("log_tag", "Error converting result " + e.toString());
				Toast.makeText(getBaseContext(), "Error Retrieving Leaderboard",
						Toast.LENGTH_LONG).show();
			}

			return null;
		}

		protected void onPostExecute(Void v) {
			try {

				JSONArray Jarray = new JSONArray(result);

				String[] arrayNames = new String[Jarray.length()];
				String[] arrayScores = new String[Jarray.length()];
				String[] arrayDates = new String[Jarray.length()];

				List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

				for (int i = 0; i < Jarray.length(); i++) {

					HashMap<String, String> hm = new HashMap<String, String>();

					JSONObject Jasonobject = Jarray.getJSONObject(i);

					arrayNames[i] = Jasonobject.getString("KEY_NAME");
					arrayScores[i] = Jasonobject.getString("KEY_SCORE");
					arrayDates[i] = Jasonobject.getString("KEY_DATE");

					hm.put("name", arrayNames[i]);
					hm.put("score", arrayScores[i]);
					hm.put("ranks", Integer.toString(rank[i]));
					aList.add(hm);

					String[] from = { "ranks", "name", "score" };

					int[] to = { R.id.imageView1, R.id.textView1,
							R.id.textView2 };

					SimpleAdapter adapter = new SimpleAdapter(getBaseContext(),
							aList, R.layout.single_row, from, to);

					ListView listView = (ListView) findViewById(R.id.listView2);

					listView.setAdapter(adapter);

					this.progressDialog.dismiss();
				}

			}

			catch (JSONException e1) {
				Toast.makeText(getBaseContext(), "No Highscores Present",
						Toast.LENGTH_LONG).show();
				this.progressDialog.dismiss();
			}

			catch (Exception e) {
				// TODO: handle exception
				Log.e("log_tag", "Error parsing data " + e.toString());
				this.progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "Error Retrieving Leaderboard",
						Toast.LENGTH_LONG).show();
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
