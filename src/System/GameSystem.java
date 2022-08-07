package System;

import Elements.Game;
import Elements.Player;

import java.util.ArrayList;

public class GameSystem
{
    public static ArrayList<Player> playerList = new ArrayList<>();
    public static ArrayList<Game> gameList = new ArrayList<>();

    public GameSystem()
    {
    }

    public static ArrayList<Game> getGameList()
    {
        return gameList;
    }

    public static ArrayList<Player> getPlayerList()
    {
        return playerList;
    }

    public static boolean checkPlayer(String playerName)
    {
        for(Player player : playerList)
            if(player.playerName.equals(playerName))
                return true;
        return false;
    }

    public static boolean checkGame(int gid)
    {
        for(Game game : gameList)
            if(game.gameID == gid)
                return true;
        return false;
    }

    // 在游戏开始时调用此过程新建一个玩家
    public static boolean addPlayer(Player player)
    {
        if(playerList != null && !playerList.isEmpty())
        {
            if(checkPlayer(player.playerName))
                return false;
        }
        assert playerList != null;
        playerList.add(player);
        return true;
    }

    // 在游戏开始时调用此过程新建一个游戏
    public static boolean addGame(Game game)
    {
        if(gameList != null && !gameList.isEmpty())
        {
            if(checkGame(game.gameID))
                return false;
        }
        Player p1 = game.playerWhite, p2 = game.playerBlack;
        if(!checkPlayer(p1.playerName) || !checkPlayer(p2.playerName))
            return false;
        if (gameList!=null) gameList.add(game);
        return true;
    }

    public static void savesPlayer(Player player)
    {
        FileSystem.writePlayer(player);
    }

    public static void saveGame(Game game,int id)
    {
        FileSystem.writeGame(game,id);
    }
}