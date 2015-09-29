package com.android.toorcomp.downloadservice;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.toorcomp.Base_Activity;
import com.android.toorcomp.Globals;
import com.android.toorcomp.R;

public class Downloader extends Base_Activity implements
		DownloadResultReceiver.Receiver {

	private DownloadResultReceiver mReceiver;
	@SuppressWarnings("rawtypes")
	private ArrayAdapter arrayAdapter;
	private ListView listView;
	private String URL;
	private String DIR;
	private String FNAME;
	

	
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.listview);
		listView = (ListView) findViewById(R.id.listView);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String command = bundle.getString("DL");

		if (command.equals("KML")) {

			// ---------------------------------------------------------

			ArrayList<String> listItems = new ArrayList<String>();
			ArrayAdapter<String> arrayAdapter;

			arrayAdapter = new ArrayAdapter<String>(Downloader.this,
					R.layout.list_text_view, listItems);

			listView.setAdapter(arrayAdapter);

			String json = null;
			try {
				InputStream is = this.getResources().openRawResource(R.raw.kml);
				int size = is.available();
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();
				json = new String(buffer, "UTF-8");
			} catch (IOException ex) {
				Log.d("TAG", "IOError");

			}
			JSONArray m_jArry = null;
			try {
				JSONObject obj = new JSONObject(json);
				m_jArry = obj.getJSONArray("kml_item");

				for (int i = 0; i < m_jArry.length(); i++) {
					
					JSONObject jo_inside = m_jArry.getJSONObject(i);
					if (jo_inside.getString("type").contains("KML_")) {
						String Item = jo_inside.getString("type") + "\n\n"
								+ jo_inside.getString("name") + "\n\n"
								+ jo_inside.getString("description") + "\n\n"
								+ jo_inside.getString("link");
						listItems.add(Item);

						arrayAdapter.notifyDataSetChanged();
						final JSONArray J_array = m_jArry;
						listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							public void onItemClick(AdapterView<?> av,
									View view, int i, long l) {
								try {
									URL = J_array.getJSONObject(i).getString(
											"link");
									DIR = "/osmdroid/kml/";
									FNAME = J_array.getJSONObject(i).getString(
											"name");
									init_dl();

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						});
					}
				}
			} catch (JSONException e) {
				Log.d("TAG", e.toString());
			}

			setProgressBarIndeterminateVisibility(false);
			// finish();

			// ---------------------------------------------------------

		}

		if (command.equals("POI")) {

			String json = null;
			try {
				InputStream is = this.getResources().openRawResource(R.raw.kml);
				int size = is.available();
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();
				json = new String(buffer, "UTF-8");
			} catch (IOException ex) {
				Log.d("TAG", "IOError");

			}
			JSONArray m_jArry = null;
			try {
				JSONObject obj = new JSONObject(json);
				m_jArry = obj.getJSONArray("kml_item");

				for (int i = 0; i < m_jArry.length(); i++) {
					JSONObject jo_inside = m_jArry.getJSONObject(i);

					if (jo_inside.getString("type").equals("POI")) {
						URL = m_jArry.getJSONObject(i).getString("link");
						DIR = m_jArry.getJSONObject(i).getString("description");
						FNAME = m_jArry.getJSONObject(i).getString("name");
						boolean isReady = init_dl();
						if (isReady)
							Toast.makeText(
									this,
									"Έγινε εκκίνηση λήψης της Βάσης Σημείων ενδιαφέροντος",
									Toast.LENGTH_SHORT).show();

						// ------ Now we have to set new DBVersion at prefs

						if (Globals.getInstance().isDBVersionchanged()) {
							SharedPreferences sharedPref = getSharedPreferences(
									MY_PREFS_NAME, Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = sharedPref.edit();
							editor.putString("DBVERSION", Globals.getInstance()
									.getNewDBVersion());
							editor.commit();
							Globals.getInstance();
							Globals.setDBVERSION(Globals.getInstance()
									.getNewDBVersion());
						}

						finish();

					}

				}
			} catch (JSONException e) {
				Log.d("TAG", e.toString());
			}

		}

		if (command.equals("MAP")) {

			String json = null;
			try {
				InputStream is = this.getResources().openRawResource(R.raw.kml);
				int size = is.available();
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();
				json = new String(buffer, "UTF-8");
			} catch (IOException ex) {
				Log.d("TAG", "IOError");

			}
			JSONArray m_jArry = null;
			try {
				JSONObject obj = new JSONObject(json);
				m_jArry = obj.getJSONArray("kml_item");

				for (int i = 0; i < m_jArry.length(); i++) {
					JSONObject jo_inside = m_jArry.getJSONObject(i);

					if (jo_inside.getString("type").equals("MAP")) {
						URL = m_jArry.getJSONObject(i).getString("link");
						DIR = m_jArry.getJSONObject(i).getString("description");
						FNAME = m_jArry.getJSONObject(i).getString("name");

						boolean isReady = init_dl();
						if (isReady)
							Toast.makeText(this, "Έγινε εκκίνηση λήψης Χάρτη",
									Toast.LENGTH_SHORT).show();

						finish();

					}

				}
			} catch (JSONException e) {
				Log.d("TAG", e.toString());
			}

		}

	}

	private boolean init_dl() {

		if (!isNetworkAvailable()) {
			Toast.makeText(this, "Internet not Available", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
	
		mReceiver = new DownloadResultReceiver(new Handler());
		mReceiver.setReceiver(this);
		
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				DownloadService.class);

		Globals.getInstance().setDownloadID(Globals.getInstance().getDownloadID()+1);
		intent.putExtra("url", URL);
		intent.putExtra("receiver", mReceiver);
		intent.putExtra("requestId",Globals.getInstance().getDownloadID() );
		intent.putExtra("Directory", DIR);
		intent.putExtra("F_name", FNAME);
		startService(intent);

		return true;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		switch (resultCode) {
		case DownloadService.STATUS_RUNNING:
			setProgressBarIndeterminateVisibility(true);
			break;
		case DownloadService.STATUS_FINISHED:
			/* Hide progress & extract result from bundle */
			setProgressBarIndeterminateVisibility(false);
			String[] results = resultData.getStringArray("result");

			/* Update ListView with result */
			arrayAdapter = new ArrayAdapter(Downloader.this,
					android.R.layout.simple_list_item_1, results);
			listView.setAdapter(arrayAdapter);
			break;
		case DownloadService.STATUS_ERROR:
			/* Handle the error */
			String error = resultData.getString(Intent.EXTRA_TEXT);
			Toast.makeText(this, error, Toast.LENGTH_LONG).show();
			break;
		}
	}

}
