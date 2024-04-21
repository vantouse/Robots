package gui;

import java.awt.*;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

public class PositionWindow extends JInternalFrame implements Observer {

    private final TextArea field;
    private RobotCore robot;

    public PositionWindow(RobotCore robotPW) {
        super(MainApplicationFrame.langResources.getString("positionWindow"), true, true, true, true);

        robot = robotPW;
        field = new TextArea("");
        field.setSize(200, 500);
        robot.addObserver(this);
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                updatePosition();
            }
        }, 0, 10);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(field, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
//        setVisible(true);
    }

    private void updatePosition() {
        String content = "X: " + " " +
                robot.getRobotPositionX() + "\n" +
                "Y: " + " " +
                robot.getRobotPositionY() + "\n";
        field.setText(content);
        field.invalidate();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof RobotCore) EventQueue.invokeLater(this::updatePosition);
    }

}
