package com.nordicsemi.nrfUARTv2;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import com.nordicsemi.MLED.R;
import com.nordicsemi.nrfUARTv2.UartService;

import android.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends Activity{

	
	LinearLayout view;
	
	
	private Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
        	
        	if (msg.what == 0) {  
            	
            }
            if (msg.what == 1) {  
            	view.setBackgroundResource(R.drawable.logo1); 
            }
            if (msg.what == 2) {  
            	view.setBackgroundResource(R.drawable.logo2); 
            }
            if (msg.what == 3) {  
            	view.setBackgroundResource(R.drawable.logo3); 
            }
            if (msg.what == 4) {  
            	view.setBackgroundResource(R.drawable.logo4); 
            }
            if (msg.what == 5) {  
            	view.setBackgroundResource(R.drawable.logo5); 
            }
            
        }  
    };
	
	
	
	/** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,        
                WindowManager.LayoutParams. FLAG_FULLSCREEN); 
        setContentView(R.layout.welcome);
        
        view=(LinearLayout)findViewById(R.id.welcomelayout);
        Start();
    }
  
    public void Start() {
                new Thread() {
                        public void run() {
                        	
                        	//View view = (ImageView) findViewById(R.layout.welcome);
                        	

                        	Message msg0 = new Message();  
                            msg0.what = 0;  
                            handler.sendMessage(msg0); 
                            
                        	 try {
                                 Thread.sleep(200);
                         } catch (InterruptedException e) {
                                 e.printStackTrace();
                         }
                        	
                                
                                
                                Message msg2 = new Message();  
                                msg2.what = 2;  
                                handler.sendMessage(msg2);  
                                
                                try {
                                    Thread.sleep(200);
                            } catch (InterruptedException e) {
                                    e.printStackTrace();
                            }
                            
                                Message msg3 = new Message();  
                                msg3.what = 3;  
                                handler.sendMessage(msg3);  
                                
                                try {
                                    Thread.sleep(200);
                            } catch (InterruptedException e) {
                                    e.printStackTrace();
                            }
                            
                                Message msg4 = new Message();  
                                msg4.what = 4;  
                                handler.sendMessage(msg4);  
                                
                                try {
                                    Thread.sleep(200);
                            } catch (InterruptedException e) {
                                    e.printStackTrace();
                            }
                            
                                
                                Message msg5 = new Message();  
                                msg5.what = 5;  
                                handler.sendMessage(msg5);  
                                
                                try {
                                    Thread.sleep(400);
                            } catch (InterruptedException e) {
                                    e.printStackTrace();
                            }
                                
                                
                                Intent intent = new Intent();
                                //intent.setClass(WelcomeActivity.this, MainActivity.class);
                                intent.setClass(WelcomeActivity.this, com.nordicsemi.nrfUARTv2.SelectActivity.class);
                                startActivity(intent);
                                WelcomeActivity.this.finish();
                        }
                }.start();
        }
}
