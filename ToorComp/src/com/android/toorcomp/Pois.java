package com.android.toorcomp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;


public class Pois extends Activity  {

	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pois);

  
        Button mapbtn = (Button) findViewById(R.id.mapBtn);
	    mapbtn.setOnClickListener(new android.view.View.OnClickListener(){
	    
	    public void onClick(View v) {
	    	Globals.getInstance().setFirstTimeOnMapActivity(true);
	        Globals.getInstance().setMapZoomLevel(16);
	    	Intent intent =new Intent(getApplicationContext(), Map.class);
	 	    startActivity(intent);
	    }});
	    
        
        Button btn1 = (Button) findViewById(R.id.btn1);
	    btn1.setOnClickListener(new android.view.View.OnClickListener(){
	    
	    public void onClick(View v) {
	    	finish();
	    }});

    }
	
	
}
