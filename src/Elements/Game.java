package Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Stack;

import System.*;
import Logic.*;

import javax.swing.*;

public class Game
{
    // gameMode表示模式，如人机
    // startTime记录开始的时间
    public String gameName;
    public int gameID;
    public int gameMode;
    public int gameDifficulty;
    public Player playerBlack, playerWhite;
    public String startTime;
    public int[][] Board;
    public ArrayList<Step> gameStep;
    public static int gameCount = 0;
    public Stack<Step> deleteStep;

    // 新建游戏
    public Game(String gameName, Player player1, Player player2, int gameMode, int gameDifficulty)
    {
        this.gameName = gameName;
        this.gameMode = gameMode;
        this.gameID = ++gameCount;
        this.playerBlack = player1;
        this.playerWhite = player2;
        this.startTime = getTime();
        this.gameDifficulty = gameDifficulty;
        this.Board = new int[8][8];
        initBoard();
        gameStep = new ArrayList<>();
        deleteStep = new Stack<>();
    }

    public void initBoard()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                Board[i][j] = 0;
            }
        }
        Board[3][3] = Board[4][4] = 1;
        Board[3][4] = Board[4][3] = -1;
    }

    public void printBoard()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                System.out.print(Board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // 玩家落子
    public void addStep(Player player, int color, int x, int y)
    {
        if(!Operate.queryBoard(Board, x, y, color))
        {
            return;
        }
        Operate.flipBoard(Board, x, y, color);
        Step step = new Step(Board, player, color, x, y);
        gameStep.add(step);
    }

    // 悔棋
    public boolean deleteStep()
    {
        if(gameStep.size() == 0)
            return false;
        deleteStep.push(gameStep.get(gameStep.size() - 1));
        gameStep.remove(gameStep.size() - 1);
        this.Board = gameStep.get(gameStep.size() - 1).getStepBoard();
        return true;
    }

    // 撤销悔棋
    public boolean antiDelete()
    {
        if(deleteStep.empty())
            return false;
        gameStep.add(deleteStep.pop());
        this.Board = gameStep.get(gameStep.size() - 1).getStepBoard();
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder ans = new StringBuilder(gameName + ">" + gameMode + ">" + gameID + ">" + playerBlack + ">" + playerWhite + ">" + startTime + ">" + gameDifficulty + ">");
        for(Step i : gameStep)
        {
            ans.append(i).append(">");
        }
        return ans.toString();
    }

    public Game(String info) throws NumberFormatException
    {
        try
        {
            String[] tmp = info.split(">");
            this.gameName = tmp[0];
            this.gameMode = Integer.parseInt(tmp[1]);
            this.gameID = Integer.parseInt(tmp[2]);
            this.playerBlack = new Player(tmp[3], "0");
            this.playerWhite = new Player(tmp[4], "0");
            this.startTime = tmp[5];
            this.gameDifficulty = Integer.parseInt(tmp[6]);
            this.gameStep = new ArrayList<>();
            for(int i = 7; i < tmp.length; i++)
            {
                this.gameStep.add(new Step(tmp[i]));
            }
            this.Board = this.gameStep.get(gameStep.size() - 1).stepBoard;
        }
        catch(NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null, "存档信息有误，可能显示不正确!", "出错", JOptionPane.OK_CANCEL_OPTION);
        }
    }

    // 获取当前时间
    public String getTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Calendar calendar = Calendar.getInstance();
        return formatter.format(calendar.getTime());
    }

    public static void main(String[] args)
    {
        Game gametest = FileSystem.readGame(1);
        System.out.println(gametest.gameName);
        System.out.println(gametest.startTime);
        for(Step i : gametest.gameStep)
        {
            System.out.println("------------");
            System.out.println("Step " + i.stepID + ":   Color = " + i.color);
            System.out.println("Click (" + i.x + "," + i.y + ")");
            for(int j = 0; j < 8; j++)
            {
                for(int k = 0; k < 8; k++)
                {
                    System.out.printf("%d ", i.stepBoard[j][k]);
                }
                System.out.println();
            }
        }
    }
}
