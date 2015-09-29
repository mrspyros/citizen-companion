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

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreen extends Base_Activity {

	Globals g = Globals.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().show();

		setContentView(R.layout.activity_main);

		File M_PATH = new File(Environment.getExternalStorageDirectory(),
				"osmdroid");
		String N_PATH = M_PATH.getAbsolutePath();

		TextView text = (TextView) findViewById(R.id.main_opt_text_2);
		text.setText(N_PATH);

		Button btn1 = (Button) findViewById(R.id.main_btn1);
		btn1.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				finish();
				System.exit(0);
			}
		});

		Button btn2 = (Button) findViewById(R.id.main_btn2);
		// btn2.setVisibility(View.GONE);

		btn2.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						MainDownloader.class);
				startActivity(intent);

			}
		});

		Button btn3 = (Button) findViewById(R.id.main_btn3);
		btn3.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				Globals.getInstance().setFirstTimeOnMapActivity(true);
				Globals.getInstance().setMapZoomLevel(16);
				Intent intent = new Intent(getApplicationContext(),
						KMLMap.class);
				startActivity(intent);
			}
		});

		Button btn4 = (Button) findViewById(R.id.main_btn4);
		btn4.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						New_request.class);
				startActivity(intent);
			}
		});

		Button stream = (Button) findViewById(R.id.stream_btn);
		stream.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				// Intent intent = new Intent(getApplicationContext(),
				// StreamPlayer.class);
				Intent intent = new Intent(getApplicationContext(),
						MainDownloader.class);
				startActivity(intent);
			}
		});

		Button qRcode = (Button) findViewById(R.id.qrcode);
		qRcode.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {

				/**
				 * 
				 *   https://github.com/zxing/zxing
				 *   ZXing ("zebra crossing") is an open-source, 
				 *   multi-format 1D/2D barcode image processing library 
				 *   implemented in Java, with ports to other languages.
				 *   
				 *   Distributed under
				 *         Apache License
                           Version 2.0, January 2004
				 */
				
				try {

					String packageString = "com.android.toorcomp";
					Intent intent = new Intent(
							"com.google.zxing.client.android.SCAN");
					intent.setPackage(packageString);
					// --- comment this for all types of code reading
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
					startActivityForResult(intent, 0);

				} catch (Exception e) {

					Toast.makeText(getApplicationContext(), e.toString(),
							Toast.LENGTH_LONG).show();

				}

			}

		});

		if (isNetworkAvailable()) {
			// --- Check db and program version and notify if new exists
			final VersionControl init = new VersionControl(this);
			init.checkVersion();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {

			if (resultCode == RESULT_OK) {
				Globals g = Globals.getInstance();
				g.setWebViewUrl(data.getStringExtra("SCAN_RESULT"));
				Intent intent = new Intent(getApplicationContext(),
						webview.class);
				startActivity(intent);
			}
			if (resultCode == RESULT_CANCELED) {
				// handle cancel
			}
		}
	}

}