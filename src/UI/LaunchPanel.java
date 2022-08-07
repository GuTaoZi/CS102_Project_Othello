package UI;

import Elements.Game;
import Elements.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import Elements.Step;
import Remote.Message;
import Remote.Receive;
import Remote.Send;
import System.FileSystem;
import System.GameSystem;
import System.Record;

import static UI.style.*;


public class LaunchPanel extends JFrame
{
    public JFrame frame;
    // 全局的位置变量，用于表示鼠标在窗口上的位置
    public static Point MousePosition = new Point();
    final static int K = 1;
    final static int Kp = 1;

    public JPanel NorthPanel;
    public JPanel WestPanel;
    public JPanel CenterPanel;
    public themes theme;

    public JButton LOGO, gameButton, saveButton, themeButton, quitButton;
    public static ImageIcon logo = new ImageIcon(new ImageIcon("media/public/Logo.png").getImage()
            .getScaledInstance(50 * K, 50 * K, 1));
    //一级菜单按钮
    //logo,按下后转到帮助文档
    //游戏，读取，创意工坊，退出

    public void updateNorthPanel()
    {
        this.NorthPanel = new JPanel(new GridLayout(1, 5));//一行五列
        NorthPanel.setBounds(0, 0, 800 * K, 60 * K);
        NorthPanel.setBackground(ToolKit.bgc);

        LOGO = new JButton("", logo);
        LOGO.setBounds(0, 0, 50 * K, 50 * K);
        LOGO.setBorderPainted(false);
        LOGO.setFocusPainted(false);
        LOGO.setContentAreaFilled(false);

        gameButton = ToolKit.YaHeiButton("游戏", 200 * K, 0, 80 * K, 100 * K, 20 * Kp);
        saveButton = ToolKit.YaHeiButton("读取", 200 * K, 0, 80 * K, 100 * K, 20 * Kp);
        themeButton = ToolKit.YaHeiButton("创意工坊", 200 * K, 0, 80 * K, 100 * K, 20 * Kp);
        quitButton = ToolKit.YaHeiButton("退出", 200 * K, 0, 80 * K, 100 * K, 20 * Kp);

        LOGO.addActionListener(new NorthPanelAction());
        gameButton.addActionListener(new NorthPanelAction());
        saveButton.addActionListener(new NorthPanelAction());
        themeButton.addActionListener(new NorthPanelAction());
        quitButton.addActionListener(new NorthPanelAction());
        OneWhiteNorth(gameButton);

        NorthPanel.add(LOGO);
        NorthPanel.add(gameButton);
        NorthPanel.add(saveButton);
        NorthPanel.add(themeButton);
        NorthPanel.add(quitButton);

        frame.add(NorthPanel);
    }


    //游戏
    public JPanel modePanel, startPanel, PVPPanel, PVEPanel, RemotePanel;
    public int curMode = 1;//1-PVE,2-PVP,3-Remote
    public JButton PVEButton, PVPButton, RemoteButton;

    public static JButton Start;
    public JButton[] avatarButton = new JButton[2];
    public int[] avatarID = {0, 0};
    //在单人游戏中，只使用avatarButton[0]和avatarID[0]代表玩家的头像数据
    //双人游戏则黑方[0]白方[1]
    public JTextField GameName;
    public JTextField[] UserName = new JTextField[2];
    public boolean isPlayerWhite = false;//0-玩家先手,1-PC先手
    public JButton ColorSelect;
    public int difficulty = 0;//0,1,2,3
    public JButton DifficultySelect;
    public JButton connect;

    public PVEGamePanel pveGamePanel;
    public PVPGamePanel pvpGamePanel;
    public RemoteGamePanel remoteGamePanel;
    public ColorSelectAction CSA;

    public static Player blackPlayer;
    public static Player whitePlayer;

