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

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * Menu/Information Activity
 *
 */

public class Info extends Base_Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		
		TextView text = (TextView) findViewById(R.id.infoEditText);
		Globals.getInstance();
		final String mPrgversion = Globals.getPROGRAMVERSION();
		final String mDbversion = Globals.getDBVERSION();
		
		text.setEnabled(false);
		text.setText(Html.fromHtml(""
				+ "<b>Program Version : </b>"+mPrgversion+"<br><b>Database Version : </b>"+mDbversion+"<br>"
				));
		
				
		Button btn_back = (Button) findViewById(R.id.info_back);
		btn_back.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		
		
	}

	
	
}
