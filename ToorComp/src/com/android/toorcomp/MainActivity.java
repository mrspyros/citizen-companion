package com.android.toorcomp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;

/**
 * @author mrspyros
 * 
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final Startup init = new Startup(this);

		Boolean XMLExists = init.CheckXML();

		final Intent intent = new Intent(getApplicationContext(),
				MainScreen.class);

		if (!XMLExists) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setTitle(R.string.noxmltitle);
			builder.setMessage(R.string.noxmlmessage);

			builder.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							// Code that is executed when clicking YES
							
							Boolean download = init.DownloadXML();
														
							if (!download) {
								Globals g = Globals.getInstance();
								Toast.makeText(getApplicationContext(),
										R.string.noxmldl+g.getWait() ,
										Toast.LENGTH_LONG)
										.show();
								g.setXmLERROR("YES");
								dialog.dismiss();
								finish();
								startActivity(intent);
							} else {
								Globals g = Globals.getInstance();
								Toast.makeText(getApplicationContext(),
										R.string.noxmldlok, 
										Toast.LENGTH_LONG)
										.show();
								g.setXmLERROR("NO");
								dialog.dismiss();
								finish();
								startActivity(intent);

							}

						}

					});

			builder.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							// Code that is executed when clicking NO
							Globals g = Globals.getInstance();
							g.setXmLERROR("YES");
							dialog.dismiss();
							finish();
							startActivity(intent);

						}

					});

			AlertDialog alert = builder.create();
			alert.show();

		}

		else {
			Globals g = Globals.getInstance();
			g.setXmLERROR("NO");
			finish();
			startActivity(intent);
		}

	}

}