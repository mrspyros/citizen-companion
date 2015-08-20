package com.android.toorcomp;

import java.util.ArrayList;

import File_picker.File_Picker_Class;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.toorcomp.*;



public class File_picker_activity extends Base_Activity{
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picker);
		
		final ListView mainLst= (ListView) findViewById(R.id.main_lst);
		
		File_Picker_Class f_pick = new File_Picker_Class("/osmdroid/kml/");
		ArrayList<String> mFiles = new ArrayList<String>();		
		mFiles = f_pick.File_Picker();
		
	
		String[] F_table = mFiles.toArray(new String[mFiles.size()]);
		
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, F_table);
			mainLst.setAdapter(adapter);
	
			OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
			    public void onItemClick(AdapterView parent, View v, int position, long id) {
			    	
			    	Globals.getInstance().setKml_File(mainLst.getItemAtPosition(position).toString());
			    	finish();
			                
			    }
			};

			mainLst.setOnItemClickListener(mMessageClickedHandler);
		
		
	}
	

}
