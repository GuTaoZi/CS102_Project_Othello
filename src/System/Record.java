package System;

import Elements.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static System.GameSystem.*;

public class Record
{
    // 胜利场次排序
    public static ArrayList<Player> sortWinCount()
    {
        playerList.sort((p1, p2) -> p2.winCount - p1.winCount);
        return playerList;
    }

    // 胜率排序
    public static ArrayList<Player> sortWinRate()
    {
        playerList.sort((p1, p2) -> (int) (100000*p2.getWinRate() - 100000*p1.getWinRate()));
        return playerList;
    }

    // 得分排序
    public static ArrayList<Player> sortScoreCount()
    {
        playerList.sort((p1, p2) -> p2.scoreCount - p1.scoreCount);
        return playerList;
    }
}