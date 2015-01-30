package com.android.toorcomp;

import org.osmdroid.util.GeoPoint;

//import com.android.toorcomp.Map.InnerLocationListener;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class New_request extends Activity {

	private static int PICK_IMAGE = 0;
	private LocationManager InnerLocationManager;
	private LocationListener InnerLocationListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_request);
		final GPSTracker mGPS = new GPSTracker(this);
		final TextView myTextView1 = (TextView) findViewById(R.id.editText2);
		final TextView myTextView = (TextView) findViewById(R.id.editText1);
        final TextView photoTextView=(TextView) findViewById(R.id.photo);

		
		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						New_request.this);

				// set title
				alertDialogBuilder
						.setTitle("Θέλετε να χρησιμοποιηθεί η τρέχουσα θέση σας");

				// set dialog message
				alertDialogBuilder
						.setMessage("Εισαγωγή τρέχουσας θέσης")
						.setCancelable(false)
						.setPositiveButton("Νάι",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// --------------------------GPS--------------------------------------

										final GPSTracker mGPS = new GPSTracker(
												New_request.this);

										// If gps enabled
										if (mGPS.canGetLocation) {

											// if GPS is ready
											if (mGPS.getLatitude() != 0) {
											myTextView1.append("http://www.google.com/maps/place/"
															+ mGPS.getLatitude()
															+ ","
															+ mGPS.getLongitude()
															+"/@"
															+mGPS.getLatitude()
															+","
															+mGPS.getLongitude()
															+"17z");			
													
											/*	myTextView1.append("Lat="
														+ mGPS.getLatitude()
														+ "Lon="
														+ mGPS.getLongitude());*/

											} else {

												LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
												LocationListener mlocListener = new MyLocationListener(
														New_request.this);
												mlocManager
														.requestLocationUpdates(
																LocationManager.GPS_PROVIDER,
																0, 0,
																mlocListener);


												InnerLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
												InnerLocationListener = new InnerLocationListener();
												InnerLocationManager.requestLocationUpdates(
														LocationManager.GPS_PROVIDER, 0, 0, InnerLocationListener);
												
												
												  /*myTextView1.append("Lat=" +
												  mGPS.getLatitude() + "Lon=" +
												  mGPS.getLongitude());*/
												 

											}

										} else
											mGPS.showSettingsAlert();
									}
								})

						.setNegativeButton("Χάρτης",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
										Intent intent = new Intent(
												getApplicationContext(),
												Selector_map.class);
										startActivityForResult(intent, 1);
										// startActivity(intent);
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}

		});

		Button button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				TextView myTextView = (TextView) findViewById(R.id.editText1);
				myTextView.setText("");
				TextView myTextView1 = (TextView) findViewById(R.id.editText2);
				myTextView1.setText("");

				// Intent intent =new Intent(getApplicationContext(),
				// New_request.class);
				// startActivity(intent);
			}
		});

		
		// ------------------ If tapped to load photo -----------------
		PICK_IMAGE = 2;
		photoTextView.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Intent intent = new Intent();
		    	intent.setType("image/*");
		    	intent.setAction(Intent.ACTION_GET_CONTENT);
		    	startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
		    }
		});
		
		
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// ----------- pick image -------------------
		
		 if(requestCode == PICK_IMAGE && data != null && data.getData() != null) {
		        Uri _uri = data.getData();

		        //User had pick an image.
		        Cursor cursor = getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
		        cursor.moveToFirst();

		        //Link to the image
		        final String imageFilePath = cursor.getString(0);
		        cursor.close();
		        final TextView photoTextView=(TextView) findViewById(R.id.photo);
		        photoTextView.setText(imageFilePath);
		    }
		    super.onActivityResult(requestCode, resultCode, data);
		
		// ----------- pick image end ---------------
		
		
		
		
		
		if (requestCode == 1) {

			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("result");
			}
			if (resultCode == RESULT_CANCELED) {

				Globals.getInstance().getMap_Center();
				final TextView myTextView1 = (TextView) findViewById(R.id.editText2);
				myTextView1.append("Lat="
						+ Globals.getInstance().getMap_Center().getLatitudeE6()
						+ "Lon="
						+ Globals.getInstance().getMap_Center()
								.getLongitudeE6());
				// Write your code if there's no result
			}
		}

	}

	private class InnerLocationListener implements LocationListener {

		public void onLocationChanged(Location argLocation) {
			// TODO Auto-generated method stub
			// super.onLocationChanged(location);
			GeoPoint myGeoPoint = new GeoPoint(
					(int) (argLocation.getLatitude() * 1000000),
					(int) (argLocation.getLongitude() * 1000000));
			
			final TextView myTextView1 = (TextView) findViewById(R.id.editText2);
			 myTextView1.append("    Lat= " +
					 argLocation.getLatitude() + " Lon= " +
					 argLocation.getLongitude());
			
			//m_mapView.getController().setCenter(myGeoPoint);
			//m_mapView.invalidate();
			// ((IMapController) m_mapView).animateTo(myGeoPoint);
			// CenterLocatio(myGeoPoint);
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}

	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	};

}
