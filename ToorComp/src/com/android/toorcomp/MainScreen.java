package com.android.toorcomp;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

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
				// Intent intent =new Intent(getApplicationContext(),
				// Pois.class);//Pois.class);
				Intent intent = new Intent(getApplicationContext(),
						webview.class);
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

		Button test = (Button) findViewById(R.id.test);
		test.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
		    final Dialog myDialog = new Dialog(MainScreen.this);
	        myDialog.setContentView(R.layout.custom_dialog);
	      //  myDialog.setTitle("hello");
	        
	       

	        myDialog.setCancelable(false);

	        TextView text = (TextView) myDialog.findViewById(R.id.dialog);
	       // text.setMovementMethod(ScrollingMovementMethod.getInstance());
	        text.setText("text");

	        Button createAccount= (Button) myDialog.findViewById(R.id.dialogcancel);
	        createAccount.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {

	                myDialog.dismiss();
	            }
	        });

	        Button test = (Button) myDialog.findViewById(R.id.morebtn);
	        test.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {

	            	Intent intent = new Intent(getApplicationContext(), webview.class);
					startActivity(intent);
                    myDialog.dismiss();

	            }
	        });

	        


	        myDialog.show();

	    }

		
		});

	}
}


