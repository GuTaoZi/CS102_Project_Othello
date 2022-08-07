package System;

import Elements.Game;
import Elements.Player;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileSystem
{
    public FileSystem()
    {
    }

    public static void writeGame(Game game, int id)
    {
        PrintStream print;
        try
        {
            print = new PrintStream("saves\\Game-" + id + ".txt");
            print.print(game);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static Game readGame(int id)
    {
        File f = new File("saves\\Game-" + id + ".txt");
        if(!f.exists())
        {
            JOptionPane.showMessageDialog(null, "您输入的游戏不存在", "提示", JOptionPane.PLAIN_MESSAGE);
            return null;
        }
        Scanner in = null;
        try
        {
            in = new Scanner(f);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        assert in != null;
        return new Game(in.nextLine());
    }

    public static void writePlayer(Player player)
    {
        PrintStream print;
        try
        {
            print = new PrintStream("saves\\Player-" + player.playerName + ".txt");
            print.print(player);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static Player readPlayer(String playerName)
    {
        File f = new File("saves\\Player-" + playerName + ".txt");
        if(!f.exists())
        {
            JOptionPane.showMessageDialog(null, "您输入的玩家不存在", "提示", JOptionPane.PLAIN_MESSAGE);
            return null;
        }
        Scanner in = null;
        try
        {
            in = new Scanner(f);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        assert in != null;
        return new Player(in.nextLine(), "0");
    }

    public static boolean isPlayerExisted(String playerName)
    {
        File f = new File("saves\\Player-" + playerName + ".txt");
        return f.exists();
    }

    public static ArrayList<Game> getAllGame()
    {
        ArrayList<Game> gameList = new ArrayList<>();
        for(int i = 0; i < 5; i++)
        {
            gameList.add(readGame(i));
        }
        return gameList;
    }

    public static ArrayList<Player> getAllPlayer()
    {
        ArrayList<Player> playerList = new ArrayList<>();
        File readFile = new File("saves\\");
        File[] fileList = readFile.listFiles();
        if(fileList == null)
            return playerList;
        for(File i : fileList)
        {
            if(!i.isFile())
                continue;
            if(!i.getName().contains("Player-"))
                continue;
            String name = i.getName().replace("Player-", "").replace(".txt", "");
            playerList.add(readPlayer(name));
        }
        return playerList;
    }

    public static void main(String[] args)
    {
        Player ai = new Player("233");
        writePlayer(ai);
        System.out.println(readPlayer("233"));
    }
}