package controller;

import view.*;

import javax.swing.*;


public class MainWindowController {

    // GlobalController global_controller;
    static MainWindow main_window;

    // MainWindowController(GlobalController gctrl) {
    //     this.global_controller = gctrl;
    // }

    public static void startMainWindow() {
        main_window = new MainWindow();
        main_window.init();
        main_window.setVisible(true);
    }

    // public MainWindow getMainWindow() {
    //     System.out.println("get MAINWINDOEW");
    //     return MainWindowController.this.main_window;
    // }

    public static void injectCanvas(Canvas canvas) {
        main_window.injectCanvas(canvas);
    }


    public static void setStatusBarText(String s) {
        main_window.setStatusBarText(s);
    }

}