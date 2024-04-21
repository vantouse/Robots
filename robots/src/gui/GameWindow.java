package gui;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    private RobotCore m_robot;
    private final GameVisualizer m_visualizer;
    public GameWindow() 
    {
        super(MainApplicationFrame.langResources.getString("mainWindow"), true, true, true, true);
        m_robot = new RobotCore();
        m_visualizer = new GameVisualizer(m_robot);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public RobotCore getActiveRobot() {
        return m_robot;
    }
}
