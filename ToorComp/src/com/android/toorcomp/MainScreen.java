package com.android.toorcomp;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Globals g = Globals.getInstance();
		String ERROR = Globals.getInstance().getXmLERROR();

		// if (ERROR=="YES") Toast.makeText(getApplicationContext(),
		// "Δέν βρέθηκαν αποθηκευμένα σημεία ενδιαφέροντος",
		// Toast.LENGTH_LONG).show();

		// Toast.makeText(getApplicationContext(),"message="+ ERROR,
		// Toast.LENGTH_LONG).show();
		// }

		File M_PATH = new File(Environment.getExternalStorageDirectory(),
				"osmdroid");
		String N_PATH = M_PATH.getAbsolutePath();

		TextView text = (TextView) findViewById(R.id.textView1);
		text.setText(N_PATH);

		Button btn1 = (Button) findViewById(R.id.btn1);
		btn1.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				finish();
				System.exit(0);
			}
		});

		Button btn2 = (Button) findViewById(R.id.btn2);
		btn2.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				 Intent intent =new Intent(getApplicationContext(),
				 Pois.class);//Pois.class);
				//Intent intent = new Intent(getApplicationContext(),
				//		webview.class);
				startActivity(intent);
			}
		});

		Button btn3 = (Button) findViewById(R.id.btn3);
		btn3.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				Globals.getInstance().setFirstTimeOnMapActivity(true);
				Globals.getInstance().setMapZoomLevel(16);
				Intent intent = new Intent(getApplicationContext(), Map.class);
				startActivity(intent);
			}
		});

		Button btn4 = (Button) findViewById(R.id.btn4);
		btn4.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						New_request.class);
				startActivity(intent);
			}
		});

		Button qRcode = (Button) findViewById(R.id.test);
		qRcode.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
		  
				try {

					String packageString = "com.android.toorcomp";
					Intent intent = new Intent("com.google.zxing.client.android.SCAN");
					intent.setPackage(packageString);
					intent.putExtra("SCAN_MODE", "SCAN_MODE");
					startActivityForResult(intent, 0);
					
					
					/*
					Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
	                startActivityForResult(intent, 0);
	            */

				} catch (Exception e) {

					Toast.makeText(getApplicationContext(),
							e.toString(),
							 Toast.LENGTH_LONG).show();

				}
				
	        }

		
		});

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {           
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == 0) {

	        if (resultCode == RESULT_OK) {
	        	Globals g = Globals.getInstance();
	        	g.setWebViewUrl(data.getStringExtra("SCAN_RESULT"));
	        	Intent intent = new Intent(getApplicationContext(), webview.class);
				startActivity(intent);
	        }
	        if(resultCode == RESULT_CANCELED){
	            //handle cancel
	        }
	    }
	}
	
	
	
	
	
}


