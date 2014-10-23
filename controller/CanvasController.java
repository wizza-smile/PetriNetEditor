package controller;

import model.*;
import view.*;


import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.*;

public class CanvasController {

    static Canvas canvas;


    public static Canvas createCanvas() {
        canvas = new Canvas();

        return canvas;
    }


    public static void mousePressed(MouseEvent e) {

        switch (GlobalController.mode) {
            case  GlobalController.MODE_PLACE:
                PetriNetController.addPetriNetElement();
                break;
            default:
                System.out.println("MOUSE PRESSSSSS");
                break;
        }



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