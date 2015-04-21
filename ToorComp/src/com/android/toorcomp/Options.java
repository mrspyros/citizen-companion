package com.android.toorcomp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Options extends Activity {

	private static final String MY_PREFS_NAME = "CitizenCompanion";
	private String Pois_To_Display;
	private boolean offlinemap;
    private boolean wifi;
    private boolean roaming;
    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);

		
		
		Globals g = Globals.getInstance();

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
		
		if (poi1.isChecked()) Pois_To_Display="1"; else Pois_To_Display="0";
		if (poi2.isChecked()) Pois_To_Display=Pois_To_Display.concat("2"); else Pois_To_Display=Pois_To_Display.concat("0");
		if (poi3.isChecked()) Pois_To_Display=Pois_To_Display.concat("3"); else Pois_To_Display=Pois_To_Display.concat("0");
		if (poi4.isChecked()) Pois_To_Display=Pois_To_Display.concat("4"); else Pois_To_Display=Pois_To_Display.concat("0");
		if (poi5.isChecked()) Pois_To_Display=Pois_To_Display.concat("5"); else Pois_To_Display=Pois_To_Display.concat("0");
		if (poi6.isChecked()) Pois_To_Display=Pois_To_Display.concat("6"); else Pois_To_Display=Pois_To_Display.concat("0");
		
		final CheckBox _offlinemap = (CheckBox) findViewById(R.id.offlinemap);
		final CheckBox _wifi = (CheckBox) findViewById(R.id.opt_wifi);
		final CheckBox _roaming = (CheckBox) findViewById(R.id.opt_roaming);
		
		if (_offlinemap.isChecked()) offlinemap = true; else offlinemap = false;
		if (_wifi.isChecked()) wifi = true; else wifi = false;
		if (_roaming.isChecked()) roaming = true; else roaming = false;
		
		set_prefs();
	}

	private boolean set_prefs() {

		SharedPreferences sharedPref = getSharedPreferences(MY_PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("Pois_To_Display", Pois_To_Display);
		editor.putBoolean("Offline_Map", offlinemap);
		editor.putBoolean("Wifi", wifi);
		editor.putBoolean("Roaming", roaming);
		editor.commit();

		// Now set new prefs on Globals

		Globals g = Globals.getInstance();
		g.setOfflineMap(offlinemap);
		g.setPois_To_Display(Pois_To_Display);
		g.setOptions_Wifi(wifi);
		g.setOptions_Roaming(roaming);
		g.setOptions_Changed(true);
		

		return true;
	}

	
	
	
}