    public void updateWestCenterPanelForGame()
    {
        if(WestPanel != null)
        {
            WestPanel.removeAll();
        }
        if(CenterPanel != null)
        {
            CenterPanel.removeAll();
        }
        WestPanel = new JPanel();
        WestPanel.repaint();
        CenterPanel = new JPanel();
        CenterPanel.repaint();
        WestPanel.setLayout(null);

        WestPanel.setBounds(0, 60 * K, 250 * K, 440 * K);
        WestPanel.setBorder(new EmptyBorder(-5, 0, -5, 0));

        modePanel = new JPanel(new GridLayout(1, 3, 2, 0));

        modePanel.setBounds(0, 0, 250 * K, 60 * K);
        modePanel.setBackground(ToolKit.bgc);

        PVEButton = ToolKit.YaHeiButton("人机", 0, 60 * K, 83 * K, 60 * K, 16 * Kp);
        PVPButton = ToolKit.YaHeiButton("双人", 0, 60 * K, 83 * K, 60 * K, 16 * Kp);
        RemoteButton = ToolKit.YaHeiButton("远程", 0, 60 * K, 84 * K, 60 * K, 16 * Kp);

        PVEButton.addActionListener(new modePanelAction());
        PVPButton.addActionListener(new modePanelAction());
        RemoteButton.addActionListener(new modePanelAction());
        modePanel.add(PVEButton);
        modePanel.add(PVPButton);
        modePanel.add(RemoteButton);
        WestPanel.add(modePanel);

        if(curMode == 1)
        //人机模式
        {
            style.Stylize(theme);
            OneWhitemode(PVEButton);

            PVEPanel = new JPanel();
            PVEPanel.setLayout(null);
            PVEPanel.setBounds(250 * K, 60 * K, 550 * K, 440 * K);
            GameName = new JTextField();
            GameName.setFont(ToolKit.YaHei);
            GameName.addFocusListener(new TextFieldFocus(GameName, "请输入游戏名"));
            GameName.setBounds(200 * K, 60 * K, 150 * K, 50 * K);
            //这里读入游戏名

            UserName[0] = new JTextField();
            UserName[0].setFont(ToolKit.YaHei);
            UserName[0].addFocusListener(new TextFieldFocus(UserName[0], "请输入用户名"));
            UserName[0].addFocusListener(new readPlayerInfoAction());
            UserName[0].setBounds(200 * K, 120 * K, 150 * K, 50 * K);
            //这里读入用户名

            ColorSelect = ToolKit.YaHeiButton(isPlayerWhite ? "玩家执白" : "玩家执黑", 200 * K, 180 * K, 150 * K, 50 * K, 20 * Kp);
            if(isPlayerWhite)
            {
                ColorSelect.setText("玩家执白");
                ColorSelect.setForeground(Color.black);
                ColorSelect.setBackground(Color.white);
            }
            else
            {
                ColorSelect.setText("玩家执黑");
                ColorSelect.setBackground(Color.black);
                ColorSelect.setForeground(Color.white);
            }
            ColorSelect.setContentAreaFilled(true);
            ColorSelect.addActionListener(new ColorSelectAction());
            //isPlayerWhite控制谁先手，作为参数传入gamePanel

            //这里要添加根据主题不同，不同难度按钮换肤的功能

            DifficultySelect = ToolKit.YaHeiButton("", 200 * K, 240 * K, 150 * K, 50 * K, 20 * Kp);
            DifficultySelect.setIcon(difficultyIcon[difficulty]);
            DifficultySelect.addActionListener(new DifficultySelectAction());

            PVEPanel.add(GameName);
            PVEPanel.add(UserName[0]);
            PVEPanel.add(ColorSelect);
            PVEPanel.add(DifficultySelect);

            startPanel = new JPanel();
            startPanel.removeAll();
            startPanel.setLayout(null);
            startPanel.setBounds(0, 60 * K, 250 * K, 380 * K);
            startPanel.setBackground(new Color(230, 230, 230));
            avatarButton[0] = new JButton();
            avatarButton[0].setContentAreaFilled(false);
            avatarButton[0].setBounds(63 * K, 60 * K, 125 * K, 125 * K);
            avatarButton[0].setBorderPainted(false);

            avatarButton[0].setFocusPainted(false);
            avatarButton[0].setIcon(avatar[0]);
            avatarButton[0].addActionListener(new avatarSwitchAction());

            Start = ToolKit.YaHeiButton("开始游戏", 65 * K, 160 * K, 120 * K, 60 * K, 20 * K);
            Start.setBackground(ToolKit.bgc);
            Start.setContentAreaFilled(true);
            Start.setBounds(65 * K, 240 * K, 120 * K, 60 * K);
            Start.addActionListener(new StartGameAction());

            startPanel.add(avatarButton[0]);
            startPanel.add(Start);

            CenterPanel = PVEPanel;
            WestPanel.add(startPanel);
        }
        else if(curMode == 2)
        {
            style.Stylize(theme);
            style.stylize(theme);
            OneWhitemode(PVPButton);

            PVPPanel = new JPanel();
            PVPPanel.setLayout(null);
            PVPPanel.setBounds(250 * K, 60 * K, 550 * K, 440 * K);
            GameName = new JTextField();
            GameName.setFont(ToolKit.YaHei);
            GameName.addFocusListener(new TextFieldFocus(GameName, "请输入游戏名"));
            GameName.setBounds(200 * K, 60 * K, 150 * K, 50 * K);
            //这里读入游戏名

            UserName[0] = new JTextField();
            UserName[0].setFont(ToolKit.YaHei);
            UserName[0].addFocusListener(new TextFieldFocus(UserName[0], "请输入黑方名"));
            UserName[0].addFocusListener(new readPlayerInfoAction());
            UserName[0].setBounds(100 * K, 120 * K, 150 * K, 50 * K);
            UserName[1] = new JTextField();
            UserName[1].setFont(ToolKit.YaHei);
            UserName[1].addFocusListener(new TextFieldFocus(UserName[1], "请输入白方名"));
            UserName[1].addFocusListener(new readPlayerInfoAction());
            UserName[1].setBounds(300 * K, 120 * K, 150 * K, 50 * K);
            //这里读入用户名

            avatarButton[0] = new JButton();
            avatarButton[0].setBounds(30 * K, 60 * K, 80 * K, 80 * K);
            avatarButton[0].setBorderPainted(false);
            avatarButton[0].setIcon(avatar[0]);
            avatarButton[0].addActionListener(new avatarSwitchAction());
            avatarButton[0].setContentAreaFilled(false);
            avatarButton[1] = new JButton();
            avatarButton[1].setContentAreaFilled(false);
            avatarButton[1].setBounds(140 * K, 60 * K, 80 * K, 80 * K);
            avatarButton[1].setBorderPainted(false);
            avatarButton[1].setIcon(avatar[1]);
            avatarButton[1].addActionListener(new avatarSwitchAction());

            Start = ToolKit.YaHeiButton("开始游戏", 65 * K, 160 * K, 120 * K, 60 * K, 20 * K);
            Start.setBackground(ToolKit.bgc);
            Start.setContentAreaFilled(true);
            Start.setBounds(65 * K, 240 * K, 120 * K, 60 * K);
            Start.addActionListener(new StartGameAction());

            startPanel = new JPanel();
            startPanel.removeAll();
            startPanel.setLayout(null);
            startPanel.setBounds(0, 60 * K, 250 * K, 380 * K);
            startPanel.setBackground(new Color(230, 230, 230));
            startPanel.add(avatarButton[0]);
            startPanel.add(avatarButton[1]);
            startPanel.add(Start);

            PVPPanel.add(GameName);
            PVPPanel.add(UserName[0]);
            PVPPanel.add(UserName[1]);

            CenterPanel = PVPPanel;
            WestPanel.add(startPanel);
        }
        else
        {
            Stylize(theme);
            OneWhitemode(RemoteButton);

            RemotePanel = new JPanel();
            RemotePanel.setLayout(null);
            RemotePanel.setBounds(250 * K, 60 * K, 550 * K, 440 * K);
            GameName = new JTextField();
            GameName.setFont(ToolKit.YaHei);
            GameName.addFocusListener(new TextFieldFocus(GameName, "请输入服务器ip"));
            GameName.addFocusListener(new ipCheckAction());
            GameName.setBounds(200 * K, 60 * K, 150 * K, 50 * K);
            //这里读入ip

            UserName[0] = new JTextField();
            UserName[0].setFont(ToolKit.YaHei);
            UserName[0].addFocusListener(new TextFieldFocus(UserName[0], "请输入用户名"));
            UserName[0].addFocusListener(new readPlayerInfoAction());
            UserName[0].setBounds(200 * K, 120 * K, 150 * K, 50 * K);
            //这里读入用户名

            ColorSelect = ToolKit.YaHeiButton(isPlayerWhite ? "玩家执白" : "玩家执黑", 200 * K, 180 * K, 150 * K, 50 * K, 20 * Kp);
            if(isPlayerWhite)
            {
                ColorSelect.setText("玩家执白");
                ColorSelect.setForeground(Color.black);
                ColorSelect.setBackground(Color.white);
            }
            else
            {
                ColorSelect.setText("玩家执黑");
                ColorSelect.setBackground(Color.black);
                ColorSelect.setForeground(Color.white);
            }
            ColorSelect.setContentAreaFilled(true);
            CSA=new ColorSelectAction();
            ColorSelect.addActionListener(CSA);
            //isPlayerWhite控制谁先手，作为参数传入gamePanel

            connect = ToolKit.YaHeiButton("", 25 * K, 240 * K, 500 * K, 50 * K, 20 * Kp);
            connect.setForeground(ToolKit.bgc);

            RemotePanel.add(GameName);
            RemotePanel.add(UserName[0]);
            RemotePanel.add(ColorSelect);
            RemotePanel.add(connect);

            startPanel = new JPanel();
            startPanel.removeAll();
            startPanel.setLayout(null);
            startPanel.setBounds(0, 60 * K, 250 * K, 380 * K);
            startPanel.setBackground(new Color(230, 230, 230));
            avatarButton[0] = new JButton();
            avatarButton[0].setContentAreaFilled(false);
            avatarButton[0].setBorderPainted(false);
            avatarButton[0].setBounds(63 * K, 60 * K, 125 * K, 125 * K);
            avatarButton[0].setBorderPainted(false);
            avatarButton[0].setIcon(avatar[0]);
            avatarButton[0].addActionListener(new avatarSwitchAction());

            Start = ToolKit.YaHeiButton("准备", 65 * K, 160 * K, 120 * K, 60 * K, 20 * K);
            Start.setBackground(ToolKit.bgc);
            Start.setContentAreaFilled(true);
            Start.setBounds(65 * K, 240 * K, 120 * K, 60 * K);
            Start.addActionListener(new RemoteGameAction());

            startPanel.add(avatarButton[0]);
            startPanel.add(Start);

            CenterPanel = RemotePanel;
            WestPanel.add(startPanel);
        }

        frame.add(WestPanel);
        frame.add(CenterPanel);
        WestPanel.repaint();
        CenterPanel.repaint();
        frame.setVisible(true);
    }


