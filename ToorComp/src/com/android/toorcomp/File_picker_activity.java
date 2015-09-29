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

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.android.toorcomp.File_picker.File_Picker_Class;

/**
 * 
 * This Class populates a listview with the KML files that can be found on
 * /osmdroid/kml directory calls File_Picker_Class to get files and adds a click
 * listener on every item of the listview and sets global variable Kml_File with
 * the clicked name
 *
 */

public class File_picker_activity extends Base_Activity {

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.picker);

		final ListView mMainLst = (ListView) findViewById(R.id.main_lst);

		File_Picker_Class f_pick = new File_Picker_Class("/osmdroid/kml/");
		ArrayList<String> mFiles = new ArrayList<String>();
		mFiles = f_pick.File_Picker();

		/**
		 * check if no file in dir set message 
		 */

		if (mFiles.size() == 0) {

			mFiles.add("No KML Files Found");

			String[] mF_table = mFiles.toArray(new String[mFiles.size()]);

			ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, mF_table);
			mMainLst.setAdapter(mAdapter);
			Globals.getInstance().setKml_File("");

			OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
				@SuppressWarnings("rawtypes")
				public void onItemClick(AdapterView parent, View v,
						int position, long id) {
					finish();

				}
			};

			mMainLst.setOnItemClickListener(mMessageClickedHandler);

		} else {
			String[] mF_table = mFiles.toArray(new String[mFiles.size()]);

			ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, mF_table);
			mMainLst.setAdapter(mAdapter);

			OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
				@SuppressWarnings("rawtypes")
				public void onItemClick(AdapterView parent, View v,
						int position, long id) {

					Globals.getInstance().setKml_File(
							mMainLst.getItemAtPosition(position).toString());
					finish();

				}
			};

			mMainLst.setOnItemClickListener(mMessageClickedHandler);
		}
	}

}
