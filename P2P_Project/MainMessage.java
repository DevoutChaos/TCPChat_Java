import java.io.Serializable;
import java.io.*;
import java.util.*;
import java.net.*;
public class MainMessage
{
    public enum MSG
    {
         REQUEST, REPLY, LEAVE, PUT
    }
    MSG messageID;
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
        myIP = ip;
        myPort = port;
        nextIP = nxtIp;
        nextPort = nxtPort;
    }
    // REQUEST :  myIP, myPort
     public MainMessage(String ip, int port)
    {
        myIP = ip;
        myPort = port;
    }
    // REPLY     :  nextPort, nextIP
     public MainMessage(int nxtPort, String nxtIp)
    {
        nextPort = nxtPort;
        nextIP = nxtIp;
    }
    // PUT        : idSource, idDest, text
     public MainMessage(String idSrc, String idDes, String txt)
    {
        idSource = idSrc;
        idDest = idDes;
        text = txt;
    }
}
