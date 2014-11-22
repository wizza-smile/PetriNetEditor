package controller;


import model.*;
import view.*;
import view.figures.*;

import java.util.*;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;


public class PetriNetController {

    public final static int ELEMENT_PLACE = 0;
    public final static int ELEMENT_TRANSITION = 1;
    public final static int ELEMENT_ARC = 2;


    static private PetriNet petriNet;



    public static Integer getPetriNetElementCount() {
        return petriNet.getElementCount();
    }
    public static void createPetriNet() {
        petriNet = new PetriNet();
    }


    public static PetriNet getPetriNet() {
        return petriNet;
    }


    public static PetriNetElement getElementById(String elementId) {
        return petriNet.getElementById(elementId);
    }


    public static ArrayList<String> getAllElementIds() {
        ArrayList<String> allElementIds = new ArrayList<String>();
        allElementIds.addAll(petriNet.place_ids);
        allElementIds.addAll(petriNet.transition_ids);
        allElementIds.addAll(petriNet.getArcIds());

        return allElementIds;
    }

    public static ArrayList<String> getConnectablesIds() {
        ArrayList<String> allElementIds = new ArrayList<String>();
        allElementIds.addAll(petriNet.place_ids);
        allElementIds.addAll(petriNet.transition_ids);

        return allElementIds;
    }



    public static void newConnectableElementAtPosition(Point2D position, int type) {
        Integer next_element_id = petriNet.getNextElementId();
        String elementId;
        PetriNetElement element;

        switch (type) {
            case PetriNetController.ELEMENT_PLACE:
                new Place(position);
                break;
            case PetriNetController.ELEMENT_TRANSITION:
                new Transition(position);
                break;
            default:
                return;
        }
    }


    public static void addElement(PetriNetElement element, int type) {
        petriNet.addElement(element.getId(), element);

        switch (type) {
            case PetriNetController.ELEMENT_PLACE:
                petriNet.place_ids.add(element.getId());
                break;
            case PetriNetController.ELEMENT_TRANSITION:
                petriNet.transition_ids.add(element.getId());
                break;
            case PetriNetController.ELEMENT_ARC:
                petriNet.arc_ids.add(element.getId());
                break;
            default:
                return;
        }
    }


    public static void removeArc(String arc_id) {
        petriNet.removeElement(arc_id);
        petriNet.removeArcId(arc_id);
    }


    public static void removeTransition(String transition_id) {
        petriNet.removeElement(transition_id);
        petriNet.removeTransitionId(transition_id);
    }

    public static void removePlace(String place_id) {
        petriNet.removeElement(place_id);
        petriNet.removePlaceId(place_id);
    }




    public static void moveAllElements(Double x, Double y) {

        for (String elem_id : PetriNetController.getConnectablesIds()) {
            Connectable elem = (Connectable)PetriNetController.getElementById(elem_id);
            Point2D position = elem.getPosition();
            Point2D new_position = new Point2D.Double(position.getX()+x, position.getY()+y);
            elem.setPosition(new_position);
        }

        /* update grid reference point, so that the illusion of stable grid is kept */
        CanvasController.addToGridReferencePoint(new Point2D.Double(x, y));
    }



}