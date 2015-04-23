package com.android.toorcomp;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import org.osmdroid.views.Projection;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.widget.Toast;
 
public class DummyOverlay extends org.osmdroid.views.overlay.Overlay {

	private IGeoPoint Geo=null; 
	
        public DummyOverlay(Context ctx) {
            super(ctx); // TODO Auto-generated constructor stub
        }

        @Override
        protected void draw(Canvas c, MapView osmv, boolean shadow) {}

        public boolean onTap(MotionEvent e, MapView m_mapView) {
        	Projection proj = m_mapView.getProjection();
            IGeoPoint loc = proj.fromPixels((int)e.getX(), (int)e.getY()); 
            Geo=loc;
            Globals.getInstance().setSelected(loc);
        	
        	return true;
        }
        
        public boolean onDoubleTap(MotionEvent e, MapView m_mapView) {
            // This stops the 'jump to, and zoom in' of the default behaviour
            //int zoomLevel = mapView.getZoomLevel();
            //mMapController.setZoom(zoomLevel + 3);
        	 org.osmdroid.views.Projection proj = m_mapView.getProjection();
	            IGeoPoint loc = proj.fromPixels((int)e.getX(), (int)e.getY()); 
	            Geo=loc;
	            Globals.getInstance().setSelected(loc);
	            //String longitude = Double.toString(((double)loc.getLongitudeE6())/1000000);
	            //String latitude = Double.toString(((double)loc.getLatitudeE6())/1000000);
            return true;// This stops the double tap being passed on to the mapview
        }
    }	
	
	
	

