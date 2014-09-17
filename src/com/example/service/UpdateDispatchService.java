package com.example.service;
import org.ksoap2.SoapEnvelope; 
import org.ksoap2.serialization.PropertyInfo; 
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class UpdateDispatchService 
{
public final String SOAP_ACTION = "http://tempuri.org/UpdateDispatch";

public  final String OPERATION_NAME = "UpdateDispatch"; 

public  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
String or="http://www.myastroleader.com/jaipurcab/Service1.asmx";
public  final String SOAP_ADDRESS = or;
public UpdateDispatchService() 
{ 
}
public String Call(String Id)
{
SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
PropertyInfo pi=new PropertyInfo();
pi.setName("Id");
pi.setValue(Id);
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