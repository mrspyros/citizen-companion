package com.android.toorcomp;

import org.osmdroid.api.IGeoPoint;

// --------------------------------------------   Singleton class

public class Globals {
	private static Globals instance;
	// Global variable
	private String XmLERROR;
	private String Wait; // YES NO ERROR
	private int MapZoomLevel;
	private boolean FirstTimeOnMapActivity;
	private IGeoPoint Map_Center;
	private IGeoPoint Selected;

	public IGeoPoint getSelected() {
		return Selected;
	}

	public void setSelected(IGeoPoint loc) {
		Selected = loc;
	}

	public IGeoPoint getMap_Center() {
		return Map_Center;
	}

	public void setMap_Center(IGeoPoint iGeoPoint) {
		Map_Center = iGeoPoint;
	}

	public int getMapZoomLevel() {
		return MapZoomLevel;
	}

	public void setMapZoomLevel(int mapZoomLevel) {
		MapZoomLevel = mapZoomLevel;
	}

	public boolean isFirstTimeOnMapActivity() {
		return FirstTimeOnMapActivity;
	}

	public void setFirstTimeOnMapActivity(boolean firstTimeOnMapActivity) {
		FirstTimeOnMapActivity = firstTimeOnMapActivity;
	}

	public String getWait() {
		return Wait;
	}

	public void setWait(String wait) {
		Wait = wait;
	}

	// Restrict the constructor from being instantiated
	private Globals() {
	}

	public void setXmLERROR(String d) {
		this.XmLERROR = d;
	}

	public String getXmLERROR() {
		return this.XmLERROR;
	}

	public static synchronized Globals getInstance() {
		if (instance == null) {
			instance = new Globals();
		}
		return instance;
	}
}