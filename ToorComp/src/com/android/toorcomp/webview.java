package com.android.toorcomp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
 
public class webview extends Activity {
 
	// ---- fLag is used to destroy container webview activity
	// ---- when we return from inner webview
	
	private boolean fLag;
	private WebView webView;
 
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
 
		webView = (WebView) findViewById(R.id.webView1);
		webView.setWebViewClient(new WebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("http://www.google.com");
        //fLag=true;
				
	}
 
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	  if (fLag) finish();
	}
	
}