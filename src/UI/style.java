package UI;

import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class style
{
    public static ImageIcon BoardIcon;
    public static ImageIcon SelectIcon, PressedIcon;
    public static ImageIcon BlackIcon, WhiteIcon;
    public static ImageIcon BlackSelect, WhiteSelect;
    public static ImageIcon BlackPreview, WhitePreview;
    public static ImageIcon BlackSelectPreview, WhiteSelectPreview;
    public static ImageIcon BlackPressedPreview, WhitePressedPreview;
    public static ImageIcon[] difficultyIcon = new ImageIcon[4];
    public static ImageIcon[] avatar = new ImageIcon[30];

    public static AudioClip Bgm, applaud, clickBoard, win, lose, end;

    //这里实现不同主题下难度选择按钮的更换
    public static void Stylize(themes theme)
    {
        if(theme == themes.TOUHOU)
        {
            difficultyIcon[0] = new ImageIcon(new ImageIcon("media/touhou/Easy.png").getImage()
                    .getScaledInstance(150, 50, 1));
            difficultyIcon[1] = new ImageIcon(new ImageIcon("media/touhou/Normal.png").getImage()
                    .getScaledInstance(150, 50, 1));
            difficultyIcon[2] = new ImageIcon(new ImageIcon("media/touhou/Hard.png").getImage()
                    .getScaledInstance(150, 50, 1));
            difficultyIcon[3] = new ImageIcon(new ImageIcon("media/touhou/Lunatic.png").getImage()
                    .getScaledInstance(150, 50, 1));
            avatar = new ImageIcon[30];
            for(int i = 0; i < 30; i++)
            {
                avatar[i] = new ImageIcon(new ImageIcon("media/touhou/Avatar/avar" + i + ".png").getImage()
                        .getScaledInstance(150, 150, 1));
            }
            ToolKit.bgc = new Color(17, 111, 205);
        }
        else if(theme == themes.CLASSIC)
        {
            difficultyIcon[0] = new ImageIcon(new ImageIcon("media/classic/Easy.png").getImage()
                    .getScaledInstance(150, 50, 1));
            difficultyIcon[1] = new ImageIcon(new ImageIcon("media/classic/Normal.png").getImage()
                    .getScaledInstance(150, 50, 1));
            difficultyIcon[2] = new ImageIcon(new ImageIcon("media/classic/Hard.png").getImage()
                    .getScaledInstance(150, 50, 1));
            difficultyIcon[3] = new ImageIcon(new ImageIcon("media/classic/Lunatic.png").getImage()
                    .getScaledInstance(150, 50, 1));
            for(int i = 0; i < 30; i++)
            {
                avatar[i] = new ImageIcon(new ImageIcon("media/classic/Avatar/avar" + i + ".jpg").getImage()
                        .getScaledInstance(150, 150, 1));
            }
            ToolKit.bgc = new Color(245, 200, 43);
        }
        else if(theme == themes.MINECRAFT)
        {
            difficultyIcon[0] = new ImageIcon(new ImageIcon("media/minecraft/Easy.png").getImage()
                    .getScaledInstance(150, 50, 1));
            difficultyIcon[1] = new ImageIcon(new ImageIcon("media/minecraft/Normal.png").getImage()
                    .getScaledInstance(150, 50, 1));
            difficultyIcon[2] = new ImageIcon(new ImageIcon("media/minecraft/Hard.png").getImage()
                    .getScaledInstance(150, 50, 1));
            difficultyIcon[3] = new ImageIcon(new ImageIcon("media/minecraft/Lunatic.png").getImage()
                    .getScaledInstance(150, 50, 1));
            for(int i = 0; i < 30; i++)
            {
                avatar[i] = new ImageIcon(new ImageIcon("media/minecraft/Avatar/avar" + i + ".png").getImage()
                        .getScaledInstance(120, 120, 1));
            }

            ToolKit.bgc = new Color(94, 177, 39);
        }
    }

    public static void stylize(themes theme)
    {
        if(theme == themes.TOUHOU)
        {
            BoardIcon = new ImageIcon(new ImageIcon("media/touhou/Board.png").getImage()
                    .getScaledInstance(800, 800, 1));
            SelectIcon = new ImageIcon(new ImageIcon("media/touhou/SelectButton.png").getImage()
                    .getScaledInstance(88, 88, 1));
            BlackIcon = new ImageIcon(new ImageIcon("media/touhou/Black.png").getImage()
                    .getScaledInstance(88, 88, 1));
            WhiteIcon = new ImageIcon(new ImageIcon("media/touhou/White.png").getImage()
                    .getScaledInstance(88, 88, 1));
            PressedIcon = new ImageIcon(new ImageIcon("media/touhou/PressedButton.png").getImage()
                    .getScaledInstance(88, 88, 1));
            BlackSelect = new ImageIcon(new ImageIcon("media/touhou/BlackSelect.png").getImage()
                    .getScaledInstance(88, 88, 1));
            WhiteSelect = new ImageIcon(new ImageIcon("media/touhou/WhiteSelect.png").getImage()
                    .getScaledInstance(88, 88, 1));
            BlackPreview = new ImageIcon(new ImageIcon("media/touhou/BlackPreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            WhitePreview = new ImageIcon(new ImageIcon("media/touhou/WhitePreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            BlackSelectPreview = new ImageIcon(new ImageIcon("media/touhou/BlackSelectPreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            WhiteSelectPreview = new ImageIcon(new ImageIcon("media/touhou/WhiteSelectPreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            BlackPressedPreview = new ImageIcon(new ImageIcon("media/touhou/BlackPressedPreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            WhitePressedPreview = new ImageIcon(new ImageIcon("media/touhou/WhitePressedPreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            avatar = new ImageIcon[30];
            for(int i = 0; i < 30; i++)
            {
                avatar[i] = new ImageIcon(new ImageIcon("media/touhou/Avatar/avar" + i + ".png").getImage()
                        .getScaledInstance(80, 80, 1));
            }

        }
        else if(theme == themes.CLASSIC)
        {
            BoardIcon = new ImageIcon(new ImageIcon("media/classic/Board.png").getImage()
                    .getScaledInstance(800, 800, 1));
            SelectIcon = new ImageIcon(new ImageIcon("media/classic/SelectButton.png").getImage()
                    .getScaledInstance(88, 88, 1));
            BlackIcon = new ImageIcon(new ImageIcon("media/classic/Black.png").getImage()
                    .getScaledInstance(88, 88, 1));
            WhiteIcon = new ImageIcon(new ImageIcon("media/classic/White.png").getImage()
                    .getScaledInstance(88, 88, 1));
            PressedIcon = new ImageIcon(new ImageIcon("media/classic/PressedButton.png").getImage()
                    .getScaledInstance(88, 88, 1));
            BlackSelect = new ImageIcon(new ImageIcon("media/classic/BlackSelect.png").getImage()
                    .getScaledInstance(88, 88, 1));
            WhiteSelect = new ImageIcon(new ImageIcon("media/classic/WhiteSelect.png").getImage()
                    .getScaledInstance(88, 88, 1));
            BlackPreview = new ImageIcon(new ImageIcon("media/classic/BlackPreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            WhitePreview = new ImageIcon(new ImageIcon("media/classic/WhitePreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            BlackSelectPreview = new ImageIcon(new ImageIcon("media/classic/BlackSelectPreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            WhiteSelectPreview = new ImageIcon(new ImageIcon("media/classic/WhiteSelectPreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            BlackPressedPreview = new ImageIcon(new ImageIcon("media/classic/BlackPressedPreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            WhitePressedPreview = new ImageIcon(new ImageIcon("media/classic/WhitePressedPreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            avatar = new ImageIcon[30];
            for(int i = 0; i < 30; i++)
            {
                avatar[i] = new ImageIcon(new ImageIcon("media/classic/Avatar/avar" + i + ".jpg").getImage()
                        .getScaledInstance(80, 80, 1));
            }
        }
        else if(theme == themes.MINECRAFT)
        {
            BoardIcon = new ImageIcon(new ImageIcon("media/minecraft/Board.png").getImage()
                    .getScaledInstance(800, 800, 1));
            SelectIcon = new ImageIcon(new ImageIcon("media/minecraft/SelectButton.png").getImage()
                    .getScaledInstance(88, 88, 1));
            BlackIcon = new ImageIcon(new ImageIcon("media/minecraft/Black.png").getImage()
                    .getScaledInstance(88, 88, 1));
            WhiteIcon = new ImageIcon(new ImageIcon("media/minecraft/White.png").getImage()
                    .getScaledInstance(88, 88, 1));
            PressedIcon = new ImageIcon(new ImageIcon("media/minecraft/PressedButton.png").getImage()
                    .getScaledInstance(88, 88, 1));
            BlackSelect = new ImageIcon(new ImageIcon("media/minecraft/BlackSelect.png").getImage()
                    .getScaledInstance(88, 88, 1));
            WhiteSelect = new ImageIcon(new ImageIcon("media/minecraft/WhiteSelect.png").getImage()
                    .getScaledInstance(88, 88, 1));
            BlackPreview = new ImageIcon(new ImageIcon("media/minecraft/BlackPreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            WhitePreview = new ImageIcon(new ImageIcon("media/minecraft/WhitePreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            BlackSelectPreview = new ImageIcon(new ImageIcon("media/minecraft/BlackSelectPreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            WhiteSelectPreview = new ImageIcon(new ImageIcon("media/minecraft/WhiteSelectPreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            BlackPressedPreview = new ImageIcon(new ImageIcon("media/minecraft/BlackPressedPreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            WhitePressedPreview = new ImageIcon(new ImageIcon("media/minecraft/WhitePressedPreview.png").getImage()
                    .getScaledInstance(88, 88, 1));
            avatar = new ImageIcon[30];
            for(int i = 0; i < 30; i++)
            {
                avatar[i] = new ImageIcon(new ImageIcon("media/minecraft/Avatar/avar" + i + ".png").getImage()
                        .getScaledInstance(80, 80, 1));
            }
        }
    }

    public static void sstylize(themes theme)
    {
        if(theme == themes.TOUHOU)
        {
            BoardIcon = new ImageIcon(new ImageIcon("media/touhou/Board.png").getImage()
                    .getScaledInstance(800, 800, 1));
            SelectIcon = new ImageIcon(new ImageIcon("media/touhou/SelectButton.png").getImage()
                    .getScaledInstance(30, 30, 1));
            BlackIcon = new ImageIcon(new ImageIcon("media/touhou/Black.png").getImage()
                    .getScaledInstance(30, 30, 1));
            WhiteIcon = new ImageIcon(new ImageIcon("media/touhou/White.png").getImage()
                    .getScaledInstance(30, 30, 1));
            PressedIcon = new ImageIcon(new ImageIcon("media/touhou/PressedButton.png").getImage()
                    .getScaledInstance(30, 30, 1));
            BlackSelect = new ImageIcon(new ImageIcon("media/touhou/BlackSelect.png").getImage()
                    .getScaledInstance(30, 30, 1));
            WhiteSelect = new ImageIcon(new ImageIcon("media/touhou/WhiteSelect.png").getImage()
                    .getScaledInstance(30, 30, 1));
            BlackPreview = new ImageIcon(new ImageIcon("media/touhou/BlackPreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            WhitePreview = new ImageIcon(new ImageIcon("media/touhou/WhitePreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            BlackSelectPreview = new ImageIcon(new ImageIcon("media/touhou/BlackSelectPreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            WhiteSelectPreview = new ImageIcon(new ImageIcon("media/touhou/WhiteSelectPreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            BlackPressedPreview = new ImageIcon(new ImageIcon("media/touhou/BlackPressedPreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            WhitePressedPreview = new ImageIcon(new ImageIcon("media/touhou/WhitePressedPreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            avatar = new ImageIcon[30];
            for(int i = 0; i < 30; i++)
            {
                avatar[i] = new ImageIcon(new ImageIcon("media/touhou/Avatar/avar" + i + ".png").getImage()
                        .getScaledInstance(80, 80, 1));
            }

        }
        else if(theme == themes.CLASSIC)
        {
            BoardIcon = new ImageIcon(new ImageIcon("media/classic/Board.png").getImage()
                    .getScaledInstance(800, 800, 1));
            SelectIcon = new ImageIcon(new ImageIcon("media/classic/SelectButton.png").getImage()
                    .getScaledInstance(30, 30, 1));
            BlackIcon = new ImageIcon(new ImageIcon("media/classic/Black.png").getImage()
                    .getScaledInstance(30, 30, 1));
            WhiteIcon = new ImageIcon(new ImageIcon("media/classic/White.png").getImage()
                    .getScaledInstance(30, 30, 1));
            PressedIcon = new ImageIcon(new ImageIcon("media/classic/PressedButton.png").getImage()
                    .getScaledInstance(30, 30, 1));
            BlackSelect = new ImageIcon(new ImageIcon("media/classic/BlackSelect.png").getImage()
                    .getScaledInstance(30, 30, 1));
            WhiteSelect = new ImageIcon(new ImageIcon("media/classic/WhiteSelect.png").getImage()
                    .getScaledInstance(30, 30, 1));
            BlackPreview = new ImageIcon(new ImageIcon("media/classic/BlackPreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            WhitePreview = new ImageIcon(new ImageIcon("media/classic/WhitePreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            BlackSelectPreview = new ImageIcon(new ImageIcon("media/classic/BlackSelectPreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            WhiteSelectPreview = new ImageIcon(new ImageIcon("media/classic/WhiteSelectPreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            BlackPressedPreview = new ImageIcon(new ImageIcon("media/classic/BlackPreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            WhitePressedPreview = new ImageIcon(new ImageIcon("media/classic/WhitePreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            avatar = new ImageIcon[30];
            for(int i = 0; i < 30; i++)
            {
                avatar[i] = new ImageIcon(new ImageIcon("media/classic/Avatar/avar" + i + ".jpg").getImage()
                        .getScaledInstance(80, 80, 1));
            }
        }
        else if(theme == themes.MINECRAFT)
        {
            BoardIcon = new ImageIcon(new ImageIcon("media/minecraft/Board.png").getImage()
                    .getScaledInstance(800, 800, 1));
            SelectIcon = new ImageIcon(new ImageIcon("media/minecraft/SelectButton.png").getImage()
                    .getScaledInstance(30, 30, 1));
            BlackIcon = new ImageIcon(new ImageIcon("media/minecraft/Black.png").getImage()
                    .getScaledInstance(30, 30, 1));
            WhiteIcon = new ImageIcon(new ImageIcon("media/minecraft/White.png").getImage()
                    .getScaledInstance(30, 30, 1));
            PressedIcon = new ImageIcon(new ImageIcon("media/minecraft/PressedButton.png").getImage()
                    .getScaledInstance(30, 30, 1));
            BlackSelect = new ImageIcon(new ImageIcon("media/minecraft/BlackSelect.png").getImage()
                    .getScaledInstance(30, 30, 1));
            WhiteSelect = new ImageIcon(new ImageIcon("media/minecraft/WhiteSelect.png").getImage()
                    .getScaledInstance(30, 30, 1));
            BlackPreview = new ImageIcon(new ImageIcon("media/minecraft/BlackPreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            WhitePreview = new ImageIcon(new ImageIcon("media/minecraft/WhitePreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            BlackSelectPreview = new ImageIcon(new ImageIcon("media/minecraft/BlackSelectPreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            WhiteSelectPreview = new ImageIcon(new ImageIcon("media/minecraft/WhiteSelectPreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            BlackPressedPreview = new ImageIcon(new ImageIcon("media/minecraft/BlackPreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            WhitePressedPreview = new ImageIcon(new ImageIcon("media/minecraft/WhitePreview.png").getImage()
                    .getScaledInstance(30, 30, 1));
            avatar = new ImageIcon[30];
            for(int i = 0; i < 30; i++)
            {
                avatar[i] = new ImageIcon(new ImageIcon("media/minecraft/Avatar/avar" + i + ".png").getImage()
                        .getScaledInstance(80, 80, 1));
            }
        }
    }

    public static void stylizeMusic(themes theme)
    {
        try
        {
            //Bgm, applaud, clickBoard, win, lose, end
            applaud =Applet.newAudioClip(new File("media/public/Applaud.wav").toURL());
            clickBoard = Applet.newAudioClip(new File("media/public/ClickBoard.wav").toURL());
            win =Applet.newAudioClip(new File("media/public/Win.wav").toURL());
            lose =Applet.newAudioClip(new File("media/public/Lose.wav").toURL());
            end =Applet.newAudioClip(new File("media/public/end.wav").toURL());
            if(theme == themes.CLASSIC)
            {
                Bgm=Applet.newAudioClip(new File("media/classic/Background.wav").toURL());
            }
            else if(theme == themes.TOUHOU)
            {
                Bgm=Applet.newAudioClip(new File("media/touhou/Background.wav").toURL());
            }
            else
            {
                Bgm=Applet.newAudioClip(new File("media/minecraft/Background.wav").toURL());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
