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

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
 
/**
 * 
 * We use this to play stream from web cam
 *    like conference meetings etc
 *
 * TODO Test it with webcam stream
 *      SrcPath in XML Not Hard coded 
 */


public class StreamPlayer extends Base_Activity {
	
	private static ProgressDialog mProgressDialog;
	
    private static final String SrcPath = "http://articulos.cuevajaen.com/video/kids_video_review/DESPICABLE_ME.mp4";
 
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.streamplayer);
       final VideoView myVideoView = (VideoView)findViewById(R.id.myvideoview);
       myVideoView.setVideoURI(Uri.parse(SrcPath));
       myVideoView.setMediaController(new MediaController(this));
       myVideoView.requestFocus();
       mProgressDialog = ProgressDialog.show(this, "", "Loading...", true);
       
       myVideoView.setOnPreparedListener(new OnPreparedListener() {

    	    public void onPrepared(MediaPlayer arg0) {
    	        mProgressDialog.dismiss();
    	        myVideoView.start();
    	    }
    	});
       
       
       
       //myVideoView.start();
       
       
   }
}