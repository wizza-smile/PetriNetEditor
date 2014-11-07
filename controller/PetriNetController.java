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

    public final static int PETRINET_PADDING = 40;

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


    public static void checkLowerRightCorner(Point2D p) {
        petriNet.lowerRightCorner.setLocation(
            Math.max(petriNet.lowerRightCorner.getX(), p.getX()),
            Math.max(petriNet.lowerRightCorner.getY(), p.getY())
        );

    }



    public static void computePetriNetUpperLeftAndLowerRightCorner() {
        Point2D upper_left = new Point2D.Double(0, 0);
        Point2D lower_right = new Point2D.Double(0, 0);
        boolean initialized = false;
        Iterator it = petriNet.getElements().values().iterator();
        while (it.hasNext()) {
            PetriNetElement elem = (PetriNetElement)it.next();
            Point2D position = elem.getPosition();

            if (initialized) {
                upper_left.setLocation(Math.min(upper_left.getX(), position.getX()), Math.min(upper_left.getY(), position.getY()));
                lower_right.setLocation(Math.max(lower_right.getX(), position.getX()), Math.max(lower_right.getY(), position.getY()));
            } else {
                upper_left = new Point2D.Double(position.getX(), position.getY());
                lower_right = new Point2D.Double(position.getX(), position.getY());
                initialized = true;
            }
        }
        //ADD PADDING
        upper_left.setLocation(upper_left.getX() - PETRINET_PADDING, upper_left.getY() - PETRINET_PADDING);
        lower_right.setLocation(lower_right.getX() + PETRINET_PADDING, lower_right.getY() + PETRINET_PADDING);

        petriNet.upper_left = upper_left;
        petriNet.lower_right = lower_right;



        petriNet.netDimension.setSize(lower_right.getX() - upper_left.getX(), lower_right.getY() - upper_left.getY());
    }


    // public static void fixPetriNetElementPositions() {

    //     Double fix_x = PetriNetController.getPetriNet().upper_left.getX() < 0 ? PetriNetController.getPetriNet().upper_left.getX() : 0;
    //     Double fix_y = PetriNetController.getPetriNet().upper_left.getY() < 0 ? PetriNetController.getPetriNet().upper_left.getY() : 0;

    //     if (fix_x < 0 || fix_y < 0) {
    //         // moveAllElementDownDiagonally(fix_x, fix_y);



    //         Point2D position = MainWindowController.getViewport().getViewPosition();

    //         // System.out.println(x_off);

    //         Double x_off = (-1) * fix_x;
    //         Double y_off = (-1) * fix_y;


    //         // MainWindowController.getViewport().setViewPosition( new Point(x_off.intValue(), y_off.intValue()) );

    //         System.out.println("SETET VIEWPORT POS");
    //         System.out.println(MainWindowController.getViewport().getViewPosition().getX());

    //     }

    // }

    public static void moveAllElements(Double x, Double y) {
        Iterator it = petriNet.getElements().values().iterator();
        while (it.hasNext()) {
            PetriNetElement elem = (PetriNetElement)it.next();
            Point2D position = elem.getPosition();
            Point2D new_position = new Point2D.Double(position.getX()+x, position.getY()+y);
            elem.setPosition(new_position);
        }
        /* update grid reference point, so that the illusion of stable grid is kept */
        CanvasController.addToGridReferencePoint(new Point2D.Double(x, y));
    }



}