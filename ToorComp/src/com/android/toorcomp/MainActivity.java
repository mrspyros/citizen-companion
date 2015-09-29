package com.android.toorcomp;

/*

CitizenCompanion
Copyright (C) {2015}  {mrspyros}

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.


*/
 

import java.io.File;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * @author mrspyros
 * 
 * We use this Activity to initialize stuff only for one time
 * 
 */
public class MainActivity extends Base_Activity {

	private static final String MY_PREFS_NAME = "CitizenCompanion";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		Globals.getInstance().setKml_File("");
		Globals.getInstance().setKMLonMap(false);
		
		boolean prefs;
		File f = new File(
				"/data/data/com.android.toorcomp/shared_prefs/CitizenCompanion.xml");
		if (f.exists()) {
			prefs = get_prefs();
		} else
			init_prefs();

		// ------ Check if XML Exists and notify globals

		final Intent intent = new Intent(getApplicationContext(),
				MainScreen.class);
		
		Globals g = Globals.getInstance();
		g.setOptions_Changed(false);
		finish();
		startActivity(intent);

	}

	private boolean get_prefs() {

		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,
				MODE_PRIVATE);

		Globals g = Globals.getInstance();
		g.setOfflineMap(prefs.getBoolean("Offline_Map", false));
		g.setPois_To_Display(prefs.getString("Pois_To_Display", "123456"));
		g.setOptions_Wifi(prefs.getBoolean("Wifi", false));
		g.setOptions_Roaming(prefs.getBoolean("Offline_Map", false));
		g.setOptions_Rotating(prefs.getBoolean("Rotating", true));
		
		Globals.setDBVERSION(prefs.getString("DBVERSION", "1.0.0.1"));
        Globals.setPROGRAMVERSION(prefs.getString("PROGRAMVERSION","1.0.0.1"));
		
		// pref.getBoolean("key_name1", null); // getting boolean
		// pref.getInt("key_name2", null); // getting Integer
		// pref.getFloat("key_name3", null); // getting Float
		// pref.getLong("key_name4", null); // getting Long
		// pref.getString("key_name5", null); // getting String

		return true;
	}

	private boolean init_prefs() {
		Globals g = Globals.getInstance();
		g.setOfflineMap(true);
		g.setPois_To_Display("123456");
		g.setOptions_Wifi(true);
		g.setOptions_Roaming(true);
		g.setOptions_Rotating(true);
        Globals.setDBVERSION("1.0.0.1");
        Globals.setPROGRAMVERSION("1.0.0.1");
		return true;
	}

}