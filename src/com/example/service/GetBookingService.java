package com.example.service;
import org.ksoap2.SoapEnvelope; 
import org.ksoap2.serialization.PropertyInfo; 
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class GetBookingService 
{
public final String SOAP_ACTION = "http://tempuri.org/GetBooking";

public  final String OPERATION_NAME = "GetBooking"; 

public  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
String or="http://www.myastroleader.com/jaipurcab/Service1.asmx";
public  final String SOAP_ADDRESS = or;
public GetBookingService() 
{ 
}
public String Call(String DriverId)
{
SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
PropertyInfo pi=new PropertyInfo();
pi.setName("DriverId");
pi.setValue(DriverId);
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