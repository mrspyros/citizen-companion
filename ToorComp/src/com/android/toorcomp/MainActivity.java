package com.android.toorcomp;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * @author mrspyros
 * 
 */
public class MainActivity extends Base_Activity {

	private static final String MY_PREFS_NAME = "CitizenCompanion";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		boolean prefs;
		File f = new File(
				"/data/data/com.android.toorcomp/shared_prefs/CitizenCompanion.xml");
		if (f.exists()) {
			prefs = get_prefs();
		} else
			init_prefs();

		// ------ Check ic XML Exists and notify globals

		final Intent intent = new Intent(getApplicationContext(),
				MainScreen.class);
		final Startup init = new Startup(this);
		Globals g = Globals.getInstance();
		g.setOptions_Changed(false);
		Boolean XMLExists = init.CheckXML();

		if (!XMLExists) {
			g.setXmLERROR(true);
			g.set_Xml_Download_Answer("YES");
		} else
			g.setXmLERROR(false);
		finish();
		startActivity(intent);

	}

	private boolean get_prefs() {

		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,
				MODE_PRIVATE);

		Globals g = Globals.getInstance();
		g.setOfflineMap(prefs.getBoolean("Offline_Map", false));
		g.setPois_To_Display(prefs.getString("Pois_To_Display", "123456"));

		// pref.getBoolean("key_name1", null); // getting boolean
		// pref.getInt("key_name2", null); // getting Integer
		// pref.getFloat("key_name3", null); // getting Float
		// pref.getLong("key_name4", null); // getting Long
		// pref.getString("key_name5", null); // getting String

		return true;
	}

	private boolean init_prefs() {
		Globals g = Globals.getInstance();
		g.setOfflineMap(false);
		g.setPois_To_Display("123456");

		return true;
	}

}