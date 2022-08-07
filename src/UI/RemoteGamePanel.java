package UI;

import Elements.Game;
import Elements.Player;
import Elements.Step;
import Logic.Operate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import static UI.LaunchPanel.MousePosition;
import static UI.style.*;

import Remote.Message;
import System.FileSystem;

public class RemoteGamePanel extends JFrame
{
    static Game game;
    public static themes theme;
    static int curcol;

    public static JFrame gamepane;
    public static PanelWithBackground WestPanel;
    public static JPanel EastPanel;
    public static JPanel BoardPanel;

    public static JButton[][] BoardButton = new JButton[8][8];
    public static JPanel EastTop, EastCenter, EastFoot;
    //EastTop:计时器，当前颜色，存档/退出
    //EastCenter:局面统计，黑白方信息
    //直接添加一个表单到EastPanel用于记录落子
    //EastFoot:悔棋，作弊模式按钮
    public static Timer timer;
    public static long count0;
    //倒计时30s未落子视作认输
    public static JButton surrender, currentColor, SaveButton;
    //surrender投降，平时用作显示时间，点击弹窗询问投降
    //currentColor仅在作弊模式下可以点击，点击后切换落子的颜色
    //否则作弊模式下落子不变色。AI作弊模式下静默
    //SaveButton按下弹出对话框询问是否存档
    //确认后跳转存档界面存档，否则关闭PVEPanel

    public static boolean QuittedSign = false;
    static boolean isPlayerWhite;

