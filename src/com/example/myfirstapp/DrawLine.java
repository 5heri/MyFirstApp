package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class DrawLine extends View {
	
	private final static String LOG_OUT = "LOG_OUT-APP";
	
	
	// TODO: check if these are negative as negative can not be printed.
	private float x_prev;
	private float y_prev;
	private float x_curr;
	private float y_curr;

	public DrawLine(Context context) {
		super(context);
		x_prev = 0;
		y_prev = 0;
		x_curr = 0;
		y_curr = 0;
	}

	
	public void setPrevCurX(float x) {
		x_prev = x_curr;
		x_curr = x;
	}
	
	public void setPrevCurY(float y) {
		y_prev = y_curr;
		y_curr = y;
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Log.d(LOG_OUT, "prevY: " + y_prev + "   prevX: " + x_prev);
		Log.d(LOG_OUT, "currY: " + y_curr + "   currX: " + x_curr);
		
		// Try using PATH?
		
		Paint red = new Paint();
		red.setColor(Color.RED);
		red.setStyle(Paint.Style.FILL);
		//Rect rectangle = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
		//canvas.drawRect(rectangle, red);
		
		 canvas.drawLine(x_prev, y_prev, x_curr, y_curr, red);
	}
}
