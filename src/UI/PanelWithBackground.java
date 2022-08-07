package UI;

import javax.swing.*;
import java.awt.*;

public class PanelWithBackground extends JPanel
{
    Image img;
    public PanelWithBackground(ImageIcon icon)
    {
        img = icon.getImage();
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
