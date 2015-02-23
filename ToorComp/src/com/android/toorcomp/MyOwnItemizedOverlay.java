package com.android.toorcomp;

import java.util.List;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



	 public class MyOwnItemizedOverlay extends ItemizedIconOverlay<OverlayItem> {
		    protected Context mContext;

		    public MyOwnItemizedOverlay(final Context context, final List<OverlayItem> aList) {
		         super(context, aList, new OnItemGestureListener<OverlayItem>() {
		                @Override public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
		                        return false;
		                }
		                @Override public boolean onItemLongPress(final int index, final OverlayItem item) {
		                        return false;
		                }
		              } );
		        // TODO Auto-generated constructor stub
		         mContext = context;
		    }

		    @Override 
		    protected boolean onSingleTapUpHelper(final int index, final OverlayItem item, final MapView mapView) {
		        //Toast.makeText(mContext, "Item " + index + " has been tapped!", Toast.LENGTH_SHORT).show();
		        /*AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		        dialog.setTitle(item.getTitle());
		        dialog.setMessage(item.getSnippet());
		        dialog.show();*/
		       
		    	
		    	final Dialog myDialog = new Dialog(mContext,android.R.style.Theme_Translucent);
		       	myDialog.setContentView(R.layout.custom_dialog);
		        //myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		        
		       	
		       	myDialog.setTitle(item.getTitle());
		        myDialog.setCancelable(false);
		        myDialog.setCanceledOnTouchOutside(true);

		        TextView text = (TextView) myDialog.findViewById(R.id.dialog);
		       // text.setMovementMethod(ScrollingMovementMethod.getInstance());
		        text.setText(item.getSnippet());

		        Button dismiss= (Button) myDialog.findViewById(R.id.dialogcancel);
		        dismiss.setOnClickListener(new OnClickListener() {
		            public void onClick(View v) {

		                myDialog.dismiss();
		            }
		        });

		        
		        Button test = (Button) myDialog.findViewById(R.id.morebtn);
		        test.setOnClickListener(new OnClickListener() {
		            public void onClick(View v) {
/*
		            	Intent intent = new Intent(getApplicationContext(), webview.class);
						startActivity(intent);*/
	                    myDialog.dismiss();

		            }
		        });

		        


		        myDialog.show();
		    	
		    	
		    	
		    	
		    	return true;
		    }
		
		    protected boolean onItemLongPressHelper(final int index, final OverlayItem item, final MapView mapView) {
		        //Toast.makeText(mContext, "Item " + index + " has been tapped!", Toast.LENGTH_SHORT).show();
		        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		        dialog.setTitle(item.getTitle());
		        dialog.setMessage(item.getSnippet());
		        dialog.show();
		        return true;
		    }
		
	 
	 }
	

