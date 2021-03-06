package com.android.toorcomp;

/**
 * @author MrSpyros
 *
 *  This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   any later version.

 *  This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 
 *	Simple as it gets
 *	We get a link from globals
 *  And show it
 *
 */

public class webview extends Base_Activity {

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