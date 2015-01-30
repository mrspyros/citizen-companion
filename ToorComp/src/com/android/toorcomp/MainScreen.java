package com.android.toorcomp;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreen extends Activity  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Globals g = Globals.getInstance();
		String ERROR=Globals.getInstance().getXmLERROR();
		
		
		
		
		//if (ERROR=="YES") Toast.makeText(getApplicationContext(), "Δέν βρέθηκαν αποθηκευμένα σημεία ενδιαφέροντος", Toast.LENGTH_LONG).show(); 
		
		
		
		//Toast.makeText(getApplicationContext(),"message="+ ERROR, Toast.LENGTH_LONG).show(); 
		//}
		
	
		File M_PATH = new File(Environment.getExternalStorageDirectory(),"osmdroid");	
		String N_PATH = M_PATH.getAbsolutePath();
		
		TextView text = (TextView) findViewById(R.id.textView1);
		text.setText(N_PATH);
		
		Button btn1 = (Button) findViewById(R.id.btn1);
	    btn1.setOnClickListener(new android.view.View.OnClickListener(){
	    public void onClick(View v) {
	    	finish();
            System.exit(0);
	    }});
	    
	    
	    Button btn2 = (Button) findViewById(R.id.btn2);
	    btn2.setOnClickListener(new android.view.View.OnClickListener(){
	    public void onClick(View v) {
	     	 Intent intent =new Intent(getApplicationContext(), Pois.class);//Pois.class);
	    	
	    	startActivity(intent);
	    }});
	    
	    
	    Button btn3 = (Button) findViewById(R.id.btn3);
	    btn3.setOnClickListener(new android.view.View.OnClickListener(){
 	    public void onClick(View v) {
	     	 Globals.getInstance().setFirstTimeOnMapActivity(true);
 	    	 Globals.getInstance().setMapZoomLevel(16);
 	    	 Intent intent =new Intent(getApplicationContext(), Map.class);
	 	     startActivity(intent);
	    }});
	   
	    Button btn4 = (Button) findViewById(R.id.btn4);
	    btn4.setOnClickListener(new android.view.View.OnClickListener(){
	    public void onClick(View v) {
	     	 Intent intent =new Intent(getApplicationContext(), New_request.class);
	 	     startActivity(intent);
	    }});
	    
	    
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
		
   }
}
