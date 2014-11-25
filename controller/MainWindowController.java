package controller;

import model.*;
import view.figures.*;
import view.*;

import java.awt.*;
import java.awt.event.*;
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


    // public static void showLabelInput(MouseEvent e, String label_figure_id) {
    //     main_window.showLabelInput(e, label_figure_id);
    // }

    // //target type of the arrow head clicked on
    // public static void showArcPopupMenu(MouseEvent e, String arc_id, int target_type) {
    //     Arc arc = (Arc)PetriNetController.getElementById(arc_id);
    //     JPopupMenu contextMenu = arc.getPopup(arc_id, target_type);
    //     contextMenu.show(e.getComponent(), e.getX(), e.getY());
    // }

    // public static void showTransitionPopupMenu(MouseEvent e, String transition_id) {
    //     Transition transition = (Transition)PetriNetController.getElementById(transition_id);
    //     JPopupMenu contextMenu = transition.getPopup(transition_id);
    //     contextMenu.show(e.getComponent(), e.getX(), e.getY());
    // }


    // public static void showPlacePopupMenu(MouseEvent e, String place_id) {
    //     Place place = (Place)PetriNetController.getElementById(place_id);
    //     JPopupMenu contextMenu = place.getPopup(place_id);
    //     contextMenu.show(e.getComponent(), e.getX(), e.getY());
    // }




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
                GlobalController.setSize(1.5);
                System.out.println("CREATE NEW");
                break;
            case "open":
                GlobalController.setSize(0.7);
                break;
            case "save":
                GlobalController.setSize(1.);
                break;
            case "exit":
                System.exit(0);
                break;
            //Mode buttons
            case "select_mode":
                GlobalController.setMode(GlobalController.MODE_SELECT);
                break;
            case "place_mode":
                GlobalController.setMode(GlobalController.MODE_PLACE);
                break;
            case "transition_mode":
                GlobalController.setMode(GlobalController.MODE_TRANSITION);
                break;
            case "arc_mode":
                GlobalController.setMode(GlobalController.MODE_ARC);
                break;

        }

        return;
    }


}