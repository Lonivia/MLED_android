/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nordicsemi.nrfUARTv2;




import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.CharBuffer;
import java.text.DateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
//import org.apache.axis.client.Service;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

//import com.soap.mysaop.SoapTestControllerBindingStub;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;

import android.text.Editable;
import android.text.TextWatcher;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.Time;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.nordicsemi.MLED.R;





public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener {
	
	//SoapTestControllerBindingStub soapt;
	
	
    private static final int REQUEST_SELECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int UART_PROFILE_READY = 10;
    public static final String TAG = "nRFUART";
    private static final int UART_PROFILE_CONNECTED = 20;
    private static final int UART_PROFILE_DISCONNECTED = 21;
    private static final int STATE_OFF = 10;

    //int cancel=0;
    
    TextView mRemoteRssiVal;
    RadioGroup mRg;
    private int mState = UART_PROFILE_DISCONNECTED;
    private UartService mService = null;
    private BluetoothDevice mDevice = null;
    private BluetoothAdapter mBtAdapter = null;
    private ListView messageListView;
    private ArrayAdapter<String> listAdapter;
    private Button btnConnectDisconnect;
    //private EditText edtMessage;
    private EditText edtbianhao;
    private ImageView img; 
    
    private Button btnblack,btnfull,btnshangchuan;
    
    
    private Canvas canvas;
    private Bitmap baseBitmap;
    Paint paint;
    float prevX,prevY;
    
    
    
    final static String FOLDER = "/MLEDfile/";   
	//final static String FILENAME = "config";    
	final static String SUFFIX = ".mdz";
	
	
	public static final String NAMESPACE = "http://114.215.166.6/service.php/demourn:demo";
	public static final String URL = "http://114.215.166.6/service.php?wsdl";
	
	
	
	
	public String real[]={"","","","","","","","","","",
			"","","","","","","","","","",
			"","","","","","","","","","",
			"","","","","","","","","","",
			"","","","","","","","","",};
	
	private int TestFlag=0;
	
	
	
	//覆盖（重写）!!
  	private void writeFile(String sb ,String FILENAME) {
  	    String foldername = Environment.getExternalStorageDirectory().getPath()   
  	                             + FOLDER;   
  	    File folder = new File(foldername);   
  	    if (folder != null && !folder.exists()) {   
  	        if (!folder.mkdir() && !folder.isDirectory())   
  	        {   
  	            Log.d("SelectLog", "Error: make dir failed!");   
  	            return;   
  	        }   
  	    }   
  	    
  	    String stringToWrite = sb.toString();   
  	    String targetPath = foldername + FILENAME + SUFFIX;   
  	    File targetFile = new File(targetPath);   
  	    if (targetFile != null) {   
  	        if (targetFile.exists()) {   
  	            targetFile.delete();   
  	        }    
  	    
  	        OutputStreamWriter osw;   
  	        try{   
  	            osw = new OutputStreamWriter(   
  	                        new FileOutputStream(targetFile),"utf-8");   
  	            try {   
  	                osw.write(stringToWrite);   
  	                osw.flush();   
  	                osw.close();   
  	            } catch (IOException e) {     
  	                e.printStackTrace();   
  	            }   
  	        } catch (UnsupportedEncodingException e1) {     
  	            e1.printStackTrace();   
  	        } catch (FileNotFoundException e1) {     
  	            e1.printStackTrace();   
  	        }   
  	    }   
  	}
  	//不覆盖
  	private void writeCSV(String sb ,String FILENAME) {
  	    String foldername = Environment.getExternalStorageDirectory().getPath()   
  	                             + FOLDER;   
  	    File folder = new File(foldername);   
  	    if (folder != null && !folder.exists()) {   
  	        if (!folder.mkdir() && !folder.isDirectory())   
  	        {   
  	            Log.d("SelectLog", "Error: make dir failed!");   
  	            return;   
  	        }   
  	    }   
  	    
  	    String stringToWrite = sb.toString();   
  	    String targetPath = foldername + FILENAME + ".csv";   
  	    File targetFile = new File(targetPath);   
  	    if (targetFile != null) {   
  	        if (targetFile.exists()) {   
  	            //targetFile.delete();   
  	        }    
  	    
  	        OutputStreamWriter osw;   
  	        try{   
  	            osw = new OutputStreamWriter(   
  	                        new FileOutputStream(targetFile,true),"utf-8");   
  	            try {   
  	                osw.write(stringToWrite);   
  	                osw.flush();   
  	                osw.close();   
  	            } catch (IOException e) {     
  	                e.printStackTrace();   
  	            }   
  	        } catch (UnsupportedEncodingException e1) {     
  	            e1.printStackTrace();   
  	        } catch (FileNotFoundException e1) {     
  	            e1.printStackTrace();   
  	        }   
  	    }   
  	}
  	

  	private void delFile(String FILENAME){
  		String foldername = Environment.getExternalStorageDirectory().getPath()   
                  + FOLDER;   
  		File folder = new File(foldername);   
  		if (folder != null && !folder.exists()) {   
  			if (!folder.mkdir() && !folder.isDirectory())   
  			{   
  				Log.d("SelectLog", "Error: make dir failed!");   
  				return;   
  			}   
  		}   

  		String targetPath = foldername + FILENAME + ".csv";   
  		File targetFile = new File(targetPath);   
  		if (targetFile != null) {   
  			if (targetFile.exists()) {   
  				targetFile.delete();   
  			}  
  		}
  	}
  	
  	
  	private String readFile(String filename) {  
  		String foldername = Environment.getExternalStorageDirectory().getPath()   
                  + FOLDER;   
  		String targetPath = foldername + filename + SUFFIX;    
  	 
  	    String filecontent = null;   
  	    File f = new File(targetPath);   
  	    if (f != null && f.exists())   
  	    {
  	        FileInputStream fis = null;   
  	        try {   
  	            fis = new FileInputStream(f);   
  	        } catch (FileNotFoundException e1) {      
  	            e1.printStackTrace();   
  	            Log.d("SelectLog", "Error: Input File not find!");   
  	            return null;   
  	        }   
  	  
  	        CharBuffer cb;   
  	        try {   
  	            cb = CharBuffer.allocate(fis.available());   
  	        } catch (IOException e1) {     
  	            e1.printStackTrace();   
  	            Log.d("SelectLog", "Error: CharBuffer initial failed!");   
  	            return null;   
  	        }   
  	    
  	        InputStreamReader isr;   
  	        try {   
  	            isr = new InputStreamReader(fis, "utf-8");   
  	            try {   
  	                if (cb != null) {   
  	                   isr.read(cb);   
  	                }   
  	                filecontent = new String(cb.array());   
  	                isr.close();   
  	            } catch (IOException e) {   
  	                e.printStackTrace();   
  	            }   
  	        } catch (UnsupportedEncodingException e) {    
  	            e.printStackTrace();           
  	        }   
  	    }   
  	    Log.d("SelectLog", "readFile filecontent = " + filecontent);   
  	    return filecontent;   
  	} 
 	
	// 	byte[] value={0x01,0x43};
	// 	try{
	//    	mService.writeRXCharacteristic(value);
	// 	}catch(Exception e){
	// 		Log.e("MyLog", "no send 01 43 rec error!");
	// 	}
	//    Log.e("MyLog", "----------"+"Send 0143 start receive");
   // }


  	int point=0;
  	
  	
  	public Button btnnet;
    
  	Handler hd1=new Handler();
    Handler hd2=new Handler();
    Handler hd3=new Handler();
    Handler hd4=new Handler();
  	
    public static boolean isChineseChar(String str){
        boolean temp = false;
        Pattern p=Pattern.compile("[\u4e00-\u9fa5]"); 
        Matcher m=p.matcher(str); 
        if(m.find()){ 
            temp =  true;
        }
        return temp;
    }
  	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        messageListView = (ListView) findViewById(R.id.listMessage);
        listAdapter = new ArrayAdapter<String>(this, R.layout.message_detail);
        messageListView.setAdapter(listAdapter);
        messageListView.setDivider(null);
        btnConnectDisconnect=(Button) findViewById(R.id.btn_select);
        //btnSend=(Button) findViewById(R.id.sendButton);
        //edtMessage = (EditText) findViewById(R.id.sendText);
        edtbianhao=(EditText) findViewById(R.id.editTextbianhao);
        btnblack=(Button) findViewById(R.id.buttonblack);
        btnfull=(Button) findViewById(R.id.buttonfull);
        btnshangchuan=(Button) findViewById(R.id.buttonshangchuan);
        
        
        
        img=(ImageView) findViewById(R.id.imageView1); 
        paint=new Paint();
    	paint.setStrokeWidth(2); 
    	paint.setColor(Color.BLACK); 
    	if (baseBitmap == null) {  
            baseBitmap = Bitmap.createBitmap(640,  
            		480, Bitmap.Config.ARGB_8888);  
            canvas = new Canvas(baseBitmap);  
            //canvas.drawColor(Color.WHITE);  
        }
    	canvas.drawLine(50, 50, 50, 430, paint);
    	canvas.drawLine(50, 430, 600, 430, paint);
    	canvas.drawLine(50, 50, 30, 80, paint);
    	canvas.drawLine(50, 50, 70, 80, paint);
    	canvas.drawLine(570, 410, 600, 430, paint);
    	canvas.drawLine(570, 450, 600, 430, paint);
    	img.setImageBitmap(baseBitmap);

        
        //btnSend.setEnabled(true);
        //edtMessage.setEnabled(true);
        
        //buttoncancel=(Button) findViewById(R.id.buttoncancel);
        //edtMessage.setText("01 97");


        service_init();

        btnnet=(Button) findViewById(R.id.buttonnet);
     
        
        
        
        
        btnblack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	if (!mBtAdapter.isEnabled()) {
                    Log.i(TAG, "onClick - BT not enabled yet");
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                }
                else {
            		
                	btnConnectDisconnect.setEnabled(false);
                	btnblack.setEnabled(false);
                	btnfull.setEnabled(false);
                	/*
                	if (mDevice!=null){
                		//mDevice=null;
                		mService.disconnect();
                		//mDevice=null;
                		//mService.close();
                		//mService.initialize();
                		mDevice=null;
                		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                   	 	listAdapter.add("["+currentDateTimeString+"] 连接失败，请重试！");
                	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                	 	btnConnectDisconnect.setEnabled(true);
                	}else
                	*/
                	{
                		mDevice=null;
                		hd1.postDelayed(new Runnable(){    
        		    	    public void run() {    
        		    	    	mDevice =BluetoothAdapter.getDefaultAdapter().getRemoteDevice(SelectActivity.addr1);
        		    	    	Log.e("mDev!", "----------"+"GetAddapter!!!"+mDevice.getName()+"---"+mDevice.toString());
        		    	    }    
        		    	 }, 500);
                		hd2.postDelayed(new Runnable(){    
        		    	    public void run() {    
        		    	    	try{
        		    	    		//mDevice =BluetoothAdapter.getDefaultAdapter().getRemoteDevice(SelectActivity.addr1);
                        			boolean connflag=mService.connect(SelectActivity.addr1);
                        			//mService.onConnectionStateChangeConnected();
                        			Log.e("MyLog", "----------"+"Connect!!!"+SelectActivity.addr1);

                        			//mState = UART_PROFILE_CONNECTED;
                        			if(connflag){
	                        			String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
	                               	 	listAdapter.add("["+currentDateTimeString+"] Connected to: "+ mDevice.getName()+"正在连接设备");
	                            	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                        			}else{
                        				String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
	                               	 	listAdapter.add("["+currentDateTimeString+"] 连接失败，请重试！");
	                            	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
	                            	 	mService.disconnect();
	                            		mDevice=null;
	                            		btnConnectDisconnect.setEnabled(true);
	                            	 	btnblack.setEnabled(true);
	                            	 	btnfull.setEnabled(true);
                        			}
                        		}catch(Exception ee){
                            		Log.e("MyLog", "----------"+"no MDZ found!!!");
                            		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                            		//((TextView) findViewById(R.id.deviceName)).setText(mDevice.getName()+ " - ready"+"找脉中...");
                                    listAdapter.add("["+currentDateTimeString+"] 连接失败！");
                               	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                               	 	mService.disconnect();
	                        		mDevice=null;
	                        		btnConnectDisconnect.setEnabled(true);
                            	 	btnblack.setEnabled(true);
                            	 	btnfull.setEnabled(true);

                            	}
                        		//mService.enableTXNotification();
                        		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        		

        		    	    }    
        		    	 }, 1000);
                		hd3.postDelayed(new Runnable(){    
        		    	    public void run() { 
        		    	    	byte[] value={0x01,0x00};
        		    	    	Log.e("main", "0x01 0x00 0x00");
        		    	    	
        		    	    		value[1]=(byte)Integer.parseInt("57",16);
        		    	    	
        		    	    	Log.e("MyLog", "----------"+"ready to send!!!");
        		    	    	try{
	                 		    	int sendflag=
	                 		    	mService.writeRXCharacteristic(value);
	                 		    	if(sendflag==1){
		                 		    	String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
		                 		    	listAdapter.add("["+currentDateTimeString+"] Connected to: "+ mDevice.getName()+"正在进行全黑校准...");
		                        	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
		                        	 	
		                        	 	TestFlag=1;
		                        	 	
		                        	 	paint.setStrokeWidth(2); 
	                        	    	paint.setColor(Color.BLACK); 
	                        	    	if (baseBitmap == null) {  
	                        	            baseBitmap = Bitmap.createBitmap(640,  
	                        	            		480, Bitmap.Config.ARGB_8888);  
	                        	            canvas = new Canvas(baseBitmap);  
	                        	            canvas.drawColor(Color.WHITE);  
	                        	        }
	                        	    	
	                        	    	canvas.drawColor(Color.WHITE); 
	                        	    	canvas.drawLine(50, 50, 50, 430, paint);
	                        	    	canvas.drawLine(50, 430, 600, 430, paint);
	                        	    	canvas.drawLine(50, 50, 30, 80, paint);
	                        	    	canvas.drawLine(50, 50, 70, 80, paint);
	                        	    	canvas.drawLine(570, 410, 600, 430, paint);
	                        	    	canvas.drawLine(570, 450, 600, 430, paint);
	                        	    	img.setImageBitmap(baseBitmap);
		                        	 	
		                        	 	point=0;
	                 		    	}else{
	                 		    		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
		                 		    	listAdapter.add("["+currentDateTimeString+"] Connected to: "+ mDevice.getName()+"命令发送失败，请重试！");
		                        	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
		                        	 	mService.disconnect();
		                        		mDevice=null;

		                        		btnConnectDisconnect.setEnabled(true);
	                            	 	btnblack.setEnabled(true);
	                            	 	btnfull.setEnabled(true);
	                 		    	}
	            					
	            					//CSVname=t.format("%Y%m%dT%H%M%S");
        		    	    	}catch(Exception e){
        		    	    		Log.e("MyLog", "send 01 54 error!!");
        		    	    		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                     		    	listAdapter.add("["+currentDateTimeString+"] 测试命令发送失败");
                            	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                            	 	btnConnectDisconnect.setEnabled(true);
                            	 	btnblack.setEnabled(true);
                            	 	btnfull.setEnabled(true);
        		    	    	}
                 		    	//value[1]=0x46;
                 		    	//mService.writeRXCharacteristic(value);
                 		    	
                 		    	
        		    	    }    
        		    	 }, 3000);
                		
                	}
                }
            	
            }
            
    	});
        btnfull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	if (!mBtAdapter.isEnabled()) {
                    Log.i(TAG, "onClick - BT not enabled yet");
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                }
                else {
            		
                	btnConnectDisconnect.setEnabled(false);
                	btnblack.setEnabled(false);
                	btnfull.setEnabled(false);
                	/*
                	if (mDevice!=null){
                		//mDevice=null;
                		mService.disconnect();
                		//mDevice=null;
                		//mService.close();
                		//mService.initialize();
                		mDevice=null;
                		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                   	 	listAdapter.add("["+currentDateTimeString+"] 连接失败，请重试！");
                	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                	 	btnConnectDisconnect.setEnabled(true);
                	}else
                	*/
                	{
                		mDevice=null;
                		hd1.postDelayed(new Runnable(){    
        		    	    public void run() {    
        		    	    	mDevice =BluetoothAdapter.getDefaultAdapter().getRemoteDevice(SelectActivity.addr1);
        		    	    	Log.e("mDev!", "----------"+"GetAddapter!!!"+mDevice.getName()+"---"+mDevice.toString());
        		    	    }    
        		    	 }, 500);
                		hd2.postDelayed(new Runnable(){    
        		    	    public void run() {    
        		    	    	try{
        		    	    		//mDevice =BluetoothAdapter.getDefaultAdapter().getRemoteDevice(SelectActivity.addr1);
                        			boolean connflag=mService.connect(SelectActivity.addr1);
                        			//mService.onConnectionStateChangeConnected();
                        			Log.e("MyLog", "----------"+"Connect!!!"+SelectActivity.addr1);

                        			//mState = UART_PROFILE_CONNECTED;
                        			if(connflag){
	                        			String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
	                               	 	listAdapter.add("["+currentDateTimeString+"] Connected to: "+ mDevice.getName()+"正在连接设备");
	                            	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                        			}else{
                        				String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
	                               	 	listAdapter.add("["+currentDateTimeString+"] 连接失败，请重试！");
	                            	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
	                            	 	mService.disconnect();
	                            		mDevice=null;
	                            		btnConnectDisconnect.setEnabled(true);
	                            	 	btnblack.setEnabled(true);
	                            	 	btnfull.setEnabled(true);
                        			}
                        		}catch(Exception ee){
                            		Log.e("MyLog", "----------"+"no MDZ found!!!");
                            		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                            		//((TextView) findViewById(R.id.deviceName)).setText(mDevice.getName()+ " - ready"+"找脉中...");
                                    listAdapter.add("["+currentDateTimeString+"] 连接失败！");
                               	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                               	 	mService.disconnect();
	                        		mDevice=null;
	                        		btnConnectDisconnect.setEnabled(true);
                            	 	btnblack.setEnabled(true);
                            	 	btnfull.setEnabled(true);

                            	}
                        		//mService.enableTXNotification();
                        		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        		

        		    	    }    
        		    	 }, 1000);
                		hd3.postDelayed(new Runnable(){    
        		    	    public void run() {    
        		    	    	byte[] value={0x01,0x00};
        		    	    	value[1]=(byte)Integer.parseInt("58",16);
        		    	    	Log.e("MyLog", "----------"+"ready to send!!!");
        		    	    	try{
	                 		    	int sendflag=
	                 		    	mService.writeRXCharacteristic(value);
	                 		    	if(sendflag==1){
		                 		    	String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
		                 		    	listAdapter.add("["+currentDateTimeString+"] Connected to: "+ mDevice.getName()+"正在进行全反射校准...");
		                        	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
		                        	 	
		                        	 	TestFlag=2;
		                        	 	
		                        	 	paint.setStrokeWidth(2); 
	                        	    	paint.setColor(Color.BLACK); 
	                        	    	if (baseBitmap == null) {  
	                        	            baseBitmap = Bitmap.createBitmap(640,  
	                        	            		480, Bitmap.Config.ARGB_8888);  
	                        	            canvas = new Canvas(baseBitmap);  
	                        	            canvas.drawColor(Color.WHITE);  
	                        	        }
	                        	    	
	                        	    	canvas.drawColor(Color.WHITE); 
	                        	    	canvas.drawLine(50, 50, 50, 430, paint);
	                        	    	canvas.drawLine(50, 430, 600, 430, paint);
	                        	    	canvas.drawLine(50, 50, 30, 80, paint);
	                        	    	canvas.drawLine(50, 50, 70, 80, paint);
	                        	    	canvas.drawLine(570, 410, 600, 430, paint);
	                        	    	canvas.drawLine(570, 450, 600, 430, paint);
	                        	    	img.setImageBitmap(baseBitmap);
		                        	 	
		                        	 	point=0;
	                 		    	}else{
	                 		    		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
		                 		    	listAdapter.add("["+currentDateTimeString+"] Connected to: "+ mDevice.getName()+"命令发送失败，请重试！");
		                        	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
		                        	 	mService.disconnect();
		                        		mDevice=null;
		                        		btnConnectDisconnect.setEnabled(true);
	                            	 	btnblack.setEnabled(true);
	                            	 	btnfull.setEnabled(true);
	                 		    	}
	            					
	            					//CSVname=t.format("%Y%m%dT%H%M%S");
        		    	    	}catch(Exception e){
        		    	    		Log.e("MyLog", "send 01 54 error!!");
        		    	    		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                     		    	listAdapter.add("["+currentDateTimeString+"] 测试命令发送失败");
                            	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                            	 	btnConnectDisconnect.setEnabled(true);
                            	 	btnblack.setEnabled(true);
                            	 	btnfull.setEnabled(true);
        		    	    	}
                 		    	//value[1]=0x46;
                 		    	//mService.writeRXCharacteristic(value);
                 		    	
                 		    	
        		    	    }    
        		    	 }, 3000);
                		
                	}
                }
            	
            	
            	
            }
            
    	});
        btnshangchuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	if(isChineseChar(edtbianhao.getText().toString())){
            		AlertDialog.Builder builder = new Builder(MainActivity.this);
                    builder.setMessage("编号不能包含中文");
                    builder.setTitle("提示");
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setPositiveButton("返回",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
            		
            	}else{
            	
            		String currentDateTimeString2 = DateFormat.getTimeInstance().format(new Date());
	         	 	listAdapter.add("["+currentDateTimeString2+"] 正在上传，请勿操作...");
	         	 	//listAdapter.add("["+currentDateTimeString+"] 采集完成！");
	         	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
            		
            		if(TestFlag==0){
            			String CSVname="";
                		Time t=new Time();
    					t.setToNow();
    					CSVname="";
    					String date=t.format("%Y%m%dT%H%M%S");
    					CSVname=SelectActivity.user_s+"-"+SelectActivity.obj_s+"-"+edtbianhao.getText().toString()+"-"+date;
    	         	 	String dataArr="";
    	         	 	for(int pointi=0;pointi<28;pointi++){
    	         	 		if(pointi==27){
    	         	 			dataArr+=real[pointi];
    	            		}else{
    	            			dataArr+=real[pointi]+",";
    	            		}
    	            	}
    	         	 	String resul=Data(edtbianhao.getText().toString(),date,dataArr);
    	         	 	if(resul.equals("ok")){
    	         	 		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
    		         	 	listAdapter.add("["+currentDateTimeString+"] 已上传"+"---存储的文件名为："+CSVname+"未找到校准文件，请先校准");
    		         	 	//listAdapter.add("["+currentDateTimeString+"] 采集完成！");
    		         	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
    	         	 	}else if(resul.substring(0,4).equals("okok")){
    	         	 		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
    		         	 	listAdapter.add("["+currentDateTimeString+"] 已上传,运算完毕"+"---存储的文件名为："+CSVname);
    		         	 	//listAdapter.add("["+currentDateTimeString+"] 采集完成！");
    		         	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
    		         	 	try{
	    		         	 	currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
	    		         	 	listAdapter.add("["+currentDateTimeString+"] 运算结果："+resul.substring(4));
	    		         	 	//listAdapter.add("["+currentDateTimeString+"] 采集完成！");
	    		         	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
    		         	 	}catch(Exception eew){
    		         	 		currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
	    		         	 	listAdapter.add("["+currentDateTimeString+"] 运算失败，未知错误");
	    		         	 	//listAdapter.add("["+currentDateTimeString+"] 采集完成！");
	    		         	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
    		         	 	}
    	         	 	}else if(resul.equals("bad")){
    	         	 		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
    		         	 	listAdapter.add("["+currentDateTimeString+"] 已上传,运算出错，原因：设备故障，请联系技术人员");
    		         	 	//listAdapter.add("["+currentDateTimeString+"] 采集完成！");
    		         	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
    	         	 	}else{
    	         	 		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
    		         	 	listAdapter.add("["+currentDateTimeString+"] 上传失败，请检查网络！");
    		         	 	//listAdapter.add("["+currentDateTimeString+"] 采集完成！");
    		         	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
    	         	 	}
            		}else if(TestFlag==1){
            			String CSVname="";
                		Time t=new Time();
    					t.setToNow();
    					CSVname="";
    					String date=t.format("%Y%m%dT%H%M%S");
    					CSVname=SelectActivity.user_s+"-black";
    	         	 	String dataArr="";
    	         	 	for(int pointi=0;pointi<28;pointi++){
    	         	 		if(pointi==27){
    	         	 			dataArr+=real[pointi];
    	            		}else{
    	            			dataArr+=real[pointi]+",";
    	            		}
    	            	}
    	         	 	if(DataTest("black",dataArr).equals("ok")){
    	         	 		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
    		         	 	listAdapter.add("["+currentDateTimeString+"] 已上传全黑校准"+"---存储的文件名为："+CSVname);
    		         	 	//listAdapter.add("["+currentDateTimeString+"] 采集完成！");
    		         	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
    	         	 	}else{
    	         	 		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
    		         	 	listAdapter.add("["+currentDateTimeString+"] 上传全黑校准失败，请检查网络！");
    		         	 	//listAdapter.add("["+currentDateTimeString+"] 采集完成！");
    		         	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
    	         	 	}
            		}else if(TestFlag==2){
            			String CSVname="";
                		Time t=new Time();
    					t.setToNow();
    					CSVname="";
    					String date=t.format("%Y%m%dT%H%M%S");
    					CSVname=SelectActivity.user_s+"-full";
    	         	 	String dataArr="";
    	         	 	for(int pointi=0;pointi<49;pointi++){
    	         	 		if(pointi==48){
    	         	 			dataArr+=real[pointi];
    	            		}else{
    	            			dataArr+=real[pointi]+",";
    	            		}
    	            	}
    	         	 	if(DataTest("full",dataArr).equals("ok")){
    	         	 		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
    		         	 	listAdapter.add("["+currentDateTimeString+"] 已上传全反射校准"+"---存储的文件名为："+CSVname);
    		         	 	//listAdapter.add("["+currentDateTimeString+"] 采集完成！");
    		         	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
    	         	 	}else{
    	         	 		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
    		         	 	listAdapter.add("["+currentDateTimeString+"] 上传全反射校准失败，请检查网络！");
    		         	 	//listAdapter.add("["+currentDateTimeString+"] 采集完成！");
    		         	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
    	         	 	}
            		}else{
            			String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
		         	 	listAdapter.add("["+currentDateTimeString+"] 上传失败，未知错误！");
		         	 	//listAdapter.add("["+currentDateTimeString+"] 采集完成！");
		         	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
            		}
            		
	            	
            	}
            	
            }
            
    	});
        
        
        //TODO net
        btnnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	//checkVersion();
            	//Data();
            	//tv1.setText("hello");
            	
            	//about save!!!!!
            	
            	if(isChineseChar(edtbianhao.getText().toString())){
            		AlertDialog.Builder builder = new Builder(MainActivity.this);
                    builder.setMessage("编号不能包含中文");
                    builder.setTitle("提示");
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setPositiveButton("返回",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
            		
            	}else{
            	
            		if(TestFlag==0){
            			String CSVname="";
                		Time t=new Time();
    					t.setToNow();
    					CSVname="";
    					String date=t.format("%Y%m%dT%H%M%S");
    					CSVname=SelectActivity.user_s+"-"+SelectActivity.obj_s+"-"+edtbianhao.getText().toString()+"-"+date;
                	
    	            	for(int pointi=0;pointi<28;pointi++){
    	            		writeCSV(pointi+","+real[pointi]+"\n",CSVname);
    	            	}
    	            	String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
    	         	 	listAdapter.add("["+currentDateTimeString+"] 已保存"+"---存储的文件名为："+CSVname);
    	         	 	//listAdapter.add("["+currentDateTimeString+"] 采集完成！");
    	         	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
            		}else if(TestFlag==1){
            			String CSVname="";
    					CSVname=SelectActivity.user_s+"-"+"black";
                	
    					
    					delFile(CSVname);
    					
    					
    	            	for(int pointi=0;pointi<28;pointi++){
    	            		writeCSV(pointi+","+real[pointi]+"\n",CSVname);
    	            	}
    	            	String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
    	         	 	listAdapter.add("["+currentDateTimeString+"] 已保存全黑校准"+"---存储的文件名为："+CSVname);
    	         	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
            		}else if(TestFlag==2){
            			String CSVname="";
    					CSVname=SelectActivity.user_s+"-"+"full";
                	
    					delFile(CSVname);
    					
    	            	for(int pointi=0;pointi<49;pointi++){
    	            		writeCSV(pointi+","+real[pointi]+"\n",CSVname);
    	            	}
    	            	String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
    	         	 	listAdapter.add("["+currentDateTimeString+"] 已保存全反射校准"+"---存储的文件名为："+CSVname);
    	         	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
            		}else{
            			String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
    	         	 	listAdapter.add("["+currentDateTimeString+"] 保存失败，未知错误");
    	         	 	//listAdapter.add("["+currentDateTimeString+"] 采集完成！");
    	         	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
            		}
            		

            	}
            	
            	

            	

            	
            	
            }
            
    	});
        
        
        

       
        //TODO Handler Disconnect & Connect button
        btnConnectDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBtAdapter.isEnabled()) {
                    Log.i(TAG, "onClick - BT not enabled yet");
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                }
                else {
            		
                	btnConnectDisconnect.setEnabled(false);
                	btnblack.setEnabled(false);
                	btnfull.setEnabled(false);
                	/*
                	if (mDevice!=null){
                		//mDevice=null;
                		mService.disconnect();
                		//mDevice=null;
                		//mService.close();
                		//mService.initialize();
                		mDevice=null;
                		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                   	 	listAdapter.add("["+currentDateTimeString+"] 连接失败，请重试！");
                	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                	 	btnConnectDisconnect.setEnabled(true);
                	}else
                	*/
                	{
                		mDevice=null;
                		hd1.postDelayed(new Runnable(){    
        		    	    public void run() {    
        		    	    	mDevice =BluetoothAdapter.getDefaultAdapter().getRemoteDevice(SelectActivity.addr1);
        		    	    	Log.e("mDev!", "----------"+"GetAddapter!!!"+mDevice.getName()+"---"+mDevice.toString());
        		    	    }    
        		    	 }, 500);
                		hd2.postDelayed(new Runnable(){    
        		    	    public void run() {    
        		    	    	try{
        		    	    		//mDevice =BluetoothAdapter.getDefaultAdapter().getRemoteDevice(SelectActivity.addr1);
                        			boolean connflag=mService.connect(SelectActivity.addr1);
                        			//mService.onConnectionStateChangeConnected();
                        			Log.e("MyLog", "----------"+"Connect!!!"+SelectActivity.addr1);

                        			//mState = UART_PROFILE_CONNECTED;
                        			if(connflag){
	                        			String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
	                               	 	listAdapter.add("["+currentDateTimeString+"] Connected to: "+ mDevice.getName()+"正在连接设备");
	                            	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                        			}else{
                        				String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
	                               	 	listAdapter.add("["+currentDateTimeString+"] 连接失败，请重试！");
	                            	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
	                            	 	mService.disconnect();
	                            		mDevice=null;
	                            	 	btnConnectDisconnect.setEnabled(true);
	                            	 	btnblack.setEnabled(true);
	                            	 	btnfull.setEnabled(true);
                        			}
                        		}catch(Exception ee){
                            		Log.e("MyLog", "----------"+"no MDZ found!!!");
                            		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                            		//((TextView) findViewById(R.id.deviceName)).setText(mDevice.getName()+ " - ready"+"找脉中...");
                                    listAdapter.add("["+currentDateTimeString+"] 连接失败！");
                               	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                               	 	mService.disconnect();
	                        		mDevice=null;
	                        		btnConnectDisconnect.setEnabled(true);
                            	 	btnblack.setEnabled(true);
                            	 	btnfull.setEnabled(true);

                            	}
                        		//mService.enableTXNotification();
                        		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        		

        		    	    }    
        		    	 }, 1000);
                		hd3.postDelayed(new Runnable(){    
        		    	    public void run() {    
        		    	    	byte[] value={0x01,0x00};
        		    	    	value[1]=(byte)Integer.parseInt("97",16);
        		    	    	Log.e("MyLog", "----------"+"ready to send!!!");
        		    	    	try{
	                 		    	int sendflag=
	                 		    	mService.writeRXCharacteristic(value);
	                 		    	if(sendflag==1){
		                 		    	String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
		                 		    	listAdapter.add("["+currentDateTimeString+"] Connected to: "+ mDevice.getName()+"正在测试中...");
		                        	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
		                        	 	
		                        	 	TestFlag=0;
		                        	 	
		                        	 	paint.setStrokeWidth(2); 
	                        	    	paint.setColor(Color.BLACK); 
	                        	    	if (baseBitmap == null) {  
	                        	            baseBitmap = Bitmap.createBitmap(640,  
	                        	            		480, Bitmap.Config.ARGB_8888);  
	                        	            canvas = new Canvas(baseBitmap);  
	                        	            canvas.drawColor(Color.WHITE);  
	                        	        }
	                        	    	
	                        	    	canvas.drawColor(Color.WHITE); 
	                        	    	canvas.drawLine(50, 50, 50, 430, paint);
	                        	    	canvas.drawLine(50, 430, 600, 430, paint);
	                        	    	canvas.drawLine(50, 50, 30, 80, paint);
	                        	    	canvas.drawLine(50, 50, 70, 80, paint);
	                        	    	canvas.drawLine(570, 410, 600, 430, paint);
	                        	    	canvas.drawLine(570, 450, 600, 430, paint);
	                        	    	img.setImageBitmap(baseBitmap);
		                        	 	
		                        	 	point=0;
	                 		    	}else{
	                 		    		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
		                 		    	listAdapter.add("["+currentDateTimeString+"] Connected to: "+ mDevice.getName()+"命令发送失败，请重试！");
		                        	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
		                        	 	mService.disconnect();
		                        		mDevice=null;
		                        		btnConnectDisconnect.setEnabled(true);
	                            	 	btnblack.setEnabled(true);
	                            	 	btnfull.setEnabled(true);
	                 		    	}
	            					
	            					//CSVname=t.format("%Y%m%dT%H%M%S");
        		    	    	}catch(Exception e){
        		    	    		Log.e("MyLog", "send 01 54 error!!");
        		    	    		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                     		    	listAdapter.add("["+currentDateTimeString+"] 测试命令发送失败");
                            	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                            	 	btnConnectDisconnect.setEnabled(true);
                            	 	btnblack.setEnabled(true);
                            	 	btnfull.setEnabled(true);
        		    	    	}
                 		    	//value[1]=0x46;
                 		    	//mService.writeRXCharacteristic(value);
                 		    	
                 		    	
        		    	    }    
        		    	 }, 3000);
                		
                	}
                	
                	
                	/*
            		if (btnConnectDisconnect.getText().equals("断开连接")){
            			mService.disconnect();
            			Log.e("MyLog", "----------"+"DisConnect!!!");
            			//mService.onConnectionStateChangeDisconnected();
            			btnConnectDisconnect.setText("连接");
            			String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                	 	listAdapter.add("["+currentDateTimeString+"] 已断开连接");
                	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                	 	mDevice=null;
                	 	//mService=null;
            		}else{
            			BluetoothAdapter.getDefaultAdapter().getRemoteDevice(SelectActivity.addr1);
    		    	   Log.e("MyLog", "----------"+"GetAddapter!!!");
    		    	   mService.connect(SelectActivity.addr1);
    		    	   //btnConnectDisconnect.setEnabled(false);
    		    	   btnConnectDisconnect.setText("断开连接");
    		    	   String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
               	 	   listAdapter.add("["+currentDateTimeString+"] 已连接");
               	 	   messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
            		}
            		*/


            		
            		
            		
                }
            }
        });
        // Handler Send button  

        // Set initial UI state
        
        
    }
        
    
    
    
    
    
    
    
    
    //UART service connected/disconnected
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
        		mService = ((UartService.LocalBinder) rawBinder).getService();
        		Log.d(TAG, "onServiceConnected mService= " + mService);
        		if (!mService.initialize()) {
                    Log.e(TAG, "Unable to initialize Bluetooth");
                    finish();
                }

        }

        public void onServiceDisconnected(ComponentName classname) {
       ////     mService.disconnect(mDevice);
        		mService = null;
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        
        //Handler events that received from UART service 
        public void handleMessage(Message msg) {
  
        }
    };
    
    
    
    
    
    //int reci=0;
    
    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            final Intent mIntent = intent;
           //*********************//
            if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
            	 runOnUiThread(new Runnable() {
                     public void run() {
                         	//String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                             //Log.d(TAG, "UART_CONNECT_MSG");
                            // btnConnectDisconnect.setText("Disconnect");
                            // edtMessage.setEnabled(true);
                           //  btnSend.setEnabled(true);
                           //  ((TextView) findViewById(R.id.deviceName)).setText(mDevice.getName()+ " - ready");
                          //   listAdapter.add("["+currentDateTimeString+"] Connected to: "+ mDevice.getName());
                        	// 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                             mState = UART_PROFILE_CONNECTED;
                     }
            	 });
            }
           
          //*********************//
            if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
            	 runOnUiThread(new Runnable() {
                     public void run() {
                    	 //	 String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                          //   Log.d(TAG, "UART_DISCONNECT_MSG");
                          //   btnConnectDisconnect.setText("Connect");
                         //    edtMessage.setEnabled(false);
                         //    btnSend.setEnabled(false);
                         //    ((TextView) findViewById(R.id.deviceName)).setText("Not Connected");
                         //    listAdapter.add("["+currentDateTimeString+"] Disconnected to: "+ mDevice.getName());
                             mState = UART_PROFILE_DISCONNECTED;
                             mService.close();
                            //setUiState();
                         
                     }
                 });
            }
            
          
          //*********************//
            if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
             	 mService.enableTXNotification();
            }
          //*********************//

          //TODO receive!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {
              
            	
                 final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
                 
                 
	                 runOnUiThread(new Runnable() {
	                     public void run() {
	                         try {
	                        	 
	                        	 
	                        	 
	                        	 String hex=new String();
	                        	 /*
	                        	 for(reci=0;txValue[reci]!=0;reci++){
	                        		 String hex = Integer.toHexString(txValue[reci] & 0xFF);
	                        	 }
	                        	 */
	                        	 for (int i = 0; i < txValue.length; i++) {    
	                        	     hex += Integer.toHexString(txValue[i] & 0xFF);    
	                        	     if (hex.length() == 1) {    
	                        	       hex = '0' + hex;    
	                        	     }    
	                        	     //System.out.print(hex.toUpperCase() );    
	                        	 }  
	                        	 //Log.e("aass","----"+txValue[0]+"asd"+(txValue[1]&0xFF));
	                        	 
	                        	 
	                        	 
	                         	//String text = new String(txValue, "ISO-8859-1");
	                        	 String currentDateTimeString;
	                        	 	
	                        	 	
	                        	 	if(txValue[0]==0x01 && (txValue[1]&0xFF)==0x52){
	                        	 		currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
		                        	 	listAdapter.add("["+currentDateTimeString+"] BAT: "+txValue[4]);
		                        	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
	                        	 	}else{
	                        	 		currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
		                        	 	listAdapter.add("["+currentDateTimeString+"] RX: "+hex);
		                        	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
	                        	 	}
	                        	 	
	                        	 	
	                        	 	
	                        	 	if(txValue[0]==0x01 && (txValue[1]&0xFF)==0x96 && TestFlag!=2){
		                        		 //Log.e("aaaa", "---"+point);
	                        	 		if((txValue[4]&0xFF)>0x7F){
	                        	 			txValue[4]=(byte) (((txValue[4]&0xFF)^0x80)&0xFF);
	                        	 			//writeCSV(point+","+(((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF)*(-1)+32768)*(-1)+"\n",CSVname);
	                        	 			real[point]=""+((((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF)*(-1)+32768)*(-1));
	                        	 			

		                        	        
		                        	    	paint.setStrokeWidth(6); 
		                        	    	paint.setColor(Color.RED); 
		                        	    	canvas.drawPoint(50+(point+1)*14, (float) (240+Math.abs(((((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF)*(-1)+32768)*(-1)))*0.01), paint);
		                        	    	if(point>0){
		                        	    		paint.setStrokeWidth(2); 
		                        	    		canvas.drawLine(prevX, prevY, 50+(point+1)*14, (float) (240+Math.abs(((((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF)*(-1)+32768)*(-1)))*0.01), paint);
		                        	    	}
		                        	    	img.setImageBitmap(baseBitmap);
		                        	    	
		                        	    	prevX=50+(point+1)*14;
		                        	    	prevY=(float) (240+Math.abs(((((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF)*(-1)+32768)*(-1)))*0.01);
	                        	 			
	                        	 		}else{
	                        	 			//writeCSV(point+","+(((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF))+"\n",CSVname);
	                        	 			real[point]=""+((((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF)));
	                        	 			
		                        	        
		                        	    	paint.setStrokeWidth(6); 
		                        	    	paint.setColor(Color.RED); 
		                        	    	canvas.drawPoint(50+(point+1)*14, (float) (240-Math.abs(((((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF))))*0.01), paint);
		                        	    	if(point>0){
		                        	    		paint.setStrokeWidth(2); 
		                        	    		canvas.drawLine(prevX, prevY, 50+(point+1)*14, (float) (240-Math.abs(((((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF))))*0.01), paint);
		                        	    	}
		                        	    	img.setImageBitmap(baseBitmap);
		                        	    	
		                        	    	prevX=50+(point+1)*14;
		                        	    	prevY=(float) (240-Math.abs(((((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF))))*0.01);
	                        	 			
	                        	 		}
	                        	 		
	                        	 		
	                        	 		
	                        	 		
	                        	 		
	                        	 		
	                        	 		
	                        	 		
	                        	 		
		                        		 //writeCSV(point+","+((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF)+"\n",CSVname);
		                        		 point++;
		                        		 
	                        		 	
		                        		 
		                        		 
		                        	     Log.e("bbbb", "---"+point+"===");
		                        		 if(point>=28){
		                        			 currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
		 	                        	 	//listAdapter.add("["+currentDateTimeString+"] 采集完成！正在保存..."+"---存储的文件名为："+CSVname);
		 	                        	 	listAdapter.add("["+currentDateTimeString+"] 采集完成！");
		 	                        	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
		 	                        	 	
		 	                        	 	//Data();
		 	                        	 	
		 	                        	 	//btnSend.setEnabled(true);
		 	                        	 	
		 	                        	 	
		 	                        	 	hd1.postDelayed(new Runnable(){    
			 	               		    	    public void run() {    
				 	               		    	    try{
				 	               		    	    Log.i("bat", "---");
					 	                        	 	byte[] value={0x01,0x00};
					 	        		    	    	value[1]=(byte)Integer.parseInt("51",16);
					 	        		    	    	Log.e("MyLog", "----------"+"ready to send!!!");
					 		                 		    mService.writeRXCharacteristic(value);
					 		                 		    Log.i("bat", ""+value);
				 	                        	 	}catch(Exception ef){
				 	                        	 		
				 	                        	 	}
			 	               		    	    }    
		 	                        	 	}, 500);
		 	                        	 	
		 	                        	 	
		 	                        	 	hd2.postDelayed(new Runnable(){    
			 	               		    	    public void run() {    
			 	               		    	    	mService.disconnect();
			 	               		    	    	
				 	               		    	    
					 	                        	btnConnectDisconnect.setEnabled(true);
				                            	 	btnblack.setEnabled(true);
				                            	 	btnfull.setEnabled(true);
			 	               		    	    }    
		 	                        	 	}, 1000);
		 	                        	 	
		 	                        	 	//Data();
		 	                        	 	point=0;
		 	                        	 	
		 	                        	 	
		 	                        	 	 //currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
			 	                        	 //	listAdapter.add("["+currentDateTimeString+"] 上传完毕");
			 	                        	 //	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
			 	                        	 	
		 	                        	 	//mService.disconnect();
		 	                        	 	//mDevice=null;

		 	                        	 	
			 	                        	 //point=0;
			 	                        	//btnConnectDisconnect.setEnabled(true);
		                            	 	//btnblack.setEnabled(true);
		                            	 	//btnfull.setEnabled(true);
			 	                        	 
		                        		 }
		                        		 
		                        	 }
	                        	 	
	                        	 	
	                        	 	if(txValue[0]==0x01 && (txValue[1]&0xFF)==0x96 && TestFlag==2){
		                        		 //Log.e("aaaa", "---"+point);
	                        	 		
	                        	 			
		                        	 		if((txValue[4]&0xFF)>0x7F){
		                        	 			txValue[4]=(byte) (((txValue[4]&0xFF)^0x80)&0xFF);
		                        	 			//writeCSV(point+","+(((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF)*(-1)+32768)*(-1)+"\n",CSVname);
		                        	 			real[point]=""+((((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF)*(-1)+32768)*(-1));
		                        	 			
	
			                        	        
			                        	    	paint.setStrokeWidth(6); 
			                        	    	paint.setColor(Color.RED); 
			                        	    	canvas.drawPoint(50+(point+1)*14, (float) (240+Math.abs(((((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF)*(-1)+32768)*(-1)))*0.01), paint);
			                        	    	if(point>0){
			                        	    		paint.setStrokeWidth(2); 
			                        	    		canvas.drawLine(prevX, prevY, 50+(point+1)*14, (float) (240+Math.abs(((((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF)*(-1)+32768)*(-1)))*0.01), paint);
			                        	    	}
			                        	    	img.setImageBitmap(baseBitmap);
			                        	    	
			                        	    	prevX=50+(point+1)*14;
			                        	    	prevY=(float) (240+Math.abs(((((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF)*(-1)+32768)*(-1)))*0.01);
		                        	 			
		                        	 		}else{
		                        	 			//writeCSV(point+","+(((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF))+"\n",CSVname);
		                        	 			real[point]=""+((((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF)));
		                        	 			
			                        	        
			                        	    	paint.setStrokeWidth(6); 
			                        	    	paint.setColor(Color.RED); 
			                        	    	canvas.drawPoint(50+(point+1)*14, (float) (240-Math.abs(((((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF))))*0.01), paint);
			                        	    	if(point>0){
			                        	    		paint.setStrokeWidth(2); 
			                        	    		canvas.drawLine(prevX, prevY, 50+(point+1)*14, (float) (240-Math.abs(((((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF))))*0.01), paint);
			                        	    	}
			                        	    	img.setImageBitmap(baseBitmap);
			                        	    	
			                        	    	prevX=50+(point+1)*14;
			                        	    	prevY=(float) (240-Math.abs(((((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF))))*0.01);
		                        	 			
		                        	 		}

	                        	 		
		                        		 //writeCSV(point+","+((int)(txValue[4]<<8)&0xFF00 | (int)txValue[5]&0xFF)+"\n",CSVname);
		                        		 point++;
		                        		 if(point>=49){
		                        			 currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
		 	                        	 	//listAdapter.add("["+currentDateTimeString+"] 采集完成！正在保存..."+"---存储的文件名为："+CSVname);
		 	                        	 	listAdapter.add("["+currentDateTimeString+"] 采集完成！");
		 	                        	 	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
		 	                        	 	
		 	                        	 	
		 	                        	 	hd1.postDelayed(new Runnable(){    
			 	               		    	    public void run() {    
				 	               		    	    try{
				 	               		    	    Log.i("bat", "---");
					 	                        	 	byte[] value={0x01,0x00};
					 	        		    	    	value[1]=(byte)Integer.parseInt("51",16);
					 	        		    	    	Log.e("MyLog", "----------"+"ready to send!!!");
					 		                 		    mService.writeRXCharacteristic(value);
					 		                 		    Log.i("bat", ""+value);
				 	                        	 	}catch(Exception ef){
				 	                        	 		
				 	                        	 	}
			 	               		    	    }    
		 	                        	 	}, 500);
		 	                        	 	
		 	                        	 	
		 	                        	 	hd2.postDelayed(new Runnable(){    
			 	               		    	    public void run() {    
			 	               		    	    	mService.disconnect();
			 	               		    	    	
				 	               		    	    
					 	                        	btnConnectDisconnect.setEnabled(true);
				                            	 	btnblack.setEnabled(true);
				                            	 	btnfull.setEnabled(true);
			 	               		    	    }    
		 	                        	 	}, 1000);
		 	                        	 	
		 	                        	 	//Data();
		 	                        	 	point=0;
		 	                        	 	//btnSend.setEnabled(true);
		 	                        	 	
		 	                        	 	 //currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
			 	                        	 //	listAdapter.add("["+currentDateTimeString+"] 上传完毕");
			 	                        	 //	messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
			 	                        	 	
		 	                        	 	
		 	                        	 	//mDevice=null;

		 	                        	 	
			 	                        	
			 	                        	 
		                        		 }
		                        		 
		                        	 }
	                        	 	
	                        	 	
	                        	
	                         } catch (Exception e) {
	                             Log.e(TAG, e.toString());
	                         }
	                     }
	                 });
                 
                 
             }
           //*********************//
            if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART)){
            	showMessage("Device doesn't support UART. Disconnecting");
            	mService.disconnect();
            }
            
            
        }
    };


    
    
    public  String Data(String SN_f,String date_f,String dataArr_f){
		 
		 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
	                .detectDiskReads()  
	                .detectDiskWrites()  
	                .detectNetwork()  
	                .penaltyLog()  
	                .build());   
	    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
	                .detectLeakedSqlLiteObjects()  
	                .detectLeakedClosableObjects()  
	                .penaltyLog()  
	                .penaltyDeath()  
	                .build());  
		 

	    String webResponse = "";
	    String SOAP_ACTION = "http://114.215.166.6/service.php/demourn:demo/Data";
		String METHOD_NAME = "Data";
	    
	    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); 
	    
	    
		  PropertyInfo fromProp =new PropertyInfo();
		  fromProp.setName("user");
		fromProp.setValue(SelectActivity.user_s);
		fromProp.setType(String.class);
		request.addProperty(fromProp);
		//Log.e("net-user",SelectActivity.user_s);
		
		PropertyInfo fromProp2 =new PropertyInfo();
		fromProp2.setName("SN");
		fromProp2.setValue(SN_f);
		  fromProp2.setType(String.class);
		  request.addProperty(fromProp2);
		         
		  PropertyInfo fromProp3 =new PropertyInfo();
			fromProp3.setName("obj");
		fromProp3.setValue(SelectActivity.obj_s);
		fromProp3.setType(String.class);
		request.addProperty(fromProp3);
		//Log.e("net-obj",SelectActivity.obj_s);
		
		
		 PropertyInfo fromProp4 =new PropertyInfo();
			fromProp4.setName("date");
		fromProp4.setValue(date_f);
		fromProp4.setType(String.class);
		request.addProperty(fromProp4);
		
		PropertyInfo fromProp5 =new PropertyInfo();
		fromProp5.setName("dataArr");
		fromProp5.setValue(dataArr_f);
		fromProp5.setType(String.class);
		request.addProperty(fromProp5);
		
	    
	    /*
	    request.addProperty("user", "gln");
	    request.addProperty("SN", "gln");
	    request.addProperty("obj", "gln");
	    request.addProperty("dataArr", "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36");
		   */
                    
      SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
      envelope.dotNet = true;
      envelope.setOutputSoapObject(request);
      HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
           
      try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
		} catch (HttpResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
      /*
      SoapPrimitive response = null;
		try {
			response = (SoapPrimitive)envelope.getResponse();
		} catch (SoapFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
      
      Object response = null;
//!!!!!!!!!!!!!!!!!!!!!!!!!array mode!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		response =envelope.bodyIn;
		try{
		      webResponse = response.toString();
		}catch(Exception es){
			webResponse="Error!!!!!!";
		}
		
		//webResponse = response.toString();
      
      //tv1.setText(webResponse);

      		Log.e("WEB!!!", webResponse);

			return webResponse;
	    }
    
    
    
    
    
    public  String DataTest(String type_f,String dataArr_f){
		 
		 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
	                .detectDiskReads()  
	                .detectDiskWrites()  
	                .detectNetwork()  
	                .penaltyLog()  
	                .build());   
	    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
	                .detectLeakedSqlLiteObjects()  
	                .detectLeakedClosableObjects()  
	                .penaltyLog()  
	                .penaltyDeath()  
	                .build());  
		 

	    String webResponse = "";
	    String SOAP_ACTION = "http://114.215.166.6/service.php//DataTest";
		String METHOD_NAME = "DataTest";
	    
	    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); 
	    
	    
		  PropertyInfo fromProp =new PropertyInfo();
		  fromProp.setName("user");
		fromProp.setValue(SelectActivity.user_s);
		fromProp.setType(String.class);
		request.addProperty(fromProp);
		//Log.e("net-user",SelectActivity.user_s);
		
		PropertyInfo fromProp2 =new PropertyInfo();
		fromProp2.setName("type");
		fromProp2.setValue(type_f);
		  fromProp2.setType(String.class);
		  request.addProperty(fromProp2);
		         
		
		PropertyInfo fromProp5 =new PropertyInfo();
		fromProp5.setName("dataArr");
		fromProp5.setValue(dataArr_f);
		fromProp5.setType(String.class);
		request.addProperty(fromProp5);
		
		
		PropertyInfo fromProp6 =new PropertyInfo();
		fromProp6.setName("date");
		fromProp6.setValue("date123");
		fromProp6.setType(String.class);
		request.addProperty(fromProp6);
		
		
		PropertyInfo fromProp7 =new PropertyInfo();
		fromProp7.setName("obj");
		fromProp7.setValue("obj456");
		fromProp7.setType(String.class);
		request.addProperty(fromProp7);
		
		
	    
	    /*
	    request.addProperty("user", SelectActivity.user_s);
	    request.addProperty("type", type_f);
	    request.addProperty("dataArr", dataArr_f);
	    request.addProperty("date", "123");
	    request.addProperty("obj", "456");
	    */
		   
                   
     SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
     envelope.dotNet = true;
     envelope.setOutputSoapObject(request);
     HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
          
     try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
		} catch (HttpResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
     /*
     SoapPrimitive response = null;
		try {
			response = (SoapPrimitive)envelope.getResponse();
		} catch (SoapFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
     
     Object response = null;
//!!!!!!!!!!!!!!!!!!!!!!!!!array mode!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		response =envelope.bodyIn;
		try{
		      webResponse = response.toString();
		}catch(Exception es){
			webResponse="Error!!!!!!";
		}

		
		
		
		//webResponse = response.toString();
     
     //tv1.setText(webResponse);

     		Log.e("WEB!!!", webResponse);

			return webResponse;
	    }
    
    
    public static String UserLogin(String pwdd){
		 
		 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
	                .detectDiskReads()  
	                .detectDiskWrites()  
	                .detectNetwork()  
	                .penaltyLog()  
	                .build());   
	    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
	                .detectLeakedSqlLiteObjects()  
	                .detectLeakedClosableObjects()  
	                .penaltyLog()  
	                .penaltyDeath()  
	                .build());  
		 

	    String webResponse = "";
	    String SOAP_ACTION = "http://114.215.166.6/service.php//UserLogin";
		String METHOD_NAME = "UserLogin";
	    
	    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); 
	    
	    
		  PropertyInfo fromProp =new PropertyInfo();
		  fromProp.setName("user");
		fromProp.setValue("user");
		fromProp.setType(String.class);
		request.addProperty(fromProp);
		//Log.e("net-user",SelectActivity.user_s);
		
		PropertyInfo fromProp2 =new PropertyInfo();
		fromProp2.setName("pwd");
		fromProp2.setValue(pwdd);
		  fromProp2.setType(String.class);
		  request.addProperty(fromProp2);
		         
		
		PropertyInfo fromProp5 =new PropertyInfo();
		fromProp5.setName("dataArr");
		fromProp5.setValue("dataarr");
		fromProp5.setType(String.class);
		request.addProperty(fromProp5);
		
		
		PropertyInfo fromProp6 =new PropertyInfo();
		fromProp6.setName("date");
		fromProp6.setValue("date123");
		fromProp6.setType(String.class);
		request.addProperty(fromProp6);
		
		
		PropertyInfo fromProp7 =new PropertyInfo();
		fromProp7.setName("obj");
		fromProp7.setValue("obj456");
		fromProp7.setType(String.class);
		request.addProperty(fromProp7);
		
		
	    
	    /*
	    request.addProperty("user", SelectActivity.user_s);
	    request.addProperty("type", type_f);
	    request.addProperty("dataArr", dataArr_f);
	    request.addProperty("date", "123");
	    request.addProperty("obj", "456");
	    */
		   
                  
    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    envelope.dotNet = true;
    envelope.setOutputSoapObject(request);
    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
         
    try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
		} catch (HttpResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
    /*
    SoapPrimitive response = null;
		try {
			response = (SoapPrimitive)envelope.getResponse();
		} catch (SoapFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
    
    Object response = null;
//!!!!!!!!!!!!!!!!!!!!!!!!!array mode!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		response =envelope.bodyIn;
		try{
		      webResponse = response.toString();
		}catch(Exception es){
			webResponse="Error!!!!!!";
		}

		
		
		
		//webResponse = response.toString();
    
    //tv1.setText(webResponse);

    		Log.e("WEB!!!", webResponse);

			return webResponse;
	    }
    
    
    
    
    
    
    
    private void service_init() {
        Intent bindIntent = new Intent(this, UartService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
  
        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
    	 super.onDestroy();
        Log.d(TAG, "onDestroy()");
        
        try {
        	LocalBroadcastManager.getInstance(this).unregisterReceiver(UARTStatusChangeReceiver);
        } catch (Exception ignore) {
            Log.e(TAG, ignore.toString());
        } 
        unbindService(mServiceConnection);
        mService.stopSelf();
        mService= null;
       
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (!mBtAdapter.isEnabled()) {
            Log.i(TAG, "onResume - BT not enabled yet");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
 
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

        case REQUEST_SELECT_DEVICE:
        	//When the DeviceListActivity return, with the selected device address
            if (resultCode == Activity.RESULT_OK && data != null) {
                String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);
               
                Log.d(TAG, "... onActivityResultdevice.address==" + mDevice + "mserviceValue" + mService);
                //((TextView) findViewById(R.id.deviceName)).setText(mDevice.getName()+ " - connecting");
                mService.connect(deviceAddress);
                            

            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Bluetooth has turned on ", Toast.LENGTH_SHORT).show();

            } else {
                // User did not enable Bluetooth or an error occurred
                Log.d(TAG, "BT not enabled");
                Toast.makeText(this, "Problem in BT Turning ON ", Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
        default:
            Log.e(TAG, "wrong request code");
            break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
       
    }

    
    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  
    }

    @Override
    public void onBackPressed() {

            new AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("提示")
            .setMessage("确定退出吗？")
            .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
   	                finish();
                }
            })
            .setNegativeButton("取消", null)
            .show();

    }
}
