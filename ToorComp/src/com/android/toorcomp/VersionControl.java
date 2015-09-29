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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class VersionControl {

	Context c;
	private XMLParser mParser = new XMLParser();
	private String mPRGVersion;
	private String mDB_Version;
	private static final String TAG = "ERROR_VersionControl";
	

	
	// c is Like getApplicationContext() because VersionControl is not an activity

	public VersionControl(Context cont) {
		c = cont;
	  
	}

	public void checkVersion() {

		Globals.getInstance();
		
        /**
		* ----- Starts Async task to download version.xml
		* ----- and on post execute
		* ----- Starts new Async to read and compare versions with hard coded
		* ones
        **/
		
		AsyncTask<Void, Void, Boolean> dl = new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {

			}

			@Override
			protected Boolean doInBackground(Void... arg0) {
				try {
					dlFile();
					Thread.sleep(100);
				} catch (InterruptedException e) {

				}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					AsyncTask<Void, Void, Boolean> check = new AsyncTask<Void, Void, Boolean>() {

						@Override
						protected void onPreExecute() {

						}

						@Override
						protected Boolean doInBackground(Void... arg0) {
							try {
								compairVersion();
								Thread.sleep(100);
							} catch (InterruptedException e) {

							}
							return true;
						}

						@Override
						protected void onPostExecute(Boolean result) {

							if (result) {
								Globals g = Globals.getInstance();
								if (Globals.getPROGRAMVERSION().equals(
										mPRGVersion)) {
									g.setPRGVersionChanged(false);

								} else {
									g.setPRGVersionChanged(true);
									Log.d("PRG", mPRGVersion + " ");
									Log.d("PRG", Globals.getPROGRAMVERSION()
											+ " ");
								}

								if (Globals.getDBVERSION().equals(mDB_Version)) {
									g.setDBVersionchanged(false);
								} else {
									g.setDBVersionchanged(true);
									g.setNewDBVersion(mDB_Version);
									Log.d("DB", mDB_Version + " ");
									Log.d("DB", Globals.getDBVERSION() + " ");
								}
							

								//----------------------------------------
								
								Log.d("RESULT", result.toString());
								String message = "Υπάρχει νέα έκδοση ";
								
						

								if (g.isPRGVersionChanged()) {
									message = message.concat("της εφαρμογής ");
								}
                                
								/**
								* if app is to be downloaded no need to inform for
								* db
                                * TODO Clean all info Logs
                                **/
								
								if (g.isDBVersionchanged() && !g.isPRGVersionChanged()) {
									message = message.concat("της βάσης");
								}

								if (g.isPRGVersionChanged() || g.isDBVersionchanged()) {
									Log.d("MESSAGE", "I was Here");
									AlertDialog.Builder builder = new AlertDialog.Builder(c);
								
									
									builder.setMessage(message)
											.setCancelable(false)
											.setPositiveButton("OK",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int id) {
															dialog.dismiss();
														}
													});
									AlertDialog alert = builder.create();
									alert.show();
								
								
								//-----------------------------------------
								
								}
							}
						}
					};
					check.execute((Void[]) null);
				}
			}

		};
		dl.execute((Void[]) null);

	}

	
	
	public void compairVersion() {
		Globals g = Globals.getInstance();
		if (CheckFileExists(Environment.getExternalStorageDirectory()
				+ "/osmdroid/Version.xml")) {
			String XMLData;
			try {
				XMLData = getXmlFromFile("/osmdroid/Version.xml");

				BufferedReader br = new BufferedReader(
						new StringReader(XMLData));
				InputSource is = new InputSource(br);

				/************ Parse XML **************/

				// XMLParser mParser = new XMLParser();
				SAXParserFactory factory = SAXParserFactory.newInstance();

				SAXParser sp;
				sp = factory.newSAXParser();

				XMLReader reader = sp.getXMLReader();
				reader.setContentHandler(mParser);
				reader.parse(is);

				mPRGVersion = mParser.getProgramVersion();
				mDB_Version = mParser.getDBVersion();

				g.setXmLERROR(false);
				g.set_Xml_Download_Answer("NO");

			} catch (ParserConfigurationException e) {
				Log.d(TAG, e.toString());

			} catch (SAXException e) {
				Log.d(TAG, e.toString());

			} catch (IOException e) {
				Log.d(TAG, e.toString());

			}
			deleteFiles(Environment.getExternalStorageDirectory()
					+ "/osmdroid/Version.xml");
		}

	}

	private void dlFile() {
		Globals g = Globals.getInstance();
		String _url = "";

		// -- Open Json from raw and get db url

		String json = null;
		try {
			InputStream is = this.c.getResources().openRawResource(R.raw.kml);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			Log.d(TAG, "IOError");

		}
		JSONArray m_jArry = null;
		try {
			JSONObject obj = new JSONObject(json);
			m_jArry = obj.getJSONArray("kml_item");

			for (int i = 0; i < m_jArry.length(); i++) {
				JSONObject jo_inside = m_jArry.getJSONObject(i);

				if (jo_inside.getString("type").equals("VERSION")) {
					_url = m_jArry.getJSONObject(i).getString("link");
				}
			}
		} catch (JSONException e) {
			Log.d(TAG, e.toString());
		}

		// -- Parser End
		// -- Start Dl

		try {
			g.setWait("YES");
			URL url = new URL(_url);
			
			// create the new connection
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.connect();

			// set the path where we want to save the file
			// in this case, going to save it on the root directory of the
			// sd card.
			File SDCardRoot = Environment.getExternalStorageDirectory();
			// create a new file, specifying the path, and the filename
			// which we want to save the file as.

			
			File dirs = new File(SDCardRoot + "/osmdroid/");
			dirs.mkdirs();
			

			File file = new File(SDCardRoot + "/osmdroid/", "Version.xml");

			// this will be used to write the downloaded data into the file we
			// created
			FileOutputStream fileOutput = new FileOutputStream(file);

			// this will be used in reading the data from the internet
			InputStream inputStream = urlConnection.getInputStream();

			// create a buffer...
			byte[] buffer = new byte[1024];
			int bufferLength = 0; // used to store a temporary size of the
									// buffer

			// now, read through the input buffer and write the contents to the
			// file
			while ((bufferLength = inputStream.read(buffer)) > 0) {
				// add the data in the buffer to the file in the file output
				// stream (the file on the sd card
				fileOutput.write(buffer, 0, bufferLength);
					

			}
			// close the output stream when done
			fileOutput.close();
			g.setWait("NO");
			// catch some possible errors...
	       
			// @ TODO Fix catch statements
		
		} catch (MalformedURLException e) {
			Log.d(TAG, "URL Error");
			Globals.getInstance().setWait("URLERROR");

		} catch (IOException e) {
			Log.d(TAG, "IO Error" + e.toString());
			Globals.getInstance().setWait(e.getLocalizedMessage());// "IOERROR");

		}

		g.setWait("NO");

	}

	public static void deleteFiles(String path) {

		File file = new File(path);

		if (file.exists()) {
			String deleteCmd = "rm -r " + path;
			Runtime runtime = Runtime.getRuntime();
			try {
				
				runtime.exec(deleteCmd);
				
			} catch (IOException e) {
				Log.d (TAG, e.toString());
			}
		}
	}

	private Boolean CheckFileExists(String path) {

		File file = new File(path);
		return file.exists();
	}

	public String getXmlFromFile(String filename) throws IOException {
		StringBuffer buff = new StringBuffer();
		File root = Environment.getExternalStorageDirectory();
		File xml = new File(root, filename);

		BufferedReader reader = new BufferedReader(new FileReader(xml));
		String line = null;
		while ((line = reader.readLine()) != null) {
			buff.append(line).append("\n");
		}
		reader.close();
		return buff.toString();
	}

}