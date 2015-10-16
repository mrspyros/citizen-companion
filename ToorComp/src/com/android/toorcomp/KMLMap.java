package com.android.toorcomp;

/**
 * @author MrSpyros
 *
 *  This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   any later version.

 *  This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.clustering.StaticCluster;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlFolder;
import org.osmdroid.bonuspack.kml.KmlLineString;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.KmlPoint;
import org.osmdroid.bonuspack.kml.KmlPolygon;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.overlays.FolderOverlay;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Marker.OnMarkerDragListener;
import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.ArchiveFileFactory;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.NetworkLocationIgnorer;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.DirectedLocationOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.TilesOverlay;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * Our Main map
 * 
 * Based on "https://github.com/MKergall/osmbonuspack/tree/master/OSMBonusPackTuto/"
 *  
 *  
 */


public class KMLMap extends Base_Activity implements 
		LocationListener, SensorEventListener {

	MapView map;

	private String mPois_To_Display;
	protected LocationManager mLocationManager;
	protected DirectedLocationOverlay mLocationOverlay;
	float mAzimuthAngleSpeed = 0.0f;
	protected boolean mTrackingMode = true; // FolowLocation
	protected boolean mGps = false;
	//protected FolderOverlay mKmlOverlay;
	protected FolderOverlay mRoadMarkers;
	//protected Polyline mRoadOverlay;
	protected Polyline mRoadOverlay ;
	protected FolderOverlay mKmlOverlay; 
	protected XMLParser mParser;
	protected MyOwnItemizedOverlay mPOIoverlay;
	protected OverlayItem mPoilItem;
	protected List<Poi_Struct> mPoisList;
	protected ArrayList<OverlayItem> mOverlayItemArray; 
	protected XMLReader mReader; 
	private static final int MAXZOOMLEVEL=16;
	private static final int MINZOOMLEVEL=11;
	
	

	public static KmlDocument mKmlDocument; // made static to pass between
											// activities
	public static Stack<KmlFeature> mKmlStack; // passed between activities, top
												// is the current KmlFeature to
												// edit.
	public static KmlFolder mKmlClipboard;

	// Default map Latitude:
	private static final double MAP_DEFAULT_LATITUDE = 39.524125;
	// Default map Longitude:
	private static final double MAP_DEFAULT_LONGITUDE = 20.881799;

	
	private TilesOverlay tilesOverlay;
	private TextView speedTextView; 
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		// Introduction
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);		
		
		
		//setContentView(R.layout.map);
		//map = (MapView) findViewById(R.id.mapview);
				
		
		// -----------------------------------------------------
		// ---- Test Dynamicaly drawing 
		
		map = new MapView(this, 256);
		speedTextView = new TextView(this);
		speedTextView.setBackgroundColor(Color.parseColor("#AAD3D3D3"));
		speedTextView.setText("Speed:");
				
		
		final RelativeLayout relativeLayout = new RelativeLayout(this);
		@SuppressWarnings("deprecation")
		final RelativeLayout.LayoutParams mapViewLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);

//		final RelativeLayout.LayoutParams crossLayoutParams = new RelativeLayout.LayoutParams(
//				RelativeLayout.LayoutParams.FILL_PARENT,
//				RelativeLayout.LayoutParams.WRAP_CONTENT);
//		crossLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//		crossLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

		@SuppressWarnings("deprecation")
		final RelativeLayout.LayoutParams textViewLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

