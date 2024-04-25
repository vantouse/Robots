package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

import log.Logger;

import features.FrameSettingsHandler;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    // private final JDesktopPane desktopPane = new JDesktopPane();
    // (unable to pass private object into FrameSettingsHandler functions as argument)
    public final JDesktopPane desktopPane = new JDesktopPane();
    public final LogWindow logWindow = createLogWindow();
//    public final LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
    public final GameWindow gameWindow = new GameWindow();
    public static String lang = "ru";
    public static String country = "RU";
    public static Locale loc = new Locale(lang, country);
    public static String projHomeDir = System.getProperty("user.dir");
    public static ResourceBundle langResources = ResourceBundle.getBundle("resources", loc);
    public static FrameSettingsHandler fsh = new FrameSettingsHandler();

    void buttonsLocalize() {
        UIManager.put("OptionPane.yesButtonText", langResources.getString("yesButtonText"));
        UIManager.put("OptionPane.noButtonText", langResources.getString("noButtonText"));
        UIManager.put("OptionPane.cancelButtonText", langResources.getString("cancelButtonText"));
        UIManager.put("OptionPane.okButtonText", langResources.getString("okButtonText"));

        UIManager.put("InternalFrame.iconButtonToolTip", langResources.getString("minimizeButtonText"));
        UIManager.put("InternalFrame.maxButtonToolTip", langResources.getString("maximizeButtonText"));
        UIManager.put("InternalFrame.closeButtonToolTip", langResources.getString("closeButtonText"));

        UIManager.put("InternalFrameTitlePane.minimizeButtonAccessibleName", langResources.getString("minimizeButtonText"));
        UIManager.put("InternalFrameTitlePane.maximizeButtonAccessibleName", langResources.getString("maximizeButtonText"));
        UIManager.put("InternalFrameTitlePane.closeButtonAccessibleName", langResources.getString("closeButtonText"));

        UIManager.put("InternalFrameTitlePane.restoreButtonText", langResources.getString("maximizeButtonText"));
        UIManager.put("InternalFrameTitlePane.moveButtonText", langResources.getString("moveButtonText"));
        UIManager.put("InternalFrameTitlePane.sizeButtonText", langResources.getString("sizeButtonText"));
        UIManager.put("InternalFrameTitlePane.minimizeButtonText", langResources.getString("minimizeButtonText"));
        UIManager.put("InternalFrameTitlePane.maximizeButtonText", langResources.getString("maximizeButtonText"));
        UIManager.put("InternalFrameTitlePane.closeButtonText", langResources.getString("closeButtonText"));
    }

    public MainApplicationFrame() throws PropertyVetoException, IOException {
        // Make the big window be indented 50 pixels from each edge of the screen
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        setBounds(inset, inset,
//                screenSize.width - inset * 2,
//                screenSize.height - inset * 2);

        // Set up main application frame
        this.setVisible(true);
        setContentPane(desktopPane);
        addWindow(logWindow);
        addWindow(gameWindow);

        // Restore state of all active frames
        fsh.restoreAppFrameState(this);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        buttonsLocalize();
    }

    protected LogWindow createLogWindow() throws PropertyVetoException {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        Logger.debug(langResources.getString("logMessageDefault"));
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    public void exitApplication() throws IOException {
        // Confirm saving frame settings
        int option_fsh = JOptionPane.showConfirmDialog(null,
                langResources.getString("fshConfirm"), langResources.getString("fshItem"),
                JOptionPane.YES_NO_OPTION);
        if (option_fsh == JOptionPane.YES_OPTION) {
            fsh.saveAppFrameState(this);
        }

        //Confirm exit
        int answer = JOptionPane.showConfirmDialog(null,
                langResources.getString("exitConfirm"), langResources.getString("exitItem"), JOptionPane.YES_NO_OPTION);
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

        JMenu lookAndFeelMenu = new JMenu(langResources.getString("lookAndFeelMenu"));
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                langResources.getString("lookAndFeelMenuDescript"));

        {
            JMenuItem systemLookAndFeel = new JMenuItem(langResources.getString("systemLookAndFeel"), KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem(langResources.getString("crossplatformLookAndFeel"), KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu(langResources.getString("testMenu"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                langResources.getString("testCommands"));

        {
            JMenuItem addLogMessageItem = new JMenuItem(langResources.getString("addLogMessageItem"), KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug(langResources.getString("logMessageNewStr"));
            });
            testMenu.add(addLogMessageItem);
        }

        JMenu exitMenu = new JMenu(langResources.getString("exitMenu"));
        testMenu.setMnemonic(KeyEvent.VK_E);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem exitItem = new JMenuItem(langResources.getString("exitItem"), KeyEvent.VK_S);
            exitItem.addActionListener((event) -> {
                try {
                    exitApplication();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
