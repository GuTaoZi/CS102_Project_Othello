package Elements;

import javax.swing.*;

public class Player
{
    public String playerName;
    public int avatarID;
    public int gameCount, winCount, scoreCount;
    public static int playerCnt = 0;

    public Player(String playerName)
    {
        this.playerName = playerName;
        this.avatarID = 0;
        this.gameCount = 0;
        this.winCount = 0;
        this.scoreCount = 0;
    }


    public Player(String info, String description) throws NumberFormatException
    {
        try
        {
            String[] tmp = info.split("<");
            this.playerName = tmp[0];
            this.avatarID = Integer.parseInt(tmp[1]);
            this.gameCount = Integer.parseInt(tmp[2]);
            this.winCount = Integer.parseInt(tmp[3]);
            this.scoreCount = Integer.parseInt(tmp[4]);
        }
        catch(NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null, "存档信息有误，可能显示不正确!", "出错", JOptionPane.OK_CANCEL_OPTION);
        }
    }

    public double getWinRate()
    {
        return gameCount == 0 ? 0 : (double) winCount / gameCount;
    }

    @Override
    public String toString()
    {
        return this.playerName + "<" + this.avatarID + "<" + this.gameCount + "<" + this.winCount + "<" + this.scoreCount;
    }
}