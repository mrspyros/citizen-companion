package com.android.toorcomp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
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
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;
import android.widget.Toast;

public class Map extends Activity implements SensorEventListener {

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
	private MyLocationOverlay myCompass;
	private SensorManager sensorManager;
	private boolean filterSensor = false;
	ArrayList<OverlayItem> overlayItemArray = new ArrayList<OverlayItem>();
	private float compassBearing;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// seeeeeensorrrr

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
		m_mapView.setUseDataConnection(true);
		
		// https://github.com/johnjohndoe/OSMDroidOfflineDemo/blob/master/app/src/main/java/com/example/android/osmdroidofflinedemo/MainActivity.java
		
		m_mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
		m_mapView.getController().setZoom(MAP_DEFAULT_ZOOM);


		// My location overlay
		{
			// Create a static Overlay showing a the current location and a
			// compass

		//https://code.google.com/p/osmdroid/source/browse/branches/rotation/OpenStreetMapViewer/src/org/osmdroid/MapActivity.java?r=914
			
			
			
			
			this.myCompass = new MyLocationOverlay(this, m_mapView);
			this.m_mapView.getOverlays().add(myCompass);
			this.myCompass.runOnFirstFix(new Runnable() {
				public void run() {
					// Animate to the current location on first GPS fix
					m_mapView.getController().animateTo(
							myCompass.getMyLocation());
				}
			});
		}

		InnerLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		InnerLocationListener = new InnerLocationListener();
		InnerLocationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 0, 0, InnerLocationListener);

		final GPSTracker mGPS = new GPSTracker(Map.this);

		// If gps enabled

		if (mGPS.canGetLocation) 
		//if (1==0)
		{

			// get Lat Long from gps

			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! uncomment to read gps
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!

			//MAP_DEFAULT_LATITUDE = mGPS.getLatitude();
			//MAP_DEFAULT_LONGITUDE = mGPS.getLongitude();

			// if GPS is ready
			if (MAP_DEFAULT_LATITUDE > 0) {
			//if (1==1) {
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
		}

		m_mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
		// start overlays ----------------------------

		// -------------------------------------------------------------
		// Parce xml for pois
		// -------------------------------------------------------------

		Globals g = Globals.getInstance();

		if (g.getXmLERROR() == "NO") 
		{

			/************** Read XML *************/

			String XMLData;
			try {
				XMLData = getXmlFromFile("/osmdroid/Pois.xml");

				BufferedReader br = new BufferedReader(
						new StringReader(XMLData));
				InputSource is = new InputSource(br);

				/************ Parse XML **************/

				XMLParser parser = new XMLParser();
				SAXParserFactory factory = SAXParserFactory.newInstance();

				SAXParser sp;
				sp = factory.newSAXParser();

				XMLReader reader = sp.getXMLReader();
				reader.setContentHandler(parser);
				reader.parse(is);

				// -----------------------------------------------------------------

				List<Poi_Struct> pois = new ArrayList<Poi_Struct>();

				pois = parser.getPois();

				// ArrayList<OverlayItem> overlayItemArray = new
				// ArrayList<OverlayItem>();

				int marker = R.drawable.markerbig;
				OverlayItem olItem;
				for (int i = 0; i < pois.size(); i++) {
					olItem = new OverlayItem(pois.get(i).getName(), pois.get(i)
							.getDesc(), new GeoPoint(pois.get(i).getLon(), pois
							.get(i).getLat()));


					Drawable newMarker = this.getResources()
							.getDrawable(marker);

					olItem.setMarker(newMarker);
					overlayItemArray.add(olItem);

				}
				
				//---------- Here we put xml overlays to map -----------------

				// ------- Add Your Position -----------------------------------
				
				 OverlayItem olItem1 = new OverlayItem("Position", "You Are Here", new
				 GeoPoint( MAP_DEFAULT_LATITUDE, MAP_DEFAULT_LONGITUDE));
			     Drawable newMarker = this.getResources().getDrawable(
				 R.drawable.marker); olItem1.setMarker(newMarker);
				 overlayItemArray.add(olItem1);
			 
	      
				// ------------ This puts the overlays
				// ------------
				MyOwnItemizedOverlay overlay = new MyOwnItemizedOverlay(this,overlayItemArray);
                m_mapView.getOverlays().add(overlay);
                // ------------
                // ------------ Do not comment
				
				// ---- it gives error
				//setContentView(m_mapView); //displaying the MapView
				 
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

		}

	
		
		// ------------------------------------------------------------

		/*
		 * Toast.makeText(getApplicationContext(),
		 * "ZoomLevel="+MAP_DEFAULT_ZOOM, Toast.LENGTH_LONG).show();
		 */

	//	Globals.getInstance().setFirstTimeOnMapActivity(false);
	//	Globals.getInstance().setMap_Center(m_mapView.getMapCenter());
	//	Globals.getInstance().setMapZoomLevel(m_mapView.getZoomLevel());

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

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
			// super.onLocationChanged(location);
			GeoPoint myGeoPoint = new GeoPoint(
					(int) (argLocation.getLatitude() * 1000000),
					(int) (argLocation.getLongitude() * 1000000));

			m_mapView.setMapOrientation(-compassBearing);
			m_mapView.getController().setCenter(myGeoPoint);
			m_mapView.invalidate();
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

	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		super.onResume();

		myCompass.enableCompass();
		myCompass.enableMyLocation();
		myCompass.followLocation(true);
		myCompass.enableFollowLocation();
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_FASTEST);

	}

	/** disable compass and location updates */
	@Override
	public void onPause() {
		super.onPause();

		myCompass.disableMyLocation();
		myCompass.disableCompass();
		myCompass.disableFollowLocation();
		sensorManager.unregisterListener(this);

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float bearing = event.values[0];
		if (filterSensor) {
			bearing = (int) bearing;
		}
		compassBearing = bearing;
		// m_mapView.setMapOrientation(-bearing);

	}

} // end class YourMap
