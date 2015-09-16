public class Chat
{
    public enum MSG
    {
        PUT, REQUEST, REPLY, LEAVE
    }
    private class Server implements Runnable
    {
        public class MainMessage implements Serializable
        {
            MSG messageID;
            //...
            public MainMessage()
            {
                
            }
            
            public MainMessage(MSG msg, String Ip, int Port)
            {
                this.messageID = msg;
                this.ip = Ip;
                this.port = Port;
            }
            //...
        }
    }
    public void run()
    {
        //...
        serveSock = new ServerSocket(port);
        
        while (true)
        {
            Docket clntSock = servSock = servSock.accept();//Get client connections
            ObjectInputStream ois = new ObjectInputStream(clntSock.getInput);
            ObjectOutputStream oos = new ObjectOutputStream(clntSock.getOutput);
            MainMessage m = (MainMessage)ois.readObject();
            //Handle Messages
            //...
            clntSock.close();
        }
    }


    private class Client  implements Runnable
    {
        public void run()
        {
            while(true)
            {
                //Read commands from the keyboard
                //prepare message m
                Socket socket = new Socket(ip, port);
                ObjectOutputStream oos = new ObjectOutputStream(clntSock.getOutput);
                ObjectInputStream ois = new ObjectInputStream(clntSock.getInput);
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
        client.join();
        server.join();
    }
}