//		final RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(
//				RelativeLayout.LayoutParams.FILL_PARENT,
//				RelativeLayout.LayoutParams.WRAP_CONTENT);
//		buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

		relativeLayout.addView(map, mapViewLayoutParams);
		//relativeLayout.addView(cross, crossLayoutParams);
		relativeLayout.addView(speedTextView, textViewLayoutParams);
		//relativeLayout.addView(selectBtn, buttonLayoutParams);

		setContentView(relativeLayout);
		
		// ----- End Dynamic drawing ---------------------------------------

		map.setBuiltInZoomControls(true);
		map.setMultiTouchControls(true);
		map.setUseDataConnection(true);
		map.setTileSource(TileSourceFactory.MAPNIK);
		
		
		
		// In case there is no internet
		// But there is Offline map
		// We create a new tileprovider
		
		if (!isNetworkAvailable()&& CheckFileExists(Environment.getExternalStorageDirectory()+"/osmdroid/DodoniMap.zip")){
			map.setUseDataConnection(false);
		
			String packageDir = "/osmdroid/DodoniMap.zip";
	        String p = Environment.getExternalStorageDirectory() + packageDir;
			
			IArchiveFile[] archives = new IArchiveFile[1];
		    archives[0] = ArchiveFileFactory.getArchiveFile(new File (p));

		    // Simple implementation that extends BitmapTileSourceBase and nothing else
		    CustomTileSource customTiles = new CustomTileSource("MapQuest", null, MINZOOMLEVEL, MAXZOOMLEVEL, 256, ".jpg");  

		    MapTileModuleProviderBase[] providers = new MapTileModuleProviderBase[1];
		    providers[0] = new MapTileFileArchiveProvider(new SimpleRegisterReceiver(this.getApplicationContext()), customTiles, archives);    // this one is for local tiles (zip etc.)
		    //providers[1] =  new MapTileDownloader(TileSourceFactory.MAPNIK);    // MAPNIK web tile source
		   
		    MapTileProviderArray tileProvider = new MapTileProviderArray(customTiles, 
		            new SimpleRegisterReceiver(this.getApplicationContext()), providers);
		    		    	    
		    tilesOverlay = new TilesOverlay(tileProvider, this.getApplicationContext());
		    tilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);  // this makes sure that the invisble tiles of local tiles

	        map.getOverlays().add(tilesOverlay);
	        
	    
		    double north = 39.653813;
		    double east  =  21.042938;
		    double south = 39.291797;
		    double west  =  20.674896;
		    BoundingBoxE6 bBox = new BoundingBoxE6(north, east, south, west);
		    
		    // Limit scrolable area and zoom levels to
		    // Ones that exist in local map database
		    
		    map.setScrollableAreaLimit(bBox);
		    map.setMaxZoomLevel(MAXZOOMLEVEL);
		    map.setMinZoomLevel(MINZOOMLEVEL);
		    
		}
		
		// inform if no Internet and no cashed map exists
		
		if (!isNetworkAvailable()&& !CheckFileExists(Environment.getExternalStorageDirectory()+"/osmdroid/DodoniMap.zip")){
		
			Toast.makeText(this,
					 "No Internet Connection and no map database available",
					 Toast.LENGTH_LONG).show();
			
		}
		
		
		// ------------- check if GPS is enabled
		// ------------- only one time

		final GPSTracker mGPS = new GPSTracker(this);
		if (!mGPS.canGetLocation && !mGps) {
			mGPS.showSettingsAlert();
			mGps = true;
		}

		GeoPoint startPoint = new GeoPoint(MAP_DEFAULT_LATITUDE,
				MAP_DEFAULT_LONGITUDE);

		IMapController mapController = map.getController();
		mapController.setZoom(MINZOOMLEVEL);
		mapController.setCenter(startPoint);

		// 0. Using the Marker overlay
		Marker startMarker = new Marker(map);
		startMarker.setPosition(startPoint);
		startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
		startMarker.setTitle("Map Center");
		// startMarker.setIcon(getResources().getDrawable(R.drawable.marker_kml_point).mutate());
		// startMarker.setImage(getResources().getDrawable(R.drawable.ic_launcher));
		// startMarker.setInfoWindow(new
		// MarkerInfoWindow(R.layout.bonuspack_bubble_black, map));
		startMarker.setDraggable(true);
		startMarker.setOnMarkerDragListener(new OnMarkerDragListenerDrawer());
		map.getOverlays().add(startMarker);

		// 1. "Hello, Routing World"
		// RoadManager roadManager = new OSRMRoadManager();
		// 2. Playing with the RoadManager
		// roadManager roadManager = new MapQuestRoadManager("YOUR_API_KEY");
		// roadManager.addRequestOption("routeType=bicycle");
		ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
		waypoints.add(startPoint);
		// GeoPoint endPoint = new GeoPoint(MAP_DEFAULT_LATITUDE,
		// MAP_DEFAULT_LONGITUDE);
		// waypoints.add(endPoint);
		
		/**
		 * TODO
		 * 
		 * This is for use for road overlay
		 * 
		 * eg Show route
		 * 
		 
		 * Road road = roadManager.getRoad(waypoints);
		 *
		 *
		 * if (road.mStatus != Road.STATUS_OK) Toast.makeText(this,
		 * "Error when loading the road - status=" + road.mStatus,
		 * Toast.LENGTH_SHORT).show();
		 *
		 * Polyline mRoadOverlay = RoadManager.buildRoadOverlay(road, this);
		 * map.getOverlays().add(mRoadOverlay);
		 */
		
		
		// --------------------------------------------------------------

		// 3. Showing the Route steps on the map
		FolderOverlay roadMarkers = new FolderOverlay(this);
		map.getOverlays().add(roadMarkers);

		
		// ------------------------------------------------------------------------
		// My Location Overlay
		// ------------------------------------------------------------------------
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		mLocationOverlay = new DirectedLocationOverlay(this);
		map.getOverlays().add(mLocationOverlay);

		if (savedInstanceState == null) {
			Location location = mLocationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location == null)
				location = mLocationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location != null) {
				// location known:
				onLocationChanged(location);
			} else {
				// no location known: hide myLocationOverlay
				mLocationOverlay.setEnabled(false);
			}
			startPoint = null;
			// destinationPoint = null;
			// viaPoints = new ArrayList<GeoPoint>();
		} else {
			mLocationOverlay.setLocation((GeoPoint) savedInstanceState
					.getParcelable("location"));
			// TODO: restore other aspects of myLocationOverlay...
			startPoint = savedInstanceState.getParcelable("start");
			// destinationPoint =
			// savedInstanceState.getParcelable("destination");
			// viaPoints =
			// savedInstanceState.getParcelableArrayList("viapoints");
		}


	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////

	boolean startLocationUpdates() {
		boolean result = false;
		for (final String provider : mLocationManager.getProviders(true)) {
			mLocationManager.requestLocationUpdates(provider, 2 * 1000, 0.0f,
					this);
			result = true;
		}
		return result;
	}

	@Override
	protected void onResume() {
		super.onResume();
		boolean isOneProviderEnabled = startLocationUpdates();
		mLocationOverlay.setEnabled(isOneProviderEnabled);
		mTrackingMode = Globals.getInstance().isOptions_Rotating();
	
		new POILoadingTask(getString(R.string.loading)).execute("" , true);
		
		if (mPOIoverlay !=null){
			//map.getOverlays().add(mPOIoverlay);
		} 
				
		if (Globals.getInstance().getKml_File() != "") {
			new KmlLoadingTask(getString(R.string.loading) + " "
					+ Globals.getInstance().getKml_File()).execute(Globals
					.getInstance().getKml_File(), true);

			if (mKmlOverlay != null) {
				mKmlOverlay.closeAllInfoWindows();
				map.getOverlays().remove(mKmlOverlay);
			}
			//mKmlOverlay = (FolderOverlay) mKmlDocument.mKmlRoot.buildOverlay(
				//	map, buildDefaultStyle(), null, mKmlDocument);
			//map.getOverlays().add(mKmlOverlay);
			// map.invalidate();
		} else{
			if (mKmlOverlay != null) {
				try {
					
					mKmlOverlay.closeAllInfoWindows();
					map.getOverlays().remove(mKmlOverlay);
				} catch (Exception e) {
					Log.d("mKmlOverlay Error", e.toString());
				}
			}
			map.getOverlays().add(tilesOverlay);
		}
			

		map.invalidate();
		// TODO: not used currently
		// mSensorManager.registerListener(this, mOrientation,
		// SensorManager.SENSOR_DELAY_NORMAL);
		// sensor listener is causing a high CPU consumption...
	}

	@Override
	protected void onPause() {
		super.onPause();
		mLocationManager.removeUpdates(this);
		// TODO: mSensorManager.unregisterListener(this);
		// savePrefs();
	}

	void updateUIWithTrackingMode() {
		if (mTrackingMode) {
			// mTrackingModeButton.setBackgroundResource(R.drawable.btn_tracking_on);
			if (mLocationOverlay.isEnabled()
					&& mLocationOverlay.getLocation() != null) {
				map.getController().animateTo(mLocationOverlay.getLocation());
			}
			map.setMapOrientation(-mAzimuthAngleSpeed);
			// mTrackingModeButton.setKeepScreenOn(true);
		} else {
			// mTrackingModeButton.setBackgroundResource(R.drawable.btn_tracking_off);
			map.setMapOrientation(0.0f);
			// mTrackingModeButton.setKeepScreenOn(false);
		}
	}

	// 0. Using the Marker and Polyline overlays - advanced options
	class OnMarkerDragListenerDrawer implements OnMarkerDragListener {
		ArrayList<GeoPoint> mTrace;
		Polyline mPolyline;

		OnMarkerDragListenerDrawer() {
			mTrace = new ArrayList<GeoPoint>(100);
			mPolyline = new Polyline(map.getContext());
			mPolyline.setColor(0xAA0000FF);
			mPolyline.setWidth(2.0f);
			mPolyline.setGeodesic(true);
			map.getOverlays().add(mPolyline);
		}

		@Override
		public void onMarkerDrag(Marker marker) {
			// mTrace.add(marker.getPosition());
		}

		@Override
		public void onMarkerDragEnd(Marker marker) {
			mTrace.add(marker.getPosition());
			mPolyline.setPoints(mTrace);
			map.invalidate();
		}

		@Override
		public void onMarkerDragStart(Marker marker) {
			// mTrace.add(marker.getPosition());
		}
	}

	// 7. Customizing the bubble behaviour
	class CustomInfoWindow extends MarkerInfoWindow {
		POI mSelectedPoi;

		public CustomInfoWindow(MapView mapView) {
			super(R.layout.bonuspack_bubble, mapView);
			Button btn = (Button) (mView.findViewById(R.id.bubble_moreinfo));
			btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (mSelectedPoi.mUrl != null) {
						Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri
								.parse(mSelectedPoi.mUrl));
						view.getContext().startActivity(myIntent);
					} else {
						Toast.makeText(view.getContext(), "Button clicked",
								Toast.LENGTH_LONG).show();
					}
				}
			});
		}

		@Override
		public void onOpen(Object item) {
			super.onOpen(item);
			mView.findViewById(R.id.bubble_moreinfo)
					.setVisibility(View.VISIBLE);
			Marker marker = (Marker) item;
			mSelectedPoi = (POI) marker.getRelatedObject();

			// 8. put thumbnail image in bubble, fetching the thumbnail in
			// background:
			if (mSelectedPoi.mThumbnailPath != null) {
				ImageView imageView = (ImageView) mView
						.findViewById(R.id.bubble_image);
				mSelectedPoi.fetchThumbnailOnThread(imageView);
			}
		}
	}

	// 11. Customizing the clusters design - and beyond
	class CirclesGridMarkerClusterer extends RadiusMarkerClusterer {

		public CirclesGridMarkerClusterer(Context ctx) {
			super(ctx);
		}

		@Override
		public Marker buildClusterMarker(StaticCluster cluster, MapView mapView) {
			Marker m = new Marker(mapView);
			m.setPosition(cluster.getPosition());
			m.setInfoWindow(null);
			m.setAnchor(0.5f, 0.5f);
			int radius = (int) Math.sqrt(cluster.getSize() * 3);
			radius = Math.max(radius, 10);
			radius = Math.min(radius, 30);
			Bitmap finalIcon = Bitmap.createBitmap(radius * 2, radius * 2,
					mClusterIcon.getConfig());
			Canvas iconCanvas = new Canvas(finalIcon);
			Paint circlePaint = new Paint();
			if (cluster.getSize() < 20)
				circlePaint.setColor(Color.BLUE);
			else
				circlePaint.setColor(Color.RED);
			circlePaint.setAlpha(200);
			iconCanvas.drawCircle(radius, radius, radius, circlePaint);
			String text = "" + cluster.getSize();
			int textHeight = (int) (mTextPaint.descent() + mTextPaint.ascent());
			iconCanvas.drawText(text, mTextAnchorU * finalIcon.getWidth(),
					mTextAnchorV * finalIcon.getHeight() - textHeight / 2,
					mTextPaint);
			m.setIcon(new BitmapDrawable(mapView.getContext().getResources(),
					finalIcon));
			return m;
		}
	}

	// 13.2 Loading KML content - Advanced styling with Styler
	class MyKmlStyler implements KmlFeature.Styler {
		Style mDefaultStyle;

		MyKmlStyler(Style defaultStyle) {
			mDefaultStyle = defaultStyle;
		}

		@Override
		public void onLineString(Polyline polyline, KmlPlacemark kmlPlacemark,
				KmlLineString kmlLineString) {
			// Custom styling:
			polyline.setColor(Color.GREEN);
			polyline.setWidth(Math.max(
					kmlLineString.mCoordinates.size() / 200.0f, 3.0f));
		}

		@Override
		public void onPolygon(Polygon polygon, KmlPlacemark kmlPlacemark,
				KmlPolygon kmlPolygon) {
			// Keeping default styling:
			kmlPolygon.applyDefaultStyling(polygon, mDefaultStyle,
					kmlPlacemark, mKmlDocument, map);
		}

		@Override
		public void onPoint(Marker marker, KmlPlacemark kmlPlacemark,
				KmlPoint kmlPoint) {
			// Styling based on ExtendedData properties:
			if ("panda_area".equals(kmlPlacemark.getExtendedData("category")))
				kmlPlacemark.mStyle = "panda_area";
			else if ("gorilla_area".equals(kmlPlacemark
					.getExtendedData("category")))
				kmlPlacemark.mStyle = "gorilla_area";
			kmlPoint.applyDefaultStyling(marker, mDefaultStyle, kmlPlacemark,
					mKmlDocument, map);
		}

		@Override
		public void onFeature(Overlay overlay, KmlFeature kmlFeature) {
			// If nothing to do, do nothing.
		}
	}

	// 16. Handling Map events
	/*@Override
	public boolean singleTapConfirmedHelper(GeoPoint p) {
		Toast.makeText(this,
				"Tap on (" + p.getLatitude() + "," + p.getLongitude() + ")",
				Toast.LENGTH_SHORT).show();
		InfoWindow.closeAllInfoWindowsOn(map);
		return true;
	}*/

	float mGroundOverlayBearing = 0.0f;

	/*@Override
	public boolean longPressHelper(GeoPoint p) {
		// Toast.makeText(this, "Long press", Toast.LENGTH_SHORT).show();
		// 17. Using Polygon, defined as a circle:
		Polygon circle = new Polygon(this);
		circle.setPoints(Polygon.pointsAsCircle(p, 2000.0));
		circle.setFillColor(0x12121212);
		circle.setStrokeColor(Color.RED);
		circle.setStrokeWidth(2);
		map.getOverlays().add(circle);
		circle.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, map));
		circle.setTitle("Centered on " + p.getLatitude() + ","
				+ p.getLongitude());

		// 18. Using GroundOverlay
		GroundOverlay myGroundOverlay = new GroundOverlay(this);
		myGroundOverlay.setPosition(p);
		myGroundOverlay.setImage(getResources().getDrawable(
				R.drawable.ic_launcher).mutate());
		myGroundOverlay.setDimensions(2000.0f);
		// myGroundOverlay.setTransparency(0.25f);
		myGroundOverlay.setBearing(mGroundOverlayBearing);
		mGroundOverlayBearing += 20.0f;
		map.getOverlays().add(myGroundOverlay);

		map.invalidate();
		return true;
	}*/

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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("location", mLocationOverlay.getLocation());
		outState.putBoolean("isgps", mGps);
		// outState.putBoolean("tracking_mode", mTrackingMode);
		// outState.putParcelable("start", startPoint);
		// outState.putParcelable("destination", destinationPoint);
		// outState.putParcelableArrayList("viapoints", viaPoints);
		// STATIC - outState.putParcelable("road", mRoad);
		// STATIC - outState.putParcelableArrayList("poi", mPOIs);
		// STATIC - outState.putParcelable("kml", mKmlDocument);

		// savePrefs();
	}

	// -------------------------------------------------------------------
	// ------------ LocationListener implementation
	// -------------------------------------------------------------------

	private final NetworkLocationIgnorer mIgnorer = new NetworkLocationIgnorer();
	long mLastTime = 0; // milliseconds
	double mSpeed = 0.0; // km/h

	@Override
	public void onLocationChanged(final Location pLoc) {
		long currentTime = System.currentTimeMillis();
		if (mIgnorer.shouldIgnore(pLoc.getProvider(), currentTime))
			return;
		double dT = currentTime - mLastTime;
		if (dT < 100.0) {
			// Toast.makeText(this, pLoc.getProvider()+" dT="+dT,
			// Toast.LENGTH_SHORT).show();
			return;
		}
		mLastTime = currentTime;

		GeoPoint newLocation = new GeoPoint(pLoc);
		if (!mLocationOverlay.isEnabled()) {
			// we get the location for the first time:
			mLocationOverlay.setEnabled(true);
			map.getController().animateTo(newLocation);
		}

		GeoPoint prevLocation = mLocationOverlay.getLocation();
		mLocationOverlay.setLocation(newLocation);
		mLocationOverlay.setAccuracy((int) pLoc.getAccuracy());

		if (prevLocation != null
				&& pLoc.getProvider().equals(LocationManager.GPS_PROVIDER)) {
			/*
			 * double d = prevLocation.distanceTo(newLocation); mSpeed =
			 * d/dT*1000.0; // m/s mSpeed = mSpeed * 3.6; //km/h
			 */
			mSpeed = pLoc.getSpeed() * 3.6;
			
			/**
			 * @TODO
			 * For future use
			 */
			
			@SuppressWarnings("unused")
			long speedInt = Math.round(mSpeed);
			// TextView speedTxt = (TextView)findViewById(R.id.speed);
			// speedTxt.setText(speedInt + " km/h");

			// TODO: check if speed is not too small
			if (mSpeed >= 0.1) {
				// mAzimuthAngleSpeed =
				// (float)prevLocation.bearingTo(newLocation);
				mAzimuthAngleSpeed = (float) pLoc.getBearing();
				mLocationOverlay.setBearing(mAzimuthAngleSpeed);
			}
		}

		if (mTrackingMode) {
			// keep the map view centered on current location:
			map.getController().animateTo(newLocation);
			map.setMapOrientation(-mAzimuthAngleSpeed);
		} else {
			// just redraw the location overlay:
			map.invalidate();
		}
		
		speedTextView.setText("Speed: "+ pLoc.getSpeed() * 3.6);
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	// ------------ SensorEventListener implementation
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		mLocationOverlay.setAccuracy(accuracy);
		map.invalidate();
	}

	static float mAzimuthOrientation = 0.0f;

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		
		/**
		 * 
		 * We use orientation because the other way is to use
		 * ACCELEROMETER and MAGNETOMETER and they do NOT work so well YET
		 * 
		 * TODO Use other sensors if we find a way to work better
		 * 
		 */
		
		
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ORIENTATION:
			if (mSpeed < 0.1) {
				/*
				 * TODO Filter to implement... float azimuth = event.values[0];
				 * if (Math.abs(azimuth-mAzimuthOrientation)>2.0f){
				 * mAzimuthOrientation = azimuth;
				 * myLocationOverlay.setBearing(mAzimuthOrientation); if
				 * (mTrackingMode) map.setMapOrientation(-mAzimuthOrientation);
				 * else map.invalidate(); }
				 */
			}
			// at higher speed, we use speed vector, not phone orientation.
			break;
		default:
			break;
		}
	}

	// ------------------- KML --------------------------------------------
	
	ProgressDialog createSpinningDialog(String title){
		ProgressDialog pd = new ProgressDialog(map.getContext());
		pd.setTitle(title);
		pd.setMessage(getString(R.string.wait));
		pd.setCancelable(false);
		pd.setIndeterminate(true);
		return pd;
	}
	
	Style buildDefaultStyle(){
		Drawable defaultKmlMarker = getResources().getDrawable(R.drawable.marker_kml_point);
		Bitmap bitmap = ((BitmapDrawable)defaultKmlMarker).getBitmap();
		Style defaultStyle = new Style(bitmap, 0x901010AA, 3.0f, 0x20AA1010);
		return defaultStyle;
	}

	class KmlLoadingTask extends AsyncTask<Object, Void, Boolean>{
		String mUri;
		boolean mOnCreate;
		ProgressDialog mPD;
		String mMessage;
		KmlLoadingTask(String message){
			super();
			mMessage = message;
		}
		@Override protected void onPreExecute() {
			mPD = createSpinningDialog(mMessage);
			mPD.show();
		}
		@Override protected Boolean doInBackground(Object... params) {
			mUri = (String)params[0];
			mOnCreate = (Boolean)params[1];
			mKmlDocument = new KmlDocument();
			
			File mFile = new File(Environment.getExternalStorageDirectory()
					+ "/osmdroid/kml/" + Globals.getInstance().getKml_File());
			boolean ok = mKmlDocument.parseKMLFile(mFile);
			
			return ok;
		}
		@Override protected void onPostExecute(Boolean ok) {
			if (mPD != null)
				mPD.dismiss();

			if (ok) {
				Globals.getInstance().setKMLonMap(true);
				// 13.1 Simple styling
				Drawable defaultMarker = getResources().getDrawable(
						R.drawable.hiking);
				Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker)
						.getBitmap();
				Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 3.0f,
						0x20AA1010);
				// 13.2 Advanced styling with Styler
				KmlFeature.Styler styler = new MyKmlStyler(defaultStyle);

			    mKmlOverlay = (FolderOverlay) mKmlDocument.mKmlRoot
						.buildOverlay(map, defaultStyle, styler, mKmlDocument);
				map.getOverlays().add(mKmlOverlay);
				
				BoundingBoxE6 bb = mKmlDocument.mKmlRoot.getBoundingBox();
				if (bb != null) {
					// map.zoomToBoundingBox(bb); => not working in onCreate -
					// this
					// is a well-known osmdroid bug.
					// Workaround:
					map.getController().setCenter(
							new GeoPoint(bb.getLatSouthE6()
									+ bb.getLatitudeSpanE6() / 2, bb
									.getLonWestE6()
									+ bb.getLongitudeSpanE6()
									/ 2));
				}
			} else
				Toast.makeText(getApplicationContext(), "Error when loading KML",
						Toast.LENGTH_SHORT).show();

			// 14. Grab overlays in KML structure, save KML document locally
			if (mKmlDocument.mKmlRoot != null) {
				KmlFolder root = (KmlFolder) mKmlDocument.mKmlRoot;
				root.addOverlay(mRoadOverlay, mKmlDocument);
				root.addOverlay(mRoadMarkers, mKmlDocument);
				mKmlDocument.saveAsKML(mKmlDocument
						.getDefaultPathForAndroid("my_route.kml"));
				// 15. Loading and saving of GeoJSON content
				mKmlDocument.saveAsGeoJSON(mKmlDocument
						.getDefaultPathForAndroid("my_route.json"));
			}
			
			
			
			
		}
	}
	
	
	
	// ---------------------------POI-------------------------------------
	
	class POILoadingTask extends AsyncTask<Object, Void, Boolean> {
		boolean mOnCreate;
		ProgressDialog mPD;
		String mMessage;

		POILoadingTask(String message) {
			super();
			mMessage = message;
		}

		@Override
		protected void onPreExecute() {
		//	mPD = createSpinningDialog(mMessage);
		//	mPD.show();
		}

		@Override
		protected Boolean doInBackground(Object... params) {
			
		 	try {
				map.getOverlays().remove(mPOIoverlay);
			} catch (Exception e1) {
				Log.d("I WAS HERE ", e1.toString());
			}
		
			mParser = new XMLParser();
			String XMLData;
			try {
				XMLData = getXmlFromFile("/osmdroid/Options.xml");

				BufferedReader br = new BufferedReader(
						new StringReader(XMLData));
				InputSource is = new InputSource(br);

				/************ Parse XML **************/

				// XMLParser mParser = new XMLParser();
				SAXParserFactory factory = SAXParserFactory.newInstance();

				SAXParser sp;

				sp = factory.newSAXParser();

				mReader = sp.getXMLReader();
				mReader.setContentHandler(mParser);
				mReader.parse(is);
				
				//boolean ok =true;
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				mMessage= "ParserConf Error" + e;
						
			} catch (SAXException e) {
				mMessage="SAX Error" + e;
				
			} catch (IOException e) {
				mMessage= "IOError" + e;
			
			}

			// ------ End Parsing start building overlays from POIS

			return true;
		}

		@Override
		protected void onPostExecute(Boolean ok) {
			if (mPD != null)
				mPD.dismiss();
						
			if (ok) {
				 mPoisList = new ArrayList<Poi_Struct>();
				mOverlayItemArray = new ArrayList<OverlayItem>();

				mPoisList = mParser.getPois();
				int marker = R.drawable.markerbig;
				

				// ------ SELECT POIS TO DISPLAY FROM OPTIONS

				mPois_To_Display = Globals.getInstance().getPois_To_Display();
				String Pois_Used = convertNumbersToPois(mPois_To_Display);
				for (int i = 0; i < mPoisList.size(); i++) {

					if (Pois_Used.contains(mPoisList.get(i).getType().toString())) {

						mPoilItem = new OverlayItem(mPoisList.get(i).getName(),
								mPoisList.get(i).getDesc() + "#"
										+ mPoisList.get(i).getLink(), new GeoPoint(
										mPoisList.get(i).getLon(), mPoisList.get(i)
												.getLat()));

						Drawable newMarker = getApplicationContext()
								.getResources().getDrawable(marker);

						mPoilItem.setMarker(newMarker);
						mOverlayItemArray.add(mPoilItem);
						
						
					}
				}
				mPOIoverlay = new MyOwnItemizedOverlay(KMLMap.this,mOverlayItemArray);
				map.getOverlays().add(mPOIoverlay);
				map.invalidate();

			}else {
				Toast.makeText(getApplicationContext(),mMessage,
						Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	
	
	
	// --------------------------------------------------------------------
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		if (Globals.getInstance().isKMLonMap()){
			MenuItem kml_delete = menu.findItem(R.id.kml_delete);
			kml_delete.setVisible(true);	
			MenuItem kml_search = menu.findItem(R.id.kml_search);
			kml_search.setVisible(false);
			
		}else {
			MenuItem register = menu.findItem(R.id.kml_search);
			register.setVisible(true);
			MenuItem kml_delete = menu.findItem(R.id.kml_delete);
			kml_delete.setVisible(false);
			
		}
		

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.opt:
			startActivity(new Intent(getApplicationContext(), Options.class));
			return true;
			
		case R.id.kml_search:
			startActivity(new Intent(getApplicationContext(),
					File_picker_activity.class));
			return true;
		
		case R.id.kml_delete:
		    Globals.getInstance().setKml_File("");
		    Globals.getInstance().setKMLonMap(false);
		 	KMLMap.this.onResume();    
		   	return true;
	
		}
		return false;
	}
}
