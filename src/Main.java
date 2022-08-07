import UI.LaunchPanel;

import java.awt.*;

public class Main
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() ->
        {
            try
            {
                LaunchPanel window = new LaunchPanel();
                window.frame.setVisible(true);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        });
    }
}
