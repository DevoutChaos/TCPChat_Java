import java.io.Serializable;
import java.io.*;
import java.util.*;
import java.net.*;
public class Chat
{
    String myId;
    int myPort;          
    String myIP;          
    String nextIP;    
    int nextPort;       
    String idSource;
    String idDest;
    String text;
    private class Server implements Runnable
    {
        //Server Run
        public void run()
        {
            //...
            ServerSocket servSock = null;
            try {
                servSock = new ServerSocket ( myPort );
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } 
            while ( true )
            {
                Socket clntSock;
                try {
                    clntSock = servSock . accept ();
                    ObjectInputStream   ois = new ObjectInputStream ( clntSock . getInputStream ());
                    ObjectOutputStream oos = new ObjectOutputStream( clntSock . getOutputStream ());
                    MainMessage m = (MainMessage) ois.readObject (); // Handle Messages
                    //m.messageID is null
                    //System.out.println(m);
                    switch(m.messageID)
                    {
                        case REQUEST:
                        System.out.println("Request Received: "+m.myPort);
                        break;
                        case PUT:
                        break;
                        case LEAVE:
                        break;
                        default:
                        break;
                    }
                    //. . .if m.message id = request
                    //construct new message
                    //if put
                    //send to next
                    clntSock . close ();
                    //      
                } 
                catch (IOException e) 
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }   // Get client   connections
                catch (ClassNotFoundException e) 
                {
                    // TODO Auto-generated catch block
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
            while ( true ) 
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
                    //System.out.println("Input port to connect to:");
                    //port = scanner.nextInt();
                    System.out.println("Connecting on specified port: "+myPort);
                    port = myPort;
                    System.out.println("Sending Request...");
                    //create REQUEST message with your ip and port
                    m = new MainMessage(myIP, myPort);
                    break;

                    case "PUT":
                    break;
                    case"LEAVE":
                    break;
                }

                //create an id in command line
                ///user inputs request
                //Prepare message m

                Socket socket;
                try 
                {
                    socket = new Socket(ip,port);//ip and port to connect to
                    ObjectOutputStream oos = new ObjectOutputStream( socket . getOutputStream ());
                    ObjectInputStream	ois = new ObjectInputStream ( socket . getInputStream ());
                    oos.writeObject (m);
                    ois.read (); 
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
        //Initialisation of the peer
        Thread server = new Thread(new Server());
        Thread client = new Thread(new Client());
        server.start();
        client.start();

        myIP = Ip;
        myPort = port;
        myId = Id;
        nextPort =port;
        nextIP = Ip;
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
            System.out.println("1Parameter: <id> <port>");
            return;
        }
        //get Id argument 
        String Id = args[0];
        //get port argument
        int port = Integer.parseInt(args[1]);
        try
        {
            InetAddress ip = InetAddress.getLocalHost();
            Chat chat = new Chat(args[0], Integer.parseInt(args[1]), ip.getHostAddress());       
        }
        catch(UnknownHostException p)
        {
            System.out.println("2Parameter: <id> <port>");
        }     
    }

}