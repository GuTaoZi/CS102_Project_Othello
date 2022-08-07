package UI;

import Elements.Game;
import Elements.Step;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;

import static UI.style.*;

import System.FileSystem;

public class SaveFrame extends JFrame implements ActionListener, MouseListener, FocusListener
{
    public Game game;
    public JPanel NorthPanel;
    public JPanel WestPanel;
    public JPanel CenterPanel;
    public themes theme;
    public JFrame frame;
    // 全局的位置变量，用于表示鼠标在窗口上的位置
    public static Point MousePosition = new Point();

    public JButton LOGO, quitButton;

    public static ImageIcon logo = new ImageIcon(new ImageIcon("media/public/Logo.jpg").getImage()
            .getScaledInstance(50, 50, 1));

    public boolean saved = false;

    public void updateNorthPanel()
    {
        NorthPanel = new JPanel();
        NorthPanel.setLayout(null);
        NorthPanel.setBounds(0, 0, 800, 60);
        NorthPanel.setBackground(ToolKit.bgc);

        LOGO = new JButton("", logo);
        LOGO.setBounds(50, 0, 50, 50);
        LOGO.setBorderPainted(false);
        LOGO.setFocusPainted(false);
        LOGO.setContentAreaFilled(false);

        quitButton = ToolKit.YaHeiButton("退出", 720, 0, 80, 60, 20);

        LOGO.addActionListener(new NorthPanelAction());
        quitButton.addActionListener(new NorthPanelAction());

        NorthPanel.add(LOGO);
        NorthPanel.add(quitButton);

        frame.add(NorthPanel);
    }

    JPanel infoSwitchPanel;
    JButton readSave;
    JPanel saveSelectPanel;
    JButton[] records = new JButton[5];

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
        WestPanel.setBounds(0, 60, 250, 440);
        WestPanel.setBorder(new EmptyBorder(-5, 0, -5, 0));

        CenterPanel.setLayout(null);
        CenterPanel.setBounds(250, 60, 550, 440);
        if(infoSwitchPanel != null)
        {
            infoSwitchPanel.removeAll();
        }
        infoSwitchPanel = new JPanel(new GridLayout(1, 3, 2, 0));
        infoSwitchPanel.setBounds(0, 0, 250, 60);
        infoSwitchPanel.setBackground(ToolKit.bgc);
        readSave = ToolKit.YaHeiButton("读档", 0, 60, 83, 60, 16);
        infoSwitchPanel.add(readSave);
        WestPanel.add(infoSwitchPanel);

        if(saveSelectPanel != null)
        {
            saveSelectPanel.removeAll();
        }
        saveSelectPanel = new JPanel();
        saveSelectPanel.setLayout(null);
        saveSelectPanel.setBounds(0, 60, 250, 380);

        for(int i = 0; i < 5; i++)
        {
            records[i] = ToolKit.YaHeiButton("存档" + (i+1), 0, 76 * i, 250, 76, 20);
            records[i].setContentAreaFilled(false);
            records[i].setForeground(ToolKit.bgc);
            records[i].addMouseListener(new saveSelectAction());
            saveSelectPanel.add(records[i]);
        }
        WestPanel.add(saveSelectPanel);
        frame.add(WestPanel);
        frame.add(CenterPanel);
        WestPanel.repaint();
        CenterPanel.repaint();
        frame.setVisible(true);
    }

    public SaveFrame(Game game, themes theme)
    {
        this.theme = theme;
        this.game = game;
        initialize();
        updateNorthPanel();
        updateWestCenterPanelForSave();
    }

    private void initialize()
    {
        theme = themes.TOUHOU;
        frame = new JFrame();
        frame.setBounds(100, 100, 800, 500);
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

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    @Override
    public void focusGained(FocusEvent e)
    {
        Stylize(theme);
        sstylize(theme);
    }

    @Override
    public void focusLost(FocusEvent e)
    {

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
            else if(e.getSource().equals(quitButton))
            {
                Stylize(theme);
                stylize(theme);
                System.out.println("SaveFrame quited.");
                frame.dispose();
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
                    int opt = JOptionPane.showConfirmDialog(null, "将" + game.gameName + "存入到" + (i + 1) + "号存档槽吗?", "存档确认", JOptionPane.OK_CANCEL_OPTION);
                    if(opt == JOptionPane.OK_OPTION)
                    {
                        FileSystem.writeGame(game, i + 1);
                        JOptionPane.showMessageDialog(null, "已成功将" + game.gameName + "存入到" + (i + 1) + "号存档槽", "存档成功", JOptionPane.OK_OPTION);
                        saved=true;
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
                    System.out.println("preview:" + (i + 1));
                    previewRecords(i + 1);
                }
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
        CenterPanel.setBounds(250, 60, 550, 440);
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
        avatarRec[0].setBorderPainted(false);
        avatarRec[1].setBorderPainted(false);
        avatarRec[0].setIcon(avatar[game.playerBlack.avatarID ]);
        avatarRec[1].setIcon(avatar[game.playerWhite.avatarID ]);
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

        System.out.println("?");
    }
}