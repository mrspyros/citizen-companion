package com.android.toorcomp.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;


/**
 * 
 * @author MrSpyros
 * 
 * 
 * This Class Stores mail information
 * 
 * @param Short_Desc;
 * @param Category;
 * @param Description;
 * @param Image_File;
 * @param TelNumber;
 * @param Date;
 * 
 * It Gives methods to store/retrieve params @ SDCARD with Json format
 * It has  2 Constructors 
 *      One that creates Object from given data
 *      One that creates and returns Object from SDCARD data
 * 
 *
 */



@SuppressLint("SimpleDateFormat")
public class MailObject {

	private static final String TAG = "MAILOBJECT";
	public String Short_Desc;
	public String Category;
	public String Description;
	public String Image_File;
	public String TelNumber;
	public String Date;

	public MailObject(String SD, String Cat, String Desc, String IF, String Num) {

		this.Short_Desc = SD;
		this.Category = Cat;
		this.Description = Desc;
		this.Image_File = IF;
		this.TelNumber = Num;
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String fDate = df.format(c.getTime());
        this.Date=fDate;

	}

	
	//  --- Empty constructor ---
	//  --- it returns an object from file SDCARD/osmdroid/mail/Mail.json
	
	public MailObject (){
		
		getMailObject();
		
	}
	
	public boolean Write_To_Sd (){
		
		JSONObject obj = makeJSON();
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		@SuppressWarnings("unused")
		String fDate = df.format(c.getTime());

		String sFileName = "Mail.json";

		try {
			File root = new File(Environment.getExternalStorageDirectory(),
					"osmdroid/mail");
			if (!root.exists()) {
				root.mkdirs();
			}
			File sFile = new File(root, sFileName);

			FileWriter writer = new FileWriter(sFile);
			writer.append(obj.toString());
			writer.flush();
			writer.close();
			return true;
		} catch (IOException e) {
			Log.d(TAG, e.toString());
			return false;
		}
	
			
	}
	
	
	
	private JSONObject makeJSON() {
		
		JSONObject obj = new JSONObject();

		try {
			obj.put("DATE", this.Date);
			obj.put("SHORTDESC",this.Short_Desc);
			obj.put("CATEGORY",this.Category);
			obj.put("DESCRIPTION",this.Description);
			obj.put("IMAGE",this.Image_File);
			obj.put("PHONE", this.TelNumber);
			return obj;
		} catch (JSONException e) {
		    Log.d(TAG, e.toString());
		    return null;
		}
			
	}
	
	private void getMailObject(){
		
		String jsonStr = null;
		File sFile = new File(Environment.getExternalStorageDirectory(), "osmdroid/mail/mail.json");
       
		FileInputStream stream=null;

		try {
			stream = new FileInputStream(sFile);

			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			jsonStr = Charset.defaultCharset().decode(bb).toString();
			JSONObject obj = new JSONObject(jsonStr);

			this.Date = obj.getString("DATE");
			this.Short_Desc = obj.getString("SHORTDESC");
			this.Category = obj.getString("CATEGORY");
			this.Description = obj.getString("DESCRIPTION");
			this.Image_File = obj.getString("IMAGE");
			this.TelNumber = obj.getString("PHONE");
		} catch (FileNotFoundException e) {
			Log.d(TAG, "FileNotFound");	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "IOError");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "JSONError");
		}
		finally {
			try {
				stream.close();
			} catch (IOException e) {
				Log.d(TAG, "IOError");
			
			}
		}

		

	 }
		
		
		

	
}
