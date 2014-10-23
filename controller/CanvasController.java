package controller;

import model.*;
import view.*;


import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.*;

public class CanvasController {

    // GlobalController global_controller;
    // ContainedElements containedElements;

    static Canvas canvas;
    // MainWindow main_window;

    // CanvasController(GlobalController gctrl) {
    //     this.global_controller = gctrl;
    //     // this.main_window = this.global_controller.mainWindow_controller.main_window;
    // }


    public static Canvas createCanvas() {
        canvas = new Canvas();

        return canvas;
    }


    public static void mousePressed(MouseEvent e) {
        controller.MainWindowController.setStatusBarText("CanvasController");
        controller.PetriNetController.addPetriNetElement();

        canvas.repaint();


        // switch (global_controller.mode) {
        //     case Global.PLACEMODE:
        //         addFigure(Global.PLACEMODE);//, snapPointToGrid(e.getPoint())
        //         main_window.setStatusBarText("addFigure");
        //         break;
        //     // case Global.TRANSITIONMODE:
        //     //     addFigure(Global.TRANSITIONMODE, snapPointToGrid(e.getPoint()));
        //     //     break;
        //     // case Global.NORMALARCMODE:
        //     //     addArc(e.getPoint());
        //     //     break;

        //     default:
        //         break;
        // }

    }



}