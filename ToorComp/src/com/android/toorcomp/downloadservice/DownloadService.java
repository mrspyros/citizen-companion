package com.android.toorcomp.downloadservice;

import android.R;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.android.toorcomp.Globals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "DownloadService";
    private NotificationManager mNotificationManager;
   // private Notification notification ;
    private String dir;
    private String _fname;
    
    
    public DownloadService() {
        super(DownloadService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String url = intent.getStringExtra("url");
        dir = intent.getStringExtra("Directory");
        _fname = intent.getStringExtra("F_name");

        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(url)) {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            try {
              //  String[] results = downloadData(url);
                  boolean ok = FileDownload(url);
                /* Sending result back to activity */
                //if (null != results && results.length > 0) {
                  if (ok){
                    //bundle.putStringArray("result", results);
                    
                	String [] res = new String[]{"Downloaded",url};
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

    @SuppressWarnings("deprecation")
	private boolean FileDownload (String _url) throws DownloadException, IOException{
    	
    	FileOutputStream fileOutput=null;
    	
    	Globals g = Globals.getInstance();
		g.setWait("YES");

		// ---------------Notification-------------------
		
		
		String ns = Context.NOTIFICATION_SERVICE;
        mNotificationManager = (NotificationManager) getSystemService(ns);
            

            int icon = R.drawable.ic_dialog_alert;
            CharSequence tickerText = "Downloading";
            long when = System.currentTimeMillis();
            
            @SuppressWarnings("deprecation")
        //    Notification notification = new Notification(icon,
          //          tickerText, when);
         //   notification.flags |= Notification.FLAG_AUTO_CANCEL;
            Context context = getApplicationContext();
            CharSequence contentTitle = getResources().getText(com.android.toorcomp.R.string.app_name);
            CharSequence contentText = getResources().getText(com.android.toorcomp.R.string.loading);
            Intent notificationIntent = new Intent(context,            
            		Downloader.class);
            PendingIntent contentIntent = PendingIntent
                    .getActivity(context, 0, notificationIntent, 0);            
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    getApplicationContext()).setSmallIcon(R.drawable.ic_dialog_alert)
                    .setSmallIcon(icon)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setContentIntent(contentIntent);
            
            mBuilder.setProgress(0, 0, true);
            // Displays the progress bar for the first time.
            
            
         //   notification.setLatestEventInfo(context, contentTitle,
                //    contentText, contentIntent);

            //mNotificationManager.notify(1, notification);
            mNotificationManager.notify(1, mBuilder.build());
		
		// ----------------------------------
		
		
		
		try {
			URL url = new URL(_url);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			//urlConnection.setInstanceFollowRedirects(true); 
			urlConnection.connect();
			File SDCardRoot = Environment.getExternalStorageDirectory();
			File dirs = new File(SDCardRoot + dir);
			dirs.mkdirs();
			File file = new File(SDCardRoot + dir, _fname);
			fileOutput = new FileOutputStream(file);
			InputStream inputStream = urlConnection.getInputStream();
				
			//int totalSize = urlConnection.getContentLength();
			//int downloadedSize = 0;

			byte[] buffer = new byte[1024];
			int bufferLength = 0; 

			while ((bufferLength = inputStream.read(buffer)) > 0) {
				
			//	fileOutput.write(buffer, 0, bufferLength);
			//	downloadedSize += bufferLength;

			}
			fileOutput.close();
			
			
            mBuilder.setContentText("Download Finished");
            mBuilder.setProgress(0, 0, false);
            mNotificationManager.notify(1, mBuilder.build());
			
            
			 //  notification.setLatestEventInfo(context, "Finished",
	         //           "Download Finished", contentIntent);
			 //  mNotificationManager.notify(1, notification);
		} catch (MalformedURLException e) {
			 
			mBuilder.setContentText("Download Error");
            mBuilder.setProgress(0, 0, false);
            mNotificationManager.notify(1, mBuilder.build());
			
			throw new DownloadException("Failed to fetch data!!");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			mBuilder.setContentText("IO Error");
            mBuilder.setProgress(0, 0, false);
            mNotificationManager.notify(1, mBuilder.build());
			
		}
			
			
    	
    	return false;
    }
    
    
    public class DownloadException extends Exception {

        public DownloadException(String message) {
            super(message);
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}