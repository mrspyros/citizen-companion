package com.android.toorcomp;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class MyLocationListener implements LocationListener{
    private Context cnt;
    private  ProgressDialog d;

    
public MyLocationListener(Context cnt){
    this.cnt = cnt;
    
    d = ProgressDialog.show(cnt, "", "Finding your location...");
    if (this.d.isShowing())
      d.setCancelable(true);
}

@Override
public void onLocationChanged(Location loc){

loc.getLatitude();
loc.getLongitude();

if (d.isShowing())
    d.dismiss();

//String Text = "My current location is: " +"Latitud = " + loc.getLatitude() +"Longitud = " + loc.getLongitude();
//Toast.makeText(cnt,Text,Toast.LENGTH_SHORT).show();
}


@Override

public void onProviderDisabled(String provider) {

//Toast.makeText(getApplicationContext(),"Gps Disabled",Toast.LENGTH_SHORT ).show();

}


@Override

public void onProviderEnabled(String provider) {

//Toast.makeText(getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();

}





@Override
public void onStatusChanged(String provider, int status, Bundle extras) {
	// TODO Auto-generated method stub
	
}

}/* End of Class MyLocationListener */