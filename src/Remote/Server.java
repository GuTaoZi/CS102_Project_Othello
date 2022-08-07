package Remote;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server
{
    static List<ClientInfo> clientInfoList = new ArrayList<>();

    public static void main(String[] args)
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(8888);
            while(true)
            {
                Socket socket = serverSocket.accept();
                ClientInfo yyy = new ClientInfo(socket);
                System.out.println("connected");
                Thread thr = new Thread(yyy);
                clientInfoList.add(yyy);
                thr.start();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
