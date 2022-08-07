package Logic;

public class Operate
{
    private static final int[] dx = {0, 0, 1, -1, 1, 1, -1, -1};
    private static final int[] dy = {1, -1, 0, 0, 1, -1, 1, -1};

    public Operate()
    {
    }

    public static boolean checkIn(int x, int y)
    {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    // 查询所有敌人，仅在本文件内部使用
    private static int[][] findEnemy(int[][] Board, int color)
    {
        int[][] enemy = new int[105][3];
        enemy[0][0] = 0;
        for(int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++)
                if(Board[i][j] == -color)
                    enemy[++enemy[0][0]] = new int[]{0, i, j};
        return enemy;
    }

    // 查询某位置能否落子
//    public static boolean queryBoard(int[][] Board, int x, int y, int color)
//    {
//        if(!checkIn(x, y) || Board[x][y] != 0)
//            return false;
//        int Flag = 0;
//        int[][] b = findEnemy(Board, color);
//        for(int i = 1; i <= b[0][0]; i++)
//        {
//            if(Math.abs(b[i][1] - x) > 1 || Math.abs(b[i][2] - y) > 1)
//                continue;
//            for(int j = 0; j < 8; j++)
//                if(x + dx[j] == b[i][1] && y + dy[j] == b[i][2])
//                {
//                    for(int k = 1; checkIn(b[i][1] + k * dx[j], b[i][2] + k * dy[j]); k++)
//                    {
//                        int tx = b[i][1] + k * dx[j], ty = b[i][2] + k * dy[j];
//                        if(Board[tx][ty] == color)
//                        {
//                            Flag = 1;
//                            break;
//                        }
//                        else if(Board[tx][ty] == 0)
//                            break;
//                    }
//                }
//        }
//        return Flag == 1;
//    }

    // 查询所有能落子的位置
    public static boolean[][] queryAllValid(int[][] Board, int color)
    {
        boolean[][] flag = new boolean[8][8];
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                flag[i][j] = queryBoard(Board, i, j, color);
            }
        }
        return flag;
    }

    // 判断是否让手
    public static int nextPlayer(int[][] Board, int color)
    {
        boolean[][] valid = queryAllValid(Board, -color);
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(valid[i][j])
                {
                    return -color;
                }//对手有地方下，换颜色
            }
        }
        return color;
    }

    // 在某位置落子并更新棋盘
    public static int[][] flipBoard(int[][] Board, int x, int y, int color)
    {
        if(!queryBoard(Board, x, y, color))
            return Board;
        int[][] b = findEnemy(Board, color);
        for(int i = 1; i <= b[0][0]; i++)
        {
            if(Math.abs(b[i][1] - x) > 1 || Math.abs(b[i][2] - y) > 1)
                continue;
            for(int j = 0; j < 8; j++)
                if(x + dx[j] == b[i][1] && y + dy[j] == b[i][2])
                {
                    for(int k = 1, l = k; checkIn(b[i][1] + k * dx[j], b[i][2] + k * dy[j]); l = ++k)
                    {
                        int tx = b[i][1] + k * dx[j], ty = b[i][2] + k * dy[j];
                        if(Board[tx][ty] == color)
                        {
                            while(--l >= 0)
                                Board[b[i][1] + l * dx[j]][b[i][2] + l * dy[j]] = color;
                            break;
                        }
                        else if(Board[tx][ty] == 0)
                            break;
                    }
                }
        }
        Board[x][y] = color;
        return Board;
    }

    // 查询某位置能够翻转多少棋子
    public static int countFlip(int[][] Board, int x, int y, int color)
    {
        if(!checkIn(x, y) || Board[x][y] != 0)
            return 0;
        int sum = 1;
        int[][] b = findEnemy(Board, color);
        for(int i = 1; i <= b[0][0]; i++)
        {
            if(Math.abs(b[i][1] - x) > 1 || Math.abs(b[i][2] - y) > 1)
                continue;
            for(int j = 0; j < 8; j++)
                if(x + dx[j] == b[i][1] && y + dy[j] == b[i][2])
                {
                    for(int k = 1, l = k; checkIn(b[i][1] + k * dx[j], b[i][2] + k * dy[j]); l = ++k)
                    {
                        int tx = b[i][1] + k * dx[j], ty = b[i][2] + k * dy[j];
                        if(Board[tx][ty] == color)
                        {
                            while(--l >= 0)
                                sum++;
                            break;
                        }
                        else if(Board[tx][ty] == 0)
                            break;
                    }
                }
        }
        return sum;
    }

    // 判断游戏是否结束，并返回赢家对应颜色和双方棋子数之差
    // -1 - Black    1 - White    0 - 未结束    2 - 平局
    public static int[] judgeFinish(int[][] Board)
    {
        int sumBlack = 0, sumWhite = 0;
        boolean flag = false;
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(Board[i][j] == -1)
                    sumBlack++;
                else if(Board[i][j] == 1)
                    sumWhite++;
                else if(Board[i][j] == 0)
                    flag = true;
            }
        }
        if(nextPlayer(Board,-1)==-1&&nextPlayer(Board,1)==1)//都没法下,互相谦让
        {
            flag=false;
        }
        if(flag&&sumBlack!=0&&sumWhite!=0)
            return new int[]{0, sumBlack - sumWhite};
        else if(sumBlack > sumWhite)
            return new int[]{-1, sumBlack - sumWhite};
        else if(sumBlack < sumWhite)
            return new int[]{1, sumWhite - sumBlack};
        else
            return new int[]{2, 0};
    }


    public static int DIR[][] = {{1, 1}, {1, 0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}};

    public static boolean onBoard(int x, int y)
    {
        return x >= 0 && y >= 0 && x < 8 && y < 8;
    }

    public static boolean queryBoard(int[][] Board, int x, int y, int col)
    {
        boolean flag = false;
        if(!onBoard(x, y) || Board[x][y] != 0)
        {
            return false;
        }
        for(int d = 0; d < 8; d++)//8向查询
        {
            int NextX = x + DIR[d][0];
            int NextY = y + DIR[d][1];
            while(onBoard(NextX, NextY) && Board[NextX][NextY] == -col)
            {
                NextX += DIR[d][0];
                NextY += DIR[d][1];
            }//这个方向不是自己人就一直走，直到走不通/不是自己人
            //System.out.println(NextX+","+NextY);
            if(onBoard(NextX, NextY) && Board[NextX][NextY] == col)
            {
                //System.out.println("!");
                while(true)
                {
                    NextX -= DIR[d][0];
                    NextY -= DIR[d][1];
                    //System.out.println(":"+NextX+","+NextY);
                    if(NextX == x && NextY == y)
                    {
                        break;
                    }//回到起点，结束
                    return true;
                }
            }
        }
        return false;
    }//检查是否合法
}