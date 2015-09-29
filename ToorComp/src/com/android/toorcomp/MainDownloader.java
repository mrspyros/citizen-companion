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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.toorcomp.downloadservice.Downloader;

public class MainDownloader extends Base_Activity{

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_downloader);
	}

	public void btnOnClick(View view) {

		Intent intent = new Intent(getApplicationContext(),Downloader.class);
		Bundle extras = new Bundle();
		
		switch (view.getId()) {

		case R.id.man_dl_btn1:
			extras.putString("DL", "KML");
	        intent.putExtras(extras);	
	        startActivity(intent);
			break;		
		case R.id.man_dl_btn2:
			extras.putString("DL", "POI");
	        intent.putExtras(extras);
	        startActivity(intent);
			break;
		case R.id.man_dl_btn3:
			extras.putString("DL", "MAP");
	        intent.putExtras(extras);
	        startActivity(intent);
			break;
		case R.id.man_dl_btn4:
		{
			finish();
		}
		
		}
		
	}
	
	
}
