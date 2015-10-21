package com.android.toorcomp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.osmdroid.util.GeoPoint;
import android.annotation.SuppressLint;
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
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.toorcomp.mail.GMailSender;
import com.android.toorcomp.mail.MailObject;

//import com.android.toorcomp.Map.InnerLocationListener;

public class New_request extends Base_Activity {

	private static int PICK_IMAGE = 0;
	private LocationManager InnerLocationManager;
	private LocationListener InnerLocationListener;

	// ---- mail params

	public boolean delete_Mail = false;
	public String mail_param_Date;
	public String mail_param_ShortDesc;
	public String mail_param_Category;
	public String mail_param_Description;
	public String mail_param_TelNumber;
	public String mail_param_ImageFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().show();
		setContentView(R.layout.new_request);

		@SuppressWarnings("unused")
		final GPSTracker mGPS = new GPSTracker(this);
		final TextView descText = (TextView) findViewById(R.id.editText2);
		final TextView shortDes = (TextView) findViewById(R.id.editText1);
		final TextView photoTextView = (TextView) findViewById(R.id.photo);
		final Spinner forSelector = (Spinner) findViewById(R.id.spinner1);

		// ------------- Clear description text -------------------

		shortDes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				shortDes.setText("");
			}
		});

		descText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				descText.setText("");
			}
		});

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
												descText.append("\n Θέση : http://www.google.com/maps/place/"
														+ mGPS.getLatitude()
														+ ","
														+ mGPS.getLongitude()
														+ "/@"
														+ mGPS.getLatitude()
														+ ","
														+ mGPS.getLongitude()
														+ "17z");

												/*
												 * descText.append("Lat=" +
												 * mGPS.getLatitude() + "Lon=" +
												 * mGPS.getLongitude());
												 */

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
												InnerLocationManager
														.requestLocationUpdates(
																LocationManager.GPS_PROVIDER,
																0, 0,
																InnerLocationListener);

												/*
												 * descText.append("Lat=" +
												 * mGPS.getLatitude() + "Lon=" +
												 * mGPS.getLongitude());
												 */

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
				TextView shortDes = (TextView) findViewById(R.id.editText1);
				shortDes.setText("");
				TextView descText = (TextView) findViewById(R.id.editText2);
				descText.setText("");

			}
		});

		// --------------- send request -----------------------------------

		Button button4 = (Button) findViewById(R.id.send);
		button4.setOnClickListener(new android.view.View.OnClickListener() {
			@SuppressLint("SimpleDateFormat")
			public void onClick(View v) {

				// --------- set email params

				Context mAppContext = New_request.this.getApplicationContext();
				TelephonyManager tMgr = (TelephonyManager) mAppContext
						.getSystemService(Context.TELEPHONY_SERVICE);
				String mPhoneNumber = tMgr.getLine1Number();

				Calendar c = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
				String fDate = df.format(c.getTime());

				mail_param_Date = fDate;
				mail_param_ShortDesc = shortDes.getText().toString();
				mail_param_Category = forSelector.getSelectedItem().toString();
				mail_param_Description = descText.getText().toString();
				mail_param_TelNumber = mPhoneNumber;
				mail_param_ImageFile = photoTextView.getText().toString();

				if (isNetworkAvailable()) {

					// ----- start async task to send email

					mailSendingTask what = new mailSendingTask(null);
					what.execute();

				} else

				{
					// ------ if no internet connection
					// ------ store request for future

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							New_request.this);

					// set title
					alertDialogBuilder
							.setTitle("Δέν υπάρχει σύνδεση στο Διαδίκτυο.. ");

					// set dialog message
					alertDialogBuilder
							.setMessage(
									"Θέλετε να γίνει αποθήκευση της αίτησης για μελοντική αποστολή")
							.setCancelable(false)
							.setPositiveButton("Ναί",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											Context mAppContext = New_request.this
													.getApplicationContext();
											MailObject sMail = new MailObject(
													mail_param_ShortDesc,
													mail_param_Category,
													mail_param_Description,
													mail_param_ImageFile,
													mail_param_TelNumber);
											if (sMail.Write_To_Sd()) {

												Toast.makeText(mAppContext,
														"Έγινε αποθήκευση",
														Toast.LENGTH_LONG)
														.show();

											} else {

												Toast.makeText(mAppContext,
														"Σφάλμα αποθήκευσης",
														Toast.LENGTH_LONG)
														.show();
											}

											// TODO Implement

										}
									})
							.setNegativeButton("Οχι",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {

											finish();

										}
									}

							);

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();

				}
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
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						PICK_IMAGE);
			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// ----------- pick image -------------------

		if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {
			Uri _uri = data.getData();

			// User had pick an image.
			Cursor cursor = getContentResolver()
					.query(_uri,
							new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
							null, null, null);
			cursor.moveToFirst();

			// Link to the image
			final String imageFilePath = cursor.getString(0);
			cursor.close();
			final TextView photoTextView = (TextView) findViewById(R.id.photo);
			photoTextView.setText(imageFilePath);
		}
		super.onActivityResult(requestCode, resultCode, data);

		// ----------- pick image end ---------------

		if (requestCode == 1) {

			if (resultCode == RESULT_OK) {
				@SuppressWarnings("unused")
				String result = data.getStringExtra("result");
			}
			if (resultCode == RESULT_CANCELED) {

				Globals.getInstance().getMap_Center();
				final TextView descText = (TextView) findViewById(R.id.editText2);

				descText.append("\n Θέση : http://maps.google.com/maps?f=q&q="
						+ Globals.getInstance().getMap_Center().getLatitudeE6()
						/ 1E6
						+ ","
						+ Globals.getInstance().getMap_Center()
								.getLongitudeE6() / 1E6 + "&z=16");

			}
		}

	}

	private class InnerLocationListener implements LocationListener {

		public void onLocationChanged(Location argLocation) {
			// TODO Auto-generated method stub
			// super.onLocationChanged(location);
			@SuppressWarnings("unused")
			GeoPoint myGeoPoint = new GeoPoint(
					(int) (argLocation.getLatitude() * 1000000),
					(int) (argLocation.getLongitude() * 1000000));

			final TextView descText = (TextView) findViewById(R.id.editText2);
			descText.append("    Lat= " + argLocation.getLatitude() / 1E6
					+ " Lon= " + argLocation.getLongitude());

			// m_mapView.getController().setCenter(myGeoPoint);
			// m_mapView.invalidate();
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

	// ----- if file exists in mail folder
	// ----- show send mail menu item

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		File root = new File(Environment.getExternalStorageDirectory(),
				"osmdroid/mail/Mail.json");

		if (root.exists()) {
			MenuItem mail_send = menu.findItem(R.id.mail_send);
			mail_send.setVisible(true);

		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.opt:
			startActivity(new Intent(getApplicationContext(), Options.class));
			return true;

		case R.id.mail_send:
			
			MailObject sMail = new MailObject();

			// TODO Check for wrong or corrupted data in file
			
			mail_param_Date = sMail.Date;
			mail_param_ShortDesc = sMail.Short_Desc;
			mail_param_Category = sMail.Category;
			mail_param_Description = sMail.Description;
			mail_param_TelNumber = sMail.TelNumber;
			mail_param_ImageFile = sMail.Image_File;

			// --- set the flag that we send stored message

			delete_Mail = true;

			mailSendingTask what = new mailSendingTask(null);
			what.execute();

			return true;

		}
		return false;

	}

	ProgressDialog createSpinningDialog(String title) {
		ProgressDialog pd = new ProgressDialog(this);
		pd.setTitle(title);
		pd.setMessage(getString(R.string.wait));
		pd.setCancelable(false);
		pd.setIndeterminate(true);
		return pd;
	}

	class mailSendingTask extends AsyncTask<Object, Void, Boolean> {
		boolean mOnCreate;
		ProgressDialog mPD;
		String mMessage;

		mailSendingTask(String message) {
			super();
			mMessage = "Sending";
		}

		@Override
		protected void onPreExecute() {
			mPD = createSpinningDialog(mMessage);
			mPD.show();
		}

		@Override
		protected Boolean doInBackground(Object... params) {

			if (isNetworkAvailable()) {

				File imageFile = new File("");
				try {

					GMailSender sender = new GMailSender("aaa@gmail.com",
							"aaaaa");
					try {
						imageFile = new File(mail_param_ImageFile);
					} catch (Exception e) {
						imageFile = new File("");
						Log.e("imageFile", e.getMessage(), e);
					}
					sender.sendMail(mail_param_ShortDesc, " Date = "
							+ mail_param_Date + " For = " + mail_param_Category
							+ " Desc = " + mail_param_Description + " Phone= "
							+ mail_param_TelNumber, imageFile,
							"aaa@gmail.com", "aaa@gmail.com");

				} catch (Exception e) {
					Context mAppContext = New_request.this
							.getApplicationContext();
					Toast.makeText(mAppContext, "Σφάλμα αποστολής",
							Toast.LENGTH_LONG).show();

					Log.e("SendMail", e.getMessage(), e);
				}
			} else {
				Context mAppContext = New_request.this.getApplicationContext();
				Toast.makeText(mAppContext, "Σφάλμα δικτύου", Toast.LENGTH_LONG)
						.show();

			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean ok) {
			if (delete_Mail) {

				File root = new File(Environment.getExternalStorageDirectory(),
						"osmdroid/mail/Mail.json");

				if (root.exists())
					root.delete();
				delete_Mail = false;
			}

			mPD.dismiss();

			Context mAppContext = New_request.this.getApplicationContext();
			Toast.makeText(mAppContext, "Έγινε αποστολή", Toast.LENGTH_LONG)
					.show();

		}

		// -------------------------------------------

	}
}
