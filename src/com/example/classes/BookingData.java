package com.example.classes;

public class BookingData {
    String Id,ClientName,Contact1,Contact2,MailId,SourcePoint,Sourcedate,SourceTime,Status,DestinationPoint,
    DestinationDate,DestinationTime,Landmark,Remarks,Price,Type;
    public BookingData(String Id,String ClientName,String Contact1,String Contact2,String MailId,String SourcePoint
            ,String Sourcedate,String SourceTime,String DestinationPoint,String DestinationDate,String DestinationTime
            ,String Landmark,String Remarks,String Status,String Price,String Type)
    {
        this.Id=Id;
        this.ClientName=ClientName;
        this.Contact1=Contact1;
        this.Contact2=Contact2;
        this.MailId=MailId;
        this.Sourcedate=Sourcedate;
        this.SourcePoint=SourcePoint;
        this.SourceTime=SourceTime;
        this.DestinationDate=DestinationDate;
        this.DestinationPoint=DestinationPoint;
        this.DestinationTime=DestinationTime;
        this.Landmark=Landmark;
        this.Remarks=Remarks;
        this.Status=Status;
        this.Price=Price;
        this.Type=Type;
    }
    public String Type() {
        return Type;
    }
    public String Price() {
        return Price;
    }
    public String Status() {
        return Status;
    }
    public String Remarks() {
        return Remarks;
    }
public String Id() {
    return Id;
}
public String getClientName() {
    return ClientName;
}
public String Contact1() {
    return Contact1;
}
public String Contact2() {
    return Contact2;
}
public String MailId() {
    return MailId;
}
public String getSourcedate() {
    return Sourcedate;
}
public String SourcePoint() {
    return SourcePoint;
}
public String getSourceTime() {
    return SourceTime;
}
public String DestinationDate() {
    return DestinationDate;
}
public String DestinationPoint() {
    return DestinationPoint;
}
public String DestinationTime() {
    return DestinationTime;
}
public String Landmark() {
    return Landmark;
}

}
