package com.example.myfirstapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;

public class DisplayMessageActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Display Message Activity");
		
		// Get the message from the intent
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		//Create the text view
		TextView textView = new TextView(this);
		textView.setTextSize(40);
		textView.setText(message);
		
		// Set the text view as the activity layout
		setContentView(textView);
		
		/*
		 * commented as it overrides the "setContentView(textView)".
		 */
		//setContentView(R.layout.activity_display_message);  

	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
