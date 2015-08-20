package File_picker;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import android.os.Environment;
import android.util.Log;

public class File_Picker_Class {

	private String Directory;
	private File mFile;
	private String mUrls;

	
	public File_Picker_Class(String Directory) {
		
		this.Directory = Directory;
	}
	
    public File_Picker_Class(File mFile) {
		
    	this.mFile = mFile;
		
	}
	
	
	
	public ArrayList<String> File_Picker() {

		ArrayList<String> mFile = new ArrayList<String>();

		String path = Environment.getExternalStorageDirectory().toString()+ this.Directory;

		File f = new File(path);
		
		File file[]=null;
		
	
		
		try {
			file = f.listFiles();
				
		for (int i = 0; i < file.length; i++) {
			
			mFile.add(file[i].getName());
		}
		} catch (Exception e) {
			
		}
		
		return mFile;

	}


}