    public static void drawWestPanel()
    {
        WestPanel = new PanelWithBackground(BoardIcon);
        BoardPanel = new JPanel(new GridLayout(8, 8));

        WestPanel.setLayout(null);
        WestPanel.setBounds(0, 0, 800, 800);

        BoardPanel.setBounds(50, 50, 700, 700);
        BoardPanel.setOpaque(false);//按钮盘透明

        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                BoardButton[i][j] = new JButton();
                BoardButton[i][j].setBorderPainted(false);
                BoardButton[i][j].setContentAreaFilled(false);
                BoardButton[i][j].addMouseListener(new ML());
                BoardPanel.add(BoardButton[i][j]);
            }
        }
        WestPanel.add(BoardPanel);
    }

    public static JButton[] avatarButton = new JButton[2];
    public static JButton[] stateDisplay = new JButton[2];

    public static Vector<String> comlumnName = new Vector<>();
    public static Vector<Vector<String>> stepData = new Vector<>();
    public static JTable stepTable;
    public static JScrollPane stepPane;
    public static DefaultTableModel defaultTableModel;

    public static void drawEastPanel()
    {
        EastPanel = new JPanel();
        EastPanel.setLayout(null);
        EastPanel.setBounds(800, 0, 400, 800);

        EastTop = new JPanel(new GridLayout(1, 3));
        EastTop.setBounds(0, 0, 405, 134);

        surrender = ToolKit.YaHeiButton("0s", 0, 0, 134, 133, 20);
        surrender.setContentAreaFilled(true);
        surrender.setBackground(ToolKit.bgc);

        currentColor = new JButton("", BlackIcon);
        currentColor.setBorderPainted(false);
        currentColor.setFocusPainted(false);
        currentColor.setBackground(ToolKit.bgc);

        SaveButton = ToolKit.YaHeiButton("保存退出", 0, 0, 134, 134, 20);
        SaveButton.setContentAreaFilled(true);
        SaveButton.setBackground(ToolKit.bgc);
        SaveButton.addMouseListener(new ML());

        EastTop.add(surrender);
        EastTop.add(currentColor);
        EastTop.add(SaveButton);
        EastPanel.add(EastTop);

        EastCenter = new JPanel();
        EastCenter.setLayout(null);
        EastCenter.setBounds(0, 133, 400, 160);

        avatarButton = new JButton[2];
        avatarButton[0] = new JButton();
        avatarButton[1] = new JButton();
        avatarButton[0].setIcon(avatar[game.playerBlack.avatarID]);
        avatarButton[1].setIcon(avatar[game.playerWhite.avatarID]);

        avatarButton[0].setBounds(0, 0, 80, 80);
        avatarButton[1].setBounds(0, 80, 80, 80);
        avatarButton[0].setBorderPainted(false);
        avatarButton[1].setBorderPainted(false);

        stateDisplay[0] = ToolKit.YaHeiButton(game.playerBlack.playerName + "得分:" + Step.countColor(game.Board, -1), 80, 0, 320, 80, 24);
        stateDisplay[1] = ToolKit.YaHeiButton(game.playerWhite.playerName + "得分:" + Step.countColor(game.Board, 1), 80, 80, 320, 80, 24);
        stateDisplay[0].setBackground(ToolKit.bgc);
        stateDisplay[1].setBackground(ToolKit.bgc);
        stateDisplay[0].setHorizontalAlignment(JButton.RIGHT);
        stateDisplay[1].setHorizontalAlignment(JButton.RIGHT);
        stateDisplay[0].setContentAreaFilled(true);
        stateDisplay[1].setContentAreaFilled(true);
        stateDisplay[0].setBorderPainted(false);
        stateDisplay[1].setBorderPainted(false);

        EastCenter.add(avatarButton[0]);
        EastCenter.add(stateDisplay[0]);
        EastCenter.add(avatarButton[1]);
        EastCenter.add(stateDisplay[1]);
        EastPanel.add(EastCenter);

        comlumnName=new Vector<>();
        comlumnName.add("序号");
        comlumnName.add("名字");
        comlumnName.add("颜色");
        comlumnName.add("位置");
        defaultTableModel = new DefaultTableModel(stepData, comlumnName)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        stepTable = new JTable(defaultTableModel);
        stepTable.setBounds(0, 0, 400, 400);
        stepPane = new JScrollPane(stepTable);
        stepPane.setBounds(0, 294, 400, 400);
        EastPanel.add(stepPane);

        EastFoot = new JPanel();
        EastFoot.setLayout(new GridLayout(1, 2));
        EastFoot.setBounds(0, 694, 400, 106);

        EastPanel.add(EastFoot);
    }

    public RemoteGamePanel(Game game, themes theme, boolean isPlayerWhite)
    //传一个game的参进行读取、修改，从Launcher继承AI先后手
    {
        init();
        RemoteGamePanel.isPlayerWhite = isPlayerWhite;
        RemoteGamePanel.game = game;
        RemoteGamePanel.theme = theme;
        curcol = -1;
        stylize(theme);
        stylizeMusic(theme);
        Bgm.loop();

        drawWestPanel();
        gamepane.add(WestPanel);
        drawEastPanel();
        gamepane.add(EastPanel);

        count0 = System.currentTimeMillis();
        timer = new Timer(1000, e ->
        {
            long during = System.currentTimeMillis() - count0;
            surrender.setText(during / 1000 + "s");
        });

        timer.start();

        System.out.println("Game: " + game.gameName + " Start.");
        System.out.println(game.playerBlack.playerName + " vs " + game.playerWhite.playerName);
    }

    public static void init()
    {
        gamepane = new JFrame();
        gamepane.setLayout(null);
        gamepane.setResizable(false);
        gamepane.setBounds(0, 0, 1200, 800);
        gamepane.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gamepane.getContentPane().setLayout(null);
        gamepane.setUndecorated(true);
        gamepane.setVisible(true);

        gamepane.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                MousePosition.x = e.getX();
                MousePosition.y = e.getY();
            }
        });//鼠标按下位置
        gamepane.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                Point p = gamepane.getLocation();
                gamepane.setLocation(p.x + e.getX() - MousePosition.x, p.y + e.getY() - MousePosition.y);
            }
        });//鼠标拖动位移
    }

    static class ML implements MouseListener
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {

        }

        @Override
        public void mousePressed(MouseEvent e)
        {
            if(e.getSource().equals(SaveButton))
            {
                long during = System.currentTimeMillis() - count0;
                timer.stop();
                int choice = JOptionPane.showConfirmDialog(null, "是否中止棋局并退出?", "退出", JOptionPane.OK_CANCEL_OPTION);
                if(choice == JOptionPane.OK_OPTION)
                {//不存档直接退
                    System.out.println("Game quited without saving.");
                    QuittedSign = true;
                    Bgm.stop();
                    gamepane.dispose();
                }
                else//继续游戏
                {
                    count0 = System.currentTimeMillis() - during;
                    timer.start();
                }
            }
            else
            {

                for(int i = 0; i < 8; i++)
                {
                    for(int j = 0; j < 8; j++)
                    {
                        if(e.getSource().equals(BoardButton[i][j]) && game.Board[i][j] == 0 && BoardButton[i][j].getIcon() != null && BoardButton[i][j].getIcon().equals(SelectIcon))
                        {
                            BoardButton[i][j].setIcon(PressedIcon);
                        }
                        else if(e.getSource().equals(BoardButton[i][j]) && game.Board[i][j] == 0 && BoardButton[i][j].getIcon() != null && BoardButton[i][j].getIcon().equals(BlackSelectPreview))
                        {
                            BoardButton[i][j].setIcon(BlackPressedPreview);
                        }
                        else if(e.getSource().equals(BoardButton[i][j]) && game.Board[i][j] == 0 && BoardButton[i][j].getIcon() != null && BoardButton[i][j].getIcon().equals(WhiteSelectPreview))
                        {
                            BoardButton[i][j].setIcon(WhitePressedPreview);
                        }
                    }
                }
            }
        }


        @Override
        public void mouseReleased(MouseEvent e)
        {
            for(int i = 0; i < 8; i++)
            {
                for(int j = 0; j < 8; j++)
                {
                    if(e.getSource().equals(BoardButton[i][j]) && game.Board[i][j] == 0 && curcol == (isPlayerWhite ? 1 : -1))//该本方下棋
                    {
                        if(BoardButton[i][j].getIcon() != null)
                        {
                            if(Operate.queryBoard(game.Board, i, j, isPlayerWhite ? 1 : -1))
                            {
                                count0 = System.currentTimeMillis();
                                System.out.println(curcol == -1 ? "Black: " : "White: " + i + "," + j);
                                game.addStep(curcol == -1 ? game.playerBlack : game.playerWhite, curcol, i, j);
                                clickBoard.play();
                                addLatestStep();
                                repaintBoard(i, j, curcol);
                                curcol = Operate.nextPlayer(game.Board, curcol);
                                Message.message = "P" + i + "," + j;
                            }
                            else
                            {
                                BoardButton[i][j].setIcon(SelectIcon);
                            }
                        }
                    }
                }
            }
            if(Operate.judgeFinish(game.Board)[0] != 0)//结束
            {
                end();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {//鼠标浮过界面，显示选择框，如果已经有子则不显示框，落子提示要和框可以同时存在
            repaintBoard();
            boolean[][] F = Operate.queryAllValid(game.Board, curcol);
            int[][] t = new int[8][8];
            for(int i = 0; i < 8; i++)
            {
                for(int j = 0; j < 8; j++)
                {
                    t[i][j] = game.Board[i][j];
                }
            }
            int x = -1, y = -1;
            for(int i = 0; i < 8; i++)
            {
                for(int j = 0; j < 8; j++)
                {
                    if(BoardButton[i][j].equals(e.getSource()))
                    {
                        x = i;
                        y = j;
                    }
                }
            }
            if(x >= 0 && t[x][y] == 0)
            {
                if(F[x][y])//可落子,虚拟落子显示
                {
                    Operate.flipBoard(t, x, y, curcol);
                    for(int i = 0; i < 8; i++)
                    {
                        for(int j = 0; j < 8; j++)
                        {
                            if(game.Board[i][j] == -1 && t[i][j] == 1)
                            {
                                BoardButton[i][j].setIcon(WhitePreview);
                            }
                            else if(game.Board[i][j] == 1 && t[i][j] == -1)
                            {
                                BoardButton[i][j].setIcon(BlackPreview);
                            }
                        }
                    }
                    BoardButton[x][y].setIcon(curcol == -1 ? BlackSelectPreview : WhiteSelectPreview);
                }
                else//不可落子,只显示选框
                {
                    BoardButton[x][y].setIcon(SelectIcon);
                }
            }//已经有子的地方不做反应
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
            for(int i = 0; i < 8; i++)
            {
                for(int j = 0; j < 8; j++)
                {
                    if(e.getSource().equals(BoardButton[i][j]) && game.Board[i][j] == 0)
                    {
                        BoardButton[i][j].setIcon(null);
                    }
                }
            }
            if(game.gameStep.size() != 0)
            {
                Step step = game.gameStep.get(game.gameStep.size() - 1);
                repaintBoard(step.x, step.y, step.color);
            }
        }
    }

    public static void end()
    {
        timer.stop();
        //这里呼出结束窗口
        int res = Operate.judgeFinish(game.Board)[0];
        switch(res)
        {
            case -1:
            {
                JOptionPane.showConfirmDialog(null, (game.playerBlack.playerName) + "获胜!", "游戏结束!", JOptionPane.OK_CANCEL_OPTION);

                String winner = game.playerBlack.playerName;
                if(!FileSystem.isPlayerExisted(winner))
                {
                    Player player = game.playerBlack;
                    player.winCount++;
                    player.gameCount++;
                    player.scoreCount += Step.countColor(game.Board, -1);
                    player.avatarID = game.playerBlack.avatarID;
                    FileSystem.writePlayer(game.playerBlack);
                }
                else
                {
                    Player t = FileSystem.readPlayer(winner);
                    t.winCount++;
                    t.gameCount++;
                    t.scoreCount += Step.countColor(game.Board, -1);
                    t.avatarID = game.playerBlack.avatarID;
                    FileSystem.writePlayer(t);
                }
                if(!FileSystem.isPlayerExisted(game.playerWhite.playerName))
                {
                    Player player = game.playerWhite;
                    player.gameCount++;
                    player.avatarID = game.playerWhite.avatarID;
                    FileSystem.writePlayer(player);
                }
                else
                {
                    Player t = FileSystem.readPlayer(game.playerWhite.playerName);
                    t.gameCount++;
                    t.avatarID = game.playerWhite.avatarID;
                    FileSystem.writePlayer(t);
                }
                break;
            }
            case 1:
            {
                String winner = game.playerWhite.playerName;
                if(!FileSystem.isPlayerExisted(winner))
                {
                    Player player = game.playerWhite;
                    player.winCount++;
                    player.gameCount++;
                    player.scoreCount += Step.countColor(game.Board, 1);
                    player.avatarID = game.playerWhite.avatarID;
                    FileSystem.writePlayer(game.playerWhite);
                }
                else
                {
                    Player t = FileSystem.readPlayer(winner);
                    t.winCount++;
                    t.gameCount++;
                    t.avatarID = game.playerWhite.avatarID;
                    t.scoreCount += Step.countColor(game.Board, 1);
                    FileSystem.writePlayer(t);
                }
                if(!FileSystem.isPlayerExisted(game.playerBlack.playerName))
                {
                    Player player = game.playerBlack;
                    player.gameCount++;
                    player.avatarID = game.playerBlack.avatarID;
                    FileSystem.writePlayer(player);
                }
                else
                {
                    Player t = FileSystem.readPlayer(game.playerBlack.playerName);
                    t.avatarID = game.playerBlack.avatarID;
                    t.gameCount++;
                    FileSystem.writePlayer(t);
                }

                JOptionPane.showConfirmDialog(null, (game.playerWhite.playerName) + "获胜!", "游戏结束!", JOptionPane.OK_CANCEL_OPTION);
                break;
            }
            default:
            {
                if(!FileSystem.isPlayerExisted(game.playerBlack.playerName))
                {
                    Player player = game.playerBlack;
                    player.winCount++;
                    player.gameCount++;
                    player.scoreCount += Step.countColor(game.Board, -1);
                    FileSystem.writePlayer(player);
                }
                else
                {
                    Player t = FileSystem.readPlayer(game.playerBlack.playerName);
                    t.winCount++;
                    t.gameCount++;
                    t.scoreCount += Step.countColor(game.Board, -1);
                    FileSystem.writePlayer(t);
                }
                if(!FileSystem.isPlayerExisted(game.playerWhite.playerName))
                {
                    Player player = game.playerWhite;
                    player.winCount++;
                    player.gameCount++;
                    player.scoreCount += Step.countColor(game.Board, -1);
                    FileSystem.writePlayer(player);
                }
                else
                {
                    Player t = FileSystem.readPlayer(game.playerWhite.playerName);
                    t.winCount++;
                    t.gameCount++;
                    t.scoreCount += Step.countColor(game.Board, -1);
                    FileSystem.writePlayer(t);
                }
                JOptionPane.showConfirmDialog(null, "平分秋色!", "游戏结束!", JOptionPane.OK_CANCEL_OPTION);
                break;
            }
        }
    }

    public static void repaintBoard()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(BoardButton[i][j].getIcon() != null && (BoardButton[i][j].getIcon().equals(BlackSelect) || BoardButton[i][j].getIcon().equals(WhiteSelect)))
                {
                    continue;
                }
                switch(game.Board[i][j])
                {
                    case 1:
                    {
                        BoardButton[i][j].setIcon(WhiteIcon);
                        break;
                    }
                    case 0:
                    {
                        BoardButton[i][j].setIcon(null);
                        break;
                    }
                    case -1:
                    {
                        BoardButton[i][j].setIcon(BlackIcon);
                        break;
                    }
                }
            }
        }
        stateDisplay[0].setText(game.playerBlack.playerName + "得分:" + Step.countColor(game.Board, -1));
        stateDisplay[1].setText(game.playerWhite.playerName + "得分:" + Step.countColor(game.Board, 1));
    }

    public static void repaintBoard(int x, int y, int col)//刚刚下过棋的地方
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                switch(game.Board[i][j])
                {
                    case 1:
                    {
                        BoardButton[i][j].setIcon(WhiteIcon);
                        break;
                    }
                    case 0:
                    {
                        BoardButton[i][j].setIcon(null);
                        break;
                    }
                    case -1:
                    {
                        BoardButton[i][j].setIcon(BlackIcon);
                        break;
                    }
                }
            }
        }
        stateDisplay[0].setText(game.playerBlack.playerName + "得分:" + Step.countColor(game.Board, -1));
        stateDisplay[1].setText(game.playerWhite.playerName + "得分:" + Step.countColor(game.Board, 1));
        BoardButton[x][y].setIcon(col == -1 ? BlackSelect : WhiteSelect);
    }

    public static void addLatestStep()
    {
        if(game.gameStep.isEmpty())
        {
            return;
        }
        Step latestStep = game.gameStep.get(game.gameStep.size() - 1);
        Vector<String> info = new Vector<>();
        info.add("" + (defaultTableModel.getRowCount()+1));
        info.add(latestStep.player.playerName);
        info.add("" + latestStep.color);
        info.add("(" + latestStep.x + "," + (char) ('A' + latestStep.y) + ")");
        defaultTableModel.addRow(info);
        stepTable.setModel(defaultTableModel);
    }

    public static void OppoMove()
    {
        count0 = System.currentTimeMillis();
        int x = Message.x;
        int y = Message.y;
        if(!Operate.queryBoard(game.Board,x,y,isPlayerWhite?-1:1))
        {
            return;
        }
        Message.x = -1;
        Message.y = -1;
        game.addStep(isPlayerWhite?game.playerBlack:game.playerWhite,isPlayerWhite?-1:1,x,y);
        clickBoard.play();
        Step step = new Step(game.Board, isPlayerWhite ? game.playerBlack : game.playerWhite, isPlayerWhite?-1:1, x, y);
        game.gameStep.add(step);
        addLatestStep();
        System.out.println((isPlayerWhite? "Black: " : "White: ") + x + "," + y);
        repaintBoard(x,y,isPlayerWhite?-1:1);
        curcol = Operate.nextPlayer(game.Board, isPlayerWhite?-1:1);
        if(Operate.judgeFinish(game.Board)[0] != 0)//结束
        {
            end();
        }
    }
}
