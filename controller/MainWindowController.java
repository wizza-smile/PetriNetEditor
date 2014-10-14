package controller;

import view.*;

import javax.swing.*;


public class MainWindowController {

    GlobalController gctrl;
    MainWindow mwnd;

    MainWindowController(GlobalController gctrl) {
        this.gctrl = gctrl;
    }

    public void startMainWindow() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mwnd = new MainWindow(gctrl);
                mwnd.setVisible(true);
            }
        });
    }

}