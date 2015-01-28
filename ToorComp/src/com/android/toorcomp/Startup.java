package com.android.toorcomp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

public class Startup  {
	
	Context c;
	// c is Like getApplicationContext() because Startup is not an activity

	
	public Startup(Context cont) {
		c=cont;
		// TODO Auto-generated constructor stub
	}

	public Boolean DownloadXML () {
		
		
		try {

			Globals g = Globals.getInstance();
			g.setWait("YES");
			
	        URL url = new URL("http://rss.in.gr/feed/news");

	        //create the new connection
	        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

	        urlConnection.connect();

	        //set the path where we want to save the file
	        //in this case, going to save it on the root directory of the
	        //sd card.
	        File SDCardRoot = Environment.getExternalStorageDirectory();
	        //create a new file, specifying the path, and the filename
	        //which we want to save the file as.
	        File file = new File(SDCardRoot+"/osmdroid/","Demo.xml");

	        //this will be used to write the downloaded data into the file we created
	        FileOutputStream fileOutput = new FileOutputStream(file);

	        //this will be used in reading the data from the internet
	        InputStream inputStream = urlConnection.getInputStream();

	        //this is the total size of the file
	        int totalSize = urlConnection.getContentLength();
	        //progressDialog.setMax(totalSize);

	        //variable to store total downloaded bytes
	        int downloadedSize = 0;

	        //create a buffer...
	        byte[] buffer = new byte[1024];
	        int bufferLength = 0; //used to store a temporary size of the buffer

	        //now, read through the input buffer and write the contents to the file
	        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
	                //add the data in the buffer to the file in the file output stream (the file on the sd card
	                fileOutput.write(buffer, 0, bufferLength);
	                //add up the size so we know how much is downloaded
	                downloadedSize += bufferLength;

	        }
	        //close the output stream when done
	        fileOutput.close();
	        //Globals g = Globals.getInstance();
			g.setWait("NO");
	        //catch some possible errors...
	} catch (MalformedURLException e) {
		Globals.getInstance().setWait("URLERROR");
	    return false;  
	    //e.printStackTrace();
	} catch (IOException e) {
		Globals.getInstance().setWait(e.getLocalizedMessage());//"IOERROR");
	    	    return false;    
		//e.printStackTrace();
	}
		 Globals g = Globals.getInstance();
		 g.setWait("NO");
		 return true;
		
	}
	

	
	
	
	
	public Boolean CheckXML(){
		//File SDCardRoot = Environment.getExternalStorageDirectory();
		//deleteFiles(SDCardRoot+"/osmdroid/tiles/Mapnik/");
		//return false;
	    
		
		//return CheckFileExists(Environment.getExternalStorageDirectory().getAbsolutePath()+"/osmdroid/");
		
		return CheckFileExists(Environment.getExternalStorageDirectory().getAbsolutePath()+"/osmdroid/Pois.xml");
				
	}
	
	
	public static void deleteFiles(String path) {

	    File file = new File(path);

	    if (file.exists()) {
	        String deleteCmd = "rm -r " + path;
	        Runtime runtime = Runtime.getRuntime();
	        try {
	            runtime.exec(deleteCmd);
	        } catch (IOException e) { }
	    }
	}
	
	
	private Boolean CheckFileExists(String path){
		
		File file = new File(path);
		return file.exists();
	}

	
}
