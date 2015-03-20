package com.android.toorcomp;

import org.osmdroid.api.IGeoPoint;

// --------------------------------------------   Singleton class

public class Globals {
	private static Globals instance;
	// Global variable
	private boolean XmLERROR;
	private String Wait; // YES NO ERROR
	private int MapZoomLevel;
	private boolean FirstTimeOnMapActivity;
	private IGeoPoint Map_Center;
	private IGeoPoint Selected;
	private String WebViewUrl="";
    private String _Xml_Download_Answer;                                                       
    private boolean OfflineMap;
    private boolean Enable_Pois;
    private String Pois_To_Display;  // 12345 for all zeroes if not
    private boolean Options_Changed;
	
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

	public void setXmLERROR(boolean b) {
		this.XmLERROR = b;
	}

	public boolean getXmLERROR() {
		return this.XmLERROR;
	}

	public static synchronized Globals getInstance() {
		if (instance == null) {
			instance = new Globals();
		}
		return instance;
	}

	public String getWebViewUrl() {
		return WebViewUrl;
	}

	public void setWebViewUrl(String webViewUrl) {
		WebViewUrl = webViewUrl;
	}

	public String get_Xml_Download_Answer() {
		return _Xml_Download_Answer;
	}

	public void set_Xml_Download_Answer(String _Xml_Download_Answer) {
		this._Xml_Download_Answer = _Xml_Download_Answer;
	}

	public boolean isOfflineMap() {
		return OfflineMap;
	}

	public void setOfflineMap(boolean offlineMap) {
		OfflineMap = offlineMap;
	}

	public boolean isEnable_Pois() {
		return Enable_Pois;
	}

	public void setEnable_Pois(boolean enable_Pois) {
		Enable_Pois = enable_Pois;
	}

	public String getPois_To_Display() {
		return Pois_To_Display;
	}

	public void setPois_To_Display(String pois_To_Display) {
		Pois_To_Display = pois_To_Display;
	}

	public boolean isOptions_Changed() {
		return Options_Changed;
	}

	public void setOptions_Changed(boolean options_Changed) {
		Options_Changed = options_Changed;
	}
}