    JPanel infoSwitchPanel;
    JButton userInfo, readSave, rankList;

    JPanel saveSelectPanel;
    JButton[] records = new JButton[5];
    int saveMODE = 1;

    JButton[] sortKey = new JButton[3];
    JPanel sortKeyPanel;
    JTable rankListTable;
    DefaultTableModel defaultTableModel;
    Vector<String> tableHead = new Vector<>();
    Vector<Vector<String>> tableData = new Vector<>();

    //读取功能
    public void updateWestCenterPanelForSave()
    {
        if(WestPanel != null)
        {
            WestPanel.removeAll();
        }
        if(CenterPanel != null)
        {
            CenterPanel.removeAll();
        }
        WestPanel = new JPanel();
        WestPanel.repaint();
        CenterPanel = new JPanel();
        CenterPanel.repaint();
        WestPanel.setLayout(null);
        WestPanel.setBounds(0, 60 * K, 250 * K, 440 * K);
        WestPanel.setBorder(new EmptyBorder(-5, 0, -5, 0));

        CenterPanel.setLayout(null);
        CenterPanel.setBounds(250 * K, 60 * K, 550 * K, 440 * K);
        if(infoSwitchPanel != null)
        {
            infoSwitchPanel.removeAll();
        }
        infoSwitchPanel = new JPanel(new GridLayout(1, 3, 2, 0));
        infoSwitchPanel.setBounds(0, 0, 250 * K, 60 * K);
        infoSwitchPanel.setBackground(ToolKit.bgc);
        userInfo = ToolKit.YaHeiButton("用户", 0, 60 * K, 83 * K, 60 * K, 16 * Kp);
        readSave = ToolKit.YaHeiButton("读档", 0, 60 * K, 83 * K, 60 * K, 16 * Kp);
        rankList = ToolKit.YaHeiButton("排行", 0, 60 * K, 84 * K, 60 * K, 16 * Kp);
        userInfo.addActionListener(new savePanelAction());
        readSave.addActionListener(new savePanelAction());
        rankList.addActionListener(new savePanelAction());
        OneWhiteInfo(userInfo);
        infoSwitchPanel.add(userInfo);
        infoSwitchPanel.add(readSave);
        infoSwitchPanel.add(rankList);
        WestPanel.add(infoSwitchPanel);

        if(saveMODE == 1)
        {//用户信息
            OneWhiteInfo(userInfo);
            UserName[0] = new JTextField();
            UserName[0].setFont(ToolKit.YaHei);
            UserName[0].addFocusListener(new TextFieldFocus(UserName[0], "请输入用户名"));
            UserName[0].addFocusListener(new checkPlayerInfoAction());
            UserName[0].setBounds(50 * K, 120 * K, 150 * K, 50 * K);

            JButton jButton = ToolKit.YaHeiButton("查询信息", 50, 200, 150, 50, 20);
            jButton.setBackground(ToolKit.bgc);
            jButton.setContentAreaFilled(true);
            WestPanel.add(UserName[0]);
            WestPanel.add(jButton);
        }
        else if(saveMODE == 2)
        {//读档
            OneWhiteInfo(readSave);
            if(saveSelectPanel != null)
            {
                saveSelectPanel.removeAll();
            }
            saveSelectPanel = new JPanel();
            saveSelectPanel.setLayout(null);
            saveSelectPanel.setBounds(0, 60, 250 * K, 380 * K);

            for(int i = 0; i < 5; i++)
            {
                records[i] = ToolKit.YaHeiButton("存档" + (i + 1), 0, 76 * i, 250 * K, 76, 20);
                records[i].setContentAreaFilled(false);
                records[i].setForeground(ToolKit.bgc);
                records[i].addMouseListener(new saveSelectAction());
                saveSelectPanel.add(records[i]);
            }
            WestPanel.add(saveSelectPanel);
        }
        else
        {
            applaud.play();
            OneWhiteInfo(rankList);
            GameSystem.playerList = FileSystem.getAllPlayer();

            if(sortKeyPanel != null)
            {
                sortKeyPanel.removeAll();
            }
            sortKeyPanel = new JPanel();
            sortKeyPanel.setLayout(null);
            sortKeyPanel.setBounds(0, 60, 250 * K, 380 * K);

            tableHead = new Vector<>();
            tableHead.add("用户名");
            tableHead.add("点数");
            tableHead.add("胜利场次");
            tableHead.add("游戏场次");
            tableHead.add("胜率");

            defaultTableModel = new DefaultTableModel(tableData, tableHead)
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    return false;
                }
            };
            rankListTable = new JTable(defaultTableModel);
            JScrollPane rankPane = new JScrollPane(rankListTable);
            rankPane.setBounds(0, 0, 550, 440);


            for(int i = 0; i < 3; i++)
            {
                sortKey[i] = ToolKit.YaHeiButton("", 25, 60 * i + 60, 200, 60, 20);
                sortKey[i].setBackground(ToolKit.bgc);
                sortKey[i].setContentAreaFilled(true);
                sortKey[i].addActionListener(new rankListAction());
            }
            sortKey[0].setText("按点数排序");
            sortKey[1].setText("按胜场排序");
            sortKey[2].setText("按胜率排序");

