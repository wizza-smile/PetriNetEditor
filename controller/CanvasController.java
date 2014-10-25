package controller;

import model.*;
import view.*;
import view.figures.*;


import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.*;

public class CanvasController {

    static Canvas canvas;
    static Point2D mousePressOrigin;


    public static Canvas createCanvas() {
        canvas = new Canvas();

        return canvas;
    }


    public static void computeAndSetCanvasSize() {
        canvas.setCanvasSize();
    }


    public static void mousePressed(MouseEvent e) {

        mousePressOrigin = new Point2D.Double(e.getX(), e.getY());

        //BaseFigure figure = canvas.selectFigure(e.getPoint());

        switch (GlobalController.mode) {
            case  GlobalController.MODE_SELECT:
                System.out.println("MODE_SELECT");
                break;
            case  GlobalController.MODE_PLACE:
                System.out.println("MODE_PLACE");
                PetriNetController.addPetriNetElement(mousePressOrigin, PetriNetController.ELEMENT_PLACE);
                break;
            default:
                System.out.println("MOUSE PRESSSSSS");
                break;
        }

        canvas.repaint();
    }


    public static void mouseDragged(MouseEvent e) {
        MainWindowController.setStatusBarText("mouseDragged " + e.getY() + "/" + e.getX() + " origin " + mousePressOrigin.getY() + "/" + mousePressOrigin.getX());

        switch (GlobalController.mode) {
            case  GlobalController.MODE_SELECT:
                // PetriNetController.addPetriNetElement();
                break;
            default:
                // MainWindowController.setStatusBarText("MOUSE DRAGGED");
                break;
        }

        canvas.repaint();
    }



}