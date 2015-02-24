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

	public MyOwnItemizedOverlay(final Context context,
			final List<OverlayItem> aList) {
		super(context, aList, new OnItemGestureListener<OverlayItem>() {
			@Override
			public boolean onItemSingleTapUp(final int index,
					final OverlayItem item) {
				return false;
			}

			@Override
			public boolean onItemLongPress(final int index,
					final OverlayItem item) {
				return false;
			}
		});
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	protected boolean onSingleTapUpHelper(final int index,
			final OverlayItem item, final MapView mapView) {
		// Toast.makeText(mContext, "Item " + index + " has been tapped!",
		// Toast.LENGTH_SHORT).show();
		
		final Dialog myDialog = new Dialog(mContext,
				android.R.style.Theme_Translucent);
		myDialog.setContentView(R.layout.custom_dialog);
		// myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		myDialog.setTitle(item.getTitle());
		myDialog.setCancelable(false);
		myDialog.setCanceledOnTouchOutside(true);

		TextView text = (TextView) myDialog.findViewById(R.id.dialog);
		text.setText(getSubfromString(item.getSnippet(), 1));

		Button dismiss = (Button) myDialog.findViewById(R.id.dialogcancel);
		dismiss.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				myDialog.dismiss();
			}
		});

		// ------------ so what we do here
		// ------------ If there is a link in String
		// ------------ We get the link
		// ------------ We put it in globals
		// ------------ And we start a webview intent to show the web page
		// ------------ Else if there is no link
		// ------------ We hide the more button

		final String _link = getSubfromString(item.getSnippet(), 2);
		if (_link.length() != 1) {

			Button test = (Button) myDialog.findViewById(R.id.morebtn);
			test.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					Globals g = Globals.getInstance();
					g.setWebViewUrl(_link.substring(_link.indexOf("#") + 1));
					Intent intent = new Intent(mContext, webview.class);
					mContext.startActivity(intent);
					myDialog.dismiss();

				}
			});

		} else {
			Button test = (Button) myDialog.findViewById(R.id.morebtn);
			test.setVisibility(View.GONE);
		}

		myDialog.show();

		return true;
	}

	protected boolean onItemLongPressHelper(final int index,
			final OverlayItem item, final MapView mapView) {
	
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}

	// If selection = 1 returns description
	// if selection = 2 returns link if exists
	// !!!!! Returns link with "#" at beginning
	// If we want to use link we have to remove it

	private String getSubfromString(String item, int selection) {
		String str = "";
		if (selection == 1) {
			return item.substring(0, item.indexOf("#"));
		} else if (selection == 2) {
			return item.substring(item.indexOf("#"));
		} else
			return str;
	}

}
