package UI;

import Bot.Bot;
import Elements.Game;
import Elements.Player;
import Elements.Step;
import Logic.Operate;
import System.FileSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

import static UI.LaunchPanel.MousePosition;
import static UI.style.*;

public class PVEGamePanel extends JFrame implements ActionListener, MouseListener,FocusListener
{
    Game game;
    int difficulty;//0,1,2,3
    themes theme;
    int curcol;
    String BotName;

    JFrame gamepane;
    PanelWithBackground WestPanel;
    JPanel EastPanel;
    JPanel BoardPanel;

    JButton[][] BoardButton = new JButton[8][8];
    JPanel EastTop, EastCenter, EastFoot;
    //EastTop:计时器，当前颜色，存档/退出
    //EastCenter:局面统计，黑白方信息
    //直接添加一个表单到EastPanel用于记录落子
    //EastFoot:悔棋，作弊模式按钮
    Timer timer;
    long count0;
    //倒计时30s未落子视作认输
    JButton surrender, currentColor, SaveButton;
    //surrender投降，平时用作显示时间，点击弹窗询问投降
    //currentColor仅在作弊模式下可以点击，点击后切换落子的颜色
    //否则作弊模式下落子不变色。AI作弊模式下静默
    //SaveButton按下弹出对话框询问是否存档
    //确认后跳转存档界面存档，否则关闭PVEPanel
    boolean isAIBlack;

    boolean QuittedSign = false;

