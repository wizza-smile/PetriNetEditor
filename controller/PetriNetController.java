package controller;


import model.*;
import view.*;

import java.util.*;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;


public class PetriNetController {

    public final static int ELEMENT_PLACE = 0;
    public final static int ELEMENT_TRANSITION = 1;
    public final static int ELEMENT_ARC = 2;

    static private PetriNet petriNet;


    public static void createPetriNet() {
        petriNet = new PetriNet();
    }

    public static PetriNet getPetriNet() {
        return petriNet;
    }

    public static void addPetriNetElement(Point2D position, int type) {
        Integer next_element_id = petriNet.getNextElementId();
        String elementId;
        PetriNetElement element;

        switch (type) {
            case PetriNetController.ELEMENT_PLACE:
                elementId = "p_" + next_element_id.toString();
                element = new Place(elementId, position);
                break;
            default:
                return;
        }

        petriNet.addElement(elementId, element);
    }



    public static PetriNetElement getElementById(String elementId) {
        return petriNet.getElementById(elementId);
    }



    public static void moveAllElementDownDiagonally(Double x_off, Double y_off) {
        Iterator it = PetriNetController.getPetriNet().getElements().values().iterator();
        while (it.hasNext()) {
            PetriNetElement elem = (PetriNetElement)it.next();
            Point2D position = elem.getPosition();
            Point2D new_position = new Point2D.Double(position.getX()+x_off, position.getY()+y_off);
            elem.setPosition(new_position);
            CanvasController.setGridReferencePoint(new Point2D.Double(x_off, y_off));
        }

        //adjust ScrollPosition of Viewport
        JScrollPane scrollPane = MainWindowController.main_window.canvasPane;
        Point2D position = scrollPane.getViewport().getViewPosition();

        x_off = x_off + position.getX();
        y_off = y_off + position.getY();

        scrollPane.getViewport().setViewPosition( new Point(x_off.intValue(), y_off.intValue()) );
    }







}