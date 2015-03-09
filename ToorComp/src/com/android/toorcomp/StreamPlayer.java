package com.android.toorcomp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
 
public class StreamPlayer extends Activity {
	
	private static ProgressDialog progressDialog;
	
String SrcPath = "http://articulos.cuevajaen.com/video/kids_video_review/DESPICABLE_ME.mp4";
 
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.streamplayer);
       final VideoView myVideoView = (VideoView)findViewById(R.id.myvideoview);
       myVideoView.setVideoURI(Uri.parse(SrcPath));
       myVideoView.setMediaController(new MediaController(this));
       myVideoView.requestFocus();
       progressDialog = ProgressDialog.show(this, "", "Loading...", true);
       
       myVideoView.setOnPreparedListener(new OnPreparedListener() {

    	    public void onPrepared(MediaPlayer arg0) {
    	        progressDialog.dismiss();
    	        myVideoView.start();
    	    }
    	});
       
       
       
       //myVideoView.start();
       
       
   }
}