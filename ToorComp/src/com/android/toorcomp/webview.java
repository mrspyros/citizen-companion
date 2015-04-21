package com.android.toorcomp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class webview extends Base_Activity {

	// ----- Simple as it gets
	// ----- We get a link from globals
	// ----- And show it

	private WebView webView;

	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().show();
		setContentView(R.layout.webview);

		Globals g = Globals.getInstance();

		webView = (WebView) findViewById(R.id.webView1);
		webView.setWebViewClient(new WebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(g.getWebViewUrl());
		g.setWebViewUrl("");

	}

	public void onResume() {
		super.onResume();
	}

	

}