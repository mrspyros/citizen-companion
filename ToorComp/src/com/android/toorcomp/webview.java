package com.android.toorcomp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
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
	
}