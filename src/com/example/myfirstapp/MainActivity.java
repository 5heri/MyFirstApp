package com.example.myfirstapp;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import android.app.Dialog;
import android.app.Activity;
import android.content.Context;
//import android.app.DialogFragment;
import android.content.IntentSender;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment; //TODO: check difference in android.app and android.support...
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements 
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener,
	LocationListener {
	
	private final static String LOG_OUT = "LOG_OUT-APP";

	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	public static final String KEY_UPDATES_REQUESTED =
            "com.example.android.location.KEY_UPDATES_REQUESTED";
	
	//handle errors for location
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	
	
	private static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
	
	// Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;
    
    // Define an object that holds accuracy and frequency parameters
    private LocationRequest mLocationRequest;
    
    private boolean mUpdatesRequested;
    
    // Handle to SharedPreferences for this app
    SharedPreferences mPrefs;
    
    // Handle to a SharedPreferences editor
    SharedPreferences.Editor mEditor;
    
    
    // MOCK VALUES
   /* private static final String PROVIDER = "flp";
    private static final double LAT = 37.377166;
    private static final double LNG = -122.086966;
    private static final float ACCURACY = 3.0f;*/
    
    
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toast.makeText(this, "Activity CREATED", Toast.LENGTH_SHORT).show();
		
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		
		mPrefs = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
		
		// Get an editor
        mEditor = mPrefs.edit();
        
		mLocationClient = new LocationClient(this, this, this);
		
		mUpdatesRequested = false; // starts turned off, until user turns on
	}
	
	
	
	//TODO: DISPLAY LNG_LAT_STR IN UI (rather than Toast)
	//TODO: UPDATE LNG_LAT_STR after some time
	//TODO: FIX GET_LOCATION BUTTON
	// This method is run when "getLocation" button is pressed
	public void getLocation(View view) {
		// If Google Play Services is available
        if (servicesConnected()) {

            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();
            double latitude = currentLocation.getLatitude();
            double longitude = currentLocation.getLongitude();
            String lng_lat_str = "LAT: " + latitude + "           LNG: " + longitude; 
            Toast.makeText(this, lng_lat_str, Toast.LENGTH_LONG).show();
    		
        }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		//Toast.makeText(this, "Activity STARTED", Toast.LENGTH_SHORT).show();
		
		// Connecting client
		mLocationClient.connect();
		
		/*if (mLocationClient.isConnected()) {			
			mLocationClient.setMockMode(true);
			Location testLocation = createLocation(LAT, LNG, ACCURACY);
    		mLocationClient.setMockLocation(testLocation);
		}*/

	}
	
	@Override
	public void onResume() {
		super.onResume();
		//Toast.makeText(this, "Activity RESUMED", Toast.LENGTH_SHORT).show();
		if (mPrefs.contains(KEY_UPDATES_REQUESTED)) {
			//Log.d("CONTAINS KEY_UPDATES", "TRUE");
			mUpdatesRequested = true; 
					//mPrefs.getBoolean(KEY_UPDATES_REQUESTED, false);   // FIX THIS!
			/*if (mUpdatesRequested) {
				Log.d("onResume: ", "TRUE");
			} else {
				Log.d("onResume: ", "FALSE");
			}*/
			
		} else {
			//Log.d("CONTAINS KEY_UPDATES", "FALSE");
			mEditor.putBoolean(KEY_UPDATES_REQUESTED, false);
			mEditor.commit();
		}
		
	}
	
	@Override
	public void onPause() {
		//Toast.makeText(this, "Activity PAUSED", Toast.LENGTH_SHORT).show();
		mEditor.putBoolean(KEY_UPDATES_REQUESTED, mUpdatesRequested);
		mEditor.commit();
		super.onPause();
	}
	
	@Override
	public void onStop() {
		//Toast.makeText(this, "Activity STOPPED", Toast.LENGTH_SHORT).show();

		if (mLocationClient.isConnected()) {
			mLocationClient.removeLocationUpdates(this);
		}
		
		// Disconnecting the client
		mLocationClient.disconnect();
		
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(this, "Activity DESTROYED", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_search:
	            openSearch();
	            return true;
	        case R.id.action_settings:
	            openSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
	
	private void openSearch() {
	    Toast.makeText(this, "Search button pressed", Toast.LENGTH_SHORT).show();
	}
	
	private void openSettings() {
	    Toast.makeText(this, "Settings button pressed", Toast.LENGTH_SHORT).show();
	}
	
	private Location createLocation(double lat, double lng, float acc) {
		Location newLocation = new Location("flp");
		newLocation.setLatitude(lat);
		newLocation.setLongitude(lng);
		newLocation.setAccuracy(acc);
		return newLocation;
	}
	
	/*********************************/
	/* REGARDING LOCATION MANAGEMENT */
	/*********************************/
	 
	
	// Handle error dialog 
	public static class ErrorDialogFragment extends DialogFragment {
		
		private Dialog mDialog;
		
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}
		
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
		
	}
	
	/*
	 * handles results from google play services 
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {

        case CONNECTION_FAILURE_RESOLUTION_REQUEST :
        /*
         * If the result code is Activity.RESULT_OK, try
         * to connect again
         */
            switch (resultCode) {
                case Activity.RESULT_OK :
                /*
                 * Try the request again
                 */

                break;
            }

		}
	}
	
	
	// Checks that google play services are available
	private boolean servicesConnected() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		
		if (ConnectionResult.SUCCESS == resultCode) {
			// Google play services is available
			Log.d("Location Updates", "Google Play services is available");
			return true;
		} else {
			// Google play services is not available
			
			// Get the error code
			// Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
            		resultCode, 
            		this, 
            		CONNECTION_FAILURE_RESOLUTION_REQUEST);
            
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }
            return false;
		}
		
	}
	
	@Override
	public void onConnected(Bundle arg0) {
		 Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		 
		/* if (mUpdatesRequested) {
				Log.d("onConnected before: ", "TRUE");
			} else {
				Log.d("onConnted before: ", "FALSE");
			}*/
		 
		 if (mUpdatesRequested) {
			 mLocationClient.requestLocationUpdates(mLocationRequest, this);
		 }
		/* if (mUpdatesRequested) {
				Log.d("onResume after: ", "TRUE");
			} else {
				Log.d("onResume after: ", "FALSE");
			}*/
		
	}
	
	@Override
	public void onDisconnected() {
		 Toast.makeText(this, "Disonnected. Please re-connect.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		
		if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
        	
        	showDialog(connectionResult.getErrorCode());
            //showErrorDialog(connectionResult.getErrorCode());
        }
		
	}



	@Override
	public void onLocationChanged(Location location) {
		// Report to the UI that the location was updated
        String msg = "Updated " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Log.d(LOG_OUT, "Location-Change:     " + msg);
        
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		
	}

}
