
package com.android.toorcomp;
 
import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreen extends Base_Activity {

        private boolean download = false;
        @Override
		public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        		getActionBar().show();
                setContentView(R.layout.activity_main);

                File M_PATH = new File(Environment.getExternalStorageDirectory(),
                                "osmdroid");
                String N_PATH = M_PATH.getAbsolutePath();

                TextView text = (TextView) findViewById(R.id.main_opt_text_2);
                text.setText(N_PATH);

                Button btn1 = (Button) findViewById(R.id.main_btn1);
                btn1.setOnClickListener(new android.view.View.OnClickListener() {
                        public void onClick(View v) {
                                finish();
                                System.exit(0);
                        }
                });

                Button btn2 = (Button) findViewById(R.id.main_btn2);
                btn2.setVisibility(View.GONE);
                /*btn2.setOnClickListener(new android.view.View.OnClickListener() {
                        public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Options.class);// Options.class);
                                // Intent intent = new Intent(getApplicationContext(),
                                // webview.class);
                               
									startActivity(intent);
								
                        }
                });*/

                Button btn3 = (Button) findViewById(R.id.main_btn3);
                btn3.setOnClickListener(new android.view.View.OnClickListener() {
                        public void onClick(View v) {
                                Globals.getInstance().setFirstTimeOnMapActivity(true);
                                Globals.getInstance().setMapZoomLevel(16);
                                Intent intent = new Intent(getApplicationContext(), Map.class);
                                startActivity(intent);
                        }
                });

                Button btn4 = (Button) findViewById(R.id.main_btn4);
                btn4.setOnClickListener(new android.view.View.OnClickListener() {
                        public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(),
                                                New_request.class);
                                startActivity(intent);
                        }
                });

                Button stream = (Button) findViewById(R.id.stream_btn);
                stream.setOnClickListener(new android.view.View.OnClickListener() {
                        public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), StreamPlayer.class);
                                
                                startActivity(intent);
                        }
                });
                
                
                
                Button qRcode = (Button) findViewById(R.id.qrcode);
                qRcode.setOnClickListener(new android.view.View.OnClickListener() {
                        public void onClick(View v) {

                                try {

                                        String packageString = "com.android.toorcomp";
                                        Intent intent = new Intent(
                                                        "com.google.zxing.client.android.SCAN");
                                        intent.setPackage(packageString);
                                        intent.putExtra("SCAN_MODE", "SCAN_MODE");
                                        startActivityForResult(intent, 0);

                                } catch (Exception e) {

                                        Toast.makeText(getApplicationContext(), e.toString(),
                                                        Toast.LENGTH_LONG).show();

                                }

                        }

                });

                final Startup init = new Startup(this);

                Globals g = Globals.getInstance();

                boolean XMLExists = !g.getXmLERROR();
                String XMLanswer = g.get_Xml_Download_Answer();

                if (!XMLExists && XMLanswer == "YES") {

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);

                        builder.setTitle(R.string.noxmltitle);
                        builder.setMessage(R.string.noxmlmessage);

                        builder.setPositiveButton("YES",
                                        new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(final DialogInterface dialog,
                                                                int which) {

                                                        // Code that is executed when clicking YES
                                                        // dialog.dismiss();

                                                        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                                                                ProgressDialog pd = new ProgressDialog(
                                                                                MainScreen.this);

                                                                @Override
                                                                protected void onPreExecute() {

                                                                        try {

                                                                                pd.setTitle("Downloading");
                                                                                pd.setMessage("Please Wait");
                                                                                pd.setCancelable(true);
                                                                                pd.setIndeterminate(true);
                                                                                pd.show();
                                                                        } catch (Exception e) {
                                                                                Toast.makeText(getApplicationContext(),
                                                                                                "1= " + e.toString(),
                                                                                                Toast.LENGTH_LONG).show();

                                                                        }

                                                                }

                                                                @Override
                                                                protected Void doInBackground(Void... arg0) {
                                                                        try {
                                                                                download = init.DownloadXML();
                                                                                Thread.sleep(5000);
                                                                        } catch (InterruptedException e) {
                                                                                // TODO Auto-generated catch block
                                                                                e.printStackTrace();
                                                                        }
                                                                        return null;
                                                                }

                                                                @Override
                                                                protected void onPostExecute(Void result) {

                                                                        try {
                                                                                if (pd.isShowing()) {
                                                                                        pd.hide();
                                                                                        pd.dismiss();
                                                                                }
                                                                        } catch (Exception e) {
                                                                                Toast.makeText(getApplicationContext(),
                                                                                                "2= " + e.toString(),
                                                                                                Toast.LENGTH_LONG).show();
                                                                        }

                                                                        if (!download) {
                                                                                Globals g = Globals.getInstance();
                                                                                Toast.makeText(getApplicationContext(),
                                                                                                R.string.noxmldl + g.getWait(),
                                                                                                Toast.LENGTH_LONG).show();
                                                                                g.setXmLERROR(true);
                                                                                g.set_Xml_Download_Answer("NO");
                                                                                dialog.dismiss();

                                                                        } else {
                                                                                Globals g = Globals.getInstance();
                                                                                Toast.makeText(getApplicationContext(),
                                                                                                R.string.noxmldlok,
                                                                                                Toast.LENGTH_LONG).show();
                                                                                g.setXmLERROR(false);
                                                                                dialog.dismiss();

                                                                        }

                                                                }

                                                        };
                                                        task.execute((Void[]) null);

                                                }

                                        });

                        builder.setNegativeButton("NO",
                                        new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                        // Code that is executed when clicking NO
                                                        Globals g = Globals.getInstance();
                                                        g.setXmLERROR(true);
                                                        g.set_Xml_Download_Answer("NO");
                                                        dialog.dismiss();

                                                }

                                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                }

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == 0) {

                        if (resultCode == RESULT_OK) {
                                Globals g = Globals.getInstance();
                                g.setWebViewUrl(data.getStringExtra("SCAN_RESULT"));
                                Intent intent = new Intent(getApplicationContext(),
                                                webview.class);
                                startActivity(intent);
                        }
                        if (resultCode == RESULT_CANCELED) {
                                // handle cancel
                        }
                }
        }

        
}