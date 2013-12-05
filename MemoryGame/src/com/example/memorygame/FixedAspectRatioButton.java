package com.example.memorygame;

import android.content.Context;
import android.widget.Button;

//Source: aleb on GitHub src/com/triposo/barone/FixedAspectRatioFrameLayout.java
class FixedAspectRatioButton extends Button{
	
	private float aspectRatio;
	
	FixedAspectRatioButton(Context context){
		super(context);
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
