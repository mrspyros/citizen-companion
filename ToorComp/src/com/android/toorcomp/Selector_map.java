package com.android.toorcomp;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Selector_map extends Activity /* implements MapEventsReceiver */{
	// The MapView variable:
	// private MapView m_mapView;

	// Default map zoom level:
	private int MAP_DEFAULT_ZOOM = 14;

	// Default map Latitude:
	private double MAP_DEFAULT_LATITUDE = 39.524125;

	// Default map Longitude:
	private double MAP_DEFAULT_LONGITUDE = 20.881799;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		final MapView m_mapView = new MapView(this, 256);

		if (!Globals.getInstance().isFirstTimeOnMapActivity())
			MAP_DEFAULT_ZOOM = 13;// Globals.getInstance().getMapZoomLevel();

		// ---------------------------- setting up
		// Layout---------------------------
		TextView myTextView = new TextView(this);
		myTextView.setTextAppearance(this,
				android.R.style.TextAppearance_Large_Inverse);
		myTextView.setText("");

		Button selectBtn = new Button(this);
		selectBtn.setText("Επιλογή");
		selectBtn.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				Globals.getInstance().setMap_Center(m_mapView.getMapCenter());
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED, returnIntent);
				finish();
				// Toast.makeText(getApplicationContext(),
				// "Η Επιλογή σας είναι"+(m_mapView.getMapCenter().getLongitudeE6()+" - "+m_mapView.getMapCenter().getLatitudeE6()),
				// Toast.LENGTH_LONG).show();
				// int lon = mapView.getMapCenter().getLongitudeE6();
				// int lat = mapView.getMapCenter().getLatitudeE6();

				// finish();
			}
		});

		ImageView cross = new ImageView(this);
		cross.setImageResource(R.drawable.cross);

		final RelativeLayout relativeLayout = new RelativeLayout(this);
		final RelativeLayout.LayoutParams mapViewLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);

		final RelativeLayout.LayoutParams crossLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		crossLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		crossLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

		final RelativeLayout.LayoutParams textViewLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		final RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

		relativeLayout.addView(m_mapView, mapViewLayoutParams);
		relativeLayout.addView(cross, crossLayoutParams);
		relativeLayout.addView(myTextView, textViewLayoutParams);
		relativeLayout.addView(selectBtn, buttonLayoutParams);

		setContentView(relativeLayout);
		// ---------------------------- setting up frontend
		// END----------------------------

		m_mapView.setBuiltInZoomControls(true);
		m_mapView.setMultiTouchControls(true);
		m_mapView.setClickable(false);
		m_mapView.setUseDataConnection(true);
		m_mapView.getController().setZoom(MAP_DEFAULT_ZOOM);
		m_mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
		m_mapView.getController().setCenter(
				new GeoPoint(MAP_DEFAULT_LATITUDE, MAP_DEFAULT_LONGITUDE));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// creates a menu inflater
		MenuInflater inflater = getMenuInflater();
		// generates a Menu from a menu resource file
		// R.menu.main_menu represents the ID of the XML resource file
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.opt:

			startActivity(new Intent(getApplicationContext(), Options.class));

			return true;

		}
		return false;
	}

}