package com.android.toorcomp.File_picker;


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

import java.io.File;
import java.util.ArrayList;

import android.os.Environment;
import android.util.Log;

/**
 * 
 * This Class gets as parameter a directory in SD
 * and return an ArrayList with files in Directory
 *
 */

public class File_Picker_Class {

	private String Directory;
	private ArrayList<String> mFile;
	private static String TAG = "ERROR_FILE_PICKER_CLASS";

	
	public File_Picker_Class(String Directory) {
		
		this.Directory = Directory;
	}

	
	
	public ArrayList<String> File_Picker() {

		mFile = new ArrayList<String>();

		String path = Environment.getExternalStorageDirectory().toString()+ this.Directory;

		File f = new File(path);
		
		File file[]=null;
		
	
		
		try {
			file = f.listFiles();
				
		for (int i = 0; i < file.length; i++) {
			
			mFile.add(file[i].getName());
		}
		} catch (Exception e) {
			Log.d (TAG, e.toString());
		}
		
		return mFile;

	}


}
