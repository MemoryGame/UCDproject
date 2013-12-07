package com.ucdmscconversion.memorygame;

import com.example.memorygame.R;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

//Source: aleb on GitHub src/com/triposo/barone/FixedAspectRatioFrameLayout.java
class GameButton extends Button{	
	
	static final int[] buttonsOn = new int[] { R.drawable.blue_button_on,
			R.drawable.orange_button_on, R.drawable.yellow_button_on,
			R.drawable.purple_button_on, R.drawable.green_button_on,
			R.drawable.red_button_on, R.drawable.black_button_on,
			R.drawable.pink_button_on };	
	
	static final int[] buttonsOff = new int[] { R.drawable.blue_button_off,
			R.drawable.orange_button_off, R.drawable.yellow_button_off,
			R.drawable.purple_button_off, R.drawable.green_button_off,
			R.drawable.red_button_off, R.drawable.black_button_off,
			R.drawable.pink_button_off };		
	
	static final int[] buttonSound = new int[] { R.raw.button1a, R.raw.button2a, 
			R.raw.button3a, R.raw.button4a, R.raw.button5a, 
			R.raw.button6a, R.raw.buttona7a, R.raw.button8a };		
	
	static final LayoutParams bParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.50f);
	
	private float aspectRatio;
	private int id;
	
	GameButton(Context context, int n){
		super(context);		
		id = n;
		this.setBackgroundResource(buttonsOff[id]);
		this.setLayoutParams(bParams);
		this.setId(id);
	}	
	
	void turnOff(){
		this.setBackgroundResource(buttonsOff[id]);
	}
	
	void turnOn(){
		this.setBackgroundResource(buttonsOn[id]);
	}	
	
	@Override protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){
	
		aspectRatio = 1.0000f;
	
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int receivedWidth = MeasureSpec.getSize(widthMeasureSpec);
		int receivedHeight = MeasureSpec.getSize(heightMeasureSpec);
	
		int measuredWidth;
		int measuredHeight;
		boolean widthDynamic;
	
		if (heightMode == MeasureSpec.EXACTLY) {
			if (widthMode == MeasureSpec.EXACTLY) {
				widthDynamic = receivedWidth == 0;
			} else {
				widthDynamic = true;
			}
		} else if (widthMode == MeasureSpec.EXACTLY) {
			widthDynamic = false;
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			return;
		}
	
		if (widthDynamic) {
			// Width is dynamic.
			int w = (int) (receivedHeight * aspectRatio);
			measuredWidth = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
			measuredHeight = heightMeasureSpec;
		} else {
			// Height is dynamic.
			measuredWidth = widthMeasureSpec;
			int h = (int) (receivedWidth / aspectRatio);
			measuredHeight = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
		}	
		super.onMeasure(measuredWidth, measuredHeight);
	}

}
