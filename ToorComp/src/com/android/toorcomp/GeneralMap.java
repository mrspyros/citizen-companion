package com.android.toorcomp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
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
import org.osmdroid.bonuspack.location.GeoNamesPOIProvider;
import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.location.OverpassAPIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.location.PicasaPOIProvider;
import org.osmdroid.bonuspack.overlays.BasicInfoWindow;
import org.osmdroid.bonuspack.overlays.FolderOverlay;
import org.osmdroid.bonuspack.overlays.GroundOverlay;
import org.osmdroid.bonuspack.overlays.InfoWindow;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Marker.OnMarkerDragListener;
import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.NetworkLocationIgnorer;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.DirectedLocationOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

//import com.osmnavigator.R;










import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class GeneralMap extends Base_Activity implements MapEventsReceiver, LocationListener, SensorEventListener  {

	MapView map;
	
	private String Pois_To_Display;
	protected LocationManager mLocationManager;
	protected DirectedLocationOverlay myLocationOverlay;
	float mAzimuthAngleSpeed = 0.0f;
	protected boolean mTrackingMode=true;  // FolowLocation
	protected boolean mGps=false;
	
	public static KmlDocument mKmlDocument; //made static to pass between activities
	public static Stack<KmlFeature> mKmlStack; //passed between activities, top is the current KmlFeature to edit. 
	public static KmlFolder mKmlClipboard; 
	
	// Default map Latitude:
	private double MAP_DEFAULT_LATITUDE = 39.524125;
	// Default map Longitude:
	private double MAP_DEFAULT_LONGITUDE = 20.881799;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		// Introduction
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		map = (MapView) findViewById(R.id.mapview);
		map.setBuiltInZoomControls(true);
		map.setMultiTouchControls(true);
		
		
		// ------------- check if GPS is enabled
		// ------------- only one time
		
		final GPSTracker mGPS = new GPSTracker(this);
		if (!mGPS.canGetLocation && !mGps)
			{
			  mGPS.showSettingsAlert();
			  mGps=true;
			}
		
		
		GeoPoint startPoint = new GeoPoint(MAP_DEFAULT_LATITUDE, MAP_DEFAULT_LONGITUDE);
		
		
		IMapController mapController = map.getController();
		mapController.setZoom(10);
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
		RoadManager roadManager = new OSRMRoadManager();
		// 2. Playing with the RoadManager
		// roadManager roadManager = new MapQuestRoadManager("YOUR_API_KEY");
		// roadManager.addRequestOption("routeType=bicycle");
		ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
		waypoints.add(startPoint);
		//GeoPoint endPoint = new GeoPoint(MAP_DEFAULT_LATITUDE, MAP_DEFAULT_LONGITUDE);
		//waypoints.add(endPoint);
		Road road = roadManager.getRoad(waypoints);
		if (road.mStatus != Road.STATUS_OK)
			Toast.makeText(this,
					"Error when loading the road - status=" + road.mStatus,
					Toast.LENGTH_SHORT).show();

		Polyline roadOverlay = RoadManager.buildRoadOverlay(road, this);
		map.getOverlays().add(roadOverlay);
//--------------------------------------------------------------
		
		
		// 3. Showing the Route steps on the map
		FolderOverlay roadMarkers = new FolderOverlay(this);
		map.getOverlays().add(roadMarkers);
		
//----------------------------------------------------------------		
		
		
		// 12. Loading KML content
		// String url =
		// "http://mapsengine.google.com/map/kml?mid=z6IJfj90QEd4.kUUY9FoHFRdE";
		mKmlDocument = new KmlDocument();
		
		
		// boolean ok = mKmlDocument.parseKMLUrl(url);

		File mFile = new File(Environment.getExternalStorageDirectory()
				+ "/osmdroid/kml/kml.kml");

		boolean ok = mKmlDocument.parseKMLFile(mFile);

		// Get OpenStreetMap content as KML with Overpass API:
		/*
		 * OverpassAPIProvider overpassProvider = new OverpassAPIProvider();
		 * BoundingBoxE6 oBB = new BoundingBoxE6(startPoint.getLatitude()+0.25,
		 * startPoint.getLongitude()+0.25, startPoint.getLatitude()-0.25,
		 * startPoint.getLongitude()-0.25); String oUrl =
		 * overpassProvider.urlForTagSearchKml("highway=speed_camera", oBB, 500,
		 * 30); boolean ok =
		 * overpassProvider.addInKmlFolder(mKmlDocument.mKmlRoot, oUrl);
		 */

		if (ok) {
			// 13.1 Simple styling
			Drawable defaultMarker = getResources().getDrawable(
					R.drawable.marker_kml_point);
			Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
			Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 3.0f,
					0x20AA1010);
			// 13.2 Advanced styling with Styler
			KmlFeature.Styler styler = new MyKmlStyler(defaultStyle);

			FolderOverlay kmlOverlay = (FolderOverlay) mKmlDocument.mKmlRoot
					.buildOverlay(map, defaultStyle, styler, mKmlDocument);
			map.getOverlays().add(kmlOverlay);
			BoundingBoxE6 bb = mKmlDocument.mKmlRoot.getBoundingBox();
			if (bb != null) {
				// map.zoomToBoundingBox(bb); => not working in onCreate - this
				// is a well-known osmdroid bug.
				// Workaround:
				map.getController().setCenter(
						new GeoPoint(bb.getLatSouthE6()
								+ bb.getLatitudeSpanE6() / 2, bb.getLonWestE6()
								+ bb.getLongitudeSpanE6() / 2));
			}
		} else
			Toast.makeText(this, "Error when loading KML", Toast.LENGTH_SHORT)
					.show();

		// 14. Grab overlays in KML structure, save KML document locally
		if (mKmlDocument.mKmlRoot != null) {
			KmlFolder root = (KmlFolder) mKmlDocument.mKmlRoot;
			root.addOverlay(roadOverlay, mKmlDocument);
			root.addOverlay(roadMarkers, mKmlDocument);
			mKmlDocument.saveAsKML(mKmlDocument
					.getDefaultPathForAndroid("my_route.kml"));
			// 15. Loading and saving of GeoJSON content
			mKmlDocument.saveAsGeoJSON(mKmlDocument
					.getDefaultPathForAndroid("my_route.json"));
		}

		
