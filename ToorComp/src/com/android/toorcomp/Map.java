package com.android.toorcomp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.osmdroid.bonuspack.overlays.ExtendedOverlayItem;
import org.osmdroid.bonuspack.overlays.ItemizedOverlayWithBubble;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Map extends Base_Activity implements SensorEventListener {

	Double latToPass;
	Double longToPass;

	// The MapView variable:
	private MapView m_mapView;

	// Default map zoom level:
	private int MAP_DEFAULT_ZOOM = 16;

	// Default map Latitude:
	private double MAP_DEFAULT_LATITUDE = 39.524125;

	// Default map Longitude:
	private double MAP_DEFAULT_LONGITUDE = 20.881799;

	protected ItemizedOverlayWithBubble<ExtendedOverlayItem> markerOverlays;
	protected GeoPoint startPoint, destinationPoint;

	private LocationManager InnerLocationManager;
	private LocationListener InnerLocationListener;

	// Compass and Rotation

	private MyLocationNewOverlay myPositionOverlay;

	ArrayList<OverlayItem> overlayItemArray = new ArrayList<OverlayItem>();
	private float compassBearing;

	// private MyMyLocationNewOverlay mLocationOverlay;
	private CompassOverlay mCompassOverlay;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Sensor mMagnetometer;
	private float[] mLastAccelerometer = new float[3];
	private float[] mLastMagnetometer = new float[3];
	private boolean mLastAccelerometerSet = false;
	private boolean mLastMagnetometerSet = false;
	private float[] mR = new float[9];
	private float[] mOrientation = new float[3];
	private String Pois_To_Display;
	//
	private List<Poi_Struct> pois = new ArrayList<Poi_Struct>();
	private XMLParser parser = new XMLParser();
	private Activity _activity;
	static final float ALPHA = 0.25f; // if ALPHA = 1 OR 0, no filter applies.

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().show();
		_activity = this;

		// ----- seeeeeensorrrr

		if (!Globals.getInstance().isFirstTimeOnMapActivity())
			MAP_DEFAULT_ZOOM = Globals.getInstance().getMapZoomLevel();

		// Specify the XML layout to use:
		setContentView(R.layout.map);

		// Find the MapView controller in that layout:
		m_mapView = (MapView) findViewById(R.id.mapview);
		// Setup the mapView controller:
		m_mapView.setBuiltInZoomControls(true);
		m_mapView.setMultiTouchControls(true);
		m_mapView.setClickable(false);
		
		
		
		if (useOfflineMap())
		{
	    	m_mapView.setUseDataConnection(false);
		} else m_mapView.setUseDataConnection(true);
		
		
		m_mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
			// https://github.com/johnjohndoe/OSMDroidOfflineDemo/blob/master/app/src/main/java/com/example/android/osmdroidofflinedemo/MainActivity.java
		m_mapView.getController().setZoom(MAP_DEFAULT_ZOOM);

		// -------------------------
		// MyOwnItemizedOverlay overlay = new MyOwnItemizedOverlay(this,
		// overlayItemArray);
		// ---------------------------

		// My location overlay
		{
			// Create a static Overlay showing a the current location and a
			// compass

			myPositionOverlay = new MyLocationNewOverlay(this,
					new GpsMyLocationProvider(this), m_mapView);
			// Enables user's location
			myPositionOverlay.enableMyLocation();
			// Enable following user
			myPositionOverlay.enableFollowLocation();
			// And we add the Overlay
			m_mapView.getOverlays().add(myPositionOverlay);

			this.myPositionOverlay.runOnFirstFix(new Runnable() {
				public void run() {
					// Animate to the current location on first GPS fix
					// --- https://github.com/osmdroid/osmdroid/issues/47
					// --- getMyLocation!=null
					if (myPositionOverlay.getMyLocation() != null)
						m_mapView.getController().animateTo(
								myPositionOverlay.getMyLocation());
				}
			});

			// We add the compass
			mCompassOverlay = new CompassOverlay(this,
					new InternalCompassOrientationProvider(this), m_mapView);
			m_mapView.getOverlays().add(mCompassOverlay);
			mCompassOverlay.enableCompass();

			// setContentView(m_mapView);

			mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
			mAccelerometer = mSensorManager
					.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			mMagnetometer = mSensorManager
					.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		}

		InnerLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		InnerLocationListener = new InnerLocationListener();
		InnerLocationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 0, 0, InnerLocationListener);

		final GPSTracker mGPS = new GPSTracker(Map.this);

		// If gps enabled

		if (mGPS.canGetLocation)
		// if (1==0)
		{

			// get Lat Long from gps

			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! uncomment to read gps
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!

			// MAP_DEFAULT_LATITUDE = mGPS.getLatitude();
			// MAP_DEFAULT_LONGITUDE = mGPS.getLongitude();

			// if GPS is ready
			if (MAP_DEFAULT_LATITUDE > 0) {
				// if (1==1) {
				// String LAT = Double.toString(MAP_DEFAULT_LATITUDE);
				// String LONGT = Double.toString(MAP_DEFAULT_LONGITUDE);
				if (Globals.getInstance().isFirstTimeOnMapActivity())
					Toast.makeText(
							getApplicationContext(),
							"Your Location is - \nLat: " + MAP_DEFAULT_LATITUDE
									+ "\nLong: " + MAP_DEFAULT_LONGITUDE,
							Toast.LENGTH_LONG).show();
				// GPS if not ready

			} else {
				Toast.makeText(getApplicationContext(), "GPS is not Ready",
						Toast.LENGTH_LONG).show();

				LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				LocationListener mlocListener = new MyLocationListener(Map.this);
				mlocManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

			}

		} else {

			mGPS.showSettingsAlert();

		}

		if (Globals.getInstance().isFirstTimeOnMapActivity()) {
			m_mapView.getController().setCenter(
					new GeoPoint(MAP_DEFAULT_LATITUDE, MAP_DEFAULT_LONGITUDE));
			Globals.getInstance().setFirstTimeOnMapActivity(false);
		} else {
			m_mapView.getController().setCenter(
					Globals.getInstance().getMap_Center());
			m_mapView.getController().setZoom(Globals.getInstance().getMapZoomLevel());
		}

		
		// start overlays ----------------------------

		// -------------------------------------------------------------
		// Parce xml for pois
		// -------------------------------------------------------------

		Globals g = Globals.getInstance();

		if (!g.getXmLERROR()) {

			/************** Read XML *************/

			String XMLData;
			try {
				XMLData = getXmlFromFile("/osmdroid/Options.xml");

				BufferedReader br = new BufferedReader(
						new StringReader(XMLData));
				InputSource is = new InputSource(br);

				/************ Parse XML **************/

				// XMLParser parser = new XMLParser();
				SAXParserFactory factory = SAXParserFactory.newInstance();

				SAXParser sp;
				sp = factory.newSAXParser();

				XMLReader reader = sp.getXMLReader();
				reader.setContentHandler(parser);
				reader.parse(is);

				// -----------------------------------------------------------------

				// List<Poi_Struct> pois = new ArrayList<Poi_Struct>();

				pois = parser.getPois();
				int marker = R.drawable.markerbig;
				OverlayItem olItem;

				// ------ SELECT POIS TO DISPLAY FROM OPTIONS
				Pois_To_Display = g.getPois_To_Display();
				String Pois_Used = convertNumbersToPois(Pois_To_Display);

				for (int i = 0; i < pois.size(); i++) {

					// ---- SELECT POIS TO DISPLAY FROM OPTIONS

					if (Pois_Used.contains(pois.get(i).getType().toString())) {
						olItem = new OverlayItem(pois.get(i).getName(),
								pois.get(i).getDesc() + "#"
										+ pois.get(i).getLink(), new GeoPoint(
										pois.get(i).getLon(), pois.get(i)
												.getLat()));

						Drawable newMarker = this.getResources().getDrawable(
								marker);

						olItem.setMarker(newMarker);
						overlayItemArray.add(olItem);
					}
				}

				// ---------- Here we put xml overlays to map -----------------
				MyOwnItemizedOverlay overlay = new MyOwnItemizedOverlay(this,
						overlayItemArray);
				m_mapView.getOverlays().add(overlay);
				// ------------
				// ---- it gives error
				// setContentView(m_mapView); //displaying the MapView

			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				Toast.makeText(
						getApplicationContext(),
						"Parcer Configuration Error=" + e.getLocalizedMessage(),
						Toast.LENGTH_LONG).show();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(),
						"SAX Exception=" + e.getLocalizedMessage(),
						Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(),
						"IO Exception=" + e.getLocalizedMessage(),
						Toast.LENGTH_LONG).show();
			}

		} else
			Toast.makeText(getApplicationContext(),
					"g.getxmlerror=" + g.getXmLERROR(), Toast.LENGTH_LONG)
					.show();

		// ------------------------------------------------------------


	} // end onCreate()

	public String getXmlFromFile(String filename) throws IOException {
		StringBuffer buff = new StringBuffer();
		File root = Environment.getExternalStorageDirectory();
		File xml = new File(root, filename);

		BufferedReader reader = new BufferedReader(new FileReader(xml));
		String line = null;
		while ((line = reader.readLine()) != null) {
			buff.append(line).append("\n");
		}
		reader.close();
		return buff.toString();
	}

	// local location listener

	private class InnerLocationListener implements LocationListener {

		public void onLocationChanged(Location argLocation) {
			// TODO Auto-generated method stub
			// super.onLocationChanged(argLocation);
			GeoPoint myGeoPoint = new GeoPoint(
					(int) (argLocation.getLatitude() * 1000000),
					(int) (argLocation.getLongitude() * 1000000));

			m_mapView.setMapOrientation(-compassBearing);
			m_mapView.getController().setCenter(myGeoPoint);
			m_mapView.invalidate();
		
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

		
	
	
	@Override
	public void onResume() {

		super.onResume();

		myPositionOverlay.enableMyLocation();
		myPositionOverlay.enableFollowLocation();
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_FASTEST);
		mSensorManager.registerListener(this, mMagnetometer,
				SensorManager.SENSOR_DELAY_FASTEST);

		Globals g = Globals.getInstance();
		if (g.isOptions_Changed()) {
			g.setMapZoomLevel(m_mapView.getZoomLevel());
			
			g.setMap_Center(m_mapView.getMapCenter());
			g.setMapZoomLevel(m_mapView.getZoomLevel());
			g.setOptions_Changed(false);
		//	g.setFirstTimeOnMapActivity(true);
			this._activity.finish();
			overridePendingTransition(0, 0);
			Intent intent = new Intent(getApplicationContext(), Map.class);
			startActivity(intent);
		}

		
		
		
		
	}

	/** disable compass and location updates */
	@Override
	public void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this, mAccelerometer);
		mSensorManager.unregisterListener(this, mMagnetometer);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	
	    // http://en.wikipedia.org/wiki/Low-pass_filter
		// http://www.raweng.com/blog/2013/05/28/applying-low-pass-filter-to-android-sensors-readings/
		//---- We use lowpass filter to stabilize map
	
		protected float[] lowPassFilter( float[] input, float[] output ) {
		    if ( output == null ) return input;     
		    for ( int i=0; i<input.length; i++ ) {
		        output[i] = output[i] + ALPHA * (input[i] - output[i]);
		    }
		    return output;
		}
	
	
	
	@Override
	public void onSensorChanged(SensorEvent event) {

				
		if (event.sensor == mAccelerometer) {
			
			mLastAccelerometer = lowPassFilter(event.values.clone(), mLastAccelerometer);
			mLastAccelerometerSet = true;
		} else if (event.sensor == mMagnetometer) {
			
			mLastMagnetometer = lowPassFilter(event.values.clone(), mLastMagnetometer);
			mLastMagnetometerSet = true;
		}
		if (mLastAccelerometerSet && mLastMagnetometerSet) {
			float R[] = new float[9];
	        float I[] = new float[9];

	        boolean success = SensorManager.getRotationMatrix(mR, I, mLastAccelerometer, mLastMagnetometer);
	        if (success) {
	        	SensorManager.getOrientation(mR, mOrientation);
				float azimuthInRadians = mOrientation[0];
				float azimuthInDegress = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;
				m_mapView.setMapOrientation(-azimuthInDegress);
	        }
			
		
		}
		
	  
		
	}

	private String convertNumbersToPois(String numbers) {

		// ---- This gets numbers 123456 from globals
		// ---- loaded from shared prefs
		// ---- if it finds number makes a string with poi's_en name
		// ---- IT IS FOR SELECTING POIS TO SHOW

		String pois = "";
		try {
			for (int i = 0; i < numbers.length(); ++i) {
				char c = numbers.charAt(i);

				if (i == 0 & c == '1')
					pois = getResources().getString(R.string.poi1_en);
				if (i == 1 & c == '2')
					pois = pois.concat(getResources().getString(
							R.string.poi2_en));
				if (i == 2 & c == '3')
					pois = pois.concat(getResources().getString(
							R.string.poi3_en));
				if (i == 3 & c == '4')
					pois = pois.concat(getResources().getString(
							R.string.poi4_en));
				if (i == 4 & c == '5')
					pois = pois.concat(getResources().getString(
							R.string.poi5_en));
				if (i == 5 & c == '6')
					pois = pois.concat(getResources().getString(
							R.string.poi6_en));
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Error=" + e.toString(),
					Toast.LENGTH_LONG).show();
		}

		return pois;
	}

	private boolean useOfflineMap() {
		
		if (Globals.getInstance().isOfflineMap()) return true;
				
		if (! Globals.getInstance().isOfflineMap()){
			if (!isNetworkAvailable()){
				
		            	
		            		/*Intent intent = new Intent(Intent.ACTION_MAIN);
		     				intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
		     				startActivity(intent);*/
				Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
				startActivity(intent);
			
		    }
		}
		
		return false;
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public class NetworkChangeReceiver extends BroadcastReceiver {
		 
	    @Override
	    public void onReceive(final Context context, final Intent intent) {
	    	new Runnable() {
	            @Override
	            public void run() {
	            	 try {
	                     HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
	                     urlc.setRequestProperty("User-Agent", "Test");
	                     urlc.setRequestProperty("Connection", "close");
	                     urlc.setConnectTimeout(1500); 
	                     urlc.connect();
	                 } catch (IOException e) {
	                	 Toast.makeText(context, "Δέν υπάρχει σύνδεση στο διαδίκτυο", Toast.LENGTH_LONG).show();
	                 }
	            }
	        };
	    	
	    }
	}
	
	
} // end class YourMap
