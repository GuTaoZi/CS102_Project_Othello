package Remote;

import java.io.IOException;
import java.net.Socket;

public class Client
{
    public static void main(String[] args)
    {
        try
        {
            Socket socket = new Socket("127.0.0.1", 8888);
            Send send = new Send(socket);
            Receive receive = new Receive(socket);
            Thread Send_thr = new Thread(send);
            Thread Rec_thr = new Thread(receive);
            Send_thr.start();
            Rec_thr.start();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
