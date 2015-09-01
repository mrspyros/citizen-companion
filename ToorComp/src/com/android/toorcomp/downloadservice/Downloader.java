package com.android.toorcomp.downloadservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.toorcomp.Base_Activity;
import com.android.toorcomp.R;
import com.android.toorcomp.downloadservice.DownloadResultReceiver;
import com.android.toorcomp.downloadservice.DownloadService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Downloader extends Base_Activity implements
		DownloadResultReceiver.Receiver {

	private DownloadResultReceiver mReceiver;
	private ArrayAdapter arrayAdapter;

	private String url = "https://drive.google.com/uc?export=download&id=0B2s4PStL3vizdEtGcjNDMUFqUEU";
	private ListView listView;
	private String URL;
	private String DIR;
	private String FNAME;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.listview);
		listView = (ListView) findViewById(R.id.listView);
		
		// ---------------------------------------------------------

		ArrayList<String> listItems = new ArrayList<String>();
		ArrayAdapter<String> arrayAdapter;
		//arrayAdapter = new ArrayAdapter<String>(Downloader.this,
		//		android.R.layout.simple_list_item_1, listItems);
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

				String Item =  jo_inside.getString("type") + "\n\n"
						+ jo_inside.getString("name") + "\n\n"
						+ jo_inside.getString("description") + "\n\n"
						+ jo_inside.getString("link");
				listItems.add(Item);
				
								
				arrayAdapter.notifyDataSetChanged();
                final JSONArray J_array = m_jArry;
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> av, View view,
							int i, long l) {
							try {
								URL = J_array.getJSONObject(i).getString("link");
							    DIR= "/osmdroid/kml/";
							    FNAME =J_array.getJSONObject(i).getString("name");
								init_dl();
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						
					}
				});

			}
		} catch (JSONException e) {
			Log.d("TAG", e.toString());
		}

		setProgressBarIndeterminateVisibility(false);

		/*
		 * JSONObject jsonCountry = countries.getJSONObject(i); CountryVO
		 * country = new CountryVO();
		 * country.setCountryName(jsonCountry.getString("country")); String co =
		 * jsonCountry.getString("countryCode"); country.setCountryCode(co); try
		 * { Class<?> drawableClass = com.example.R.drawable.class; // replace
		 * package Field drawableField = drawableClass.getField(co); int
		 * drawableId = (Integer)drawableField.get(null); Drawable drawable =
		 * getResources().getDrawable(drawableId);
		 * country.setCountryFlag(drawable); } catch (Exception e) { // report
		 * exception } countries.add(country);
		 */

		// ---------------------------------------------------------

		/*mReceiver = new DownloadResultReceiver(new Handler());
		mReceiver.setReceiver(this);
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				DownloadService.class);

		intent.putExtra("url", url);
		intent.putExtra("receiver", mReceiver);
		intent.putExtra("requestId", 101);
		intent.putExtra("Directory", "/osmdroid/");
		intent.putExtra("F_name", "Options.xml");
		startService(intent);*/

	}

	public void init_dl (){
		
		mReceiver = new DownloadResultReceiver(new Handler());
		mReceiver.setReceiver(this);
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				DownloadService.class);

		intent.putExtra("url", URL);
		intent.putExtra("receiver", mReceiver);
		intent.putExtra("requestId", 101);
		intent.putExtra("Directory", DIR);
		intent.putExtra("F_name", FNAME);
		startService(intent);
		
		
	}
	
	
	
	@SuppressWarnings("unchecked")
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

			// String results = resultData.getString("Downloaded", "Error");

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
