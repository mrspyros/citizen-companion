package com.android.toorcomp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
 
public class StreamPlayer extends Activity {
 
String SrcPath = "https://www.youtube.com/watch?v=uyr1ICZxJYI";
 
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.streamplayer);
       VideoView myVideoView = (VideoView)findViewById(R.id.myvideoview);
       myVideoView.setVideoURI(Uri.parse(SrcPath));
       myVideoView.setMediaController(new MediaController(this));
       myVideoView.requestFocus();
       myVideoView.start();
   }
}