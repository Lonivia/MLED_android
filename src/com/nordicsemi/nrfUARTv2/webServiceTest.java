package com.nordicsemi.nrfUARTv2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.StrictMode;
import android.util.Log;

public class webServiceTest {


	
	 public  String getProvince(){
		 
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
		 
		 
		 
	        List<String> provinces=new ArrayList<String>();
	        //String str="";
	        String targetNameSpace = "urn:SoapTestControllerwsdl";
	        String getSupportProvince = "testSoap1";
	        String WSDL="http://121.40.53.156/mDoctor/index.php?r=soapTest/patientInfo&ws=1";
	        
//	        String targetNameSpace = "http://WebXml.com.cn/";
//	        String getSupportProvince = "getSupportProvince";
//	        String WSDL="http://webservice.webxml.com.cn/WebServices/WeatherWebService.asmx?wsdl";
	        
	        
	        SoapObject soapObject=new SoapObject(targetNameSpace, getSupportProvince);
	        //request.addProperty("参数", "参数值");调用的方法参数与参数值（根据具体需要可选可不选）
	        
	        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER10);
	        envelope.dotNet=true;
	        envelope.setOutputSoapObject(soapObject);//envelope.bodyOut=request;	        
	        
	        //HttpTransportSE  httpTranstation=new HttpTransportSE(WSDL);
	        HttpTransportSE httpTranstation=new HttpTransportSE(WSDL);
	        try {
	            
	            httpTranstation.call(targetNameSpace+"#"+getSupportProvince, envelope);
	            SoapPrimitive result=(SoapPrimitive)envelope.getResponse();
	            
	            //String result=(String)envelope.getResponse();
	            Log.i("aa", result.toString());
	            return result.toString();
	            //下面对结果进行解析，结构类似json对象
	            //str=(String) result.getProperty(6).toString();
	            
//	            int count=result.getPropertyCount();
//	            for(int index=0;index<count;index++){
//	                provinces.add(result.getProperty(index).toString());
//	            }
	            
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (XmlPullParserException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } 
	        //return provinces;
			return null;
	    }
	 
public  String checkVersion(){
		 
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
		 
		 
		 
	        List<String> provinces=new ArrayList<String>();
	        //String str="";
	        String targetNameSpace = "urn:SoapTestControllerwsdl";
	        String getSupportProvince = "checkVersion";
	        String WSDL="http://121.40.53.156/mDoctor/index.php?r=soapTest/patientInfo&ws=1";
	        
//	        String targetNameSpace = "http://WebXml.com.cn/";
//	        String getSupportProvince = "getSupportProvince";
//	        String WSDL="http://webservice.webxml.com.cn/WebServices/WeatherWebService.asmx?wsdl";
	        
	        
	        SoapObject soapObject=new SoapObject(targetNameSpace, getSupportProvince);
	        soapObject.addProperty("ver", "123");
	        
	        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER10);
	        envelope.dotNet=true;
	        envelope.setOutputSoapObject(soapObject);//envelope.bodyOut=request;	        
	        
	        //HttpTransportSE  httpTranstation=new HttpTransportSE(WSDL);
	        HttpTransportSE httpTranstation=new HttpTransportSE(WSDL);
	        try {
	            
	            httpTranstation.call(targetNameSpace+"#"+getSupportProvince, envelope);
	            SoapPrimitive result=(SoapPrimitive)envelope.getResponse();
	            
	            //String result=(String)envelope.getResponse();
	            Log.e("aa", result.toString());
	            return result.toString();
	            //下面对结果进行解析，结构类似json对象
	            //str=(String) result.getProperty(6).toString();
	            
//	            int count=result.getPropertyCount();
//	            for(int index=0;index<count;index++){
//	                provinces.add(result.getProperty(index).toString());
//	            }
	            
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (XmlPullParserException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } 
	        //return provinces;
			return null;
	    }



public  void uploadData(int[][][] adsend,int step,int track){
	 
	/*
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
	 */
	 
	 
       List<String> provinces=new ArrayList<String>();
       //String str="";
       String targetNameSpace = "urn:SoapTestControllerwsdl";
       String getSupportProvince = "uploadSampleData";
       String WSDL="http://121.40.53.156/mDoctor/index.php?r=soapTest/patientInfo&ws=1";

       
       
       SoapObject soapObject=new SoapObject(targetNameSpace, getSupportProvince);
       soapObject.addProperty("user", "gln2");
       soapObject.addProperty("pwd", "gln");
       soapObject.addProperty("sn", "1231");
	   String data=null;
	   for(int datai=0;datai<=2399;datai++){
		   data+=adsend[datai][track-1][step];
		   data+=",";
	   }
	   soapObject.addProperty("step",step);
	   soapObject.addProperty("track",track);
	   soapObject.addProperty("data", data);
	   Log.e("aa", "---------set property");
       
       SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER10);
       envelope.dotNet=true;
       envelope.setOutputSoapObject(soapObject);//envelope.bodyOut=request;	        
       
       //HttpTransportSE  httpTranstation=new HttpTransportSE(WSDL);
       HttpTransportSE httpTranstation=new HttpTransportSE(WSDL,10000);
       
