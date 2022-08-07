package UI;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolKit
{
    public static Color bgc = new Color(17, 111, 205);//BackGroundColor

    public static Font YaHei = new Font("微软雅黑", Font.BOLD, 20);

    public static Font YaHei(int size)
    {
        return new Font("微软雅黑", Font.BOLD, size);
    }

    public static JButton YaHeiButton(String text, int x, int y, int w, int h, int size)
    {
        JButton button = new JButton(text);
        button.setFont(YaHei(size));
        button.setForeground(Color.white);
        button.setBounds(x,y,w,h);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setVisible(true);
        return button;
    }

    public static boolean isSpecialChar(String str)
    {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;,\\[\\].<>/?！￥…（）—【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }
}