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

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.android.toorcomp.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

/**
 *
 * @PARAM 'url' the download url 
 * @PARAM 'Directory' the directory to download To 
 * @PARAM 'F_name' the filename to save file
 * 
 * @return STATUS_RUNNING/FINISHED/ERROR
 * 
 */

public class DownloadService extends IntentService {

	public static final int STATUS_RUNNING = 0;
	public static final int STATUS_FINISHED = 1;
	public static final int STATUS_ERROR = 2;

	NotificationCompat.Builder mBuilder;
	private static final String TAG = "DownloadService";
	private NotificationManager mNotificationManager;
	private String mDirName;
	private String mFileName;
	protected final NetworkChangeReceiver mConnReceiver = new NetworkChangeReceiver();

	protected void registerReceivers() {
		registerReceiver(mConnReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
		isReceiverRegistered = true;
	}

	protected void unRegisterReceivers() {
		unregisterReceiver(mConnReceiver);
		isReceiverRegistered = false;
	}

	protected boolean isReceiverRegistered;

	public DownloadService() {
		super(DownloadService.class.getName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Log.d(TAG, "Service Started!");

		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		String url = intent.getStringExtra("url");
		mDirName = intent.getStringExtra("Directory");
		mFileName = intent.getStringExtra("F_name");

		Bundle bundle = new Bundle();

		if (!TextUtils.isEmpty(url)) {
			/* Update UI: Download Service is Running */
			receiver.send(STATUS_RUNNING, Bundle.EMPTY);
			Log.d("URL", url.toString());
			try {
				// String[] results = downloadData(url);
				boolean ok = FileDownload(url);
				/* Sending result back to activity */
				// if (null != results && results.length > 0) {
				if (ok) {
					// bundle.putStringArray("result", results);

					String[] res = new String[] { "Downloaded", url };
					bundle.putStringArray("Result", res);

					receiver.send(STATUS_FINISHED, bundle);
				}
			} catch (Exception e) {

				/* Sending error message back to activity */
				bundle.putString(Intent.EXTRA_TEXT, e.toString());
				receiver.send(STATUS_ERROR, bundle);
			}
		}
		Log.d(TAG, "Service Stopping!");
		this.stopSelf();
	}

	private boolean FileDownload(String _url) throws DownloadException,
			IOException {

		registerReceivers();

		FileOutputStream fileOutput = null;

		Globals g = Globals.getInstance();
		g.setWait("YES");

		// ---------------Notification-------------------

		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) getSystemService(ns);

		int icon = R.drawable.ic_action_download;

		Context context = getApplicationContext();
		CharSequence contentTitle = getResources().getText(
				com.android.toorcomp.R.string.app_name);
		CharSequence contentText = getResources().getText(
				com.android.toorcomp.R.string.loading);
		Intent notificationIntent = new Intent(context, Downloader.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		mBuilder = new NotificationCompat.Builder(getApplicationContext())
				.setSmallIcon(R.drawable.ic_action_download).setSmallIcon(icon)
				.setContentTitle(contentTitle).setContentText(contentText)
				.setContentIntent(contentIntent);

		mBuilder.setProgress(100, 0, false);
		// Displays the progress bar for the first time.

		mNotificationManager.notify(1, mBuilder.build());

		// ----------------------------------

		try {
			URL url = new URL(_url);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			// urlConnection.setInstanceFollowRedirects(true);
			urlConnection.connect();
			File SDCardRoot = Environment.getExternalStorageDirectory();
			File dirs = new File(SDCardRoot + mDirName);
			dirs.mkdirs();
			File file = new File(SDCardRoot + mDirName, mFileName);
			fileOutput = new FileOutputStream(file);
			InputStream inputStream = urlConnection.getInputStream();
			Log.d("URL", url.toString());
			int totalSize = urlConnection.getContentLength();

			if (totalSize == -1)
				totalSize = 47514203;

			int downloadedSize = 0;

			byte[] buffer = new byte[1024];
			int bufferLength = 0;

			while ((bufferLength = inputStream.read(buffer)) > 0) {

				fileOutput.write(buffer, 0, bufferLength);
				downloadedSize += bufferLength;
				mBuilder.setContentText("Downloading ..."
						+ (getFileSize(downloadedSize)));
				mBuilder.setProgress(totalSize, downloadedSize, false);
				mNotificationManager.notify(1, mBuilder.build());
				if (!Globals.getInstance().isIsNetworkAvailable()) {
					mBuilder.setContentText("ERROR ...No Internet");
					mBuilder.setProgress(0, 0, false);
					mNotificationManager.notify(1, mBuilder.build());
					break;
				}
			}
			fileOutput.close();

			mBuilder.setContentText("Download Finished");
			mBuilder.setProgress(0, 0, false);
			mNotificationManager.notify(1, mBuilder.build());

			// notification.setLatestEventInfo(context, "Finished",
			// "Download Finished", contentIntent);
			// mNotificationManager.notify(1, notification);
		} catch (MalformedURLException e) {
			mBuilder.setContentText("Wrong Url");
			mBuilder.setProgress(0, 0, false);
			mNotificationManager.notify(1, mBuilder.build());
			if (isReceiverRegistered)
				unRegisterReceivers();

			throw new DownloadException("Failed to fetch data!!");
		} catch (FileNotFoundException e) {
			if (isReceiverRegistered)
				unRegisterReceivers();

			mBuilder.setContentText("File Not Found");
			mBuilder.setProgress(0, 0, false);
			mNotificationManager.notify(1, mBuilder.build());
		} catch (IOException e) {
			if (isReceiverRegistered)
				unRegisterReceivers();

			mBuilder.setContentText("IO Error");
			mBuilder.setProgress(0, 0, false);
			mNotificationManager.notify(1, mBuilder.build());

		}

		if (isReceiverRegistered)
			unRegisterReceivers();
		
			Integrity_Check();
	

		return false;

	}

	public static String getFileSize(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size
				/ Math.pow(1024, digitGroups))
				+ " " + units[digitGroups];
	}

	@SuppressWarnings("serial")
	public class DownloadException extends Exception {

		public DownloadException(String message) {
			super(message);
			Integrity_Check();
		}

		public DownloadException(String message, Throwable cause) {
			super(message, cause);
			Integrity_Check();
		}
	}

	/**
	 * 
	 * Check if file downloaded ok and if not delete remainings
	 * 
	 */

	private void Integrity_Check() {

		File SDCardRoot = Environment.getExternalStorageDirectory();
		File file = new File(SDCardRoot + mDirName, mFileName);

		if (file.length() == 0) {
			file.delete();
		}

	}

}
