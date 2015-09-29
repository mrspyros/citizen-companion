package com.android.toorcomp;

import org.osmdroid.api.IGeoPoint;
 
// --------------------------------------------   Singleton class

public class Globals {
	
	
	private static Globals instance;
	// Global variable
	
	private static String PROGRAMVERSION;
	private static String DBVERSION;
	
	private boolean isPRGVersionChanged;
	private boolean isDBVersionchanged;
	
	
	private String NewDBVersion;
	
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
	private boolean Options_Wifi;
	private boolean Options_Roaming;
	private boolean Options_Rotating;
    private boolean KMLonMap;
    private boolean IsNetworkAvailable;
    private int DownloadID = 101;
	
	
	private String Kml_File;
    
	
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

	public boolean isOptions_Wifi() {
		return Options_Wifi;
	}

	public void setOptions_Wifi(boolean options_Wifi) {
		Options_Wifi = options_Wifi;
	}

	public boolean isOptions_Roaming() {
		return Options_Roaming;
	}

	public void setOptions_Roaming(boolean options_roaming) {
		Options_Roaming = options_roaming;
	}

	public boolean isOptions_Rotating() {
		return Options_Rotating;
	}

	public void setOptions_Rotating(boolean options_Rotating) {
		Options_Rotating = options_Rotating;
	}

	public String getKml_File() {
		return Kml_File;
	}

	public void setKml_File(String kml_File) {
		Kml_File = kml_File;
	}

	public boolean isKMLonMap() {
		return KMLonMap;
	}

	public void setKMLonMap(boolean kMLonMap) {
		KMLonMap = kMLonMap;
	}

	public static String getDBVERSION() {
		return DBVERSION;
	}

	public static void setDBVERSION(String dBVERSION) {
		DBVERSION = dBVERSION;
	}

	public static String getPROGRAMVERSION() {
		return PROGRAMVERSION;
	}

	public static void setPROGRAMVERSION(String pROGRAMVERSION) {
		PROGRAMVERSION = pROGRAMVERSION;
	}

	public boolean isPRGVersionChanged() {
		return isPRGVersionChanged;
	}

	public void setPRGVersionChanged(boolean isPRGVersionChanged) {
		this.isPRGVersionChanged = isPRGVersionChanged;
	}

	public boolean isDBVersionchanged() {
		return isDBVersionchanged;
	}

	public void setDBVersionchanged(boolean isDBVersionchanged) {
		this.isDBVersionchanged = isDBVersionchanged;
	}

	public String getNewDBVersion() {
		return NewDBVersion;
	}

	public void setNewDBVersion(String newDBVersion) {
		NewDBVersion = newDBVersion;
	}

	public boolean isIsNetworkAvailable() {
		return IsNetworkAvailable;
	}

	public void setIsNetworkAvailable(boolean isNetworkAvailable) {
		IsNetworkAvailable = isNetworkAvailable;
	}

	public int getDownloadID() {
		return DownloadID;
	}

	public void setDownloadID(int downloadID) {
		DownloadID = downloadID;
	}
}