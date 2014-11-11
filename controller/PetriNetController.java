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
            case PetriNetController.ELEMENT_TRANSITION:
                elementId = "t_" + next_element_id.toString();
                element = new Transition(elementId, position);
                break;
            default:
                return;
        }

        petriNet.addElement(elementId, element);
    }



    public static PetriNetElement getElementById(String elementId) {
        return petriNet.getElementById(elementId);
    }


    public static Rectangle getPetriNetRectangle() {
        Double width, height;
        Double upper_left_x = .0;
        Double upper_left_y = .0;

        Point2D lower_right = new Point2D.Double(0, 0);

        boolean initialized = false;
        Iterator it = petriNet.getElements().values().iterator();
        while (it.hasNext()) {
            PetriNetElement elem = (PetriNetElement)it.next();
            Point2D position = elem.getPosition();

            if (initialized) {
                upper_left_x = Math.min(upper_left_x, position.getX());
                upper_left_y = Math.min(upper_left_y, position.getY());
            } else {
                upper_left_x = position.getX();
                upper_left_y = position.getY();
                initialized = true;
            }
            lower_right.setLocation(Math.max(lower_right.getX(), position.getX()), Math.max(lower_right.getY(), position.getY()));
        }

        //ADD PADDING
        upper_left_x = upper_left_x - PETRINET_PADDING;
        upper_left_y = upper_left_y - PETRINET_PADDING;
        lower_right.setLocation(lower_right.getX() + PETRINET_PADDING, lower_right.getY() + PETRINET_PADDING);

        width = lower_right.getX() - upper_left_x;
        height = lower_right.getY() - upper_left_y;

        return new Rectangle(upper_left_x.intValue(), upper_left_y.intValue(), width.intValue(), height.intValue());
    }



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