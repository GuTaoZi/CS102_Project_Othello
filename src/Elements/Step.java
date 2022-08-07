package Elements;

import UI.themes;

import javax.swing.*;
import java.awt.*;

public class Step
{
    public int[][] stepBoard;
    public int stepID;
    public Player player;
    public int color;
    public int x, y;
    public static int stepCount = 0;
    public int countBlack, countWhite;

    public Step(int[][] stepBoard, Player player, int color, int x, int y)
    {
        this.stepBoard = new int[8][8];
        for(int i = 0; i < 8; i++)
        {
            System.arraycopy(stepBoard[i], 0, this.stepBoard[i], 0, 8);
        }
        this.stepID = ++stepCount;
        this.player = player;
        this.color = color;
        this.x = x;
        this.y = y;
        this.countBlack = countColor(stepBoard, -1);
        this.countWhite = countColor(stepBoard, 1);
    }

    public int[][] getStepBoard()
    {
        return this.stepBoard;
    }

    @Override
    public String toString()
    {
        StringBuilder ans = new StringBuilder(stepID + "!" + player + "!" + color + "!" + x + "!" + y + "!" + countBlack + "!" + countWhite + "!");
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(this.stepBoard[i][j] == -1)
                    ans.append("b");
                else if(this.stepBoard[i][j] == 1)
                    ans.append("w");
                else
                    ans.append("z");
            }
        }
        return ans.toString();
    }

    public static int countColor(int[][] stepBoard, int color)
    {
        int sum = 0;
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(stepBoard[i][j] == color)
                    sum++;
            }
        }
        return sum;
    }

    public Step(String info) throws NumberFormatException
    {
        try
        {
            String[] tmp = info.split("!");
            this.stepID = Integer.parseInt(tmp[0]);
            this.player = new Player(tmp[1], "0");
            this.color = Integer.parseInt(tmp[2]);
            this.x = Integer.parseInt(tmp[3]);
            this.y = Integer.parseInt(tmp[4]);
            this.countBlack = Integer.parseInt(tmp[5]);
            this.countWhite = Integer.parseInt(tmp[6]);
            //System.out.println(tmp[7]);
            int[][] temp = new int[8][8];
            for(int i = 0; i < 8; i++)
            {
                for(int j = 0; j < 8; j++)
                {
                    char ch = tmp[7].charAt(i * 8 + j);
                    if(ch == 'b')
                        temp[i][j] = -1;
                    else if(ch == 'w')
                        temp[i][j] = 1;
                    else if(ch == 'z')
                        temp[i][j] = 0;
                    else
                    {
                        JOptionPane.showMessageDialog(null, "存档信息有误，可能显示不正确!", "出错", JOptionPane.OK_CANCEL_OPTION);
                    }
                }
            }
            this.stepBoard = temp;
        }
        catch(NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null, "存档信息有误，可能显示不正确!", "出错", JOptionPane.OK_CANCEL_OPTION);
        }
    }
}