    public void drawWestPanel()
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
                BoardButton[i][j].addMouseListener(this);
                BoardPanel.add(BoardButton[i][j]);
            }
        }
        WestPanel.add(BoardPanel);
    }

    JButton[] avatarButton = new JButton[2];
    JButton[] stateDisplay = new JButton[2];

    Vector<String> comlumnName = new Vector<>();
    Vector<Vector<String>> stepData = new Vector<>();
    JTable stepTable;
    JScrollPane stepPane;
    DefaultTableModel defaultTableModel;

    JButton regret;
    JButton cheatMode;
    boolean cheat = false;
    boolean cheated = false;
    boolean saveFinish=false;

    public void drawEastPanel()
    {
        EastPanel = new JPanel();
        EastPanel.setLayout(null);
        EastPanel.setBounds(800, 0, 400, 800);

        EastTop = new JPanel(new GridLayout(1, 3));
        EastTop.setBounds(0, 0, 405, 134);

        surrender = ToolKit.YaHeiButton("0s", 0, 0, 134, 133, 20);
        surrender.setContentAreaFilled(true);
        surrender.setBackground(ToolKit.bgc);

        currentColor = new JButton("", isAIBlack ? WhiteIcon : BlackIcon);
        currentColor.setBorderPainted(false);
        currentColor.setFocusPainted(false);
        currentColor.setBackground(ToolKit.bgc);
        currentColor.addActionListener(e ->
        {
            if(e.getSource().equals(currentColor) && cheat)
            {
                isAIBlack = !isAIBlack;
                curcol = -curcol;
                if(curcol == -1)
                {
                    currentColor.setIcon(BlackIcon);
                }
                else
                {
                    currentColor.setIcon(WhiteIcon);
                }
                Player t = game.playerBlack;
                game.playerBlack = game.playerWhite;
                game.playerWhite = t;
            }
        });

        SaveButton = ToolKit.YaHeiButton("保存退出", 0, 0, 134, 134, 20);
        SaveButton.setContentAreaFilled(true);
        SaveButton.setBackground(ToolKit.bgc);
        SaveButton.addMouseListener(this);

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
        avatarButton[0].setIcon(avatar[(isAIBlack ? game.playerWhite : game.playerBlack).avatarID]);
        avatarButton[1].setIcon(avatar[(isAIBlack ? game.playerBlack : game.playerWhite).avatarID]);

        avatarButton[0].setBounds(0, 0, 80, 80);
        avatarButton[1].setBounds(0, 80, 80, 80);
        avatarButton[0].setBorderPainted(false);
        avatarButton[1].setBorderPainted(false);

        stateDisplay[0] = ToolKit.YaHeiButton((isAIBlack ? game.playerWhite : game.playerBlack).playerName + "得分:" + Step.countColor(game.Board, isAIBlack ? 1 : -1), 80, 0, 320, 80, 24);

        stateDisplay[1] = ToolKit.YaHeiButton(BotName + "得分:" + Step.countColor(game.Board, isAIBlack ? -1 : 1), 80, 80, 320, 80, 24);
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
        EastCenter.add(stateDisplay[1]);
        EastCenter.add(avatarButton[1]);
        EastPanel.add(EastCenter);

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

        regret = ToolKit.YaHeiButton("悔棋", 0, 694, 200, 106, 24);
        regret.setContentAreaFilled(true);
        regret.setBackground(ToolKit.bgc);
        regret.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                if(e.isMetaDown())
                {
                    System.out.println("redo");
                    redoLatestStep();
                }
                else
                {
                    removeLatestStep();
                }
            }
        });

        cheatMode = ToolKit.YaHeiButton("作弊模式(关)", 0, 694, 200, 106, 24);
        cheatMode.setContentAreaFilled(true);
        cheatMode.setBackground(ToolKit.bgc);
        cheatMode.addActionListener(e ->
        {
            if(e.getSource().equals(cheatMode))
            {
                cheated = true;
                cheat = !cheat;
                if(cheat)
                {
                    cheatMode.setText("作弊模式(开)");
                    cheatMode.setBackground(new Color(153, 102, 0));
                }
                else
                {
                    cheatMode.setText("作弊模式(关)");
                    cheatMode.setBackground(ToolKit.bgc);
                }
            }
        });

        EastFoot.add(regret);
        EastFoot.add(cheatMode);
        EastPanel.add(EastFoot);
    }

    public PVEGamePanel(Game game, boolean isAIBlack, int difficulty, themes theme)
    //传一个game的参进行读取、修改，从Launcher继承AI先后手
    {
        init();
        this.game = game;
        if(game.deleteStep==null)
        {
            game.deleteStep=new Stack<>();
        }
        this.theme = theme;
        this.isAIBlack = isAIBlack;
        this.difficulty = difficulty;
        curcol = isAIBlack ? 1 : -1;
        style.stylize(theme);
        stylizeMusic(theme);
        Bgm.loop();
        stylizeBotName();

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
        if(game.gameStep.isEmpty())//新游戏
        {
            if(isAIBlack)
            {
                game.addStep(game.playerBlack, -1, 2, 3);
                clickBoard.play();
                addLatestStep();
            }
        }
        else
        {
            for(Step step:game.gameStep)
            {
                Vector<String> info = new Vector<>();
                info.add("" + game.gameStep.size());
                info.add(step.player.playerName.equals("AI Player") ? BotName : step.player.playerName);
                info.add("" + (step.color==-1?"黑":"白"));
                info.add("(" + step.x + "," + (char) ('A' + step.y) + ")");
                defaultTableModel.addRow(info);
                stepTable.setModel(defaultTableModel);
            }
        }
        System.out.println("Game: " + game.gameName + " Start.");
        System.out.println(game.playerBlack.playerName + " vs " + game.playerWhite.playerName);
    }

    public void init()
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

    @Override
    public void actionPerformed(ActionEvent e)
    {

    }

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
            if(saveFinish)
            {
                QuittedSign=true;
                Bgm.stop();
                gamepane.dispose();
            }
            int choice = JOptionPane.showConfirmDialog(null, "是否中止棋局并存档?", "保存", JOptionPane.YES_NO_CANCEL_OPTION);
            if(choice == JOptionPane.YES_OPTION)
            {//存档
                SaveFrame saveFrame= new SaveFrame(game,theme);
                saveFinish=saveFinish||saveFrame.saved;
                style.stylize(theme);
                if(saveFrame.saved)
                {
                    QuittedSign=true;
                    Bgm.stop();
                    gamepane.dispose();
                }
            }
            else if(choice == JOptionPane.NO_OPTION)
            {//不存档直接退
                System.out.println("Game quited without saving.");
                QuittedSign = true;
                Bgm.stop();
                gamepane.dispose();
            }//继续游戏
            else
            {
                count0 = System.currentTimeMillis() - during;
                timer.start();
            }
        }
        else
        {
            if(cheat)
            {
                for(int i = 0; i < 8; i++)
                {
                    for(int j = 0; j < 8; j++)
                    {
                        if(e.getSource().equals(BoardButton[i][j]))
                        {
                            BoardButton[i][j].setIcon(curcol == -1 ? BlackPressedPreview : WhitePressedPreview);
                        }
                    }
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
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(cheat)
        {
            for(int i = 0; i < 8; i++)
            {
                for(int j = 0; j < 8; j++)
                {
                    if(e.getSource().equals(BoardButton[i][j]))
                    {
                        game.Board[i][j] = curcol;
                        game.gameStep.add(new Step(game.Board, game.playerBlack.playerName.equals("AI Player") ?
                                game.playerWhite : game.playerBlack, curcol, i, j));
                        addLatestStep();
                        repaint();
                    }
                }
            }
        }
        else
        {
            for(int i = 0; i < 8; i++)
            {
                for(int j = 0; j < 8; j++)
                {
                    if(e.getSource().equals(BoardButton[i][j]) && game.Board[i][j] == 0)
                    {
                        if(BoardButton[i][j].getIcon() != null)
                        {
                            if(Operate.nextPlayer(game.Board, isAIBlack ? -1 : 1) == (isAIBlack ? 1 : -1))
                            //该玩家行动
                            {
                                if(Operate.queryBoard(game.Board, i, j, isAIBlack ? 1 : -1))
                                {
                                    saveFinish=false;
                                    count0 = System.currentTimeMillis();
                                    System.out.println("Player: " + i + "," + j);
                                    game.addStep(isAIBlack ? game.playerWhite : game.playerBlack, isAIBlack ? 1 : -1, i, j);
                                    clickBoard.play();
                                    if(game.deleteStep==null)
                                    {
                                        game.deleteStep=new Stack<>();
                                    }
                                    if(!game.deleteStep.empty())
                                        game.deleteStep.clear();
                                    addLatestStep();
                                    repaintBoard(i, j, isAIBlack ? 1 : -1);
                                    if(Operate.nextPlayer(game.Board, isAIBlack ? 1 : -1) == (isAIBlack ? -1 : 1))
                                    {
                                        count0 = System.currentTimeMillis();
                                        int[] AISTEP = BotStep();
                                        System.out.println("AI: " + AISTEP[0] + "," + AISTEP[1]);
                                        game.addStep(isAIBlack ? game.playerBlack : game.playerWhite, isAIBlack ? -1 : 1, AISTEP[0], AISTEP[1]);
                                        clickBoard.play();
                                        addLatestStep();
                                        repaintBoard(AISTEP[0], AISTEP[1], isAIBlack ? -1 : 1);
                                        while(Operate.nextPlayer(game.Board, isAIBlack ? -1 : 1) == (isAIBlack ? -1 : 1))
                                        {
                                            AISTEP = BotStep();
                                            System.out.println("AI: " + AISTEP[0] + "," + AISTEP[1]);
                                            if(AISTEP[0] == -1 || Operate.judgeFinish(game.Board)[0] != 0)
                                            {
                                                break;
                                            }
                                            game.addStep(isAIBlack ? game.playerBlack : game.playerWhite, isAIBlack ? -1 : 1, AISTEP[0], AISTEP[1]);
                                            clickBoard.play();
                                            //game.printBoard();
                                            addLatestStep();
                                            repaintBoard(AISTEP[0], AISTEP[1], isAIBlack ? -1 : 1);
                                        }
                                    }//AI走一步，如果持续让手就持续走
                                }
                                else
                                {
                                    BoardButton[i][j].setIcon(SelectIcon);
                                }
                            }
                            //如果能落子就更新棋盘，如果不能就还原绿框
                        }
                    }
                }
            }
        }
        if(Operate.judgeFinish(game.Board)[0] != 0)//结束
        {
            timer.stop();
            //这里呼出结束窗口
            int res = Operate.judgeFinish(game.Board)[0];
            switch(res)
            {
                case -1:
                {
                    JOptionPane.showConfirmDialog(null, (isAIBlack ? BotName : game.playerBlack.playerName) + "获胜!", "游戏结束!", JOptionPane.OK_CANCEL_OPTION);
                    if(isAIBlack)
                    {
                        lose.play();
                    }
                    else
                    {
                        win.play();
                    }
                    if(!cheated)
                    {
                        String winner =game.playerBlack.playerName;
                        if(!FileSystem.isPlayerExisted(winner))
                        {
                            Player player=game.playerBlack;
                            player.winCount++;
                            player.gameCount++;
                            player.scoreCount+=Step.countColor(game.Board,-1);
                            player.avatarID=game.playerBlack.avatarID;
                            FileSystem.writePlayer(game.playerBlack);
                        }
                        else
                        {
                            Player t = FileSystem.readPlayer(winner);
                            t.winCount++;
                            t.gameCount++;
                            t.scoreCount += Step.countColor(game.Board, -1);
                            t.avatarID=game.playerBlack.avatarID;
                            FileSystem.writePlayer(t);
                        }
                        if(!FileSystem.isPlayerExisted(game.playerWhite.playerName))
                        {
                            Player player=game.playerWhite;
                            player.gameCount++;
                            player.avatarID=game.playerWhite.avatarID;
                            FileSystem.writePlayer(player);
                        }
                        else
                        {
                            Player t=FileSystem.readPlayer(game.playerWhite.playerName);
                            t.gameCount++;
                            t.avatarID=game.playerWhite.avatarID;
                            FileSystem.writePlayer(t);
                        }
                    }
                    break;
                }
                case 1:
                {
                    if(isAIBlack)
                    {
                        win.play();
                    }
                    else
                    {
                        lose.play();
                    }
                    if(!cheated)
                    {
                        String winner = game.playerWhite.playerName;
                        if(!FileSystem.isPlayerExisted(winner))
                        {
                            Player player=game.playerWhite;
                            player.winCount++;
                            player.gameCount++;
                            player.scoreCount+=Step.countColor(game.Board,1);
                            player.avatarID=game.playerWhite.avatarID;
                            FileSystem.writePlayer(game.playerWhite);
                        }
                        else
                        {
                            Player t = FileSystem.readPlayer(winner);
                            t.winCount++;
                            t.gameCount++;
                            t.avatarID=game.playerWhite.avatarID;
                            t.scoreCount += Step.countColor(game.Board, 1);
                            FileSystem.writePlayer(t);
                        }
                        if(!FileSystem.isPlayerExisted(game.playerBlack.playerName))
                        {
                            Player player=game.playerBlack;
                            player.gameCount++;
                            player.avatarID=game.playerBlack.avatarID;
                            FileSystem.writePlayer(player);
                        }
                        else
                        {
                            Player t=FileSystem.readPlayer(game.playerBlack.playerName);
                            t.avatarID=game.playerBlack.avatarID;
                            t.gameCount++;
                            FileSystem.writePlayer(t);
                        }
                    }
                    JOptionPane.showConfirmDialog(null, (isAIBlack ? game.playerWhite.playerName : BotName) + "获胜!", "游戏结束!", JOptionPane.OK_CANCEL_OPTION);
                    break;
                }
                default:
                {
                    end.play();
                    if(!cheated)
                    {
                        if(!FileSystem.isPlayerExisted(game.playerBlack.playerName))
                        {
                            Player player=game.playerBlack;
                            player.winCount++;
                            player.gameCount++;
                            player.scoreCount+=Step.countColor(game.Board,-1);
                            FileSystem.writePlayer(player);
                        }
                        else
                        {
                            Player t=FileSystem.readPlayer(game.playerBlack.playerName);
                            t.winCount++;
                            t.gameCount++;
                            t.scoreCount+=Step.countColor(game.Board,-1);
                            FileSystem.writePlayer(t);
                        }
                        if(!FileSystem.isPlayerExisted(game.playerWhite.playerName))
                        {
                            Player player=game.playerWhite;
                            player.winCount++;
                            player.gameCount++;
                            player.scoreCount+=Step.countColor(game.Board,-1);
                            FileSystem.writePlayer(player);
                        }
                        else
                        {
                            Player t=FileSystem.readPlayer(game.playerWhite.playerName);
                            t.winCount++;
                            t.gameCount++;
                            t.scoreCount+=Step.countColor(game.Board,-1);
                            FileSystem.writePlayer(t);
                        }
                    }
                    JOptionPane.showConfirmDialog(null, "平分秋色!", "游戏结束!", JOptionPane.OK_CANCEL_OPTION);
                    break;
                }
            }
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
    }

    public void repaintBoard()
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
        stateDisplay[0].setText((isAIBlack ? game.playerWhite : game.playerBlack).playerName + "得分:" + Step.countColor(game.Board, isAIBlack ? 1 : -1));
        stateDisplay[1].setText(BotName + "得分:" + Step.countColor(game.Board, isAIBlack ? -1 : 1));
    }

    public void repaintBoard(int x, int y, int col)//刚刚下过棋的地方
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
        stateDisplay[0].setText((isAIBlack ? game.playerWhite : game.playerBlack).playerName + "得分:" + Step.countColor(game.Board, isAIBlack ? 1 : -1));
        stateDisplay[1].setText(BotName + "得分:" + Step.countColor(game.Board, isAIBlack ? -1 : 1));
        BoardButton[x][y].setIcon(col == -1 ? BlackSelect : WhiteSelect);
    }

    public int[] BotStep()
    {
        switch(difficulty)
        {
            case 0:
            {
                return Bot.Random(game.Board, isAIBlack ? -1 : 1);
            }
            case 1:
            {
                return Bot.lowerGreedy(game.Board, isAIBlack ? -1 : 1);
            }
            case 2:
            {
                return Bot.Greedy(game.Board, isAIBlack ? -1 : 1);
            }
            default:
            {
                return Bot.search(game.Board, isAIBlack ? -1 : 1);
            }
        }
    }


    public void addLatestStep()
    {
        if(cheat)
        {
            Step latestStep = game.gameStep.get(game.gameStep.size() - 1);
            Vector<String> info = new Vector<>();
            info.add("" + game.gameStep.size());
            info.add((game.playerWhite.playerName.equals("AI Player") ? game.playerBlack.playerName : game.playerWhite.playerName) + "(cheat)");
            info.add("" + (latestStep.color==1?"白":"黑"));
            info.add("(" + latestStep.x + "," + (char) ('A' + latestStep.y) + ")");
            defaultTableModel.addRow(info);
            stepTable.setModel(defaultTableModel);
        }
        else
        {
            Step latestStep = game.gameStep.get(game.gameStep.size() - 1);
            Vector<String> info = new Vector<>();
            info.add("" + game.gameStep.size());
            info.add(latestStep.player.playerName.equals("AI Player") ? BotName : latestStep.player.playerName);
            info.add("" + (latestStep.color==1?"白":"黑"));
            info.add("(" + latestStep.x + "," + (char) ('A' + latestStep.y) + ")");
            defaultTableModel.addRow(info);
            stepTable.setModel(defaultTableModel);
        }
    }

    public void removeLatestStep()//由于人机，回退直到上次是玩家
    {
        if(cheated)
        {
            if(game.gameStep.isEmpty())
            {
                return;
            }
            if(game.gameStep.size() == 1)
            {
                if(!isAIBlack)
                {
                    count0 = System.currentTimeMillis();
                    game.deleteStep.push(game.gameStep.get(0));
                    defaultTableModel.removeRow(0);
                    //stepData.subList(0, stepData.size() - 1);
                    stepTable.setModel(defaultTableModel);
                    game.initBoard();
                    game.gameStep.clear();
                    simpleRepaint();
                }
                return;
            }
            count0 = System.currentTimeMillis();
            game.deleteStep();
            defaultTableModel.removeRow(defaultTableModel.getRowCount() - 1);
            stepData.subList(0, stepData.size() - 1);
            stepTable.setModel(defaultTableModel);
            simpleRepaint();
            return;
        }
        int size_ = game.gameStep.size();
        if(size_ == 2 && !isAIBlack)
        {
            count0 = System.currentTimeMillis();
            game.deleteStep.push(game.gameStep.get(1));
            game.deleteStep.push(game.gameStep.get(0));
            game.gameStep = new ArrayList<>();
            game.initBoard();
            defaultTableModel.removeRow(1);
            defaultTableModel.removeRow(0);
            stepData.subList(0, stepData.size() - 2);
            stepTable.setModel(defaultTableModel);
            simpleRepaint();
            return;
        }
        if(size_ == 1 && isAIBlack)
        {
            return;
        }
        if(size_ == 0)
        {
            return;
        }
        do
        {
            game.deleteStep();
            defaultTableModel.removeRow(defaultTableModel.getRowCount() - 1);
            stepData.subList(0, stepData.size() - 1);
        }
        while(game.gameStep.get(game.gameStep.size() - 1).player.playerName.equals("AI Player"));
        game.deleteStep();
        defaultTableModel.removeRow(defaultTableModel.getRowCount() - 1);
        stepData.subList(0, stepData.size() - 1);
        stepTable.setModel(defaultTableModel);
        count0 = System.currentTimeMillis();
        simpleRepaint();
    }

    public void redoLatestStep()
    {
        if(cheated)
        {
            count0 = System.currentTimeMillis();
            game.antiDelete();
            addLatestStep();
            return;
        }
        if(game.deleteStep.empty())
        {
            System.out.println("empty stack");
            return;
        }
        count0 = System.currentTimeMillis();
        Step step = game.deleteStep.pop();
        do
        {
            game.gameStep.add(step);
            addLatestStep();
            if(game.deleteStep.empty())
            {
                break;
            }
            step = game.deleteStep.pop();
        }
        while(step.player.playerName.equals("AI Player"));
        if(!game.deleteStep.empty())
        {
            game.deleteStep.push(step);
        }
        game.Board = step.stepBoard;
        simpleRepaint();
    }

    public void simpleRepaint()
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
    }

    public void stylizeBotName()
    {
        if(theme==themes.TOUHOU)
        {
            switch(difficulty)
            {
                case 0:
                {
                    BotName = "Cirno";
                    break;
                }
                case 1:
                {
                    BotName = "Hakurei Reimu";
                    break;
                }
                case 2:
                {
                    BotName = "Yakumo Yukari";
                    break;
                }
                default:
                {
                    BotName = "Komeiji Koishi";
                    break;
                }
            }
        }
        else if(theme==themes.MINECRAFT)
        {
            switch(difficulty)
            {
                case 0:
                {
                    BotName = "EnderMan";
                    break;
                }
                case 1:
                {
                    BotName = "Shulker";
                    break;
                }
                case 2:
                {
                    BotName = "Guardian";
                    break;
                }
                default:
                {
                    BotName = "Wither";
                    break;
                }
            }
        }
        else
        {
            switch(difficulty)
            {
                case 0:
                {
                    BotName = "亦";
                    break;
                }
                case 1:
                {
                    BotName = "疾";
                    break;
                }
                case 2:
                {
                    BotName = "艳";
                    break;
                }
                default:
                {
                    BotName = "霸";
                    break;
                }
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e)
    {
        Stylize(theme);
        stylize(theme);
    }

    @Override
    public void focusLost(FocusEvent e)
    {

    }
}
