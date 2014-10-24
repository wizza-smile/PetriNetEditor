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
            case  GlobalController.MODE_ARROW:
                System.out.println("MODE_ARROW");
                break;
            default:
                System.out.println("MOUSE PRESSSSSS");
                break;
        }



        canvas.repaint();


    }



}