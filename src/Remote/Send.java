package Remote;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;

public class Send implements Runnable
{
    DataOutputStream dos;
    boolean t = true;

    public Send(Socket cl)
    {
        try
        {
            dos = new DataOutputStream(cl.getOutputStream());
        }
        catch(IOException e)
        {
            t = false;
            try
            {
                dos.close();
                cl.close();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public String getMessage()
    {
        String str = "";
        if(Message.message.isEmpty())
        {
            return "";
        }
        if(!Message.message.isEmpty()&&Message.message.charAt(0)=='$')
        {
            str = Message.message+"@"+Message.MyCol;
        }
        if(!Message.message.isEmpty()&&Message.message.charAt(0)=='P')
        {
            str=Message.message;
        }
        Message.message="";
        if(!str.equals(""))
        {
            System.out.println("send:"+str);
        }
        return str;
    }

    public void send(String str)
    {
        try
        {
            dos.writeUTF(str);
            dos.flush();
        }
        catch(IOException e)
        {
            try
            {
                dos.close();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();

            }
            t = false;
        }
    }

    @Override
    public void run()
    {
        while(t)
        {
            send(getMessage());
        }
    }
}