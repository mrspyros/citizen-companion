package com.android.toorcomp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webview extends Activity {

	// ----- Simple as it gets
	// ----- We get a link from globals
	// ----- And show it

	private WebView webView;

	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// creates a menu inflater
		MenuInflater inflater = getMenuInflater();
		// generates a Menu from a menu resource file
		// R.menu.main_menu represents the ID of the XML resource file
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.opt:

			startActivity(new Intent(getApplicationContext(), Options.class));

			return true;

		}
		return false;
	}

}