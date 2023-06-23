package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public static String lang = "ru";
    public static String country = "RU";
    public static Locale loc = new Locale(lang, country);
    public static ResourceBundle res = ResourceBundle.getBundle("resources", loc);

    void buttonsLocalize() {
        UIManager.put("OptionPane.yesButtonText", res.getString("yesButtonText"));
        UIManager.put("OptionPane.noButtonText", res.getString("noButtonText"));
        UIManager.put("OptionPane.cancelButtonText", res.getString("cancelButtonText"));
        UIManager.put("OptionPane.okButtonText", res.getString("okButtonText"));

        UIManager.put("InternalFrame.iconButtonToolTip", res.getString("minimizeButtonText"));
        UIManager.put("InternalFrame.maxButtonToolTip", res.getString("maximizeButtonText"));
        UIManager.put("InternalFrame.closeButtonToolTip", res.getString("closeButtonText"));

        UIManager.put("InternalFrameTitlePane.minimizeButtonAccessibleName", res.getString("minimizeButtonText"));
        UIManager.put("InternalFrameTitlePane.maximizeButtonAccessibleName", res.getString("maximizeButtonText"));
        UIManager.put("InternalFrameTitlePane.closeButtonAccessibleName", res.getString("closeButtonText"));

        UIManager.put("InternalFrameTitlePane.restoreButtonText", res.getString("maximizeButtonText"));
        UIManager.put("InternalFrameTitlePane.moveButtonText", res.getString("moveButtonText"));
        UIManager.put("InternalFrameTitlePane.sizeButtonText", res.getString("sizeButtonText"));
        UIManager.put("InternalFrameTitlePane.minimizeButtonText", res.getString("minimizeButtonText"));
        UIManager.put("InternalFrameTitlePane.maximizeButtonText", res.getString("maximizeButtonText"));
        UIManager.put("InternalFrameTitlePane.closeButtonText", res.getString("closeButtonText"));
    }

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);
        setContentPane(desktopPane);


        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        buttonsLocalize();
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(res.getString("logMessageDefault"));
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    public void exitApplication() {
        int answer = JOptionPane.showConfirmDialog(null,
                res.getString("exitConfirm"), res.getString("exitItem"), JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            Runtime.getRuntime().exit(0);
        }
    }

//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = new JMenu(res.getString("lookAndFeelMenu"));
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                res.getString("lookAndFeelMenuDescript"));

        {
            JMenuItem systemLookAndFeel = new JMenuItem(res.getString("systemLookAndFeel"), KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem(res.getString("crossplatformLookAndFeel"), KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu(res.getString("testMenu"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                res.getString("testCommands"));

        {
            JMenuItem addLogMessageItem = new JMenuItem(res.getString("addLogMessageItem"), KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug(res.getString("logMessageNewStr"));
            });
            testMenu.add(addLogMessageItem);
        }

        JMenu exitMenu = new JMenu(res.getString("exitMenu"));
        testMenu.setMnemonic(KeyEvent.VK_E);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem exitItem = new JMenuItem(res.getString("exitItem"), KeyEvent.VK_S);
            exitItem.addActionListener((event) -> {
                exitApplication();
            });
            exitMenu.add(exitItem);
        }

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(exitMenu);
        return menuBar;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