       //MyAndroidHttpTransport transport = new MyAndroidHttpTransport(WSDL, 50000);
       Log.e("aa", "---------HTTP trans");
       try {
           
    	   httpTranstation.call(targetNameSpace+"#"+getSupportProvince, envelope);
           SoapPrimitive result=(SoapPrimitive)envelope.getResponse();
           
           //String result=(String)envelope.getResponse();
           Log.e("aa", result.toString());
           //return result.toString();
           //return result.toString();
           //下面对结果进行解析，结构类似json对象
           //str=(String) result.getProperty(6).toString();
           
//           int count=result.getPropertyCount();
//           for(int index=0;index<count;index++){
//               provinces.add(result.getProperty(index).toString());
//               Log.e("aa", result.getProperty(index).toString());
//           }
           
       } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       } catch (XmlPullParserException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       } 
       //return provinces;
		//return null;
   }

public  String setDiag(){
	
	/*
	 
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
              
      */        
	 
	 
	 
      List<String> provinces=new ArrayList<String>();
      //String str="";
      String targetNameSpace = "urn:SoapTestControllerwsdl";
      String getSupportProvince = "setDiag3";
      String WSDL="http://121.40.53.156/mDoctor/index.php?r=soapTest/patientInfo&ws=1";
      
//      String targetNameSpace = "http://WebXml.com.cn/";
//      String getSupportProvince = "getSupportProvince";
//      String WSDL="http://webservice.webxml.com.cn/WebServices/WeatherWebService.asmx?wsdl";
      
      
      SoapObject soapObject=new SoapObject(targetNameSpace, getSupportProvince);
      soapObject.addProperty("user", "gln");
      soapObject.addProperty("pwd", "gln");
      soapObject.addProperty("sn", "123");
      soapObject.addProperty("patientInfo.user", "system_patient");
      soapObject.addProperty("patientInfo.phone", "111111");
      soapObject.addProperty("patientInfo.pwd", "system_FecX593");
      soapObject.addProperty("patientInfo.age", "100");
      soapObject.addProperty("patientInfo.sex", "男");
      soapObject.addProperty("id", "123");
      soapObject.addProperty("question_result", "");

      
      SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER10);
      envelope.dotNet=true;
      envelope.setOutputSoapObject(soapObject);//envelope.bodyOut=request;	        
      
      //HttpTransportSE  httpTranstation=new HttpTransportSE(WSDL);
      HttpTransportSE httpTranstation=new HttpTransportSE(WSDL);
      try {
          
          httpTranstation.call(targetNameSpace+"#"+getSupportProvince, envelope);
          Log.e("aa", "finish net trans!");
          SoapPrimitive result=(SoapPrimitive)envelope.getResponse();
          //SoapObject result=(SoapObject) envelope.getResponse();
          
          //String result=(String)envelope.getResponse();
          Log.e("aa", "finish envelop!");
          
          return result.toString();
          //下面对结果进行解析，结构类似json对象
          //str=(String) result.getProperty(6).toString();
          
//          int count=result.getPropertyCount();
//          for(int index=0;index<count;index++){
//              provinces.add(result.getProperty(index).toString());
//          }
          
      } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      } catch (XmlPullParserException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      } catch(Exception e){
    	  
      }
      //return provinces;
		return null;
  }



public  String getNewestDiagResult(){
	 
	
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
               
               
	 
	 
	 
       List<String> provinces=new ArrayList<String>();
       //String str="";
       String targetNameSpace = "urn:SoapTestControllerwsdl";
       String getSupportProvince = "getNewestDiagResult";
       String WSDL="http://121.40.53.156/mDoctor/index.php?r=soapTest/patientInfo&ws=1";
       
//       String targetNameSpace = "http://WebXml.com.cn/";
//       String getSupportProvince = "getSupportProvince";
//       String WSDL="http://webservice.webxml.com.cn/WebServices/WeatherWebService.asmx?wsdl";
       
       
       SoapObject soapObject=new SoapObject(targetNameSpace, getSupportProvince);
       soapObject.addProperty("doctor.user", "system_patient");
       soapObject.addProperty("doctor.cpwd", "system_FecX593");
       soapObject.addProperty("id", "0");
       
       SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER10);
       envelope.dotNet=true;
       envelope.setOutputSoapObject(soapObject);//envelope.bodyOut=request;	        
       
       //HttpTransportSE  httpTranstation=new HttpTransportSE(WSDL);
       HttpTransportSE httpTranstation=new HttpTransportSE(WSDL);
       try {
           
           httpTranstation.call(targetNameSpace+"#"+getSupportProvince, envelope);
           SoapPrimitive result=(SoapPrimitive)envelope.getResponse();
           
           //String result=(String)envelope.getResponse();
           //Log.i("aa", result.toString());
           return result.toString();
           //下面对结果进行解析，结构类似json对象
           //str=(String) result.getProperty(6).toString();
           
//           int count=result.getPropertyCount();
//           for(int index=0;index<count;index++){
//               provinces.add(result.getProperty(index).toString());
//           }
           
       } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       } catch (XmlPullParserException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       } catch(Exception e){
    	   
       }
       //return provinces;
		return null;
   }


	
}
