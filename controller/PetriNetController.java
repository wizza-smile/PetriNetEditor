package controller;


import model.*;
import view.*;

import java.util.*;

import java.awt.geom.Point2D;
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




}