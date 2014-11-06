package controller;

import view.*;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;


public class MainWindowController {

    public static MainWindow main_window;


    public static void startMainWindow() {
        main_window = new MainWindow();
        main_window.init();
        main_window.setVisible(true);
    }

    public static MainWindow getMainWindow() {
        return main_window;
    }


    public static void injectCanvas(view.Canvas canvas) {
        main_window.injectCanvas(canvas);
    }


    public static void setStatusBarText(String s) {
        main_window.setStatusBarText(s);
    }

    public static Dimension getViewportSize() {
        return main_window.canvasPane.getViewport().getSize();
    }


    public static void executeButtonBarAction(String button_id) {
        switch (button_id) {
            //File buttons
            case "create_new":
                System.out.println("CREATE NEW");
                break;
            case "exit":
                System.exit(0);
                break;
            //Mode buttons
            case "select_mode":
                GlobalController.mode = GlobalController.MODE_SELECT;
                break;
            case "place_mode":
                GlobalController.mode = GlobalController.MODE_PLACE;
                break;

        }

        return;
    }

}