// ------------------------------------------------------------------------
		// My Location Overlay
// ------------------------------------------------------------------------
		mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		myLocationOverlay = new DirectedLocationOverlay(this);
		map.getOverlays().add(myLocationOverlay);

		if (savedInstanceState == null){
			Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location == null)
				location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location != null) {
				//location known:
				onLocationChanged(location);
			} else {
				//no location known: hide myLocationOverlay
				myLocationOverlay.setEnabled(false);
			}
			startPoint = null;
			//destinationPoint = null;
		    //viaPoints = new ArrayList<GeoPoint>();
		} else {
			myLocationOverlay.setLocation((GeoPoint)savedInstanceState.getParcelable("location"));
			//TODO: restore other aspects of myLocationOverlay...
			startPoint = savedInstanceState.getParcelable("start");
			//destinationPoint = savedInstanceState.getParcelable("destination");
			//viaPoints = savedInstanceState.getParcelableArrayList("viapoints");
		}
		
		
		
//--------------------------------------------------------------------------
// XML Parser 
//--------------------------------------------------------------------------
		
		XMLParser parser = new XMLParser();
		String XMLData;
		try {
			XMLData = getXmlFromFile("/osmdroid/Options.xml");

			BufferedReader br = new BufferedReader(new StringReader(XMLData));
			InputSource is = new InputSource(br);

			/************ Parse XML **************/

			// XMLParser parser = new XMLParser();
			SAXParserFactory factory = SAXParserFactory.newInstance();

			SAXParser sp;

			sp = factory.newSAXParser();

			XMLReader reader = sp.getXMLReader();
			reader.setContentHandler(parser);
			reader.parse(is);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "ParserConf Error" + e, Toast.LENGTH_SHORT)
					.show();
		} catch (SAXException e) {
			Toast.makeText(this, "SAX Error" + e, Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(this, "IOError" + e, Toast.LENGTH_SHORT).show();
		}

		//------ End Parsing start building overlays from POIS 
		
		List<Poi_Struct> _pois = new ArrayList<Poi_Struct>();
		ArrayList<OverlayItem> overlayItemArray = new ArrayList<OverlayItem>();
		
		_pois = parser.getPois();
		int marker = R.drawable.markerbig;
		OverlayItem olItem;

		// ------ SELECT POIS TO DISPLAY FROM OPTIONS

	
		Pois_To_Display = Globals.getInstance().getPois_To_Display();
		String Pois_Used = convertNumbersToPois(Pois_To_Display);
		for (int i = 0; i < _pois.size(); i++) {

			if (Pois_Used.contains(_pois.get(i).getType().toString()))
			{

			olItem = new OverlayItem(_pois.get(i).getName(), _pois.get(i)
					.getDesc() + "#" + _pois.get(i).getLink(), new GeoPoint(
					_pois.get(i).getLon(), _pois.get(i).getLat()));

			Drawable newMarker = this.getResources().getDrawable(marker);

			olItem.setMarker(newMarker);
			overlayItemArray.add(olItem);
			
			}
		}

		// ---------- Here we put xml overlays to map -----------------
		MyOwnItemizedOverlay overlay = new MyOwnItemizedOverlay(this,
				overlayItemArray);
		map.getOverlays().add(0, overlay);

// ///////////////////////////////////

		// 16. Handling Map events
		MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
		map.getOverlays().add(0, mapEventsOverlay); // inserted at the "bottom"
													// of all overlays
	}

