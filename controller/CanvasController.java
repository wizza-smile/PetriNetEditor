package controller;

import model.*;
import view.*;
import view.figures.*;

import java.lang.Math;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;


import javax.swing.*;


public class CanvasController {

    static view.Canvas canvas;
    static Point2D mousePressPoint;
    static Point2D currentMousePoint;


    public static view.Canvas createCanvas() {
        canvas = new view.Canvas();

        return canvas;
    }


    public static void computeAndSetCanvasSize() {
        canvas.setCanvasSize(new Dimension(800, 800));
    }


    public static void mousePressed(MouseEvent e) {
        mousePressPoint = new Point2D.Double(e.getX(), e.getY());

        switch (GlobalController.mode) {
            case GlobalController.MODE_SELECT:
                System.out.println("MODE_SELECT");
                break;
            case GlobalController.MODE_PLACE:
                System.out.println("MODE_PLACE");
                PetriNetController.addPetriNetElement(mousePressPoint, PetriNetController.ELEMENT_PLACE);
                break;
            default:
                System.out.println("MOUSE PRESSSSSS");
                break;
        }

        canvas.repaint();
    }


    public static void mouseDragged(MouseEvent e) {
        currentMousePoint = new Point2D.Double(e.getX(), e.getY());

        switch (GlobalController.mode) {
            case  GlobalController.MODE_SELECT:
                SelectionController.updateSelection();
                break;
            default:
                // MainWindowController.setStatusBarText("MOUSE DRAGGED");
                break;
        }

        canvas.repaint();
    }


    public static void mouseReleased(MouseEvent e) {
        currentMousePoint = new Point2D.Double(e.getX(), e.getY());

        switch (GlobalController.mode) {
            case GlobalController.MODE_SELECT:
                SelectionController.updateSelection();
                break;
            case GlobalController.MODE_PLACE:
                //
                break;
            default:
                System.out.println("MOUSE mouseReleased");
                break;
        }

        canvas.repaint();
    }


}