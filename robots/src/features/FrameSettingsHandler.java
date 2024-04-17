package features;

import gui.GameWindow;
import gui.LogWindow;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import gui.MainApplicationFrame;
import org.w3c.dom.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class FrameSettingsHandler {
    public static String projHomeDir = System.getProperty("user.dir");
    public static String frameCfgPath = projHomeDir + "/robots/src/frame_cfg.xml";

    public void saveAppFrameState(MainApplicationFrame frameMain) {
        Properties frameGeom = new Properties();
        try {
            // Save main frame state
            String prefix = frameMain.getClass().toString().replaceAll("^class ", "");
            frameGeom.setProperty(prefix + "X", String.valueOf(frameMain.getBounds().x));
            frameGeom.setProperty(prefix + "Y", String.valueOf(frameMain.getBounds().y));
            frameGeom.setProperty(prefix + "Width", String.valueOf(frameMain.getBounds().width));
            frameGeom.setProperty(prefix + "Height", String.valueOf(frameMain.getBounds().height));
            frameGeom.setProperty(prefix + "Iconified", String.valueOf(frameMain.getExtendedState()));

            // Save internal frames state
            JInternalFrame[] internalFrames = frameMain.desktopPane.getAllFrames();
            for (JInternalFrame internalFrame : internalFrames) {
                prefix = internalFrame.getClass().toString().replaceAll("^class ", "");
                frameGeom.setProperty(prefix + "X", String.valueOf(internalFrame.getBounds().x));
                frameGeom.setProperty(prefix + "Y", String.valueOf(internalFrame.getBounds().y));
                frameGeom.setProperty(prefix + "Width", String.valueOf(internalFrame.getBounds().width));
                frameGeom.setProperty(prefix + "Height", String.valueOf(internalFrame.getBounds().height));
                frameGeom.setProperty(prefix + "Iconified", String.valueOf(internalFrame.isIcon()));
            }

            // Store properties to XML
            frameGeom.storeToXML(new FileOutputStream(frameCfgPath), "Frame configuration");

            System.out.println("Frame configuration stored to " + frameCfgPath);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restoreAppFrameState(MainApplicationFrame frameMain) {
        Properties frameGeom = new Properties();
        try {
            frameGeom.loadFromXML(new FileInputStream(frameCfgPath));

            // Restore main frame state
            String prefix = frameMain.getClass().toString().replaceAll("^class ", "");
            frameMain.setBounds(
                    Integer.parseInt(frameGeom.getProperty(prefix + "X")),
                    Integer.parseInt(frameGeom.getProperty(prefix + "Y")),
                    Integer.parseInt(frameGeom.getProperty(prefix + "Width")),
                    Integer.parseInt(frameGeom.getProperty(prefix + "Height"))
            );
            frameMain.setExtendedState(
                    Integer.parseInt(frameGeom.getProperty(prefix + "Iconified"))
            );

            // Restore internal frames state
            JInternalFrame[] internalFrames = frameMain.desktopPane.getAllFrames();
            for (JInternalFrame internalFrame : internalFrames) {
                prefix = internalFrame.getClass().toString().replaceAll("^class ", "");
                internalFrame.setBounds(
                        Integer.parseInt(frameGeom.getProperty(prefix + "X")),
                        Integer.parseInt(frameGeom.getProperty(prefix + "Y")),
                        Integer.parseInt(frameGeom.getProperty(prefix + "Width")),
                        Integer.parseInt(frameGeom.getProperty(prefix + "Height"))
                );
                internalFrame.setIcon(
                        Boolean.parseBoolean(frameGeom.getProperty(prefix + "Iconified"))
                );
            }
        }
        catch (IOException | PropertyVetoException e) {
            e.printStackTrace();
        }
    }
}