            sortKeyPanel.add(sortKey[0]);
            sortKeyPanel.add(sortKey[1]);
            sortKeyPanel.add(sortKey[2]);

            WestPanel.add(sortKeyPanel);
            CenterPanel.add(rankPane);
        }
        frame.add(WestPanel);
        frame.add(CenterPanel);
        WestPanel.repaint();
        CenterPanel.repaint();
        frame.setVisible(true);
    }

    JButton[] th = new JButton[3];
    JTextField themeAccess;
    JButton display;
    ImageIcon Touhou=new ImageIcon(new ImageIcon("media/public/1.png").getImage()
            .getScaledInstance(470, 280, 1));
    ImageIcon Classic=new ImageIcon(new ImageIcon("media/public/2.png").getImage()
            .getScaledInstance(470, 280, 1));
    ImageIcon Minecraft=new ImageIcon(new ImageIcon("media/public/3.png").getImage()
            .getScaledInstance(470, 280, 1));

    public void updateThemeshop()
    {
        if(WestPanel != null)
        {
            WestPanel.removeAll();
        }
        if(CenterPanel != null)
        {
            CenterPanel.removeAll();
        }
        WestPanel = new JPanel();
        WestPanel.repaint();
        CenterPanel = new JPanel();
        CenterPanel.repaint();

        WestPanel.setBounds(0, 60 * K, 250 * K, 440 * K);
        WestPanel.setBorder(new EmptyBorder(-5, 0, -5, 0));
        CenterPanel.setBounds(250 * K, 60 * K, 550 * K, 440 * K);

        WestPanel.setLayout(null);
        CenterPanel.setLayout(null);

        themeAccess = new JTextField();
        themeAccess.addFocusListener(new TextFieldFocus(themeAccess, "请输入用户名"));
        themeAccess.setBounds(30, 20, 190, 50);
        themeAccess.setFont(ToolKit.YaHei(30));
        themeAccess.addFocusListener(new themeUserAction());

        th[0] = ToolKit.YaHeiButton("東方Project", 25, 80, 200, 100, 24);
        th[1] = ToolKit.YaHeiButton("古风经典", 25, 180, 200, 100, 24);
        th[2] = ToolKit.YaHeiButton("MineCraft", 25, 280, 200, 100, 24);
        th[0].setForeground(ToolKit.bgc);
        th[1].setForeground(ToolKit.bgc);
        th[2].setForeground(ToolKit.bgc);

        th[0].addMouseListener(new themeAction());
        th[1].addMouseListener(new themeAction());
        th[2].addMouseListener(new themeAction());

        th[1].setVisible(false);
        th[2].setVisible(false);

        display = new JButton();
        display.setContentAreaFilled(false);
        display.setBorderPainted(false);
        display.setFocusPainted(false);
        display.setBounds(40, 30, 470, 380);

        WestPanel.add(themeAccess);
        WestPanel.add(th[0]);
        WestPanel.add(th[1]);
        WestPanel.add(th[2]);
        CenterPanel.add(display);
        frame.add(WestPanel);
        frame.add(CenterPanel);
        WestPanel.repaint();
        CenterPanel.repaint();
        frame.setVisible(true);
    }

    public LaunchPanel()
    {
        initialize();
        updateNorthPanel();
        updateWestCenterPanelForGame();
        stylizeMusic(theme);
    }


    private void initialize()
    {
        theme = themes.TOUHOU;
        frame = new JFrame();
        Stylize(theme);
        frame.setBounds(100 * K, 100 * K, 800 * K, 500 * K);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setUndecorated(true);

        frame.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                MousePosition.x = e.getX();
                MousePosition.y = e.getY();
            }
        });//鼠标按下位置
        frame.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                Point p = frame.getLocation();
                frame.setLocation(p.x + e.getX() - MousePosition.x, p.y + e.getY() - MousePosition.y);
            }
        });//鼠标拖动位移
    }

    class NorthPanelAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)//NorthPanel按钮响应
        {
            if(e.getSource().equals(LOGO))
            //LOGO:转到帮助文档
            {
                try
                {
                    Desktop.getDesktop().browse(new URI("https://blog.chjrx0387.top/post/help-documentation-of-othello/"));
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
            }
            else if(e.getSource().equals(gameButton))
            //gameButton:WestPanel,CenterPanel->clear
            //modePanel,startPanel->WestPanel
            //
            {
                System.out.println("Game");
                updateWestCenterPanelForGame();
                OneWhiteNorth(gameButton);
            }
            else if(e.getSource().equals(saveButton))
            {
                System.out.println("Save");
                updateWestCenterPanelForSave();
                OneWhiteNorth(saveButton);
            }
            else if(e.getSource().equals(themeButton))
            {
                System.out.println("Theme");
                updateThemeshop();
                OneWhiteNorth(themeButton);
            }
            else if(e.getSource().equals(quitButton))
            {
                System.out.println("Launcher quited.");
                if(pveGamePanel != null && !pveGamePanel.QuittedSign)
                {//单人游戏进行时关闭启动器，强制存档结束
                    int choice = JOptionPane.showConfirmDialog(null, "是否存档棋局?", "保存", JOptionPane.YES_NO_CANCEL_OPTION);
                    if(choice == JOptionPane.YES_OPTION)
                    {
                        pveGamePanel.timer.stop();
                        //timer静止，跳转到存档界面
                        System.exit(0);
                    }
                    else if(choice == JOptionPane.NO_OPTION)
                    {
                        System.out.println("Game quited without saving");
                        System.exit(0);
                    }//取消关闭操作.
                }
                else
                {
                    frame.dispose();
                }
            }
        }
    }

    class modePanelAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)//NorthPanel按钮响应
        {
            if(e.getSource().equals(PVEButton))
            //gameButton:WestPanel,CenterPanel->clear
            //modePanel,startPanel->WestPanel
            //
            {
                System.out.println("PVE");
                curMode = 1;
                updateWestCenterPanelForGame();
            }
            else if(e.getSource().equals(PVPButton))
            {
                System.out.println("PVP");
                curMode = 2;
                updateWestCenterPanelForGame();
            }
            else if(e.getSource().equals(RemoteButton))
            {
                System.out.println("Remote");
                curMode = 3;
                updateWestCenterPanelForGame();
            }
        }
    }

    class savePanelAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)//NorthPanel按钮响应
        {
            if(e.getSource().equals(userInfo))
            //gameButton:WestPanel,CenterPanel->clear
            //modePanel,startPanel->WestPanel
            //
            {
                System.out.println("User Info");
                saveMODE = 1;
            }
            else if(e.getSource().equals(readSave))
            {
                System.out.println("Read Save");
                saveMODE = 2;
            }
            else if(e.getSource().equals(rankList))
            {
                System.out.println("Rank");
                saveMODE = 3;
            }
            updateWestCenterPanelForSave();
        }
    }

    class ColorSelectAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource().equals(ColorSelect))
            {
                isPlayerWhite = !isPlayerWhite;
                System.out.println("Player Color Changed To" + (isPlayerWhite ? "White" : "Black"));
                if(isPlayerWhite)
                {
                    ColorSelect.setText("玩家执白");
                    ColorSelect.setForeground(Color.black);
                    ColorSelect.setBackground(Color.white);
                }
                else
                {
                    ColorSelect.setText("玩家执黑");
                    ColorSelect.setBackground(Color.black);
                    ColorSelect.setForeground(Color.white);
                }
                ColorSelect.setContentAreaFilled(true);
            }
        }
    }

    class DifficultySelectAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource().equals(DifficultySelect))
            {
                difficulty = (difficulty + 1) % 4;
                DifficultySelect.setIcon(difficultyIcon[difficulty]);
                System.out.println("Difficulty Changed To " + difficulty);
            }
        }
    }

    class StartGameAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource().equals(Start))
            {
                if(curMode == 1)//PVE
                {
                    String gameName = GameName.getText();
                    String userName = UserName[0].getText();
                    if(gameName.equals("请输入游戏名"))
                    {
                        JOptionPane.showMessageDialog(null,
                                "请输入游戏名", "要素缺失", JOptionPane.ERROR_MESSAGE);
                    }
                    else if(userName.equals("请输入用户名"))
                    {
                        JOptionPane.showMessageDialog(null,
                                "请输入用户名", "要素缺失", JOptionPane.ERROR_MESSAGE);
                    }
                    else if(ToolKit.isSpecialChar(gameName))
                    {
                        JOptionPane.showMessageDialog(null,
                                "游戏名不能包含特殊字符", "格式错误", JOptionPane.ERROR_MESSAGE);
                    }
                    else if(ToolKit.isSpecialChar(userName))
                    {
                        JOptionPane.showMessageDialog(null,
                                "用户名不能包含特殊字符", "格式错误", JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        blackPlayer = isPlayerWhite ? new Player("AI Player") : new Player(userName);
                        whitePlayer = isPlayerWhite ? new Player(userName) : new Player("AI Player");
                        if(isPlayerWhite)
                        {
                            whitePlayer.avatarID = avatarID[0];
                            blackPlayer.avatarID = 26 + difficulty;
                        }//0~26为玩家头像选区
                        else
                        {
                            blackPlayer.avatarID = avatarID[0];
                            whitePlayer.avatarID = 26 + difficulty;
                        }//26~29为Bot头像选区(根据难度)
                        GameSystem.addPlayer(blackPlayer);
                        GameSystem.addPlayer(whitePlayer);
                        Game newGame = new Game(gameName, blackPlayer, whitePlayer, 1, difficulty);
                        GameSystem.addGame(newGame);
                        pveGamePanel = new PVEGamePanel(newGame, isPlayerWhite, difficulty, theme);
                    }
                }
                else if(curMode == 2)//PVP
                {
                    String gameName = GameName.getText();
                    String blackName = UserName[0].getText();
                    String whiteName = UserName[1].getText();
                    if(gameName.equals("请输入游戏名"))
                    {
                        JOptionPane.showMessageDialog(null,
                                "请输入游戏名", "要素缺失", JOptionPane.ERROR_MESSAGE);
                    }
                    else if(blackName.equals("请输入黑方名"))
                    {
                        JOptionPane.showMessageDialog(null,
                                "请输入黑方名", "要素缺失", JOptionPane.ERROR_MESSAGE);
                    }
                    else if(blackName.equals("请输入白方名"))
                    {
                        JOptionPane.showMessageDialog(null,
                                "请输入白方名", "要素缺失", JOptionPane.ERROR_MESSAGE);
                    }
                    else if(ToolKit.isSpecialChar(gameName))
                    {
                        JOptionPane.showMessageDialog(null,
                                "游戏名不能包含特殊字符", "格式错误", JOptionPane.ERROR_MESSAGE);
                    }
                    else if(ToolKit.isSpecialChar(blackName))
                    {
                        JOptionPane.showMessageDialog(null,
                                "黑方名不能包含特殊字符", "格式错误", JOptionPane.ERROR_MESSAGE);
                    }
                    else if(ToolKit.isSpecialChar(whiteName))
                    {
                        JOptionPane.showMessageDialog(null,
                                "白方名不能包含特殊字符", "格式错误", JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        blackPlayer = FileSystem.isPlayerExisted(blackName) ? FileSystem.readPlayer(blackName) : new Player(blackName);
                        whitePlayer = FileSystem.isPlayerExisted(whiteName) ? FileSystem.readPlayer(whiteName) : new Player(whiteName);
                        blackPlayer.avatarID = avatarID[0];
                        whitePlayer.avatarID = avatarID[1];
                        GameSystem.addPlayer(blackPlayer);
                        GameSystem.addPlayer(whitePlayer);
                        Game newGame = new Game(gameName, blackPlayer, whitePlayer, 2, -1);
                        GameSystem.addGame(newGame);
                        pvpGamePanel = new PVPGamePanel(newGame, theme);
                    }
                }
            }
        }
    }

    class RemoteGameAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource().equals(Start))
            {
                if(Start.getText().equals("准备"))
                {
                    Start.setText(isPlayerWhite?"等待黑方":"等待白方");
                    Start.setBackground(new Color(100,100,100));
                    GameName.setEditable(false);
                    UserName[0].setEditable(false);
                    ColorSelect.removeActionListener(CSA);
                    Message.clear();
                    Message.MyCol=isPlayerWhite?1:-1;
                    if(isPlayerWhite)
                    {
                        String  whiteName=UserName[0].getText();
                        whitePlayer = FileSystem.isPlayerExisted(whiteName) ? FileSystem.readPlayer(whiteName) : new Player(whiteName);
                        whitePlayer.avatarID = avatarID[0];
                        Message.message="$"+whitePlayer;
                    }
                    else
                    {
                        String blackName=UserName[0].getText();
                        blackPlayer = FileSystem.isPlayerExisted(blackName) ? FileSystem.readPlayer(blackName) : new Player(blackName);
                        blackPlayer.avatarID = avatarID[0];
                        Message.message="$"+blackPlayer;
                    }
                }
                else if(Start.getText().equals("等待黑方")||Start.getText().equals("等待白方"))
                {
                    Message.clear();
                    Start.setText("准备");
                    Start.setBackground(ToolKit.bgc);
                    GameName.setEditable(true);
                    UserName[0].setEditable(true);
                    ColorSelect.addActionListener(CSA);
                }
                else if(Start.getText().equals("开始游戏"))
                {
                    Game newGame=new Game("远程游戏",blackPlayer,whitePlayer,3,-1);
                    remoteGamePanel=new RemoteGamePanel(newGame,theme,isPlayerWhite);
                }
                if(isPlayerWhite)
                {
                    String whiteName = UserName[0].getText();
                    whitePlayer = FileSystem.isPlayerExisted(whiteName) ? FileSystem.readPlayer(whiteName) : new Player(whiteName);
                    whitePlayer.avatarID = avatarID[0];
                    Message.message = "$" + whitePlayer;
                }
                else
                {
                    String blackName = UserName[0].getText();
                    blackPlayer = FileSystem.isPlayerExisted(blackName) ? FileSystem.readPlayer(blackName) : new Player(blackName);
                    blackPlayer.avatarID = avatarID[0];
                    Message.message = "$" + blackPlayer;
                }
            }
        }
    }

    class readPlayerInfoAction implements FocusListener
    {
        @Override
        public void focusGained(FocusEvent e)
        {
            if(e.getSource().equals(UserName[0]))
            {
                if(curMode == 1)
                {
                    if(FileSystem.isPlayerExisted(UserName[0].getText()))
                    {
                        avatarID[0] = Objects.requireNonNull(FileSystem.readPlayer(UserName[0].getText())).avatarID;
                    }
                    else
                    {
                        avatarID[0] = 0;
                    }
                    avatarButton[0].setIcon(avatar[avatarID[0]]);
                }
                else if(curMode == 2)
                {
                    if(FileSystem.isPlayerExisted(UserName[0].getText()))
                    {
                        avatarID[0] = Objects.requireNonNull(FileSystem.readPlayer(UserName[0].getText())).avatarID;
                    }
                    else
                    {
                        avatarID[0] = 0;
                    }
                    avatarButton[0].setIcon(avatar[avatarID[0]]);
                    if(FileSystem.isPlayerExisted(UserName[1].getText()))
                    {
                        avatarID[1] = Objects.requireNonNull(FileSystem.readPlayer(UserName[1].getText())).avatarID;
                    }
                    else
                    {
                        avatarID[1] = 1;
                    }
                    avatarButton[1].setIcon(avatar[avatarID[1]]);
                }
                else
                {
                    if(curMode == 3)
                    {
                        if(FileSystem.isPlayerExisted(UserName[0].getText()))
                        {
                            avatarID[0] = Objects.requireNonNull(FileSystem.readPlayer(UserName[0].getText())).avatarID;
                        }
                        else
                        {
                            avatarID[0] = 0;
                        }
                        avatarButton[0].setIcon(avatar[avatarID[0]]);
                    }
                }
            }
        }

        @Override
        public void focusLost(FocusEvent e)
        {
            if(e.getSource().equals(UserName[0]))
            {
                if(curMode == 1)
                {
                    if(FileSystem.isPlayerExisted(UserName[0].getText()))
                    {
                        avatarID[0] = Objects.requireNonNull(FileSystem.readPlayer(UserName[0].getText())).avatarID;
                    }
                    else
                    {
                        avatarID[0] = 0;
                    }
                    avatarButton[0].setIcon(avatar[avatarID[0]]);
                }
                else if(curMode == 2)
                {
                    if(FileSystem.isPlayerExisted(UserName[0].getText()))
                    {
                        avatarID[0] = Objects.requireNonNull(FileSystem.readPlayer(UserName[0].getText())).avatarID;
                    }
                    else
                    {
                        avatarID[0] = 0;
                    }
                    avatarButton[0].setIcon(avatar[avatarID[0]]);
                    if(FileSystem.isPlayerExisted(UserName[1].getText()))
                    {
                        avatarID[1] = Objects.requireNonNull(FileSystem.readPlayer(UserName[1].getText())).avatarID;
                    }
                    else
                    {
                        avatarID[0] = 1;
                    }
                    avatarButton[1].setIcon(avatar[avatarID[1]]);
                }
            }
        }
    }

    class checkPlayerInfoAction implements FocusListener
    {

        @Override
        public void focusGained(FocusEvent e)
        {

        }

        @Override
        public void focusLost(FocusEvent e)
        {
            if(e.getSource().equals(UserName[0]))
            {
                String name = UserName[0].getText();
                if(FileSystem.isPlayerExisted(name))
                {
                    if(CenterPanel != null)
                    {
                        CenterPanel.removeAll();
                    }
                    CenterPanel = displayUserInfo(FileSystem.readPlayer(name));
                    frame.add(CenterPanel);
                    CenterPanel.repaint();
                }
                else
                {
                    if(CenterPanel != null)
                    {
                        CenterPanel.removeAll();
                    }
                    JButton notExist = ToolKit.YaHeiButton("该用户不存在!", 150, 100, 250, 100, 30);
                    notExist.setForeground(ToolKit.bgc);
                    CenterPanel.add(notExist);
                    frame.add(CenterPanel);
                    frame.setVisible(true);
                    CenterPanel.repaint();
                }
            }
        }
    }

    public JPanel displayUserInfo(Player player)
    {
        Stylize(theme);
        JPanel info = new JPanel();
        info.setLayout(null);
        info.setBounds(250 * K, 60 * K, 550 * K, 440 * K);
        JButton ava, name, score, wincount, gamecount, winrate;
        ava = new JButton();
        ava.setBounds(200, 30, 150, 150);
        ava.setBorderPainted(false);
        ava.setIcon(avatar[player.avatarID]);

        name = ToolKit.YaHeiButton(player.playerName, 125, 180, 300, 40, 24);
        name.setForeground(ToolKit.bgc);

        score = ToolKit.YaHeiButton("点数:" + player.scoreCount, 125, 220, 300, 40, 24);
        score.setForeground(ToolKit.bgc);

        wincount = ToolKit.YaHeiButton("胜利场次:" + player.winCount, 125, 260, 300, 40, 24);
        wincount.setForeground(ToolKit.bgc);

        gamecount = ToolKit.YaHeiButton("游戏场次:" + player.gameCount, 125, 300, 300, 40, 24);
        gamecount.setForeground(ToolKit.bgc);

        winrate = ToolKit.YaHeiButton("胜率:" + String.format("%.2f", player.getWinRate() * 100) + "%", 125, 340, 300, 40, 24);
        winrate.setForeground(ToolKit.bgc);

        info.add(name);
        info.add(ava);
        info.add(score);
        info.add(wincount);
        info.add(gamecount);
        info.add(winrate);
        return info;
    }

    class rankListAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            tableData.clear();
            List<Player> t;
            if(e.getSource().equals(sortKey[0]))//点数
            {
                t = Record.sortScoreCount();
                OneWhiteRank(sortKey[0]);
            }
            else if(e.getSource().equals(sortKey[1]))
            {
                t = Record.sortWinCount();
                OneWhiteRank(sortKey[1]);
            }
            else
            {
                t = Record.sortWinRate();
                OneWhiteRank(sortKey[2]);
            }

            for(Player p : t)
            {
                Vector<String> tmp = new Vector<>();
                tmp.add(p.playerName);
                tmp.add("" + p.scoreCount);
                tmp.add("" + p.winCount);
                tmp.add("" + p.gameCount);
                tmp.add("" + String.format("%.2f", p.getWinRate() * 100) + "%");
                //tableData.add(tmp);
                defaultTableModel.addRow(tmp);
            }
            rankListTable.setModel(defaultTableModel);
        }
    }

    class avatarSwitchAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource().equals(avatarButton[0]))
            {
                avatarID[0] = (avatarID[0] + 1) % 27;
                avatarButton[0].setIcon(avatar[avatarID[0]]);
            }
            if(e.getSource().equals(avatarButton[1]))
            {
                avatarID[1] = (avatarID[1] + 1) % 27;
                avatarButton[1].setIcon(avatar[avatarID[1]]);
            }
        }
    }

    class saveSelectAction implements MouseListener
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {

        }

        @Override
        public void mousePressed(MouseEvent e)
        {

        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            for(int i = 0; i < 5; i++)
            {
                if(e.getSource().equals(records[i]))
                {
                    Game newGame = FileSystem.readGame(i + 1);
                    if(newGame.gameMode == 1)
                    {
                        pveGamePanel = new PVEGamePanel(newGame, newGame.playerBlack.playerName.equals("AI Player"), newGame.gameDifficulty, theme);
                    }
                    if(newGame.gameMode == 2)
                    {
                        pvpGamePanel = new PVPGamePanel(newGame, theme);
                    }
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
            for(int i = 0; i < 5; i++)
            {
                if(e.getSource().equals(records[i]))
                {
                    OneBlueSave(records[i]);
                    previewRecords(i + 1);
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e)
        {

        }
    }

    Socket socket;
    Send send;
    Receive receive;
    Thread Send_thr;
    Thread Rec_thr;

    class ipCheckAction implements FocusListener
    {
        @Override
        public void focusGained(FocusEvent e)
        {
            connect.setText("");
        }

        @Override
        public void focusLost(FocusEvent e)
        {
            if(e.getSource().equals(GameName))
            {
                String ip = GameName.getText();
                if(!ip.contains("."))
                {
                    connect.setText("连接失败!请检查ip格式!");
                }
                else
                {
                    try
                    {
                        socket = new Socket(ip, 8888);
                        send = new Send(socket);
                        receive = new Receive(socket);
                        Send_thr = new Thread(send);
                        Rec_thr = new Thread(receive);
                        Send_thr.start();
                        Rec_thr.start();
                        System.out.println("Successfully connected to:" + ip);
                        connect.setText("已成功连接至" + ip);
                    }
                    catch(IOException exception)
                    {
                        exception.printStackTrace();
                    }
                }
            }
        }
    }

    class themeUserAction implements FocusListener
    {

        @Override
        public void focusGained(FocusEvent e)
        {

        }

        @Override
        public void focusLost(FocusEvent e)
        {
            if(e.getSource().equals(themeAccess))
            {
                String name = themeAccess.getText();
                if(FileSystem.isPlayerExisted(name))
                {
                    Player p = FileSystem.readPlayer(name);
                    if(p.scoreCount > 500)
                    {
                        th[1].setVisible(true);
                    }
                    else
                    {
                        th[1].setVisible(false);
                        th[2].setVisible(false);
                    }
                    th[2].setVisible(p.scoreCount > 1000);
                }
            }
        }
    }

    class themeAction implements MouseListener
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {}

        @Override
        public void mousePressed(MouseEvent e)
        {}

        @Override
        public void mouseReleased(MouseEvent e)
        {
            if(e.getSource().equals(th[0]))
            {
                theme=themes.TOUHOU;
                OneBlueTheme(th[0]);
            }
            else if(e.getSource().equals(th[1]))
            {
                theme=themes.CLASSIC;
                OneBlueTheme(th[1]);
            }
            else
            {
                theme=themes.MINECRAFT;
                OneBlueTheme(th[2]);
            }
            Stylize(theme);
            stylizeMusic(theme);
            updateThemeshop();
            NorthPanel.setBackground(ToolKit.bgc);
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
            if(e.getSource().equals(th[0]))
            {
                display.setIcon(Touhou);
            }
            else if(e.getSource().equals(th[1]))
            {
                display.setIcon(Classic);
            }
            else
            {
                display.setIcon(Minecraft);
            }
        }

        @Override
        public void mouseExited(MouseEvent e)
        {

        }
    }

    JButton[] avatarRec = new JButton[2];
    JButton gameInfo;
    JButton[] scoreInfo = new JButton[2];
    JButton[] nameInfo = new JButton[2];
    JButton[][] prev = new JButton[8][8];
    JPanel bgPanel, prevPanel;

    public void previewRecords(int id)
    {
        style.Stylize(theme);
        style.sstylize(theme);
        Game game = FileSystem.readGame(id);
        game.printBoard();
        if(CenterPanel != null)
        {
            CenterPanel.removeAll();
        }
        CenterPanel = new JPanel();
        CenterPanel.setLayout(null);
        CenterPanel.setBounds(250 * K, 60 * K, 550 * K, 440 * K);
        JLayeredPane CenterPane = new JLayeredPane();
        CenterPane.setLayout(null);


        gameInfo = ToolKit.YaHeiButton(game.gameName + " " + game.startTime + " " + (game.gameMode == 1 ? "单人" : "双人"), 0, 0, 550, 140, 24);
        gameInfo.setContentAreaFilled(true);
        gameInfo.setBackground(ToolKit.bgc);

        gameInfo = ToolKit.YaHeiButton(game.gameName + " " + game.startTime + " " + (game.gameMode == 1 ? "单人" : "双人"), 0, 0, 550, 140, 20);
        gameInfo.setContentAreaFilled(false);
        gameInfo.setForeground(ToolKit.bgc);

        avatarRec[0] = new JButton();
        avatarRec[1] = new JButton();
        avatarRec[0].setBackground(ToolKit.bgc);
        avatarRec[1].setBackground(ToolKit.bgc);
        avatarRec[0].setFocusPainted(false);
        avatarRec[1].setFocusPainted(false);
        avatarRec[0].setBorderPainted(false);
        avatarRec[1].setBorderPainted(false);
        avatarRec[0].setIcon(avatar[game.playerBlack.avatarID]);
        avatarRec[1].setIcon(avatar[game.playerWhite.avatarID]);
        avatarRec[0].setBounds(300, 140, 75, 75);
        avatarRec[1].setBounds(300, 290, 75, 75);

        scoreInfo[0] = ToolKit.YaHeiButton("得分:" + Step.countColor(game.Board, -1), 375, 140, 175, 75, 20);
        scoreInfo[1] = ToolKit.YaHeiButton("得分:" + Step.countColor(game.Board, 1), 375, 290, 175, 75, 20);
        scoreInfo[0].setContentAreaFilled(true);
        scoreInfo[1].setContentAreaFilled(true);
        scoreInfo[0].setBackground(ToolKit.bgc);
        scoreInfo[1].setBackground(ToolKit.bgc);

        nameInfo[0] = ToolKit.YaHeiButton(game.playerBlack.playerName, 300, 215, 250, 75, 20);
        nameInfo[1] = ToolKit.YaHeiButton(game.playerWhite.playerName, 300, 365, 250, 75, 20);
        nameInfo[0].setContentAreaFilled(true);
        nameInfo[1].setContentAreaFilled(true);
        nameInfo[0].setBackground(ToolKit.bgc);
        nameInfo[1].setBackground(ToolKit.bgc);

        if(bgPanel != null)
        {
            bgPanel.removeAll();
        }
        if(prevPanel != null)
        {
            prevPanel.removeAll();
        }
        bgPanel = new PanelWithBackground(BoardIcon);
        bgPanel.setBounds(0, 140, 300, 300);
        BoardIcon.setImage(BoardIcon.getImage().getScaledInstance(300, 300, 0));
        JLabel bgLabel = new JLabel()
        {
            @Override
            public void paint(Graphics g)
            {
                super.paint(g);
                BoardIcon.paintIcon(this, g, 0, 140);
            }
        };
        bgLabel.setBounds(0, 0, 300, 600);
        prevPanel = new JPanel(new GridLayout(8, 8));
        prevPanel.setBounds(20, 160, 260, 260);
        prevPanel.setOpaque(false);
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                prev[i][j] = new JButton();
                prev[i][j].setBorderPainted(false);
                prev[i][j].setContentAreaFilled(false);
                //prev[i][j].setBackground(null);
                if(game.Board[i][j] == -1)
                {
                    prev[i][j].setIcon(BlackIcon);
                }
                else if(game.Board[i][j] == 1)
                {
                    prev[i][j].setIcon(WhiteIcon);
                }
                else if(game.Board[i][j] == 0)
                {
                    prev[i][j].setIcon(null);
                }
                prevPanel.add(prev[i][j]);
            }
        }
        CenterPanel.add(gameInfo, 0);
        CenterPanel.add(avatarRec[0], 0);
        CenterPanel.add(avatarRec[1], 0);
        CenterPanel.add(scoreInfo[0], 0);
        CenterPanel.add(scoreInfo[1], 0);
        CenterPanel.add(nameInfo[0], 0);
        CenterPanel.add(nameInfo[1], 0);
        CenterPanel.add(bgLabel, -1);
        CenterPanel.add(prevPanel, 0);
        frame.add(CenterPanel);
        CenterPanel.repaint();
        frame.setVisible(true);
    }

    //这里实现按一个按钮，使其白底蓝字，同级其他按钮蓝底白字-针对NorthPanel
    public void OneWhiteNorth(JButton white)
    {
        gameButton.setContentAreaFilled(false);
        saveButton.setContentAreaFilled(false);
        themeButton.setContentAreaFilled(false);

        gameButton.setForeground(Color.white);
        saveButton.setForeground(Color.white);
        themeButton.setForeground(Color.white);

        white.setBackground(Color.white);
        white.setContentAreaFilled(true);
        white.setForeground(ToolKit.bgc);
    }

    //这里实现按一个按钮，使其白底蓝字，同级其他按钮蓝底白字-针对modePanel
    public void OneWhitemode(JButton white)
    {
        PVEButton.setContentAreaFilled(false);
        PVPButton.setContentAreaFilled(false);
        RemoteButton.setContentAreaFilled(false);

        PVEButton.setForeground(Color.white);
        PVPButton.setForeground(Color.white);
        RemoteButton.setForeground(Color.white);

        white.setBackground(Color.white);
        white.setContentAreaFilled(true);
        white.setForeground(ToolKit.bgc);
    }

    //这里实现按一个按钮，使其白底蓝字，同级其他按钮蓝底白字-针对infoSwitchPanel
    public void OneWhiteInfo(JButton white)
    {
        userInfo.setContentAreaFilled(false);
        readSave.setContentAreaFilled(false);
        rankList.setContentAreaFilled(false);

        userInfo.setForeground(Color.white);
        readSave.setForeground(Color.white);
        rankList.setForeground(Color.white);

        white.setBackground(Color.white);
        white.setContentAreaFilled(true);
        white.setForeground(ToolKit.bgc);
    }

    //这里实现按一个按钮，使其白底蓝字，同级其他按钮蓝底白字-针对rank
    public void OneWhiteRank(JButton white)
    {
        for(int i = 0; i < 3; i++)
        {
            sortKey[i].setContentAreaFilled(true);
            sortKey[i].setBackground(ToolKit.bgc);
            sortKey[i].setForeground(Color.white);
        }
        white.setBackground(Color.white);
        white.setForeground(ToolKit.bgc);
    }

    //这里实现按一个按钮，使其蓝底白字，同级其他按钮白底蓝字-针对records
    public void OneBlueSave(JButton blue)
    {
        for(int i = 0; i < 5; i++)
        {
            records[i].setContentAreaFilled(false);
            records[i].setForeground(ToolKit.bgc);
        }
        blue.setContentAreaFilled(true);
        blue.setBackground(ToolKit.bgc);
        blue.setForeground(Color.white);
    }

    public void OneBlueTheme(JButton blue)
    {
        for(int i = 0; i < 3; i++)
        {
            th[i].setContentAreaFilled(false);
            th[i].setForeground(ToolKit.bgc);
        }
        blue.setContentAreaFilled(true);
        blue.setBackground(ToolKit.bgc);
        blue.setForeground(Color.white);
    }
}