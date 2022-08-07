package Remote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ClientInfo implements Runnable
{
    DataOutputStream dos;
    DataInputStream dis;
    boolean flag = true;
    Socket clientSocket;

    public ClientInfo(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
        try
        {
            dis = new DataInputStream(clientSocket.getInputStream());
            dos = new DataOutputStream(clientSocket.getOutputStream());
        }
        catch(IOException e)
        {
            flag = false;
            try
            {
                dis.close();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
            try
            {
                dos.close();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    String receive()
    {
        String str="";
        try
        {
            str =dis.readUTF();
        }
        catch(IOException e)
        {
            flag=false;
            try
            {
                dis.close();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
            Server.clientInfoList.remove(this);
        }
        return str;
    }

    void send(String str)
    {
        if(str==null||str.isEmpty())
        {
            return;
        }
        try
        {
            dos.writeUTF(str);
            dos.flush();
        }
        catch(IOException e)
        {
            flag=false;
            try
            {
                dos.close();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
            Server.clientInfoList.remove(this);
        }
    }

    void transmit(List<ClientInfo> src)
    {
        String str=receive();
        //System.out.println("trans!"+str);
        for(ClientInfo it: src)
        {
            if(it==this)
            {
                continue;
            }
            it.send(str);
        }
    }

    @Override
    public void run()
    {
        while(flag)
        {
            transmit(Server.clientInfoList);
        }
    }
}
