package com.nordicsemi.nrfUARTv2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;
import com.nordicsemi.MLED.R;


public class SelectActivity extends Activity {
	
    private BluetoothAdapter mBtAdapter = null;

	public static String addr1 ="D0:6A:00:F6:3C:83";
	public static String addr2 ="D5:F1:81:51:17:81";
	
	public static  String user_s="";
	public  static String obj_s="";
	

	public int item=0;
	
	public int choose=-1;
	
	private Button button1;
	private Button button2;

	private CheckBox chb1;
	
	
	CheckBox[] checkbox = new CheckBox[100];
	
	int i100;
	
	private Button buttonzhongyao;
	private Button buttonroulei;
	private Button buttonsiliao;
	private Button buttonshuiguo;
	private Button buttonshucai;
	private Button buttonhuaxian;
	TableLayout layout;
	
	private EditText edittextyhm;
	private EditText edittextpwd;
	
	String zhongyao[]={"人参","三七","山楂","川牛膝","川穹","马钱子","甘草","麻黄","骨碎补","乳香",
						"土鳖虫","桂枝","红花","苍术","广藿香","牛黄","百合","决明子","红景天","当归",
						"地黄","白芍","何首乌","芦荟","杜仲","两面针","阿胶","板蓝根","金银花","茯苓",
						"威灵仙","柴胡","独一味","独活","桃仁","夏枯草","益母草","党参","桑寄生","黄苓",
						"黄芪","银杏叶","葛根","黑芝麻","蒲公英","罂粟壳","槲寄生","麝香","蟾蜍",
						};
	String zhongyaopy[]={"renshen","sanqi","shanzha","chuanniuxi","chuanqiong","maqianzi","gancao","mahuang","gusuibu","ruxiang",
			"tubiechong","guizhi","honghua","cangshu","guanghuoxiang","niuhuang","baihe","juemingzi","hongjingtian","danggui",
			"dihuang","baishao","heshouwu","luhui","duzhong","liangmianzhen","ejiao","banlangen","jinyinhua","fuling",
			"weilingshan","chaihu","duyiwei","duhuo","taoren","xiakucao","yimucao","dangshen","sangjisheng","huangling",
			"huangqi","yinxingye","gegen","heizhima","pugongying","yingsuke","hujisheng","shexiang","chanchu",
			};


	
	final static String FOLDER = "/MLEDfile/";   
	//final static String FILENAME = "config";    
	final static String SUFFIX = ".mdz";
	//CSVWriter writer = null;

	//TODO write file! cover mode!
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
	
