import java.io.Serializable;
import java.io.*;
import java.util.*;
import java.net.*;
public class MainMessage implements Serializable
{
    
    MSG messageID; // Id of the message REQUEST = 0, REPLY = 1, LEAVE =2, PUT = 3
    int myPort;          
    String myIP;          
    String nextIP;    
    int nextPort;       
    String idSource;
    String idDest;
    String text;
    
    public MainMessage()
    {
        
    }
    // LEAVE   : myIP, myPort, nextIP, nextPort
    public MainMessage(String ip, int port, String nxtIp, int nxtPort)
    {
        messageID = MSG.LEAVE;
        myIP = ip;
        myPort = port;
        nextIP = nxtIp;
        nextPort = nxtPort;
    }
    // REQUEST :  myIP, myPort
     public MainMessage(String ip, int port)
    {
        messageID = MSG.REQUEST;
        myIP = ip;
        myPort = port;
    }
    // REPLY     :  nextPort, nextIP
     public MainMessage(int nxtPort, String nxtIp)
    {
        messageID = MSG.REPLY;
        nextPort = nxtPort;
        nextIP = nxtIp;
    }
    // PUT        : idSource, idDest, text
     public MainMessage(String idSrc, String idDes, String txt)
    {
        messageID = MSG.PUT;
        idSource = idSrc;
        idDest = idDes;
        text = txt;
    }
    
    public MainMessage (MSG msg, String Ip, int Port)
    {
        this.messageID = msg;
        myIP = Ip;
        myPort = Port;
    }
}
