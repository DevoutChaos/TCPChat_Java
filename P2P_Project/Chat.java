import java.io.Serializable;
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.Scanner;
public class Chat
{
    String myId;
    int myPort;          
    String myIP;          
    String nextIP;    
    int nextPort;       
    private class Server implements Runnable
    {
        //Server Run
        public void run()
        {
            //...
            ServerSocket servSock = null;
            try 
            {
                servSock = new ServerSocket ( myPort );
            } 
            catch (IOException e1) 
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } 
            while ( true )
            {
                MainMessage reply = null;
                Socket clntSock;
                try 
                {
                    clntSock = servSock.accept ();
                    ObjectInputStream ois = new ObjectInputStream(clntSock.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(clntSock.getOutputStream());
                    MainMessage m = (MainMessage)ois.readObject (); // Handle Messages
                    switch(m.messageID)
                    {
                        case REQUEST:
                        System.out.println("'REQUEST' received: "+m.myPort);
                        reply = new MainMessage(nextPort, nextIP);
                        nextIP = m.myIP;
                        nextPort = m.myPort;
                        oos.writeObject(reply);
                        break;
                        
                        case PUT:
                        System.out.println("'PUT' received from: " + m.idSource);
                        if((myId).equals(m.idDest))
                        {
                            System.out.println(m.text);
                        }
                        else if((myId).equals(m.idSource))
                        {
                            System.out.println("Message was not received!");
                            reply = new MainMessage(myPort, myIP);
                            oos.writeObject(reply);
                        }
                        else
                        {
                            reply = new MainMessage(m.idSource, m.idDest, m.text);
                            oos.writeObject(reply);
                        }
                        break;
                        
                        case LEAVE:
                        System.out.println("'LEAVE' received: " + m.myPort);
                        if(nextIP == m.myIP && nextPort == m.myPort)
                        {
                            nextIP = m.nextIP;
                            nextPort = m.nextPort;
                        }
                        if(myIP == m.myIP && myPort == m.myPort)
                        {
                            return;
                        }
                        oos.writeObject(m);
                        break;
                        
                        default:
                        break;
                    }
                    //. . .if m.message id = request
                    //construct new message
                    //if put
                    //send to next
                    clntSock.close();
                }
                catch (IOException e) 
                {
                    e.printStackTrace();

                }   // Get client   connections
                catch (ClassNotFoundException e) 
                {
                    e.printStackTrace();
                }
            }
        }
    }
    private class Client implements Runnable
    {
        //Client Run
        public void run()
        {
            // create a scanner so we can read the command-line input
            Scanner scanner = new Scanner(System.in);
            MainMessage m = null;
            String ip = null;
            int port = 0;
            while (true) 
            {
                // Read commands form the keyboard
                //request first
                //reply
                //put
                //leave

                //  prompt for the user's message
                System.out.println("Enter your message (REQUEST, PUT, LEAVE): ");

                // get their input as a String
                String msgInput = scanner.next();

                switch(msgInput)
                {
                    case "REQUEST":
                    //  prompt for the ip and port to connect to
                    System.out.println("Input IP to connect to:");
                    ip = scanner.next();
                    System.out.println("Input port to connect to:");
                    port = scanner.nextInt();
                    System.out.println("Connecting on specified port: " + port);
                    System.out.println("Sending Request...");
                    m = new MainMessage(myIP, myPort);
                    break;

                    case "PUT":
                    String receiverId;
                    String msgToSend;
                    System.out.println("Input the recipient of message: ");
                    receiverId = scanner.next();
                    System.out.println("Input message: ");
                    //One word only
                    msgToSend = scanner.next();
                    m = new MainMessage(myId, receiverId, msgToSend);
                    break;
                    
                    case"LEAVE":
                    System.out.println("Leaving chat");
                    m = new MainMessage(myIP, myPort, nextIP, nextPort);
                    nextIP = myIP;
                    nextPort = myPort;
                    System.out.println("Successfully left the chat");
                    return;
                }
                Socket socket;
                try 
                {
                    socket = new Socket(ip,port);//ip and port to connect to
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    oos.writeObject (m);
                    try
                    {
                        MainMessage received = (MainMessage)ois.readObject();
                        if(received.messageID.REPLY != null)
                        {
                            nextIP = received.nextIP;
                            nextPort = received.nextPort;
                        }
                    }
                    catch(ClassNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    socket.close ();
                } 
                catch (IOException e) 
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }
    public Chat(String Id, int port, String Ip)
    {
        myIP = Ip;
        myPort = port;
        myId = Id;
        nextPort = port;
        nextIP = Ip;
        
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
        if (args.length != 2 ) 
        {  
            System.out.println("(err1)Parameter: <id> <port>");
            return;
        }
        try
        {
            InetAddress ip = InetAddress.getLocalHost();
            Chat chat = new Chat(args[0], Integer.parseInt(args[1]), ip.getHostAddress());       
        }
        catch(UnknownHostException p)
        {
            System.out.println("(err2)Parameter: <id> <port>");
        }     
    }

}