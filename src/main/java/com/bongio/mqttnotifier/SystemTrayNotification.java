package com.bongio.mqttnotifier;

import java.awt.*;

public class SystemTrayNotification  {
    private TrayIcon trayIcon;

    public SystemTrayNotification(String toolTip) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();

        Image image = Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("bell.png"));

        trayIcon = new TrayIcon(image, toolTip);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip(toolTip);

        PopupMenu popupMenu = new PopupMenu();

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> {
            tray.remove(trayIcon);
            System.exit(0);
        });

        popupMenu.add(exitItem);

        trayIcon.setPopupMenu(popupMenu);

        tray.add(trayIcon);
    }

    public void notify(String title, String message) {
        trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
    }
    public void displayError(String title, String message) {
        trayIcon.displayMessage(title, message, TrayIcon.MessageType.ERROR);
    }

    public void setToolTip(String toolTip) {
        trayIcon.setToolTip(toolTip);
    }
}