//////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	boolean startLocationUpdates(){
		boolean result = false;
		for (final String provider : mLocationManager.getProviders(true)) {
			mLocationManager.requestLocationUpdates(provider, 2*1000, 0.0f, this);
			result = true;
		}
		return result;
	}

	@Override protected void onResume() {
		super.onResume();
		boolean isOneProviderEnabled = startLocationUpdates();
		myLocationOverlay.setEnabled(isOneProviderEnabled);
		mTrackingMode = Globals.getInstance().isOptions_Rotating();
		
		//TODO: not used currently
		//mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
			//sensor listener is causing a high CPU consumption...
	}

	@Override protected void onPause() {
		super.onPause();
		mLocationManager.removeUpdates(this);
		//TODO: mSensorManager.unregisterListener(this);
		//savePrefs();
	}
	
	
	
	void updateUIWithTrackingMode(){
		if (mTrackingMode){
	//		mTrackingModeButton.setBackgroundResource(R.drawable.btn_tracking_on);
			if (myLocationOverlay.isEnabled()&& myLocationOverlay.getLocation() != null){
				map.getController().animateTo(myLocationOverlay.getLocation());
			}
			map.setMapOrientation(-mAzimuthAngleSpeed);
		//	mTrackingModeButton.setKeepScreenOn(true);
		} else {
		//	mTrackingModeButton.setBackgroundResource(R.drawable.btn_tracking_off);
			map.setMapOrientation(0.0f);
		//	mTrackingModeButton.setKeepScreenOn(false);
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
	@Override
	public boolean singleTapConfirmedHelper(GeoPoint p) {
		Toast.makeText(this,
				"Tap on (" + p.getLatitude() + "," + p.getLongitude() + ")",
				Toast.LENGTH_SHORT).show();
		InfoWindow.closeAllInfoWindowsOn(map);
		return true;
	}

	float mGroundOverlayBearing = 0.0f;

	@Override
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
	}

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

	@Override protected void onSaveInstanceState (Bundle outState){
		outState.putParcelable("location", myLocationOverlay.getLocation());
		outState.putBoolean("isgps",mGps);
		//outState.putBoolean("tracking_mode", mTrackingMode);
		//outState.putParcelable("start", startPoint);
		//outState.putParcelable("destination", destinationPoint);
		//outState.putParcelableArrayList("viapoints", viaPoints);
		//STATIC - outState.putParcelable("road", mRoad);
		//STATIC - outState.putParcelableArrayList("poi", mPOIs);
		//STATIC - outState.putParcelable("kml", mKmlDocument);
		
		//savePrefs();
	}
	
	//-------------------------------------------------------------------	
	//------------ LocationListener implementation
	//-------------------------------------------------------------------
	
	
		private final NetworkLocationIgnorer mIgnorer = new NetworkLocationIgnorer();
		long mLastTime = 0; // milliseconds
		double mSpeed = 0.0; // km/h
		@Override public void onLocationChanged(final Location pLoc) {
			long currentTime = System.currentTimeMillis();
			if (mIgnorer.shouldIgnore(pLoc.getProvider(), currentTime))
	            return;
			double dT = currentTime - mLastTime;
			if (dT < 100.0){
				//Toast.makeText(this, pLoc.getProvider()+" dT="+dT, Toast.LENGTH_SHORT).show();
				return;
			}
			mLastTime = currentTime;
			
			GeoPoint newLocation = new GeoPoint(pLoc);
			if (!myLocationOverlay.isEnabled()){
				//we get the location for the first time:
				myLocationOverlay.setEnabled(true);
				map.getController().animateTo(newLocation);
			}
			
			GeoPoint prevLocation = myLocationOverlay.getLocation();
			myLocationOverlay.setLocation(newLocation);
			myLocationOverlay.setAccuracy((int)pLoc.getAccuracy());

			if (prevLocation != null && pLoc.getProvider().equals(LocationManager.GPS_PROVIDER)){
				/*
				double d = prevLocation.distanceTo(newLocation);
				mSpeed = d/dT*1000.0; // m/s
				mSpeed = mSpeed * 3.6; //km/h
				*/
				mSpeed = pLoc.getSpeed() * 3.6;
				long speedInt = Math.round(mSpeed);
				//TextView speedTxt = (TextView)findViewById(R.id.speed);
				//speedTxt.setText(speedInt + " km/h");
				
				//TODO: check if speed is not too small
				if (mSpeed >= 0.1){
					//mAzimuthAngleSpeed = (float)prevLocation.bearingTo(newLocation);
					mAzimuthAngleSpeed = (float)pLoc.getBearing();
					myLocationOverlay.setBearing(mAzimuthAngleSpeed);
				}
			}
			
			if (mTrackingMode){
				//keep the map view centered on current location:
				map.getController().animateTo(newLocation);
				map.setMapOrientation(-mAzimuthAngleSpeed);
			} else {
				//just redraw the location overlay:
				map.invalidate();
			}
		}

		@Override public void onProviderDisabled(String provider) {}
		

		@Override public void onProviderEnabled(String provider) {}

		@Override public void onStatusChanged(String provider, int status, Bundle extras) {}

			
		//------------ SensorEventListener implementation
		@Override public void onAccuracyChanged(Sensor sensor, int accuracy) {
			myLocationOverlay.setAccuracy(accuracy);
			map.invalidate();
		}

		static float mAzimuthOrientation = 0.0f;
		@Override public void onSensorChanged(SensorEvent event) {
			switch (event.sensor.getType()){
				case Sensor.TYPE_ORIENTATION: 
					if (mSpeed < 0.1){
						/* TODO Filter to implement...
						float azimuth = event.values[0];
						if (Math.abs(azimuth-mAzimuthOrientation)>2.0f){
							mAzimuthOrientation = azimuth;
							myLocationOverlay.setBearing(mAzimuthOrientation);
							if (mTrackingMode)
								map.setMapOrientation(-mAzimuthOrientation);
							else
								map.invalidate();
						}
						*/
					}
					//at higher speed, we use speed vector, not phone orientation. 
					break;
				default:
					break;
			}
		}
		
		
	
}
