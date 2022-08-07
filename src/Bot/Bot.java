package Bot;

import Elements.*;
import Logic.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bot {
    public static int[][] weight = {
            {65, -3, 6, 4, 4, 6, -3, 65},
            {-3, -29, 3, 1, 1, 3, -29, -3},
            {6, 3, 5, 3, 3, 5, 3, 6},
            {4, 1, 3, 1, 1, 3, 1, 4},
            {4, 1, 3, 1, 1, 3, 1, 4},
            {6, 3, 5, 3, 3, 5, 3, 6},
            {-3, -29, 3, 1, 1, 3, -29, -3},
            {65, -3, 6, 4, 4, 6, -3, 65},
    };

    public static int[][] DIR = {{1, 1}, {1, 0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}};

    public static int[] Greedy(int[][] Board, int col) {
        boolean[][] f = Operate.queryAllValid(Board, col);
        int hi = -0x7fffff;
        int[] best = {-1, -1};
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (f[i][j] && weight[i][j] > hi) {
                    hi = weight[i][j];
                    best[0] = i;
                    best[1] = j;
                }
            }
        }
        return best;
    }

    public static int[] lowerGreedy(int[][] Board, int col) {
        int count = -0x7fffff;
        int[] best = {-1, -1};
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int tmp = Operate.countFlip(Board, i, j, col);
                if (tmp > count) {
                    count = tmp;
                    best = new int[]{i, j};
                }
            }
        }
        return best;
    }


    public static int[] Random(int[][] Board, int col) {
        Random random = new Random();
        boolean[][] flag = Operate.queryAllValid(Board, col);
        ArrayList<int[]> valid = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (flag[i][j]) {
                    valid.add(new int[]{i, j});
                }
            }
        }
        if (valid.isEmpty())
            return new int[]{-1, -1};
        else
            return valid.get(valid.size() == 1 ? 0 : random.nextInt(valid.size() - 1));
    }

    public static int[] search(int[][] Board, int col) {
        Max = -0x7fffff - 1;
        boolean[][] flag = Operate.queryAllValid(Board, col);
        ArrayList<int[]> listValid = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (flag[i][j]) listValid.add(new int[]{i, j});
            }
        }
        int t = 0;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                t += Board[i][j] * Board[i][j];
        for (int[] node : listValid) {
            int[][] tmpBoard = new int[8][8];
            for (int k = 0; k < 8; k++) {
                System.arraycopy(Board[k], 0, tmpBoard[k], 0, 8);
            }
            tmpBoard = Operate.flipBoard(tmpBoard, node[0], node[1], col).clone();
            int depth;
            if (t<=20) depth=5;
            else if (t<=40) depth=6;
            else depth=7;
            int x = MinMaxSearch(tmpBoard, col,depth, -0x7fffff, 0x7fffff, true);
            if (x > Max) {
                Max = x;
                MinMaxSearchAnswer = node;
            }
        }
        return MinMaxSearchAnswer.clone();
    }

    public static int[] MinMaxSearchAnswer = new int[]{-1, -1};
    public static int Max;

    public static int MinMaxSearch(int[][] Board, int col, int depth, int alpha, int beta, boolean MaxPlayer) {
        //if (Max>=beta) return beta;
        int[] c = Operate.judgeFinish(Board);
        if (depth == 0 || c[0]!=0) {
            int t = 0;
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++)
                    t += Board[i][j] * Board[i][j];
            int weightBlack = 0, weightWhite = 0;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (Board[i][j] == -1) weightBlack += t <= 40 ? weight[i][j] : 1;
                    else if (Board[i][j] == 1) weightWhite += t <= 40 ? weight[i][j] : 1;
                }
            }
            if (MaxPlayer) {
                if (col == -1) return weightBlack - weightWhite;
                else return weightWhite - weightBlack;
            } else {
                if (col == 1) return weightBlack - weightWhite;
                else return weightWhite - weightBlack;
            }
        }
        boolean[][] flag = Operate.queryAllValid(Board, col);
        ArrayList<int[]> listValid = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (flag[i][j]) listValid.add(new int[]{i, j});
            }
        }
        for (int[] node : listValid) {
            int[][] tmpBoard = new int[8][8];
            for (int k = 0; k < 8; k++) {
                System.arraycopy(Board[k], 0, tmpBoard[k], 0, 8);
            }
            tmpBoard = Operate.flipBoard(tmpBoard, node[0], node[1], col).clone();
            int x = MinMaxSearch(tmpBoard, -col, depth - 1, alpha, beta, !MaxPlayer);
            if (MaxPlayer) {
                alpha = Math.max(alpha, x);
                if (beta < alpha) return beta;
            } else {
                beta = Math.min(beta, x);
                if (beta <= alpha) return alpha;
            }
        }
        if (MaxPlayer) return alpha;
        else return beta;
    }
}
