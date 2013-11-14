package com.example.memorygame;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseScores {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_SCORE = "score";
	public static final String KEY_DATE = "date";

	private static final String DATABASE_NAME = "HighScores";
	private static final String DATABASE_TABLE = "HighScore";
	private static final int DATABASE_VERSION = 1;

	private DbHelper ourHelper;
	private final Context ourContext;

	private SQLiteDatabase ourDatabase;

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME
					+ " STRING NOT NULL, " + KEY_SCORE + " INTEGER NOT NULL, "
					+ KEY_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}

	}

	public DatabaseScores(Context c) {
		ourContext = c;
	}

	public DatabaseScores open() throws SQLException {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	public long createEntry(String name, int score) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_SCORE, score);

		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}

	/**NEED TO BREAK INTO TWO SEPERATE METHODS**/
	public ArrayList<String> getData() {
		// TODO Auto-generated method stub
		String[] columns = new String[] { KEY_ROWID, KEY_NAME, KEY_SCORE, KEY_DATE };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, KEY_SCORE);

		ArrayList<String> result = new ArrayList<String>();

		int iName = c.getColumnIndex(KEY_NAME);
		int iScore = c.getColumnIndex(KEY_SCORE);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			
			String name = c.getString(iName);
			String score = c.getString(iScore);

			result.add(name);
			result.add(score);
		}

		return result;
	}

	public void deleteModule(String rowID) {
		// TODO Auto-generated method stub
		ourDatabase.delete(DATABASE_TABLE, null, null);
	}

}
