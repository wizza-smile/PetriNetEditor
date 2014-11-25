package controller;

import model.*;
import view.figures.*;
import view.*;
import parser.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;
import java.io.File;


public class MainWindowController {

    public static MainWindow main_window;
    static File current_directory;



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


    public static JViewport getViewport() {
        return main_window.canvasPane.getViewport();
    }


    public static Rectangle getViewportRectangle() {
        Double upper_left_x = getViewport().getViewPosition().getX();
        Double upper_left_y = getViewport().getViewPosition().getY();
        Double lower_right_x = getViewport().getSize().getWidth() + getViewport().getViewPosition().getX();
        Double lower_right_y = getViewport().getSize().getHeight() + getViewport().getViewPosition().getY();

        Double width = lower_right_x - upper_left_x;
        Double height = lower_right_y - upper_left_y;

        return new Rectangle(upper_left_x.intValue(), upper_left_y.intValue(), width.intValue(), height.intValue());
    }


    public static void executeButtonBarAction(String button_id) {
        switch (button_id) {
            //File buttons
            case "create_new":
                System.out.println("CREATE NEW");
                break;
            case "open":
                final JFileChooser fileChooser = new JFileChooser(current_directory);
                fileChooser.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent e) {

                    current_directory = fileChooser.getCurrentDirectory();
                    System.out.println(fileChooser.getCurrentDirectory());

                  }
                });
                int returnVal = fileChooser.showOpenDialog(main_window);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    // System.out.println( file );
                    final String[] args = {file.toString()};
                    parser.PNMLParser.main(args);
                }
                break;
            case "save":
                GlobalController.setSize(1.);
                break;
            case "exit":
                System.exit(0);
                break;
            //Mode buttons
            case "select_mode":
                GlobalController.setMode(GlobalController.ACTION_SELECT);
                break;
            case "place_mode":
                GlobalController.setMode(GlobalController.ACTION_PLACE);
                break;
            case "transition_mode":
                GlobalController.setMode(GlobalController.ACTION_TRANSITION);
                break;
            case "arc_mode":
                GlobalController.setMode(GlobalController.ACTION_ARC);
                break;

        }

        return;
    }


}