	//TODO read file!!!   
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
	            // TODO Auto-generated catch block   
	            e1.printStackTrace();   
	            Log.d("SelectLog", "Error: Input File not find!");   
	            return null;   
	        }   
	  
	        CharBuffer cb;   
	        try {   
	            cb = CharBuffer.allocate(fis.available());   
	        } catch (IOException e1) {   
	            // TODO Auto-generated catch block   
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
	            // TODO Auto-generated catch block   
	            e.printStackTrace();           
	        }   
	    }   
	    Log.d("SelectLog", "readFile filecontent = " + filecontent);   
	    return filecontent;   
	} 
	

    public static boolean isChineseChar(String str){
        boolean temp = false;
        Pattern p=Pattern.compile("[\u4e00-\u9fa5]"); 
        Matcher m=p.matcher(str); 
        if(m.find()){ 
            temp =  true;
        }
        return temp;
    }
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        button1=(Button) findViewById(R.id.button1);
        button2=(Button) findViewById(R.id.button2);
        //button3=(Button) findViewById(R.id.button3);
        chb1=(CheckBox) findViewById(R.id.checkBox1);
        //chb2=(CheckBox) findViewById(R.id.checkBox2);
        chb1.setEnabled(false);
        edittextyhm=(EditText)findViewById(R.id.editTextyonghuming);
        edittextpwd=(EditText)findViewById(R.id.editTextmima);
        
        buttonzhongyao=(Button) findViewById(R.id.buttonzhongyao);
    	buttonroulei=(Button) findViewById(R.id.buttonroulei);
    	buttonsiliao=(Button) findViewById(R.id.buttonsiliao);
    	buttonshuiguo=(Button) findViewById(R.id.buttonshuiguo);
    	buttonshucai=(Button) findViewById(R.id.buttonshucai);
    	buttonhuaxian=(Button) findViewById(R.id.buttonhuaxian);

    	layout = (TableLayout)findViewById(R.id.TableLayout01);
    	
    	obj_s="";
    	user_s="";
        //button2.setEnabled(false);

        if(readFile("MDZWconfig")!=null){
        	chb1.setText(readFile("MDZWconfig"));
        	addr1=readFile("MDZWconfig");
        	chb1.setEnabled(true);
        	chb1.setChecked(true);
        }
        if(readFile("MDZBconfig")!=null){

        }
        if(readFile("MDZWuser")!=null){
        	edittextyhm.setText(readFile("MDZWuser"));
        	user_s=readFile("MDZWuser");

        }
        if(readFile("MDZWx")!=null){
        	edittextpwd.setText(readFile("MDZWx"));

        }
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        
        Start();
    }
  
	
    public void Start() {
    	button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	if (!mBtAdapter.isEnabled()) {
                    Log.i("SelectLog", "onClick - BT not enabled yet");
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, 2);
                }
                else {

            	choose=1;
                Intent newIntent = new Intent(SelectActivity.this, DeviceListActivity.class);
    			startActivityForResult(newIntent, 1);
            	
                }
            	//button2.setEnabled(true);
            }
            
    	});
    	
    	
    	
    	button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	//if(chb1.isChecked() && chb2.isChecked()){
            	button2.setEnabled(false);
            	if(chb1.isChecked() && !edittextyhm.getText().toString().equals("") && !obj_s.equals("") && !isChineseChar(edittextyhm.getText().toString())){

            		if(MainActivity.UserLogin(edittextpwd.getText().toString()).equals("ok")){
            			Log.e("Waring!", "Please Select Right Device!");
                		AlertDialog.Builder builder = new Builder(SelectActivity.this);
                        builder.setMessage("登陆成功！");
                        builder.setTitle("提示");
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setPositiveButton("继续",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        
                                        
                                        user_s=edittextyhm.getText().toString();
                						writeFile(user_s,"MDZWuser");
                						writeFile(edittextpwd.getText().toString(),"MDZWx");
                						
                                        Intent intent = new Intent();
                                        //intent.setClass(WelcomeActivity.this, MainActivity.class);
                                        intent.setClass(SelectActivity.this, com.nordicsemi.nrfUARTv2.MainActivity.class);
                                        startActivity(intent);
                                        SelectActivity.this.finish();
                                    }
                                });
                        builder.create().show();
                        
            			
            		}else{
            			Log.e("Waring!", "Please Select Right Device!");
                		AlertDialog.Builder builder = new Builder(SelectActivity.this);
                        builder.setMessage("登录失败！请检查网络！");
                        builder.setTitle("提示");
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setPositiveButton("返回",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        button2.setEnabled(true);
                                    }
                                });
                        builder.create().show();
            		}
            						

            		
            		
            	}else if(edittextyhm.getText().toString().equals("")){
            		Log.e("Waring!", "Please Select Right Device!");
            		AlertDialog.Builder builder = new Builder(SelectActivity.this);
                    builder.setMessage("用户名不能为空");
                    builder.setTitle("提示");
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setPositiveButton("返回",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    button2.setEnabled(true);
                                }
                            });
                    builder.create().show();
            	}else if(isChineseChar(edittextyhm.getText().toString())){
            		Log.e("Waring!", "Please Select Right Device!");
            		AlertDialog.Builder builder = new Builder(SelectActivity.this);
                    builder.setMessage("用户名不能包含中文");
                    builder.setTitle("提示");
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setPositiveButton("返回",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    button2.setEnabled(true);
                                }
                            });
                    builder.create().show();
            	}
            	else if(obj_s.equals("")){
            		Log.e("Waring!", "Please Select Right Device!");
            		AlertDialog.Builder builder = new Builder(SelectActivity.this);
                    builder.setMessage("请选择需要测试的样品");
                    builder.setTitle("提示");
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setPositiveButton("返回",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    button2.setEnabled(true);
                                }
                            });
                    builder.create().show();
            	}else{
            		//Log.e("Waring!", "Please Select Right Device!");
            		AlertDialog.Builder builder = new Builder(SelectActivity.this);
                    builder.setMessage("请选择搜索到的设备");
                    builder.setTitle("提示");
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setPositiveButton("返回",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    button2.setEnabled(true);
                                }
                            });
                    builder.create().show();
            		
            	}
            	
            	
            	
            }
            
    	});
    	
    	for(i100=0;i100<100;i100++){
    		checkbox[i100]=new CheckBox(SelectActivity.this);
    		checkbox[i100].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  
                
    			int inow=i100;
                @Override  
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {  
                    if (isChecked) {  
                        for(int j=0;j<100;j++){
                        	checkbox[j].setChecked(false);
                        }
                        checkbox[inow].setChecked(true);
                        
                        
                        if(item==1){
                        	obj_s=zhongyaopy[inow];
                        }
                    }else {  
                        //System.out.println("swim is unChecked");  
                    	obj_s="";
                    }  
                }  
            });  
    	}
    	
    	
    	buttonzhongyao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	layout.removeAllViews();
            	
            	for(int i=0;i<zhongyao.length;i++){

            		checkbox[i].setText(zhongyao[i]);
            		layout.addView(checkbox[i]);
            	}
            	//button2.setEnabled(true);
            	item=1;
            }
            
    	});
    	
    	buttonroulei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	layout.removeAllViews();
            	
            	for(int i=0;i<=40;i++){
            		//checkbox[i]=new CheckBox(SelectActivity.this);
            		checkbox[i].setText("肉类---"+i);
            		layout.addView(checkbox[i]);
            	}
            	//button2.setEnabled(true);
            	item=2;
            }
            
    	});
    	
    	buttonsiliao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	layout.removeAllViews();
            	
            	for(int i=0;i<=30;i++){
            		//checkbox[i]=new CheckBox(SelectActivity.this);
            		checkbox[i].setText("饲料---"+i);
            		layout.addView(checkbox[i]);
            	}
            	//button2.setEnabled(true);
            	item=3;
            }
            
    	});
    	
    	buttonshuiguo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	layout.removeAllViews();
            	
            	for(int i=0;i<=60;i++){
            		//checkbox[i]=new CheckBox(SelectActivity.this);
            		checkbox[i].setText("水果---"+i);
            		layout.addView(checkbox[i]);
            	}
            	//button2.setEnabled(true);
            	item=4;
            }
            
    	});
    	
    	buttonshucai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	layout.removeAllViews();
            	
            	for(int i=0;i<=70;i++){
            		//checkbox[i]=new CheckBox(SelectActivity.this);
            		checkbox[i].setText("蔬菜---"+i);
            		layout.addView(checkbox[i]);
            	}
            	//button2.setEnabled(true);
            	item=5;
            }
            
    	});
    	
    	buttonhuaxian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	layout.removeAllViews();
            	
            	for(int i=0;i<=20;i++){
            		//checkbox[i]=new CheckBox(SelectActivity.this);
            		checkbox[i].setText("化纤---"+i);
            		layout.addView(checkbox[i]);
            	}
            	//button2.setEnabled(true);
            	item=6;
            }
            
    	});
    	
    	
    	
    	
    }
    
    
    
    
    
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK && data != null) {
            	
            	if(choose==1){
	                String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
	                if(data.getStringExtra(BluetoothDevice.EXTRA_NAME).equals("MLED")){
	                	addr1=deviceAddress;
		                chb1.setText(deviceAddress);
		                chb1.setEnabled(true);
		                writeFile(deviceAddress,"MDZWconfig");
	                }else{
	                	AlertDialog.Builder builder = new Builder(SelectActivity.this);
	                    builder.setMessage("您未选择正确的设备");
	                    builder.setTitle("提示");
	                    builder.setIcon(android.R.drawable.ic_dialog_alert);
	                    builder.setPositiveButton("返回",
	                            new DialogInterface.OnClickListener() {
	                                public void onClick(DialogInterface dialog, int which) {
	                                    dialog.dismiss();
	                                }
	                            });
	                    builder.create().show();
	                }
	                
            	}else if(choose==2){
            		String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
            		if(data.getStringExtra(BluetoothDevice.EXTRA_NAME).equals("MDZB")){
            			addr2=deviceAddress;
    	                
    	                writeFile(deviceAddress,"MDZBconfig");
	                }else{
	                	AlertDialog.Builder builder = new Builder(SelectActivity.this);
	                    builder.setMessage("您未选择正确的腰带设备");
	                    builder.setTitle("提示");
	                    builder.setIcon(android.R.drawable.ic_dialog_alert);
	                    builder.setPositiveButton("返回",
	                            new DialogInterface.OnClickListener() {
	                                public void onClick(DialogInterface dialog, int which) {
	                                    dialog.dismiss();
	                                }
	                            });
	                    builder.create().show();
	                }
            	}
            }
    } 
}
