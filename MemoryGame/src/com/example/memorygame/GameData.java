package com.example.memorygame;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;

public class GameData {
	
	private ArrayList<Integer> pattern;
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	
	private final int defaultSequenceLength = 4;
	private final int defaultNumButtons = 4;
	private final int defaultLives = 4;
	private final int defaultPatternPosition = 0;
	private final int defaultScore = 0;
	private final int defaultDifficultyType = 0;
	private final int defaultRoundCounter = 0;
	private final long defaultTimeBetweenChangesMs = 500;
	private final String defaultPatternString = null;
	
	private int sequenceLength;
	private int numButtons;
	private int lives;
	private int patternPosition;
	private int score;
	private int difficultyType;
	private int roundCounter;
	private long timeBetweenChangesMs;
	private String patternString;		
	
	GameData(Context context){
		this.reset();
		
		pattern = new ArrayList<Integer>();
		settings = context.getSharedPreferences("settings", 0);
		editor = settings.edit();	
		
		sequenceLength = settings.getInt("sequenceLength", defaultSequenceLength);
		numButtons = settings.getInt("numButtons", defaultNumButtons);
		score = settings.getInt("score", defaultScore);
		difficultyType = settings.getInt("difficultyType", defaultDifficultyType);
		roundCounter = settings.getInt("roundCounter", defaultRoundCounter);
		timeBetweenChangesMs = settings.getLong("timeBetweenChangesMs", defaultTimeBetweenChangesMs);
		patternString = settings.getString("patternString", defaultPatternString);	
		lives =settings.getInt("lives", lives);
	}
	
	void reset(){
		sequenceLength = defaultSequenceLength;
		numButtons = defaultNumButtons;
		lives = defaultLives;
		patternPosition = defaultPatternPosition;
		score = defaultScore;
		difficultyType = defaultDifficultyType;
		roundCounter = defaultRoundCounter;
		timeBetweenChangesMs = defaultTimeBetweenChangesMs;
		patternString = defaultPatternString;				
	}
	
	void commit(){		
		editor.putInt("sequenceLength", sequenceLength);
		editor.putInt("numButtons", numButtons);
		editor.putInt("lives", lives);
		editor.putInt("score", score);
		editor.putInt("difficultyType", difficultyType);
		editor.putInt("roundCounter", roundCounter);
		editor.putLong("timeBetweenChangesMs", timeBetweenChangesMs);
		editor.putString("patternString", patternString);	
	    editor.commit();			
	}	
	
	void setDifficulty(){
		//game difficulty logic based on number of rounds successfull
		if (roundCounter == 2) {
			if (difficultyType == 0) {
				timeBetweenChangesMs -= 15;		//speed increase
				difficultyType++;
			} else if (difficultyType == 1) {
				sequenceLength++;				//pattern length increase
				if (numButtons == 8) {
					difficultyType = 0;
				} else {
					difficultyType++;
				}
			} else if (difficultyType == 2) {
				numButtons++;					//number of buttons increase
				difficultyType = 0;
			}	
			roundCounter = 0;
		} else {
			roundCounter++;
		}		
		
	}
	
	/* Getter methods */
	
	int getSequenceLength(){
		return sequenceLength;
	}
	int getNumButtons(){
		return numButtons;
	}
	int getLives(){
		return lives;
	}
	int getPatternPosition(){
		return patternPosition;
	}
	int getScore(){
		return score;
	}
	int getDifficultyType(){
		return difficultyType;
	}	
	int getRoundCounter(){
		return roundCounter;
	}
	long getTimeBetweenChangesMs(){
		return timeBetweenChangesMs;
	}
	String getPatternString(){
		return patternString;
	}
	ArrayList<Integer> getPattern(){
		return pattern;
	}

	/* Setter methods */
	
	void setSequenceLength(int i){
		sequenceLength = i;
	}
	void setNumButtons(int i){
		numButtons = i;
	}
	void setLives(int i){
		lives = i;
	}
	void setPatternPosition(int i){
		patternPosition = i;
	}
	void setScore(int i){
		score = i;
	}
	void setDifficultyType(int i){
		difficultyType = i;
	}	
	void setRoundCounter(int i){
		roundCounter = i;
	}
	void setTimeBetweenChangesMs(long l){
		timeBetweenChangesMs = l;
	}
	void setPatternString(String s){
		patternString = s;
	}
	
	void newPattern() {
		// random number generator
		Random r = new Random();
		for (int i = 0; i < sequenceLength; i++) {
			int x = r.nextInt(numButtons);
			pattern.add(Integer.valueOf(x));
		}
	}
	void reusePattern(){
		copyPatternStringIntoPatternArrayList();
		editor.putString("patternString", null);
		editor.commit();
	}	
	
	void copyPatternArrayListIntoPatternString(){
		patternString = "";
		for (Integer i: pattern){
			patternString = patternString + i.toString();
		}
	}	
	void copyPatternStringIntoPatternArrayList(){
		pattern.clear();
		for (char c: patternString.toCharArray()){
			int n = c - 48; 
			pattern.add(n);
		}	
	}		

	void increaseScoreBy(int i){
		score += i;
	}
	void incrementPatternPosition(){
		patternPosition++;
	}
	void decrementLives(){
		lives--;
	}
	int getNumberAtCurrentPosition(){
		return pattern.get(patternPosition);
	}
	
}
