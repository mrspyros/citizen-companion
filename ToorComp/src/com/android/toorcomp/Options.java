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


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * This class is manipulating Options menu
 * and sets sharedprefs is something is changed in options
 *
 */

public class Options extends Activity {

	private static final String MY_PREFS_NAME = "CitizenCompanion";
	private String mPois_To_Display;
	private boolean mOfflinemap;
	private boolean mWifi;
	private boolean mRoaming;
	private boolean mRotating;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);

		Globals g = Globals.getInstance();

		final LinearLayout Lin_Poi = (LinearLayout) this
				.findViewById(R.id.Lin_poi);
		final TextView Opt_Poi = (TextView) this.findViewById(R.id.Opt_poi);
		Opt_Poi.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (Lin_Poi.isShown()) {
					Lin_Poi.setVisibility(View.GONE);
				} else {
					Lin_Poi.setVisibility(View.VISIBLE);
				}
			}
		});

		final LinearLayout Lin_Map = (LinearLayout) this
				.findViewById(R.id.Lin_map);
		final TextView Opt_Map = (TextView) this.findViewById(R.id.Opt_map);
		Opt_Map.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Lin_Map.isShown()) {
					Lin_Map.setVisibility(View.GONE);
				} else {
					Lin_Map.setVisibility(View.VISIBLE);
				}
			}
		});

		final LinearLayout Lin_Conn = (LinearLayout) this
				.findViewById(R.id.Lin_conn);
		final TextView Opt_Conn = (TextView) this.findViewById(R.id.Opt_conn);
		Opt_Conn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Lin_Conn.isShown()) {
					Lin_Conn.setVisibility(View.GONE);
				} else {
					Lin_Conn.setVisibility(View.VISIBLE);
				}
			}
		});

		final CheckBox poi1 = (CheckBox) findViewById(R.id.poi1);
		if (g.getPois_To_Display().contains("1")) {
			poi1.setChecked(true);
		} else
			poi1.setChecked(false);

		poi1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				onCheckboxClicked(v);

			}
		});
		;

		final CheckBox poi2 = (CheckBox) findViewById(R.id.poi2);
		if (g.getPois_To_Display().contains("2")) {
			poi2.setChecked(true);
		} else
			poi2.setChecked(false);

		poi2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				onCheckboxClicked(v);

			}
		});
		;

		final CheckBox poi3 = (CheckBox) findViewById(R.id.poi3);
		if (g.getPois_To_Display().contains("3")) {
			poi3.setChecked(true);
		} else
			poi3.setChecked(false);
		poi3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				onCheckboxClicked(v);

			}
		});
		;

		final CheckBox poi4 = (CheckBox) findViewById(R.id.poi4);
		if (g.getPois_To_Display().contains("4")) {
			poi4.setChecked(true);
		} else
			poi4.setChecked(false);
		poi4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				onCheckboxClicked(v);

			}
		});
		;

		final CheckBox poi5 = (CheckBox) findViewById(R.id.poi5);
		if (g.getPois_To_Display().contains("5")) {
			poi5.setChecked(true);
		} else
			poi5.setChecked(false);
		poi5.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				onCheckboxClicked(v);

			}
		});
		;

		final CheckBox poi6 = (CheckBox) findViewById(R.id.poi6);
		if (g.getPois_To_Display().contains("6")) {
			poi6.setChecked(true);
		} else
			poi6.setChecked(false);
		poi6.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				onCheckboxClicked(v);

			}
		});
		;

		final CheckBox _offlinemap = (CheckBox) findViewById(R.id.offlinemap);
		_offlinemap.setChecked(g.isOfflineMap());
		_offlinemap.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				onCheckboxClicked(v);

			}
		});
		;

		final CheckBox _rotatingmap = (CheckBox) findViewById(R.id.rotatingmap);
		_rotatingmap.setChecked(g.isOptions_Rotating());
		_rotatingmap.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				onCheckboxClicked(v);

			}
		});

		final CheckBox opt_wifi = (CheckBox) findViewById(R.id.opt_wifi);
		if (g.isOptions_Wifi()) {
			opt_wifi.setChecked(true);
		} else
			opt_wifi.setChecked(false);
		opt_wifi.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				onCheckboxClicked(v);

			}
		});
		;

		final CheckBox opt_roaming = (CheckBox) findViewById(R.id.opt_roaming);
		if (g.isOptions_Roaming()) {
			opt_roaming.setChecked(true);
		} else
			opt_roaming.setChecked(false);
		opt_roaming.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				onCheckboxClicked(v);

			}
		});
		;

		Button btn1 = (Button) findViewById(R.id.btn1);
		btn1.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first

	}

	public void onCheckboxClicked(View view) {
		CheckBox poi1 = (CheckBox) findViewById(R.id.poi1);
		CheckBox poi2 = (CheckBox) findViewById(R.id.poi2);
		CheckBox poi3 = (CheckBox) findViewById(R.id.poi3);
		CheckBox poi4 = (CheckBox) findViewById(R.id.poi4);
		CheckBox poi5 = (CheckBox) findViewById(R.id.poi5);
		CheckBox poi6 = (CheckBox) findViewById(R.id.poi6);

		if (poi1.isChecked())
			mPois_To_Display = "1";
		else
			mPois_To_Display = "0";
		if (poi2.isChecked())
			mPois_To_Display = mPois_To_Display.concat("2");
		else
			mPois_To_Display = mPois_To_Display.concat("0");
		if (poi3.isChecked())
			mPois_To_Display = mPois_To_Display.concat("3");
		else
			mPois_To_Display = mPois_To_Display.concat("0");
		if (poi4.isChecked())
			mPois_To_Display = mPois_To_Display.concat("4");
		else
			mPois_To_Display = mPois_To_Display.concat("0");
		if (poi5.isChecked())
			mPois_To_Display = mPois_To_Display.concat("5");
		else
			mPois_To_Display = mPois_To_Display.concat("0");
		if (poi6.isChecked())
			mPois_To_Display = mPois_To_Display.concat("6");
		else
			mPois_To_Display = mPois_To_Display.concat("0");

		final CheckBox _offlinemap = (CheckBox) findViewById(R.id.offlinemap);
		final CheckBox _wifi = (CheckBox) findViewById(R.id.opt_wifi);
		final CheckBox _roaming = (CheckBox) findViewById(R.id.opt_roaming);
		final CheckBox _rotatingmap = (CheckBox) findViewById(R.id.rotatingmap);

		if (_offlinemap.isChecked())
			mOfflinemap = true;
		else
			mOfflinemap = false;
		if (_wifi.isChecked())
			mWifi = true;
		else
			mWifi = false;
		if (_roaming.isChecked())
			mRoaming = true;
		else
			mRoaming = false;
		if (_rotatingmap.isChecked())
			mRotating = true;
		else
			mRotating = false;

		set_prefs();
	}

	private boolean set_prefs() {

		SharedPreferences sharedPref = getSharedPreferences(MY_PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("mPois_To_Display", mPois_To_Display);
		editor.putBoolean("Offline_Map", mOfflinemap);
		editor.putBoolean("Wifi", mWifi);
		editor.putBoolean("Roaming", mRoaming);
		editor.putBoolean("Rotating", mRotating);

		// Now set new prefs on Globals

		Globals g = Globals.getInstance();
		g.setOfflineMap(mOfflinemap);
		g.setPois_To_Display(mPois_To_Display);
		g.setOptions_Wifi(mWifi);
		g.setOptions_Roaming(mRoaming);
		g.setOptions_Rotating(mRotating);
		g.setOptions_Changed(true);

		return true;
	}

}
