package com.example.service;
import org.ksoap2.SoapEnvelope; 
import org.ksoap2.serialization.PropertyInfo; 
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class CheckUserService 
{
public final String SOAP_ACTION = "http://tempuri.org/CheckUser";

public  final String OPERATION_NAME = "CheckUser"; 

public  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
String or="http://www.myastroleader.com/jaipurcab/Service1.asmx";
public  final String SOAP_ADDRESS = or;
public CheckUserService() 
{ 
}
public String Call(String CabNo,String Password)
{    
SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
PropertyInfo pi=new PropertyInfo();
pi.setName("cabno");
pi.setValue(CabNo);
pi.setType(String.class);
request.addProperty(pi);
pi=new PropertyInfo();
pi.setName("password");
pi.setValue(Password);
pi.setType(String.class);
request.addProperty(pi);
SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
SoapEnvelope.VER11);
envelope.dotNet = true;
envelope.setOutputSoapObject(request);
HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
Object response=null;
try
{
httpTransport.call(SOAP_ACTION, envelope);
response = envelope.getResponse();
}
catch(Exception e)
{
	e.printStackTrace();
	return "Error";	
}
return response.toString();
}
}