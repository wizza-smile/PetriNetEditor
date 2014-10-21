package controller;

import view.*;

import javax.swing.*;


public class MainWindowController {

    GlobalController global_controller;
    MainWindow main_window;

    MainWindowController(GlobalController gctrl) {
        this.global_controller = gctrl;
    }

    public void startMainWindow() {
        main_window = new MainWindow(global_controller);
        main_window.init();
        main_window.setVisible(true);
    }

    // public MainWindow getMainWindow() {
    //     System.out.println("get MAINWINDOEW");
    //     return MainWindowController.this.main_window;
    // }

    public void injectCanvas(Canvas canvas) {
        this.main_window.injectCanvas(canvas);
    }


    public void setStatusBarText(String s) {
        main_window.setStatusBarText(s);
    }

}