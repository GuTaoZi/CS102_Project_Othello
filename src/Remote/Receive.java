package Remote;

import Elements.Player;
import UI.LaunchPanel;
import UI.RemoteGamePanel;
import UI.ToolKit;
import jdk.jfr.events.TLSHandshakeEvent;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class Receive implements Runnable
{
    DataInputStream dis;
    boolean t = true;

    public Receive(Socket cl)
    {
        try
        {
            dis = new DataInputStream(cl.getInputStream());
        }
        catch(IOException e)
        {
            t = false;
            try
            {
                cl.close();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public String getMessage()
    {
        String str = "";
        try
        {
            str = dis.readUTF();
        }
        catch(IOException e)
        {
            t = false;
            try
            {
                dis.close();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return str;
    }

    @Override
    public void run()
    {
        while(true)
        {
            String msg = getMessage();
            if(Objects.equals(msg, ""))
            {
                return;
            }
            if(msg.charAt(0) == '$')
            {
                String[] info = msg.split("@");
                info[0] = info[0].substring(1);
                Message.YourCol = Integer.parseInt(info[1]);
                if(Message.MyCol * Message.YourCol == -1)
                {
                    if(Message.YourCol == -1)
                    {
                        LaunchPanel.blackPlayer = new Player(info[0], "From Server");
                    }
                    else
                    {
                        LaunchPanel.whitePlayer = new Player(info[0], "From Server");
                    }
                    LaunchPanel.Start.setBackground(ToolKit.bgc);
                    LaunchPanel.Start.setText("开始游戏");
                }
            }
            if(msg.charAt(0) == 'P')
            {
                String[] info = msg.substring(1).split(",");
                Message.x = Integer.parseInt(info[0]);
                Message.y = Integer.parseInt(info[1]);
                RemoteGamePanel.OppoMove();
            }
        }
    }
}
