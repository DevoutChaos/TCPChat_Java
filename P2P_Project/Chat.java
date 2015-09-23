import java.io.Serializable;
import java.io.*;
import java.util.*;
import java.net.*;
public class Chat
{
    private class Server implements Runnable
    {
        //Server Run
        public void run()
        {
            //...
            int port;
            ServerSocket servSock;
            servSock = new ServerSocket(port);
            while (true)
            {
                Socket clntSock = servSock.accept();//Get client connections
                ObjectInputStream ois = new ObjectInputStream(clntSock.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(clntSock.getOutputStream());
                MainMessage m = (MainMessage)ois.readObject();
                //Handle Messages
                //...
                clntSock.close();
            }
        }
    }
    private class Client implements Runnable
    {
        //Client Run
        public void run()
        {
            String ip;
            int port;
            while(true)
            {
                //Read commands from the keyboard
                //prepare message m
                Socket socket = new Socket(ip, port);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                oos.writeObject(m);
                ois.read();
                socket.close();
            }
        }
    }
    public Chat(String Id, int port)
    {
        //Initialisation of the peer
        Thread server = new Thread(new Server());
        Thread client = new Thread(new Client());
        server.start();
        client.start();
        try
        {
            client.join();
        }
        catch (InterruptedException e)
        {
            //TODO auto generated catch block
            e.printStackTrace();
        }
        try
        {
            server.join();
        }
        catch (InterruptedException e)
        {
            //TODO auto generated catch block
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        if (args.length < 2 ) 
         {  
            System.out.println("Parameter: <id> <port>");
            return;
        }
        try
        {
            InetAddress ip = InetAddress.getLocalHost();
            Chat chat = new Chat(args[0], Integer.parseInt(args[1]), ip.getHostAddress());       
        }
        catch(UnknownHostException p)
        {
            System.out.println("Parameter: <id> <port>");
        }     
    }
}