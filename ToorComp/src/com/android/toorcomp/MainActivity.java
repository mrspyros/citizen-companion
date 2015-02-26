package com.android.toorcomp;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

/**
 * @author mrspyros
 * 
 */
public class MainActivity extends Activity {

	private boolean download = false;
	private ProgressDialog pd;
	private Context context;

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
							//dialog.dismiss();
						
							AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
								
								@Override
								protected void onPreExecute() {
								
									try {
										pd = new ProgressDialog(MainActivity.this);
										pd.setTitle("Processing...");
										pd.setMessage("Please wait.");
										pd.setCancelable(false);
										pd.setIndeterminate(true);
										pd.show();
									} catch (Exception e) {
										Toast.makeText(MainActivity.this,"Error= "+e,
												Toast.LENGTH_LONG).show();
										
										// TODO Auto-generated catch block
										//e.printStackTrace();
									}
								
								}
									
								@Override
								protected Void doInBackground(Void... arg0) {
									try {
										download = init.DownloadXML();
										Thread.sleep(5000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									return null;
								}
								
								@Override
								protected void onPostExecute(Void result) {
																	
									if (pd!=null) {
									pd.dismiss();
									//	b.setEnabled(true);
									}
								}
									
							};
							try {
								Void str_result= task.execute().get();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//task.execute((Void[])null);
							
						
							
						//	download = init.DownloadXML();

							if (!download) {
								Globals g = Globals.getInstance();
								Toast.makeText(getApplicationContext(),
										R.string.noxmldl + g.getWait(),
										Toast.LENGTH_LONG).show();
								g.setXmLERROR("YES");
								dialog.dismiss();
								finish();
								startActivity(intent);
							} else {
								Globals g = Globals.getInstance();
								Toast.makeText(getApplicationContext(),
										R.string.noxmldlok, Toast.LENGTH_LONG)
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