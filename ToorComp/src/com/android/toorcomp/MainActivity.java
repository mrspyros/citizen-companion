package com.android.toorcomp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author mrspyros
 * 
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		
		//------ Check ic XML Exists and notify globals
		
		final Intent intent = new Intent(getApplicationContext(),
				MainScreen.class);
		final Startup init = new Startup(this);
		Globals g = Globals.getInstance();
		Boolean XMLExists = init.CheckXML();

		if (!XMLExists) {
			g.setXmLERROR(true);
			g.set_Xml_Download_Answer("YES");
		} else
			g.setXmLERROR(false);
		finish();
		startActivity(intent);

	}

}